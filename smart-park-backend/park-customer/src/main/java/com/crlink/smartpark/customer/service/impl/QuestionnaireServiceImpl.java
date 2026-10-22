package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.controller.request.EditorResultQuery;
import com.crlink.smartpark.customer.controller.response.QuestionnaireAnalysisReportDTO;
import com.crlink.smartpark.customer.controller.response.QuestionnaireDataVO;
import com.crlink.smartpark.customer.controller.response.editor.EditorAnalysisDTO;
import com.crlink.smartpark.customer.controller.response.editor.EditorAnalysisDetailDTO;
import com.crlink.smartpark.customer.controller.response.editor.EditorChoiceAnalysisDTO;
import com.crlink.smartpark.customer.controller.response.editor.EditorChoiceItemDTO;
import com.crlink.smartpark.customer.dto.EditorDTO;
import com.crlink.smartpark.customer.dto.EditorsDTO;
import com.crlink.smartpark.customer.dto.QuestionnaireDTO;
import com.crlink.smartpark.customer.entity.Editor;
import com.crlink.smartpark.customer.entity.Questionnaire;
import com.crlink.smartpark.customer.entity.QuestionnaireSubmission;
import com.crlink.smartpark.customer.mapper.QuestionnaireMapper;
import com.crlink.smartpark.customer.model.QuestionnaireReqDTO;
import com.crlink.smartpark.customer.model.dto.QuestionnaireUpdateStatusReqDTO;
import com.crlink.smartpark.customer.model.query.QuestionnaireQuery;
import com.crlink.smartpark.customer.model.vo.QuestionnaireVO;
import com.crlink.smartpark.customer.service.EditorService;
import com.crlink.smartpark.customer.service.QuestionnaireService;
import com.crlink.smartpark.customer.service.QuestionnaireSubmissionService;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.LocalDateTime_MAX;
import static com.crlink.smartpark.customer.common.Constant.EditorElementTypeEnum.*;
import static com.crlink.smartpark.customer.common.QuestionnaireConstant.ANSWER_DETAILS_FILE_NAME;
import static com.crlink.smartpark.customer.common.QuestionnaireConstant.EmployeeAnsweredStatusEnum.*;
import static com.crlink.smartpark.customer.common.QuestionnaireConstant.QuestionnaireStatusEnum.COLLECTING;
import static com.crlink.smartpark.customer.common.QuestionnaireConstant.QuestionnaireStatusEnum.END;


/**
 * 问卷 服务实现类
 * */
@Service
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireMapper, Questionnaire> implements QuestionnaireService {


    @Autowired
    private QuestionnaireSubmissionService questionnaireSubmissionService;

    @Autowired
    private EditorService editorService;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private FileProvider fileProvider;

    @Override
    public Boolean saveOrUpdateQuestionnaire(QuestionnaireReqDTO reqDTO) {
        if (Objects.isNull(reqDTO.getDeadlineTime())) {
            reqDTO.setDeadlineTime(LocalDateTime_MAX);
        }
        Questionnaire entity = MyBeanUtils.copy(reqDTO, Questionnaire::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    @Override
    @Transactional
    public Boolean copy(String id) {
        Questionnaire questionnaire = this.getById(id);
        List<String> editorIds = questionnaire.getEditorIds();
        List<Editor> editors = editorService.getBaseMapper().selectBatchIds(editorIds);
        editors.forEach(editor -> {
            editor.setId(null);
            EntityUserUtil.setAll(editor);
        });
        editorService.saveBatch(editors);
        List<String> copyEditorIds = editors.stream().map(Editor::getId).collect(Collectors.toList());
        Questionnaire copy = MyBeanUtils.copy(questionnaire, Questionnaire::new, "id", "status", "createTime", "createBy", "updateBy", "updateTime");
        copy.setEditorIds(copyEditorIds);
        EntityUserUtil.setAll(copy);
        return this.save(copy);
    }

    @Override
    @Transactional
    public Boolean editQuestionnaire(EditorsDTO reqDTO) {
        List<Editor> editors = MyBeanUtils.copyList(reqDTO.getEditors(), Editor::new, (s, t) -> {
            EntityUserUtil.setAllDynamic(t);
        });
        editorService.saveOrUpdateBatch(editors);
        List<String> editorIds = editors.stream().map(Editor::getId).collect(Collectors.toList());
        Questionnaire questionnaire = this.lambdaQuery().select(Questionnaire::getId, Questionnaire::getCoverPhoto)
                .eq(Questionnaire::getId, reqDTO.getId()).one();
        questionnaire.setTitle(reqDTO.getTitle());
        questionnaire.setDescription(reqDTO.getDescription());
        questionnaire.setEditorIds(editorIds);
        questionnaire.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
        return this.updateById(questionnaire);
    }


    @Override
    public PageInfo<QuestionnaireDTO> listEmployeeQuestionnaires(Integer answeredStatus, BaseQuery query) {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        MyPageInfoUtils.isEmptyPage(employeeId);
        EmployeeInfoVO employeeInfo = SystemUtil.getInstance().getEmployeeInfo(employeeId);
        String companyId = employeeInfo.getCompanyId();
        String parkOrgId = employeeInfo.getParkOrgId();
        String lastSql = new StringBuilder(" ( json_contains(company_ids,").append("'\"").append(companyId).append("\"'").append(")").append(" or ")
                .append("json_contains(employee_ids,").append("'\"").append(employeeId).append("\"'").append(")")
                .append(")")
                .toString();
        Set<String> answeredQuestionnaireIds = questionnaireSubmissionService.lambdaQuery().select(QuestionnaireSubmission::getQuestionnaireId)
                .eq(QuestionnaireSubmission::getEmployeeId, employeeId)
                .list().parallelStream().map(QuestionnaireSubmission::getQuestionnaireId).collect(Collectors.toSet());

        LambdaQueryWrapper<Questionnaire> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Questionnaire::getId, Questionnaire::getTitle, Questionnaire::getCoverPhoto, Questionnaire::getStartTime, Questionnaire::getDeadlineTime, Questionnaire::getStatus);
        wrapper.eq(Questionnaire::getParkOrgId, parkOrgId);

        //未开始的不现实
        wrapper.le(Questionnaire::getStartTime, LocalDateTime.now());
        //未完成：未填写的，与截止时间无关
        //已完成：已填写的，与截止时间无关
        //已结束：截止时间已过、未填写的；未到截止时间，未填写，且问卷收集停止的
//        1、未完成
        if (answeredStatus == NOT_FINISHED.status) {
            //wrapper.ge(Questionnaire::getDeadlineTime, LocalDateTime.now());
            wrapper.notIn(ObjectUtil.isNotEmpty(answeredQuestionnaireIds), Questionnaire::getId, answeredQuestionnaireIds);
            wrapper.eq(Questionnaire::getStatus, COLLECTING.status)
                    .gt(Questionnaire::getDeadlineTime, LocalDateTime.now());
        } else if (answeredStatus == FINISHED.status) {
//        2、已完成
            MyPageInfoUtils.isEmptyPage(answeredQuestionnaireIds);
            wrapper.in(Questionnaire::getId, answeredQuestionnaireIds);
//            wrapper.eq(Questionnaire::getStatus, COLLECTING.status);
        } else if (answeredStatus == ANSWERED_END.status) {
//        3、已结束
            wrapper.notIn(ObjectUtil.isNotEmpty(answeredQuestionnaireIds), Questionnaire::getId, answeredQuestionnaireIds);
            wrapper.and(t -> t.le(Questionnaire::getDeadlineTime, LocalDateTime.now()).or(r -> r.eq(Questionnaire::getStatus, END.status)));
        }
        wrapper.apply(lastSql);
        wrapper.orderByDesc(Questionnaire::getCreateTime);
        query.startPage();
        List<Questionnaire> list = this.list(wrapper);
        List<QuestionnaireDTO> result = MyBeanUtils.copyList(list, QuestionnaireDTO::new, (s, t) -> {
            t.setStatus(getQuestionnaireStatus(s));
            if (LocalDateTime_MAX.equals(s.getDeadlineTime())) {
                t.setDeadlineTime(null);
            }
        });
        return query.createPageInfo(result);
    }


    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public QuestionnaireDTO getQuestionnaireById(String id) {
        Questionnaire entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        List<String> editorIds = entity.getEditorIds();
        QuestionnaireDTO questionnaireDTO = MyBeanUtils.copy(entity, QuestionnaireDTO::new, (s, t) -> {
            if (LocalDateTime_MAX.equals(s.getDeadlineTime())) {
                t.setDeadlineTime(null);
            }
        });
        if (ObjectUtil.isNotEmpty(editorIds)) {
            List<Editor> editors = editorService.listByIds(editorIds);
            List<EditorDTO> editorDTOS = MyBeanUtils.copyList(editors, EditorDTO::new);
            questionnaireDTO.setEditors(editorDTOS);
        }
        return questionnaireDTO;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<QuestionnaireVO> listQuestionnairePage(QuestionnaireQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.startPage();
        List<Questionnaire> list = this.lambdaQuery().eq(StringUtils.isNotBlank(query.getParkOrgId()), Questionnaire::getParkOrgId, query.getParkOrgId())
                .like(StringUtils.isNotBlank(query.getTitle()), Questionnaire::getTitle, query.getTitle())
                .in(Questionnaire::getParkOrgId, parkOrgIds).orderByDesc(Questionnaire::getCreateTime).list();
        MyPageInfoUtils.isEmptyPage(list);
        List<QuestionnaireVO> result = MyBeanUtils.copyList(list, QuestionnaireVO::new, (s, t) -> {
            Integer countSubmission = questionnaireSubmissionService.countSubmission(t.getId());
            t.setAnsweredCount(countSubmission);
            t.setStatus(getQuestionnaireStatus(s));
            if (LocalDateTime_MAX.equals(s.getDeadlineTime())) {
                t.setDeadlineTime(null);
            }
        });
        return query.createPageInfo(result);
    }


    @Override
    public Boolean removeQuestionnaireById(String id) {
        Questionnaire questionnaire = this.getById(id);
        Integer questionnaireStatus = getQuestionnaireStatus(questionnaire);
        if (questionnaireStatus == COLLECTING.status) {
            throw new ResolinkRuntimeException("10023");
        }
        return this.removeById(id);
    }

    @Override
    public Boolean updateStatus(QuestionnaireUpdateStatusReqDTO reqDTO) {
        Questionnaire one = this.getById(reqDTO.getId());
        Integer originStatus = one.getStatus();
        LocalDateTime deadlineTime = one.getDeadlineTime();
        if (reqDTO.getStatus() == COLLECTING.status) {
            if (Objects.nonNull(deadlineTime) && deadlineTime.isBefore(LocalDateTime.now())) {
                throw new ResolinkRuntimeException("50100");
            }
            if (ObjectUtils.isEmpty(one.getEditorIds())) {
                throw new ResolinkRuntimeException("50101");
            }
        } else if (reqDTO.getStatus() == END.status && originStatus != COLLECTING.status) {
            throw new ResolinkRuntimeException("10023");
        }
        one.setStatus(reqDTO.getStatus());
        one.setUpdateTime(null);
        EntityUserUtil.setUpdateBy(one);
        return this.updateById(one);
    }

    @Override
    public PageInfo<QuestionnaireDataVO> statistics(BaseQuery<String> baseQuery) {
        String questionnaireId = baseQuery.getParam();
        Questionnaire one = this.lambdaQuery().select(Questionnaire::getTitle, Questionnaire::getAnonymous).eq(Questionnaire::getId, questionnaireId).one();
        baseQuery.startPage();
        List<QuestionnaireSubmission> list = questionnaireSubmissionService.lambdaQuery()
                .select(QuestionnaireSubmission::getId, QuestionnaireSubmission::getEmployeeId, QuestionnaireSubmission::getCreateTime)
                .eq(QuestionnaireSubmission::getQuestionnaireId, questionnaireId).list();
        Map<String, String> map = null;
        if (!one.getAnonymous()) {
            Set<String> employeeIds = list.parallelStream().map(QuestionnaireSubmission::getEmployeeId).collect(Collectors.toSet());
            map = queryEmployeeMobile(employeeIds);
        }
        Map<String, String> finalMap = map;
        List<QuestionnaireDataVO> result = list.parallelStream().map(t -> {
            QuestionnaireDataVO vo = new QuestionnaireDataVO();
            vo.setQuestionnaireId(questionnaireId);
            vo.setTitle(one.getTitle());
            String mobile = Optional.ofNullable(finalMap).map(m -> m.get(t.getEmployeeId())).orElse(null);
            vo.setMobile(mobile);
            vo.setCreateTime(t.getCreateTime());
            vo.setQuestionnaireSubmissionId(t.getId());
            return vo;
        }).collect(Collectors.toList());
        return baseQuery.createPageInfo(result);
    }

    @Override
    public EditorsDTO getQuestionnaireSubmitDetail(String questionnaireSubmissionId) {
        QuestionnaireSubmission submission = questionnaireSubmissionService.getById(questionnaireSubmissionId);
        String questionnaireId = submission.getQuestionnaireId();
        Questionnaire questionnaire = this.lambdaQuery().select(Questionnaire::getTitle, Questionnaire::getDescription, Questionnaire::getEditorIds)
                .eq(Questionnaire::getId, questionnaireId).one();
        EditorsDTO dto = new EditorsDTO();
        dto.setId(questionnaireId);
        dto.setTitle(questionnaire.getTitle());
        dto.setDescription(questionnaire.getDescription());
        if (ObjectUtil.isNotEmpty(questionnaire.getEditorIds())) {
            List<Editor> editors = editorService.listByIds(questionnaire.getEditorIds());
            List<EditorDTO> editorDTOS = MyBeanUtils.copyList(editors, EditorDTO::new);
            dto.setEditors(editorDTOS);
        }

        dto.setEditorResults(JSONUtil.parseObj(submission.getSubmission()));
        return dto;
    }

    @Override
    public QuestionnaireAnalysisReportDTO getQuestionnaireAnalysisReport(String questionnaireId) {
        Questionnaire questionnaire = this.lambdaQuery().select(Questionnaire::getId, Questionnaire::getEditorIds, Questionnaire::getCompanyIds,
                Questionnaire::getEditorIds)
                .eq(Questionnaire::getId, questionnaireId).one();
        List<String> editorIds = questionnaire.getEditorIds();
        List<Editor> editors = editorService.listByIds(editorIds);
        Map<String, String> editorTypeMap = editors.parallelStream().collect(Collectors.toMap(Editor::getId, Editor::getElementType));
        List<QuestionnaireSubmission> submissions = questionnaireSubmissionService.lambdaQuery().select(QuestionnaireSubmission::getSubmission)
                .eq(QuestionnaireSubmission::getQuestionnaireId, questionnaireId).list();
        List<EditorAnalysisDTO> analysisList = Lists.newArrayList();
        Map<String, Map<Integer, Integer>> submissionCountMap = getSubmissionCount(submissions, editorTypeMap);
        for (Editor editor : editors) {
            EditorAnalysisDTO editorAnalysisDTO = null;
            String elementType = editor.getElementType();
            if (StringUtils.equalsAnyIgnoreCase(elementType, RADIO.name(), CHECKBOX.name())) {
                editorAnalysisDTO = buildChoiceAnalysisDTO(editor, submissionCountMap);
            } else {
                editorAnalysisDTO = new EditorAnalysisDTO(editor.getId(), editor.getName(), elementType);
            }
            analysisList.add(editorAnalysisDTO);
        }
        QuestionnaireAnalysisReportDTO result = new QuestionnaireAnalysisReportDTO();
        Integer targetTotalCount = getTargetTotalCount(questionnaire);
        result.setTargetCount(targetTotalCount);
        result.setActualCount(submissions.size());
        double percent = NumberUtil.div(submissions.size(), targetTotalCount.intValue(), 4);
        result.setPercent(percent);
        result.setAnalysis(analysisList);
        return result;
    }

    @Override
    public PageInfo<EditorAnalysisDetailDTO> getQuestionnaireAnalysisDetail(EditorResultQuery query) {
        String questionnaireId = query.getQuestionnaireId();
        String elementType = editorService.lambdaQuery().select(Editor::getElementType)
                .eq(Editor::getId, query.getEditorId()).one().getElementType();
        LambdaQueryChainWrapper<QuestionnaireSubmission> wrapper = questionnaireSubmissionService.lambdaQuery().select(QuestionnaireSubmission::getId, QuestionnaireSubmission::getSubmission);
//        String sql = new StringBuilder(" JSON_EXTRACT(submission,").append("'$.\"").append(query.getEditorId()).append("\"'").append(")").toString();
//        wrapper.apply(sql + " IS NOT NULL");
        wrapper.eq(QuestionnaireSubmission::getQuestionnaireId, questionnaireId);
        wrapper.orderByDesc(QuestionnaireSubmission::getCreateTime);
        List<EditorAnalysisDetailDTO> result = Lists.newArrayList();
        Set<String> submissionIds = Sets.newHashSet();
        if (StringUtils.equalsAnyIgnoreCase(elementType, INPUT.name(), TEXTAREA.name())) {
            if (StringUtils.isNotBlank(query.getKeyword())) {
//                String likeSQL = new StringBuilder(sql).append(" like").append("'%").append(query.getKeyword()).append("%'").toString();
//                wrapper.apply(likeSQL);
//            }
                wrapper.list().forEach(t -> {
                    Map<String, Object> submission = JSONUtil.parseObj(t.getSubmission());
                    String str = (String) submission.get(query.getEditorId());
                    if (StrUtil.containsAny(str, query.getKeyword())) {
                        submissionIds.add(t.getId());
                    }
                });
                MyPageInfoUtils.isEmptyPage(submissionIds);
                wrapper.in(QuestionnaireSubmission::getId, submissionIds);
            }
        }
        Set<String> fileKeys = Sets.newHashSet();
        query.startPage();
        List<QuestionnaireSubmission> list = wrapper.list();
        for (int i = 0; i < list.size(); i++) {
            QuestionnaireSubmission submission = list.get(i);
            String label = (String) JSONUtil.parseObj(submission.getSubmission()).get(query.getEditorId());
            if (StringUtils.equalsAnyIgnoreCase(elementType, UPLOAD.name())) {
                fileKeys.addAll(StrUtil.splitTrim(label, ","));
            }
            EditorAnalysisDetailDTO analysisDTO = new EditorAnalysisDetailDTO(elementType, (query.getPageNum() - 1) * query.getPageSize() + i + 1, label);
            result.add(analysisDTO);
        }
        if (StringUtils.equalsAnyIgnoreCase(elementType, UPLOAD.name())) {
            Map<String, FileDTO> fileDTOMap = fileProvider.getFileDetailByKeys(fileKeys).getData();
            result.forEach(t -> {
                t.setKey(t.getText());
                Set<String> keys = Arrays.stream(t.getText().split(",")).collect(Collectors.toSet());
                String fileOriginalName = keys.stream().map(fileDTOMap::get).filter(Objects::nonNull)
                        .map(FileDTO::getFileOriginalName)
                        .collect(Collectors.joining(","));
//                FileDTO fileDTO = fileDTOMap.get(t.getText());
                t.setText(fileOriginalName);
            });
        }
        return query.createPageInfo(result);
    }

    @Override
    public void analysisDetailDownload(EditorResultQuery query) {
        query.setPageNum(1);
        query.setPageSize(100000);
        PageInfo<EditorAnalysisDetailDTO> analysisDetail = this.getQuestionnaireAnalysisDetail(query);
        ExcelExportUtils.export(analysisDetail.getList(), ANSWER_DETAILS_FILE_NAME, EditorAnalysisDetailDTO.class, Lists.newArrayList("number", "text"));
    }


    private EditorAnalysisDTO buildChoiceAnalysisDTO(Editor editor, Map<String, Map<Integer, Integer>> submissionCountMap) {
        EditorChoiceAnalysisDTO analysisDTO = new EditorChoiceAnalysisDTO(editor.getId(), editor.getName(), editor.getElementType());
        String elementContent = editor.getElementContent();
        JSONArray contents = JSONUtil.parseArray(editor.getElementContent());
        List<EditorChoiceItemDTO> choiceItems = Lists.newArrayList();
        Map<Integer, Integer> integerMap = submissionCountMap.get(editor.getId());
        int totalCount = Optional.ofNullable(integerMap).map(t -> t.values().stream().mapToInt(Integer::intValue).sum()).orElse(0);
        for (int i = 0; i < contents.size(); i++) {
            EditorChoiceItemDTO item = new EditorChoiceItemDTO((String) contents.get(i));
            int count = integerMap == null ? 0 : Optional.ofNullable(integerMap.get(i)).orElse(0);
            item.setCount(count);
            BigDecimal percent = totalCount == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(NumberUtil.div(count, totalCount, 4));
            item.setPercent(percent);
            choiceItems.add(item);
        }
        analysisDTO.setChoices(choiceItems);
        return analysisDTO;
    }


    private Integer getTargetTotalCount(Questionnaire questionnaire) {
        List<String> companyIds = questionnaire.getCompanyIds();
        List<String> employeeIds = questionnaire.getEmployeeIds();
        Map<String, Integer> countMap = employeeProvider.queryEmployeesCount(companyIds);
        AtomicReference<Integer> total = new AtomicReference<>(0);
        countMap.forEach((k, v) -> {
            total.updateAndGet(v1 -> v1 + v);
        });
        if (ObjectUtil.isNotNull(employeeIds)) {
            Map<String, EmployeeDTO> employeeMap = employeeProvider.queryEmployeeByCondition(EmployeeQueryDTO.employeeIdsBuildDTO(employeeIds, "company_id"));
            AtomicInteger employeeCount = new AtomicInteger(0);
            employeeMap.forEach((k, v) -> {
                if (!CollectionUtil.contains(companyIds, v.getCompanyId())) {
                    employeeCount.getAndIncrement();
                }
            });

            total.updateAndGet(v1 -> v1 + employeeCount.get());
        }
        return total.get();
    }

    private Map<String, Map<Integer, Integer>> getSubmissionCount(List<QuestionnaireSubmission> submissions, Map<String, String> editorTypeMap) {
        Map<String, Map<Integer, Integer>> countMap = Maps.newHashMap();
        for (QuestionnaireSubmission submission : submissions) {
            Map<String, Object> result = JSONUtil.parseObj(submission.getSubmission());
            result.forEach((editorId, editorResult) -> {
                Map<Integer, Integer> itemMap = countMap.getOrDefault(editorId, new HashMap<>());
                if (StringUtils.equalsAnyIgnoreCase(editorTypeMap.get(editorId), CHECKBOX.name())) {
                    JSONArray array = ((JSONArray) editorResult);
                    for (int i = 0; i < array.size(); i++) {
                        itemMap.compute(i, (index, val) -> {
                            if (val == null) {
                                val = 1;
                            } else {
                                val += 1;
                            }
                            return val;
                        });
                    }
                } else if (StringUtils.equalsAnyIgnoreCase(editorTypeMap.get(editorId), RADIO.name()) && Objects.nonNull(editorResult)) {
                    itemMap.compute((Integer) editorResult, (index, val) -> {
                        if (val == null) {
                            val = 1;
                        } else {
                            val += 1;
                        }
                        return val;
                    });
                } else {
                    //其他类型不统计
                    return;
                }
                countMap.put(editorId, itemMap);
            });

        }
        return countMap;
    }


    private Map<String, String> queryEmployeeMobile(Set<String> employeeIds) {
        EmployeeQueryDTO queryDTO = EmployeeQueryDTO.employeeIdsBuildDTO(employeeIds, "mobile");
        Map<String, EmployeeDTO> employeeDTOMap = employeeProvider.queryEmployeeByCondition(queryDTO);
        Map<String, String> map = Maps.newHashMap();
        employeeDTOMap.forEach((k, v) -> {
            map.put(k, v.getMobile());
        });
        return map;
    }

    private Integer getQuestionnaireStatus(Questionnaire questionnaire) {
        Integer questionnaireStatus = questionnaire.getStatus();
        Boolean collectingStatusFlag = Optional.ofNullable(questionnaire.getDeadlineTime()).map(c -> c.isBefore(LocalDateTime.now())).orElse(false);
        if (questionnaireStatus == COLLECTING.status && collectingStatusFlag) {
            return END.status;
        }
        return questionnaireStatus;
    }

    public static void main(String[] args) {
        String elementType = "input";
        List<String> abc = StrUtil.splitTrim("abc,cdf, rgd", ",");

        System.out.println(abc);
        System.out.println(StringUtils.equalsAnyIgnoreCase(elementType, INPUT.name(), TEXTAREA.name()));
        double div = NumberUtil.div(5, 3, 2);
        System.out.println(div);
    }

}
