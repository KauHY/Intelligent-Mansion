/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-21 10:22:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-29 17:16:25
 */

/**
 * @description:
 * @param {string} formName
 * @return {*}
 * @author: GUOCHAO82
 */
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { REGS } from '@/constants/validate';
import { ReceiveAuditSetting } from '~models/asset-mangement';
import useForm from '~utils/useForm';

export const useAddPatrolPlace = (() => {
  const createInstance = () => {
    const modelRef = reactive<ReceiveAuditSetting>({
      companyIds: [],
      employeeIds: [],
      parkOrgId: '',
      id: '',
      parkOrgName: '',
    });
    const fill = (defaultModelRef: any) => {
      // debugger;
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
        // copyModelRef.houseIds = Object.keys(defaultModelRef.businessHouseVO);
      });
      // copyModelRef.
    };

    const { validateInfos, validate } = useForm(modelRef);
    return {
      modelRef,
      fill,
      validateInfos,
      validate,
    };
  };
  // 固定写法
  let userFormInstance: ReturnType<typeof createInstance> | null;
  return () => {
    if (!userFormInstance) {
      userFormInstance = createInstance();
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
