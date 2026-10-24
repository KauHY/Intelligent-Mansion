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
import { number } from 'mathjs';
import { createSearchParams } from '~models/company';
import { CustomerTagInfoKind } from '~models/customer-manage';

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
  const modelRef = reactive<createSearchParams>({
    contactPerson: '', // 联系人
    contactPhone: '', // 联系电话
    parkOrgId: '',
  });
  const rulesRef = reactive({
    parkOrgId: { required: true, message: '请选择所属园区' },
    contactPhone: {
      required: false,
      validator: (rules: unknown, value: string) => {
        if (!value) return Promise.resolve();
        if (!REGS.phoAndMob.test(value)) return Promise.reject('请输入正确的联系电话');
        return Promise.resolve();
      },
    },

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
