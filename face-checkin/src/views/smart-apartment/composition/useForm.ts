import { REGS } from '@/constants/validate';
import {
  onUnmounted, reactive,
} from 'vue';
import { AddParams } from '~models/smarter-apartment';

export const customerForm = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<AddParams>({
      companyId: '', // 所属企业Id
      deviceIds: [], // 公寓门禁设备id集合
      effectEndDate: '', // 生效结束日期
      effectStartDate: '', // 生效开始日期
      employeeId: '', // 关联员工Id
      houseIds: [], // 关联房屋id集合
      id: '',
      idCardNumber: '', // 身份证号
      parkOrgId: '', // 园区组织Id
      remark: '', // 备注
      houseNames: [],
    });
    const rulesRef = reactive({
      employeeId: { required: true, message: '请选择人员' },
      parkOrgId: { required: true, message: '请选择园区' },
      companyId: { required: true, message: '请选择企业' },
      houseIds: { required: true, message: '请选择房屋' },
      effectStartDate: { required: true, message: '请选择日期' },
      deviceIds: { required: true, message: '请选择通行设备' },
      idCardNumber: [{
        validator: (rules:unknown, value:string) => {
          if (!value) return Promise.resolve();
          if (!REGS.idCard.test(value)) return Promise.reject('请输入正确的身份证号码');
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
