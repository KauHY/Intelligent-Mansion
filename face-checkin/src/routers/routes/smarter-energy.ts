/*
 * @Description:招商管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 11:00:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-22 16:52:54
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/smarter-energy';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'smarterEnergy',
    props: true,
    redirect: { name: 'wisdomCanvassBusinessHousingBusiness' },
    meta: {
      title: '智慧能源',
    },
  },
  // 电表管理
  {
    path: `${BASEPATH}/ammeter-management`,
    name: 'smarterEnergyAmmeterManagement',
    props: true,
    component: () => import(/* webpackChunkName: "smarter-energy.ammeter-management" */ '~views/smarter-energy/ammeter-management/index.vue'),
    meta: {
      title: '电表管理',
    },
  },
  // 水表管理
  {
    path: `${BASEPATH}/water-meter-management`,
    name: 'smarterEnergyWaterMeterManagement',
    props: true,
    component: () => import(/* webpackChunkName: "smarter-energy.water-meter-management" */ '~views/smarter-energy/water-meter-management/water-meter.vue'),
    meta: {
      title: '水表管理',
    },
  },
  // 抄表记录
  {
    path: `${BASEPATH}/check-meter`,
    name: 'smarterEnergyCheckMeter',
    props: true,
    component: () => import(/* webpackChunkName: "smarter-energy.check-meter" */ '~views/smarter-energy/check-meter/check-meter.vue'),
    meta: {
      title: '抄表记录',
    },
  },
  // 用量记录
  {
    path: `${BASEPATH}/operating-record`,
    name: 'smarterEnergyOperatingRecord',
    props: true,
    component: () => import(/* webpackChunkName: "smarter-energy.operating-record" */ '~views/smarter-energy/operating-record/record.vue'),
    meta: {
      title: '表计用量记录',
    },
  },
  {
    path: `${BASEPATH}/house-record`,
    name: 'ContractHouseConsumptionRecord',
    props: true,
    component: () => import(/* webpackChunkName: "smarter-energy.house-record" */ '~views/smarter-energy/house-record/index.vue'),
    meta: {
      title: '合同房屋用量记录',
    },
  },
  {
    path: `${BASEPATH}/enterprise-consumption`,
    name: 'EnterpriseConsumption',
    props: true,
    component: () => import(/* webpackChunkName: "smarter-energy.enterprise-consumption" */ '~views/smarter-energy/enterprise-consumption/index.vue'),
    meta: {
      title: '企业用量记录',
    },
  },
  // 异常警告
  {
    path: `${BASEPATH}/unusual-warn`,
    name: 'smarterEnergyUnusualWarn',
    props: true,
    component: () => import(/* webpackChunkName: "smarter-energy.unusual-warn" */ '~views/smarter-energy/unusual-warn/warn.vue'),
    meta: {
      title: '异常告警',
    },
  },
  // 能源分析
  {
    path: `${BASEPATH}/energy-analysis`,
    name: 'smarterEnergyEnergyAnalysis',
    props: true,
    component: () => import(/* webpackChunkName: "smarter-energy.energy-analysis" */ '~views/smarter-energy/energy-analysis/analysis.vue'),
    meta: {
      title: '能耗分析',
    },
  },
];

export default routes;
