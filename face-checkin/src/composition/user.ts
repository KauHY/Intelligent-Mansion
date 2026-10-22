/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 12:35:32
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 16:46:57
 */
import {
  computed, ref, onMounted, reactive, toRef, toRefs, watch,
} from 'vue';

import { storeToRefs } from 'pinia';
import { RouteLocationRaw, useRoute, RouteLocationNormalizedLoaded } from 'vue-router';

import { useUserStore } from '@/store/user';
import { getStaticMenusFromRoutes } from '@/routers/routes/index';

// import asyncRouter from '@/routers/routes';

// import { fetchMenu  } from '~services/user';
// import { MenuItem } from '@/models/meun/type';
import { rootMenu } from '@/constants/config';
import { UserInfo } from '@/models/user';
// import { MenuItem } from 'ant-design-vue';
import { getTree } from '~utils/tool';

const normalizeMenuItem = (menuItem: MenuItem): MenuItem => ({
  ...menuItem,
  id: String(menuItem.id),
  parentId: String(menuItem.parentId),
});

const mergeStaticMenus = (menuList: Array<MenuItem>): Array<MenuItem> => {
  const normalizedMenuList = menuList.map(normalizeMenuItem);
  const routeNameIdMap = new Map<string, string>(
    normalizedMenuList.map(({ routeName, id }) => [routeName, id]),
  );
  const existKeys = new Set(
    normalizedMenuList.flatMap(({ id, routeName }) => [`id:${id}`, `routeName:${routeName}`]),
  );
  const appendedStaticMenus = getStaticMenusFromRoutes()
    .map((menuItem: MenuItem): MenuItem => {
      const normalizedMenuItem = normalizeMenuItem(menuItem);
      const parentId = routeNameIdMap.get(normalizedMenuItem.parentId) || normalizedMenuItem.parentId;
      routeNameIdMap.set(normalizedMenuItem.routeName, normalizedMenuItem.id);
      return {
        ...normalizedMenuItem,
        parentId,
      };
    })
    .map(normalizeMenuItem)
    .filter(({ id, routeName }: MenuItem) => !existKeys.has(`id:${id}`) && !existKeys.has(`routeName:${routeName}`));

  return [...normalizedMenuList, ...appendedStaticMenus];
};
/**
 * @description: 获取侧边导航
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useMenus = async (addRouter: Function, getUserMenuList:()=>Promise<Array<MenuItem>>) => {
  const userMenuList = await getUserMenuList();
  const data = [rootMenu as MenuItem, ...mergeStaticMenus(userMenuList)];
  const { moduleSet, addMenu, setRouterInitComplete } = useUserStore();
  moduleSet({
    key: 'baseMenuList',
    data,
  });
  const menuTree = getTree(data, rootMenu, ['0']);
  const rotuerList = data.filter(({ type }:MenuItem) => type === 0 || type === 2);

  addMenu(menuTree as MenuItem[]);
  setRouterInitComplete(true); // 路由加载完成
  console.log(menuTree, 'menulist');
  // debugger;
  addRouter(rotuerList);
  // };
  // getMenus();
};

/**
 * @description: 根据当前路由获取路由记录
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useRouterRecord = (currentRoute:RouteLocationNormalizedLoaded, baseMenuList:Array<MenuItem>) => {
  // debugger;
  /**
 * @description: 根据当前name查找父级别name
 * @param {*} routeName
 * @return {*}
 * @author: GUOCHAO82
 */
  const useStore = useUserStore();
  const { setRouterRecordList, moduleSet } = useStore;
  const { routerRecordList: storeSouterRecordList } = storeToRefs(useStore);
  const routerRecordList:Array<MenuItem> = []; // 当前激活的路由
  const { name, query, params } = currentRoute;
  let openKeys:Array<string> = []; // 要展开的菜单

  const currentMenu = baseMenuList.find((item:MenuItem) => name === item.routeName) as MenuItem;
  if (currentMenu) {
    routerRecordList.unshift({ ...currentMenu, query: { ...query }, params: { ...params } } || rootMenu); // 推入当前路由
  }
  pushParentMenu(currentMenu); // 推入历史路由
  openKeys = routerRecordList.map(({ routeName, id }) => `${routeName}_${id}`);
  setRouterRecordList(routerRecordList);
  // 更新缓存
  console.log(routerRecordList, openKeys, '----routerRecordList');
  moduleSet({
    key: 'openkeys',
    data: openKeys,
  });

  // 根据当前路由查询历史路由
  function pushParentMenu(currentMenu:MenuItem) {
    // debugger;
    // 先查找缓存中有没有父级路由
    let parentMenu:MenuItem|undefined;
    parentMenu = storeSouterRecordList.value.find((item) => item.id === currentMenu?.parentId);
    if (!parentMenu) {
      parentMenu = baseMenuList.find((item:MenuItem) => item.id === currentMenu?.parentId);
    }
    if (parentMenu) {
      // debugger;
      routerRecordList.unshift(parentMenu);
      pushParentMenu(parentMenu);
    }
  }
  return {
    routerRecordList,
    openKeys,
  };
};

export const useUserInfo = () => {
  const userInfo = window.$localStorage?.userInfo as UserInfo;
  userInfo.isPlatformAdminRole = userInfo.headOrgId === '0';
  return userInfo;
};
