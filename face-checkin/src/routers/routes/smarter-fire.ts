/*
 * @Description:招商管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 11:00:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-22 16:52:54
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/smarter-fire';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'smarterFire',
    props: true,
    redirect: { name: 'wisdomCanvassBusinessHousingBusiness' },
    meta: {
      title: '智慧消防',
    },
  },
  {
    path: `${BASEPATH}/alarm-log`,
    name: 'smarterAlarmLogManagement',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.view-house-apply" */ '~views/smarter-fire/index.vue'),
    meta: {
      title: '报警记录',
    },
  },
];

export default routes;
