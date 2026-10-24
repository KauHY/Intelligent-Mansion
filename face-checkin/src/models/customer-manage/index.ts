/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 11:29:09
 * @LastEditTime: 2022-03-08 19:02:21
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import URLS from '@/models/urlConstant/customer-manage';
import request from '~utils/axios';
import {
  SearchcustomerParams,
  CustomerListKind,
  CustomerInfoKind,
  CustomerTagListKind,
  CustomerTagInfoKind,
} from './type';

export default class CustomerVo {
  // 客户管理-分页查询
  async customerListPageApi<T>(searchData:SearchcustomerParams, init:boolean) {
    const { data } = await request({
      url: URLS.customerListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<CustomerListKind>>list,
      total: <number>total,
    };
  }

  // 客户管理-新增
  async customerCreateApi(data:CustomerInfoKind) {
    await request({
      url: URLS.customerAdd,
      data,
    });
  }

  // 客户管理-修改
  async customerModifyApi(data:CustomerInfoKind) {
    await request({
      url: URLS.customerModify,
      data,
    });
  }

  // 客户管理删除
  async customerRemoveApi(id:string) {
    await request({
      url: `${URLS.customerDel}/${id}`,
      method: 'DELETE',
    });
  }

  // 客户管理查找
  async customerFindByIdApi(id:string) {
    const { data } = await request({
      url: `${URLS.customerFindById}/${id}`,
      method: 'GET',
    });
    return data;
  }

  // 客户标签-分页查询
  async customerTagListPageApi<T>(searchData:SearchcustomerParams, init:boolean) {
    const { data } = await request({
      url: URLS.customerTagListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<CustomerTagListKind>>list,
      total: <number>total,
    };
  }

  // 客户标签-新增
  async customerTagCreateApi(data:CustomerTagInfoKind) {
    await request({
      url: URLS.customerTagAdd,
      data,
    });
  }

  // 客户标签-修改
  async customerTagModifyApi(data:CustomerTagInfoKind) {
    await request({
      url: URLS.customerTagModify,
      data,
    });
  }

  // 客户标签删除
  async customerTagRemoveApi(id:string) {
    await request({
      url: `${URLS.customerTagDel}/${id}`,
      method: 'DELETE',
    });
  }

  // 客户标下拉
  async listCustomerTagDictApi(id:string) {
    const { data } = await request({
      url: `${URLS.listCustomerTagDict}/${id}`,
      method: 'GET',
    });
    return data;
  }

  async customerExportApi(searchData:SearchcustomerParams) {
    const { data, headers } = await request({
      url: URLS.customerExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async customerImportApi(params:any) {
    const { data } = await request({
      url: '/park-device/meter/importMeters/2',
      data: params,
    });
    return data;
  }

  // 导入
  async listCustomerImport(searchData: any) {
    const { data } = await request({
      url: URLS.listCustomerImport,
      data: searchData,
    });
    return data;
  }
}

export * from './type';
