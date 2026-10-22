/* eslint-disable global-require */
/* eslint-disable import/no-dynamic-require */
/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-03 21:18:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-30 15:32:17
 */
// import from '~views/layout'
import { RouteRecordRaw } from 'vue-router';

function getComponent(component:string) {
  return () => import(`~views/${component}`);
}
const routes: Array<any> = [
  {
    path: '/',
    name: 'index',
    props: true,
    component: () => import(/* webpackChunkName: "layout" */ '~views/layout/layout.vue'),
  },
  {
    path: '/login',
    name: 'login',
    props: true,
    component: getComponent('login/login.vue'),
    meta: {
      title: '登录',
    },
  },
  {
    path: '/wisdom-park-company/login-company/:id',
    name: 'loginCompany',
    props: true,
    component: () => import(/* webpackChunkName: "reset-login-company" */ '~views/login-company/login.vue'),
    meta: {
      title: '企业登录',
    },
  },
  {
    path: '/reset-password',
    name: 'resetPassword',
    props: true,
    component: () => import(/* webpackChunkName: "reset-password" */ '~views/login/reset-password.vue'),
    meta: {
      title: '重置密码',
    },
  },
  {
    path: '/wisdom-park-company/company-success',
    name: 'companySuccess',
    props: true,
    component: () => import(/* webpackChunkName: "company-success" */ '~views/login-company/company-success.vue'),
    meta: {
      title: '企业注册',
    },
  },
  {
    path: '/scheduled-overview',
    name: 'scheduledOverview',
    props: true,
    component: () => import(/* webpackChunkName: "company-success" */ '~views/meeting-manage/meeting-list/LookScreenWindow.vue'),
    meta: {
      title: '预定概览',
    },
  },
  {
    path: '/demo',
    name: 'testDemo',
    component: () => import(/* webpackChunkName: "reset-demo" */ '~views/demo/inde.vue'),
    meta: {
      title: '测试',
    },
  },
];

export default routes;
