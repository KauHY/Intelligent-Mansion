package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.contract.entity.Contract;
import com.crlink.smartpark.contract.entity.HouseRelation;
import com.crlink.smartpark.contract.mapper.ContractMapper;
import com.crlink.smartpark.contract.model.excel.ContractHouseExcelExportDTO;
import com.crlink.smartpark.contract.model.query.ApartmentPeopleHouseQuery;
import com.crlink.smartpark.contract.model.query.ContractVoListQuery;
import com.crlink.smartpark.contract.model.vo.ContractHouseVO;
import com.crlink.smartpark.contract.service.ContractService;
import com.crlink.smartpark.contract.service.ContractVoService;
import com.crlink.smartpark.contract.service.HouseRelationService;
import com.crlink.smartpark.core.utils.ExcelExportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.system.dto.house.HouseQueryDTO;
import com.crlink.smartpark.system.feign.HouseProvider;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.DictConstant.ContractState.IN_EFFECT;
import static com.crlink.smartpark.common.constants.DictConstant.ContractState.TO_BE_EFFECTIVE;
import static com.crlink.smartpark.common.constants.DictConstant.ContractType.APARTMENT_CONTRACT;
import static com.crlink.smartpark.common.constants.DictConstant.ContractType.RENTAL_CONTRACT;
import static com.crlink.smartpark.contract.common.ContractConstants.HouseRelationType.CONTRACT;
import static com.crlink.smartpark.system.enums.HouseStatusEnum.RENTED;
import static com.crlink.smartpark.system.enums.HouseStatusEnum.RENTING;

/**
 * @ClassName: ContractVoServiceImpl
 * @Package com.crlink.smartpark.contract.service.impl
 * @Description: 合同视图 服务实现类
 * @Version 1.0
 */
@Service
public class ContractVoServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractVoService {

    @Autowired
    private ContractService contractService;
    @Autowired
    private HouseRelationService houseRelationService;
    @Autowired
    private HouseProvider houseProvider;

    @Override
    @Id2objHandle
    public PageInfo<ContractHouseVO> listPageWithHouseVO(ContractVoListQuery query) {
        query.setFromApartmentPeople(false);
        List<ContractHouseVO> voList = getVOList(query);
        voList = voList.stream().sorted(Comparator.comparing(ContractHouseVO::getCreateTime).reversed())
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(voList)) {
            return new PageInfo<>();
        }
        // 3、抽取 IDs 查询对应合同数据
        // 4、数据拼接
        // query.startPage();
        // 创建Page类
        // Page page = new Page(query.getPageNum(), query.getPageSize());
        // // 为Page类中的total属性赋值
        // int total = voList.size();
        // page.setTotal(total);
        // // 计算当前需要显示的数据下标起始值
        // int startIndex = (query.getPageNum() - 1) * query.getPageSize();
        // int endIndex = Math.min(startIndex + query.getPageSize(), total);
        // // 从链表中截取需要显示的子链表，并加入到Page
        // page.addAll(voList.subList(startIndex, endIndex));
        // 以Page创建PageInfo
        Page page = createPage(query, voList);
        PageInfo pageInfo = new PageInfo<>(page);
        return pageInfo;
        // return query.createPageInfo(voList);
    }

    @Override
    public void export(ContractVoListQuery query) {
        query.setFromApartmentPeople(false);
        List<ContractHouseVO> result = getVOList(query);
        String fileName = "房源状态查询";
        ExcelExportUtils.export(() -> result, this::convertToExcel, fileName, ContractHouseExcelExportDTO.class);
    }

    private List<ContractHouseExcelExportDTO> convertToExcel(List<ContractHouseVO> entities) {
        return MyBeanUtils.copyList(entities, ContractHouseExcelExportDTO::new, (s, t) -> {

        });
    }

    /**
     * 公寓人员管理关联房屋时使用
     *
     * @param query
     * @return
     */
    @Override
    @Id2objHandle
    public PageInfo<ContractHouseVO> apartmentPeopleHouse(ApartmentPeopleHouseQuery query) {
        ContractVoListQuery contractVoListQuery = MyBeanUtils.copy(query, ContractVoListQuery::new);
        contractVoListQuery.setFromApartmentPeople(true);
        List<ContractHouseVO> voList = getVOList(contractVoListQuery);
        MyPageInfoUtils.isEmptyPage(voList);
        Page page = createPage(contractVoListQuery, voList);
        PageInfo<ContractHouseVO> pageInfo = new PageInfo<>(page);
        return pageInfo;
    }

    /**
     * 根据园区获取已租、未租的房屋id集合
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public Map<Integer, Set<String>> getRentedHouseId(String parkOrgId) {
        ContractVoListQuery query = new ContractVoListQuery();
        query.setFromApartmentPeople(false);
        query.setParkOrgId(parkOrgId);
        Map<Integer, Set<String>> result = new HashMap<>();
//        query.setHouseStatus(RENTED.getCode());
        List<ContractHouseVO> voList = getVOList(query);
        if (CollectionUtil.isEmpty(voList)) {
            result.put(RENTED.getCode(), Collections.emptySet());
            result.put(RENTING.getCode(), Collections.emptySet());
            return result;
        }
        Set<String> rentedHouseIds = voList.stream().filter(t -> t.getHouseStatus().equals(RENTED.getCode()))
                .map(ContractHouseVO::getHouseId).collect(Collectors.toSet());
        Set<String> rentingHouseIds = voList.stream().filter(t -> t.getHouseStatus().equals(RENTING.getCode()))
                .map(ContractHouseVO::getHouseId).collect(Collectors.toSet());
        result.put(RENTED.getCode(), rentedHouseIds);
        result.put(RENTING.getCode(), rentingHouseIds);
        return result;
    }

    /**
     * 创建分页
     *
     * @param query
     * @param voList
     * @return
     */
    private Page createPage(ContractVoListQuery query, List<ContractHouseVO> voList) {
        // 3、抽取 IDs 查询对应合同数据
        // 4、数据拼接
        // query.startPage();
        // 创建Page类
        Page page = new Page(query.getPageNum(), query.getPageSize());
        // 为Page类中的total属性赋值
        int total = voList.size();
        page.setTotal(total);
        // 计算当前需要显示的数据下标起始值
        int startIndex = (query.getPageNum() - 1) * query.getPageSize();
        if (startIndex > total) {
            startIndex = 0;
            page.setPageNum(1);
        }
        int endIndex = Math.min(startIndex + query.getPageSize(), total);
        // 从链表中截取需要显示的子链表，并加入到Page
        page.addAll(voList.subList(startIndex, endIndex));
        // 以Page创建PageInfo
        // PageInfo pageInfo = new PageInfo<>(page);
        return page;
    }

    /**
     * 列表查询 - 分页 导出 通用 新版
     *
     * @param query:
     * @return java.util.List<com.crlink.smartpark.contract.model.vo.ContractHouseVO>
     * @MethodName getVOList
     */
    private List<ContractHouseVO> getVOList(ContractVoListQuery query) {
        // 1、筛选查询条件，后三个有值则拼接查询出HouseId Set
        String parkOrgId = query.getParkOrgId();
        String[] queryColumn = new String[]{"house_status", "building_id", "floor", "name", "house_function", "create_time"};
        HouseQueryDTO houseQueryDTO = HouseQueryDTO.buildingIdsBuildDTO(parkOrgId, null, queryColumn);

        if (StringUtils.isNotBlank(query.getHouseId())) {

            // 在进行条件重写
            Set<String> houseIdNew = new HashSet<String>();
            houseIdNew.add(query.getHouseId());
            houseQueryDTO.setHouseIds(houseIdNew);
        }
        if (StringUtils.isNotBlank(query.getBuildingId())) {
            Set<String> buildingIdNew = new HashSet<String>();
            buildingIdNew.add(query.getBuildingId());
            houseQueryDTO.setBuildingIds(buildingIdNew);
        }
        //公寓人员使用
        if (query.getFromApartmentPeople()) {
            // 查询房屋功能为公寓的房屋
            houseQueryDTO.setHouseFunctions(Sets.newHashSet(2, 3, 4));
        }
        // houseQueryDTO.setHouseStatus(Objects.nonNull(query.getHouseStatus()) ? query.getHouseStatus() : null);

        // 2、远程调用内部服务，查询对应house Map 格式
        Map<String, Map<String, Object>> queryHouseIdMap = houseProvider.queryHouseColumns(houseQueryDTO);

        // map 转List
        List<ContractHouseVO> voList = new ArrayList<>();
        queryHouseIdMap.forEach((k, v) -> {
            ContractHouseVO houseVO = entitieToVo(v);
            houseVO.setParkOrgId(parkOrgId);
            // 拼接合同字段
            // 依次取值：已生效>待生效（合同开始时间距当前时间最近的合同）>待提交/审核中（最近一个待提交/审核中的合同）>已退约/已失效/已作废（最近一个已退约/已失效/已作废的合同）
            Contract contract = getNearContract(houseVO.getHouseId());
            int flag = 1;

            if (contract != null) {
                Integer state = contract.getState();
                if (IN_EFFECT.getCode() == state
                        || DictConstant.ContractState.UNDER_REVIEW_WITHDRAW.getCode() == state
                        || (DictConstant.ContractState.TO_BE_SUBMITTED.getCode() == state && contract.getAttributes() == DictConstant.ProtocolAttributes.CHANGE_PROTOCOL.getCode())
                        || (DictConstant.ContractState.TO_BE_SUBMITTED.getCode() == state && contract.getAttributes() == DictConstant.ProtocolAttributes.WITHDRAWAL_PROTOCOL.getCode())
                        || (DictConstant.ContractState.UNDER_REVIEW.getCode() == state && contract.getAttributes() == DictConstant.ProtocolAttributes.CHANGE_PROTOCOL.getCode())
                        || (DictConstant.ContractState.UNDER_REVIEW.getCode() == state && contract.getAttributes() == DictConstant.ProtocolAttributes.WITHDRAWAL_PROTOCOL.getCode())) {
                    houseVO.setHouseStatus(houseVO.getHouseStatus() + 1);
                }
                houseVO.setContractId(contract.getId());
                houseVO.setContractNo(contract.getNo());
                houseVO.setType(contract.getType());
                houseVO.setState(state);
                // 在公寓人员管理处使用,查询合同状态为合同状态为“待生效、生效中”的房屋
                if (query.getFromApartmentPeople()) {
                    HashSet<Integer> states = Sets.newHashSet(TO_BE_EFFECTIVE.getCode(), IN_EFFECT.getCode());
                    if (!states.contains(state)) {
                        flag = 0;
                    }
                }
            } else if (query.getFromApartmentPeople()) {
                flag = 0;
            }
            if (Objects.nonNull(query.getHouseStatus())
                    && !query.getHouseStatus().equals(houseVO.getHouseStatus())) {
                flag = 0;
            }
            if (checkType(query)) {
                if (contract == null) {
                    flag = 0;
                } else if (Objects.nonNull(query.getState()) && query.getState() != contract.getState()) {
                    flag = 0;
                } else if (Objects.nonNull(query.getType()) && query.getType() != contract.getType()) {
                    flag = 0;
                } else if (query.getEndType() != null && query.getEndType() == Constants.ONE) {
                    // 筛选最近30天数据
                    LocalDate daysAgo = LocalDate.now().plusDays(30);
                    LocalDate daysEnd = contract.getEndDate();
                    if (daysEnd.isBefore(LocalDate.now()) || daysEnd.isAfter(daysAgo)) {
                        flag = 0;
                    }
                }

            }
            if (flag == 1) {
                voList.add(houseVO);
            }

        });
        return voList;
    }

    /**
     * 列表查询 - 分页 导出 通用
     *
     * @param query:
     * @return java.util.List<com.crlink.smartpark.contract.model.vo.ContractHouseVO>
     * @MethodName getVOList
     */
    private List<ContractHouseVO> getVOListOld(ContractVoListQuery query) {
        // 1、筛选查询条件，后三个有值则拼接查询出HouseId Set
        String parkOrgId = query.getParkOrgId();
        String[] queryColumn = new String[]{"house_status", "building_id", "floor", "name", "house_function"};
        HouseQueryDTO houseQueryDTO = HouseQueryDTO.buildingIdsBuildDTO(parkOrgId, null, queryColumn);

        Set<String> houseIds = new HashSet<>();
        List<Contract> contractList = new ArrayList<>();
        if (checkType(query)) {
            LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<Contract>();
            // 本身正常调用
//            if (query.getFromContractPge()) {
            queryWrapper.eq(Objects.nonNull(query.getState()), Contract::getState, query.getState());
//            } else {
//                // 在公寓人员管理处使用,查询合同状态为合同状态为“待生效、生效中”的房屋
//                queryWrapper.eq(Contract::getState, IN_EFFECT.getCode()).or().eq(Contract::getState,
//                    TO_BE_EFFECTIVE.getCode());
//                // 查询房屋功能为公寓的房屋
//                houseQueryDTO.setHouseFunctions(Sets.newHashSet(2, 3, 4));
//            }
            queryWrapper.eq(Objects.nonNull(query.getType()), Contract::getType, query.getType());
            if (query.getEndType() != null && query.getEndType() == Constants.ONE) {
                // 筛选最近30天数据
                LocalDate daysAgo = LocalDate.now().plusDays(30);
                queryWrapper.le(Contract::getEndDate, daysAgo);
                queryWrapper.ge(Contract::getEndDate, LocalDate.now());
            }
            contractList = contractService.list(queryWrapper);
            if (CollUtil.isEmpty(contractList)) {
                return null;
            }
            Set<String> ids = contractList.parallelStream().map(Contract::getId).collect(Collectors.toSet());
            houseIds = houseRelationService.lambdaQuery().eq(HouseRelation::getType, CONTRACT.value)
                    .in(HouseRelation::getPkId, ids).list().parallelStream().map(HouseRelation::getHouseId)
                    .collect(Collectors.toSet());
            if (CollUtil.isEmpty(houseIds)) {
                return null;
            }

            houseQueryDTO.setHouseIds(houseIds);
        }
        if (StringUtils.isNotBlank(query.getHouseId())) {
            // 当 houseId 非空，判断是否在里面 不在返回空
            if (CollUtil.isNotEmpty(houseIds) && !houseIds.contains(query.getHouseId())) {
                return null;
            }
            // 在进行条件重写
            Set<String> houseIdNew = new HashSet<String>();
            houseIdNew.add(query.getHouseId());
            houseQueryDTO.setHouseIds(houseIdNew);
        }
        if (StringUtils.isNotBlank(query.getBuildingId())) {
            Set<String> buildingIdNew = new HashSet<String>();
            buildingIdNew.add(query.getBuildingId());
            houseQueryDTO.setBuildingIds(buildingIdNew);
        }
        houseQueryDTO.setHouseStatus(Objects.nonNull(query.getHouseStatus()) ? query.getHouseStatus() : null);

        // 2、远程调用内部服务，查询对应house Map 格式
        Map<String, Map<String, Object>> queryHouseIdMap = houseProvider.queryHouseColumns(houseQueryDTO);

        // map 转List
        List<ContractHouseVO> voList = new ArrayList<>();
        queryHouseIdMap.forEach((k, v) -> {
            ContractHouseVO houseVO = entitieToVo(v);
            houseVO.setParkOrgId(parkOrgId);
            // 拼接合同字段
            // 依次取值：已生效>待生效（合同开始时间距当前时间最近的合同）>待提交/审核中（最近一个待提交/审核中的合同）>已退约/已失效/已作废（最近一个已退约/已失效/已作废的合同）
            Contract contract = getNearContract(houseVO.getHouseId());
            if (contract != null) {
                houseVO.setContractId(contract.getId());
                houseVO.setContractNo(contract.getNo());
                houseVO.setType(contract.getType());
                houseVO.setState(contract.getState());
            }
            voList.add(houseVO);
        });
        return voList;
    }

    //2022.6.7
    //v17修改：房源状态由5个合同类型判断调整为仅按“租赁合同、公寓合同”判定房屋状态，并调整“合同类型”检索项下拉选项。
    private Contract getNearContract(String houseId) {
        Set<String> contractIds = new HashSet<>();
        contractIds = houseRelationService.lambdaQuery().eq(HouseRelation::getType, CONTRACT.value)
                .eq(HouseRelation::getHouseId, houseId).list().parallelStream().map(HouseRelation::getPkId)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(contractIds)) {
            return null;
        }
        List<Contract> list = contractService.lambdaQuery()
                .in(Contract::getType, RENTAL_CONTRACT.getCode(), APARTMENT_CONTRACT.getCode())
                .in(Contract::getId, contractIds).orderByDesc(Contract::getUpdateTime).list();
        // 依次取值：已生效>待生效（合同开始时间距当前时间最近的合同）
        // >待提交/审核中（最近一个待提交/审核中的合同）
        // >已退约/已失效/已作废（最近一个已退约/已失效/已作废的合同）

        Contract contract = new Contract();
        boolean changeFlag = false;
        // 存储当前合同级别
        int sort = 999;
        // 存储当前数据对应最近日期
        LocalDate tobeTime = LocalDate.parse("2099-12-31");
        // 存储当前数据结束日期
        LocalDate expiredTime = LocalDate.parse("2000-01-01");
        for (int i = 0; i < list.size(); i++) {
            Contract nowContract = list.get(i);
            // switch (nowContract.getState ()){
            // case DictConstant.ContractState.IN_EFFECT.getCode (): contract = nowContract;
            // }
            int state = nowContract.getState();
            if (IN_EFFECT.getCode() == state) {
                // 已生效
                contract = list.get(i);
                changeFlag = true;
                sort = 0;
                break;
            } else if (TO_BE_EFFECTIVE.getCode() == state) {
                // 待生效
                sort = 1;
                if (tobeTime.isAfter(nowContract.getStartDate())) {
                    tobeTime = nowContract.getStartDate();
                    contract = list.get(i);
                    changeFlag = true;
                }
            } else if (sort > 2 && (DictConstant.ContractState.TO_BE_SUBMITTED.getCode() == state
                    || DictConstant.ContractState.UNDER_REVIEW.getCode() == state)) {
                sort = 2;
                contract = list.get(i);
                changeFlag = true;
            } else if (sort >= 3) {
                if (expiredTime.isBefore(nowContract.getEndDate())) {
                    expiredTime = nowContract.getEndDate();
                    contract = list.get(i);
                    changeFlag = true;
                }
            }
        }
        if (changeFlag) {
            return contract;
        } else {
            return null;
        }

    }
    // 依次取值：已生效
    // >待生效（合同开始时间距当前时间最近的合同）
    // >待提交/审核中（最近一个待提交/审核中的合同）
    // >已退约/已失效/已作废（最近一个已退约/已失效/已作废的合同）
    // TO_BE_SUBMITTED(1, "待提交"),//待提交
    // UNDER_REVIEW(2, "审核中"),//审核中
    // TO_BE_EFFECTIVE(3, "待生效"),//待生效
    // IN_EFFECT(4, "生效中"),//生效中
    // WITHDRAWN(5, "已退约"),//已退约
    // INVALIDATION(6, "已失效"),//已失效
    // ABOLISHED(7, "已作废"),//已作废
    // EXPIRED(8, "已过期");//已过期

    /***
     * Map 转 VO
     *
     * @return DTO集合
     */
    private ContractHouseVO entitieToVo(Map<String, Object> map) {
        ContractHouseVO houseVO = new ContractHouseVO();
        // "house_status", "building_id", "floor", "name"
        houseVO.setHouseId((String) map.get("id"));
        houseVO.setHouseName((String) map.get("name"));
        houseVO.setHouseStatus((Integer) map.get("house_status"));
        houseVO.setBuildingId((String) map.get("building_id"));
        houseVO.setFloor((Integer) map.get("floor"));
        houseVO.setHouseFunction((Integer) map.get("house_function"));
        houseVO.setCreateTime(LocalDateTime.parse((String) map.get("create_time"), DateTimeFormatter.ofPattern(Constants.TIME_PATTERN)));
        return houseVO;
    }

    private boolean checkType(ContractVoListQuery query) {
        if (Objects.nonNull(query.getType())
                || (Objects.nonNull(query.getEndType()) && query.getEndType() == Constants.ONE)
                || Objects.nonNull(query.getState())) {
            return true;
        }
        return false;
    }
}
