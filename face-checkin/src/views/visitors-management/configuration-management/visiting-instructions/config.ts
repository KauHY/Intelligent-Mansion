/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-29 16:45:20
 */

// import { onUnmounted, reactive } from 'vue';
// // import useForm from '~composition/useForm';
// import { REGS } from '@/constants/validate';
// import useForm from '~utils/useForm';
// import { TagInfo } from '~models/company-mangement';

export const columns = [
  {
    title: '须知名称',
    dataIndex: 'noticeName',
    slots: { customRender: 'noticeName' },
  },
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增来访须知',
  edit: '编辑来访须知',
  del: '删除',
  details: '来访须知详情',
};
