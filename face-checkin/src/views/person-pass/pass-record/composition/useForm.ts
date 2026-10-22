/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-07 13:55:09
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { ParkingLotInfo } from '~models/car-pass';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useParkingLot = (() => {
  const createInstance = () => {
    const modelRef = reactive<ParkingLotInfo>({
      id: '',
      numberOfSpace: null,
      parkingLotName: '',
      parkOrgId: '',
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择园区' }],
      parkingLotName: [{ required: true, message: '请输入停车场名称' }],
    });

    const fill = (defaultModelRef?: any) => {
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
