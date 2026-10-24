/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-07 14:51:16
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-24 14:37:33
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/person-pass';
const routes: Array<any> = [
  {
    path: BASEPATH,
    name: 'personPass',
    props: true,
    redirect: { name: 'personPassPassRecordList' },
    meta: {
      title: '人行管理',
    },
  },
  {
    path: `${BASEPATH}/pass-record`,
    name: 'personPassPassRecordList',
    component: () => import(/* webpackChunkName: "person-pass.pass-record" */ '~views/person-pass/pass-record/index.vue'),
    meta: {
      title: '通行记录',
    },
  },
  {
    path: `${BASEPATH}/pass-snap-record`,
    name: 'personPassPassSnapRecordList',
    component: () => import(/* webpackChunkName: "person-pass.pass-snap-record" */ '~views/person-pass/pass-snap-record/index.vue'),
    meta: {
      title: '抓拍记录',
    },
  },
  {
    path: `${BASEPATH}/access-policy`,
    name: 'personPassAccessPolicy',
    component: () => import(/* webpackChunkName: "person-pass.access-policy" */ '~views/person-pass/access-policy/index.vue'),
    meta: {
      title: '通行策略',
    },
  },
];
export default routes;
