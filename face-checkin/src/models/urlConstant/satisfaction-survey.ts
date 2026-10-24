/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-24 14:22:51
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 15:40:25
 */
enum DesignQuestionnaire {
    questionnaireListPage = '/park-customer/parkManagement/questionnaire/listQuestionnairePage', // 问卷分页
    questionnaireAdd = '/park-customer/parkManagement/questionnaire/create', // 问卷新增
    questionnaireDel = '/park-customer/parkManagement/questionnaire/remove/', // 问卷删除
    questionnaireModify = '/park-customer/parkManagement/questionnaire/modify', // 问卷编辑
    questionnaireFindById = '/park-customer/parkManagement/questionnaire/findById/', // 问卷详情
    questionnaireUpdateState = '/park-customer/parkManagement/questionnaire/updateStatus', // 问卷状态更新
    questionnaireDesign = '/park-customer/parkManagement/questionnaire/edit', // 问卷设计
    questionnaireCopy = '/park-customer/parkManagement/questionnaire/copy/', // 复制问卷
    questionnaireStatistics = '/park-customer/parkManagement/questionnaire/result/statistics/list', // 问卷分析列表
    questionnaireConfAndResultDetails = '/park-customer/parkManagement/questionnaire/result/questionnaireSubmission/detail', // 数据分析获取提交详情
    questionnaireAnalysisReport = '/park-customer/parkManagement/questionnaire/result/getQuestionnaireAnalysisReport', // 问卷数据分析接口
    questionnaireAnswerDetailsListPage = '/park-customer/parkManagement/questionnaire/result/getQuestionnaireAnalysisDetail', // 问卷答案详情
    questionnaireAnswerDetailsExport = '/park-customer/parkManagement/questionnaire/result/analysisDetail/download', // 问卷答案详情导出

}
export default DesignQuestionnaire;
