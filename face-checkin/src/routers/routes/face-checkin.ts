import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/meeting-checkin';

const routes: Array<any> = [
  {
    path: BASEPATH,
    name: 'meetingCheckin',
    redirect: { name: 'meetingCheckinRecords' },
    meta: {
      title: '会议列表',
      notAuthentication: true,
      staticMenu: {
        icon: 'calendar-fill',
      },
    },
  },
  {
    path: `${BASEPATH}/records`,
    name: 'meetingCheckinRecords',
    props: true,
    component: () => import('~views/meeting-checkin/records.vue'),
    meta: {
      title: '签到记录',
      notAuthentication: true,
      staticMenu: {
        parentName: 'meetingCheckin',
      },
    },
  },
  {
    path: `${BASEPATH}/uploads`,
    name: 'meetingCheckinUploads',
    props: true,
    component: () => import('~views/meeting-checkin/uploads.vue'),
    meta: {
      title: '上传记录',
      notAuthentication: true,
      staticMenu: {
        parentName: 'meetingCheckin',
      },
    },
  },
  {
    path: `${BASEPATH}/persons`,
    name: 'meetingCheckinPersons',
    props: true,
    component: () => import('~views/meeting-checkin/persons.vue'),
    meta: {
      title: '人员管理',
      notAuthentication: true,
      staticMenu: {
        parentName: 'meetingCheckin',
      },
    },
  },
  {
    path: `${BASEPATH}/meetings`,
    name: 'meetingCheckinMeetings',
    props: true,
    component: () => import('~views/meeting-checkin/meetings.vue'),
    meta: {
      title: '会议管理',
      notAuthentication: true,
      staticMenu: {
        parentName: 'meetingCheckin',
      },
    },
  },
];

export default routes;
