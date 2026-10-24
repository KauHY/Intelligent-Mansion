/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-21 10:22:38
 * @LastEditors: your name
 * @LastEditTime: 2022-03-14 17:29:17
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
import { ChargeItem } from '~models/collect-fee';
import useForm from '~utils/useForm';

export const useAddChargeItem = (() => {
  const createInstance = () => {
    const modelRef = reactive<ChargeItem>({
      chargeName: '', // 收费项名称
      feeType: null, // 收费项类型
      id: '',
      parkOrgId: '', // 园区
      payeeId: null, // 收款方
      remark: '', // 备注
      status: 1, // 状态：0-关闭；1-开启
      taxRate: 0, // 税率
    });
    const rulesRef = reactive({
      payeeId: [{ required: true, message: '请选择收款方' }],
      parkOrgId: [{ required: true, message: '请选择园区' }],
      chargeName: [{ required: true, message: '请输入费用项名称' }],
      feeType: [{ required: true, message: '请选择费用类型' }],
      taxRate: [{
        validator: (rules:unknown, value:string) => {
          console.log(value, '----value');
          if (value && !REGS.rate.test(value)) return Promise.reject('最多输入两位整数，小数点后最多两位');
          return Promise.resolve();
        },
      }],
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
