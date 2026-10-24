/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:42:13
 * @LastEditTime: 2022-04-24 14:01:45
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/contract-manage';

const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'contractManage',
    props: true,
    redirect: { name: 'enterParkContract' },
    meta: {
      title: '合同管理',
    },
  },
  {
    path: `${BASEPATH}/enter-park-contract`,
    name: 'enterParkContract',
    props: { contractType: 1 },
    component: () => import('~views/contract-manage/contract/contract-list/index.vue'),
    meta: {
      title: '入园协议',
    },
  },
  {
    path: `${BASEPATH}/attract-contract`,
    name: 'attractContract',
    props: { contractType: 2 },
    component: () => import('~views/contract-manage/contract/contract-list/index.vue'),
    meta: {
      title: '招引合同',
    },
  },
  {
    path: `${BASEPATH}/lease-contract`,
    name: 'leaseContract',
    props: { contractType: 3 },
    component: () => import('~views/contract-manage/contract/contract-list/index.vue'),
    meta: {
      title: '租赁合同管理',
    },
  },
  {
    path: `${BASEPATH}/property-contract`,
    name: 'propertyContract',
    props: { contractType: 4 },
    component: () => import('~views/contract-manage/contract/contract-list/index.vue'),
    meta: {
      title: '物业合同管理',
    },
  },
  {
    path: `${BASEPATH}/apartment-contract`,
    name: 'apartmentContract',
    props: { contractType: 5 },
    component: () => import('~views/contract-manage/contract/contract-list/index.vue'),
    meta: {
      title: '公寓合同管理',
    },
  },
  {
    path: `${BASEPATH}/contract-draft`,
    name: 'contractDraft',
    props: (route: any) => ({ ...route.query }),
    component: () => import('~views/contract-manage/contract/contract-draft/index.vue'),
    meta: {
      title: '起草合同',
    },
  },
  {
    path: `${BASEPATH}/contract-draft-edit`,
    name: 'contractDraftEdit',
    props: (route: any) => ({ ...route.query }),
    component: () => import('~views/contract-manage/contract/contract-draft/index.vue'),
    meta: {
      title: '编辑合同',
    },
  },
  {
    path: `${BASEPATH}/contract-change`,
    name: 'contractChange',
    props: true,
    component: () => import('~views/contract-manage/contract/contract-change/index.vue'),
    meta: {
      title: '合同变更',
    },
  },
  {
    path: `${BASEPATH}/contract-renewal`,
    name: 'contractRenewal',
    props: true,
    component: () => import('~views/contract-manage/contract/contract-renewal/index.vue'),
    meta: {
      title: '合同续约',
    },
  },
  {
    path: `${BASEPATH}/house-status`,
    name: 'houseStatus',
    props: true,
    component: () => import('~views/contract-manage/contract/house-status/index.vue'),
    meta: {
      title: '房源状态查询',
    },
  },
  // ----------------------------------合同模板管理-------------------------------------------
  {
    path: `${BASEPATH}/contract-template`,
    name: 'contractTemplate',
    props: true,
    redirect: { name: 'contractContractTemplateList' },
    meta: {
      title: '合同模板管理',
    },
  },
  {
    path: `${BASEPATH}/contract-template/template-list`,
    name: 'contractContractTemplateList',
    props: true,
    component: () => import('~views/contract-manage/contract-template/template-list/index.vue'),
    meta: {
      title: '合同模板',
    },
  },
  {
    path: `${BASEPATH}/contract-template/template-field-lib`,
    name: 'contractContractTemplateFieldLib',
    props: true,
    component: () => import('~views/contract-manage/contract-template/template-field-lib/index.vue'),
    meta: {
      title: '模板字段库',
    },
  },
];

export default routes;
