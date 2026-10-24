/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 13:59:52
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-31 10:54:46
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/satisfaction-survey'; // 问卷调查
const routes: Array<any> = [
  {
    path: BASEPATH,
    name: 'satisfactionSurvey',
    props: true,
    redirect: { name: 'satisfactionSurveyQuestionnaireList' },
    meta: {
      title: '满意度调查',
    },
  },
  {
    path: `${BASEPATH}/questionnaire-list`,
    name: 'satisfactionSurveyQuestionnaireList',
    component: () => import(/* webpackChunkName: "satisfaction-survey.questionnaire-list" */ '~views/satisfaction-survey/questionnaire-list/index.vue'),
    meta: {
      title: '问卷管理',
    },
  },
  {
    path: `${BASEPATH}/design-questionnaire`,
    name: 'satisfactionSurveyDesignQuestionnaire',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "satisfaction-survey.design-questionnaire" */ '~views/satisfaction-survey/questionnaire-list/design-questionnaire.vue'),
    meta: {
      title: '问卷设计',
    },
  },
  {
    path: `${BASEPATH}/data-statistics`,
    name: 'satisfactionSurveyDataStatistics',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "satisfaction-survey.data-statistics" */ '~views/satisfaction-survey/data-statistics/index.vue'),
    meta: {
      title: '收集统计',
    },
  },
  {
    path: `${BASEPATH}/analysis-report`,
    name: 'satisfactionSurveyAnalysisReport',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "satisfaction-survey.analysis-report" */ '~views/satisfaction-survey/data-statistics/analysis-report.vue'),
    meta: {
      title: '数据分析',
    },
  },
  {
    path: `${BASEPATH}/answer-details`,
    name: 'satisfactionSurveyAnswerDetails',
    props: (route: any) => ({ ...route.query }),
    component: () => import(/* webpackChunkName: "satisfaction-survey.answer-details" */ '~views/satisfaction-survey/data-statistics/answer-details.vue'),
    meta: {
      title: '答案详情',
    },
  },
];
export default routes;
