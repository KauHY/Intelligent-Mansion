/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-14 15:54:43
 */

import { AddParams } from '@/models/role';
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
// import useForm from '~composition/useForm';
import useForm from '~utils/useForm';
import { AddUserParams, UserPwdReset } from '~models/user';

export const statusName = {
  0: '已禁用',
  1: '已启用',
};
export const statusOpt = [
  {
    label: '已禁用',
    value: 0,
  }, {
    label: '已启用',
    value: 1,
  },
];

export const roleTypeOpt = {
  0: '园区平台角色',
  1: '租户超级管理员',
  2: '普通用户',
};

export const operateDialogTitleName = {
  add: '新增角色',
  edit: '编辑角色',
  authorityAllot: '权限分配',
};

export const columns = [{
  title: '角色名称',
  dataIndex: 'roleName',
}, {
  title: '角色描述',
  dataIndex: 'roleDescription',
  // slots: { customRender: 'type' },
}, {
//   title: '角色类型',
//   dataIndex: 'roleType',
//   slots: { customRender: 'roleType' },
// }, {
  title: '关联组织',
  dataIndex: 'orgName',
}, {
  title: '状态',
  dataIndex: 'status',
  slots: { customRender: 'status' },
}, {
  title: '修改时间',
  dataIndex: 'updateTime',
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 280,
  slots: { customRender: 'operate' },
}];

/**
 * @description:
 * @param {string} formName
 * @param {*} isPlatformAdminRole 是否平台管理员
 * @param {any} defaultModelRef 默认值
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddRoleForm = (() => {
  const createInstance = (isPlatformAdminRole?:boolean, defaultModelRef?: any) => {
    const modelRef = reactive<AddParams>({
      id: '',
      orgId: '', // 组织id
      roleDescription: '', // 角色描述,
      roleName: '', // 真实姓名,
      roleType: isPlatformAdminRole ? 2 : 0, // 角色类型,
      userIds: [], // 租户id
    });

    const orgIdRequired = computed(() => (isPlatformAdminRole && modelRef.roleType === 1) || !isPlatformAdminRole);

    const rulesRef = reactive({
      roleName: [{ required: true, message: '角色名不能为空' }],
      orgId: [{
        validator(rules:unknown, value:string, cb:unknown) {
        // 平台管理员创建租户超级管理员角色||不是平台管理员创建角色，必须关联租户/组织
          if (orgIdRequired.value && !value) return Promise.reject('不能为空');
          return Promise.resolve();
        },
        required: true,
      }],
    });
    const fill = () => {
      if (!defaultModelRef || !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key]) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
      if (!isPlatformAdminRole) copyModelRef.roleType = 2; // 不是平台管理员时，角色类型默认为2
      // if (defaultModelRef.userMap)copyModelRef.userIds = Object.keys(defaultModelRef.userMap);
      modelRef.userIds = (defaultModelRef?.users as SelectOptions).map(({ value }) => value);
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
  return (isPlatformAdminRole?:boolean, defaultModelRef?: any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(isPlatformAdminRole, defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
