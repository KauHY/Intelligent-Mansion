/*
 * @Description:招商管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 11:00:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-22 16:52:54
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/wisdom-park-company';
const routes: Array<any> = [
  {
    path: `${BASEPATH}/enterprise-information`,
    name: 'enterpriseInformation',
    props: true,
    component: () => import(/* webpackChunkName: "enterpriseInformation" */ '~views/enterprise-information/index.vue'),
    meta: {
      title: '企业信息',
    },
  },
];

export default routes;
