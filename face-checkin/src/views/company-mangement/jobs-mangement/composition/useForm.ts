/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-27 11:53:33
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { JobInfo } from '~models/company-mangement';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddJob = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<JobInfo>({
      companyId: '',
      companyName: '',
      companyType: '0',
      employeeIds: [],
      employeeNames: [],
      id: '',
      jobCode: null,
      parkOrgId: '',
      parkOrgName: '',
    });
    const rulesRef = reactive({
      jobCode: [{ required: true, message: '请选择岗位' }],
      parkOrgId: [{ required: true, message: '请选择园区' }],
      companyId: [{ required: true, message: '请选择所属企业' }],
    });

    const fill = () => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key]) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
    };
    const { validateInfos, validate } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      validateInfos,
      validate,
      rulesRef,
    };
  };

  // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return (defaultModelRef?: any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
