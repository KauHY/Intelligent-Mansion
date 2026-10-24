/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-21 10:22:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 16:13:29
 */

/**
 * @description:
 * @param {string} formName
 * @return {*}
 * @author: GUOCHAO82
 */
import {
  onUnmounted, reactive,
} from 'vue';
import { REGS } from '@/constants/validate';
import { BaseHousingInfo } from '~models/wisdom-canvass-business';
import useForm from '~utils/useForm';

export const useAddBusinessInfo = (() => {
  const createInstance = () => {
    const modelRef = reactive<BaseHousingInfo>({
      id: '',
      stationNum: null, // 容纳工位
      visitTime: '', // 看房时间
      chargeType: null, // 押付方式
      freeRentMonth: null, // 免租期
      canDivided: false, // 能否分割
      unitRent: '', // 单位租金
      houseId: '',
      earliestRentTime: '', // 最早租期
      visitStartTime: '',
      visitEndTime: '',
      parkOrgId: '',
      minRentMonth: null, // minRentMonth
      discussPersonally: false,
    });
    const rulesRef = reactive({
      stationNum: [{
        validator: (rules:unknown, value:string) => {
          if (value && !REGS.notNegativeNumber.test(value)) return Promise.reject('请输入容纳工位，只能输入整数');
          return Promise.resolve();
        },
      }],
      unitRent: [{
        validator: (rules:unknown, value:string) => {
          // debugger;
          if (modelRef.discussPersonally) return Promise.resolve();
          if (!REGS.area.test(value)) return Promise.reject('请输入租金，只能输入数字，小数点后最多两位');
          return Promise.resolve();
        },
        required: true,
      }],
      freeRentMonth: [{
        validator: (rules:unknown, value:string) => {
          if (value && !/(^[1-9]\d*$)/.test(value)) return Promise.reject('请输入免租期，只能输入整数');
          return Promise.resolve();
        },
      }],
      minRentMonth: [{
        validator: (rules:unknown, value:string) => {
          if (value && !/(^[1-9]\d*$)/.test(value)) return Promise.reject('请输入最短租期，只能输入整数');
          return Promise.resolve();
        },
      }],
      houseId: [{ required: true, message: '请选择房屋' }],
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

    const { validateInfos, validate, validateField } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      validateInfos,
      validate,
      rulesRef,
      validateField,
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
