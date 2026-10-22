/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-28 16:46:52
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-02-24 13:53:22
 */
import URLS from '~models/urlConstant/attendance';
import request from '~utils/axios';
import {
  AttendanceRulesSearchParams,
  AttendanceRecordSearchParams,
  AttendanceRules,
} from './type';

export default class {
  async attendanceListPage<T>(searchData:AttendanceRulesSearchParams) {
    const { data } = await request({
      url: URLS.attendanceListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<AttendanceRules>>list,
      total: <number>total,
    };
  }

  async attendanceDel(id:string) {
    await request({
      url: `${URLS.attendanceDel}${id}`,
      method: 'DELETE',
    });
  }

  async attendanceAdd(data:AttendanceRules) {
    await request({
      url: URLS.attendanceAdd,
      data,
    });
  }

  async attendanceModify(data:AttendanceRules) {
    await request({
      url: URLS.attendanceModify,
      data,
    });
  }

  async attendanceFindById(id:string) {
    const { data } = await request({
      url: `${URLS.attendanceFindById}${id}`,
      method: 'get',
    });
    return data as AttendanceRules;
  }

  async modifyStatus(data:{id:string, enable:0|1}) {
    await request({
      url: URLS.modifyStatus,
      data,
      method: 'put',
    });
  }

  async getAllEmployeeByRuleId(ruleId:string) {
    const { data } = await request({
      url: `${URLS.getAllEmployeeByRuleId}${ruleId}`,
      method: 'get',
    });
    return data as SelectOptions;
  }

  async monthAttendanceListPageApi<T>(searchData:AttendanceRulesSearchParams) {
    const { data } = await request({
      url: URLS.listAttendanceMonthRecordPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<AttendanceRules>>list,
      total: <number>total,
    };
  }

  async dayAttendanceListPageApi<T>(searchData:AttendanceRulesSearchParams) {
    const { data } = await request({
      url: URLS.listAttendanceRecordPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<AttendanceRules>>list,
      total: <number>total,
    };
  }

  async clockRecordListPageApi<T>(searchData:AttendanceRulesSearchParams) {
    const { data } = await request({
      url: URLS.listPunchRecordPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<AttendanceRules>>list,
      total: <number>total,
    };
  }

  async fieldApplicationListPageApi<T>(searchData:AttendanceRulesSearchParams) {
    const { data } = await request({
      url: URLS.listFieldworkApplyRecordPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<AttendanceRules>>list,
      total: <number>total,
    };
  }

  // 考勤导出
  async monthRecordExportApi(searchData:AttendanceRecordSearchParams) {
    const { data, headers } = await request({
      url: URLS.monthRecordExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async dayRecordExportApi(searchData:AttendanceRecordSearchParams) {
    const { data, headers } = await request({
      url: URLS.dayRecordExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async punchRecordExportApi(searchData:AttendanceRecordSearchParams) {
    const { data, headers } = await request({
      url: URLS.punchRecordExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async applicationRecordExportApi(searchData:AttendanceRecordSearchParams) {
    const { data, headers } = await request({
      url: URLS.applicationRecordExport,
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
