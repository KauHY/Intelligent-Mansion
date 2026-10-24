/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-24 15:35:20
 * @LastEditTime: 2022-01-26 17:17:04
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import URLS from '@/models/urlConstant/meeting-manage';
import request from '~utils/axios';
import {
  SearchContractParams,
  MeetingList,
  Meeting,
  MeetingMap,
  MeetingMapList,
} from './type';

export default class MeetingVo {
  // 会议室管理-分页查询
  async meetingListPageApi<T>(searchData:SearchContractParams, init:boolean) {
    const { data } = await request({
      url: URLS.meetingListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<MeetingList>>list,
      total: <number>total,
    };
  }

  // 会议室管理-新增
  async meetingCreateApi(data:Meeting) {
    await request({
      url: URLS.meetingCreate,
      data,
    });
  }

  // 会议室管理-修改
  async meetingModifyApi(data:Meeting) {
    await request({
      url: URLS.meetingModify,
      data,
    });
  }

  // 会议室管理-删除
  async meetingRemoveApi(id:string) {
    await request({
      url: `${URLS.meetingRemove}/${id}`,
      method: 'delete',
    });
  }

  // 会议室管理-根据ID查询
  async meetingFindApi(id:string) {
    const { data } = await request({
      url: `${URLS.meetingFindById}/${id}`,
      method: 'get',
    });
    return data;
  }

  // -休眠会议室屏
  async sleepDevice(id:string) {
    const { data } = await request({
      url: `${URLS.sleepDevice}${id}`,
      method: 'post',
    });
    return data;
  }

  // -唤醒会议室屏
  async wakeUpDevice(id:string) {
    const { data } = await request({
      url: `${URLS.wakeUpDevice}/${id}`,
      method: 'post',
    });
    return data;
  }

  // 查看会议室屏屏幕
  async getMeetingScreenshot(id:string) {
    const { data } = await request({
      url: `${URLS.getMeetingScreenshot}?screenDeviceId=${id}`,
      method: 'get',
    });
    return data;
  }

  // 预定概览
  async overviewMeeting(id:string, type?:number) {
    const { data } = await request({
      url: `${URLS.overviewMeeting}?id=${id}&&idType=${type}`,
      method: 'get',
      hiddenLoading: true,
    });
    return data;
  }

  // 会议室管理-启用禁用
  async meetingChangeApi(data:any) {
    await request({
      url: URLS.meetingChange,
      data,
    });
  }

  // 会议室分布图-分页查询
  async meetingMapListPageApi<T>(searchData:SearchContractParams, init:boolean) {
    const { data } = await request({
      url: URLS.meetingMapListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<MeetingMapList>>list,
      total: <number>total,
    };
  }

  // 会议室分布图-新增
  async meetingMapCreateApi(data:MeetingMap) {
    await request({
      url: URLS.meetingMapCreate,
      data,
    });
  }

  // 会议室分布图-修改
  async meetingMapModifyApi(data:MeetingMap) {
    await request({
      url: URLS.meetingMapModify,
      data,
    });
  }

  // 会议室分布图-删除
  async meetingMapRemoveApi(id:string) {
    await request({
      url: `${URLS.meetingMapRemove}/${id}`,
      method: 'delete',
    });
  }

  // 会议室分布图-根据ID查询
  async meetingMapFindApi(id:string) {
    await request({
      url: `${URLS.meetingMapFindById}/${id}`,
      method: 'get',
    });
  }
}

export * from './type';
