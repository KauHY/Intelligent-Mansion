/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-29 13:46:46
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-07 15:38:49
 */
import URLS from '@/models/urlConstant/patrol-inspection';
import request from '~utils/axios';
import {
  PatrolPlace,
  SearchPatrolPlaceParams,
  PatrolLine,
  PatrolLineSearchParams,
  PatrolLineLinkPlaceParams,
  PatrolPlan,
  PatrolPlanSearchParams,
  LinkEmployee,
  PatrolTaskSearchParams,
  PatrolTask,
  PatrolTaskRotateParams,
} from './type';

export default class Park {
  // 巡检地点分页查询
  async patrolPlaceListPage<T>(searchData:SearchPatrolPlaceParams, init:boolean) {
    const { data } = await request({
      url: URLS.patrolPlaceListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<PatrolPlace>>list,
      total: <number>total,
    };
  }

  // 新增巡检地点
  async patrolPlaceAdd(data:PatrolPlace) {
    await request({
      url: URLS.patrolPlaceAdd,
      data,
    });
  }

  // 巡检地点删除
  async patrolPlaceDel(id:string) {
    await request({
      url: `${URLS.patrolPlaceDel}${id}`,
      method: 'delete',
    });
  }

  // 巡检地点编辑
  async patrolPlaceEdit(data:PatrolPlace) {
    await request({
      url: URLS.patrolPlaceEdit,
      data,
    });
  }

  async patrolPlaceImportPoint(searchData: any) {
    const { data } = await request({
      url: `${URLS.patrolPlaceImportPoint}`,
      data: searchData,
    });
    return data;
  }

  async patrolPlaceExport(searchData:any) {
    const { data, headers } = await request({
      url: URLS.patrolPlaceExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 巡检路线分页查询
  async patrolLineListPage(searchData:PatrolLineSearchParams) {
    const { data } = await request({
      url: URLS.patrolLineListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<PatrolLine>>list,
      total: <number>total,
    };
  }

  // 新增巡检路线
  async patrolLineAdd(data:PatrolLine) {
    await request({
      url: URLS.patrolLineAdd,
      data,
    });
  }

  // 巡检路线删除
  async patrolLineDel(id:string) {
    await request({
      url: `${URLS.patrolLineDel}${id}`,
      method: 'delete',
    });
  }

  // 巡检路线编辑
  async patrolLineEdit(data:PatrolLine) {
    await request({
      url: URLS.patrolLineEdit,
      data,
    });
  }

  // 巡检路线关联巡检的
  async patrolLineLinkPlace(data:PatrolLineLinkPlaceParams) {
    await request({
      url: URLS.patrolLineLinkPlace,
      data,
    });
  }

  // 根据id查询巡检线路详情
  async patrolLineFindById(id:string) {
    const { data } = await request({
      url: `${URLS.patrolLineFindById}${id}`,
      method: 'get',
    });
    return data as PatrolLine;
  }

  // 巡检路线下拉数据
  async patrolLineLinkOpts(params:{parkOrgId:string, type:number|''}) {
    const { data } = await request({
      url: URLS.patrolLineLinkOpts,
      method: 'get',
      data: params,
    });
    return data as SelectOptions;
  }

  // 巡检计划分页查询
  async patrolPlanListPage(searchData:PatrolPlanSearchParams) {
    const { data } = await request({
      url: URLS.patrolPlanListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<PatrolPlan>>list,
      total: <number>total,
    };
  }

  // 巡检计划新增
  async patrolPlanAdd(data:PatrolPlan) {
    await request({
      url: URLS.patrolPlanAdd,
      data,
    });
  }

  // 巡检计划编辑
  async patrolPlanEdit(data:PatrolPlan) {
    await request({
      url: URLS.patrolPlanEdit,
      data,
    });
  }

  // 巡检计划删除
  async patrolPlanDel(id:string) {
    await request({
      url: `${URLS.patrolPlanDel}${id}`,
      method: 'delete',
    });
  }

  // 根据id查询巡检计划详情
  async patrolPlanFindById(id:string) {
    const { data } = await request({
      url: `${URLS.patrolPlanFindById}${id}`,
      method: 'get',
    });
    return data as PatrolPlan;
  }

  // 关联巡检人
  async patrolPlanLinkEmployees(data:LinkEmployee) {
    await request({
      url: URLS.patrolPlanLinkEmployees,
      data,
    });
  }

  // -------------巡检任务---------------------

  // 巡检任务分页
  async patrolTaskListPage(searchData:PatrolTaskSearchParams) {
    const { data } = await request({
      url: URLS.patrolTaskListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<PatrolTask>>list,
      total: <number>total,
    };
  }

  // 巡检任务流转
  async patrolTaskRotate(data:PatrolTaskRotateParams) {
    await request({
      url: URLS.patrolTaskRotate,
      data,
    });
  }

  // 根据id查询巡检流转详情
  async patrolTaskRotateFindById(id:string) {
    const { data } = await request({
      url: `${URLS.patrolTaskRotateFindById}${id}`,
      method: 'get',
    });
    return data as PatrolTask;
  }
}
export * from './type';
