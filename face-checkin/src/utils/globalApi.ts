/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-01 19:56:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-06 17:04:37
 */
import { CreateAppFunction } from 'vue';

const sort = <T extends any[]|Object>(opt:T, sortKey = 'sort') => {
  try {
    const baseOpt:any[] = Array.isArray(opt) ? opt : Object.values(opt);
    const temp = baseOpt.sort((current, next):any => current[sortKey] - next[sortKey]);
    return temp;
  } catch (error) {
    return opt;
  }
};

export default (app:ReturnType<CreateAppFunction<Element>>) => {
  app.config.globalProperties.$sort = sort;
};
