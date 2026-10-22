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
import { GoodsCard } from '~models/asset-mangement';
import useForm from '~utils/useForm';

export const useAddPatrolPlace = (() => {
  const createInstance = () => {
    const modelRef = reactive<GoodsCard>({
      id: '',
      parkOrgId: '',
      parkOrgName: '',
      remark: '', // 备注
      goodsType: null, // 物品类别
      goodsName: '', // 物品类别
      unit: '', // 计量单位
      brand: '', // 品牌
      model: '', // 型号
      unitPrice: null, // 入库单位
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择园区' }],
      goodsType: [{ required: true, message: '请选择物品类别' }],
      goodsName: [{ required: true, message: '请输入物品名称' }],
      unit: [{ required: true, message: '请输入计量单位' }],
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
