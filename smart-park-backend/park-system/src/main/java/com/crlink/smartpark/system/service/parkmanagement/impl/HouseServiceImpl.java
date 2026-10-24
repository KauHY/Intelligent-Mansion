package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.business.feign.BusinessProvider;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.contract.dto.ContractHouseStatusDTO;
import com.crlink.smartpark.contract.feign.ContractProvider;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.house.*;
import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.enums.Constant;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.mapper.HouseMapper;
import com.crlink.smartpark.system.model.HouseExcelExportDTO;
import com.crlink.smartpark.system.model.HouseExcelImportDTO;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.*;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.HouseListVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.system.enums.Constant.HOUSE_EXPORT_FILENAME;
import static com.crlink.smartpark.system.enums.Constant.HousePropertyEnum.RENT;
import static com.crlink.smartpark.system.enums.HouseStatusEnum.RENTED;
import static com.crlink.smartpark.system.enums.HouseStatusEnum.RENTING;


/**
 * <p>
 * 房屋表 服务实现类
 * </p>
 * */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {


    @Autowired
    private HouseRentRecordService rentRecordService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private BusinessProvider businessProvider;

    @Autowired
    private CompanyService companyService;

    @Override
    public Map<String, String> getBuildingHouseNamesByIds(Set<String> houseIds) {
        if (CollectionUtil.isEmpty(houseIds)) {
            return Collections.emptyMap();
        }
        Map<String, String> buildingIdsMap = new HashMap<>();
        Map<String, String> houseMap = this.lambdaQuery().select(House::getId, House::getName, House::getBuildingId)
                .in(House::getId, houseIds).list().parallelStream().map(t -> {
                    buildingIdsMap.put(t.getId(), t.getBuildingId());
                    return t;
                })
                .collect(Collectors.toMap(House::getId, House::getName));
        Map<String, ?> buildingNameMap = buildingService.id2ValueMap(Sets.newHashSet(buildingIdsMap.values()), Building::getName);
        houseMap.forEach((k, v) -> {
            String buildingId = buildingIdsMap.get(k);
            String buildingName = (String) buildingNameMap.get(buildingId);
            houseMap.put(k, buildingName + v);
        });
        return houseMap;
    }

    @Override
    public Map<String, ?> id2ValueMap(Set<String> ids, SFunction<House, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> houseId2ValueMap = this.lambdaQuery()
                .select(House::getId, sValue)
                .in(House::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(House::getId, sValue));
        return houseId2ValueMap;
    }

    @Override
    public Map<String, ?> name2IdMap(Set<String> names, String buildingId, @NotNull String parkOrgId) {
        Map<String, String> name2IdMap = this.lambdaQuery()
                .select(House::getId, House::getName)
                .eq(House::getBuildingId, buildingId)
                .eq(House::getParkOrgId, parkOrgId)
                .in(ObjectUtils.isNotEmpty(names), House::getName, names)
                .list().parallelStream().collect(Collectors.toMap(House::getName, House::getId, (k1, k2) -> k2));
        return name2IdMap;
    }


    @Override
    public FileImportErrorInfoDTO importHouses(MultipartFile file, String parkOrgId) {
        String filename = file.getOriginalFilename();
        if (filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv")) {
            ExcelImportRollbackListener<HouseExcelImportDTO> importListener = new ExcelImportRollbackListener<>(HouseExcelImportDTO.class,
                    //业务参数校验方法,并单行保存数据
                    t -> validAndOperateExcelDTO(t, parkOrgId),
                    null
            );
            ExcelImportUtils.importExcel(file, HouseExcelImportDTO.class, importListener);
            return importListener.getImportResult();
        }
        throw new ResolinkRuntimeException("10014");
    }

    @Override
    public void exportHouses(HouseListDTO houseListDTO) {
        houseListDTO.setHeadOrgId(SystemUtil.getInstance().getCurrentHeadOrgId());
        houseListDTO.setParkOrgIds(SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        ExcelExportUtils.export(() -> baseMapper.listPage(houseListDTO),
                list -> MyBeanUtils.copyList(list, HouseExcelExportDTO::new),
                HOUSE_EXPORT_FILENAME, HouseExcelExportDTO.class);
    }

    @Override
    public Map<String, HouseDTO> listHouseFor3D(String parkOrgId, String buildingId, Integer floor) {
        List<House> list = this.lambdaQuery().eq(House::getParkOrgId, parkOrgId).eq(House::getBuildingId, buildingId)
                .eq(Objects.nonNull(floor), House::getFloor, floor).list();
        Set<String> houseIds = Sets.newHashSet();
        Map<String, HouseDTO> result = list.parallelStream().map(t -> {
            houseIds.add(t.getId());
            return MyBeanUtils.copy(t, HouseDTO::new);
        }).collect(Collectors.toMap(HouseDTO::getName, Function.identity()));
        Map<String, ContractHouseStatusDTO> houseRelationFor3DMap = contractProvider.getContractHouseRelationFor3D(houseIds);
        result.forEach((houseName, houseDTO) -> {
            String houseId = houseDTO.getId();
            ContractHouseStatusDTO statusDTO = houseRelationFor3DMap.get(houseId);
            if (Objects.nonNull(statusDTO)) {
                houseDTO.setHouseStatus(statusDTO.getHouseStatus());
                houseDTO.setCompanyName(statusDTO.getCompanyName());
            }
        });
        return result;
    }


    @Override
    public Map<String, Map<String, String>> buildingId2HouseIdNameMap(Set<String> buildingIds) {
        if (CollectionUtil.isEmpty(buildingIds)) {
            return Collections.emptyMap();
        }
        Map<String, Map<String, String>> buildingId2HouseIdNameMap = this.lambdaQuery().select(House::getId, House::getBuildingId, House::getName)
                .in(House::getBuildingId, buildingIds)
                .list().parallelStream()
                .collect(Collectors.groupingBy(House::getBuildingId, Collectors.toMap(House::getId, House::getName)));
        return buildingId2HouseIdNameMap;
    }

    @Override
    public Map<String, HouseDTO> houseId2House(Set<String> houseIds, Set<String> columns) {
        if (CollectionUtil.isEmpty(houseIds)) {
            return Collections.emptyMap();
        }
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        columns.add("id");
        queryWrapper.select(columns.toArray(new String[columns.size()]))
                .in("id", houseIds);
        Map<String, HouseDTO> houseId2HouseMap = this.list(queryWrapper)
                .parallelStream().map(t -> MyBeanUtils.copy(t, HouseDTO::new))
                .collect(Collectors.toMap(HouseDTO::getId, Function.identity()));
        return houseId2HouseMap;
    }

    @Override
    public Map<String, HouseVO> houseId2HouseVO(Collection<String> houseIds) {
        if (CollectionUtil.isEmpty(houseIds)) {
            return Collections.emptyMap();
        }

        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", houseIds);
        List<House> houses = this.list(queryWrapper);
        Set<String> parkOrgIds = Sets.newHashSet();
        Set<String> buildingIds = Sets.newHashSet();
        for (House house : houses) {
            parkOrgIds.add(house.getParkOrgId());
            buildingIds.add(house.getBuildingId());
        }
        Map<String, Building> id2BuildingNameMap = (Map<String, Building>) buildingService.id2ValueMap(buildingIds, null);
        Map<String, String> id2OrgNameMap = (Map<String, String>) organizationService.id2ValueMap(parkOrgIds, Organization::getOrgName);
        Map<String, HouseVO> houseId2HouseVOMap = houses.parallelStream().map(t -> {
            HouseVO houseVO = MyBeanUtils.copy(t, HouseVO::new);
            Building building = id2BuildingNameMap.get(t.getBuildingId());
            houseVO.setBuildingName(building.getName());
            houseVO.setTotalFloor(building.getTotalFloor());
            houseVO.setParkOrgName(id2OrgNameMap.get(t.getParkOrgId()));
            return houseVO;
        })
                .collect(Collectors.toMap(HouseDTO::getId, Function.identity(), (k1, k2) -> k1, LinkedHashMap::new));
        return houseId2HouseVOMap;
    }

    /**
     * 分页查询
     *
     * @param houseListDTO 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<HouseListVO> listHousePage(HouseListDTO houseListDTO) {
        houseListDTO.setHeadOrgId(SystemUtil.getInstance().getCurrentHeadOrgId());
        houseListDTO.setParkOrgIds(SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        houseListDTO.startPage();
        List<HouseListVO> list = baseMapper.listPage(houseListDTO);
        PageInfo<HouseListVO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 获取某个园区所有被删的房屋id
     *
     * @param houseListDTO
     * @return
     */
    @Override
    public Set<String> getDelHouseByParkOrgId(HouseListDTO houseListDTO) {
        List<HouseListVO> list = baseMapper.getDelHouseByParkOrgId(houseListDTO);
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptySet();
        }
        return list.parallelStream().map(HouseListVO::getId).collect(Collectors.toSet());
    }

    @Override
    public HouseListVO findHouseById(String houseId) {
        HouseListDTO houseListDTO = new HouseListDTO();
        houseListDTO.setHouseId(houseId);
        houseListDTO.setHeadOrgId(SystemUtil.getInstance().getCurrentHeadOrgId());
        List<HouseListVO> list = baseMapper.listPage(houseListDTO);
        if (ObjectUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }


    @Override
    public void saveOrUpdateHouse(HouseSaveDTO houseSaveDTO) {
        checkSaveHouseParam(houseSaveDTO);
        checkHouseFloor(houseSaveDTO.getBuildingId(), houseSaveDTO.getFloor());
        House house = new House();
        BeanUtils.copyProperties(houseSaveDTO, house);
        EntityUserUtil.setAllDynamic(house);
        //根据房屋属性设置房屋状态
        Integer houseProperty = houseSaveDTO.getHouseProperty();
        Integer defaultStatus = Constant.HousePropertyEnum.getHouseDefaultStatusMap().get(houseProperty);
        house.setHouseStatus(defaultStatus);
        //if (FOR_SELF.code == houseProperty) {
        //    //房屋属性是自用，则房屋默认状态为自用
        //    house.setHouseStatus(HouseStatusEnum.FOR_SELF.getCode());
        //} else if (RENT.code == houseProperty) {
        //    //房屋属性是租赁，房屋默认状态为待租
        //    house.setHouseStatus(HouseStatusEnum.RENTING.getCode());
        //} else {
        //    //房屋属性是销售，房屋默认状态为待售
        //    house.setHouseStatus(HouseStatusEnum.SELLING.getCode());
        //}

        AssertUtil.requireTrueOrDataExp(saveOrUpdate(house));
    }

    @Override
    public void removeHouseById(String id) {
        //校验房屋是否关联过企业
        if (rentRecordService.getOne(new LambdaQueryWrapper<HouseRentRecord>().select(HouseRentRecord::getId)
                .eq(HouseRentRecord::getHouseId, id)) != null) {
            throw new ResolinkRuntimeException("20006");
        }
        AssertUtil.requireTrueOrDataExp(removeById(id));
    }

    /**
     * 22.7.13 产品要求楼层房号为纯数字，按照从小到大排序
     *
     * @param buildingId
     * @param floorNum
     * @return
     */
    @Override
    public List<DictVO> listHouseDictByBuildingId(String buildingId, Integer floorNum) {
        List<House> list = this.lambdaQuery().select(House::getId, House::getName)
                .eq(House::getBuildingId, buildingId)
                .eq(floorNum != null, House::getFloor, floorNum).last("order by name+0 ASC")
                .list();
        List<DictVO> houseDict = list.parallelStream()
//                .sorted((n1, n2) -> new PinyinComparator().compare(n1.getName(), n2.getName()))
                .map(house -> new SimpleNode(house.getId(), house.getName(), SimpleNode.TypeEnum.HOUSE.getCode())).collect(Collectors.toList());
        return houseDict;
    }

    @Override
    public HouseSaveDTO getHouseById(String id) {
        HouseSaveDTO result = new HouseSaveDTO();
        House entity = baseMapper.selectById(id);
        if (entity != null) {
            BeanUtils.copyProperties(entity, result);
        }
        return result;
    }

    @Override
    public Map<String, Map<String, Object>> queryHouseColumns(HouseQueryDTO dto) {
        HashSet<String> columns = CollectionUtil.newHashSet(dto.getColumns());
        columns.add("id");
        QueryWrapper<House> queryWrapper = Wrappers.<House>query()
                .select(columns.toArray(new String[columns.size()]))
                .eq(StringUtils.isNotBlank(dto.getParkOrgId()), "park_org_id", dto.getParkOrgId())
                .in(CollectionUtil.isNotEmpty(dto.getHouseIds()), "id", dto.getHouseIds())
                .in(CollectionUtil.isNotEmpty(dto.getBuildingIds()), "building_id", dto.getBuildingIds())
                .eq(dto.getHouseStatus() != null, "house_status", dto.getHouseStatus())
                .in(CollectionUtil.isNotEmpty(dto.getHouseFunctions()), "house_function", dto.getHouseFunctions())
                .in(CollectionUtil.isNotEmpty(dto.getHouseProperties()), "house_property", dto.getHouseProperties())
                .eq(dto.getHouseFunction() != null, "house_function", dto.getHouseFunction())
                .le(Objects.nonNull(dto.getMaxCoverArea()), "cover_area", dto.getMaxCoverArea())
                .ge(Objects.nonNull(dto.getMinCoverArea()), "cover_area", dto.getMinCoverArea())
                .like(StringUtils.isNotBlank(dto.getName()), "name", dto.getName());
        List<Map<String, Object>> listMaps = this.listMaps(queryWrapper);
        Map<String, Map<String, Object>> result = Maps.newHashMap();
        listMaps.forEach(t -> {
            String id = (String) t.get("id");
            result.put(id, t);
        });
        return result;
    }

    /**
     * 房间总数：取值于“园区管理—房屋管理”中“房屋属性”为租赁的房间总数；
     * 总面积：取值于“园区管理—房屋管理”中“房屋属性”为租赁的建筑面积总数。
     * 总商机：取值于“全部商机”模块的总商机数。
     * 1 房间：取值于“合同管理—房屋状态查询”
     * 已租率：已租房间数/“房屋属性”为租赁的房间总数。
     * 待租率：待租房间数/“房屋属性”为租赁的房间总数。
     * 2 面积：
     * 已租率：已租房间建筑面积和/“房屋属性”为租赁的房间总建筑面积和。
     * 待租率：待租房间建筑面积和/“房屋属性”为租赁的房间总建筑面积和
     * <p>
     * 入驻企业数：取值于入驻企业管理—入驻企业总数，且企业状态为“已启用”；
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public HouseRentData getHouseRentData(String parkOrgId, Boolean fromScreen) {
        Future<BigDecimal> future = ThreadUtil.execAsync(() -> companyService.getHouseUseRate());
        List<House> houseList = getHouseListByParkOrgId(parkOrgId);
        Map<Integer, Set<String>> houseIds = contractProvider.getRentedHouseId(parkOrgId);
        Set<String> rentedHouseIds = houseIds.get(RENTED.getCode());
        Set<String> rentingHouseIds = houseIds.get(RENTING.getCode());
        HouseRentData result = new HouseRentData();
        result.setTotalHouseCount(houseList.size());
        result.setRentedHouseCount(rentedHouseIds.size());
        result.setRentingHouseCount(rentingHouseIds.size());
        if (CollectionUtil.isNotEmpty(houseList)) {
            AtomicReference<Double> rentedBuildArea = new AtomicReference<>(0.0);
            AtomicReference<Double> rentingBuildArea = new AtomicReference<>(0.0);
            houseList.forEach(t -> {
                if (rentedHouseIds.contains(t.getId())) {
                    rentedBuildArea.set(rentedBuildArea.get() + t.getBuildArea());
                } else {
                    rentingBuildArea.set(rentingBuildArea.get() + t.getBuildArea());
                }
            });
            BigDecimal resultRentedBuildArea = new BigDecimal(Double.toString(rentedBuildArea.get()));
            BigDecimal resultRentingBuildArea = new BigDecimal(Double.toString(rentingBuildArea.get()));
            result.setRentedBuildArea(resultRentedBuildArea.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_DOWN));
            result.setRentingBuildArea(resultRentingBuildArea.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_DOWN));
        }
        result.setTotalBuildArea(result.getRentedBuildArea().add(result.getRentingBuildArea()));
        if (fromScreen) {
            result.setChanceCount(businessProvider.getChanceCount(parkOrgId));
            result.setCompanyCount(0);
        } else {
            Integer companyCount = companyService.lambdaQuery().select(Company::getId)
                    .eq(Company::getParkOrgId, parkOrgId)
                    .eq(Company::getStatus, StatusEnum.ENABLE.code).count();
            result.setCompanyCount(companyCount);
            result.setChanceCount(0);
            BigDecimal occupancyRate = BigDecimal.ZERO;
            try {
                occupancyRate = future.get();
            } catch (Exception e) {
                log.error("入驻率计算异常", e);
            }
            result.setOccupancyRate(occupancyRate);
        }
        return result;
    }

    private List<House> getHouseListByParkOrgId(String parkOrgId) {
        List<House> list = this.lambdaQuery().select(House::getId, House::getBuildArea,
                House::getHouseProperty, House::getHouseStatus)
                .eq(House::getHouseProperty, RENT.code)
                .eq(House::getParkOrgId, parkOrgId).list();
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    private boolean checkHouseCode(String code) {
        House one = this.lambdaQuery().select(House::getId).eq(House::getCode, code).last("limit 1").one();
        return Objects.nonNull(one);
    }

    private boolean checkHouse(HouseExcelImportDTO dto) {
        House house = this.lambdaQuery().select(House::getId).eq(House::getBuildingId, dto.getBuildingId()).eq(House::getName, dto.getName())
                .eq(House::getParkOrgId, dto.getParkOrgId()).one();
//        if (Objects.nonNull(house)) {
////            dto.setId(house.getId());
//            throw new ResolinkRuntimeException("20008");
//        }
        return Objects.nonNull(house);
    }

    /**
     * 楼层不能超出所在楼栋最高层
     *
     * @param
     */
    private void checkHouseFloor(String buildingId, Integer floor) {
        Integer totalFloor = (Integer) buildingService.id2ValueMap(Sets.newHashSet(buildingId), Building::getTotalFloor).get(buildingId);
        if (Objects.nonNull(floor) && Objects.nonNull(totalFloor)) {
            if (floor > totalFloor) {
                throw new ResolinkRuntimeException(Message.code("20020", totalFloor));
            }
        }
    }

    private void checkSaveHouseParam(HouseSaveDTO house) {
        LambdaQueryWrapper<House> wrapper = new LambdaQueryWrapper();
        int countByName = this.count(Wrappers.<House>query().lambda()
                .eq(House::getBuildingId, house.getBuildingId())
                .eq(House::getName, house.getName())
                .ne(StringUtils.isNotBlank(house.getId()), House::getId, house.getId()));
        if (countByName > 0) {
            throw new ResolinkRuntimeException("20008");
        }
        wrapper.eq(House::getCode, house.getCode());
        House existHouse = getOne(wrapper, false);
        if (existHouse != null && !existHouse.getId().equals(house.getId())) {
            throw new ResolinkRuntimeException("20005");
        }
        Organization organization = organizationService.getOne(Wrappers.<Organization>lambdaQuery().select(Organization::getId).eq(Organization::getId, house.getParkOrgId()), false);
        if (organization == null || buildingService.getById(house.getBuildingId()) == null) {
            throw new ResolinkRuntimeException("10003");
        }
    }

    /**
     * 房屋导入校验
     *
     * @param dto
     * @param parkOrgId
     * @return
     */
    private String validAndOperateExcelDTO(HouseExcelImportDTO dto, String parkOrgId) {
        dto.setParkOrgId(parkOrgId);
        //房屋编码：此为国土局法定编码，若编码已存在，则对应行将导入失败。
        if (StringUtils.isNotBlank(dto.getCode()) && checkHouseCode(dto.getCode())) {
            return "房屋编码已经存在！";
        }
        //填充楼栋
        Map<String, String> buildingNameMap = commonProvider.getIdsByNamesAndType(Sets.newHashSet(dto.getBuildingId()), SimpleNode.TypeEnum.BUILDING.getCode(), dto.getParkOrgId());
        if (ObjectUtil.isEmpty(buildingNameMap)) {
            return "该楼栋不存在";
        }
        dto.setBuildingId(buildingNameMap.get(dto.getBuildingId()));
        //v12迭代同一园区、同一楼栋下，若楼层房号名称相同，则以待导入的数据为准更新系统已有数据。
        //v13迭代同一园区、同一楼栋下，若楼层房号名称相同，则以系统已有数据为准，对应行将不做导入处理。
        if (checkHouse(dto)) {
            return "楼层房号已存在！";
        }
        //校验楼层
        checkHouseFloor(dto.getBuildingId(), dto.getFloor());
        House afterHouse = MyBeanUtils.copy(dto, House::new);
        Integer houseDefaultStatus = Constant.HousePropertyEnum.getHouseDefaultStatusMap().get(afterHouse.getHouseProperty());
        afterHouse.setHouseStatus(houseDefaultStatus);
        EntityUserUtil.setAllDynamic(afterHouse);
        return this.saveOrUpdate(afterHouse) ? null : "插入数据失败！";
    }

}
