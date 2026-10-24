/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-10 18:34:12
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-28 14:46:36
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/company-mangement';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'companyMangement',
    props: true,
    redirect: { name: 'companyMangementPompanyList' },
    meta: {
      title: '入住企业管理',
    },
  },
  {
    path: `${BASEPATH}/company-list`,
    name: 'companyMangementPompanyList',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.park" */ '~views/company-mangement/company-list/index.vue'),
    meta: {
      title: '园区企业',
    },
  },
  {
    path: `${BASEPATH}/company-check`,
    name: 'companyCheck',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.park" */ '~views/company-mangement/company-check/index.vue'),
    meta: {
      title: '企业入驻审核',
    },
  },
  {
    path: `${BASEPATH}/company-info-check`,
    name: 'companyInfoCheck',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.park" */ '~views/company-mangement/company-check/company-info-check.vue'),
    meta: {
      title: '企业信息审核',
    },
  },
  {
    path: `${BASEPATH}/bind-house`,
    name: 'companyMangementBindHouse',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.park" */ '~views/company-mangement/company-list/bind-house.vue'),
    meta: {
      title: '绑定房屋',
    },
  },
  {
    path: `${BASEPATH}/unbind-house`,
    name: 'companyMangementUnbindHouse',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.park" */ '~views/company-mangement/company-list/unbind-house.vue'),
    meta: {
      title: '解绑房屋',
    },
  },
  {
    path: `${BASEPATH}/occupancy-record`,
    name: 'companyMangementOccupancyRecord',
    // props: false,
    props: (route: any) => ({ params: route.query }),
    component: () => import(/* webpackChunkName: "park-management.details" */ '~views/company-mangement/occupancy-record/index.vue'),
    meta: {
      title: '入住记录',
    },
  },
  {
    path: `${BASEPATH}/occupancy-record/renewal-record`,
    name: 'companyMangementRenewalRecord',
    // props: false,
    props: true,
    component: () => import(/* webpackChunkName: "park-management.details" */ '~views/company-mangement/occupancy-record/renewal-record.vue'),
    meta: {
      title: '续租记录',
    },
  },
  {
    path: `${BASEPATH}/employees-mangement`,
    name: 'companyMangementEmployeesMangement',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.house" */ '~views/company-mangement/employees-mangement/index.vue'),
    meta: {
      title: '员工管理',
    },
  },
  {
    path: `${BASEPATH}/employees-labels`,
    name: 'companyMangementEmployeesLabels',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.house" */ '~views/company-mangement/employees-labels/index.vue'),
    meta: {
      title: '员工标签管理',
    },
  },
  {
    path: `${BASEPATH}/jobs-mangement`,
    name: 'companyMangementJobsMangement',
    props: true,
    component: () => import(/* webpackChunkName: "park-management.house" */ '~views/company-mangement/jobs-mangement/index.vue'),
    meta: {
      title: '员工岗位管理',
    },
  },
];

export default routes;
