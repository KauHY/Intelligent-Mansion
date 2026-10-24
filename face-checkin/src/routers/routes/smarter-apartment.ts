/*
 * @Description:招商管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 11:00:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-22 16:52:54
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/smarter-apartment';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'smarterApartment',
    props: true,
    redirect: { name: 'wisdomCanvassBusinessHousingBusiness' },
    meta: {
      title: '智慧公寓',
    },
  },
  {
    path: `${BASEPATH}/personnel-management`,
    name: 'ApartmentPersonnelManagement',
    props: true,
    component: () => import(/* webpackChunkName: "ApartmentPersonnelManagement" */ '~views/smart-apartment/index.vue'),
    meta: {
      title: '公寓人员管理',
    },
  },
];

export default routes;
