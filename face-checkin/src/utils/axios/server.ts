/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-18 17:29:33
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-07 18:54:26
 */
import axios, {
  AxiosError, AxiosRequestConfig,
} from 'axios';
import { notification, Spin } from 'ant-design-vue';
import whiteList from '@/constants/white.list';
import { Loading } from '~components/loading';
import AXIOS_CONFIG from './axios.conf';
import { RequestConfig } from './type';

function isExistWhiteList(autUrl:string) {
  // debugger
  return whiteList.some((url) => autUrl.indexOf(url) !== -1);
}

// 判断是否是企业端
const companyFlag = sessionStorage.localStorageKey === 'WISDOM-PARK-COMPANY';
// console.log(companyFlag, 'companyFlag');

const loadingInstance = Loading();
const instance = axios.create(AXIOS_CONFIG);
// 添加请求拦截器
instance.interceptors.request.use((config:RequestConfig) => {
  if (!config.hiddenLoading) {
    loadingInstance.show();
  }
  const { url } = config;
  if (!window.$localStorage.Authorization && !isExistWhiteList(url as string) && (url as string).indexOf('overviewMeeting') === -1) {
    if (companyFlag) {
      window.location.replace(`#/wisdom-park-company/login-company/${sessionStorage.companyId}`);
    } else {
      window.location.replace('#/login');
    }
    window.$localStorage.removeItem('Authorization');
  }
  config.headers.Authorization = window.$localStorage.Authorization || '';
  return config;
},
(error:AxiosError) => {
  loadingInstance.close();
  return Promise.reject(error);
});

// 添加响应拦截器
instance.interceptors.response.use((response) => {
  // 对响应数据做点什么
  loadingInstance.close();
  const {
    code, message, errorMsg, success, callId, errorCode,
  } = response?.data;
  if (!response.headers['content-type'].includes('application/json')) {
    return response;
  }
  if (callId) {
    if (success) {
      return response;
    }
    notification.error({
      message: `提示-${errorCode}`,
      description: errorMsg || '网络异常，请稍后再试',
    });
    return Promise.reject(response.data);
  }
  // if(!headers['content-type'].includes('application/json')){}
  if (code === 10000 || code === 0) {
    return response.data;
  } if (code === 40009) {
    setTimeout(() => {
      if (companyFlag) {
        window.location.replace(`#/wisdom-park-company/login-company/${sessionStorage.companyId}`);
      } else {
        window.location.replace('#/login');
      }
      window.$localStorage.removeItem('Authorization');
    }, 300);
  }
  notification.error({
    message: `提示-${code}`,
    description: message || '网络异常，请稍后再试',
  });
  return Promise.reject(response.data);
}, (error:AxiosError) => {
  console.log(error.message, '----error');
  loadingInstance.close();
  notification.error({
    message: '服务异常',
    description: error.message,
  });
  return Promise.reject(error);
});

export default instance;
