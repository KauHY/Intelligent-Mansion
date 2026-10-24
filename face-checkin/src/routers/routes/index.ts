/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-03 21:24:39
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-22 17:24:25
 */
import { RouteRecordRaw } from 'vue-router';

const routeFiles = require.context('./', false, /\.ts$/);
type StaticMenuMeta = boolean | {
  parentName?: string
  icon?: string
};

type RouteMenuMeta = {
  title?: string
  staticMenu?: StaticMenuMeta
};

const asyncRouter = routeFiles.keys().reduce((routes, key) => {
  if (['./index.ts', './common.ts', './others.ts'].includes(key)) return routes;
  const { default: __default } = routeFiles(key);
  if (!__default) return routes;
  return [...routes, ...__default];
}, [] as Array<RouteRecordRaw>);

const buildStaticMenuList = (routes:Array<RouteRecordRaw>): Array<MenuItem> => {
  const menus: Array<MenuItem> = [];
  routes.forEach((route) => {
    const { meta, name, path } = route;
    const routeMeta = (meta || {}) as RouteMenuMeta;
    const staticMenu = routeMeta.staticMenu;
    const routeName = typeof name === 'string' ? name : '';
    if (!staticMenu || !routeName || !routeMeta.title) return;
    const staticMenuConf = staticMenu === true ? {} : staticMenu;
    menus.push({
      apiUrl: '',
      id: routeName,
      routeName,
      route: path,
      status: 1,
      type: 0 as 0,
      name: routeMeta.title,
      parentId: staticMenuConf.parentName || '0',
      isMenu: 1,
      icon: staticMenuConf.icon,
    });
  });
  return menus;
};

const getAsyncRouter = () => asyncRouter;

export const getStaticMenusFromRoutes = (): Array<MenuItem> => buildStaticMenuList(asyncRouter);

export default getAsyncRouter;
