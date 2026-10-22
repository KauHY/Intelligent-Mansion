/*
 * @Description:信息统计
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-23 14:34:12
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-30 13:54:18
 */
import URLS from '@/models/urlConstant/information-statistical';
import request from '~utils/axios';
import {
  FormTemplateSet,
  FormTemplateSearchParams,
  FormTemplateInfo,
  FormManageSearchParams,
  FormManageInfo,
  StatusType,
} from './type';

export default class {
  // 表单模板分页查询
  async questionnaireListPage<T>(searchData:FormTemplateSearchParams) {
    const { data } = await request<{list:FormTemplateInfo[], total:number}>({
      url: URLS.formTemplateListPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 表单模板删除
  async formTemplateDel(id:string) {
    await request({
      url: `${URLS.formTemplateDel}${id}`,
      method: 'DELETE',
    });
  }

  // 表单模板新增
  async formTemplateAdd(data:FormTemplateSet) {
    await request({
      url: URLS.formTemplateAdd,
      data,
    });
  }

  // 表单模板修改
  async formTemplateModify(data:FormTemplateSet) {
    await request({
      url: URLS.formTemplateModify,
      data,
    });
  }

  // 保单模板设计
  async formTemplateDesign(data:FormTemplateInfo) {
    // 数据处理成后台能接收的
    const editors = data.editors.map((editorsItem) => {
      const { constraintCondition, elementContent } = editorsItem;
      const tempConstraintCondition = constraintCondition ? JSON.stringify(constraintCondition) : '';
      const tempElementContent = elementContent ? JSON.stringify(elementContent) : '';
      return {
        ...editorsItem,
        constraintCondition: tempConstraintCondition,
        elementContent: tempElementContent,
      };
    });
    await request({
      url: URLS.formTemplateDesign,
      data: {
        ...data,
        editors,
      },
    });
  }

  // 表单模板下拉数据
  async formTemplateDic(parkOrgId:string) {
    const { data } = await request<SelectOptions>({
      url: URLS.formTemplateDic,
      method: 'get',
      data: {
        parkOrgId,
      },
    });
    return data;
  }

  // 根据id查询问卷详情
  async formTemplateFindById(id:string) {
    const { data } = await request<FormTemplateInfo>({
      url: `${URLS.formTemplateFindById}${id}`,
      method: 'get',
    });
    const editors = data.editors?.map((editorsItem) => {
      const { constraintCondition, elementContent } = editorsItem;
      return {
        ...editorsItem,
        constraintCondition: typeof constraintCondition === 'string' ? JSON.parse(constraintCondition) : {},
        elementContent: typeof elementContent === 'string' ? JSON.parse(elementContent) : [],
      };
    });
    return {
      ...data,
      editors,
    };
  }

  // ------------------------ 表单管理------------------------------------------
  // 表单管理分页查询
  async formManageListPage<T>(searchData:FormManageSearchParams) {
    const { data } = await request<{list:FormManageInfo[], total:number}>({
      url: URLS.formManageListPage,
      method: 'POST',
      data: searchData,
    });
    return data;
  }

  // 表单管理删除
  async formManageDel(id:string) {
    await request({
      url: `${URLS.formManageDel}${id}`,
      method: 'DELETE',
    });
  }

  // 表单管理新增
  async formManageAdd(data:FormManageInfo) {
    await request({
      url: URLS.formManageAdd,
      data,
    });
  }

  // 表单管理修改
  async formManageModify(data:FormTemplateSet) {
    await request({
      url: URLS.formManageModify,
      data,
    });
  }

  // 表单管理设计
  async formManageUpdateStatus(data:{id:string, status:StatusType}) {
    await request({
      url: URLS.formManageUpdateStatus,
      data,
    });
  }

  // 根据id查询问卷详情
  async formManageFindById(id:string) {
    const { data } = await request<FormManageInfo>({
      url: `${URLS.formManageFindById}${id}`,
      method: 'get',
    });
    return data;
  }
}

export * from './type';
