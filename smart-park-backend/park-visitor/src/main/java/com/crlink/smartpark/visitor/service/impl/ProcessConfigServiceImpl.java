package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.vo.CompanyVO;
import com.crlink.smartpark.visitor.model.dto.ProcessConfigReqDTO;
import com.crlink.smartpark.visitor.model.dto.ProcessConfigStatusReqDTO;
import com.crlink.smartpark.visitor.model.query.ProcessConfigQuery;
import com.crlink.smartpark.visitor.dto.ProcessConfigDTO;
import com.crlink.smartpark.visitor.entity.ProcessConfig;
import com.crlink.smartpark.visitor.mapper.ProcessConfigMapper;
import com.crlink.smartpark.visitor.service.ProcessConfigService;
import com.crlink.smartpark.visitor.service.VisitorTypeService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * 访客流程配置 服务实现类
 * */
@Service
public class ProcessConfigServiceImpl extends ServiceImpl<ProcessConfigMapper, ProcessConfig> implements ProcessConfigService {

    @Resource
    private CompanyProvider companyProvider;

    @Resource
    private VisitorTypeService visitorTypeService;


    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateProcessConfig(ProcessConfigReqDTO reqDTO) {
        checkSaveOrUpdateProcessConfigParams(reqDTO);
        checkProcessConfigDuplicate(reqDTO);
        ResponseResult<CompanyVO> companyResult = companyProvider.findById(reqDTO.getCompanyId());
        AssertUtil.requireApiSuccess(companyResult);
        CompanyVO companyVO = companyResult.getData();
        AssertUtil.requireNotNullOrParamExp(companyVO);
        ProcessConfig entity = MyBeanUtils.copy(reqDTO, ProcessConfig::new);
        entity.setParkOrgId(companyVO.getParkOrgId());
        entity.setEnable(DictUtil.getValue(DictConstant.YesOrNo.YES));
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 检查参数合法
     *
     * @param reqDTO
     */
    private void checkSaveOrUpdateProcessConfigParams(ProcessConfigReqDTO reqDTO) {
        //预约途径,非邀请人审核情况下必须指定一个审核人
        AssertUtil.requireNotNullOrParamExp(visitorTypeService.getById(reqDTO.getVisitorTypeId()));
        String[] nodes = reqDTO.getProcessNodes().split(",");
        Arrays.stream(nodes).forEach(e -> {
            Integer nodeIntVal = Integer.valueOf(e);
            if (DictUtil.getValue(DictConstant.VisitSourceType.SUBSCRIBE).equals(reqDTO.getProcessType())) {
                if (!nodeIntVal.equals(DictUtil.getValue(DictConstant.VisitProcessNode.VISIT_SUBSCRIBE)) &&
                        !nodeIntVal.equals(DictUtil.getValue(DictConstant.VisitProcessNode.AUDIT))) {
                    throw new ResolinkRuntimeException("70005");
                }
            } else if (DictUtil.getValue(DictConstant.VisitSourceType.INVITE).equals(reqDTO.getProcessType())) {
                if (!nodeIntVal.equals(DictUtil.getValue(DictConstant.VisitProcessNode.INVITE)) &&
                        !nodeIntVal.equals(DictUtil.getValue(DictConstant.VisitProcessNode.COMPLETE_INFO))) {
                    throw new ResolinkRuntimeException("70005");
                }
            } else {
                throw new ResolinkRuntimeException("70005");
            }
        });
    }

    /**
     * 检查访客流程类型重复
     *
     * @param reqDTO
     */
    private void checkProcessConfigDuplicate(ProcessConfigReqDTO reqDTO) {
        LambdaQueryWrapper<ProcessConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessConfig::getCompanyId, reqDTO.getCompanyId());
        //wrapper.and(e -> e.eq(ProcessConfig::getProcessName, reqDTO.getProcessName())
        //        .or(e1 -> e1.eq(ProcessConfig::getProcessType, reqDTO.getProcessType())));
        wrapper.eq(ProcessConfig::getProcessName, reqDTO.getProcessName());
        wrapper.ne(reqDTO.getId() != null, ProcessConfig::getId, reqDTO.getId());
        AssertUtil.requireTrue(this.count(wrapper) == 0, "70004");
        //添加2022-08-15
        wrapper.clear();
        wrapper.eq(ProcessConfig::getCompanyId, reqDTO.getCompanyId());
        wrapper.eq(ProcessConfig::getProcessType, reqDTO.getProcessType());
        wrapper.eq(ProcessConfig::getVisitorTypeId, reqDTO.getVisitorTypeId());
        wrapper.ne(reqDTO.getId() != null, ProcessConfig::getId, reqDTO.getId());
        AssertUtil.requireTrue(this.count(wrapper) == 0, "70004");
    }

    /**
     * 通过id查询实体
     */
    @Override
    public ProcessConfigDTO getProcessConfigById(String id) {
        ProcessConfig entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<ProcessConfigDTO> listProcessConfigPage(ProcessConfigQuery query) {
        query.startPage();
        LambdaQueryWrapper<ProcessConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ProcessConfig::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrRetEmpty());
        wrapper.like(StrUtil.isNotBlank(query.getProcessName()), ProcessConfig::getProcessName, query.getProcessName());
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), ProcessConfig::getParkOrgId, query.getParkOrgId());
        wrapper.orderByDesc(ProcessConfig::getUpdateTime);
        List<ProcessConfig> list = list(wrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<ProcessConfigDTO> result = entitiesToDTOs(list);
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(result);
        return pageInfo;
    }

    @Override
    public Boolean modifyStatus(ProcessConfigStatusReqDTO dto) {
        LambdaUpdateWrapper<ProcessConfig> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(ProcessConfig::getEnable, dto.getEnable());
        wrapper.eq(ProcessConfig::getId, dto.getId());
        return update(wrapper);
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ProcessConfigDTO> entitiesToDTOs(List<ProcessConfig> entities) {
        return MyBeanUtils.copyList(entities, ProcessConfigDTO::new, (s, t) -> {

        });
    }
}
