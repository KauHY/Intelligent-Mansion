/*
 * @Description:系统管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:30:24
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-14 17:31:58
 */
import { RouteRecordRaw } from 'vue-router';

const BASEPATH = '/system-management';
const routes: Array<any> = [
  {
    path: `${BASEPATH}`,
    name: 'systemManagement',
    props: true,
    redirect: { name: 'systemManagementUser' },
    meta: {
      title: '系统管理',
    },
  },
  // {
  //   path: `${BASEPATH}/tenant`,
  //   name: 'systemManagementTenant',
  //   props: true,
  //   component: () => import(/* webpackChunkName: "system-management.tenant" */ '~views/system-management/tenant/tenant.vue'),
  //   meta: {
  //     title: '租户管理',
  //   },
  // },
  {
    path: `${BASEPATH}/user`,
    name: 'systemManagementUser',
    props: true,
    component: () => import(/* webpackChunkName: "system-management.user" */ '~views/system-management/user/user.vue'),
    meta: {
      title: '用户管理',
    },
  },
  {
    path: `${BASEPATH}/company`,
    name: 'companyManagement',
    props: true,
    component: () => import(/* webpackChunkName: "system-management.user" */ '~views/system-management/company/user.vue'),
    meta: {
      title: '企业端管理',
    },
  },
  {
    path: `${BASEPATH}/organizational-structure`,
    name: 'systemManagementOrganizationalStructure',
    props: true,
    component: () => import(/* webpackChunkName: "system-management.organizationalStructure" */ '~views/system-management/organizational-structure/organizational-structure.vue'),
    meta: {
      title: '组织架构',
    },
  },
  {
    path: `${BASEPATH}/role`,
    name: 'systemManagementRole',
    props: true,
    component: () => import(/* webpackChunkName: "system-management.role" */ '~views/system-management/role/role.vue'),
    meta: {
      title: '角色管理',
    },
  },
  {
    path: `${BASEPATH}/menu-list`,
    name: 'menuManagementMenuList',
    props: true,
    component: () => import(/* webpackChunkName: "menu-management.add-menu" */ '~views/system-management/menu-management/menu-list.vue'),
    meta: {
      title: '菜单列表',
    },
  },
];

export default routes;
