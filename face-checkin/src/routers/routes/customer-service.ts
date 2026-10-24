/*
 * @Description:客户服务
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 11:00:11
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-08 13:47:16
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/customer-service';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'customerService',
    props: true,
    redirect: { name: 'customerServiceReportComplaint' },
    meta: {
      title: '客户服务',
    },
  },
  {
    path: `${BASEPATH}/report-complaint`,
    name: 'customerServiceReportComplaint',
    props: { orderType: 1 },
    component: () => import(/* webpackChunkName: "customer-service.report-complaint" */ '~views/customer-service/orders/index.vue'),
    meta: {
      title: '服务反馈',
    },
  },
  {
    path: `${BASEPATH}/repair-service`,
    name: 'customerServiceRepairService',
    props: { orderType: 2 },
    component: () => import(/* webpackChunkName: "customer-service.repair-service" */ '~views/customer-service/orders/index.vue'),
    meta: {
      title: '报修服务',
    },
  },
  {
    path: `${BASEPATH}/announcement`,
    name: 'customerServiceAnnouncement',
    component: () => import(/* webpackChunkName: "customer-service.repair-service" */ '~views/customer-service/announcement/index.vue'),
    meta: {
      title: '公告管理',
    },
  },
  {
    path: `${BASEPATH}/notice`,
    name: 'customerServiceNotice',
    component: () => import(/* webpackChunkName: "customer-service.repair-service" */ '~views/customer-service/notice/index.vue'),
    meta: {
      title: '资讯管理',
    },
  },
  // ---------活动管理-----
  {
    path: `${BASEPATH}/activity-list`,
    name: 'customerServiceActivityManagement',
    component: () => import(/* webpackChunkName: "customer-service.activity-management" */ '~views/customer-service/activity-management/index.vue'),
    meta: {
      title: '活动管理',
    },
  },
  {
    path: `${BASEPATH}/activity-management/sign-up-record`,
    name: 'customerServiceActivitySignUpRecord',
    component: () => import(/* webpackChunkName: "customer-service.activity-sign-up-record" */ '~views/customer-service/activity-management/sign-up-record.vue'),
    meta: {
      title: '报名记录',
    },
  },
  // -----------物品放行----------
  {
    path: `${BASEPATH}/goods-release`,
    name: 'customerServiceGoodsRelease',
    component: () => import(/* webpackChunkName: "customer-service.goods-release" */ '~views/customer-service/goods-release/index.vue'),
    meta: {
      title: '物品放行',
    },
  },
  {
    path: `${BASEPATH}/advertisement`,
    name: 'customerServiceAdvertisement',
    component: () => import(/* webpackChunkName: "customer-service.advertisement" */ '~views/customer-service/advertisement/index.vue'),
    meta: {
      title: '广告管理',
    },
  },
  // 会议室租赁
  {
    path: `${BASEPATH}/meeting-room-rental`,
    name: 'meetingRoomManage',
    props: true,
    redirect: { name: 'meetingRoomManage' },
    meta: {
      title: '会议室租赁',
    },
  },
  {
    path: `${BASEPATH}/meeting-room-manage`,
    name: 'meetingRoomManage',
    props: { type: 0 },
    component: () => import(/* webpackChunkName: "meeting-room-rental.meeting-room-manage" */ '~views/customer-service/meeting-room-rental/index.vue'),
    meta: {
      title: '会议室管理',
    },
  },
  {
    path: `${BASEPATH}/material-manage`,
    name: 'materialManage',
    props: { type: 0 },
    component: () => import(/* webpackChunkName: "meeting-room-rental.material-manage" */ '~views/customer-service/meeting-room-rental/material-manage.vue'),
    meta: {
      title: '物资管理',
    },
  },
  {
    path: `${BASEPATH}/order-manage`,
    name: 'orderManage',
    props: { type: 0 },
    component: () => import(/* webpackChunkName: "meeting-room-rental.order-manage" */ '~views/customer-service/meeting-room-rental/order-manage.vue'),
    meta: {
      title: '订单管理',
    },
  },
];

export default routes;
