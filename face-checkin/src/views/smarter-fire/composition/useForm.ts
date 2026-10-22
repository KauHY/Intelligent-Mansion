import { REGS } from '@/constants/validate';
import {
  reactive,
} from 'vue';
import { CustomerInfoKind } from '~models/smarter-fire';

const telRule = {
  validator: (rules: unknown, value: string) => {
    if (!value) return Promise.resolve();
    if (!REGS.mobile.test(value)) return Promise.reject('请输入正确的电话号码');
    return Promise.resolve();
  },
};
const emailRule = {
  validator: (rules: unknown, value: string) => {
    if (!value) return Promise.resolve();
    if (!REGS.email.test(value)) return Promise.reject('请输入正确的邮箱');
    return Promise.resolve();
  },
};

export const customerForm = () => {
  const modelRef = reactive<CustomerInfoKind>({
    buildingName: '',
    parkOrgName: '',
    warnReasonCode: '',
    warnTime: '',
    warnType: null,
    deviceNo: '',
    realReasonType: 1,
    adminId: '',
    brand: '',
    buildingId: null,
    categoryId: '',
    deviceCode: '',
    floor: 0,
    id: '',
    installationDate: '',
    installationLocation: '',
    manufacturer: '',
    manufacturerMobile: '',
    manufacturerUnit: '',
    deviceName: '',
    originalNumber: '',
    parkOrgId: '',
    pic: '',
    item: '',
    remark: '',
    scrapDate: '',
    specifications: '',
    state: true,
    systemType: null,
    ip: null,
    deviceId: null,
    communicationPort: null,
    houseId: null,
    serialNumber: null,
    port: null,
    opinion: '',
  });
  const rulesRef = reactive({
    name: { required: true, message: '请输入客户名称' },
    parkOrgId: { required: true, message: '请选择所属园区' },
    code: {
      validator: (rules: unknown, value: string) => {
        if (!REGS.creditCode.test(value)) return Promise.reject('请输入18位统一社会信用代码');
        return Promise.resolve();
      },
      required: true,
    },
    type: { required: true, message: '请选择客户类型' },
    status: { required: true, message: '请选择客户状态' },
    contacts: { required: true, message: '请输入联系人' },
    contactsTel: {
      required: true,
      validator: (rules: unknown, value: string) => {
        if (!REGS.mobile.test(value)) return Promise.reject('请输入正确的电话号码');
        return Promise.resolve();
      },
    },
    employeeId: { required: true, message: '请选择跟进人' },
    tel: [telRule],
    companyMail: [emailRule],
    contactsMail: [emailRule],
    mail: [emailRule],
  });
  return {
    modelRef,
    rulesRef,
  };
};
