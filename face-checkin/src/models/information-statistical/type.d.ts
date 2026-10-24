/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-23 14:30:42
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-30 11:02:33
 */
import { QuestionnaireConf, ElementType } from '~components/design-questionnaire/type';

interface FormTemplateSearchParams extends BaseSearchParams { // 表单模板分页查询
  title:string, // 问卷名称
  parkOrgId:string,
}

interface FormTemplateSet { //  表单模板新增
  id:string,
  title:string,
  parkOrgId:string,
}

interface FormTemplateInfo { // 表单模板设计|表单模板分页
    id: string,
    title:string,
    parkOrgId:string,
    parkOrgName:string,
    editors: QuestionnaireConf[],
    description: string, // 描述
}

// ----------------------- 表单管理 -------------------

type StatusType = 1|2|3 // 状态，1设计中，2收集中，3已结束
interface FormManageSearchParams extends BaseSearchParams { // 问卷分页查询
  param: string, // 问卷id
  percent:string,
}

interface FormManageInfo {
  id: string,
  title:string,
  templateId:string, // 模板id
  companyIds:string[],
  employeeIds:string[], // 收集对象,公司
  employeeNames?:string[], // 收集员工姓名
  cycle:1|2|3|4|null, // 周期，1每日、2每周、3每月、4自定义
  // editors: QuestionnaireConf[],
  parkOrgId:string,
  status?:StatusType,
  isChooseTarget?:boolean, // 前端辅助字段是否选择收集对象
  reminded:boolean, // 是否提醒，false不提醒，1true
  description: string, // 描述
  deadlineNum:number|null, // 截止时间的周几或第几天
  deadlineTime:string, // 截止时间
  updateTime?:string
}

export {
  FormTemplateSet,
  FormTemplateSearchParams,
  FormTemplateInfo,
  FormManageSearchParams,
  FormManageInfo,
  StatusType,
};
