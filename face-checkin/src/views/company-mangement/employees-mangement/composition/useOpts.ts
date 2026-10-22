/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:40:59
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-07 17:26:36
 */
import { ref } from 'vue';
import { useParkInfoOpts } from '~composition/park';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';

const { parkOpts, getParkOpts } = useParkInfoOpts();
getParkOpts();
const companyMangementInstance = new CompanyMangement();
export default (modelRef?:any) => {
  const tagOpts = ref<SelectOptions>([]); // 标签下来数据
  const jobOpts = ref<SelectOptions>([]); // 岗位下拉数据
  const companyOpts = ref<SelectOptions>([]);
  const parkOrgIdChange = async (parkOrgId:string, isClear = true) => { // 切换园区
    companyOpts.value = await companyMangementInstance.listCompanyDict(parkOrgId);
    if (modelRef && isClear)modelRef.companyId = '';
  };
  const companyChange = async (companyId:string, isClear = true) => {
    if (modelRef && isClear) {
      modelRef.jobIds = [];
      modelRef.tagIds = [];
    }
    jobOpts.value = await companyMangementInstance.listJobDict(companyId);
    tagOpts.value = await companyMangementInstance.listTagDict({ companyId });
  };
  return {
    parkOpts,
    tagOpts,
    jobOpts,
    companyOpts,
    parkOrgIdChange,
    companyChange,
  };
};
