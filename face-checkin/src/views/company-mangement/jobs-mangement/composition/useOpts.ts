/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:40:59
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-01 18:06:52
 */
import { ref, Ref } from 'vue';
import { useParkInfoOpts } from '~composition/park';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
// import { listDictByTypes } from '~models/common';
import { useHashMapDic } from '~composition/useHashMapDic';

const { parkOpts, getParkOpts } = useParkInfoOpts();
getParkOpts();
const companyMangementInstance = new CompanyMangement();
export default (modelRef?:any, otherParams?:Ref<any>) => {
  const tagOpts = ref<SelectOptions>([]); // 标签下来数据
  const jobOpts = ref<SelectOptionsMap>({}); // 岗位下拉数据
  const companyOpts = ref<SelectOptions>([]);
  const parkOrgIdChange = async (parkOrgId:string, isClear = true) => { // 切换园区
    companyOpts.value = await companyMangementInstance.listCompanyDict(parkOrgId);
    if (modelRef && isClear)modelRef.companyId = '';
  };
  const companyChange = async (companyId:string) => {
    if (otherParams?.value) {
      otherParams.value.companyId = companyId;
    }
    console.log(otherParams, '---otherParams');
    // jobOpts.value = await companyMangementInstance.listJobDict(companyId);
    // tagOpts.value = await companyMangementInstance.listTagDict(companyId);
  };
  const init = async () => {
    const data = await useHashMapDic(['JOBCODE']);
    jobOpts.value = data.JOBCODE;
  };
  init();
  return {
    parkOpts,
    tagOpts,
    jobOpts,
    companyOpts,
    parkOrgIdChange,
    companyChange,
  };
};
