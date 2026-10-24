/*
 * @Author: chenhao
 * @Date: 2022-03-22 15:50:37
 * @LastEditTime: 2022-04-13 12:24:57
 * @FilePath: \wisdom-park\src\routers\routes\enterprise-service.ts
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/enterprise-service';

const routes: Array<any> = [

  {
    path: `${BASEPATH}`,
    name: 'enterpriseService',
    redirect: { name: 'enterpriseServiceMsg' },
    meta: {
      title: '产业服务',
    },
  },
  {
    path: `${BASEPATH}/enterpriseServiceList`,
    name: 'enterpriseServiceMsg',
    props: true,
    component: () => import('~views/enterprise-service/enterprise-industrial-service/index.vue'),
    meta: {
      title: '企业产业链服务',
    },
  },
  {
    path: `${BASEPATH}/enterpriseServiceCompanyGroup`,
    name: 'enterpriseServicecompanyGroup',
    props: true,
    component: () => import('~views/enterprise-service/company-group/index.vue'),
    meta: {
      title: '企业群组管理',
    },
  },
  {
    path: `${BASEPATH}/supplyServices`,
    name: 'supplyServices',
    props: true,
    component: () => import('~views/enterprise-service/supply-service/index.vue'),
    meta: {
      title: '供应服务',
    },
  },
  {
    path: `${BASEPATH}/companyService`,
    name: 'companyService',
    props: true,
    component: () => import('~views/enterprise-service/company-service/index.vue'),
    meta: {
      title: '企业服务',
    },
  },
  {
    path: `${BASEPATH}/purchaseService`,
    name: 'purchaseService',
    props: true,
    component: () => import('~views/enterprise-service/purchase-service/index.vue'),
    meta: {
      title: '采购服务',
    },
  },
  {
    path: `${BASEPATH}/bid-service`,
    name: 'bidService',
    props: true,
    component: () => import('~views/enterprise-service/bid-service/index.vue'),
    meta: {
      title: '投标信息详情',
    },
  },

];

export default routes;
