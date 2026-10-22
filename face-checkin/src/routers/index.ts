/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-03 21:18:33
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 11:17:27
 */
import { createRouter, createWebHashHistory } from 'vue-router';
import { nextTick } from 'vue';
import getAsyncRouter from '@/routers/routes/index';
import Menu from '@/models/meun';
import NProgress from 'nprogress';
import { useUserStore } from '@/store/user';
import { useMenus } from '~composition/user';
import commonRouter from './routes/common';
import { notFound } from './routes/others';

const { getUserMenuList } = new Menu();
const router = createRouter({
  history: createWebHashHistory(compileConf.publicPath),
  routes: commonRouter,
});
// console.log(compileConf, '---compileConf');
const asyncRouter = getAsyncRouter();
const addRouter = (menuList:Array<MenuItem>) => {
  const currentUserRouter = asyncRouter.filter(({ name, meta }) => {
    if (meta && meta.notAuthentication) return true; // 不用鉴权的路由
    if (menuList.find(({ routeName }:MenuItem) => routeName === name)) return true;
    return false;
  });
  console.log(currentUserRouter, '----currentUserRouter');
  router.addRoute(
    {
      path: '/',
      name: 'index',
      props: true,
      redirect: { name: 'home' },
      component: () => import(/* webpackChunkName: "layout" */ '~views/layout/layout.vue'),
      children: [{
        path: 'home',
        name: 'home',
        props: true,
        component: () => import(/* webpackChunkName: "home" */ '~views/home/home.vue'),
        meta: {
          title: '首页',
        },
      },
      ...currentUserRouter,
      notFound,
      ],
    },
  );
  // console.log(router, notFound, '路由添加完成');
  // router.replace({ path: '/' });
};

export const refreshRouter = async () => {
  await useMenus(addRouter, getUserMenuList);
};

router.beforeEach(async (to, from, next) => {
  const useStore = useUserStore();
  const { name } = to;
  if (['testDemo', 'login', 'loginCompany', 'resetPassword', 'scheduledOverview', 'companySuccess', 'procurementDetail'].includes(name as string)) { // 排除login
    next();
    return;
  }
  // 缓存特定页面
  if (!useStore.routerInitComplete) { // 是否需要拉去路由权限刷新页面重新获取路由权限
    await refreshRouter();
    next({ ...to, replace: true });
  } else {
    next();
  }
});
router.beforeResolve((to) => {
  NProgress.start();
  return true;
});
router.afterEach((to) => {
  NProgress.done();

  return true;
});

export default router;
