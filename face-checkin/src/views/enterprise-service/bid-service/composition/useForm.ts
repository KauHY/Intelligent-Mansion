/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-03-30 09:58:46
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import { message } from 'ant-design-vue';
import useForm from '~utils/useForm';
import { LaneInfo } from '~models/supply-service';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useLaneAdd = (() => {
  const createInstance = () => {
    const modelRef = reactive<LaneInfo>({
      id: '',
      parkOrgId: null, // 园区ID
      companyId: '', // 企业名称
      name: '', // 服务名称
      supplyType: null, // 供应类型
      detail: '', // 供应详情
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
      supplyType: [{ required: true, message: '请选择供应类型' }],
      detail: [{ required: true, message: '请输入供应详情' }],
      jumpType: [{ required: true, message: '请选择跳转类型' }],
      tel: [{ validator: valid, trigger: ['change', 'blur'] }],
      remark: [{ required: false, max: 200 }],
      url: [{ required: true, message: '请输入跳转地址' }],
    });

    const fill = (defaultModelRef?: any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || key === 'supplyType') {
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
