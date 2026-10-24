/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-05 19:36:17
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-07 16:22:11
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/asset-mangement';

const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'assetMangement',
    props: true,
    redirect: { name: 'assetMangementGoodsMangement' },
    meta: {
      title: '资产管理',
    },
  },
  {
    path: `${BASEPATH}/goods-mangement`,
    name: 'assetMangementGoodsMangement',
    component: () => import(/* webpackChunkName: "asset-mangement.good-mangement" */ '~views/asset-mangement/goods-mangement/index.vue'),
    meta: {
      title: '物品管理',
    },
  },
  {
    path: `${BASEPATH}/receive-mangement`,
    name: 'assetMangementReceiveMangement',
    component: () => import(/* webpackChunkName: "asset-mangement.receive-mangement" */ '~views/asset-mangement/receive-mangement/index.vue'),
    meta: {
      title: '领用管理',
    },
  },
  {
    path: `${BASEPATH}/receive-audit`,
    name: 'assetMangementReceiveAudit',
    component: () => import(/* webpackChunkName: "asset-mangement.receive-audit" */ '~views/asset-mangement/receive-audit/index.vue'),
    meta: {
      title: '领用权限设置',
    },
  },
  {
    path: `${BASEPATH}/out-in-stock-record`,
    name: 'assetMangementOutInStockRecord',
    component: () => import(/* webpackChunkName: "asset-mangement.out-in-stock-record" */ '~views/asset-mangement/out-In-stock-record/index.vue'),
    meta: {
      title: '领用权限设置',
    },
  },
  {
    path: `${BASEPATH}/out-in-stock-record`,
    name: 'assetMangementOutInStockRecord',
    component: () => import(/* webpackChunkName: "asset-mangement.out-in-stock-record" */ '~views/asset-mangement/out-In-stock-record/index.vue'),
    meta: {
      title: '领用权限设置',
    },
  },
  {
    path: `${BASEPATH}/calculation-management`,
    name: 'assetMangementCalculationManagement',
    component: () => import(/* webpackChunkName: "asset-mangement.calculatio-management" */ '~views/asset-mangement/calculation-management/index.vue'),
    meta: {
      title: '盘点管理',
    },
  },
];

export default routes;
