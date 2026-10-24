/*
 * @Description:招商管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 11:00:11
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-02-25 14:11:09
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/wisdom-canvass-business';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'wisdomCanvassBusiness',
    props: true,
    redirect: { name: 'wisdomCanvassBusinessHousingBusiness' },
    meta: {
      title: '招商管理',
    },
  },
  {
    path: `${BASEPATH}/housing-business`,
    props: true,
    name: 'wisdomCanvassBusinessHousingBusiness',
    component: () => import(/* webpackChunkName: "wisdom-canvass-business.housing-business" */ '~views/wisdom-canvass-business/housing-business/index.vue'),
    meta: {
      title: '房源招商信息',
    },
  },
  {
    path: `${BASEPATH}/business-info-Add`,
    name: 'wisdomCanvassBusinessBusinessInfoAdd',
    props: true,
    component: () => import(/* webpackChunkName: "wisdom-canvass-business.housing-business" */ '~views/wisdom-canvass-business/housing-business/business-info-add.vue'),
    meta: {
      title: '新增招商信息',
    },
  },
  {
    path: `${BASEPATH}/house-list`,
    name: 'wisdomCanvassBusinessHouseList',
    props: true,
    // props: (route) => ({ params: route.query }),
    component: () => import(/* webpackChunkName: "wisdom-canvass-business.housing-list" */ '~views/wisdom-canvass-business/housing-list/index.vue'),
    meta: {
      title: '招商房屋',
    },
  },
  {
    path: `${BASEPATH}/housing-info-edit`,
    name: 'wisdomCanvassBusinessHousingInfoEdit',
    props: true,
    // props: (route) => ({ params: route.query }),
    component: () => import(/* webpackChunkName: "wisdom-canvass-business.house-info-edit" */ '~views/wisdom-canvass-business/housing-list/housing-info-edit.vue'),
    meta: {
      title: '编辑房屋信息',
    },
  },
  // 商机管理
  {
    path: `${BASEPATH}/business-opportunity-management`,
    name: 'wisdomCanvassBusinessBusinessOpportunityManagement',
    props: true,
    redirect: { name: 'wisdomCanvassBusinessAllBusinessOpportunities' },
    meta: {
      title: '商机管理',
    },
  },
  {
    path: `${BASEPATH}/all-business-opportunities`,
    name: 'wisdomCanvassBusinessAllBusinessOpportunities',
    props: { type: 0 },
    component: () => import(/* webpackChunkName: "business-opportunity-management.business-opportunity-management" */ '~views/wisdom-canvass-business/business-opportunity-management/index.vue'),
    meta: {
      title: '全部商机',
    },
  },
  {
    path: `${BASEPATH}/public-business-opportunities`,
    name: 'wisdomCanvassBusinessPublicBusinessOpportunities',
    props: { type: 1 },
    component: () => import(/* webpackChunkName: "business-opportunity-management.business-opportunity-management" */ '~views/wisdom-canvass-business/business-opportunity-management/index.vue'),
    meta: {
      title: '公共商机',
    },
  },
  {
    path: `${BASEPATH}/my-business-opportunities`,
    name: 'wisdomCanvassBusinessMyBusinessOpportunities',
    props: { type: 2 },
    component: () => import(/* webpackChunkName: "business-opportunity-management.business-opportunity-management" */ '~views/wisdom-canvass-business/business-opportunity-management/index.vue'),
    meta: {
      title: '我的商机',
    },
  },
  {
    path: `${BASEPATH}/business-opportunities-details`,
    name: 'wisdomCanvassBusinessOpportunitiesDetails',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.business-opportunities-details" */ '~views/wisdom-canvass-business/business-opportunity-management/details.vue'),
    meta: {
      title: '商机详情',
    },
  },
  {
    path: `${BASEPATH}/view-house-apply`,
    name: 'wisdomCanvassBusinessViewHouseApply',
    props: true,
    component: () => import(/* webpackChunkName: "business-opportunity-management.view-house-apply" */ '~views/wisdom-canvass-business/view-house-apply/index.vue'),
    meta: {
      title: '看房申请',
    },
  },
  // 客户管理
  {
    path: `${BASEPATH}/customer-manage`,
    name: 'customerManage',
    props: true,
    redirect: { name: 'customerManagement' },
    meta: {
      title: '客户管理',
    },
  },
  {
    path: `${BASEPATH}/customer-management`,
    name: 'customerManagement',
    props: { type: 0 },
    component: () => import(/* webpackChunkName: "customer-manage.customer-management" */ '~views/wisdom-canvass-business/customer-manage/index.vue'),
    meta: {
      title: '客户列表',
    },
  },
  {
    path: `${BASEPATH}/customer-tag`,
    name: 'customerTag',
    props: { type: 0 },
    component: () => import(/* webpackChunkName: "customer-manage.customer-tag" */ '~views/wisdom-canvass-business/customer-manage/customer-tag.vue'),
    meta: {
      title: '客户标签管理',
    },
  },
];

export default routes;
