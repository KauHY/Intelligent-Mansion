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
    title: '访客类型',
    dataIndex: 'visitorTypeName',
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增访客类型',
  edit: '编辑访客类型',
  del: '删除',
};
