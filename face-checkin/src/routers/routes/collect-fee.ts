/*
 * @Description:收费管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-17 11:14:14
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-13 15:33:54
 */

import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/collect-fee';
const routes: Array<any> = [
  {
    path: BASEPATH,
    name: 'collectFee',
    props: true,
    redirect: { name: 'collectFeeBillingSettings' },
    meta: {
      title: '收费管理',
    },
  },
  {
    path: `${BASEPATH}/billing-settings`,
    name: 'collectFeeBillingSettings',
    props: true,
    redirect: { name: 'collectFeeChargingItems' },
    meta: {
      title: '计费设置',
    },
  },
  {
    path: `${BASEPATH}/charging-items`,
    name: 'collectFeeChargingItems',
    component: () => import(/* webpackChunkName: "collect-fee.charging-items" */ '~views/collect-fee/billing-settings/charging-items/index.vue'),
    meta: {
      title: '收费项管理',
    },
  },
  {
    path: `${BASEPATH}/billing-scheme`,
    name: 'collectFeeBillingScheme',
    component: () => import(/* webpackChunkName: "collect-fee.billing-scheme" */ '~views/collect-fee/billing-settings/billing-scheme/index.vue'),
    meta: {
      title: '计费方案管理',
    },
  },
  // ---------------------租赁费用-----------------------------------------
  {
    path: `${BASEPATH}/rent-feeMange`,
    name: 'rentFeeMange',
    redirect: { name: 'rentFeeMangeUnknownFee' },
    meta: {
      title: '租赁费用管理',
    },
  },
  {
    path: `${BASEPATH}/rent-feeMange/unknown-fee`,
    name: 'rentFeeMangeUnknownFee',
    props: true,
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.unknown-fee" */ '~views/collect-fee/rent-fee/unknown-fee/index.vue'),
    meta: {
      title: '未出费用',
    },
  },
  {
    path: `${BASEPATH}/rent-feeMange/final-fee`,
    name: 'rentFeeMangeFinalFee',
    props: true,
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.final-fee" */ '~views/collect-fee/rent-fee/final-fee/index.vue'),
    meta: {
      title: '已出费用',
    },
  },
  {
    path: `${BASEPATH}/rent-feeMange/bill`,
    name: 'rentFeeMangebill',
    props: true,
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.bill" */ '~views/collect-fee/rent-fee/bill/index.vue'),
    meta: {
      title: '账单',
    },
  },
  {
    path: `${BASEPATH}/rent-feeMange/transaction-detail`,
    name: 'rentFeeMangeTransactionDetail',
    props: true,
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.transaction-detail" */ '~views/collect-fee/rent-fee/transaction-detail/index.vue'),
    meta: {
      title: '交易明细',
    },
  },
  {
    path: `${BASEPATH}/rent-feeMange/transaction-detail/details`,
    name: 'rentFeeMangeTransactionDetailDetails',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.transaction-detail.details" */ '~views/collect-fee/rent-fee/transaction-detail/details.vue'),
    meta: {
      title: '交易明细详情',
    },
  },
  {
    path: `${BASEPATH}/rent-feeMange/late-fee`,
    name: 'rentFeeMangeLateFee',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.rent-late-fee" */ '~views/collect-fee/rent-fee/rent-late-fee/index.vue'),
    meta: {
      title: '滞纳金管理',
    },
  },
  {
    path: `${BASEPATH}/rent-feeMange/deposit-fee`,
    name: 'rentFeeMangeDepositFee',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.deposit-fee" */ '~views/collect-fee/rent-fee/deposit-fee/index.vue'),
    meta: {
      title: '押金管理',
    },
  },
  {
    path: `${BASEPATH}/rent-feeMange/deposit-fee/details`,
    name: 'rentFeeMangeDepositFeeDetails',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.deposit-fee.details" */ '~views/collect-fee/rent-fee/deposit-fee/details.vue'),
    meta: {
      title: '押金详情',
    },
  },
  {
    path: `${BASEPATH}/departure-settlement-record`,
    name: 'departureSettlementRecord',
    props: true,
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.departure-settlement-record" */ '~views/collect-fee/rent-fee/departure-settlement-record/index.vue'),
    meta: {
      title: '离场结算记录',
    },
  },
  {
    path: `${BASEPATH}/departure-settlement-record/departure-settlement`,
    name: 'departureSettlement',
    props: true,
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.departure-settlement" */ '~views/collect-fee/rent-fee/departure-settlement-record/departure.vue'),
    meta: {
      title: '离场结算',
    },
  },

  // ------------- 物业费用 ----------
  {
    path: `${BASEPATH}/property-feeMange`,
    name: 'propertyFeeMange',
    redirect: { name: 'propertyFeeMangeUnknownFee' },
    meta: {
      title: '物业费用管理',
    },
  },
  {
    path: `${BASEPATH}/property/rent-feeMange/unknown-fee`,
    name: 'propertyFeeMangeUnknownFee',
    props: { contractType: 4 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.unknown-fee" */ '~views/collect-fee/rent-fee/unknown-fee/index.vue'),
    meta: {
      title: '未出费用',
    },
  },
  {
    path: `${BASEPATH}/property/rent-feeMange/final-fee`,
    name: 'propertyFeeMangeFinalFee',
    props: { contractType: 4 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.final-fee" */ '~views/collect-fee/rent-fee/final-fee/index.vue'),
    meta: {
      title: '已出费用',
    },
  },
  {
    path: `${BASEPATH}/property/rent-feeMange/bill`,
    name: 'propertyFeeMangebill',
    props: { contractType: 4 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.bill" */ '~views/collect-fee/rent-fee/bill/index.vue'),
    meta: {
      title: '账单',
    },
  },
  {
    path: `${BASEPATH}/property/rent-feeMange/transaction-detail`,
    name: 'propertyFeeMangeTransactionDetail',
    props: { contractType: 4 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.transaction-detail" */ '~views/collect-fee/rent-fee/transaction-detail/index.vue'),
    meta: {
      title: '交易明细',
    },
  },
  {
    path: `${BASEPATH}/property/rent-feeMange/transaction-detail/details`,
    name: 'propertyFeeMangeTransactionDetailDetails',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.transaction-detail.details" */ '~views/collect-fee/rent-fee/transaction-detail/details.vue'),
    meta: {
      title: '交易明细详情',
    },
  },
  {
    path: `${BASEPATH}/property/rent-feeMange/late-fee`,
    name: 'propertyFeeMangeLateFee',
    props: { contractType: 4 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.rent-late-fee" */ '~views/collect-fee/rent-fee/rent-late-fee/index.vue'),
    meta: {
      title: '滞纳金管理',
    },
  },
  {
    path: `${BASEPATH}/property/departure-settlement-record`,
    name: 'propertyDepartureSettlementRecord',
    props: { contractType: 4 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.departure-settlement-record" */ '~views/collect-fee/rent-fee/departure-settlement-record/index.vue'),
    meta: {
      title: '离场结算记录',
    },
  },
  {
    path: `${BASEPATH}/property/departure-settlement-record/departure-settlement`,
    name: 'propertyDepartureSettlement',
    props: { contractType: 4 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.departure-settlement" */ '~views/collect-fee/rent-fee/departure-settlement-record/departure.vue'),

    meta: {
      title: '离场结算',
    },
  },

  // ------------------------------------公寓费用管理-----------------------------------------------

  {
    path: `${BASEPATH}/apartment-feeMange`,
    name: 'apartmentFeeMange',
    redirect: { name: 'apartmentFeeMangeUnknownFee' },
    meta: {
      title: '公寓费用管理',
    },
  },
  {
    path: `${BASEPATH}/apartment/rent-feeMange/unknown-fee`,
    name: 'apartmentFeeMangeUnknownFee',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.unknown-fee" */ '~views/collect-fee/rent-fee/unknown-fee/index.vue'),
    meta: {
      title: '未出费用',
    },
  },
  {
    path: `${BASEPATH}/apartment/rent-feeMange/final-fee`,
    name: 'apartmentFeeMangeFinalFee',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.final-fee" */ '~views/collect-fee/rent-fee/final-fee/index.vue'),
    meta: {
      title: '已出费用',
    },
  },
  {
    path: `${BASEPATH}/apartment/rent-feeMange/bill`,
    name: 'apartmentFeeMangebill',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.bill" */ '~views/collect-fee/rent-fee/bill/index.vue'),
    meta: {
      title: '账单',
    },
  },
  {
    path: `${BASEPATH}/apartment/rent-feeMange/transaction-detail`,
    name: 'apartmentFeeMangeTransactionDetail',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.transaction-detail" */ '~views/collect-fee/rent-fee/transaction-detail/index.vue'),
    meta: {
      title: '交易明细',
    },
  },
  {
    path: `${BASEPATH}/apartment/rent-feeMange/transaction-detail/details`,
    name: 'apartmentFeeMangeTransactionDetailDetails',
    props: (route: any) => ({ ...route.query, contractType: 5 }),
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.transaction-detail.details" */ '~views/collect-fee/rent-fee/transaction-detail/details.vue'),
    meta: {
      title: '交易明细详情',
    },
  },
  {
    path: `${BASEPATH}/apartment/rent-feeMange/late-fee`,
    name: 'apartmentFeeMangeLateFee',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.rent-late-fee" */ '~views/collect-fee/rent-fee/rent-late-fee/index.vue'),
    meta: {
      title: '滞纳金管理',
    },
  },
  {
    path: `${BASEPATH}/apartment/departure-settlement-record`,
    name: 'apartmentDepartureSettlementRecord',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.departure-settlement-record" */ '~views/collect-fee/rent-fee/departure-settlement-record/index.vue'),
    meta: {
      title: '离场结算记录',
    },
  },
  {
    path: `${BASEPATH}/apartment/departure-settlement-record/departure-settlement`,
    name: 'apartmentDepartureSettlement',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.departure-settlement" */ '~views/collect-fee/rent-fee/departure-settlement-record/departure.vue'),

    meta: {
      title: '离场结算',
    },
  },
  {
    path: `${BASEPATH}/apartment/rent-feeMange/deposit-fee`,
    name: 'apartmentFeeMangeDepositFee',
    props: (route: any) => ({ ...route.query, contractType: 5 }),
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.apartment-deposit-fee" */ '~views/collect-fee/rent-fee/deposit-fee/index.vue'),
    meta: {
      title: '押金管理',
    },
  },
  {
    path: `${BASEPATH}/apartment/rent-feeMange/deposit-fee/details`,
    name: 'apartmentFeeMangeDepositFeeDetails',
    props: (route: any) => ({ ...route.query, contractType: 5 }),
    component: () => import(/* webpackChunkName: "collect-fee.rent-fee.deposit-fee.details" */ '~views/collect-fee/rent-fee/deposit-fee/details.vue'),
    meta: {
      title: '押金详情',
    },
  },

  // ------------------------------------企业水电费管理-----------------------------------------------
  {
    path: `${BASEPATH}/water-ectricity-mange`,
    name: 'WaterAndEectricityMange',
    redirect: { name: 'waterAndEectricityUnknownFee' },
    meta: {
      title: '企业水电费管理',
    },
  },
  {
    path: `${BASEPATH}/water-ectricity-mange/unknown-fee`,
    name: 'waterAndEectricityUnknownFee',
    props: { contractType: 3 },
    component: () => import(/* webpackChunkName: "waterAndEectricityUnknownFee" */ '~views/collect-fee/water-ectricity-mange/unknown-fee/index.vue'),
    meta: {
      title: '未出水电费用',
    },
  },
  {
    path: `${BASEPATH}/water-ectricity-mange/final-fee`,
    name: 'waterAndEectricityFinalFee',
    props: { contractType: 3 },
    component: () => import(/* webpackChunkName: "waterAndEectricityFinalFee" */ '~views/collect-fee/water-ectricity-mange/final-fee/index.vue'),
    meta: {
      title: '已出水电费用',
    },
  },
  {
    path: `${BASEPATH}/water-ectricity-mange/late-fee`,
    name: 'waterAndEectricityLateFee',
    props: { contractType: 6 },
    component: () => import(/* webpackChunkName: "waterAndEectricityLateFee" */ '~views/collect-fee/rent-fee/rent-late-fee/index.vue'),
    meta: {
      title: '滞纳金管理',
    },
  },
  {
    path: `${BASEPATH}/water-ectricity-mange/bill`,
    name: 'waterAndEectricityBill',
    props: { contractType: 6 },
    component: () => import(/* webpackChunkName: "waterAndEectricityBill" */ '~views/collect-fee/rent-fee/bill/index.vue'),
    meta: {
      title: '账单',
    },
  },
  {
    path: `${BASEPATH}/water-ectricity-mange/transaction-detail`,
    name: 'waterAndEectricityTransactionDetail',
    props: { contractType: 6 },
    component: () => import(/* webpackChunkName: "waterAndEectricityTransactionDetail" */ '~views/collect-fee/rent-fee/transaction-detail/index.vue'),
    meta: {
      title: '交易明细',
    },
  },
  {
    path: `${BASEPATH}/water-ectricity-mange/transaction-detail/details`,
    name: 'waterAndEectricityTransactionDetailDetails',
    props: (route: any) => ({ ...route.query, contractType: 6 }),
    component: () => import(/* webpackChunkName: "waterAndEectricityTransactionDetailDetails" */ '~views/collect-fee/rent-fee/transaction-detail/details.vue'),
    meta: {
      title: '交易明细详情',
    },
  },
  {
    path: `${BASEPATH}/water-ectricity-mange/departure-settlement-record`,
    name: 'WaterAndEectricitySettlementRecord',
    props: { contractType: 3 },
    component: () => import(/* webpackChunkName: "WaterAndEectricitySettlementRecord" */ '~views/collect-fee/water-ectricity-mange/departure-settlement-record/index.vue'),
    meta: {
      title: '离场结算记录',
    },
  },
  {
    path: `${BASEPATH}/water-ectricity-mange/departure-settlement-record/departure-settlement`,
    name: 'WaterAndEectricitySettlement',
    props: { contractType: 3 },
    component: () => import(/* webpackChunkName: "WaterAndEectricitySettlement" */ '~views/collect-fee/water-ectricity-mange/departure-settlement-record/departure.vue'),

    meta: {
      title: '离场结算',
    },
  },
  // ------------------------------------公寓水电费管理-----------------------------------------------
  {
    path: `${BASEPATH}/apartment-water-ectricity-mange`,
    name: 'waterAndEectricityApartmentMange',
    redirect: { name: 'waterAndEectricityApartmentUnknownFee' },
    meta: {
      title: '公寓水电费管理',
    },
  },
  {
    path: `${BASEPATH}/apartment-ectricity-mange/unknown-fee`,
    name: 'waterAndEectricityApartmentUnknownFee',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "waterAndEectricityApartmentUnknownFee" */ '~views/collect-fee/water-ectricity-mange/unknown-fee/index.vue'),
    meta: {
      title: '未出水电费用',
    },
  },
  {
    path: `${BASEPATH}/apartment-ectricity-mange/final-fee`,
    name: 'waterAndEectricityApartmentFinalFee',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "waterAndEectricityApartmentFinalFee" */ '~views/collect-fee/water-ectricity-mange/final-fee/index.vue'),
    meta: {
      title: '已出水电费用',
    },
  },
  {
    path: `${BASEPATH}/apartment-ectricity-mange/late-fee`,
    name: 'waterAndEectricityApartmentLateFee',
    props: { contractType: 7 },
    component: () => import(/* webpackChunkName: "waterAndEectricityApartmentLateFee" */ '~views/collect-fee/rent-fee/rent-late-fee/index.vue'),
    meta: {
      title: '滞纳金管理',
    },
  },
  {
    path: `${BASEPATH}/apartment-ectricity-mange/bill`,
    name: 'waterAndEectricityApartmentBill',
    props: { contractType: 7 },
    component: () => import(/* webpackChunkName: "waterAndEectricityApartmentBill" */ '~views/collect-fee/rent-fee/bill/index.vue'),
    meta: {
      title: '账单',
    },
  },
  {
    path: `${BASEPATH}/apartment-ectricity-mange/transaction-detail`,
    name: 'waterAndEectricityApartmentTransactionDetail',
    props: { contractType: 7 },
    component: () => import(/* webpackChunkName: "waterAndEectricityApartmentTransactionDetail" */ '~views/collect-fee/rent-fee/transaction-detail/index.vue'),
    meta: {
      title: '交易明细',
    },
  },
  {
    path: `${BASEPATH}/apartment-ectricity-mange/transaction-detail/details`,
    name: 'waterAndEectricityApartmentTransactionDetailDetails',
    props: (route: any) => ({ ...route.query, contractType: 7 }),
    component: () => import(/* webpackChunkName: "waterAndEectricityApartmentTransactionDetailDetails" */ '~views/collect-fee/rent-fee/transaction-detail/details.vue'),
    meta: {
      title: '交易明细详情',
    },
  },
  {
    path: `${BASEPATH}/apartment-ectricity-mange/departure-settlement-record`,
    name: 'WaterAndEectricityApartmentSettlementRecord',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "WaterAndEectricityApartmentSettlementRecord" */ '~views/collect-fee/water-ectricity-mange/departure-settlement-record/index.vue'),
    meta: {
      title: '离场结算记录',
    },
  },
  {
    path: `${BASEPATH}/apartment-ectricity-mange/departure-settlement-record/departure-settlement`,
    name: 'WaterAndEectricityApartmentSettlement',
    props: { contractType: 5 },
    component: () => import(/* webpackChunkName: "WaterAndEectricityApartmentSettlement" */ '~views/collect-fee/water-ectricity-mange/departure-settlement-record/departure.vue'),

    meta: {
      title: '离场结算',
    },
  },
];
export default routes;
