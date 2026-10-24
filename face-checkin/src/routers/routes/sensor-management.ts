/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-10 18:34:12
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-28 14:46:36
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/sensor-management';
const routes: Array<RouteRecordRaw> = [
  {
    path: `${BASEPATH}`,
    name: 'sensorManagement',
    redirect: { name: 'tb-sensor-list' },
    meta: {
      title: '传感器管理',
      notAuthentication: true,
      staticMenu: {
        icon: 'hardware',
      },
    },
  },
  {
    path: `${BASEPATH}/tb-sensor-list`,
    name: 'tb-sensor-list',
    props: true,
    component: () => import(/* webpackChunkName: "thingsboard-sensor-list.sensor" */ '@/views/thinksboard/thingsboard-sensor-list.vue'),
    meta: {
      title: '传感器列表',
      notAuthentication: true,
      staticMenu: {
        parentName: 'sensorManagement',
      },
    },
  },
  {
    path: `${BASEPATH}/tb-sensor`,
    name: 'tb-sensor',
    props: true,
    component: () => import(/* webpackChunkName: "thingsboard-sensor.sensor" */ '~views/thinksboard/thingsboard-sensor.vue'),
    meta: {
      title: '温湿传感器',
      notAuthentication: true,
      staticMenu: {
        parentName: 'sensorManagement',
      },
    },
  },
  {
    path: `${BASEPATH}/tb-sensor-hybrid`,
    name: 'tb-sensor-hybrid',
    props: true,
    component: () => import(/* webpackChunkName: "thingsboard-sensor-hybrid.sensor" */ '~views/thinksboard/thingsboard-sensor-hybrid.vue'),
    meta: {
      title: '温湿度压力传感器',
      staticMenu: {
        parentName: 'sensorManagement',
      },
    },
  },
];

export default routes;
