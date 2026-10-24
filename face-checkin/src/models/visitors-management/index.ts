/*
 * @Description: 用户管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-01 17:16:24
 */
import { encrypt } from '@/utils/tool';
import URLS from '~models/urlConstant/visitors-management';
import request from '~utils/axios';
import {
  VisitorSubscribeSearchParams,
  VisitorInfo,
  RegisterInfo,
  AuditPersonSearchParams,
  AuditPerson,
  VisitorType,
  VisitorTypeSearchParams,
  VisitorProcess,
  VisitorProcessSearchParams,
  CompanyBindInfo, // 企业绑定信息
  CompanyBindInfoSearchParams,
  visitingInstructionsSearchParams,
  visitingInstructions,
} from './type';

export default class VisitorsManagement {
  /**
   * @description: 访客预约记录分页查询
   * @param {VisitorSubscribeSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorSubscribeListPage<T>(searchData:VisitorSubscribeSearchParams) {
    const { data } = await request({
      url: URLS.visitorSubscribeListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<VisitorInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 历史访客分页查询
   * @param {VisitorSubscribeSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async historyVisitorListPage<T>(searchData:VisitorSubscribeSearchParams) {
    const { data } = await request({
      url: URLS.historyVisitorListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<VisitorInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 当前访客分页查询
   * @param {VisitorSubscribeSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async currentVisitorListPage<T>(searchData:VisitorSubscribeSearchParams) {
    const { data } = await request({
      url: URLS.currentVisitorListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<VisitorInfo>>list,
      total: <number>total,
    };
  }

  async visitorDetails(id:string) {
    const { data } = await request({
      url: `${URLS.visitorDetails}${id}`,
      method: 'get',
    });
    return data as RegisterInfo;
  }

  // 迁出
  async visitingSignOut(data:any) {
    await request({
      url: `${URLS.visitingSignOut}`,
      method: 'PUT',
      data,
    });
  }

  /**
   * @description: 登记
   * @param {RegisterInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorRegister(data:RegisterInfo) {
    await request({
      url: URLS.visitorRegister,
      data,
    });
  }

  // -------------------------------------------------审批人配置-------------------------------------------------
  /**
   * @description: 审批人分页查询
   * @param {AuditPersonSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async auditPersonListPage<T>(searchData:AuditPersonSearchParams) {
    const { data } = await request({
      url: URLS.auditPersonListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<AuditPerson>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 根据id查询审批人详情
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async auditPersonDetails(id:string) {
    const { data } = await request({
      url: `${URLS.auditPersonDetails}${id}`,
      method: 'get',
    });
    data.auditEmployeeIds = data.auditEmployeeIds ? data.auditEmployeeIds.split(',') : [];
    data.auditEmployeeNames = data.auditEmployeeNames ? data.auditEmployeeNames.split(',') : [];
    return data as AuditPerson;
  }

  /**
   * @description: 删除审批人
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async auditPersonDel(id:string) {
    await request({
      url: `${URLS.auditPersonDel}${id}`,
      method: 'DELETE',
    });
  }

  /**
   * @description: 删除访客
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async removePerson(id:string) {
    await request({
      url: `${URLS.removePerson}${id}`,
      method: 'DELETE',
    });
  }

  /**
   * @description: 新增审批人
   * @param {AuditPerson} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async auditPersonAdd(data:AuditPerson) {
    await request({
      url: URLS.auditPersonAdd,
      data,
    });
  }

  async auditPersonEdit(data:AuditPerson) {
    await request({
      url: URLS.auditPersonEdit,
      data,
      method: 'put',
    });
  }

  // -----------------------------------------------访客类型配置-------------------------------------------------

  /**
   * @description: 访客类型分页查询
   * @param {VisitorTypeSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorTypeListPage<T>(searchData:VisitorTypeSearchParams) {
    const { data } = await request({
      url: URLS.visitorTypeListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<VisitorType>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 根据id查询访客类型详情
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorTypeDetails(id:string) {
    const { data } = await request({
      url: `${URLS.visitorTypeDetails}${id}`,
      method: 'get',
    });
    return data as VisitorType;
  }

  async visitorTypeDic(companyId:string) {
    const { data } = await request({
      url: URLS.visitorTypeDic,
      data: {
        companyId,
      },
      method: 'get',
    });
    return data as SelectOptions;
  }

  /**
   * @description: 删除访客类型
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorTypeDel(id:string) {
    await request({
      url: `${URLS.visitorTypeDel}${id}`,
      method: 'DELETE',
    });
  }

  /**
   * @description: 新增访客类型
   * @param {VisitorType} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorTypeAdd(data:VisitorType) {
    await request({
      url: URLS.visitorTypeAdd,
      data,
    });
  }

  async visitorTypeEdit(data:VisitorType) {
    await request({
      url: URLS.visitorTypeEdit,
      data,
    });
  }

  // -----------------------------------------访客流程接口----------------------------------------------

  /**
   * @description: 访客流程分页查询
   * @param {VisitorProcessSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorProcessListPage<T>(searchData:VisitorProcessSearchParams) {
    const { data } = await request({
      url: URLS.visitorProcessListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<VisitorProcess>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 根据id查询访客流程详情
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorProcessDetails(id:string) {
    const { data } = await request({
      url: `${URLS.visitorProcessDetails}${id}`,
      method: 'get',
    });
    return data as VisitorProcess;
  }

  /**
   * @description: 删除访客流程
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorProcessDel(id:string) {
    await request({
      url: `${URLS.visitorProcessDel}${id}`,
      method: 'DELETE',
    });
  }

  /**
   * @description: 新增访客流程
   * @param {VisitorProcess} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitorProcessAdd(data:VisitorProcess) {
    await request({
      url: URLS.visitorProcessAdd,
      data,
    });
  }

  async visitorProcessEdit(data:VisitorProcess) {
    await request({
      url: URLS.visitorProcessEdit,
      data,
    });
  }

  async visitorProcessModifyStatus(data:{id:string, enable:0|1}) {
    await request({
      url: URLS.visitorProcessModifyStatus,
      data,
    });
  }

  // ----------------------------------------------企业绑定信息-----------------------------------------------------
  /**
   * @description: 企业绑定信息分页查询
   * @param {CompanyBindInfoSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async companyBindInfoListPage<T>(searchData:CompanyBindInfoSearchParams) {
    const { data } = await request({
      url: URLS.companyBindInfoListPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<CompanyBindInfo>>list,
      total: <number>total,
    };
  }

  /**
     * @description: 根据id查询企业绑定信息详情
     * @param {string} id
     * @return {*}
     * @author: GUOCHAO82
     */
  async companyBindInfoDetails(id:string) {
    const { data } = await request({
      url: `${URLS.companyBindInfoDetails}${id}`,
      method: 'get',
    });
    return data as CompanyBindInfo;
  }

  /**
     * @description: 删除企业绑定信息
     * @param {string} id
     * @return {*}
     * @author: GUOCHAO82
     */
  async companyBindInfoDel(id:string) {
    await request({
      url: `${URLS.companyBindInfoDel}${id}`,
      method: 'DELETE',
    });
  }

  /**
     * @description: 编辑企业绑定信息
     * @param {CompanyBindInfo} data
     * @return {*}
     * @author: GUOCHAO82
     */
  async companyBindInfoEdit(data:CompanyBindInfo, type?:'add'|'edit') {
    await request({
      url: URLS.companyBindInfoEdit,
      data,
      method: type === 'add' ? 'post' : 'put',
    });
  }

  // -----------------------------------------来访须知接口----------------------------------------------

  /**
   * @description: 来访须知分页查询
   * @param {visitingInstructionsSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitingInstructionsPage<T>(searchData:visitingInstructionsSearchParams) {
    const { data } = await request({
      url: URLS.visitingInstructionsPage,
      method: 'POST',
      data: searchData,
    });

    const { list, total } = data;
    return {
      list: <Array<VisitorProcess>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 根据id查询来访须知详情
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitingInstructionsDetails(id:string) {
    const { data } = await request({
      url: `${URLS.visitingInstructionsDetails}${id}`,
      method: 'get',
    });
    return data as VisitorProcess;
  }

  /**
   * @description: 删除来访须知
   * @param {string} id
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitingInstructionsDel(id:string) {
    await request({
      url: `${URLS.visitingInstructionsDel}${id}`,
      method: 'DELETE',
    });
  }

  /**
   * @description: 新增来访须知
   * @param {visitingInstructions} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async visitingInstructionsAdd(data:visitingInstructions) {
    await request({
      url: URLS.visitingInstructionsAdd,
      data,
    });
  }

  async visitingInstructionsEdit(data:visitingInstructions) {
    await request({
      url: URLS.visitingInstructionsEdit,
      data,
    });
  }
}

export * from './type';
