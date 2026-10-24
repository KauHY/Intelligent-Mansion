import {
  onUnmounted, reactive,
} from 'vue';
import { cardCreatedParams } from '~models/equipment-management';

export const customerForm = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<cardCreatedParams>({
      cardMedium: 1, // 卡片介质
      cardNo: '', //
      cardType: 1,
      companyId: '', // 所属企业Id
      effectEndDate: '', // 生效结束日期
      effectStartDate: '', // 生效开始日期
      employeeId: '', // 关联员工Id
      employeeName: '',
      id: '',
      parkOrgId: '', // 园区组织Id
      remark: '', // 备注
    });
    const rulesRef = reactive({
      cardNo: [{
        validator: (rules:unknown, value:string) => {
          // const patrn = /^(^[0-9]*$)|(^[A-Za-z]+$)/;
          // const patrn = /^[0-9A-F]+$/;
          // if (!patrn.test(value)) {
          //   return Promise.reject('卡号由8-16位数字或大写字母组成，输入不合法');
          // }
          if (value.length < 8) {
            return Promise.reject('请输入8-16位大写字母(A-F)或数字');
          }
          return Promise.resolve();
        },
        required: true,
        message: '请输入8-16位大写字母(A-F)或数字',
      }],
      parkOrgId: { required: true, message: '请选择所属园区' },
      companyId: { required: true, message: '请选择所属企业' },
      employeeId: { required: true, message: '请选择人员' },
      effectStartDate: { required: true, message: '请选择授权有效期' },
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
    return {
      fill,
      modelRef,
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
