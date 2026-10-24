/*
 * @Description: 用户管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-05 18:15:09
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-28 19:04:38
 */
import { encrypt } from '@/utils/tool';
import URLS from '~models/urlConstant/company-mangement';
import request from '~utils/axios';
import {
  SearchParams,
  CompanyInfo,
  BindHouseInfo,
  CompanyHouseBindListPageParams,
  CompanyHouseBindListPageItem,
  PageHouseBindRecordParams,
  HouseBindRecord,
  RenewHouse,
  PageRenewalRecordInfo,
  PageRenewalRecordSearchParams,
  EmployeeInfo,
  EmployeePageSearchParams,
  EmployeeUpdateStatus,
  TagPageSearchParams,
  TagInfo,
  JobPageSearchParams,
  JobInfo,
  EmployeesDictByTagSortSearchParma,
  EmployeesDictByTagSort,
  SearchEmployeesSelectOptParams,
  SelectTagTree,
  SelectTagTreeSearchParams,
  SelectEmployeesByIdAndType,
  CompanyCheckData,
} from './type';

export default class User {
  async searchListPage<T>(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.companyListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<CompanyInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 新增企业
   * @param {T} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async companySave(data:CompanyInfo) {
    // data.password = encrypt(data.password);
    await request({
      url: URLS.companyAdd,
      data,
    });
  }

  // 更改企业状态
  async updateStatus(data:any) {
    await request({
      url: URLS.updateStatus,
      data,
    });
  }

  // 导出入驻企业
  async companyExportApi(searchData:any) {
    const { data, headers } = await request({
      url: URLS.companyExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 导入入驻企业
  async importCompany(searchData: any, id:string) {
    const { data } = await request({
      url: `${URLS.importCompany}`,
      data: searchData,
    });
    return data;
  }

  // 导入员工文件压缩包
  async importEmployeesZip(searchData: any) {
    const { data } = await request({
      url: `${URLS.importEmployeesZip}`,
      data: searchData,
    });
    return data;
  }

  // 导入员工
  async importEmployeesNew(params:any) {
    const { data } = await request({
      url: `${URLS.importEmployeesNew}`,
      data: params,
    });
    return data;
  }

  /**
   * @description:删除入住企业
   * @param {string} id
   * @param {0} status
   * @return {*}
   * @author: GUOCHAO82
   */
  async companyDel(id:string) {
    await request({
      url: `${URLS.companyDel}${id}`,
      method: 'delete',
    });
  }

  async bindHouse(companyId:string, data:BindHouseInfo[]) {
    await request({
      url: `${URLS.bindHouse}${companyId}`,
      data: {
        list: data,
      },
    });
  }

  /**
   * @description: 企业已绑定房屋分页查询
   * @param {SearchParams} searchData
   * @param {boolean} init
   * @return {*}
   * @author: GUOCHAO82
   */
  async companyBindHouseListPage<T>(searchData:CompanyHouseBindListPageParams, init:boolean) {
    const { data } = await request({
      url: URLS.companyBindHouseListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<CompanyHouseBindListPageItem>>list,
      total: <number>total,
    };
  }

  /**
   * @description:解绑房屋
   * @param {string} id
   * @param {0} status
   * @return {*}
   * @author: GUOCHAO82
   */
  async unbindHouse(id:string) {
    await request({
      url: `${URLS.unbindHouse}${id}`,
    });
  }

  /**
   * @description: 房屋入住记录查询
   * @param {PageHouseBindRecordParams} searchData
   * @param {boolean} init
   * @return {*}
   * @author: GUOCHAO82
   */
  async pageHouseBindRecord<T>(searchData:PageHouseBindRecordParams) {
    const { data } = await request({
      url: URLS.pageHouseBindRecord,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<HouseBindRecord>>list,
      total: <number>total,
    };
  }

  async renewHouse(data:RenewHouse) {
    await request({
      url: `${URLS.renewHouse}`,
      data,
    });
  }

  /**
   * @description: 房屋续租记录
   * @param {PageRenewalRecordSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async pageRenewalRecord<T>(searchData:PageRenewalRecordSearchParams) {
    const { data } = await request({
      url: URLS.pageHouseRenewBindRecord,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<PageRenewalRecordInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 员工分页查询
   * @param {EmployeePageSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async employeeListPage<T>(searchData:EmployeePageSearchParams) {
    const { data } = await request({
      url: URLS.employeeListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<EmployeeInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 新增员工
   * @param {EmployeeInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async employeeAdd(data:EmployeeInfo) {
    await request({
      url: URLS.employeeAdd,
      data,
    });
  }

  /**
   * @description: 编辑员工
   * @param {CompanyInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async employeeEdit(data:EmployeeInfo) {
    await request({
      url: URLS.employeeEdit,
      data,
    });
  }

  /**
     * @description:删除员工
     * @param {string} id
     * @param {0} status
     * @return {*}
     * @author: GUOCHAO82
     */
  async employeeDel(id:string) {
    await request({
      url: `${URLS.employeeDel}${id}`,
      method: 'delete',
    });
  }

  /**
   * @description: 更新员工状态
   * @param {EmployeeUpdateStatus} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async employeeUpdateStatus(data:EmployeeUpdateStatus) {
    await request({
      url: URLS.employeeUpdateStatus,
      data,
    });
  }

  /**
   * @description: 根据公司查询员工下拉数据
   * @param {object} param1
   * @return {*}
   * @author: GUOCHAO82
   */
  // async listEmployeeDict({ value, companyId }:{value:string, companyId:string}) {
  //   const { data } = await request({
  //     url: `${URLS.listEmployeeDict}`,
  //     data: {
  //       name: value,
  //       companyId,
  //     },
  //     method: 'get',
  //   });
  //   return data as SelectOptions;
  // }

  /**
   * @description: 根据园区id查询物业企业员工下拉数据
   * @param {object} param1
   * @return {*}
   * @author: GUOCHAO82
   */
  // async listSpecialEmployeeDict({ value, orgId }:{value:string, orgId:string}) {
  //   const { data } = await request({
  //     url: `${URLS.listSpecialEmployeeDict}`,
  //     data: {
  //       name: value,
  //       orgId,
  //     },
  //     method: 'get',
  //   });
  //   return data as SelectOptions;
  // }

  /**
   * @description: 查询员工下拉数据
   * @param {object} param1
   * @return {*}
   * @author: GUOCHAO82
   */
  async selectEmployeesList(params:SearchEmployeesSelectOptParams) {
    console.log(params, '-----params');
    const { data } = await request({
      url: `${URLS.selectEmployeesList}`,
      data: {
        ...params,
        employeeName: params.value,
      },
      method: 'get',
    });
    return data as SelectOptions;
  }

  /**
   * @description: 投诉报修查询员工下拉列表
   * @param {SearchEmployeesSelectOptParams} params
   * @return {*}
   * @author: GUOCHAO82
   */
  async selectHandlerEmployeesList({ value, companyId, orderType }:{value:string, companyId:string, orderType:string}) {
    const { data } = await request({
      url: `${URLS.listHandlerDict}`,
      data: {
        name: value,
        companyId,
        orderType,
      },
      method: 'get',
    });
    return data as SelectOptions;
  }

  async employeesDictByTagSort(param?:EmployeesDictByTagSortSearchParma) {
    const { data } = await request({
      url: `${URLS.employeesDictByTagSort}`,
      data: param,
      method: 'get',
    });
    return data as EmployeesDictByTagSort[];
  }

  // -----------------选人组件接口---------------------------------------------------------------------------------

  async selectTagTree(param:SelectTagTreeSearchParams) { // 查询园区/公司/标签/树
    const { data } = await request({
      url: URLS.selectTagTree,
      data: param,
      method: 'get',
    });
    return data as SelectTagTree[];
  }

  async selectEmployeesByIdAndType<T>(searchData:SelectEmployeesByIdAndType) {
    const { data } = await request({
      url: URLS.selectEmployeesByIdAndType,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <SelectOptions>list,
      total: <number>total,
    };
  }

  /**
   * @description: 查询公司下拉数据
   * @param {string} parkOrgId
   * @return {*}
   * @author: GUOCHAO82
   */
  async listCompanyDict(parkOrgId:string) {
    const { data } = await request({
      url: URLS.listCompanyDict,
      method: 'get',
      data: {
        filterUnBindClientInfo: false,
        parkOrgId,
      },
    });
    return data as SelectOptions;
  }

  /**
   * @description: 获取当前用户的物业公司
   * @param {*}
   * @return {*}
   * @author: GUOCHAO82
   */
  async curUserPropertyCompanyDict() {
    const { data } = await request({
      url: URLS.curUserPropertyCompanyDict,
      method: 'get',
    });
    return data as SelectOptions;
  }

  /**
   * @description: 标签下拉列表
   * @param {string} parkOrgId
   * @return {*}
   * @author: GUOCHAO82
   */
  async listJobDict(companyId:string) {
    const { data } = await request({
      url: URLS.listJobDict,
      method: 'get',
      data: {
        companyId,
      },
    });
    return data as SelectOptions;
  }

  /**
   * @description: 通过公司id（可多个公司）或者园区id查询标签
   * @param {string} companyId 单个公司
   * @param {string} parkOrgId
   * @param {string[]} companyIds 多个公司
   * @return {*}
   * @author: GUOCHAO82
   */

  async listTagDict({ companyId = '', parkOrgId = '', companyIds }:{companyId?:string, companyIds?:string[], parkOrgId?:string}) {
    const { data } = await request({
      url: URLS.listTagDict,
      data: {
        companyIds: companyId ? [companyId] : companyIds, // 兼容之前
        parkOrgId,
      },
    });
    return data as SelectOptions;
  }

  async employeeDownload(searchData:EmployeePageSearchParams) {
    const { data, headers } = await request({
      url: URLS.employeeDownload,
      data: searchData,
      responseType: 'blob',
      // method: 'get',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 标签
  /**
   * @description: 标签分页查询
   * @param {TagPageSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async tagListPage<T>(searchData:TagPageSearchParams) {
    const { data } = await request({
      url: URLS.listTagPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<TagInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 新增标签
   * @param {TagInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async tagAdd(data:TagInfo) {
    await request({
      url: URLS.tagAdd,
      data,
    });
  }

  /**
   * @description: 编辑标签
   * @param {CompanyInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async tagEdit(data:TagInfo) {
    await request({
      url: URLS.tagEdit,
      data,
    });
  }

  /**
     * @description:删除标签
     * @param {string} id
     * @param {0} status
     * @return {*}
     * @author: GUOCHAO82
     */
  async tagDel(id:string) {
    await request({
      url: `${URLS.tagDel}${id}`,
      method: 'delete',
    });
  }

  /**
   * @description: 岗位分页查询
   * @param {JobPageSearchParams} searchData
   * @return {*}
   * @author: GUOCHAO82
   */
  async jobListPage<T>(searchData:JobPageSearchParams) {
    const { data } = await request({
      url: URLS.listJobPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<JobInfo>>list,
      total: <number>total,
    };
  }

  /**
   * @description: 新增岗位
   * @param {JobInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async jobAdd(data:JobInfo) {
    await request({
      url: URLS.jobAdd,
      data,
    });
  }

  /**
   * @description: 岗位标签
   * @param {JobInfo} data
   * @return {*}
   * @author: GUOCHAO82
   */
  async jobEdit(data:JobInfo) {
    await request({
      url: URLS.jobEdit,
      data,
    });
  }

  /**
     * @description:删除岗位
     * @param {string} id
     * @param {0} status
     * @return {*}
     * @author: GUOCHAO82
     */
  async jobDel(id:string) {
    await request({
      url: `${URLS.jobDel}${id}`,
      method: 'delete',
    });
  }

  async importEmployees(searchData: any, id:string) {
    const { data } = await request({
      url: `${URLS.importEmployees}`,
      data: searchData,
    });
    return data;
  }

  // 入住企业审核分页查询
  async companyCheckListPageApi<T>(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.companyCheckListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<CompanyInfo>>list,
      total: <number>total,
    };
  }

  // 入住企业审核详情
  async companyCheckDetailApi(id:string) {
    const { data } = await request({
      url: `${URLS.companyCheckDetail}/${id}`,
      method: 'GET',
    });
    return data;
  }

  // 入住企业审核
  async companyCheckVerifyApi(data:CompanyCheckData) {
    await request({
      url: URLS.companyCheckVerify,
      data,
    });
  }

  // 入住企业审核信息
  async companyCheckInfoListPageApi<T>(searchData:SearchParams, init:boolean) {
    const { data } = await request({
      url: URLS.companyCheckInfoListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<CompanyInfo>>list,
      total: <number>total,
    };
  }

  // 入住企业审核信息详情
  async companyCheckInfoDetailApi(id:string) {
    const { data } = await request({
      url: `${URLS.companyCheckInfoDetail}/${id}`,
      method: 'GET',
    });
    return data;
  }

  // 入住企业审核信息审核
  async companyCheckInfoVerifyApi(data:CompanyCheckData) {
    await request({
      url: URLS.companyCheckInfoVerify,
      data,
    });
  }
}

export * from './type';
