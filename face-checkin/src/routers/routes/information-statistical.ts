/*
 * @Description:信息统计
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-28 19:10:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 19:07:36
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/information-statistical';
const routes: Array<any> = [
  {
    path: BASEPATH,
    name: 'informationStatistical',
    props: true,
    redirect: { name: 'informationStatisticalFormTemplate' },
    meta: {
      title: '信息统计',
    },
  },
  {
    path: `${BASEPATH}/form-template`,
    name: 'informationStatisticalFormTemplate',
    component: () => import(/* webpackChunkName: "information-statistical.form-template" */ '~views/information-statistical/form-template/index.vue'),
    meta: {
      title: '表单模板',
    },
  },
  {
    path: `${BASEPATH}/design-questionnaire`,
    name: 'informationDesignQuestionnaire',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "information-statistical.design-questionnaire" */ '~views/information-statistical/form-template/design-questionnaire.vue'),
    meta: {
      title: '表单模板设计',
    },
  },
  {
    path: `${BASEPATH}/form-manage`,
    name: 'informationStatisticalFormManage',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "information-statistical.form-manage" */ '~views/information-statistical/form-manage/index.vue'),
    meta: {
      title: '表单管理',
    },
  },
];
export default routes;
