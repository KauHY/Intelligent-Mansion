/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-03 21:18:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-16 14:08:23
 */
// import from '~views/layout'
import { RouteRecordRaw } from 'vue-router';

export const notFound:RouteRecordRaw = {
  path: '/:pathMatch(.*)',
  name: 'notFound',
  props: true,
  component: () => import(/* webpackChunkName: "layout" */ '~views/others/not-found.vue'),
};
//   {
//     path: '*',
//     redirect: { name: 'notFound' },
//   },
