/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-07 15:12:38
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { AccessPolicy } from '~models/person-pass';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddAccessPolicy = (() => {
  const createInstance = () => {
    const modelRef = reactive<AccessPolicy>({
      allTime: 0,
      policyName: '', // 策略名称
      parkOrgId: '', // 园区id
      companyId: [], // 企业id
      allowVisitor: 0, // 访客通行：不适应，适用
      status: null,
      id: '',
      deviceNames: '', // 设备(逗号隔开)
      deviceIds: [], // 设备id(逗号隔开)
      policyExpireTime: '', // 策略过期时间
      policyStartTime: '', // 策略生效时间
      specifiedTimePeriod: {
        dayOfWeek: [], // 选中的天数数组
        startTime: '', // 开始时间
        endTime: '', // 结束时间
      },
      tagIds: [], // 允许员工标签ID(逗号隔开)
    });
    const rulesRef = reactive({
      policyName: [{ required: true, message: '请输入策略名称' }],
      parkOrgId: [{ required: true, message: '请选择园区' }],
      companyId: [{ required: true, message: '请选择企业' }],
      deviceIds: [{ required: true, message: '请选择公共门禁设备' }],
      policyStartTime: [{ required: true, message: '请选择有效期' }],
      specifiedTimePeriod: {
        dayOfWeek: [{
          validator: (rules:unknown, value:string[]) => {
            if (modelRef.allTime === 1 && !value.length) return Promise.reject('请勾选通行星期');
            return Promise.resolve();
          },
          required: modelRef.allTime === 1,
        }],
        startTime: [{
          validator: (rules:unknown, value:string) => {
            // debugger;
            if (modelRef.allTime === 1 && (!value || !modelRef.specifiedTimePeriod.endTime)) return Promise.reject('请选择每日通行时间');
            return Promise.resolve();
          },
          required: modelRef.allTime === 1,
        }],
      },

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
    // const { validateInfos, validate } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
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
