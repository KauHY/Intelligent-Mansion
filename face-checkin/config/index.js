/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-01 11:38:54
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-05 16:46:45
 */
const compileConf = {
  dev: {
    publicPath: '/',
    projectName: 'dist',
    serverBaseUrl: '/smartPark',
  },
  sit: {
    publicPath: '/wisdom-park-sit/',
    projectName: 'wisdom-park-sit',
    serverBaseUrl: '/smartParkSit',
  },
  prod: {
    publicPath: '/wisdom-park/',
    projectName: 'wisdom-park',
    serverBaseUrl: '/smartPark',
  },
};

module.exports = compileConf[process.env.ENV_MODE || 'development'];
