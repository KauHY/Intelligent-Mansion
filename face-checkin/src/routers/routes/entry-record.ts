/*
 * @Description:招商管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 11:00:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-22 16:52:54
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/wisdom-park-company/entry-record';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'entryRecord',
    props: true,
    redirect: { name: 'wisdomCanvassBusinessHousingBusiness' },
    meta: {
      title: '园区入驻',
    },
  },
  {
    path: `${BASEPATH}/entry-supply`,
    name: 'entrySupply',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.view-house-apply" */ '~views/entry-record/entry-supply/index.vue'),
    meta: {
      title: '入驻申请',
    },
  },
  {
    path: `${BASEPATH}/entry-log`,
    name: 'entryLog',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.view-house-apply" */ '~views/entry-record/entry-log/index.vue'),
    meta: {
      title: '入驻记录',
    },
  },
  {
    path: '/wisdom-park-company/supply-service',
    name: 'supplyService',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.view-house-apply" */ '~views/supply-service/index.vue'),
    meta: {
      title: '供应服务',
    },
  },
  {
    path: '/wisdom-park-company/procurement-services',
    name: 'procurementServices',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.view-house-apply" */ '~views/procurement-services/index.vue'),
    meta: {
      title: '采购服务',
    },
  },
  {
    path: '/wisdom-park-company/procurement-services/procurement-detail',
    name: 'procurementDetail',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.view-house-apply" */ '~views/procurement-services/detail.vue'),
    meta: {
      title: '投标信息详情',

    },
  },
  {
    path: '/wisdom-park-company/expenseil-bill-mange',
    name: 'expenseilBllMange',
    props: true,
    redirect: { name: 'contractExpenseilBillMange' },
    meta: {
      title: '费用账单',
    },
  },
  {
    path: '/wisdom-park-company/expenseil-bill-mange/contract-expenseil-bill-list',
    name: 'contractExpenseilBillList',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.view-house-apply" */ '~views/expense-bill/index.vue'),
    meta: {
      title: '合同费用账单',
    },
  },
  {
    path: '/wisdom-park-company/expenseil-bill-mange/utilities-expenseil-bill-list',
    name: 'utilitiesExpenseilBllList',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.view-house-apply" */ '~views/expense-bill/water-expense-bill/index.vue'),
    meta: {
      title: '水电费用账单',
    },
  },
];

export default routes;
