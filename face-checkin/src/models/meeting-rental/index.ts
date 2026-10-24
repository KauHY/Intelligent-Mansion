/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 11:29:09
 * @LastEditTime: 2022-03-14 17:52:20
 * @LastEditors: xucaizhi3
 * @Description:
 */
import URLS from '@/models/urlConstant/meeting-rental-manage';
import request from '~utils/axios';
import {
  SearchmeetingRentalParams, MeetingRentalListKind, MeetingRentalInfoKind, EnableKind,
} from './type';

export default class MeetingRentalVo {
  // 会议室-分页查询
  async meetingRentalListPageApi<T>(searchData: SearchmeetingRentalParams, init: boolean) {
    const { data } = await request({
      url: URLS.meetingRentalListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<MeetingRentalListKind>>list,
      total: <number>total,
    };
  }

  // 会议室-新增
  async meetingRentalCreateApi(data: MeetingRentalInfoKind) {
    await request({
      url: URLS.meetingRentalAdd,
      data,
    });
  }

  // 会议室-修改
  async meetingRentalModifyApi(data: MeetingRentalInfoKind) {
    await request({
      url: URLS.meetingRentalModify,
      data,
    });
  }

  // 会议室删除
  async meetingRentalRemoveApi(id: string) {
    await request({
      url: `${URLS.meetingRentalDel}/${id}`,
      method: 'DELETE',
    });
  }

  // 会议室查找
  async meetingRentalFindByIdApi(id: string) {
    const { data } = await request({
      url: `${URLS.meetingRentalFindById}/${id}`,
      method: 'GET',
    });
    return data;
  }

  // 会议室-启用禁用
  async meetingEnableApi(data: EnableKind) {
    await request({
      url: URLS.meetingEnable,
      data,
    });
  }

  // -------------物资管理-------------
  // 物资分页查询
  async materialListPageApi<T>(searchData: SearchmeetingRentalParams, init: boolean) {
    const { data } = await request({
      url: URLS.materialListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<any>>list,
      total: <number>total,
    };
  }

  // 物资-新增
  async materialCreateApi(data: any) {
    await request({
      url: URLS.materialAdd,
      data,
    });
  }

  // 物资-修改
  async materialModifyApi(data: any) {
    await request({
      url: URLS.materialModify,
      data,
    });
  }

  // 物资删除
  async materialRemoveApi(id: string) {
    await request({
      url: `${URLS.materialDel}/${id}`,
      method: 'DELETE',
    });
  }

  // 物资查找
  async materialFindByIdApi(id: string) {
    const { data } = await request({
      url: `${URLS.materialFindById}/${id}`,
      method: 'GET',
    });
    return data;
  }

  // -------------审核管理-------------
  // 审核列表
  async rentalVerifyListPage<T>(searchData: SearchmeetingRentalParams, init: boolean) {
    const { data } = await request({
      url: URLS.rentalVerifyListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<any>>list,
      total: <number>total,
    };
  }

  // 审核详情
  async rentalVerifyFindByIdApi(id: string) {
    const { data } = await request({
      url: `${URLS.rentalVerifyFindById}/${id}`,
      method: 'GET',
    });
    return data;
  }

  // 审核
  async rentalVerifyApi(data: any) {
    await request({
      url: URLS.rentalVerify,
      data,
    });
  }

  async orderExportApi(searchData:SearchmeetingRentalParams) {
    const { data, headers } = await request({
      url: URLS.orderExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }
}

export * from './type';
