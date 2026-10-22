/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-27 19:20:36
 */

// import { onUnmounted, reactive } from 'vue';
// // import useForm from '~composition/useForm';
// import { REGS } from '@/constants/validate';
// import useForm from '~utils/useForm';
// import { TagInfo } from '~models/company-mangement';
import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { VisitorInfo } from '~models/visitors-management';

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

export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) => [
  {
    title: '序号',
    // dataIndex: 'tagName',
    customRender: ({ index }:{index:number}) => index + 1,
  },
  {
    title: '访客类型',
    dataIndex: 'visitorTypeName',
  }, {
    title: '访客姓名',
    dataIndex: 'visitorName',
  }, {
    title: '访客手机号码',
    dataIndex: 'visitorTel',
  }, {
    title: '来访时间',
    dataIndex: 'visitDate',
  }, {
    title: '来访事由',
    // dataIndex: 'visitEventLabel',
    customRender: ({ record }:{record:VisitorInfo}) => (record.visitReason || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  register: '登记',
  details: '访客详情',
};
