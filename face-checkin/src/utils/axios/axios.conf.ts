/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-18 17:27:52
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-28 16:02:53
 */
import { AxiosRequestConfig } from 'axios';
import Qs from 'qs';

const BASE_URL = compileConf.serverBaseUrl;
// const BASE_URL = 'http://localhost:9000/smartPark/';

const AXIOS_CONFIG: AxiosRequestConfig = {
  timeout: 30000,
  baseURL: BASE_URL,
  paramsSerializer(params) {
    return Qs.stringify(params, { indices: false });
  },
  // headers: {
  //   'Content-Type': 'application/x-www-form-urlencoded',
  // },
  // `withCredentials` 表示跨域请求时是否需要使用凭证
  //   withCredentials: false, // default
  // `onUploadProgress` 允许为上传处理进度事件
  //  onUploadProgress: function (progressEvent) {
  //     // Do whatever you want with the native progress event
  //   },

  //   // `onDownloadProgress` 允许为下载处理进度事件
  //   onDownloadProgress: function (progressEvent) {
  //     // 对原生进度事件的处理
  //   },
};

export default AXIOS_CONFIG;

export {
  BASE_URL,
};
