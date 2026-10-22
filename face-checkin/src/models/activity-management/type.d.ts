/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-11 10:43:19
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-13 21:27:30
 */
interface ActivitySearchParams extends BaseSearchParams { // 车道分页查询
    name: string, // 活动名称
    parkOrgId: string, // 园区
    effectTimeEnd:string, // 生效结束事件
    effectTimeStart:string, // 生效开始事件
    activityTimeEnd:string, // 活动结束时间
    activityTimeStart:string, // 活动开始时间
    confRegister:boolean, // 是否需要报名
    status: 1|1|null, // 状态
}
interface ActivityInfo {
    activityTimeEnd: string, // 活动结束时间
    activityTimeStart: string, // 活动结束时间
    confRegister: boolean, // 是否需要报名
    confRegisterDeadline: string, // 报名截止时间
    confRegisterLimit: boolean, // 是否限制报名人数
    confRegisterLimitCount: number | null, // 报名人数上限
    content: string, // 活动详情
    effectTime: string, // 生效时间
    id: string,
    name: string, // 活动名称
    listPicture: string, // 列表图片
    detailPicture: string, // 详情图片
    place: string, // 活动地点
    registeredCount: number|null, // 报名人数
    selfRegistered: boolean, // 自己是否报名
    status: number|null,
    parkOrgIds:string[]
    topping?: true
    parkOrgNames?:SelectOptions
}

interface SignUpRecordSearchParams extends BaseSearchParams { // 车道分页查询
  name?: string, // 姓名
  mobile?:string,
  id:string, // 活动id
}

interface SignUpRecord {
  activityId: string,
  createTime: string,
  id: string,
  mobile: string,
  name: string

}
export {
  ActivitySearchParams,
  ActivityInfo,
  SignUpRecordSearchParams,
  SignUpRecord,
};
