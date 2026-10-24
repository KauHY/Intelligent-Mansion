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
import useForm from '~utils/useForm';
import { GoodsInStock } from '~models/asset-mangement';

export const useOutInStockEdit = (() => {
  const createInstance = () => {
    const modelRef = reactive<GoodsInStock>({
      remark: '',
      stockInNum: null,
      stockMode: 1, // 出入库类型默认手动入库
      stockInUnitPrice: null,
      id: '',
      goodsId: '',
      parkOrgId: '',
      stockNum: null,
      goodsName: '',
      parkOrgName: '',
      unitPrice: null,
    });
    const rulesRef = reactive({
      stockMode: [{ required: true, message: '请选择出入库类型' }],
      stockInNum: [{ required: true, message: '请输入入库数量' }],
      stockInUnitPrice: [{
        validator: (rule:unknown, value:string) => {
          if (!value) return Promise.resolve();
          if (!REGS.area.test(value)) return Promise.reject('请输入数字，小数点保留两位');
          return Promise.resolve();
        },
      }],
    });
    const fill = (defaultModelRef:any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
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
