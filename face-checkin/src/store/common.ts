/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-29 16:06:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-21 18:18:54
 */
/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-03 20:24:49
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-23 14:12:18
 */
import { defineStore } from 'pinia';

const createState = () => {
  const cachePageNames:string[] = ['Transition', 'page-fade'];
  return {
    cachePageNames,
    collapsed: false,
  };
};

export type Common = ReturnType<typeof createState>
//  type PayloadKey = keyof UserState
export const useCommonStore = defineStore({
  id: 'common',
  state: () => createState(),
  actions: {
    addCachePageNames(pageName:string) {
      if (!this.cachePageNames.includes(pageName)) {
        this.cachePageNames = [...this.cachePageNames, pageName];
        // state.cachePageNames.push(pageName); // 不能用push
      }
    },
    delCachePageNames(pageName:string) {
      const index = this.cachePageNames.indexOf(pageName);
      if (index !== -1) {
        this.cachePageNames.splice(index, 1);
      }
    },
    setCollapsed(collapsed:boolean) {
      this.collapsed = collapsed;
    },
  },
});
