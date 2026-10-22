/*
 * @Description: 人行管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-07 14:18:58
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-20 16:19:59
 */
interface PersonPassRecordSearchParams extends BaseSearchParams { // 车道分页查询
  userName: string, // 姓名
  mobile: string, // 手机
  labelId: string, // 标签
  deviceId:string, // 设备
  parkOrgId:string, // 园区id
  startTime: string, // 识别开始时间
  endTime: string, // 识别结束时间
}

// 通行记录
interface PersonPassRecord {
  deviceLocation: string, // 设备位置
  deviceName: string, // 设备名
  faceImageUrl: string, // 人脸图片
  healthCode: 0|1|2|null, // 健康码，0：绿色 1：黄色 2：红色
  // bodyTemperature: number, // 体温
  isOverTemp:0|1, // 0正常|1异常
  id: string,
  isPass: 0|1, // 是否通过：识别不通过，识别通过
  labels: string, // 用户标签(逗号隔开)
  mobile: string, // 手机号
  parkOrgName: string, // 园区名
  recognizeTime: string, // 识别时间
  sceneImageUrl: string, // 抓拍照片url
  userName: string, // 姓名
}

interface AccessPolicySearchParams extends BaseSearchParams { // 通行策略分页查询
  policyName: string, // 策略名称
  parkOrgId: string, // 园区id
  companyId:string, // 企业id
}

interface AccessPolicy { // 通行策略
  policyName: string, // 策略名称
  parkOrgId: string, // 园区id
  companyId:string[], // 企业id
  allowVisitor:0|1, // 访客通行：不适应，适用
  companyName?:string, // 企业名称
  parkOrgName?:string, // 园区名称
  status:0|1|null,
  id:string,
  deviceNames?:string, // 设备(逗号隔开)
  deviceIds:string[], // 设备id(逗号隔开)
  policyExpireTime:string, // 策略过期时间
  policyStartTime:string, // 策略生效时间
  allTime:0|1|null, // 全时段
  specifiedTimePeriod:{
    dayOfWeek: string[], // 选中的天数数组
    startTime:string, // 开始时间
    endTime:string, // 结束时间
  }
  tagIds:string[], // 允许员工标签ID(逗号隔开)
}

export {
  PersonPassRecordSearchParams,
  PersonPassRecord,
  AccessPolicySearchParams,
  AccessPolicy,
};
