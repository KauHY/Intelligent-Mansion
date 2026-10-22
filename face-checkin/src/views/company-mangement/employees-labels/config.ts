/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-30 18:19:20
 */

// import { onUnmounted, reactive } from 'vue';
// // import useForm from '~composition/useForm';
// import { REGS } from '@/constants/validate';
// import useForm from '~utils/useForm';
import { TagInfo } from '~models/company-mangement';

export const menuTypeName = {
  0: '菜单',
  1: '按钮',
};

export const statusName = {
  0: '禁用',
  1: '启用',
};

export const statusOpt = [
  {
    label: '禁用',
    value: '0',
  }, {
    label: '启用',
    value: '1',
  },
];

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

export const columns = [{
  title: '标签名称',
  dataIndex: 'tagName',
}, {
  title: '关联企业',
  dataIndex: 'companyName',
}, {
  title: '关联园区',
  dataIndex: 'parkOrgName',
}, {
  title: '修改时间',
  dataIndex: 'updateTime',
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 220,
  slots: { customRender: 'operate' },
}];

export const operateDialogTitleName = {
  add: '新增标签',
  edit: '编辑标签',
};
