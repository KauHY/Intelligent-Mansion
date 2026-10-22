package com.crlink.smartpark.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.business.common.BusinessChanceRecordTypeEnum;
import com.crlink.smartpark.business.controller.request.BusinessChanceOperationDTO;
import com.crlink.smartpark.business.dto.chance.*;
import com.crlink.smartpark.business.entity.BusinessChance;
import com.crlink.smartpark.business.entity.BusinessChanceRecord;
import com.crlink.smartpark.business.mapper.BusinessChanceMapper;
import com.crlink.smartpark.business.model.excel.BusinessChanceExcelVO;
import com.crlink.smartpark.business.service.BusinessChanceRecordService;
import com.crlink.smartpark.business.service.BusinessChanceService;
import com.crlink.smartpark.business.service.CustomerInfoService;
import com.crlink.smartpark.business.service.HouseInfoService;
import com.crlink.smartpark.business.vo.BusinessHouseVO;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.field.EmployeeFiled;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.business.common.BusinessChanceRecordTypeEnum.*;
import static com.crlink.smartpark.common.constants.Constants.USER_MOBILE;
import static com.crlink.smartpark.common.constants.enums.JobCodeEnum.BUSINESS_MAN_JOB_CODE;

/**
 * <p>
 * 商机表 服务实现类
 * </p>
 * */
@Service
public class BusinessChanceServiceImpl extends ServiceImpl<BusinessChanceMapper, BusinessChance> implements BusinessChanceService {

    //全部商机
    public static final int ALL_CHANCE_QUERY_TYPE = 0;
    //公共商机
    public static final int COMMON_CHANCE_QUERY_TYPE = 1;
    //我的商机
    public static final int MY_CHANCE_QUERY_TYPE = 2;

    public static final int REMIND_FREQUENCY_IS_BY_DAY = 1;

    /**
     * 商机阶段：7 赢单关闭
     */
    public static final int CHANCE_WIN_CLOSE = 7;
    //商机无跟进人
    public static final String SET_FOLLOW_EMPLOYEE_ID_NULL = "-1";


    @Autowired
    private BusinessChanceRecordService businessChanceRecordService;

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private CustomerInfoService customerInfoService;

    @Autowired
    private HouseInfoService houseInfoService;

    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 存储对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateBusinessChance(BusinessChanceReqDTO dto) {
        BusinessChance businessChance = MyBeanUtils.copy(dto, BusinessChance::new);
        EntityUserUtil.setAllDynamic(businessChance);
        if (StringUtils.isBlank(dto.getFollowEmployeeId())) {
            //如果无跟进人，将其设置为-1
            businessChance.setFollowEmployeeId(SET_FOLLOW_EMPLOYEE_ID_NULL);
        }
        this.saveOrUpdate(businessChance);
        if (StringUtils.isBlank(dto.getId())) {
            BusinessChanceRecord record = new BusinessChanceRecord();
            record.setBusinessChanceId(businessChance.getId());
            record.setRecordType(CREATE_CHANCE_1.getCode());
            record.setChanceStage(businessChance.getChanceStage());
            record.setFollowEmployeeId(businessChance.getFollowEmployeeId());
            EntityUserUtil.setAll(record);
            businessChanceRecordService.save(record);
        }
        return true;
    }


    /**
     * 运营端小程序分页查询
     *
     * @param query
     * @param type
     * @return
     */
    @Override
    public PageInfo<BusinessChanceVO> listBusinessChanceWxPage(BaseQuery<String> query, Integer type) {
        String parkOrgId = SystemUtil.getInstance().getParkOrgIdByEmployeeId();
        LambdaQueryChainWrapper<BusinessChance> lambdaQuery = getWxWrapper(parkOrgId, type);
        query.startPage();
        List<BusinessChance> chancesList = lambdaQuery.list();
        MyPageInfoUtils.isEmptyPage(chancesList);
        List<BusinessChanceVO> result = convert(chancesList);
        return query.createPageInfo(result);
    }

    /**
     * 后台分页查询
     *
     * @param query
     * @param type
     * @return
     */
    @Override
    public PageInfo<BusinessChanceVO> listBusinessChancePage(BusinessChanceQuery query, Integer type) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryChainWrapper<BusinessChance> wrapper = getWrapper(query, type, parkOrgIds);
        query.startPage();
        List<BusinessChance> chances = wrapper.list();
        MyPageInfoUtils.isEmptyPage(chances);
        List<BusinessChanceVO> result = convert(chances);
        return query.createPageInfo(result);
    }


    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public BusinessChanceVO getBusinessChanceById(String id) {
        BusinessChance entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("90003"));
        BusinessChanceVO businessChanceVO = convert(Lists.newArrayList(entity)).get(0);
        //获取意向房源的名称
        if (CollectionUtil.isNotEmpty(entity.getHouseIds())) {
            Set<String> houseIds = entity.getHouseIds().parallelStream().collect(Collectors.toSet());
            List<BusinessHouseVO> houseInfoNames = houseInfoService.getHouseInfoNames(houseIds);
            businessChanceVO.setBusinessHouseVO(houseInfoNames);
        }
        return businessChanceVO;
    }

    /**
     * 跟进记录
     *
     * @param
     * @return
     */
    @Override
    @Id2objHandle
    public PageInfo<FollowBusinessChanceVO> getFollowRecordsById(String id, BaseQuery<String> query) {
        query.startPage();
        List<BusinessChanceRecord> records = businessChanceRecordService.lambdaQuery()
                .eq(BusinessChanceRecord::getRecordType, FOLLOW_CHANCE_4.getCode())
                .eq(BusinessChanceRecord::getBusinessChanceId, id).list();
        MyPageInfoUtils.isEmptyPage(records);
//        Set<String> ids = records.parallelStream().map(t -> t.getCreateBy()).collect(Collectors.toSet());
//        Map<String, Map<String, Object>> usernames = commonProvider.getUsernameByUserId(ids, Sets.newHashSet(Constants.USER_REAL_NAME));
        List<FollowBusinessChanceVO> followLists = records.parallelStream()
                .map(record -> {
                    FollowBusinessChanceVO followRecord = MyBeanUtils.copy(record, FollowBusinessChanceVO::new);
//                    String name = (String) usernames.get(record.getCreateBy()).get(Constants.USER_REAL_NAME);
//                    followRecord.setFollowEmployeeName(name);
                    return followRecord;
                })
                .sorted(Comparator.comparing(FollowBusinessChanceVO::getCreateTime).reversed())
                .collect(Collectors.toList());
        return query.createPageInfo(followLists);
    }

    /**
     * 阶段流程
     *
     * @param id
     * @return
     */
    @Override
    @Id2objHandle
    public List<BusinessChanceRecordVO> getStageProcessById(String id) {
        Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("90003"));
        List<BusinessChanceRecord> records = businessChanceRecordService.lambdaQuery()
                .eq(BusinessChanceRecord::getBusinessChanceId, id).list();
//        Set<String> ids = records.parallelStream().map(t -> t.getCreateBy()).collect(Collectors.toSet());
//        Map<String, Map<String, Object>> usernames = commonProvider.getUsernameByUserId(ids, Sets.newHashSet(Constants.USER_REAL_NAME));
        Map<Integer, String> map = Arrays.stream(BusinessChanceRecordTypeEnum.values()).collect(Collectors.toMap(BusinessChanceRecordTypeEnum::getCode, BusinessChanceRecordTypeEnum::getDesc));
        //处理阶段流程中要返回的数据
        List<BusinessChanceRecordVO> result = records.parallelStream().map(record -> {
            BusinessChanceRecordVO vo = MyBeanUtils.copy(record, BusinessChanceRecordVO::new);
//            String name = (String) usernames.get(record.getCreateBy()).get(Constants.USER_REAL_NAME);
//            vo.setCreateByName(name);
            vo.setRecordTypeName(map.get(record.getRecordType()));
            return vo;
        })
                .sorted(Comparator.comparing(BusinessChanceRecordVO::getCreateTime).reversed())
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 操作商机:操作类型（recordType）：2分配商机、3领取商机、7转移商机、8释放商机
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean operateBusinessChance(BusinessChanceOperationDTO dto, Boolean isPc) {
        Integer recordType = dto.getRecordType();
        String id = dto.getId();
        BusinessChance businessChance = new BusinessChance();
        businessChance.setId(id);
        businessChance.setFollowEmployeeId(dto.getFollowEmployeeId());
        //后台领取商机时，校验当前用户是否为此商机所属园区下的“招商”岗位员工，若不是，则提示“仅限此园区下招商岗位员工可领取”
        if (isPc && recordType.equals(RECEIVE_CHANCE_3.getCode())) {
            getEmployeeIdByParkOrgIdAndMobile(businessChance);
        }
        //释放商机时，将商机提醒的相关信息设为空
        if (recordType.equals(RELEASE_CHANCE_8.getCode())) {
            remindSetNull(businessChance);
            businessChance.setFollowEmployeeId(SET_FOLLOW_EMPLOYEE_ID_NULL);
        }
        EntityUserUtil.setUpdateBy(businessChance);
        this.updateById(businessChance);
        BusinessChanceRecord record = new BusinessChanceRecord();
        record.setBusinessChanceId(id);
        record.setRecordType(recordType);
        record.setFollowEmployeeId(businessChance.getFollowEmployeeId());
        EntityUserUtil.setAll(record);
        return businessChanceRecordService.save(record);
    }


    /**
     * 商机提醒
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean remindBusinessChance(RemindBusinessChanceDTO dto) {
        Integer remindFrequency = dto.getRemindFrequency();
        String id = dto.getId();
        BusinessChance businessChance = new BusinessChance();
        businessChance.setId(id);
        businessChance.setRemindFrequency(remindFrequency);
        if (remindFrequency.equals(REMIND_FREQUENCY_IS_BY_DAY)) {
            //提醒频次为1 即按天提醒
            businessChance.setRemindByDay(dto.getRemindByDay());
            businessChance.setRemindByWeek(null);
        } else {
            //提醒频次为2 即按周提醒
            businessChance.setRemindByDay(null);
            businessChance.setRemindByWeek((dto.getRemindByWeek()));
        }
        businessChance.setRemindTime(dto.getRemindTime());
        businessChance.setRemindContent(dto.getRemindContent());
        EntityUserUtil.setUpdateBy(businessChance);
        this.updateById(businessChance);
        BusinessChanceRecord record = new BusinessChanceRecord();
        record.setBusinessChanceId(id);
        record.setRecordType(REMIND_CHANCE_5.getCode());
        EntityUserUtil.setAll(record);
        return businessChanceRecordService.save(record);
    }

    /**
     * 商机跟进
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean followBusinessChance(FollowBusinessChanceDTO dto) {
        String id = dto.getId();
        Integer stage = dto.getChanceStage();
        LocalDateTime followTime = dto.getFollowTime();
        BusinessChance businessChance = new BusinessChance();
        businessChance.setId(id);
        businessChance.setLatestFollowTime(followTime);
        businessChance.setChanceStage(stage);
        //跟进时将商机状态改为了 7 赢单关闭
//        if (stage.equals(CHANCE_WIN_CLOSE)) {
//            remindSetNull(businessChance);
//        }
        EntityUserUtil.setUpdateBy(businessChance);
        this.updateById(businessChance);
        BusinessChanceRecord record = new BusinessChanceRecord();
        record.setBusinessChanceId(id);
        record.setRecordType(FOLLOW_CHANCE_4.getCode());
        record.setChanceStage(stage);
        record.setFollowType(dto.getFollowType());
        record.setFollowTime(followTime);
        record.setFollowContent(dto.getFollowContent());
        EntityUserUtil.setAll(record);
        return businessChanceRecordService.save(record);
    }


    /**
     * 商机删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeBusinessChanceById(String id) {
        QueryWrapper<BusinessChanceRecord> queryWrapper = new QueryWrapper<>();
        businessChanceRecordService.remove(queryWrapper.lambda().eq(BusinessChanceRecord::getBusinessChanceId, id));
        return this.removeById(id);
    }

    /**
     * 转成客户
     *
     * @param reqDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean convertToCustomer(ConvertToCustomerReqDTO reqDTO) {
        String businessId = reqDTO.getBusinessId();
        BusinessChance businessChance = Optional.ofNullable(getById(businessId)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        //转成客户即代表商机阶段改为赢单关闭：7
        businessChance.setChanceStage(CHANCE_WIN_CLOSE);
        remindSetNull(businessChance);
        EntityUserUtil.setUpdateBy(businessChance);
        this.updateById(businessChance);
        BusinessChanceRecord record = new BusinessChanceRecord();
        record.setRecordType(TRANSFER_CUSTOMER_6.getCode());
        record.setBusinessChanceId(businessId);
        record.setChanceStage(CHANCE_WIN_CLOSE);
        EntityUserUtil.setAll(record);
        businessChanceRecordService.save(record);
        //客户名称取商机名称
        //新增一条客户信息
        return customerInfoService.saveOrUpdateWxCustomer(reqDTO);
    }

    @Override
    public void export(BusinessChanceQuery query, Integer type) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getOrgId());
        LambdaQueryChainWrapper<BusinessChance> wrapper = getWrapper(query, type, parkOrgIds);
        ExcelExportUtils.export(wrapper::list,
                sourceList -> MyBeanUtils.copyList(convert(sourceList), BusinessChanceExcelVO::new),
                getExportFileName(type), BusinessChanceExcelVO.class);
    }


    @Override
    public Integer getChanceCount(String parkOrgId) {
        return this.lambdaQuery().eq(BusinessChance::getParkOrgId, parkOrgId).count();
    }

    private String getExportFileName(Integer type) {
        if (type == ALL_CHANCE_QUERY_TYPE) {
            return "全部商机";
        } else if (type == COMMON_CHANCE_QUERY_TYPE) {
            return "公共商机";
        } else {
            return "我的商机";
        }
    }

    /**
     * 商机分析-商机概览
     *
     * @param query
     * @return
     */
    @Override
    public AnalysisOverview getChanceOverview(BusinessStatisticQuery query) {
        return getResultData(query, "BUSINESS_CHANCE_STAGE", "chance_stage", BusinessChance::getChanceStage);
    }

    /**
     * 商机分析-线索来源分析
     * 选择了线索来源的统计总数，没选的不算在总数内
     *
     * @param query
     * @return
     */
    @Override
    public AnalysisOverview getThreadSource(BusinessStatisticQuery query) {
        return getResultData(query, "THREAD_SOURCE", "thread_source", BusinessChance::getThreadSource);
    }


    /**
     * 商机转换率：开始阶段 转化至 结束阶段 的百分比
     * 前6个阶段：全部商机”模块详情中“跟进记录”历史“商机阶段”的商机次数。当商机经历每一个阶段，每个阶段加1，如果“机会评估”有两次，商机阶段加2。
     * 赢单关闭：取值于“全部商机”列表中“赢单关闭”阶段的商机次数
     *
     * @param query
     * @return
     */
    @Override
    public AnalysisOverview getTransitionRate(BusinessStatisticQuery query) {
        AnalysisOverview result = new AnalysisOverview();
        Map<Integer, String> dictTypeMaps = DictUtil.getLabels("BUSINESS_CHANCE_STAGE");
        Map<String, Long> businessData = new LinkedHashMap<>();
        dictTypeMaps.forEach((k, v) -> businessData.put(v, 0L));
        result.setBusinessData(businessData);
//        result.setTotalAmount(0);
        LambdaQueryChainWrapper<BusinessChance> wrapper = this.lambdaQuery()
                .select(BusinessChance::getId, BusinessChance::getChanceStage)
                .eq(BusinessChance::getParkOrgId, query.getParkOrgId());
        if (Objects.nonNull(query.getCreateTimeStart()) && Objects.nonNull(query.getCreateTimeEnd())) {
            wrapper.between(BusinessChance::getCreateTime, query.getCreateTimeStart(), query.getCreateTimeEnd());
        }
        List<BusinessChance> chanceList = wrapper.list();
        if (CollectionUtil.isNotEmpty(chanceList)) {
            Long winCount = chanceList.parallelStream().filter(t -> t.getChanceStage().equals(CHANCE_WIN_CLOSE)).count();
            businessData.put(dictTypeMaps.get(CHANCE_WIN_CLOSE), winCount);
            Set<String> chanceIds = chanceList.parallelStream().map(BusinessChance::getId)
                    .collect(Collectors.toSet());
            //查询出对应商机的所有跟进记录
            List<BusinessChanceRecord> recordList = businessChanceRecordService.lambdaQuery().select(BusinessChanceRecord::getId,
                    BusinessChanceRecord::getBusinessChanceId, BusinessChanceRecord::getChanceStage)
                    .eq(BusinessChanceRecord::getRecordType, FOLLOW_CHANCE_4.getCode())
                    .isNotNull(BusinessChanceRecord::getChanceStage)
                    .in(BusinessChanceRecord::getBusinessChanceId, chanceIds).list();
            Map<Integer, Long> countMap = recordList.stream().collect(
                    Collectors.groupingBy(BusinessChanceRecord::getChanceStage, Collectors.counting()));
//            countMap.forEach(businessData::put);
            countMap.forEach((k, v) -> businessData.put(dictTypeMaps.get(k), v));
        }

        return result;
    }

    private AnalysisOverview getResultData(BusinessStatisticQuery query, String dictType, String column,
                                           Function<? super BusinessChance, Integer> function) {
        AnalysisOverview result = new AnalysisOverview();
        Map<Integer, String> dictTypeMaps = DictUtil.getLabels(dictType);
        Map<String, Long> businessData = new LinkedHashMap<>();
        dictTypeMaps.forEach((k, v) -> businessData.put(v, 0L));
        result.setBusinessData(businessData);
//        result.setTotalAmount(0);
        QueryWrapper<BusinessChance> queryWrapper = Wrappers.<BusinessChance>query();
        //线索来源可能为空，需排除
        queryWrapper.select("id", column)
                .eq("park_org_id", query.getParkOrgId())
                .isNotNull(column);
        if (Objects.nonNull(query.getCreateTimeStart()) && Objects.nonNull(query.getCreateTimeEnd())) {
            queryWrapper.between("create_time", query.getCreateTimeStart(), query.getCreateTimeEnd());
        }
        List<BusinessChance> businessChanceList = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(businessChanceList)) {
            result.setTotalAmount(businessChanceList.size());
            Map<Integer, Long> countMap = businessChanceList.stream().collect(
                    Collectors.groupingBy(function, Collectors.counting()));
//            countMap.forEach(businessData::put);
            countMap.forEach((k, v) -> businessData.put(dictTypeMaps.get(k), v));
        }
        return result;
    }

    /**
     * 后端封装查询条件
     *
     * @param query
     * @param type
     * @return
     */
    private LambdaQueryChainWrapper<BusinessChance> getWrapper(BusinessChanceQuery query, Integer type, Set<String> parkOrgIds) {
        LambdaQueryChainWrapper<BusinessChance> lambdaQuery = this.lambdaQuery();
        String keyword = query.getKeyword();
        if (type == ALL_CHANCE_QUERY_TYPE) {
            if (StringUtils.isNotBlank(keyword)) {
                Map<String, Map<String, Object>> employeeColumns =
                        employeeProvider.queryEmployeeColumns(EmployeeQueryDTO.likeNameBuildDTO(keyword));
                lambdaQuery.and(wrapper -> wrapper
                        .like(BusinessChance::getChanceName, keyword)
                        .or().in(CollectionUtil.isNotEmpty(employeeColumns.keySet()),
                                BusinessChance::getFollowEmployeeId, employeeColumns.keySet()));
            }
        } else if (type == COMMON_CHANCE_QUERY_TYPE) {
            lambdaQuery.eq(BusinessChance::getFollowEmployeeId, SET_FOLLOW_EMPLOYEE_ID_NULL)
                    .like(StringUtils.isNotBlank(keyword), BusinessChance::getChanceName, keyword);
        } else if (type == MY_CHANCE_QUERY_TYPE) {
            String mobile = SystemUtil.getInstance().getCurrentUserInfo().getMobile();
            Map<String, Map<String, Object>> employeeColumns = employeeProvider.queryEmployeeColumns(EmployeeQueryDTO.mobileBuildDTO(mobile));
            MyPageInfoUtils.isEmptyPage(employeeColumns);
            lambdaQuery.in(BusinessChance::getFollowEmployeeId, employeeColumns.keySet())
                    .like(StringUtils.isNotBlank(keyword), BusinessChance::getChanceName, keyword);
        }
        if (query.getFollowStartTime() != null && query.getFollowEndTime() != null) {
            lambdaQuery.ge(BusinessChance::getLatestFollowTime, query.getFollowStartTime());
            lambdaQuery.le(BusinessChance::getLatestFollowTime, query.getFollowEndTime());
        }
        lambdaQuery.eq(Objects.nonNull(query.getChanceStage()), BusinessChance::getChanceStage, query.getChanceStage())
                .eq(Objects.nonNull(query.getBusinessSource()), BusinessChance::getBusinessSource, query.getBusinessSource())
                .in(BusinessChance::getParkOrgId, parkOrgIds)
                .orderByDesc(BusinessChance::getUpdateTime);
        return lambdaQuery;
    }


    /**
     * 运营端小程序封装查询条件
     *
     * @param parkOrgId
     * @param type
     * @return
     */
    private LambdaQueryChainWrapper<BusinessChance> getWxWrapper(String parkOrgId, Integer type) {
        LambdaQueryChainWrapper<BusinessChance> lambdaQuery = this.lambdaQuery();
        lambdaQuery.select(BusinessChance::getId, BusinessChance::getChanceName, BusinessChance::getChanceStage,
                BusinessChance::getFollowEmployeeId, BusinessChance::getContactName, BusinessChance::getContactPhone);
        switch (type) {
            case ALL_CHANCE_QUERY_TYPE:
                break;
            case COMMON_CHANCE_QUERY_TYPE:
                lambdaQuery.eq(BusinessChance::getFollowEmployeeId, SET_FOLLOW_EMPLOYEE_ID_NULL);
                break;
            case MY_CHANCE_QUERY_TYPE:
                lambdaQuery.eq(BusinessChance::getFollowEmployeeId, SystemUtil.getInstance().getEmployeeId());
                break;
            default:
                throw new ResolinkRuntimeException("90003");
        }
        lambdaQuery.eq(BusinessChance::getParkOrgId, parkOrgId).orderByDesc(BusinessChance::getUpdateTime);
        return lambdaQuery;
    }

    private List<BusinessChanceVO> convert(List<BusinessChance> list) {
        Set<String> parkOrgIds = Sets.newHashSet();
        Set<String> employeeIds = Sets.newHashSet();
        list.forEach(t -> {
            parkOrgIds.add(t.getParkOrgId());
            if (!SET_FOLLOW_EMPLOYEE_ID_NULL.equals(t.getFollowEmployeeId())) {
                employeeIds.add(t.getFollowEmployeeId());
            }
        });
        Map<String, String> parkOrgId2NameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        Map<String, Map<String, Object>> employeeColumns = employeeProvider.queryEmployeeColumns(
                EmployeeQueryDTO.employeeIdsBuildDTO(employeeIds, EmployeeFiled.COMPANY_ID));
        Map<String, String> employeeId2CompanyIdMap = Maps.newHashMap();
        employeeColumns.forEach((k, v) -> {
//            String companyId = (String) v.get("company_id");
            String companyId = (String) v.get(EmployeeFiled.COMPANY_ID);
            employeeId2CompanyIdMap.put(k, companyId);
        });
        Map<String, String> companyId2NameMap = commonProvider.getNamesByIdsAndType(Sets.newHashSet(employeeId2CompanyIdMap.values()), SimpleNode.TypeEnum.COMPANY.getCode());
        Map<String, String> employeeId2NameMap = commonProvider.getNamesByIdsAndType(employeeIds, SimpleNode.TypeEnum.EMPLOYEE.getCode());
        if (CollectionUtil.isEmpty(companyId2NameMap)) {

        }
        List<BusinessChanceVO> result = list.parallelStream().map(t -> {
            BusinessChanceVO chanceVO = MyBeanUtils.copy(t, BusinessChanceVO::new);
            String followEmployeeId = t.getFollowEmployeeId();
            if (!SET_FOLLOW_EMPLOYEE_ID_NULL.equals(followEmployeeId)) {
                String companyId = employeeId2CompanyIdMap.get(followEmployeeId);
                String companyName = companyId2NameMap.get(companyId);
                String employeeName = employeeId2NameMap.get(followEmployeeId);
                if (Objects.nonNull(companyName) || Objects.nonNull(employeeName)) {
                    employeeName = new StringBuilder(employeeName)
                            .append("(").append(companyName).append(")").toString();
                    chanceVO.setFollowEmployeeName(employeeName);
                }
            } else {
                chanceVO.setFollowEmployeeId(null);
            }
            chanceVO.setParkOrgName(parkOrgId2NameMap.get(t.getParkOrgId()));
            return chanceVO;
        }).collect(Collectors.toList());
        return result;
    }


    /**
     * 后台领取商机时判断当前登录用户在要领取的商机所在的园区是否关联了招商岗位
     *
     * @param businessChance
     * @param
     */
    private void getEmployeeIdByParkOrgIdAndMobile(BusinessChance businessChance) {
        //当前商机所在园区
        String businessParkOrgId = this.getBusinessChanceById(businessChance.getId()).getParkOrgId();
        Integer[] jobCodes = {BUSINESS_MAN_JOB_CODE.code};
        //按照园区，岗位查出员工查询出员工
        List<SimpleNode> employees = employeeProvider.queryEmployeesList(null, businessParkOrgId, null, null, null, jobCodes);
        if (CollectionUtil.isNotEmpty(employees)) {
            //当前登录用户手机号
            String mobile = SystemUtil.getInstance().getCurrentUserInfo().getMobile();
            Map<String, String> EmployeeId2Mobiles = new HashMap<>();
            employees.forEach(t -> {
                //根据手机号匹配
                HashMap<String, String> extraInfo = (HashMap<String, String>) t.getExtraInfo();
                if (mobile.equals(extraInfo.get(USER_MOBILE))) {
                    EmployeeId2Mobiles.put(extraInfo.get(USER_MOBILE), (String) t.getValue());
                }
            });
            if (EmployeeId2Mobiles.containsKey(mobile)) {
                businessChance.setFollowEmployeeId(EmployeeId2Mobiles.get(mobile));
            } else {
                throw new ResolinkRuntimeException("900012");
            }
        } else {
            throw new ResolinkRuntimeException("900012");
        }
    }


    /**
     * 检验商机名称是否重复
     *
     * @param id
     * @param chanceName
     */
    private void checkChanceName(String id, String chanceName) {
        Integer count = this.lambdaQuery().ne(StringUtils.isNotBlank(id), BusinessChance::getId, id).eq(BusinessChance::getChanceName, chanceName).count();
        if (count > 0) {
            throw new ResolinkRuntimeException(Message.code("90002"));
        }
    }

    /**
     * 将商机提醒的相关信息设为空
     *
     * @param businessChance
     * @return
     */
    private BusinessChance remindSetNull(BusinessChance businessChance) {
        businessChance.setRemindContent("");
        businessChance.setRemindByDay(0);
        businessChance.setRemindByWeek(Lists.newLinkedList());
        businessChance.setRemark("");
        businessChance.setRemindTime("");
        businessChance.setRemindFrequency(-1);
        return businessChance;
    }

}

