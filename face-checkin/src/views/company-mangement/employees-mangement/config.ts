/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 18:40:28
 */

// import { onUnmounted, reactive } from 'vue';
// // import useForm from '~composition/useForm';
// import { REGS } from '@/constants/validate';
// import useForm from '~utils/useForm';
import { EmployeeInfo } from '~models/company-mangement';

import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';

export const options = useHashMapDics<['JOBCODE', 'COMPANY_TYPE']>(['JOBCODE', 'COMPANY_TYPE']);

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
  title: '姓名',
  dataIndex: 'name',
}, {
  title: '手机号',
  dataIndex: 'mobile',
}, {
  title: '照片',
  dataIndex: 'pics',
  slots: { customRender: 'pics' },
}, {
  title: '所属企业',
  dataIndex: 'companyName',
}, {
  title: '关联园区',
  dataIndex: 'parkOrgName',
}, {
  title: '员工标签',
  // dataIndex: 'status',
  customRender: ({ record }:{record:EmployeeInfo}) => record.tagNames?.join(',') || '-',
}, {
  title: '岗位',
  dataIndex: 'jobCodes',
  customRender: ({ record }:{record:EmployeeInfo}) => {
    const jobNames = record?.jobCodes?.map((code) => options.JOBCODE[code]?.label);
    return jobNames?.join(';');
  },
}, {
  title: '备注',
  dataIndex: 'remark',
}, {
  title: '创建时间',
  dataIndex: 'createTime',
}, {
  title: '状态',
  // dataIndex: 'status',
  customRender: ({ record }:{record:EmployeeInfo}) => statusName[record.status] || '-',
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 220,
  fixed: 'right',
  slots: { customRender: 'operate' },
}];

export const operateDialogTitleName = {
  add: '新增员工',
  edit: '编辑员工',
  resetPwd: '重置密码',
};
