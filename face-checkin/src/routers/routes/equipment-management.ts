/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-05 19:36:17
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-13 10:32:20
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/equipment-management';

const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'equipmentManagement',
    props: true,
    redirect: { name: 'equipmentManagementCard' },
    meta: {
      title: '设备管理',
    },
  },
  {
    path: `${BASEPATH}/conference-screen`,
    name: 'conferenceRoomInformationScreen',
    props: true,
    component: () => import(/* webpackChunkName: "conferenceRoomInformationScreen" */ '~views/equipment-management/conference-screen/index.vue'),
    meta: {
      title: '会议室信息屏',
    },
  },
  {
    path: `${BASEPATH}/equipment-account`,
    name: 'equipmentManagementEquipmentAccount',
    component: () => import(/* webpackChunkName: "equipment-management.equipmentManagementEquipmentAccount" */ '~views/equipment-management/equipment-account/house.vue'),
    meta: {
      title: '设备台账',
    },
  },
  {
    path: `${BASEPATH}/equipment-fire`,
    name: 'fireFightingEquipment',
    component: () => import(/* webpackChunkName: "equipment-management.fireFightingEquipment" */ '~views/equipment-management/fire-equipment/index.vue'),
    meta: {
      title: '消防设备管理',
    },
  },
  {
    path: `${BASEPATH}/card-management`,
    name: 'equipmentManagementCard',
    component: () => import(/* webpackChunkName: "equipment-management.equipmentManagementCard" */ '~views/equipment-management/card-equipment/index.vue'),
    meta: {
      title: '卡片管理',
    },
  },
  {
    path: `${BASEPATH}/equipment-list`,
    name: 'equipmentManagementEquipmentList',
    component: () => import(/* webpackChunkName: "equipment-management.equipmentManagementEquipmentList" */ '~views/equipment-management/index.vue'),
    meta: {
      title: '通行记录',
    },
  },
  {
    path: `${BASEPATH}/equipment-video`,
    name: 'equipmentManagementEquipmentVideo',
    component: () => import(/* webpackChunkName: "equipment-management.equipmentManagementEquipmentVideo" */ '~views/equipment-management/video-equipment/index.vue'),
    meta: {
      title: '视频设备管理',
    },
  },
  {
    path: `${BASEPATH}/equipment-entranceGuard`,
    name: 'equipmentManagementEquipmentEntranceGuard',
    component: () => import(/* webpackChunkName: "equipment-management.equipmentManagementEquipmentEntranceGuard" */ '~views/equipment-management/entranceGuard/index.vue'),
    meta: {
      title: '门禁设备管理',
    },
  },
  {
    path: `${BASEPATH}/equipment-vehicle`,
    name: 'equipmentManagementEquipmentVehicle',
    component: () => import(/* webpackChunkName: "equipment-management.equipmentManagementEquipmentVehicle" */ '~views/equipment-management/vehicle/index.vue'),
    meta: {
      title: '车辆通行设备管理',
    },
  },
];

export default routes;
