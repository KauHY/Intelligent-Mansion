/*
 * @Description: 巡检管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-29 13:46:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-07 16:11:32
 */

interface PatrolPlace { // 巡检点
    id: string,
    location: string, // 巡检位置
    parkOrgId: string,
    parkOrgName: string,
    pointName: string, // 巡检点名称
    remark: string, // 备注
    type: number|null, // 巡检点类型
}

interface SearchPatrolPlaceParams extends BaseSearchParams {
    type: string, // 巡检点类型
    pointName: string, // 巡检点名称
    orgId:string, // 组织id
}

interface PatrolLine { // 巡检路线
  id: string,
  circuitName:string, // 巡检路线名称
  orderly: boolean, // 是否有序
  parkOrgId: string,
  parkOrgName: string,
  remark: string, // 备注
  type: number|null, // 巡检点类型
  updateTime?:string // 更新时间
  points?:PatrolPlace[]
  pointIds?:[],
}

interface PatrolLineSearchParams extends BaseSearchParams {
  type: string, // 巡检点类型
  circuitName: string, // 巡检路线名称
  orgId:string, // 组织id
}

interface PatrolLineLinkPlaceParams {
  circuitId:string, // 巡检路线ID
  pointIds:string[], // 巡检点ID
}

interface PatrolPlanSearchParams extends BaseSearchParams { // 巡检计划分页查询
  type: string, // 巡检点类型
  planName: string, // 巡检路线名称
  status: number|null,
  orgId:string, // 组织id
}

interface PatrolPlan {
  planName:string, // 计划名称
  canDelay:boolean, // 节假日是否顺延
  circuitId:string, // 巡检路线ID
  cycleType: 0|1, // 执行周期类型,0天，1周
  effectDateEnd:string, // 生效日期
  effectDateStart:string, // 生效日期
  executionTimeEnd:string, // 执行结束时间
  executionTimeStart:string, // 执行开始时间
  executionRule:string,
  remark:string, // 备注
  status?:0|1,
  type:number|null,
  id?:string,
  circuitName?:string, // 巡检路线
  employees?:string[], // 关联员工
  parkOrgId?:string,
  parkOrgName?:string
}

interface LinkEmployee {
  employeeIds:string[], // 巡检人id
  planId:string, // 巡检计划ID
}

interface PatrolTaskSearchParams extends BaseSearchParams { // 巡检任务分页查询
  completeEnd: string,
  completeStart:string,
  taskName: string, // 巡检任务名称
  taskStatus: number|null,
  orgId: string, // 组织id
  employeeName:string
  times?:[], // 页面交互辅助参数
}

interface PatrolDetails {
  state: string, // 情况
  description: string, // 情况说明
  id: string,
  picture: string, // 巡检图片
  pointName: string, // 巡检点名称
  status: boolean, // 巡检点巡检状态
  taskId: string, // 任务id
  updateTime: string, // 更新时间
  pointStatus:number,
}

interface PatrolTask {
  parkOrgName:string,
  type:number, // 巡检类型
  actualEmployeeId: string, // 实际巡检人
  actualEmployeeName: string,
  completionTime: string, // 完成时间
  employees: string, // 巡检人
  taskName:string, // 任务名称
  id: string, // 任务id
  pointDetails?:PatrolDetails[], // 巡检任务详情
  planId: string, // 巡检计划ID
  status: number|null, // 巡检状态
  taskTime: string, // 任务时间
}

// 巡检任务流转
interface PatrolTaskRotateParams {
  employeeIds:string[],
  taskId:string
}

export {
  PatrolPlace,
  SearchPatrolPlaceParams,
  PatrolLine,
  PatrolLineSearchParams,
  PatrolLineLinkPlaceParams,
  PatrolPlanSearchParams,
  PatrolPlan,
  LinkEmployee,
  PatrolTaskSearchParams,
  PatrolTask,
  PatrolTaskRotateParams,
  PatrolDetails,
};
