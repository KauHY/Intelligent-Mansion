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
  // {
  //   path: `${BASEPATH}`,
  //   name: 'assetMangement',
  //   props: true,
  //   redirect: { name: 'assetMangementGoodsMangement' },
  //   meta: {
  //     title: '资产管理',
  //   },
  // },
  {
    path: `${BASEPATH}/inventory-management`,
    name: 'assetMangementInventoryManagement',
    component: () => import(/* webpackChunkName: "asset-mangement.inventory-management" */ '~views/asset-mangement/inventory-management/index.vue'),
    meta: {
      title: '物品管理',
    },
  },
];

export default routes;
