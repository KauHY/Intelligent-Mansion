/*
 * @Description: 巡检模块
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-29 11:28:24
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-07 11:04:03
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/patrol-inspection';

const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'patrolInspection',
    props: true,
    redirect: { name: 'patrolInspectionPatrolPlace' },
    meta: {
      title: '巡检管理',
    },
  },
  {
    path: `${BASEPATH}/patrol-place`,
    name: 'patrolInspectionPatrolPlace',
    component: () => import(/* webpackChunkName: "patrol-inspection.patrol-place" */ '~views/patrol-inspection/patrol-place/index.vue'),
    meta: {
      title: '巡检点管理',
    },
  },
  {
    path: `${BASEPATH}/patrol-line`,
    name: 'patrolInspectionPatrolLine',
    component: () => import(/* webpackChunkName: "patrol-inspection.patrol-line" */ '~views/patrol-inspection/patrol-line/index.vue'),
    meta: {
      title: '巡检线路',
    },
  },
  {
    path: `${BASEPATH}/patrol-plan`,
    name: 'patrolInspectionPatrolPlan',
    component: () => import(/* webpackChunkName: "patrol-inspection.patrol-plan" */ '~views/patrol-inspection/patrol-plan/index.vue'),
    meta: {
      title: '巡检计划',
    },
  },
  {
    path: `${BASEPATH}/patrol-task`,
    name: 'patrolInspectionPatrolTask',
    component: () => import(/* webpackChunkName: "patrol-inspection.patrol-task" */ '~views/patrol-inspection/patrol-task/index.vue'),
    meta: {
      title: '巡检任务',
    },
  },
];

export default routes;
