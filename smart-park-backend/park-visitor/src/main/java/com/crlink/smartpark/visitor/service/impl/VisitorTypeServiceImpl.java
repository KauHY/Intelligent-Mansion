package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.feign.CompanyProvider;
import com.crlink.smartpark.system.vo.CompanyVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.dto.VisitorTypeDTO;
import com.crlink.smartpark.visitor.entity.ProcessConfig;
import com.crlink.smartpark.visitor.entity.VisitorRecord;
import com.crlink.smartpark.visitor.entity.VisitorType;
import com.crlink.smartpark.visitor.mapper.VisitorTypeMapper;
import com.crlink.smartpark.visitor.model.dto.VisitorTypeReqDTO;
import com.crlink.smartpark.visitor.model.query.VisitorTypeQuery;
import com.crlink.smartpark.visitor.service.ProcessConfigService;
import com.crlink.smartpark.visitor.service.VisitorRecordService;
import com.crlink.smartpark.visitor.service.VisitorTypeService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 访客类型 服务实现类
 * */
@Service
public class VisitorTypeServiceImpl extends ServiceImpl<VisitorTypeMapper, VisitorType> implements VisitorTypeService {
    @Resource
    private CompanyProvider companyProvider;
    @Resource
    private VisitorRecordService visitorRecordService;
    @Resource
    private ProcessConfigService processConfigService;

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateVisitorType(VisitorTypeReqDTO reqDTO) {
        checkVisitorNameDuplicate(reqDTO);
        VisitorType entity = MyBeanUtils.copy(reqDTO, VisitorType::new);
        ResponseResult<CompanyVO> companyResult = companyProvider.findById(reqDTO.getCompanyId());
        AssertUtil.requireApiSuccess(companyResult);
        CompanyVO companyVO = companyResult.getData();
        AssertUtil.requireNotNullOrParamExp(companyVO);
        entity.setParkOrgId(companyVO.getParkOrgId());
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 检查访客类型重复
     *
     * @param reqDTO
     */
    private void checkVisitorNameDuplicate(VisitorTypeReqDTO reqDTO) {
        LambdaQueryWrapper<VisitorType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VisitorType::getCompanyId, reqDTO.getCompanyId());
        wrapper.eq(VisitorType::getVisitorTypeName, reqDTO.getVisitorTypeName());
        VisitorType dbVisitorType = getOne(wrapper, false);
        AssertUtil.requireTrue(dbVisitorType == null || dbVisitorType.getId().equals(reqDTO.getId()), "70003");
    }

    /**
     * 通过id查询实体
     */
    @Override
    public VisitorTypeDTO getVisitorTypeById(String id) {
        VisitorType entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<VisitorTypeDTO> listVisitorTypePage(VisitorTypeQuery query) {
        LambdaQueryWrapper<VisitorType> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByDesc(VisitorType::getUpdateTime);
        queryWrapper.in(VisitorType::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrRetEmpty());
        queryWrapper.eq(StrUtil.isNotBlank(query.getCompanyId()), VisitorType::getCompanyId, query.getCompanyId());
        queryWrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), VisitorType::getParkOrgId, query.getParkOrgId());
        queryWrapper.like(StrUtil.isNotBlank(query.getVisitorTypeName()), VisitorType::getVisitorTypeName, query.getVisitorTypeName());
        query.startPage();
        List<VisitorType> list = list(queryWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        PageInfo pageInfo = new PageInfo<>(list);
        List<VisitorTypeDTO> result = entitiesToDTOs(list);
        pageInfo.setList(result);
        return pageInfo;
    }

    @Override
    public Boolean removeVisitorTypeById(String id) {
        //关联校验
        LambdaQueryWrapper<ProcessConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessConfig::getVisitorTypeId, id);
        AssertUtil.requireTrue(processConfigService.count(wrapper) <= 0, "70019");
        //校验是否关联了访客
        LambdaQueryWrapper<VisitorRecord> visitorRecordWrapper = new LambdaQueryWrapper<>();
        visitorRecordWrapper.eq(VisitorRecord::getVisitorTypeId, id);
        AssertUtil.requireTrue(visitorRecordService.count(visitorRecordWrapper) <= 0, "70006");
        return removeById(id);
    }

    @Override
    public List<DictVO> listVisitorTypeByCompany(String companyId) {
        LambdaQueryWrapper<VisitorType> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(StrUtil.isNotBlank(companyId), VisitorType::getCompanyId, companyId);
        queryWrapper.orderByDesc(VisitorType::getUpdateTime);
        List<DictVO> list = list(queryWrapper).stream().map(e -> new DictVO(e.getVisitorTypeName(), e.getId())).collect(Collectors.toList());
        return list;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<VisitorTypeDTO> entitiesToDTOs(List<VisitorType> entities) {
        return MyBeanUtils.copyList(entities, VisitorTypeDTO::new, (s, t) -> {
        });
    }
}
