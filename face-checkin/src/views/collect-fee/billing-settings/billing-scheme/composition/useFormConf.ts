/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-21 10:22:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 13:51:37
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
import { BillingScheme } from '~models/collect-fee';
import useForm from '~utils/useForm';

export const useAddChargeItem = (() => {
  const createInstance = () => {
    const modelRef = reactive<BillingScheme>({
      lateCharge: 1, // 是否收取滞纳金
      lateDayAmount: null, // 每日滞纳金
      monthBy: null, // // 账单生成日期-按月
      lateType: 1, // 滞纳率计算规则
      amountWithTax: null, // 含税金额
      amountWithoutTax: null, // 不含税金额
      billingCycle: null, // 计费周期,计费周期：1-一次性；2-月；3-季；4-年
      calculationStandard: null, // 计算标准：1-建筑面积*月单价；2-套内面积*月单价；3-用量*单价；4-天单价
      carryMode: null, // 进位方式：4-四舍五入；0-向上取整；1-向下取整
      chargeItemId: '', // 收费项id
      chargeType: null, // 收费方式
      chargingCycle: 1, // 收费周期：1-与计费周期一致；2-多计费周期
      cumulativeCycle: null, // 累计周期
      dayOfMonth: null, // 账单生成日期:每月第x天生成账单
      daysForDelay: null, // 最晚缴纳日期 最晚缴纳日期 最晚缴纳日期，账单后的第几天
      exemptDays: null, // 豁免天数
      formulaType: 1, // 公式类型
      id: '',
      lateRate: null, // 滞纳率
      parkOrgId: '',
      parkOrgName: '',
      reservedDigits: null, // 保留位数,保留位数：0-取整数；1-保留一位小数；2-保留两位小数
      schemeName: '', // 方案名称
      status: null, // 状态：0-关闭；1-开启
      unitPriceWithTax: null, // 单价元（含税）
      unitPriceWithoutTax: null, // 单价元（不含税）
      taxRate: null,
    });
    const rulesRef = reactive({
      schemeName: [{ required: true, message: '请输入方案名称' }],
      parkOrgId: [{ required: true, message: '请选择园区' }],
      chargeItemId: [{ required: true, message: '请选择收费项' }],
      billingCycle: [{ required: true, message: '请选择计费周期' }],
      reservedDigits: [{ required: true, message: '请选择保留位数' }],
      carryMode: [{ required: true, message: '请选择进位方式' }],
      formulaType: [{
        required: true,
      }],
      amountWithoutTax: [{
        validator: (rules:unknown, value:string) => {
          if (modelRef.formulaType === 2) return Promise.resolve();
          if (modelRef.formulaType !== 2 && !REGS.money.test(value)) return Promise.reject('请输入固定金额（不含税），最多输入9位整数+两位小数');
          if (Number(value) === 0) return Promise.reject('请输入固定金额（不含税），最多输入9位整数+两位小数');
          return Promise.resolve();
        },
        required: true,
      }],
      lateRate: [{
        pattern: /^(([1-9]{1}\d{0,1})|(0{0}))(\.\d{0,2})?$|^0?\.\d{0,2}$/,
        message: '请输入滞纳率，最多输入两位整数，小数点后最多两位',
        required: true,
      }],
      lateDayAmount: [{
        pattern: /^[1-9]+\d*(\.\d{0,2})?$|^0?\.\d{0,2}$/,
        message: '请输入每日滞纳金，最多输入9位整数+两位小数',
        required: false,
      }],
      lateCharge: [{
        required: false,
        validator: (rules:unknown, value:string) => {
          if (modelRef.lateCharge === 0) {
            rulesRef.lateDayAmount[0].required = false;
            rulesRef.lateRate[0].required = false;
            return Promise.resolve();
          }
          rulesRef.lateRate[0].required = true;
          return Promise.resolve();
        },
      }],
      lateType: [{
        required: false,
        validator: (rules:unknown, value:string) => {
          if (modelRef.lateType !== 1) {
            rulesRef.lateDayAmount[0].required = true;
            rulesRef.lateRate[0].required = false;
            return Promise.resolve();
          } if (modelRef.lateCharge === 0) {
            rulesRef.lateRate[0].required = false;
            return Promise.resolve();
          }
          rulesRef.lateRate[0].required = true;
          rulesRef.lateDayAmount[0].required = false;
          return Promise.resolve();
        },
      }],
      // amountWithTax: [{
      //   validator: (rules:unknown, value:string) => {
      //     if (modelRef.formulaType !== 2 && !REGS.money.test(value)) return Promise.reject('请输入固定金额（含税），最多输入9位整数+两位小数');
      //     return Promise.resolve();
      //   },
      //   required: true,
      // }],
      calculationStandard: [{
        validator: (rules:unknown, value:number) => {
          if (modelRef.formulaType !== 1 && !value && value !== 0) return Promise.reject('请选择计算标准');
          return Promise.resolve();
        },
      }],
      unitPriceWithoutTax: [{
        required: true,
        validator: (rules:unknown, value:string) => {
          if (modelRef.formulaType === 1 && !value) return Promise.resolve();
          if (!REGS.money.test(value)) return Promise.reject('请输入单价元（不含税），最多输入9位整数+两位小数');
          if (Number(value) === 0) return Promise.reject('请输入单价元（不含税），最多输入9位整数+两位小数');
          return Promise.resolve();
        },
      }],
      // unitPriceWithTax: [{
      //   validator: (rules:unknown, value:string) => {
      //     if (modelRef.formulaType !== 1 && !REGS.money.test(value)) return Promise.reject('请输入单价元（含税），最多输入9位整数+两位小数');
      //     return Promise.resolve();
      //   },
      //   required: true,
      // }],
      chargeType: [{ required: true, message: '请选择收费类型' }],
      chargingCycle: [{ required: true, message: '请选择收费周期' }],
      cumulativeCycle: [{
        validator: (rules:unknown, value:string) => {
          if (modelRef.chargingCycle === 2 && !REGS.mounth.test(value)) return Promise.reject('请输入累计计费周期，只能输入1-12的整数');
          return Promise.resolve();
        },
        required: modelRef.chargingCycle === 2,
      }],
      dayOfMonth: [{
        validator: (rules:unknown, value:any) => {
          if (modelRef.feeType === 1 || modelRef.feeType === 4) {
            if (!value) return Promise.reject('请输入账单生成日期，只能输入大于0的整数，最多3位');
            const pattern = /^([1-9]\d{0,2})$/;
            if (value && !pattern.test(value)) return Promise.reject('请输入账单生成日期，只能输入大于0的整数，最多3位');
            if (value > 365) return Promise.reject('最大输入365天');
          } else {
            if (!value) return Promise.reject('请输入账单生成日期，只能输入大于0的整数，最多2位');
            const pattern = /^([1-9]\d{0,1})$/;
            if (value && !pattern.test(value)) return Promise.reject('请输入账单生成日期，只能输入大于0的整数，最多2位');
            if (value > 28) return Promise.reject('最大输入28天');
          }
          return Promise.resolve();
        },
        required: true,
      }],
      exemptDays: [{
        pattern: /^(\d{0,4})$/,
        message: '请输入豁免天数，只能输入非负的整数',
      }],
      daysForDelay: [{
        required: true,
        pattern: /^([1-9]\d{0,3})$/,
        message: '请输入缴纳日期，只能输入大于0的整数，最多4位',
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
