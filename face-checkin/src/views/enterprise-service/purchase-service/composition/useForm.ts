/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-15 15:46:31
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import { message } from 'ant-design-vue';
import useForm from '~utils/useForm';
import { LaneInfo } from '~models/purchase-service';
/**
 * @description: 采购
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const procurement = (() => {
  const createInstance = () => {
    const modelRef = reactive<LaneInfo>({
      id: '',
      parkOrgId: null, // 园区ID
      companyId: '', // 企业名称
      name: '', // 采购名称
      purchaseType: null, // 采购类型
      detail: '', // 采购详情
      effectiveEndTime: '', // 截至时间
      attKey: '', // 附件
      jumpType: 0, // 跳转类型
      url: '', // 跳转地址
      contact: '', // 联系人
      tel: '', // 联系电话
      remark: '', // 备注

    });
    const valid = (rules:unknown, value:string) => {
      if (!value) return Promise.resolve();
      if (!REGS.mobile.test(value)) return Promise.reject('请输入正确手机号');
      return Promise.resolve();
    };
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择园区' }],
      companyId: [{ required: true, message: '请选择企业' }],
      name: [{ required: true, message: '请输入服务名称' }],
      purchaseType: [{ required: true, message: '请选择采购类型' }],
      detail: [{ required: true, message: '请输入采购详情' }],
      effectiveEndTime: [{
        validator: (rules:unknown, value:string) => {
          // console.log('value', value, modelRef.effectiveEndTime, 'rules', rules);
          const valueDate = new Date(value);
          const current = new Date();
          if (valueDate < current) return Promise.reject('截至时间需大于当前时间');
          if (!value && !modelRef.effectiveEndTime) return Promise.reject('请选择截止时间');
          return Promise.resolve();
        },
        required: true,
      }],
      url: [{ required: false, message: '请输入跳转地址' }],
      jumpType: [{
        required: true,
        message: '请选择跳转类型',
        validator: (rules:unknown, value:string) => {
          if (modelRef.jumpType === 2) {
            rulesRef.url[0].required = true;
            return Promise.resolve();
          }
          rulesRef.url[0].required = false;
          return Promise.resolve();
        },
      }],
      tel: [{ validator: valid, trigger: ['change', 'blur'] }],
      remark: [{ required: false, max: 200 }],
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
