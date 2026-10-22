package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.vo.CompanyVO;
import com.crlink.smartpark.visitor.dto.AuditPersonConfigSaveDTO;
import com.crlink.smartpark.visitor.dto.AuditPersonConfigUpdateDTO;
import com.crlink.smartpark.visitor.entity.AuditPersonConfig;
import com.crlink.smartpark.visitor.mapper.AuditPersonConfigMapper;
import com.crlink.smartpark.visitor.query.AuditPersonConfigListQuery;
import com.crlink.smartpark.visitor.service.AuditPersonConfigService;
import com.crlink.smartpark.visitor.vo.AuditPersonConfigListVO;
import com.crlink.smartpark.visitor.vo.AuditPersonConfigVO;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 访客审批人配置服务实现类
 * */
@Service
public class AuditPersonConfigServiceImpl extends ServiceImpl<AuditPersonConfigMapper, AuditPersonConfig> implements AuditPersonConfigService {

    @Resource
    private CompanyProvider companyProvider;

    @Resource
    private EmployeeProvider employeeProvider;

    @Override
    public void save(AuditPersonConfigSaveDTO dto) {
        // TODO: 2022/3/18 这里需要前端必传
        //if (dto.getInvitorEmployeeAudit() == null) {
        //    dto.setInvitorEmployeeAudit(DictUtil.getValue(DictConstant.YesOrNo.YES));
        //}
        AssertUtil.requireNotNullOrParamExp(dto.getInvitorEmployeeAudit());
        CompanyVO companyVO = companyProvider.findById(dto.getCompanyId()).getData();
        AssertUtil.requireNotNullOrParamExp(companyVO);
        //校验是否已配置
        LambdaQueryWrapper<AuditPersonConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditPersonConfig::getCompanyId, dto.getCompanyId());
        AssertUtil.requireTrue(count(wrapper) <= 0, "70007");
        AuditPersonConfig auditPersonConfig = new AuditPersonConfig();
        auditPersonConfig.setCompanyId(dto.getCompanyId());
        auditPersonConfig.setParkOrgId(companyVO.getParkOrgId());
        auditPersonConfig.setInvitorEmployeeAudit(dto.getInvitorEmployeeAudit());
        EntityUserUtil.setAll(auditPersonConfig);
        if (DictUtil.getValue(DictConstant.YesOrNo.NO).equals(dto.getInvitorEmployeeAudit())) {
            AssertUtil.requireTrueOrParamExp(CollUtil.isNotEmpty(dto.getAuditEmployeeIds()));
            String employeeIds = StrUtil.join(",", dto.getAuditEmployeeIds());
            auditPersonConfig.setAuditEmployeeIds(employeeIds);
        } else if (DictUtil.getValue(DictConstant.YesOrNo.YES).equals(dto.getInvitorEmployeeAudit())) {
            auditPersonConfig.setAuditEmployeeIds(StrUtil.EMPTY);
        }
        save(auditPersonConfig);
    }

    @Override
    public void update(AuditPersonConfigUpdateDTO dto) {
        // TODO: 2022/3/18 这里需要前端必传
        //if (dto.getInvitorEmployeeAudit() == null) {
        //    dto.setInvitorEmployeeAudit(DictUtil.getValue(DictConstant.YesOrNo.YES));
        //}
        AssertUtil.requireNotNullOrParamExp(dto.getInvitorEmployeeAudit());
        CompanyVO companyVO = companyProvider.findById(dto.getCompanyId()).getData();
        AssertUtil.requireNotNullOrParamExp(companyVO);
        AuditPersonConfig entity = getById(dto.getId());
        AssertUtil.requireNotNullOrParamExp(entity);
        //校验是否已配置
        LambdaQueryWrapper<AuditPersonConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditPersonConfig::getCompanyId, dto.getCompanyId());
        wrapper.ne(AuditPersonConfig::getId, dto.getId());
        AssertUtil.requireTrue(count(wrapper) <= 0, "70007");
        AuditPersonConfig auditPersonConfig = MyBeanUtils.copy(dto, AuditPersonConfig::new);
        if (DictUtil.getValue(DictConstant.YesOrNo.NO).equals(dto.getInvitorEmployeeAudit())) {
            AssertUtil.requireTrueOrParamExp(CollUtil.isNotEmpty(dto.getAuditEmployeeIds()));
            String employeeIds = StrUtil.join(",", dto.getAuditEmployeeIds());
            auditPersonConfig.setAuditEmployeeIds(employeeIds);
        } else if (DictUtil.getValue(DictConstant.YesOrNo.YES).equals(dto.getInvitorEmployeeAudit())) {
            auditPersonConfig.setAuditEmployeeIds(StrUtil.EMPTY);
        }
        auditPersonConfig.setParkOrgId(companyVO.getParkOrgId());
        EntityUserUtil.setUpdateBy(auditPersonConfig);
        AssertUtil.requireTrueOrDataExp(updateById(auditPersonConfig));
    }

    @Override
    public void removeAuditPersonConfigById(String id) {
        AssertUtil.requireTrueOrDataExp(removeById(id));
    }

    @Override
    @Id2objHandle
    public AuditPersonConfigVO getAuditPersonConfigById(String id) {
        AuditPersonConfig auditPersonConfig = getById(id);
        AuditPersonConfigVO vo = MyBeanUtils.copy(auditPersonConfig, AuditPersonConfigVO::new, (s, t) -> {
            if (StrUtil.isNotBlank(s.getAuditEmployeeIds())) {
                Map<String, String> employeeNames = employeeProvider.findEmployeeNamesByIds(s.getAuditEmployeeIds().split(","));
                t.setAuditEmployeeNames(StrUtil.join(",", employeeNames.values()));
            }
        });
        return vo;
    }

    @Override
    @Id2objHandle
    public PageInfo<AuditPersonConfigListVO> pageList(AuditPersonConfigListQuery query) {
        LambdaQueryWrapper<AuditPersonConfig> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getCompanyName())) {
            ResponseResult<Set<String>> result = companyProvider.findIdsByName(query.getCompanyName());
            AssertUtil.requireApiSuccess(result);
            Set<String> companyIds = result.getData();
            if (CollectionUtil.isEmpty(companyIds)) {
                return new PageInfo();
            } else {
                wrapper.in(AuditPersonConfig::getCompanyId, companyIds);
            }
        }
        wrapper.orderByDesc(AuditPersonConfig::getUpdateTime);
        wrapper.in(AuditPersonConfig::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrRetEmpty());
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), AuditPersonConfig::getParkOrgId, query.getParkOrgId());
        query.startPage();
        List<AuditPersonConfig> list = list(wrapper);
        List<String> employeeIds = new ArrayList<>();
        list.forEach(e -> {
            if (StrUtil.isNotBlank(e.getAuditEmployeeIds())) {
                employeeIds.addAll(Arrays.asList(e.getAuditEmployeeIds().split(",")));
            }
        });
        Map<String, String> employeeNames = employeeProvider.findEmployeeNamesByIds(ArrayUtil.toArray(employeeIds, String.class));
        List<AuditPersonConfigListVO> voList = MyBeanUtils.copyList(list, AuditPersonConfigListVO::new, (s, t) -> {
            if (StrUtil.isNotBlank(s.getAuditEmployeeIds())) {
                String[] ids = s.getAuditEmployeeIds().split(",");
                List<String> employeeNameList = Arrays.stream(ids).map(employeeNames::get).collect(Collectors.toList());
                t.setAuditEmployeeNames(StrUtil.join(",", employeeNameList));
            }
        });
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(voList);
        return pageInfo;
    }

}
