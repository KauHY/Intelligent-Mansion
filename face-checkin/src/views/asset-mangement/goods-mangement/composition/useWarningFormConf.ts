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
import { SetWarning } from '~models/asset-mangement';
import useForm from '~utils/useForm';

export const useAddPatrolPlace = (() => {
  const createInstance = () => {
    const modelRef = reactive<SetWarning>({
      minStock: null,
      id: '',
    });
    const rulesRef = reactive({
      minStock: [{ required: true, message: '请输入库存下限预警值,大于0的整数，最多十位', pattern: /^([1-9]\d{0,9})$/ }],
    });
    const fill = (defaultModelRef:any) => {
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

    const { validateInfos, validate, resetFields } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      validateInfos,
      validate,
      rulesRef,
      resetFields,
    };
  };
    // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
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
