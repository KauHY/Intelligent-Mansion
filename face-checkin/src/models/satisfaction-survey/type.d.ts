/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-23 14:30:42
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-30 13:44:54
 */
import { QuestionnaireConf, ElementType } from '~components/design-questionnaire/type';

interface QuestionnaireSearchParams extends BaseSearchParams { // 问卷分页查询
  title:string, // 问卷名称
  parkOrgId:string,
}

interface QuestionnaireSet { //  问卷设置
  id:'',
  title:string,
  parkOrgId:string,
  startTime:string, // 开始时间
  deadlineTime:string, // 截止时间
  employeeIds:string[], // 收集对象,员工
  companyIds:string[], // 收集对象,公司
  coverPhoto:string, // 封面图片
  isChooseTarget?:boolean, // 是否选择了收集对象，用于页面交互
  anonymous: boolean|null, // 是否匿名
}

type StatusType = 1|2|3

interface EditorResults { // 问卷答案
  [key:string]:string|number[]
}

interface QuestionnaireInfo { // 问卷设计
    id: string,
    title:string,
    companyIds?:string[],
    employeeIds?:string[],
    employeeNames?:string[],
    deadlineTime:string,
    editorResults?:EditorResults,
    editors: QuestionnaireConf[],
    status?:StatusType,
    description: string, // 描述
}

interface StatisticsListInfo { // 数据统计列表
  title:string,
  mobile:string,
  questionnaireId:string,
  questionnaireSubmissionId:string,
  createTime:string,
}

interface StatisticsListSearchParams extends BaseSearchParams { // 问卷分页查询
  param: string, // 问卷id
  percent:string,
}

interface ChoicesItem { // 问卷分析答案选项占比
  label:string,
  percent:number,
  count:number
}
interface AnalysisItem { // 问卷分析答案
  elementType:ElementType,
  id:string,
  name:string,
  choices:ChoicesItem[]
}
interface QuestionnaireAnalysisReport { // 问卷数据分析接口
  targetCount:number,
  actualCount:number,
  percent:number,
  analysis:AnalysisItem[]
}

interface QuestionnaireAnswerDetailsSearchParams extends BaseSearchParams { // 问卷答案详情查询接口
  keyword?: string, // 关键词
  questionnaireId:string,
}

interface QuestionnaireAnswerDetails { // 问卷答案详情数据结构
  elementType:string,
  id:string,
  name:string,
  number:string,
  key:string
  text:string,
}

export {
  QuestionnaireSet,
  QuestionnaireSearchParams,
  QuestionnaireInfo,
  StatusType,
  StatisticsListSearchParams,
  StatisticsListInfo,
  QuestionnaireAnalysisReport,
  QuestionnaireAnswerDetailsSearchParams,
  QuestionnaireAnswerDetails,
};
