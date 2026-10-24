/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 18:13:02
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-12 16:48:34
 */
import { CreateAppFunction } from 'vue';

const componentFiles = require.context('./', true, /\.ts$/);
export default (app:ReturnType<CreateAppFunction<Element>>) => {
  componentFiles.keys().forEach((key) => {
    if (key === './index.ts') return;
    const { default: __default } = componentFiles(key);
    console.log(__default, key, __default?.name, '---__default');
    if (__default?.name) {
      app.component(__default.name, __default);
    }
  });
};
