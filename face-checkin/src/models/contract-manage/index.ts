/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-12 14:19:20
 * @LastEditTime: 2022-04-20 15:39:58
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import URLS from '@/models/urlConstant/contract-manage';
import request from '~utils/axios';
import {
  SearchContractParams,
  Contract,
  ContractList,
  TerminationAndChange,
  VerifyInfo,
  ContractType,
  TemplateListSearchParams,
  ContractTemplateListInfo,
} from './type';

export default class ContractVo {
  // 合同-分页查询
  async contractListPageApi<T>(searchData:SearchContractParams, init:boolean) {
    const { data } = await request({
      url: URLS.contractListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<ContractList>>list,
      total: <number>total,
    };
  }

  async listBillingSchemePageApi<T>(searchData:SearchContractParams) {
    const { data } = await request({
      url: URLS.listBillingSchemePage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<any>>list,
      total: <number>total,
    };
  }

  // 合同-新增
  async contractCreateApi(data:Contract) {
    await request({
      url: URLS.contractCreate,
      data,
    });
  }

  // 合同-提交审核
  async contractSubmitForReviewApi(id:string) {
    await request({
      url: `${URLS.contractSubmitForReview}/${id}`,
    });
  }

  // 合同-审核
  async contractReviewApi(data:VerifyInfo) {
    await request({
      url: URLS.contractReview,
      data,
    });
  }

  // 合同-变更
  async contractChangeApi(data:Contract) {
    await request({
      url: URLS.contractChange,
      data,
    });
  }

  // 合同-退约
  async contractWithdrawalApi(data:TerminationAndChange) {
    await request({
      url: URLS.contractWithdrawal,
      data,
    });
  }

  // 合同-修改
  async contractModifyApi(data:Contract) {
    await request({
      url: URLS.contractModify,
      data,
    });
  }

  // 合同作废
  async contractNullifyApi(id:string) {
    await request({
      url: `${URLS.contractNullify}/${id}`,
    });
  }

  // 合同删除
  async contractRemoveApi(id:string) {
    await request({
      url: `${URLS.contractRemove}/${id}`,
      method: 'DELETE',
    });
  }

  // 合同查找
  async contractFindByIdApi(id:string) {
    const { data } = await request({
      url: `${URLS.contractFindById}/${id}`,
      method: 'GET',
    });
    return data;
  }

  // 合同终止
  async contractTerminationApi(id:string) {
    await request({
      url: `${URLS.contractTermination}/${id}`,
    });
  }

  // 合同续约
  async contractExtendApi(data:Contract) {
    await request({
      url: URLS.contractExtend,
      data,
    });
  }

  // 导出合同
  async contractExportApi(searchData:SearchContractParams) {
    const { data, headers } = await request({
      url: URLS.contractExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // 合同校验编号冲突
  async contractCheckNoConflictApi(id:string) {
    await request({
      url: `${URLS.contractCheckNoConflict}/${id}`,
      method: 'GET',
    });
  }

  // 合同-根据类型获取编号
  async contractGetNoByTypeApi(type:string) {
    const { data } = await request({
      url: `${URLS.contractGetNoByType}?type=${type}`,
      method: 'GET',
    });
    return data;
  }

  // 合同-获取合同日期内待生效和生效中所有的房屋id
  async contractUnavailableHouseApi(params:any) {
    const { data } = await request({
      url: `${URLS.contractUnavailableHouse}`,
      data: params,
    });
    return data;
  }

  async calculationListByFeeTypeApi(type:number|string) {
    const { data } = await request<SelectOptions>({
      url: `${URLS.calculationListByFeeType}/${type}`,
      method: 'get',
    });
    return data;
  }

  // 合同-分页查询
  async houseStatusListPageApi<T>(searchData:SearchContractParams, init:boolean) {
    const { data } = await request({
      url: URLS.houseStatusListPage,
      method: 'POST',
      data: searchData,
    });
    const { list, total } = data;
    return {
      list: <Array<ContractList>>list,
      total: <number>total,
    };
  }

  // 导出房源状态
  async houseStatusExportApi(searchData:any) {
    const { data, headers } = await request({
      url: URLS.houseStatusExport,
      data: searchData,
      responseType: 'blob',
    });
    return {
      data,
      contentDisposition: headers['content-disposition'],
    } as DownloadReturnData;
  }

  // --------------- 模板字段管理--------------------------------

  async getTemplateField(type:ContractType) {
    const { data } = await request<SelectOptionsMap>({
      url: `${URLS.getTemplateField}${type}`,
      method: 'get',
    });
    return data;
  }

  // 合同模板分页查询
  async templateListPage(searchData:TemplateListSearchParams) {
    const { data } = await request<{list:ContractTemplateListInfo[], total:number}>({
      url: URLS.templateListPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 合同删除
  async templateDel(id:string) {
    await request({
      url: `${URLS.templateDel}/${id}`,
      method: 'DELETE',
    });
  }

  async templateAdd(formData:ContractTemplateListInfo) {
    const { data } = await request({
      url: URLS.templateAdd,
      data: formData,
    });
    return data;
  }

  // 合同模板-生成查询
  async generateContract(searchData: any) {
    const { data } = await request({
      url: URLS.generateContract,
      method: 'POST',
      data: searchData,
    });
    return data;
  }
}

export * from './type';
