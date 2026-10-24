/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-21 17:08:25
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-27 10:44:59
 */
import { defineStore } from 'pinia';
import { UserInfo } from '~models/user';

const menuList:MenuItem[] = [];
const baseMenuList:MenuItem[] = [];
const routerRecordList:MenuItem[] = window.$localStorage.routerRecordList ? window.$localStorage.routerRecordList : [];
const openkeys:string[] = [];

const createState = () => ({
  routerInitComplete: false,
  userInfo: (window.$localStorage.userInfo ? window.$localStorage.userInfo : {}) as UserInfo,
  baseMenuList,
  menuList,
  routerRecordList,
  openkeys,
});

export type UserState = ReturnType<typeof createState>
export const useUserStore = defineStore({
  id: 'user',
  state: () => createState(),
  actions: {
    addMenu(menuList:MenuItem[]) {
      this.menuList = menuList;
    },
    setRouterInitComplete(isComplete:boolean) {
      this.routerInitComplete = isComplete;
    },
    setRouterRecordList(routerRecordList:MenuItem[]) {
      this.routerRecordList = routerRecordList;
      window.$localStorage.routerRecordList = routerRecordList;
    },
    moduleSet<T=any>(payload:{key:keyof UserState; data:T}) {
      // debugger;
      const { key, data } = payload;
      this[key] = data;
    },

  },
});
