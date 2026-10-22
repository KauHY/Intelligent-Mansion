import URLS from '~models/urlConstant/person-pass';
import request from '~utils/axios';
import {
  PersonPassRecordSearchParams,
  PersonPassRecord,
  AccessPolicySearchParams,
  AccessPolicy,
} from './type';

export default class PersonPass {
  async personPassRecordListPage<T>(searchData:PersonPassRecordSearchParams) {
    const { data } = await request({
      url: URLS.personPassRecordListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<PersonPassRecord>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 抓拍记录分页
   * @param {PersonPassRecordSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async personPassSnapRecordListPage<T>(searchData:PersonPassRecordSearchParams) {
    const { data } = await request({
      url: URLS.personPassSnapRecordListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<PersonPassRecord>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 通行记录导出
   * @param {PersonPassRecordSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async personPassRecordExport(searchData:PersonPassRecordSearchParams) {
    const { data, headers } = await request({
      url: URLS.personPassRecordExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  /**
   * @description: 抓拍记录导出
   * @param {PersonPassRecordSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async personPassSnapRecordExport(searchData:PersonPassRecordSearchParams) {
    const { data, headers } = await request({
      url: URLS.personPassSnapRecordExport,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  async personPassRecordFindById(id:string) {
    const { data } = await request({
      url: `${URLS.personPassRecordFindById}${id}`,
      method: 'get',
    });
    return data as PersonPassRecord;
  }

  /**
   * @description: 通行策略分页
   * @param {AccessPolicySearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async accessPolicyListPage<T>(searchData:AccessPolicySearchParams) {
    const { data } = await request({
      url: URLS.accessPolicyListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<AccessPolicy>>list,
      total: <number>total,
    };
  }

  async accessPolicyListPageAppointment<T>(searchData:any) {
    const { data } = await request({
      url: URLS.accessPolicyListPage,
      method: 'POST',
      data: searchData,
    });
    const { list } = data;
    return {
      list: <Array<AccessPolicy>>list,
    };
  }

  async accessPolicyDel(id:string) {
    await request({
      url: `${URLS.accessPolicyDel}${id}`,
      method: 'DELETE',
    });
  }

  async accessPolicyAdd(data:AccessPolicy) {
    await request({
      url: URLS.accessPolicyAdd,
      data,
    });
  }

  async listDropDownDevices(parkOrgId:any, deviceTypes:any) {
    const { data } = await request({
      url: URLS.listDropDownDevices,
      data: {
        parkOrgId,
        deviceTypes,
      },
    });
    return data;
  }

  /**
   * @description: 修改状态
   * @param {object} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async accessPolicySetState(data:{id:string, status:0|1}) {
    await request({
      url: URLS.accessPolicySetState,
      data,
    });
  }

  async accessPolicyModify(data:AccessPolicy) {
    await request({
      url: URLS.accessPolicyModify,
      data,
      method: 'put',
    });
  }

  async accessPolicyFindById(id:string) {
    const { data } = await request({
      url: `${URLS.accessPolicyFindById}${id}`,
      method: 'get',
    });
    return data as AccessPolicy;
  }
}

export * from './type';
