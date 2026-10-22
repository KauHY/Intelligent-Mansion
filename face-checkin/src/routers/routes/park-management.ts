/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-10 18:34:12
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-22 11:25:02
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/park-management';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'parkManagement',
    props: true,
    redirect: { name: 'parkManagementParkList' },
    meta: {
      title: '园区管理',
    },
  },
  {
    path: `${BASEPATH}/park-list`,
    name: 'parkManagementParkList',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.park" */ '~views/park-management/park/park.vue'),
    meta: {
      title: '园区列表',
    },
  },
  {
    path: `${BASEPATH}/park-details`,
    name: 'parkManagementParkDetails',
    // props: false,
    props: (route: any) => ({ params: route.query }),
    component: () => import(/* webpackChunkName: "park-management.details" */ '~views/park-management/park/details.vue'),
    meta: {
      title: '园区信息',
    },
  },
  {
    path: `${BASEPATH}/house`,
    name: 'parkManagementHouse',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.house" */ '~views/park-management/house/house.vue'),
    meta: {
      title: '房屋管理',
    },
  },
];

export default routes;
