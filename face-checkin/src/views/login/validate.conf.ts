/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 20:00:37
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 19:13:55
 */
import { Form } from 'ant-design-vue';
import { reactive, Ref } from 'vue';
import { REGS } from '@/constants/validate';

const { useForm } = Form;
// console.log(useForm);
export const useResetPassword = (currentStep:Ref<number>) => {
  const modelRef = reactive({
    username: '', // 用户名
    mobilePhone: '',
    newPassword: '',
    newPasswordAgain: '', // 确认密码
    telValidateCode: '', // 短信验证码
    validateCode: '',
    validateCodeKey: '',
  });
  let formValidateField:any = null;
  const rulesRef = reactive({
    username: [{ required: currentStep.value === 0, message: '用户名不能为空' }],
    mobilePhone: [{
      validator: (rules:unknown, value:string) => {
        if (currentStep.value !== 0) return Promise.resolve();
        if (!REGS.mobile.test(value)) return Promise.reject('请输入正确手机号');
        return Promise.resolve();
      },
      required: true,
    }],
    validateCode: [{ required: currentStep.value === 0, message: '图形验证码不能为空' }],
    telValidateCode: [{ required: currentStep.value === 0, message: '请输入短信验证码' }],
    newPassword: [{
      validator: (rules:unknown, value:string) => {
        const { newPassword, newPasswordAgain } = modelRef;
        if (currentStep.value !== 1) return Promise.resolve();
        if (!REGS.passWord.test(value)) {
          return Promise.reject('密码格式为不少于8位的数字、字母、符号两种或以上组合');
        }
        return Promise.resolve();
      },
    }],
    newPasswordAgain: [{
      validator: (rules:unknown, value:string) => {
        const { newPassword, newPasswordAgain } = modelRef;
        if (currentStep.value !== 1) return Promise.resolve();
        if (!REGS.passWord.test(value)) {
          return Promise.reject('密码格式为不少于8位的数字、字母、符号两种或以上组合');
        }
        if (newPassword !== newPasswordAgain) {
          return Promise.reject('两次输入的密码不一致');
        }
        formValidateField('newPassword', modelRef.newPassword, rulesRef.newPassword, ['change']);
        return Promise.resolve();
      },
    }],
  });
  const {
    resetFields, validate, validateInfos, validateField,
  } = useForm(modelRef, rulesRef);
  formValidateField = validateField;
  // rulesRef.newPasswordAgain = {};
  return {
    modelRef,
    resetFields,
    validate,
    validateInfos,
  };
};

export const companyForm = (currentStep:Ref<number>) => {
  const modelRef = reactive({
    username: '', // 用户名
    mobilePhone: '',
    newPassword: '',
    telValidateCode: '', // 短信验证码
    validateCode: '',
    validateCodeKey: '',
  });
  let formValidateField:any = null;
  const rulesRef = reactive({
    username: [{ required: currentStep.value === 0, message: '用户名不能为空' }],
    mobilePhone: [{
      validator: (rules:unknown, value:string) => {
        if (currentStep.value !== 0) return Promise.resolve();
        if (!REGS.mobile.test(value)) return Promise.reject('请输入正确手机号');
        return Promise.resolve();
      },
      required: true,
    }],
    validateCode: [{ required: currentStep.value === 0, message: '图形验证码不能为空' }],
    telValidateCode: [{ required: currentStep.value === 0, message: '请输入短信验证码' }],
    newPassword: [{
      required: currentStep.value === 0,
      message: '密码不能为空',
      pattern: /^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$).{8,20}$/,

    }],
  });
  const {
    resetFields, validate, validateInfos, validateField,
  } = useForm(modelRef, rulesRef);
  formValidateField = validateField;
  // rulesRef.newPasswordAgain = {};
  return {
    modelRef,
    resetFields,
    validate,
    validateInfos,
  };
};
