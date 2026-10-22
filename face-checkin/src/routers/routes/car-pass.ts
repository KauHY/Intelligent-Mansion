/*
 * @Description: 车行管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 10:21:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-05 15:43:04
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/car-pass';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'carPassManagement',
    props: true,
    redirect: { name: 'carPassManagementLane' },
    meta: {
      title: '车行管理',
    },
  },
  {
    path: `${BASEPATH}/car`,
    name: 'carPassManagementCar',
    props: true,
    component: () => import(/* webpackChunkName: "car-pass.car" */ '~views/car-pass/car/index.vue'),
    meta: {
      title: '车辆管理',
    },
  },
  {
    path: `${BASEPATH}/lane`,
    name: 'carPassManagementLane',
    props: true,
    component: () => import(/* webpackChunkName: "car-pass.lane" */ '~views/car-pass/lane/index.vue'),
    meta: {
      title: '车道管理',
    },
  },
  {
    path: `${BASEPATH}/parking-lot`,
    name: 'carPassManagementParkingLot',
    props: true,
    component: () => import(/* webpackChunkName: "car-pass.parking-lot" */ '~views/car-pass/parking-lot/index.vue'),
    meta: {
      title: '停车场管理',
    },
  },
  {
    path: `${BASEPATH}/car-pass-record`,
    name: 'carPassManagementCarPassRecord',
    props: true,
    component: () => import(/* webpackChunkName: "car-pass.car-pass-record" */ '~views/car-pass/car-pass-record/index.vue'),
    meta: {
      title: '通行记录',
    },
  },
  {
    path: `${BASEPATH}/car-pass-snap-record`,
    name: 'carPassManagementCarPassSnapRecord',
    props: true,
    component: () => import(/* webpackChunkName: "car-pass.car-pass-snap-record" */ '~views/car-pass/car-pass-snap-record/index.vue'),
    meta: {
      title: '抓拍记录',
    },
  },
];

export default routes;
