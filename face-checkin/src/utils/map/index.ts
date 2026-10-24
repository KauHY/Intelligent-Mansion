/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-27 11:45:32
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-09 10:59:13
 */

import { load } from '@amap/amap-jsapi-loader';
import '@amap/amap-jsapi-types';
import { loadOptions } from './type';

const mapLoad = async ({
  container, mapOptions, plugins = [], AMapUIPlugins = [],
}:loadOptions) => {
  const AMap = await load({
    key: 'f7dbfd00b7dcd1010b8bff494b5d2bb0', // 申请好的Web端开发者Key，首次调用 load 时必填
    version: '1.4.15', // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: plugins || [], // 需要使用的的插件列表，如比例尺'AMap.Scale'等
    AMapUI: { // 是否加载 AMapUI，缺省不加载
      version: '1.1', // AMapUI 缺省 1.1
      plugins: AMapUIPlugins, // 需要加载的 AMapUI ui插件
    },
    Loca: { // 是否加载 Loca， 缺省不加载
      version: '1.3.2', // Loca 版本，缺省 1.3.2
    },
  });
  const map:AMap.Map = new AMap.Map(container, mapOptions);
  return {
    AMap,
    map,
  };
};
// export {
//   mapLoad,
// };
export default mapLoad;
export * from './type';
