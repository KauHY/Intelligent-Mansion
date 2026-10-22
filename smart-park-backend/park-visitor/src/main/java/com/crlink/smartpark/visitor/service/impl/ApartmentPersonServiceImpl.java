package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.device.feign.DeviceInfoProvider;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.dto.house.HouseQueryDTO;
import com.crlink.smartpark.system.dto.house.HouseVO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.feign.HouseProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.crlink.smartpark.visitor.dto.ApartmentPersonDTO;
import com.crlink.smartpark.visitor.entity.ApartmentPerson;
import com.crlink.smartpark.visitor.entity.ApartmentPersonHouseRelation;
import com.crlink.smartpark.visitor.mapper.ApartmentPersonMapper;
import com.crlink.smartpark.visitor.model.dto.ApartmentPersonHouseDTO;
import com.crlink.smartpark.visitor.model.dto.ApartmentPersonReqDTO;
import com.crlink.smartpark.visitor.model.dto.UpdateApartmentPersonReqDTO;
import com.crlink.smartpark.visitor.model.excel.ApartmentPersonExportVO;
import com.crlink.smartpark.visitor.model.excel.ApartmentPersonImportDTO;
import com.crlink.smartpark.visitor.model.query.ApartmentPersonQuery;
import com.crlink.smartpark.visitor.model.vo.ApartmentPersonVO;
import com.crlink.smartpark.visitor.service.ApartmentPersonHouseRelationService;
import com.crlink.smartpark.visitor.service.ApartmentPersonService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


/**
 * 公寓人员管理 服务实现类
 * */
@Slf4j
@Service
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class ApartmentPersonServiceImpl extends ServiceImpl<ApartmentPersonMapper, ApartmentPerson> implements ApartmentPersonService {

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private HouseProvider houseProvider;

    @Autowired
    private DeviceInfoProvider deviceInfoProvider;

    @Resource
    private DeviceOperateProvider deviceOperateProvider;

    @Autowired
    private ApartmentPersonHouseRelationService apartmentPersonHouseRelationService;

    @Resource
    private CommonProvider commonProvider;

    @Resource
    DataSourceTransactionManager dataSourceTransactionManager;
    @Resource
    TransactionDefinition transactionDefinition;

    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<ApartmentPerson, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(ApartmentPerson::getId, sValue)
                .in(ApartmentPerson::getId, ids).list().stream()
                .collect(HashMap::new, (map, apartmentPerson) -> map.put(apartmentPerson.getId(), sValue.apply(apartmentPerson)), HashMap::putAll);
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateApartmentPerson(ApartmentPersonReqDTO reqDTO) {
        //身份证号不为空时检验格式
        boolean result = true;
        String idCardNumber = reqDTO.getIdCardNumber();
        String id = reqDTO.getId();
        if (StrUtil.isNotBlank(idCardNumber) && !RegexUtils.isIDCard18(idCardNumber)) {
            throw new ResolinkRuntimeException("900001");
        }
        checkEmployeeRelation(id, reqDTO.getEmployeeId());
        //手动开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            //编辑时删除关系表的数据
            if (StrUtil.isNotBlank(id)) {
                apartmentPersonHouseRelationService.remove(Wrappers.<ApartmentPersonHouseRelation>lambdaQuery()
                        .eq(ApartmentPersonHouseRelation::getApartmentPersonId, id));
            }
            ApartmentPerson entity = MyBeanUtils.copy(reqDTO, ApartmentPerson::new);
            EntityUserUtil.setAllDynamic(entity);
            entity.setExpire(reqDTO.getEffectEndDate().isBefore(LocalDate.now()) ? DictUtil.getValue(DictConstant.YesOrNo.YES) : DictUtil.getValue(DictConstant.YesOrNo.NO));
            result = this.saveOrUpdate(entity);
            //关系表新增数据
            result = apartmentPersonHouseRelationService.saveBatch(setRelationDataList(entity.getId(), reqDTO.getHouseIds(), entity.getEmployeeId()));
        } catch (Exception e) {
            log.error("新增或编辑公寓人员异常！data：{}", reqDTO);
            //手动回滚事务
            dataSourceTransactionManager.rollback(transactionStatus);
        } finally {
            //手动提交事务
            dataSourceTransactionManager.commit(transactionStatus);
        }
        //只对新增操作会做授权
        if (StrUtil.isBlank(reqDTO.getId())) {
            //刷新门禁授权
            log.info("公寓人员新增时刷新门禁授权,employeeId:{}", reqDTO.getEmployeeId());
            deviceOperateProvider.refreshEmployeeDoorAccessAuthorities(reqDTO.getEmployeeId());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            //刷新智能锁授权
            log.info("公寓人员新增时刷新智能锁授权,employeeId:{}", reqDTO.getEmployeeId());
            deviceOperateProvider.refreshSmartLockAuth(reqDTO.getEmployeeId(), false);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removePersonById(String id) {
        ApartmentPerson person = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        apartmentPersonHouseRelationService.remove(Wrappers.<ApartmentPersonHouseRelation>lambdaQuery().eq(ApartmentPersonHouseRelation::getApartmentPersonId, id));
        boolean flag = this.removeById(id);
        //if (flag) {
        //    //刷新门禁授权
        //    log.info("公寓人员删除门禁授权,employeeId:{}", person.getEmployeeId());
        //    deviceOperateProvider.refreshEmployeeDoorAccessAuthorities(person.getEmployeeId());
        //    //刷新智能锁授权
        //    log.info("公寓人员删除门禁授权,employeeId:{}", person.getEmployeeId());
        //    deviceOperateProvider.refreshSmartLockAuth(person.getEmployeeId(), true);
        //}
        return flag;
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public ApartmentPersonDTO getApartmentPersonById(String id) {
        ApartmentPerson entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        ApartmentPersonDTO result = MyBeanUtils.copy(entity, ApartmentPersonDTO::new);
        Map<String, Set<String>> map = apartmentPersonHouseRelationService.personId2HouseIds(Sets.newHashSet(id));
        Set<String> houseIds = map.get(id);
        Map<String, ApartmentPersonHouseDTO> houseNameMap = getHouseNameAndFunction(houseIds);
        //设置houseName：楼栋名+楼层房号
        List<String> houseNameList = Lists.newLinkedList();
        houseIds.forEach(t -> {
            ApartmentPersonHouseDTO houseDTO = houseNameMap.get(t);
            if (Objects.nonNull(houseDTO)) {
                houseNameList.add(houseDTO.getHouseName());
            }
        });
        result.setHouseIds(houseIds);
        result.setHouseNames(houseNameList);
        result.setDeviceNames(getDeviceNames(entity.getDeviceIds()));
        return result;
    }


    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<ApartmentPersonVO> listApartmentPersonPage(ApartmentPersonQuery query) {
        LambdaQueryWrapper<ApartmentPerson> conditionWrapper = getConditionWrapper(query);
        query.startPage();
        List<ApartmentPerson> list = list(conditionWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<ApartmentPersonVO> result = convert(list, false);
        return query.createPageInfo(result);
    }

    /**
     * 启用/禁用
     *
     * @param statusReqDTO
     * @return
     */
    @Override
    public Boolean updateStatus(UpdateApartmentPersonReqDTO statusReqDTO) {
        String id = statusReqDTO.getId();
        ApartmentPerson person = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
//        Integer status = statusReqDTO.getStatus().equals(DISABLE.code) ? ENABLE.code : DISABLE.code;
        ApartmentPerson entity = MyBeanUtils.copy(person, ApartmentPerson::new);
        entity.setId(id);
        entity.setStatus(statusReqDTO.getStatus());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        boolean result = this.updateById(entity);
        //刷新门禁授权
        log.info("公寓人员刷新门禁授权,employeeId:{}", person.getEmployeeId());
        deviceOperateProvider.refreshEmployeeDoorAccessAuthorities(person.getEmployeeId());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        //刷新智能锁授权
        String msg = null;
        log.info("公寓人员刷新智能锁授权,employeeId:{}", person.getEmployeeId());
        Future<String> future = ThreadUtil.execAsync(() ->
                deviceOperateProvider.refreshSmartLockAuth(person.getEmployeeId(), statusReqDTO.getStatus().equals(StatusEnum.DISABLE.getCode()))
        );
        try {
            msg = future.get();
        } catch (Exception e) {
            log.error("公寓人员锁授权异常", e);
        }
        if (Objects.nonNull(msg)) {
            throw new ResolinkRuntimeException(Message.code("60010", msg));
        }
        return result;
    }

    @Override
    public void exportPerson(ApartmentPersonQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)),
                sourceList -> MyBeanUtils.copyList(this.convert(sourceList, true), ApartmentPersonExportVO::new)
                , "公寓人员管理", ApartmentPersonExportVO.class);
    }

    @Override
    public FileImportErrorInfoDTO importPerson(MultipartFile file, String parkOrgId, String companyId) {
        String filename = file.getOriginalFilename();
        if (filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv")) {
            ExcelImportRollbackListener<ApartmentPersonImportDTO> importListener = new ExcelImportRollbackListener<>(ApartmentPersonImportDTO.class,
                    //业务参数校验方法,并单行保存数据
//                    t -> validAndOperateExcelDTO(t, parkOrgId, companyId),
                    //获取本对象的代理对象，再进行调用;获取到xxxService的代理类，再调用事务方法，强行经过代理类，激活事务切面。
                    t -> ((ApartmentPersonServiceImpl) AopContext.currentProxy()).validAndOperateExcelDTO(t, parkOrgId, companyId),
                    null
            );
            ExcelImportUtils.importExcel(file, ApartmentPersonImportDTO.class, importListener);
            return importListener.getImportResult();
        }
        throw new ResolinkRuntimeException("10014");
    }

    @Transactional(rollbackFor = Exception.class)
    public String validAndOperateExcelDTO(ApartmentPersonImportDTO dto, String parkOrgId, String companyId) {
        dto.setParkOrgId(parkOrgId);
        dto.setCompanyId(companyId);
        //当姓名或手机号与所选园区企业员工不符时，失败原因：姓名或手机号与所选园区企业员工不符
        EmployeeVO employeeVO = employeeProvider.findEmployeeByCompanyAndTel(dto.getCompanyId(), dto.getMobile());
        if (Objects.isNull(employeeVO)) {
            return "员工不存在！";
        } else if (!dto.getEmployeeName().equals(employeeVO.getEmployeeName())) {
            return "姓名或手机号与所选园区企业员工不符！";
        }
        //当手机号、姓名已在公寓人员表中，失败原因：此人员已存在
        ApartmentPerson apartmentPerson = this.lambdaQuery().select(ApartmentPerson::getId)
                .eq(ApartmentPerson::getEmployeeId, employeeVO.getId()).last("limit 1").one();
        if (Objects.nonNull(apartmentPerson)) {
            return "此人员已存在";
        }
        //校验楼栋
        String buildingId = Optional.ofNullable(dto.getBuildingName())
                .map(t -> commonProvider.getIdsByNamesAndType(Sets.newHashSet(t), SimpleNode.TypeEnum.BUILDING.getCode(), dto.getParkOrgId()).get(t))
                .orElse(null);
        if (Objects.isNull(buildingId)) {
            return "该楼栋不存在";
        }
        Map<String, String> houseIdsByNames = houseProvider.getHouseIdsByNames(Sets.newHashSet(dto.getHouseName()), buildingId, dto.getParkOrgId());
        if (CollUtil.isEmpty(houseIdsByNames)) {
            return "房屋不存在";
        }
        Set<String> deviceNames = dto.getDeviceNames().parallelStream().collect(Collectors.toSet());
        Map<String, String> deviceNameMaps = deviceInfoProvider.getIdByDeviceNames(deviceNames,
                DictConstant.DeviceType.APARTMENT_ACCESS_CONTROL.type, dto.getParkOrgId());
        if (CollUtil.isEmpty(deviceNameMaps) || !Objects.equals(deviceNames.size(), deviceNameMaps.size())) {
            return "设备不存在";
        }
        ApartmentPerson entity = MyBeanUtils.copy(dto, ApartmentPerson::new);
        entity.setEmployeeId(employeeVO.getId());
        entity.setDeviceIds(Sets.newHashSet(deviceNameMaps.values()));
        entity.setStatus(StatusEnum.DISABLE.getCode());
        EntityUserUtil.setAll(entity);
        entity.setExpire(dto.getEffectEndDate().isBefore(LocalDate.now()) ? DictUtil.getValue(DictConstant.YesOrNo.YES) : DictUtil.getValue(DictConstant.YesOrNo.NO));
        this.save(entity);
        //关系表新增数据
        List<ApartmentPersonHouseRelation> relations = setRelationDataList(entity.getId(), Sets.newHashSet(houseIdsByNames.values()), entity.getEmployeeId());
//        apartmentPersonHouseRelationService.saveBatch(relations);
        //刷新门禁授权
//        return deviceOperateProvider.refreshEmployeeDoorAccessAuthorities(entity.getEmployeeId()) ? null : "插入数据失败！";
        return apartmentPersonHouseRelationService.saveBatch(relations) ? null : "插入数据失败！";
    }


    @Override
    public ApartmentPersonDTO findByEmployeeId(String employeeId) {
        ApartmentPerson apartmentPerson = lambdaQuery()
                .select(ApartmentPerson::getId, ApartmentPerson::getDeviceIds, ApartmentPerson::getStatus)
                //.eq(ApartmentPerson::getStatus, StatusEnum.ENABLE.getCode())
                .eq(ApartmentPerson::getEmployeeId, employeeId)
                .eq(ApartmentPerson::getExpire, DictUtil.getValue(DictConstant.YesOrNo.NO))
                .one();
        if (Objects.isNull(apartmentPerson)) {
            return null;
        }
        ApartmentPersonDTO personDTO = new ApartmentPersonDTO();
        BeanUtils.copyProperties(apartmentPerson, personDTO);
        return personDTO;
    }

    @Override
    public String existNormalPersonByEmployeeId(String employeeId) {
        ApartmentPerson apartmentPerson = lambdaQuery()
                .select(ApartmentPerson::getId, ApartmentPerson::getEmployeeId)
                .eq(ApartmentPerson::getStatus, StatusEnum.ENABLE.getCode())
                .eq(ApartmentPerson::getEmployeeId, employeeId)
                .eq(ApartmentPerson::getExpire, DictUtil.getValue(DictConstant.YesOrNo.NO))
                .last("limit 1").one();
        if (Objects.nonNull(apartmentPerson)) {
            return apartmentPerson.getEmployeeId();
        }
        return null;
    }

    @Override
    public Set<String> findHouseIdsByEmployeeId(String employeeId) {
        ApartmentPerson person = lambdaQuery().select(ApartmentPerson::getId)
//                .eq(ApartmentPerson::getStatus, StatusEnum.ENABLE.getCode())
                .eq(ApartmentPerson::getEmployeeId, employeeId)
                .eq(ApartmentPerson::getExpire, DictUtil.getValue(DictConstant.YesOrNo.NO))
                .one();
        if (Objects.isNull(person)) {
            return Collections.emptySet();
        }
        Map<String, Set<String>> map = apartmentPersonHouseRelationService
                .personId2HouseIds(Sets.newHashSet(person.getId()));
        return map.get(person.getId());
    }

    @Override
    public boolean existsDevice(String deviceId) {
        return this.count(Wrappers.<ApartmentPerson>lambdaQuery().apply("FIND_IN_SET({0},device_ids)", deviceId)) > 0;
    }

    private void checkEmployeeRelation(String id, String employeeId) {
        ApartmentPerson one = this.lambdaQuery().select(ApartmentPerson::getId)
                .eq(ApartmentPerson::getEmployeeId, employeeId)
                .ne(StringUtils.isNotBlank(id), ApartmentPerson::getId, id).last("limit 1").one();
        if (Objects.nonNull(one)) {
            throw new ResolinkRuntimeException("900002");
        }
    }

    private List<ApartmentPersonHouseRelation> setRelationDataList(String personId, Set<String> houseIds, String employeeId) {
        return houseIds.stream().map(t -> {
            ApartmentPersonHouseRelation relation = new ApartmentPersonHouseRelation();
            relation.setApartmentPersonId(personId);
            relation.setEmployeeId(employeeId);
            relation.setHouseId(t);
            return relation;
        }).collect(Collectors.toList());
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ApartmentPerson> getConditionWrapper(ApartmentPersonQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getParkOrgId());
        String houseName = query.getHouseName();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        String name = query.getName();
        LambdaQueryWrapper<ApartmentPerson> queryWrapper = new LambdaQueryWrapper<ApartmentPerson>();
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), ApartmentPerson::getParkOrgId, query.getParkOrgId())
                .eq(StringUtils.isNotBlank(query.getCompanyId()), ApartmentPerson::getCompanyId, query.getCompanyId())
                .eq(Objects.nonNull(query.getStatus()), ApartmentPerson::getStatus, query.getStatus())
                .in(ApartmentPerson::getParkOrgId, parkOrgIds)
                .orderByDesc(ApartmentPerson::getCreateTime);
        //按姓名模糊查询员工
        if (StringUtils.isNotBlank(name)) {
            //跨服务查询符合的员工Id
            Map<String, Map<String, Object>> employeeColumns =
                    employeeProvider.queryEmployeeColumns(EmployeeQueryDTO.likeNameBuildDTO(name));
            Set<String> employeeIds = employeeColumns.keySet();
            MyPageInfoUtils.isEmptyPage(employeeIds);
            queryWrapper.in(ApartmentPerson::getEmployeeId, employeeIds);
        }
        //根据房号查询
        if (StringUtils.isNotBlank(houseName)){
            //1、首先模糊查询出
            HouseQueryDTO houseQueryDTO = new HouseQueryDTO();
            String[] queryColumn = new String[]{"name"};
            houseQueryDTO.setName(houseName);
            houseQueryDTO.setColumns(queryColumn);
            Map<String, Map<String, Object>> houseColumns = houseProvider.queryHouseColumns(houseQueryDTO);
            MyPageInfoUtils.isEmptyPage(houseColumns);
            Set<String> allHouseId = new HashSet<>();
            houseColumns.forEach((k, v) -> {
                String houseId = (String) v.get("id");
                allHouseId.add(houseId);
            });
            Map<String, Set<String>> map = apartmentPersonHouseRelationService.houseId2EmployeeIds(allHouseId);
            MyPageInfoUtils.isEmptyPage(map);
            Set<String> allEmployeeIds = new HashSet<>();
            map.forEach((k,v)-> allEmployeeIds.addAll(v));
            queryWrapper.in(ApartmentPerson::getEmployeeId,allEmployeeIds);
        }
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ApartmentPersonVO> convert(List<ApartmentPerson> list, Boolean fromExport) {
        Set<String> employeeIds = Sets.newHashSet();
        Set<String> houseIds = Sets.newHashSet();
        Set<String> personIds = Sets.newHashSet();
        //收集员工id,房屋id
        list.forEach(t -> {
            employeeIds.add(t.getEmployeeId());
            personIds.add(t.getId());
        });
        Map<String, Set<String>> personId2HouseIdsMap = apartmentPersonHouseRelationService.personId2HouseIds(personIds);
        //多个集合的非重复并集得到所有房屋id
        personId2HouseIdsMap.forEach((k, v) -> houseIds.addAll(CollectionUtil.unionDistinct(houseIds, v)));
        //查询员工相关信息
        Map<String, EmployeeDTO> employeeInfoMap = getEmployeeInfo(employeeIds);
        //查询房屋相关信息
        Map<String, ApartmentPersonHouseDTO> houseNameAndFunction = getHouseNameAndFunction(houseIds);
        List<ApartmentPersonVO> result = Lists.newLinkedList();
        list.forEach(t -> {
            ApartmentPersonVO vo = MyBeanUtils.copy(t, ApartmentPersonVO::new);
            EmployeeDTO employeeDTO = employeeInfoMap.get(t.getEmployeeId());
            if (Objects.nonNull(employeeDTO)) {
                vo.setEmployeeName(employeeDTO.getName());
                vo.setMobile(employeeDTO.getMobile());
                vo.setPics(employeeDTO.getPics());
            }
            vo.setCreateDate(t.getCreateTime().toLocalDate());
            Set<String> currentHouseIds = personId2HouseIdsMap.get(t.getId());
            if (fromExport) {
                //导出时循环设置多次
                currentHouseIds.forEach(houseId -> {
                    ApartmentPersonHouseDTO houseDTO = houseNameAndFunction.get(houseId);
                    if (Objects.nonNull(houseDTO)) {
                        ApartmentPersonVO newVO = MyBeanUtils.copy(vo, ApartmentPersonVO::new);
                        newVO.setHouseName(houseDTO.getHouseName());
                        newVO.setHouseFunction(houseDTO.getHouseFunction());
                        result.add(newVO);
                    }
                });
            } else {
                //正常分页查询
                vo.setHouseList(completeHouseList(currentHouseIds, houseNameAndFunction));
                result.add(vo);
            }

        });
        return result;
    }


    /**
     * 查询员工对应的名称、手机号、照片
     *
     * @param employeeIds
     * @return
     */
    private Map<String, EmployeeDTO> getEmployeeInfo(Set<String> employeeIds) {
        String[] queryColumn = new String[]{"name", "mobile", "pics"};
        EmployeeQueryDTO employeeQueryDTO = new EmployeeQueryDTO();
        employeeQueryDTO.setEmployeeIds(employeeIds);
        employeeQueryDTO.setColumns(queryColumn);
        Map<String, EmployeeDTO> employeeId2Map = employeeProvider.queryEmployeeByCondition(employeeQueryDTO);
        return employeeId2Map;
    }

    /**
     * 获取门禁设备名称
     *
     * @param deviceIds
     * @return
     */
    private List<String> getDeviceNames(Set<String> deviceIds) {
        Map<String, String> deviceId2Names = deviceInfoProvider.getNamesByIds(deviceIds);
        return deviceId2Names.values().parallelStream().collect(Collectors.toList());
    }


    /**
     * 查询房屋对应信息
     *
     * @param houseIds
     */
    private Map<String, ApartmentPersonHouseDTO> getHouseNameAndFunction(Collection<String> houseIds) {
        Map<String, ApartmentPersonHouseDTO> result = Maps.newHashMap();
        // 远程调用内部服务，查询对应房屋信息
        Map<String, HouseVO> houseId2map = houseProvider.getHouseDetailByHouseId(houseIds);
        houseId2map.forEach((k, v) -> {
            ApartmentPersonHouseDTO dto = new ApartmentPersonHouseDTO();
            dto.setHouseId(k);
            dto.setHouseFunction(v.getHouseFunction());
            dto.setHouseName(v.getBuildingName() + v.getName());
            result.put(k, dto);
        });
        return result;
    }

    /**
     * 列表回显房屋相关信息
     *
     * @param houseIds
     * @param map
     * @return
     */
    private List<ApartmentPersonHouseDTO> completeHouseList(Set<String> houseIds, Map<String, ApartmentPersonHouseDTO> map) {
        List<ApartmentPersonHouseDTO> result = Lists.newLinkedList();
        houseIds.forEach(houseId -> {
            result.add(map.get(houseId));
        });
        return result;
    }
}
