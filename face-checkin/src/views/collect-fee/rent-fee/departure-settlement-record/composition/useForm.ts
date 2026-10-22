import { REGS } from '@/constants/validate';
import {
  onUnmounted, reactive,
} from 'vue';
import dayjs from 'dayjs';
import useForm from '~utils/useForm';
import { CompanyInfoMation } from '~models/company-mangement';
import { leaveSettlementCreatedParams, settlementPayParams } from '~models/collect-fee';

export const customerForm = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<leaveSettlementCreatedParams>({
      companyId: '',	// 公司ID
      companyName:	'',	// 公司名称
      contractId:	'',	// 合同id
      contractNo:	'',	// 合同编号
      leaveDate:	'',	// 结算日期
      parkOrgId:	'',	// 园区组织ID
      parkOrgName:	'',
      contractType: 3,
    });
    const rulesRef = reactive({
      leaveDate: [{ required: true, message: '请选择退租合同' }],
      contractId: [{ required: true, message: '请选择退租合同' }],
      parkOrgId: [{ required: true, message: '请选择所属园区' }],
      companyId: [{ required: true, message: '请选择企业名称' }],
    });
    const fill = () => {
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

export const getEntryForm = () => {
  const modelRef = reactive<settlementPayParams>({
    checkoutDate: dayjs().format('YYYY-MM-DD'), // 联系人
    remark: '', // 联系电话
  });
  const rulesRef = reactive({
    checkoutDate: { required: true, message: '请选择收款日期' },
  });
  return {
    modelRef,
    rulesRef,
  };
};

export const useAddCompany = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<any>({
      companyId: '',
      companyName: '',
      contractId: '',
      contractNo: '',
      createBy: '',
      createByName: '',
      createTime: '',
      deposit: 14700,
      id: '',
      leaveDate: '',
      list: [],
      parkOrgId: '',
      parkOrgName: '',
      remark: '',
      totalAmount: 0,
    });
    const rulesRef = reactive({
      name: [{ required: true, message: '请输入企业名称' }],
      companyType: [{ required: true, message: '请选择企业类型' }],
      code: [{
        validator: (rules:unknown, value:string) => {
          if (!REGS.creditCode.test(value)) return Promise.reject('请输入18位统一社会信用代码');
          return Promise.resolve();
        },
        required: true,
      }],
      industryField: [{ required: true, message: '请选择行业领域' }],
      companyTaxesType: [{ required: true, message: '请选择企业纳税类型' }],
      companyScale: [{ required: true, message: '请选择企业纳税类型' }],
      licenses: [{ required: true, message: '请上传营业执照' }],
      enterpriseSize: [{ required: true, message: '请选择企业规模' }],
    });

    const fill = () => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
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
