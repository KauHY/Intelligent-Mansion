/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: your name
 * @LastEditTime: 2022-03-18 19:22:18
 */

// import { onUnmounted, reactive } from 'vue';
// // import useForm from '~composition/useForm';
// import { REGS } from '@/constants/validate';
// import useForm from '~utils/useForm';
import { AuditPerson } from '~models/visitors-management';

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

export const columns = [
  {
    title: '序号',
    // dataIndex: 'tagName',
    customRender: ({ index }:{index:number}) => index + 1,
  },
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '企业名称',
    dataIndex: 'companyName',
  }, {
    title: '审批人',
    dataIndex: 'auditEmployeeName',
    customRender: ({ record }:{record:AuditPerson}) => (record.invitorEmployeeAudit === 1 ? '被访人' : record.auditEmployeeNames || '--')
    ,
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
  add: '新增审批人',
  edit: '编辑审批人',
  del: '删除',
};
