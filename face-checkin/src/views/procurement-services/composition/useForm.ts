import { REGS } from '@/constants/validate';
import {
  onUnmounted, reactive,
} from 'vue';
import { PurchaseListItem, createSearchParams } from '~models/company';
import { CompanyInfoMation } from '~models/company-mangement';
import useForm from '~utils/useForm';

const telRule = {
  validator: (rules: unknown, value: string) => {
    if (!value) return Promise.resolve();
    if (!REGS.mobile.test(value)) return Promise.reject('请输入正确的电话号码');
    return Promise.resolve();
  },
};

export const customerForm = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<PurchaseListItem>({
      attKey: '',
      companyId: '', //  公司Id
      companyName: '', // 企业名称
      contact: '', //  联系人
      effectiveEndTime: '',
      detail: '', //
      id: '',
      jumpType: 0, // 跳转类型：0-无；1-URL；2-小程序
      name: '', // 服务名
      parkOrgId: '', // 园区Id
      parkOrgName: '', // 所属园区
      publishTime: '', // 发布时间
      purchaseType: null, //
      remark: '', // 备注
      source: null, //  数据来源：0-企业新增；1-管理员新增
      state: null, // 状态：0-待发布；1-已发布；2-停用
      tel: '', //  联系电话
      url: '',
    });
    const rulesRef = reactive({
      name: { required: true, message: '请输入采购名称' },
      parkOrgId: { required: true, message: '请选择所属园区' },
      companyId: { required: true, message: '请选择企业名称' },
      detail: { required: true, message: '请输入采购详情' },
      purchaseType: { required: true, message: '请选择采购类型' },
      url: { required: false, message: '请输入' },
      effectiveEndTime: { required: true, message: '请选择截止时间' },
      jumpType: [{
        required: false,
        message: '请选择跳转类型',
        validator: (rules:unknown, value:string) => {
          if (modelRef.jumpType === 0) {
            rulesRef.url.required = false;
            return Promise.resolve();
          }
          rulesRef.url.required = true;
          return Promise.resolve();
        },
      }],
      tel: [telRule],
    });
    const fill = () => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
      if (copyModelRef.jumpType === 1 || copyModelRef.jumpType === 2) {
        rulesRef.url.required = true;
      }
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

export const companyTypeNames = {
  0: {
    label: '物业公司',
    value: 0,
  },
  1: {
    label: '普通企业',
    value: '1',
  },
};
export const useAddCompany = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<CompanyInfoMation>({
      remark: '',
      atts: '',
      address: '', // 地址
      code: '', // 统一社会信用代码
      companyType: null, // 企业类型
      id: '',
      licenses: '', // 营业执照
      name: '', // 企业名称
      enterpriseSize: null,
      companyTaxesType: null,
      industryField: null,
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

export const getEntryForm = () => {
  const modelRef = reactive<createSearchParams>({
    contactPerson: '', // 联系人
    contactPhone: '', // 联系电话
    parkOrgId: '',
  });
  const rulesRef = reactive({
    parkOrgId: { required: true, message: '请选择所属园区' },
    // contactPhone: {
    //   required: false,
    //   validator: (rules: unknown, value: string) => {
    //     if (!REGS.mobile.test(value)) return Promise.reject('请输入正确的电话号码');
    //     return Promise.resolve();
    //   },
    // },
  });
  return {
    modelRef,
    rulesRef,
  };
};
