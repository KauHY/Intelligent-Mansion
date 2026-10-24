/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-30 16:24:31
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 14:24:07
 */
import { RouteLocationNormalized } from 'vue-router';

/**
 * @description: 设置缓存特定页面
 * @param {RouteLocationNormalized} to
 * @param {RouteLocationNormalized} form
 * @return {*}
 * @author: GUOCHAO82
 */
export default (to:RouteLocationNormalized, form:RouteLocationNormalized) => {
  const { name: toName } = to;
  const { name: formName } = form;
};
