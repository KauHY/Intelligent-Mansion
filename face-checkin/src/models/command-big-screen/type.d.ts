/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-02 11:20:59
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-17 17:00:26
 */
interface ExceptionalTaskListItem { // 车道分页查询
  pointName:string,
  taskId:string,
  taskName:string,
  updateTime:string
}

interface ExceptionalTaskInfo {
  list:ExceptionalTaskListItem[],
  1:number, // 待执行
  2:number, // 巡检中
  3:number, // 已结束
}

interface LabelData {
  companyName: string,
  buildArea: number,
  houseFunction: number, // 功能
  orientation: number, // 朝向
  houseProperty: number, // 房屋属性
  houseStatus: number,
  name:string, // 房间名称
  center: [number, number, number],
}

interface HooseDatas{
  [room:string]:LabelData
}

interface ActivityInfo { // 园区活动
  activityTimeEnd:string,
  activityTimeStart:string,
  id:string,
  name:string,
  status:1|2|3|4, // 1 待生效、2 预告中、3 进行中、4 已结束
}

interface OrderInfoBase {
  finishCount:number, // 已完成数量
  ongoingCount:number, // 进行中数量
}
interface OrderInfo extends OrderInfoBase{ // 工单信息
  orderStatistic:{
    1:OrderInfoBase,
    2:OrderInfoBase,
    3:OrderInfoBase,
  }
}

interface CompanyOverview {
  companyTotal:number, // 入驻企业总数
  employeeTotal:number, // 入驻人数总数
  occupancyRate:number, // 入驻率
  industryType:{
    [key:number]:number
  },
  taxType:{ // 税收分布
    [key:number]:number
  }
}

interface HouseRentInfo{
  chanceCount: number, // 总商机(条)
  rentedBuildArea: number, // 已租房间数面积
  rentedHouseCount: number, // 已租房间数
  rentingBuildArea: number, // 待租房间数面积
  rentingHouseCount: number, // 待租房间数
  totalBuildArea: number, // 总面积(平米)
  totalHouseCount: number, // 房间总数
  companyCount:number, // 入驻企业总数
  occupancyRate:number, // 入驻率
}

interface BillInfo {
  thisYearAmount:number,
  totalAmount:number,
  billListForScreen:{
    amountData:{[key:number]:number},
    month:string,
  }[]
}

interface ApartmentInfo {
  rentedHouseCount:number, // 已租房间数
  rentingHouseCount:number, // 待租房间数
  totalHouseCount:number, // 房间总数
  amountData:{
    METER:{
      totalAmount:number, // 累计
      nearOneMonthAmount:number, // 近一月
    }
    RENT:{
      totalAmount:number, // 累计
      nearOneMonthAmount:number, // 近一月
    }

  }
}

interface WeatherInfo { // 天气
  pm10:string,
  pm2p5:string,
  precip:string,
  warning:string,
  icon:string,
  text:string,
  temp:string,
}

interface ParkProfile { // 园区概览
  parkInfo:{ // A区停车场
    stopCount: number, // 驶入车辆数
    parkingName:string,
    parkCount: number // 车位数量
  }[]
  reserveCount:number, //  //会议当天实时占用数
  meetingCount: number // 会议室总数
}

export {
  ExceptionalTaskInfo,
  HooseDatas,
  ActivityInfo,
  OrderInfo,
  CompanyOverview,
  HouseRentInfo,
  BillInfo,
  ApartmentInfo,
  WeatherInfo,
  ParkProfile,
  // TodayTaskCount,
};
