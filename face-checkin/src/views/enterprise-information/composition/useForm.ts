import { REGS } from '@/constants/validate';
import {
  onUnmounted, reactive,
} from 'vue';
import useForm from '~utils/useForm';
import { CustomerInfoKind, companyForms } from '~models/customer-manage';
import { CompanyInfoMation } from '~models/company-mangement';

export const customerForm = () => {
  const modelRef = reactive<CustomerInfoKind>({
    companyType: null,
    atts: '',
    licenses: '',
    remark: '',
    address: '', // 地址
    businessContactsList: [],
    businessTerm: null, // 营业期限
    code: '', // 统一社会信用代码
    companyMail: '', // 企业邮箱
    companyManagementStatus: null, // 企业经营状态
    companyScale: null, // 企业规模(人数)
    companyTaxesType: null, // 企业纳税类型
    contacts: '', // 联系人
    contactsDepartment: '', // 联系人部门
    contactsMail: '', // 联系人邮箱
    contactsPosition: '', // 联系人职位
    contactsTel: '', // 联系电话
    customerCode: '', // 客户编码
    customerTagId: '', // 客户标签
    employeeId: '', // 跟进人
    id: '',
    industryField: null, // 行业领域
    legalRepresentative: '', // 法人代表
    name: '', // 客户名称
    parkOrgId: '', // 园区id
    registeredCapital: null, // 注册资本
    status: null, // 客户状态,1，已成交，2未成交，3历史成交
    type: null, // 客户类型 0：物业公司，1：普通企业
  });
  const rulesRef = reactive({
    name: { required: true, message: '请输入设备名称' },
    parkOrgId: { required: true, message: '请选择所属园区' },
    systemType: { required: true, message: '请选择所属系统' },
    serialNumber: { required: true, message: '请输入设备SN' },
    ip: { required: true, message: '请输入ip地址' },
    port: { required: true, message: '请输入端口号' },
    communicationPort: { required: true, message: '请输入通信端口' },
    deviceId: { required: true, message: '请输入设备ID' },
    floor: { required: true, message: '请选择安装楼层' },
    buildingId: { required: true, message: '请选择安装楼栋' },
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
  return {
    fill,
    modelRef,
    rulesRef,
  };
};

export const companyForm = () => {
  const modelRef = reactive<companyForms>({
    address: '',
    atts: '',
    businessUserId: '',
    code: '',
    companyTaxesType: null,
    companyType: 0,
    createBy: null,
    createTime: '',
    deleted: false,
    enterpriseSize: 0,
    headOrgId: '',
    id: '',
    industryField: null,
    licenses: '',
    name: '',
    remark: '',
    updateBy: null,
    updateTime: '',
    corporateTax: null,
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
  return {
    fill,
    modelRef,
  };
};

export const companyTypeNames = {
  0: {
    label: '物业公司',
    value: 0,
  },
  1: {
    label: '其他',
    value: 1,
  },
  2: {
    label: '科研院所',
    value: 2,
  },
  3: {
    label: '事业单位',
    value: 3,
  },
  4: {
    label: '机关单位',
    value: 4,
  },
  5: {
    label: '科技型企业',
    value: 5,
  },
};
export const useAddCompany = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<CompanyInfoMation>({
      remark: '',
      address: '', // 地址
      code: '', // 统一社会信用代码
      companyType: null, // 企业类型
      id: '',
      licenses: '', // 营业执照
      name: '', // 企业名称
      enterpriseSize: null,
      companyTaxesType: null,
      industryField: null,
      atts: '',
      corporateTax: null,
    });
    const rulesRef = reactive({
      corporateTax: [{ required: true, pattern: /^(([1-9]{1}\d*)|(0{1}))(.\d{1,2})?$/, message: '请输入企业税收，只能是数字，最多8位，小数位最多2位' }],
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
