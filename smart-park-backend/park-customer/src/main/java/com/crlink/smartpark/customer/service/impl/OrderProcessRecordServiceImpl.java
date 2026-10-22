package com.crlink.smartpark.customer.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.customer.dto.OrderProcessRecordDTO;
import com.crlink.smartpark.customer.entity.OrderProcessRecord;
import com.crlink.smartpark.customer.mapper.OrderProcessRecordMapper;
import com.crlink.smartpark.customer.service.OrderProcessRecordService;
import com.crlink.smartpark.customer.vo.OrderProcessRecordVO;
import com.crlink.smartpark.customer.vo.OrderVO;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DATE_FORMATTER;
import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS_FORMATTER;
import static com.crlink.smartpark.customer.common.Constant.CREATE_BY_SYSTEM;
import static com.crlink.smartpark.customer.common.Constant.CREATE_BY_SYSTEM_NAME;
import static com.crlink.smartpark.customer.common.enums.OrderEnum.*;

/**
 * <p>
 * 报事报修处理流程表 服务实现类
 * </p>
 * */
@Service
public class OrderProcessRecordServiceImpl extends ServiceImpl<OrderProcessRecordMapper, OrderProcessRecord> implements OrderProcessRecordService {

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private OrderProcessRecordService orderProcessRecordService;

    @Autowired
    private CommonProvider commonProvider;


    @Override
    public List<OrderProcessRecordVO> getRecordList(OrderVO orderVO, Boolean fromWx) {
        List<OrderProcessRecord> recordList = orderProcessRecordService.lambdaQuery()
                .eq(OrderProcessRecord::getOrderId, orderVO.getId())
                .orderByAsc(OrderProcessRecord::getCreateTime).list();
        List<OrderProcessRecordVO> result = records2RecordVOs(recordList);
        if (fromWx) {
            result = orderByCreateTimeDesc(result, orderVO);
        }
        return result;
    }

    /**
     * 查询工单最终派给了哪个员工
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderProcessRecordDTO getPreRecord(String orderId) {
        List<OrderProcessRecord> recordList = list(
                Wrappers.<OrderProcessRecord>lambdaQuery()
                        .select(OrderProcessRecord::getId, OrderProcessRecord::getOrderId, OrderProcessRecord::getEmployeeId)
                        .eq(OrderProcessRecord::getOrderId, orderId)
                        .eq(OrderProcessRecord::getState, RECORD_STATE_ASSIGN_ORDER.code)
                        .orderByDesc(OrderProcessRecord::getCreateTime).last("limit 1")
        );
        OrderProcessRecordDTO dto = new OrderProcessRecordDTO();
        BeanUtils.copyProperties(recordList.get(0), dto);
        return dto;
    }

    private List<OrderProcessRecordVO> records2RecordVOs(List<OrderProcessRecord> recordList) {
        Set<String> createIds = recordList.parallelStream().map(OrderProcessRecord::getCreateBy).collect(Collectors.toSet());
        Map<String, Map<String, Object>> createNames =
                commonProvider.getUsernameByUserId(createIds, Sets.newHashSet(Constants.USER_REAL_NAME, Constants.NICK_NAME));
        //获取处理人employeeId,派单、转办时
        Set<String> employeeIds = recordList.parallelStream()
//                .filter(t -> t.getState().equals(RECORD_STATE_ASSIGN_ORDER.code))
                .filter(t -> StringUtils.isNotBlank(t.getEmployeeId()))
                .map(OrderProcessRecord::getEmployeeId).collect(Collectors.toSet());
        Map<String, EmployeeDTO> employeeId2EmployeeDTOsMap = employeeProvider.orderRecordQueryEmployeeColumns(employeeIds);
//        Map<String, Map<String, Object>> employeeId2CompanyIdsMap =
//                employeeProvider.queryEmployeeColumns(EmployeeQueryDTO.employeeIdsBuildDTO(Sets.newHashSet(employeeIds),
//                        "name", "company_id"));
//        Map<String, String> employeeNameMap = getEmployeeNameAndCompanyName(employeeIds, employeeId2CompanyIdsMap);
//        Map<String, Map<String, Object>> employeeNameAndCompanyName = getEmployeeNameAndCompanyName(employeeIds, employeeId2CompanyIdsMap);
        List<OrderProcessRecordVO> result = setCreateNameAndEmployeeName(recordList, employeeId2EmployeeDTOsMap, createNames);
        return result;
    }

//    /**
//     * 获取处理人：员工名称+公司
//     *
//     * @param employeeIds
//     * @param employeeId2CompanyIdsMap
//     * @return
//     */
//    private Map<String, Map<String, Object>> getEmployeeNameAndCompanyName(Set<String> employeeIds,
//                                                                           Map<String, Map<String, Object>> employeeId2CompanyIdsMap) {
//        Set<String> companyIds = Sets.newHashSet();
//        for (String t : employeeIds) {
//            String companyId = (String) employeeId2CompanyIdsMap.get(t).get(EmployeeFiled.COMPANY_ID);
//            companyIds.add(companyId);
//        }
//        Map<String, String> companyNamesMap = commonProvider.getNamesByIdsAndType(companyIds, SimpleNode.TypeEnum.COMPANY.getCode());
//        Map<String, String> employeeNameMap = CollectionUtils.newHashMap();
//        Map<String, Map<String, Object>> employeeNameMap2 = CollectionUtils.newHashMap();
//        employeeId2CompanyIdsMap.forEach((k, v) -> {
////            String name = (String) v.get(EmployeeFiled.NAME);
////            String mobile = (String) v.get(EmployeeFiled.MOBILE);
//            String companyName = companyNamesMap.get((String) v.get(EmployeeFiled.COMPANY_ID));
//            v.put("companyName", companyName);
////            employeeNameMap.put(k, name + "-" + companyName);
//            employeeNameMap2.put(k, v);
//        });
//        return employeeNameMap2;
//    }

    /**
     * 小程序端按创建时间倒序
     *
     * @param result
     * @param orderVO
     * @return
     */
    private List<OrderProcessRecordVO> orderByCreateTimeDesc(List<OrderProcessRecordVO> result, OrderVO orderVO) {
        OrderProcessRecordVO temp = result.get(0);
        String recordId = temp.getId();
        Integer state = temp.getState();
        BeanUtils.copyProperties(orderVO, temp);
        temp.setOrderState(state);
        temp.setId(recordId);
//        result = result.parallelStream().sorted(Comparator.comparing(OrderProcessRecordVO::getCreateTime)
//                .reversed()).collect(Collectors.toList());
        Collections.reverse(result);
        return result;
    }

    /**
     * 设置投诉、报修工单记录的createName,employeeName等
     *
     * @param recordList
     * @param employeeId2EmployeeDTOsMap
     * @param createNames
     * @return
     */
    private List<OrderProcessRecordVO> setCreateNameAndEmployeeName(List<OrderProcessRecord> recordList,
                                                                    Map<String, EmployeeDTO> employeeId2EmployeeDTOsMap,
                                                                    Map<String, Map<String, Object>> createNames) {
        //使用循环获取前面的处理人（姓名+公司名）为退单人、结单人、
        List<OrderProcessRecordVO> result = recordList.stream().map(t -> {
            OrderProcessRecordVO vo = MyBeanUtils.copy(t, OrderProcessRecordVO::new);
//                    t.getCreateTime().format(TIME_WITHOUT_YEAR_FORMATTER)
            vo.setCreateTime(t.getCreateTime().format(DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS_FORMATTER));
            Integer state = t.getState();
            EmployeeDTO employeeDTO = employeeId2EmployeeDTOsMap.get(t.getEmployeeId());
            if (state.equals(RECORD_STATE_ASSIGN_ORDER.code) || state.equals(RECORD_STATE_TRANSFER.code)) {
                //派单记录
                vo.setEmployeeName(employeeDTO.getName() + "-" + employeeDTO.getCompanyName());
//                        vo.setMobile(t.getEmployeeMobile());
                vo.setMobile(employeeDTO.getMobile());
                vo.setCreateByName(setCreateByName(createNames, t));
            } else if (state.equals(RECORD_STATE_RETURN_ORDER.code) || state.equals(RECORD_STATE_FINISH_ORDER.code)) {
                //退单、结单记录
                if (CREATE_BY_SYSTEM.equals(t.getCreateBy())) {
                    vo.setCreateByName(CREATE_BY_SYSTEM_NAME);
                } else {
//                            vo.setCreateByName(employeeNameMap.get(t.getEmployeeId()));
                    LocalDateTime finishTime = t.getFinishTime();
                    if (Objects.nonNull(finishTime)) {
                        vo.setFinishTime(finishTime.format(DATE_FORMATTER));
                    }
                    vo.setCreateByName(employeeDTO.getName() + "-" + employeeDTO.getCompanyName());
                }
            } else {
                //创建、撤单记录
                vo.setCreateByName(setCreateByName(createNames, t));
            }
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 派单、创建、撤单记录设置CreateByName
     *
     * @param createNames
     * @param record
     * @return
     */
    private String setCreateByName(Map<String, Map<String, Object>> createNames, OrderProcessRecord record) {
        String name = (String) createNames.get(record.getCreateBy()).get(Constants.USER_REAL_NAME);
        //在小程序端、如果当前登录用户的real_name为空、取其nick_name
        if (StringUtils.isNotBlank(name)) {
            return name;
        } else {
            String nickName = (String) createNames.get(record.getCreateBy()).get(Constants.NICK_NAME);
            return nickName;
        }
    }

}
