package com.crlink.smartpark.attendance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.attendance.controller.request.query.FieldworkApplyRecordQuery;
import com.crlink.smartpark.attendance.dto.FieldworkApplyRecordDTO;
import com.crlink.smartpark.attendance.dto.FieldworkApplyRecordExportDTO;
import com.crlink.smartpark.attendance.entity.FieldworkApplyRecord;
import com.crlink.smartpark.attendance.mapper.FieldworkApplyRecordMapper;
import com.crlink.smartpark.attendance.model.AuditReqDTO;
import com.crlink.smartpark.attendance.model.FieldworkApplyRecordReqDTO;
import com.crlink.smartpark.attendance.service.FieldworkApplyRecordService;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.constants.enums.JobCodeEnum;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.USER_WX_SOURCE_B;
import static com.crlink.smartpark.common.constants.Constants.USER_WX_SOURCE_C;


/**
 * 外勤申请记录 服务实现类
 * */
@Service
public class FieldworkApplyRecordServiceImpl extends ServiceImpl<FieldworkApplyRecordMapper, FieldworkApplyRecord> implements FieldworkApplyRecordService {

    public static final String FILE_NAME = "外勤申请";
    @Autowired
    private CommonProvider commonProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<FieldworkApplyRecord, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<FieldworkApplyRecord> lambdaQuery = this.lambdaQuery().in(FieldworkApplyRecord::getId, ids);
        if (flag) {
            lambdaQuery.select(FieldworkApplyRecord::getId, sValue);
        }
        Stream<FieldworkApplyRecord> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(FieldworkApplyRecord::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(FieldworkApplyRecord::getId, Function.identity()));
        }
        return result;
    }

    @Id2objHandle
    @Override
    public FieldworkApplyRecord wrapApplyReq(FieldworkApplyRecordReqDTO reqDTO) {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        AssertUtil.requireTrueOrParamExp(StrUtil.isNotBlank(employeeId));
        EmployeeInfoVO employeeInfo = SystemUtil.getInstance().getEmployeeInfo(employeeId);
        FieldworkApplyRecord entity = MyBeanUtils.copy(reqDTO, FieldworkApplyRecord::new);
        entity.setParkOrgId(employeeInfo.getParkOrgId());
        entity.setCompanyId(employeeInfo.getCompanyId());
        entity.setEmployeeName(employeeInfo.getName());
        entity.setMobile(SystemUtil.getInstance().getWxUserMobile());
        entity.setEmployeeId(employeeId);
        entity.setState(DictConstant.FieldworkApplyState.TO_BE_AUDITED.getCode());
        EntityUserUtil.setAllDynamic(entity);
        return entity;
    }

    @Override
    public Boolean cancel(String id) {
        FieldworkApplyRecord fieldworkApplyRecord = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AssertUtil.requireTrue(DictConstant.FieldworkApplyState.TO_BE_AUDITED.getCode() == fieldworkApplyRecord.getState(), "500003");
        fieldworkApplyRecord.setState(DictConstant.FieldworkApplyState.CANCEL.getCode());
        EntityUserUtil.setUpdateBy(fieldworkApplyRecord);
        fieldworkApplyRecord.setUpdateTime(LocalDateTime.now());
        return this.updateById(fieldworkApplyRecord);
    }

    @Override
    public Boolean audit(AuditReqDTO reqDTO) {
        FieldworkApplyRecord fieldworkApplyRecord = Optional.ofNullable(this.getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        AssertUtil.requireTrue(DictConstant.FieldworkApplyState.TO_BE_AUDITED.getCode() == fieldworkApplyRecord.getState(), "500003");
        fieldworkApplyRecord.setState(reqDTO.getApproval() ? DictConstant.FieldworkApplyState.APPROVE.getCode() : DictConstant.FieldworkApplyState.REJECTED.getCode());
        fieldworkApplyRecord.setAuditOpinion(reqDTO.getAuditOpinion());
        EntityUserUtil.setUpdateBy(fieldworkApplyRecord);
        fieldworkApplyRecord.setUpdateTime(LocalDateTime.now());
        return this.updateById(fieldworkApplyRecord);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public FieldworkApplyRecordDTO getFieldworkApplyRecordById(String id) {
        FieldworkApplyRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<FieldworkApplyRecordDTO> listFieldworkApplyRecordPage(FieldworkApplyRecordQuery query) {
        query.startPage();
        List<FieldworkApplyRecord> list = list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<FieldworkApplyRecordDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public void export(FieldworkApplyRecordQuery query) {
        LambdaQueryWrapper<FieldworkApplyRecord> conditionWrapper = getConditionWrapper(query);
        ExcelExportUtils.export(() -> list(conditionWrapper), this::entitiesToExportDTOs, FILE_NAME, FieldworkApplyRecordExportDTO.class);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<FieldworkApplyRecord> getConditionWrapper(FieldworkApplyRecordQuery query) {
        LambdaQueryWrapper<FieldworkApplyRecord> queryWrapper = new LambdaQueryWrapper<FieldworkApplyRecord>();
        queryWrapper
                .in(!USER_WX_SOURCE_B.equals(SystemUtil.getInstance().getSource()) &&
                        !USER_WX_SOURCE_C.equals(SystemUtil.getInstance().getSource()), FieldworkApplyRecord::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds())
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), FieldworkApplyRecord::getParkOrgId, query.getParkOrgId())
                .in(Objects.nonNull(query.getAudited()) && Constants.ZERO == query.getAudited(), FieldworkApplyRecord::getState, DictConstant.FieldworkApplyState.TO_BE_AUDITED.getCode())
                .in(Objects.nonNull(query.getAudited()) && Constants.ONE == query.getAudited(), FieldworkApplyRecord::getState, DictConstant.FieldworkApplyState.APPROVE.getCode(), DictConstant.FieldworkApplyState.REJECTED.getCode())
                .eq(StringUtils.isNotBlank(query.getCompanyId()), FieldworkApplyRecord::getCompanyId, query.getCompanyId())
                .like(StringUtils.isNotBlank(query.getEmployeeName()), FieldworkApplyRecord::getEmployeeName, query.getEmployeeName())
                .ge(Objects.nonNull(query.getApplyStartTime()), FieldworkApplyRecord::getCreateTime, query.getApplyStartTime())
                .le(Objects.nonNull(query.getApplyEndTime()), FieldworkApplyRecord::getCreateTime, query.getApplyEndTime())
                .and(Objects.nonNull(query.getFieldworkStartTime()) && Objects.nonNull(query.getFieldworkEndTime())
                        , condition1 -> condition1.le(FieldworkApplyRecord::getFieldworkStartTime, query.getFieldworkStartTime()).ge(FieldworkApplyRecord::getFieldworkEndTime, query.getFieldworkStartTime())
                                .or(condition2 -> condition2.le(FieldworkApplyRecord::getFieldworkStartTime, query.getFieldworkEndTime()).ge(FieldworkApplyRecord::getFieldworkEndTime, query.getFieldworkEndTime()))
                                .or(condition3 -> condition3.le(FieldworkApplyRecord::getFieldworkStartTime, query.getFieldworkStartTime()).ge(FieldworkApplyRecord::getFieldworkEndTime, query.getFieldworkEndTime()))
                                .or(condition4 -> condition4.ge(FieldworkApplyRecord::getFieldworkStartTime, query.getFieldworkStartTime()).le(FieldworkApplyRecord::getFieldworkEndTime, query.getFieldworkEndTime()))
                )
                .eq(Objects.nonNull(query.getState()), FieldworkApplyRecord::getState, query.getState())
                .eq(USER_WX_SOURCE_C.equals(SystemUtil.getInstance().getSource()), FieldworkApplyRecord::getCreateBy, SystemUtil.getInstance().getCurrentUserId())
                .orderByDesc(FieldworkApplyRecord::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<FieldworkApplyRecordDTO> entitiesToDTOs(List<FieldworkApplyRecord> entities) {
        return MyBeanUtils.copyList(entities, FieldworkApplyRecordDTO::new, (s, t) -> {

        });
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<FieldworkApplyRecordExportDTO> entitiesToExportDTOs(List<FieldworkApplyRecord> entities) {
        return MyBeanUtils.copyList(entities, FieldworkApplyRecordExportDTO::new, (s, t) -> {
            t.setStateName(DictConstant.FieldworkApplyState.getFieldworkApplyStateByCode(s.getState()));
            t.setFieldworkTimeStr(s.getFieldworkStartTime().format(DateTimeFormatter.ofPattern(Constants.TIME_PATTERN))
                    + "-" + s.getFieldworkEndTime().format(DateTimeFormatter.ofPattern(Constants.TIME_PATTERN)));
        });
    }

    @Override
    public Boolean authority() {
        Set<Integer> jobCodes = SystemUtil.getInstance().getEmployeeJobCodes();
        return jobCodes.contains(JobCodeEnum.ADMINISTRATOR_JOB_CODE.code) || jobCodes.contains(JobCodeEnum.ADMINISTRATION_JOB_CODE.code);
    }
}
