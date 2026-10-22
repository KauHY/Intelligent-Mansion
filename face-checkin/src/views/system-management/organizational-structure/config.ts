/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-22 13:55:34
 */

import { AddOrganization } from '@/models/organization';
import { reactive, onUnmounted } from 'vue';
// import useForm from '~composition/useForm';
import useForm from '~utils/useForm';

export const statusName = {
  0: '禁用',
  1: '启用',
};

export const orgTypeNames = {
  0: '机构',
  1: '园区',
};

export const operateDialogTitleName = {
  add: '新增组织',
  edit: '编辑组织',
  details: '组织详情',
};

export const columns = [{
  title: '组织名称',
  dataIndex: 'orgName',
  slots: { customRender: 'orgName' },
}, {
  title: '组织类型',
  dataIndex: 'orgType',
  slots: { customRender: 'orgType' },
}, {
  title: '上级组织',
  dataIndex: 'parentOrgName',
}, {
  title: '创建时间',
  dataIndex: 'createTime',
}, {
  title: '操作',
  dataIndex: 'operate',
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
export const useAddOrgForm = (() => {
  const createInstance = (defaultModelRef: any) => {
    console.log(defaultModelRef, '-----defaultModelRef');
    const modelRef = reactive<AddOrganization>({
      id: '',
      description: '', // 描述
      orgName: '',
      orgType: 0,
      parentId: '',
    });

    const rulesRef = reactive({
      orgName: [{ required: true, message: '组织名称能为空' }],
      orgType: [{ required: true, message: '组织类型能为空' }],
    });

    const fill = () => {
    // debugger;
      if (!defaultModelRef || !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key]) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
      modelRef.parentId = defaultModelRef.parentId;
    };
    const { validateInfos, validate } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      validateInfos,
      rulesRef,
      validate,
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
