/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-21 10:31:46
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-08-31 11:25:21
 */
exports.assemblyProxyTable = (proxyTable, addOnProxyRes) => {
  const proxyTableConf = {};
  Object.entries(proxyTable).forEach(([path, conf]) => {
    proxyTableConf[path] = addOnProxyRes(conf, [10000]);
  });
  return proxyTableConf;
};
