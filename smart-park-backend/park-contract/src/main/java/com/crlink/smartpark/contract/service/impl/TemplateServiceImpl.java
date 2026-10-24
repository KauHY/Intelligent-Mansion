package com.crlink.smartpark.contract.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.dto.TemplateDTO;
import com.crlink.smartpark.contract.entity.Template;
import com.crlink.smartpark.contract.mapper.TemplateMapper;
import com.crlink.smartpark.contract.model.TemplateReqDTO;
import com.crlink.smartpark.contract.model.query.TemplateQuery;
import com.crlink.smartpark.contract.service.TemplateService;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 合同模板管理 服务实现类
 * */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Template, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<Template> lambdaQuery = this.lambdaQuery().in(Template::getId, ids);
        if (flag) {
            lambdaQuery.select(Template::getId, sValue);
        }
        Stream<Template> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(Template::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(Template::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateTemplate(TemplateReqDTO reqDTO) {
        Template entity = MyBeanUtils.copy(reqDTO, Template::new);
        int count = this.lambdaQuery().eq(Template::getTemplateName, reqDTO.getTemplateName()).eq(Template::getParkOrgId, reqDTO.getParkOrgId()).eq(Template::getType, reqDTO.getType()).count();
        if (count > 0) {
            throw new ResolinkRuntimeException("300010");
        }
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public TemplateDTO getTemplateById(String id) {
        Template entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<TemplateDTO> listTemplatePage(TemplateQuery query) {
        query.startPage();
        List<Template> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<TemplateDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<Template> getConditionWrapper(TemplateQuery query) {
        LambdaQueryWrapper<Template> queryWrapper = new LambdaQueryWrapper<Template>();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        queryWrapper.eq(query.getType() != null, Template::getType, query.getType());
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), Template::getParkOrgId, query.getParkOrgId())
                .in(Template::getParkOrgId, parkOrgIds);
        queryWrapper.like(StringUtils.isNotBlank(query.getTemplateName()), Template::getTemplateName,
                query.getTemplateName());
        queryWrapper.orderByDesc(Template::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @return DTO集合
     */
    private List<TemplateDTO> entitiesToDTOs(List<Template> entities) {
        return MyBeanUtils.copyList(entities, TemplateDTO::new, (s, t) -> {

        });
    }
}
