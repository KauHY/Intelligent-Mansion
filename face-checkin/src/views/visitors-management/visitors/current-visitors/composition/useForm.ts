/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-28 15:05:05
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { TagInfo } from '~models/company-mangement';
/**
 * @description: 迁出
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useShiftOut = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<{shiftOutType:string, id:string, signOutTime:string}>({
      id: '',
      shiftOutType: '',
      signOutTime: '',
    });
    const rulesRef = reactive({
      shiftOutType: [{ required: true, message: '请选择签出方式' }],
      signOutTime: [{
        validator: (rules:unknown, value:string) => {
          if (modelRef.shiftOutType === '2' && !value) return Promise.reject('请选择签出时间');
          return Promise.resolve();
        },
        required: true,
      }],
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
