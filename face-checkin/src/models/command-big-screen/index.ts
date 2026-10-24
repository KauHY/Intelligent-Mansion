/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-02 11:21:04
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-17 17:06:08
 */
import URLS from '~models/urlConstant/command-big-screen';
import request from '~utils/axios';
import Park from '~models/park';
import {
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
} from './type';

export default class {
  parkOrgId!:string

  parkOrgIdSessionKey = 'commandBigScreen_ParkOrgId'

  async initPark() {
    if (sessionStorage[this.parkOrgIdSessionKey]) {
      this.parkOrgId = sessionStorage[this.parkOrgIdSessionKey];
      return this.parkOrgId;
    }
    const parkList = await new Park().searchParkListName();
    sessionStorage[this.parkOrgIdSessionKey] = parkList[0]?.value || '';
    // sessionStorage[this.parkOrgIdSessionKey] = '1438757358306246657';

    this.parkOrgId = sessionStorage[this.parkOrgIdSessionKey];
    return this.parkOrgId;
  }

  async exceptionalTaskInfo<T>() {
    console.log(this.parkOrgId, '---this.parkOrgId');
    const { data: list } = await request<ExceptionalTaskInfo['list']>({
      url: URLS.exceptionalTaskList,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
      },
      hiddenLoading: true,
    });
    const data = await request({
      url: URLS.todayTaskCount,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
      },
      hiddenLoading: true,
    });
    // 兼容新建偷懒写的狗屎接口
    const temp = data.data || {};

    return {
      list,
      1: 0,
      2: 0,
      3: 0,
      ...temp,
    } as ExceptionalTaskInfo;
  }

  async listHouseFor3D(buildingId:string, floor:string) {
    const { data } = await request<HooseDatas>({
      url: URLS.listHouseFor3D,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
        buildingId,
        floor,
      },
      hiddenLoading: true,
    });
    return data;
  }

  // 园区活动
  async getActivityList() {
    const { data } = await request<ActivityInfo[]>({
      url: URLS.getActivityList,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return data;
  }

  // 工单那信息
  async getOrderInfo() {
    const { data } = await request({
      url: URLS.orderResponse,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
      },
      hiddenLoading: true,
    });

    const { data: orderStatistic } = await request({
      url: URLS.orderStatistic,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return {
      ...data,
      orderStatistic,
    } as OrderInfo;
  }

  // 企业总览
  async getCompanyOverview() {
    const { data } = await request<CompanyOverview>({
      url: URLS.getCompanyOverview,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return data;
  }

  // 智慧招商
  async getHouseRentData(fromScreen = true, parkOrgId?:string) {
    const { data } = await request<HouseRentInfo>({
      url: URLS.getHouseRentData,
      method: 'get',
      data: {
        parkOrgId: parkOrgId ?? this.parkOrgId,
        fromScreen,
      },
      hiddenLoading: true,
    });
    return data;
  }

  // 商机概览
  async getChanceOverview(createTimeStart:string, createTimeEnd:string, parkOrgId?:string) {
    const { data } = await request<HouseRentInfo>({
      url: URLS.getChanceOverview,
      method: 'get',
      data: {
        createTimeStart,
        createTimeEnd,
        parkOrgId: parkOrgId ?? this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return data;
  }

  // 客户行业分析
  async getCustomerIndustry(createTimeStart:string, createTimeEnd:string, parkOrgId?:string) {
    const { data } = await request<HouseRentInfo>({
      url: URLS.getCustomerIndustry,
      method: 'get',
      data: {
        createTimeStart,
        createTimeEnd,
        parkOrgId: parkOrgId ?? this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return data;
  }

  // 线索来源分析
  async getThreadSource(createTimeStart:string, createTimeEnd:string, parkOrgId?:string) {
    const { data } = await request<HouseRentInfo>({
      url: URLS.getThreadSource,
      method: 'get',
      data: {
        createTimeStart,
        createTimeEnd,
        parkOrgId: parkOrgId ?? this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return data;
  }

  // 商机转换率
  async getTransitionRate(createTimeStart:string, createTimeEnd:string, parkOrgId?:string) {
    const { data } = await request<HouseRentInfo>({
      url: URLS.getTransitionRate,
      method: 'get',
      data: {
        createTimeStart,
        createTimeEnd,
        parkOrgId: parkOrgId ?? this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return data;
  }

  async getBillForScreen() {
    const { data } = await request<BillInfo>({
      url: URLS.getBillForScreen,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return data;
  }

  // 公寓管理
  async getApartmentDataForScreen() {
    const { data } = await request<ApartmentInfo>({
      url: URLS.getApartmentDataForScreen,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return data;
  }

  // 天气
  async getWeather() {
    const { data } = await request<WeatherInfo>({
      url: URLS.getWeather,
      method: 'get',
      data: {
        parkOrgId: this.parkOrgId,
      },
      hiddenLoading: true,
    });
    return data;
  }

  async getParkProfile() {
    const { data } = await request({
      url: `${URLS.parkingStatistics}${this.parkOrgId}/get`,
      method: 'get',
      hiddenLoading: true,
    });

    const { data: meetingStatistics } = await request({
      url: `${URLS.meetingStatistics}${this.parkOrgId}/get`,
      method: 'get',
      hiddenLoading: true,
    });
    return {
      parkInfo: data,
      ...meetingStatistics,
    } as ParkProfile;
  }
}
export * from './type';
