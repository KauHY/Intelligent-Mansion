/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-30 16:23:21
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-02 11:23:52
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/command-big-sscreen';
const routes: Array<any> = [
  {
    path: BASEPATH,
    name: 'commandBigScreen',
    props: true,
    redirect: { name: 'commandBigScreenOperationScreen' },
    meta: {
      title: '指挥大屏',
    },
  },
  {
    path: `${BASEPATH}/operation-screen`,
    name: 'commandBigScreenOperationScreen',
    component: () => import(/* webpackChunkName: "command-big-sscreen.operation-screen" */ '~views/command-big-screen/operation-screen/index.vue'),
    meta: {
      title: '园区运营数据大屏',
    },
  },
];
export default routes;
