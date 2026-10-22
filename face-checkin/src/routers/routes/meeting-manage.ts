/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-24 14:22:15
 * @LastEditTime: 2022-01-24 14:32:19
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/meeting-manage';

const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'meetingManage',
    props: true,
    redirect: { name: 'meetingList' },
    meta: {
      title: '会议室管理',
    },
  },
  {
    path: `${BASEPATH}/meeting-list`,
    name: 'meetingList',
    props: true,
    component: () => import('~views/meeting-manage/meeting-list/index.vue'),
    meta: {
      title: '会议室列表',
    },
  },
  {
    path: `${BASEPATH}/meeting-map`,
    name: 'meetingMap',
    props: true,
    component: () => import('~views/meeting-manage/meeting-map/index.vue'),
    meta: {
      title: '会议室分布图',
    },
  },
  {
    path: `${BASEPATH}/meeting-book`,
    name: 'meetingBook',
    props: true,
    component: () => import('~views/meeting-manage/meeting-book/index.vue'),
    meta: {
      title: '会议室预定',
    },
  },
  {
    path: `${BASEPATH}/my-book`,
    name: 'mayBook',
    props: true,
    component: () => import('~views/meeting-manage/my-book/index.vue'),
    meta: {
      title: '我的预定',
    },
  },
  {
    path: `${BASEPATH}/face-checkin`,
    name: 'faceCheckin',
    props: true,
    component: () => import('~views/meeting-manage/face-checkin/index.vue'),
    meta: {
      title: '人脸识别签到',
    },
  },
  {
    path: `${BASEPATH}/face-checkin/:id`,
    name: 'faceCheckinMeetingDetail',
    props: true,
    component: () => import('~views/meeting-manage/face-checkin/detail.vue'),
    meta: {
      title: '会议识别详情',
      notAuthentication: true,
    },
  },
];

export default routes;
