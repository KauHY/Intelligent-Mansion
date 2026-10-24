/*
 * @Description: 访客管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 10:21:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-30 11:17:58
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/visitors-management';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'visitorsManagement',
    props: true,
    redirect: { name: 'visitorsManagementAppointmentList' },
    meta: {
      title: '访客管理',
    },
  },
  {
    path: `${BASEPATH}/visitors`,
    name: 'visitorsManagementVisitors',
    props: true,
    redirect: { name: 'visitorsManagementAppointmentList' },
    meta: {
      title: '访客信息管理',
    },
  },
  {
    path: `${BASEPATH}/appointment-list`,
    name: 'visitorsManagementAppointmentList',
    props: true,
    component: () => import(/* webpackChunkName: "visitors-management.appointment-list" */ '~views/visitors-management/visitors/appointment-list/index.vue'),
    meta: {
      title: '已预约访客',
    },
  },
  {
    path: `${BASEPATH}/current-visitors`,
    name: 'visitorsManagementCurrentVisitors',
    props: true,
    component: () => import(/* webpackChunkName: "visitors-management.current-visitors" */ '~views/visitors-management/visitors/current-visitors/index.vue'),
    meta: {
      title: '当前访客',
    },
  },
  {
    path: `${BASEPATH}/history-visitors`,
    name: 'visitorsManagementHistoryVisitors',
    props: true,
    component: () => import(/* webpackChunkName: "visitors-management.history-visitors" */ '~views/visitors-management/visitors/history-visitors/index.vue'),
    meta: {
      title: '历史访客',
    },
  },
  // 配置管理==========================================
  {
    path: `${BASEPATH}/configuration-management`,
    name: 'visitorsConfigurationManagement',
    props: true,
    redirect: { name: 'visitorsConfigurationManagementAuditPerson' },
    meta: {
      title: '配置管理',
    },
  },
  {
    path: `${BASEPATH}/audit-person`,
    name: 'visitorsConfigurationManagementAuditPerson',
    props: true,
    component: () => import(/* webpackChunkName: "visitors-configuration-management.audit-person" */ '~views/visitors-management/configuration-management/audit-person/index.vue'),
    meta: {
      title: '审批人配置',
    },
  },
  {
    path: `${BASEPATH}/visitor-type`,
    name: 'visitorsConfigurationManagementVisitorType',
    props: true,
    component: () => import(/* webpackChunkName: "visitors-configuration-management.visitor-type" */ '~views/visitors-management/configuration-management/visitor-type/index.vue'),
    meta: {
      title: '访客类型',
    },
  },
  {
    path: `${BASEPATH}/visiting-instructions`,
    name: 'visitorsConfigurationManagementVisitingInstructions',
    props: true,
    component: () => import(/* webpackChunkName: "visitors-configuration-management.visiting-instructions" */ '~views/visitors-management/configuration-management/visiting-instructions/index.vue'),
    meta: {
      title: '来访须知',
    },
  },
  {
    path: `${BASEPATH}/visitor-process`,
    name: 'visitorsConfigurationManagementVisitorProcess',
    props: true,
    component: () => import(/* webpackChunkName: "visitors-configuration-management.visitor-process" */ '~views/visitors-management/configuration-management/visitor-process/index.vue'),
    meta: {
      title: '访客流程',
    },
  },

  {
    path: `${BASEPATH}/company-info-bind`,
    name: 'visitorsConfigurationManagementCompanyInfoBind',
    props: true,
    component: () => import(/* webpackChunkName: "visitors-configuration-management.company-info-bind" */ '~views/visitors-management/configuration-management/company-info-bind/index.vue'),
    meta: {
      title: '公司信息',
    },
  },
];

export default routes;
