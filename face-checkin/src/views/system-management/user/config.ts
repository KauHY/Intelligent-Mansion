/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 14:31:52
 */

import { onUnmounted, reactive } from 'vue';
// import useForm from '~composition/useForm';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { AddUserParams, UserPwdReset } from '~models/user';

export const menuTypeName = {
  0: '菜单',
  1: '按钮',
};

export const statusName = {
  0: '已禁用',
  1: '已启用',
};

export const statusOpt = [
  {
    label: '已禁用',
    value: '0',
  }, {
    label: '已启用',
    value: '1',
  },
];

export const columns = [{
  title: '用户名',
  dataIndex: 'username',
  fixed: 'left',
}, {
  title: '姓名',
  dataIndex: 'realName',
  // customRender: ({ text }:{text:string[]}) => text.join(';')
  // slots: { customRender: 'type' },
}, {
  title: '手机号',
  dataIndex: 'mobile',
}, {
  title: '所属组织',
  dataIndex: 'orgName',
}, {
  title: '角色',
  customRender: ({ text }:{text:any}) => (text ? text.join(';') : ''),
  dataIndex: 'roleNames',
}, {
  title: '状态',
  dataIndex: 'status',
  slots: { customRender: 'status' },
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 300,
  fixed: 'right',
  slots: { customRender: 'operate' },
}];

export const operateDialogTitleName = {
  add: '新增用户',
  edit: '编辑用户',
  resetPwd: '重置密码',
};

/**
 * @description: 新增用户
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddUserForm = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<AddUserParams>({
      mobile: '',
      id: '',
      orgId: '',
      // password: '',
      realName: '',
      roleIds: [],
      roleNames: [],
      username: '',
      userId: '',
    });
    const rulesRef = reactive({
      username: [{ required: true, message: '用户名不能为空' }],
      mobile: [{
        validator: (rules:unknown, value:string) => {
          if (!REGS.mobile.test(value)) return Promise.reject('请输入正确手机号码');
          return Promise.resolve();
        },
        required: true,
      }],
      realName: [{ required: true, message: '姓名不能为空' }],
      // password: [{ required: true, message: '密码不能为空' }],
      roleIds: [{ required: true, message: '角色不能为空' }],
      orgId: [{ required: true, message: '所属组织不能为空' }],
    });

    const fill = () => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key]) {
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

/**
 * @description: 重置密码
 * @param {any} defaultModelRef
 * @return {*}
 * @author: GUOCHAO82
 */
export const useResetPwdForm = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<UserPwdReset>({
      newPassword: '',
      newPasswordAgain: '',
      username: '',
      userId: '',
    });
    let formValidateField:any = null;
    const rulesRef = reactive({
      // newPassword: [{ required: true, message: '新密码不能为空' }],
      // newPasswordAgain: [{
      //   validator(rule:unknown, value:string, cb:unknown) {
      //     if (modelRef.newPassword !== value) return Promise.reject('两次输入密码不一致');
      //     return Promise.resolve();
      //   },
      //   required: true,
      // }],
      newPassword: [{
        validator: (rules:unknown, value:string) => {
          const { newPassword, newPasswordAgain } = modelRef;
          if (!REGS.passWord.test(value)) {
            return Promise.reject('密码格式为不少于8位的数字、字母、符号两种或以上组合');
          }
          if (newPassword !== newPasswordAgain) {
            return Promise.reject('两次输入的密码不一致');
          }
          return Promise.resolve();
        },
      }],
      newPasswordAgain: [{
        validator: (rules:unknown, value:string) => {
          const { newPassword, newPasswordAgain } = modelRef;
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

    const fill = () => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key]) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
    };
    const { validateInfos, validate, validateField } = useForm(modelRef, rulesRef);
    formValidateField = validateField;
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
