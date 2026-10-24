package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.constants.enums.VisitorRegisterTypeEnum;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.ExcelExportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.visitor.dto.VisitorRecordSubscribeDTO;
import com.crlink.smartpark.visitor.dto.VisitorSignOutDTO;
import com.crlink.smartpark.visitor.entity.VisitorRecord;
import com.crlink.smartpark.visitor.mapper.VisitorRecordMapper;
import com.crlink.smartpark.visitor.model.excel.VisitorRegisterExcelExportListVO;
import com.crlink.smartpark.visitor.query.VisitorRecordListQuery;
import com.crlink.smartpark.visitor.service.VisitorRecordService;
import com.crlink.smartpark.visitor.service.mobile.VisitorCommonService;
import com.crlink.smartpark.visitor.vo.FaceVerifyDevicePushOriginDataDTO;
import com.crlink.smartpark.visitor.vo.VisitorRecordSubScribeListVO;
import com.crlink.smartpark.visitor.vo.VisitorRecordVO;
import com.crlink.smartpark.visitor.vo.VisitorRegisterListVO;
import com.crlink.smartpark.visitor.vo.VisitorVerifyPushDataDTO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 访客记录服务实现类
 * */
@Slf4j
@Service
public class VisitorRecordServiceImpl extends ServiceImpl<VisitorRecordMapper, VisitorRecord> implements VisitorRecordService {

    @Resource
    private CompanyProvider companyProvider;

    @Resource
    private EmployeeProvider employeeProvider;

    @Resource
    private VisitorCommonService visitorCommonService;

    @Override
    @Transactional(rollbackFor = ResolinkRuntimeException.class)
    public void subscribe(VisitorRecordSubscribeDTO dto) {
        VisitorRecord record = getById(dto.getId());
        AssertUtil.requireNotNullOrParamExp(record);
        AssertUtil.requireTrue(record.getProcessStatus().equals(DictUtil.getValue(DictConstant.ProcessStatus.ONE)), "70002");
        BeanUtils.copyProperties(dto, record);
        record.setProcessStatus(DictUtil.getValue(DictConstant.ProcessStatus.FOUR));
        record.setSignInTime(LocalDateTime.now());
        record.setSignInType(DictUtil.getValue(DictConstant.RegisterType.ONSITE_REG));
        EntityUserUtil.setUpdateBy(record);
        AssertUtil.requireTrueOrDataExp(updateById(record));
    }

    @Override
    public void removeVisitorRecordById(String id) {
        AssertUtil.requireTrueOrDataExp(removeById(id));
    }

    @Override
    @Id2objHandle
    public VisitorRecordVO getVisitorRecordById(String id) {
        VisitorRecord visitorRecord = getById(id);
        if (visitorRecord == null) {
            return null;
        }
        VisitorRecordVO vo = MyBeanUtils.copy(visitorRecord, VisitorRecordVO::new);
        return vo;
    }

    @Override
    public PageInfo<VisitorRegisterListVO> pageListVisitorRegister(VisitorRecordListQuery query, VisitorRegisterTypeEnum visitorRegisterType) {
        LambdaQueryWrapper<VisitorRecord> wrapper = buildVisitorRecordLambdaQueryWrapper(query, visitorRegisterType);
        query.startPage();
        List<VisitorRecord> list = list(wrapper);
        List<VisitorRegisterListVO> voList = MyBeanUtils.copyList(list, VisitorRegisterListVO::new);
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(voList);
        return pageInfo;
    }

    private List<VisitorRegisterListVO> buildVisitorRegisterListVOS(List<VisitorRecord> list) {
        List<VisitorRegisterListVO> voList = MyBeanUtils.copyList(list, VisitorRegisterListVO::new, (s, t) -> {
            t.setSignInTypeLabel(DictUtil.getLabel(DictConstant.RegisterType.class, s.getSignInType()));
        });
        return voList;
    }

    private LambdaQueryWrapper<VisitorRecord> buildVisitorRecordLambdaQueryWrapper(VisitorRecordListQuery query, VisitorRegisterTypeEnum visitorRegisterType) {
        LambdaQueryWrapper<VisitorRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(VisitorRecord::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        LocalDateTime now = LocalDateTime.now();
        if (VisitorRegisterTypeEnum.CURRENT.equals(visitorRegisterType)) {
            wrapper.eq(VisitorRecord::getProcessStatus, DictUtil.getValue(DictConstant.ProcessStatus.FOUR));
            wrapper.or(e-> e.eq(VisitorRecord::getProcessStatus, DictUtil.getValue(DictConstant.ProcessStatus.FIVE)).ge(VisitorRecord::getSignOutTime, now));
        } else if (VisitorRegisterTypeEnum.HISTORY.equals(visitorRegisterType)) {
            wrapper.in(VisitorRecord::getProcessStatus, Lists.newArrayList(DictUtil.getValue(DictConstant.ProcessStatus.ONE)
                    ,DictUtil.getValue(DictConstant.ProcessStatus.FOUR),DictUtil.getValue(DictConstant.ProcessStatus.FIVE)));
            wrapper.lt(VisitorRecord::getVisitDate, now);
        } else {
            throw new ResolinkRuntimeException("10003");
        }
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), VisitorRecord::getParkOrgId, query.getParkOrgId());
        wrapper.eq(StrUtil.isNotBlank(query.getCompanyId()), VisitorRecord::getCompanyId, query.getCompanyId());
        wrapper.like(StringUtils.isNotBlank(query.getVisitorName()), VisitorRecord::getVisitorName, query.getVisitorName());
        wrapper.orderByDesc(VisitorRecord::getSignInTime);
        return wrapper;
    }

    @Override
    public PageInfo<VisitorRecordSubScribeListVO> pageListVisitorSubscribe(VisitorRecordListQuery query) {
        LambdaQueryWrapper<VisitorRecord> wrapper = buildVisitorRecordLambdaQueryWrapper(query);
        query.startPage();
        List<VisitorRecord> list = list(wrapper);
        List<VisitorRecordSubScribeListVO> voList = MyBeanUtils.copyList(list, VisitorRecordSubScribeListVO::new);
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(voList);
        return pageInfo;
    }

    private LambdaQueryWrapper<VisitorRecord> buildVisitorRecordLambdaQueryWrapper(VisitorRecordListQuery query) {
        LambdaQueryWrapper<VisitorRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(VisitorRecord::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        wrapper.eq(VisitorRecord::getProcessStatus, DictUtil.getValue(DictConstant.ProcessStatus.ONE));
        wrapper.ge(VisitorRecord::getVisitDate, LocalDate.now());
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), VisitorRecord::getParkOrgId, query.getParkOrgId());
        wrapper.eq(StrUtil.isNotBlank(query.getCompanyId()), VisitorRecord::getCompanyId, query.getCompanyId());
        wrapper.like(StringUtils.isNotBlank(query.getVisitorName()), VisitorRecord::getVisitorName, query.getVisitorName());
        wrapper.orderByDesc(VisitorRecord::getVisitDate);
        return wrapper;
    }

    @Override
    public void exportVisitorRegister(VisitorRecordListQuery query, VisitorRegisterTypeEnum type) {
        LambdaQueryWrapper<VisitorRecord> wrapper = buildVisitorRecordLambdaQueryWrapper(query, type);
        ExcelExportUtils.export(() -> list(wrapper),
                sourceList -> MyBeanUtils.copyList(buildVisitorRegisterListVOS(sourceList), VisitorRegisterExcelExportListVO::new), VisitorRegisterExcelExportListVO.class);
    }

    @Override
    public void exportVisitorSubscribe(VisitorRecordListQuery query) {
        LambdaQueryWrapper<VisitorRecord> wrapper = buildVisitorRecordLambdaQueryWrapper(query);
        ExcelExportUtils.export(() -> list(wrapper),
                sourceList -> MyBeanUtils.copyList(buildVisitorRegisterListVOS(sourceList), VisitorRegisterExcelExportListVO::new), VisitorRegisterExcelExportListVO.class);
    }

    @Override
    public VisitorVerifyPushDataDTO faceVerifyDeviceDataPush(FaceVerifyDevicePushOriginDataDTO dto) {
        // TODO: 2022/3/17 websocket 推送数据
        return null;
    }

    @Override
    public VisitorRecordVO getByPersonId(Long devicePersonId) {
        VisitorRecord visitorRecord = lambdaQuery().select(VisitorRecord::getAccessPolicyId, VisitorRecord::getCardNo, VisitorRecord::getDevicePersonId).eq(VisitorRecord::getDevicePersonId, devicePersonId).one();
        return MyBeanUtils.copy(visitorRecord, VisitorRecordVO::new);
    }

    /**
     * 更新门禁核验状态
     *
     * @param devicePersonId
     */
    @Override
    public void updateDoorAccessStatus(Long devicePersonId) {
        LambdaUpdateWrapper<VisitorRecord> uw = new LambdaUpdateWrapper<>();
        uw.set(VisitorRecord::getSignInType, DictUtil.getValue(DictConstant.RegisterType.DOOR_ACCESS));
        uw.set(VisitorRecord::getSignInTime, LocalDateTime.now());
        uw.eq(VisitorRecord::getDevicePersonId, devicePersonId);
        this.update(uw);
    }

    /**
     * 该访客信息失效，转为历史访客
     *
     * @param signOutDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = ResolinkRuntimeException.class)
    public void signOut(VisitorSignOutDTO signOutDTO) {
        VisitorRecord record = getById(signOutDTO.getId());
        AssertUtil.requireNotNullOrParamExp(record);
        VisitorRecord updateRecord = new VisitorRecord();
        updateRecord.setId(signOutDTO.getId());
        updateRecord.setProcessStatus(DictUtil.getValue(DictConstant.ProcessStatus.FIVE));
        EntityUserUtil.setUpdateBy(updateRecord);
        LocalDateTime signOutTime = signOutDTO.getSignOutTime();
        if (signOutTime == null) {
            log.info("访客{}立即签出", record.getId());
            updateRecord.setSignOutTime(LocalDateTime.now());
            AssertUtil.requireTrueOrDataExp(updateById(updateRecord));
            visitorCommonService.removeVisitorDeviceAuth(record);
        } else {
            log.info("访客{}自定义签出{}", record.getId(), signOutTime);
            AssertUtil.requireTrue(signOutTime.compareTo(record.getSignOutTime()) > 0, "70023");
            updateRecord.setSignOutTime(signOutTime);
            AssertUtil.requireTrueOrDataExp(updateById(updateRecord));
            record.setSignOutTime(signOutTime);
            visitorCommonService.updateVisitorCard(record);
        }
    }

}
