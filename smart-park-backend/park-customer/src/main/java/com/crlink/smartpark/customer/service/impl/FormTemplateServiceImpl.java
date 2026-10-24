package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.dto.EditorDTO;
import com.crlink.smartpark.customer.dto.EditorsDTO;
import com.crlink.smartpark.customer.dto.FormTemplateDTO;
import com.crlink.smartpark.customer.entity.Editor;
import com.crlink.smartpark.customer.entity.Form;
import com.crlink.smartpark.customer.entity.FormTemplate;
import com.crlink.smartpark.customer.mapper.FormTemplateMapper;
import com.crlink.smartpark.customer.model.FormTemplateReqDTO;
import com.crlink.smartpark.customer.model.query.FormTemplateQuery;
import com.crlink.smartpark.customer.service.EditorService;
import com.crlink.smartpark.customer.service.FormService;
import com.crlink.smartpark.customer.service.FormTemplateService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 表单模板 服务实现类
 * */
@Service
public class FormTemplateServiceImpl extends ServiceImpl<FormTemplateMapper, FormTemplate> implements FormTemplateService {

    @Autowired
    private EditorService editorService;

    @Autowired
    private FormService formService;

    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<FormTemplate, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(FormTemplate::getId, sValue)
                .in(FormTemplate::getId, ids).list().parallelStream().filter(t -> Objects.nonNull(sValue.apply(t)))
                .collect(Collectors.toMap(FormTemplate::getId, sValue));
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateFormTemplate(FormTemplateReqDTO reqDTO) {
        FormTemplate entity = MyBeanUtils.copy(reqDTO, FormTemplate::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public FormTemplateDTO getFormTemplateById(String id) {
        FormTemplate entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        FormTemplateDTO templateDTO = MyBeanUtils.copy(entity, FormTemplateDTO::new);
        List<String> editorIds = entity.getEditorIds();
        if (ObjectUtil.isNotEmpty(editorIds)) {
            List<Editor> editors = editorService.listByIds(editorIds);
            List<EditorDTO> editorDTOS = MyBeanUtils.copyList(editors, EditorDTO::new);
            templateDTO.setEditors(editorDTOS);
        }
        return templateDTO;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<FormTemplateDTO> listFormTemplatePage(FormTemplateQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.startPage();
        List<FormTemplate> list = this.list(getConditionWrapper(query, parkOrgIds));
        MyPageInfoUtils.isEmptyPage(list);
        List<FormTemplateDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    @Transactional
    public Boolean editFormTemplate(EditorsDTO reqDTO) {
        List<Editor> editors = MyBeanUtils.copyList(reqDTO.getEditors(), Editor::new, (s, t) -> {
            EntityUserUtil.setAllDynamic(t);
        });
        editorService.saveOrUpdateBatch(editors);
        List<String> editorIds = editors.parallelStream().map(Editor::getId).collect(Collectors.toList());
        FormTemplate formTemplate = new FormTemplate();
        formTemplate.setId(reqDTO.getId());
        formTemplate.setTitle(reqDTO.getTitle());
        formTemplate.setDescription(reqDTO.getDescription());
        formTemplate.setEditorIds(editorIds);
        formTemplate.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        return this.updateById(formTemplate);
    }

    @Override
    public Boolean removeFormTemplateById(String id) {
        List<Form> forms = formService.lambdaQuery().select(Form::getId).eq(Form::getTemplateId, id).list();
        if (ObjectUtil.isNotEmpty(forms)) {
            throw new ResolinkRuntimeException("800001");
        }
        return this.removeById(id);
    }

    @Override
    public List<DictVO> listFormTemplateDict(String parkOrgId) {
        List<FormTemplate> list = this.lambdaQuery().select(FormTemplate::getId, FormTemplate::getTitle)
                .eq(FormTemplate::getParkOrgId, parkOrgId)
                .orderByDesc(FormTemplate::getCreateTime)
                .list();
        List<DictVO> result = list.parallelStream().map(t -> new DictVO(t.getTitle(), t.getId())).collect(Collectors.toList());
        return result;
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<FormTemplate> getConditionWrapper(FormTemplateQuery query, Set<String> parkOrgIds) {
        LambdaQueryWrapper<FormTemplate> queryWrapper = new LambdaQueryWrapper<FormTemplate>();
        queryWrapper.select(FormTemplate::getId, FormTemplate::getParkOrgId, FormTemplate::getTitle, FormTemplate::getUpdateTime)
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), FormTemplate::getParkOrgId, query.getParkOrgId())
                .like(StringUtils.isNotBlank(query.getTitle()), FormTemplate::getTitle, query.getTitle())
                .in(FormTemplate::getParkOrgId, parkOrgIds)
                .orderByDesc(FormTemplate::getUpdateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<FormTemplateDTO> entitiesToDTOs(List<FormTemplate> entities) {
        return MyBeanUtils.copyList(entities, FormTemplateDTO::new, (s, t) -> {

        });
    }
}
