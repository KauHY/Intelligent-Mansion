/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:40:59
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 17:59:15
 */
import { ref } from 'vue';
import { useParkInfoOpts } from '~composition/park';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
import Park from '~models/park';
import { useRemoteFilterSelect } from '~composition/common';
// import {} from '~models/company-mangement'
const { parkOpts, getParkOpts } = useParkInfoOpts();
getParkOpts();
export default (parkOrgId:string, orderType?:1|2) => {
  const companyOpts = ref<SelectOptions>([]);
  const companyMangementInstance = new CompanyMangement();
  const init = async () => {
    if (!parkOrgId) return;
    companyOpts.value = await new Park().listCompanyDictByType(parkOrgId, 0);
  };
  const {
    loading,
    list: employeesOpts,
    remoteSearch,
    otherParams,
  } = useRemoteFilterSelect(companyMangementInstance.selectHandlerEmployeesList, {
    disableAutoSearch: true,
  });

  const companyChange = async (companyId:string) => {
    // if (modelRef) {
    //   modelRef.jobIds = [];
    //   modelRef.tagIds = [];
    // }
    // jobOpts.value = await companyMangementInstance.listJobDict(companyId);
    // tagOpts.value = await companyMangementInstance.listTagDict(companyId);
    // await companyMangementInstance.listEmployeeDict()
    otherParams.value.companyId = companyId;
    otherParams.value.orderType = orderType;
    remoteSearch('');
  };

  init();
  return {
    parkOpts,
    companyOpts,
    companyChange,
    employeesOpts,
    remoteSearch,
    loading,
  };
};
