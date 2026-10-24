/*
 * @Description:报表分析
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-15 14:40:24
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-15 17:12:00
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/statement-analysis';
const routes: Array<any> = [
  {
    path: BASEPATH,
    name: 'statementAnalysis',
    props: true,
    redirect: { name: 'statementAnalysisAttractInvestmentAnalysis' },
    meta: {
      title: '设备管理',
    },
  },
  {
    path: `${BASEPATH}/attract-investment-analysis`,
    name: 'statementAnalysisAttractInvestmentAnalysis',
    component: () => import(/* webpackChunkName: "statement-analysis.attract-investment-analysis" */ '~views/statement-analysis/attract-investment-analysis.vue'),
    meta: {
      title: '招商分析',
    },
  },
  {
    path: `${BASEPATH}/business-opportunity-analysis`,
    name: 'statementBusinessOpportunityAnalysis',
    component: () => import(/* webpackChunkName: "statement-analysis.attract-investment-analysis" */ '~views/statement-analysis/business-opportunity-analysis.vue'),
    meta: {
      title: '商机分析',
    },
  },
  {
    path: `${BASEPATH}/fee-statistical`,
    name: 'statementAnalysisfeeStatistical',
    component: () => import(/* webpackChunkName: "statement-analysis.fee-statistical" */ '~views/statement-analysis/fee-statistical/index.vue'),
    meta: {
      title: '收费统计',
    },
  },
];
export default routes;
