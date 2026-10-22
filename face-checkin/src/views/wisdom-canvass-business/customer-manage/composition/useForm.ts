/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:43:20
 * @LastEditTime: 2022-03-02 16:29:00
 * @LastEditors: Xucaizhi3
 * @Description:
 */
/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:36:36
 * @LastEditTime: 2022-01-13 10:38:09
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { REGS } from '@/constants/validate';
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { CustomerInfoKind, CustomerTagInfoKind } from '~models/customer-manage';

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
    registeredCapital: { required: false, pattern: /^\+?[1-9][0-9]*$/, message: '请输入大于0的数字，最多五位' },
    businessTerm: { required: false, pattern: /^\+?[1-9][0-9]*$/, message: '请输入大于0的数字，最多五位' },
    companyScale: { required: false, pattern: /^\+?[1-9][0-9]*$/, message: '请输入大于0的数字，最多五位' },
    name: { required: true, message: '请输入客户名称' },
    parkOrgId: { required: true, message: '请选择所属园区' },
    code: {
      validator: (rules: unknown, value: string) => {
        if (!REGS.creditCode.test(value)) return Promise.reject('请输入18位统一社会信用代码');
        return Promise.resolve();
      },
      required: true,
    },
    contactsPosition: { required: false, pattern: /[\u4E00-\u9FA5\uF900-\uFA2D]/, message: '请输入文字' },
    contactsDepartment: { required: false, pattern: /[\u4E00-\u9FA5\uF900-\uFA2D]/, message: '请输入文字' },
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

export const customerTagForm = () => {
  const modelRef = reactive<CustomerTagInfoKind>({
    name: '',
    parkOrgId: '',
    id: '',
  });
  const rulesRef = reactive({
    name: { required: true, message: '请输入标签名称' },
    parkOrgId: { required: true, message: '请选择园区' },
  });
  return {
    modelRef,
    rulesRef,
  };
};
