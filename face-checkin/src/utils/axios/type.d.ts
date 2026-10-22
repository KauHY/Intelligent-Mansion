/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-07 18:52:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-07 18:53:28
 */
import { AxiosRequestConfig } from 'axios';

interface RequestConfig extends AxiosRequestConfig {
  cacheKey?:string // 接口缓存key
  hiddenLoading?:boolean
}

export {
  RequestConfig,
};
