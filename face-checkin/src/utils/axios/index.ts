/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-18 17:41:01
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-07 18:53:59
 */

import { AxiosPromise, AxiosRequestConfig, AxiosResponse } from 'axios';
import axios from './server';

import { RequestConfig } from './type';

// const cacheRequest = (cacheKey:string) => {
//   if()
// };

/**
 * @description: 发送请求
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
function http<T=any>({
  url,
  data,
  method = 'post',
  // responseType,
  headers,
  ...otherProps
}: RequestConfig): AxiosPromise<T> {
  const { cacheKey } = otherProps;
  return new Promise((resolve, reject) => {
    if (!cacheKey || sessionStorage[cacheKey] === undefined) {
      axios({
        url,
        method,
        params: method === 'get' ? data : '',
        data: method !== 'get' ? data : {},
        headers,
        ...otherProps,
      }).then((res) => {
        const { data } = res;
        if (cacheKey) {
          sessionStorage[cacheKey] = JSON.stringify(data);
        }
        resolve(res);
      }).catch((err) => reject(err));
      return;
    }
    resolve({ data: JSON.parse(sessionStorage[cacheKey]) } as AxiosResponse);
  });
}

export default http;
