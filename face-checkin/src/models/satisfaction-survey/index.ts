/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-23 14:34:12
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 17:37:58
 */
import URLS from '@/models/urlConstant/satisfaction-survey';
import request from '~utils/axios';
import {
  QuestionnaireInfo,
  QuestionnaireSearchParams,
  QuestionnaireSet,
  StatusType,
  StatisticsListInfo,
  StatisticsListSearchParams,
  QuestionnaireAnalysisReport,
  QuestionnaireAnswerDetailsSearchParams,
  QuestionnaireAnswerDetails,
} from './type';

export default class {
  async questionnaireListPage<T>(searchData:QuestionnaireSearchParams) {
    const { data } = await request<{list:QuestionnaireInfo[], total:number}>({
      url: URLS.questionnaireListPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  async questionnaireDel(id:string) {
    await request({
      url: `${URLS.questionnaireDel}${id}`,
      method: 'DELETE',
    });
  }

  // 问卷新增
  async questionnaireAdd(data:QuestionnaireSet) {
    await request({
      url: URLS.questionnaireAdd,
      data,
    });
  }

  // 问卷设置修改
  async questionnaireModify(data:QuestionnaireSet) {
    await request({
      url: URLS.questionnaireModify,
      data,
    });
  }

  // 问卷状态更新
  async questionnaireUpdateState(data:{id:string, status:StatusType}) {
    await request({
      url: URLS.questionnaireUpdateState,
      data,
    });
  }

  // 问卷复制
  async questionnaireCopy(id:string) {
    await request({
      url: `${URLS.questionnaireCopy}${id}`,
    });
  }

  // 问卷设计
  async questionnaireDesign(data:QuestionnaireInfo) {
    // 数据处理成后台能接收的
    const editors = data.editors.map((editorsItem) => {
      const { constraintCondition, elementContent } = editorsItem;
      const tempConstraintCondition = constraintCondition ? JSON.stringify(constraintCondition) : '';
      const tempElementContent = elementContent ? JSON.stringify(elementContent) : '';
      return {
        ...editorsItem,
        constraintCondition: tempConstraintCondition,
        elementContent: tempElementContent,
      };
    });
    await request({
      url: URLS.questionnaireDesign,
      data: {
        ...data,
        editors,
      },
    });
  }

  // 根据id查询问卷详情
  async questionnaireFindById(id:string) {
    const { data } = await request<QuestionnaireInfo>({
      url: `${URLS.questionnaireFindById}${id}`,
      method: 'get',
    });
    const editors = data.editors?.map((editorsItem) => {
      const { constraintCondition, elementContent } = editorsItem;
      return {
        ...editorsItem,
        constraintCondition: typeof constraintCondition === 'string' ? JSON.parse(constraintCondition) : {},
        elementContent: typeof elementContent === 'string' ? JSON.parse(elementContent) : [],
      };
    });
    return {
      ...data,
      editors: editors || [],
    };
  }

  // 问卷统计列表
  async questionnaireStatistics<T>(searchData:StatisticsListSearchParams) {
    const { data } = await request<{list:StatisticsListInfo[], total:number}>({
      url: URLS.questionnaireStatistics,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 根据id查询问卷配置和提交结果详情
  async questionnaireConfAndResultDetails(questionnaireSubmissionId:string) {
    const { data } = await request<QuestionnaireInfo>({
      url: URLS.questionnaireConfAndResultDetails,
      data: {
        questionnaireSubmissionId,
      },
      method: 'get',
    });
    const editors = data.editors?.map((editorsItem) => {
      const { constraintCondition, elementContent } = editorsItem;
      return {
        ...editorsItem,
        constraintCondition: typeof constraintCondition === 'string' ? JSON.parse(constraintCondition) : {},
        elementContent: typeof elementContent === 'string' ? JSON.parse(elementContent) : [],
      };
    });
    return {
      ...data,
      editors: editors || [],
    };
  }

  // 问卷数据分析接口
  async questionnaireAnalysisReport(questionnaireId:string) {
    const { data } = await request<QuestionnaireAnalysisReport>({
      url: URLS.questionnaireAnalysisReport,
      method: 'get',
      data: { questionnaireId },
    });
    return data;
  }

  // 问卷答案详情
  async questionnaireAnswerDetailsListPage<T>(searchData:QuestionnaireAnswerDetailsSearchParams) {
    const { data } = await request<{list:QuestionnaireAnswerDetails[], total:number}>({
      url: URLS.questionnaireAnswerDetailsListPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 答案详情导出
  async questionnaireAnswerDetailsExport(searchData: QuestionnaireAnswerDetailsSearchParams) {
    const { data, headers } = await request({
      url: URLS.questionnaireAnswerDetailsExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }
}

export * from './type';
