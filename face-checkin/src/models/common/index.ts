/*
 * @Description: 公共类
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-11 11:18:51
 * @LastEditors: your name
 * @LastEditTime: 2022-03-16 12:58:03
 */
import axios from '~utils/axios';
import URLS from '~models/urlConstant/common';
import { encrypt } from '~utils/tool';
import { BASE_URL } from '~utils/axios/axios.conf';
import { UploadReuqestParams, FileDetails } from './type';

/**
 * @description: 获取图形验证码
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const getImageCode = () => {
  const KEY = encrypt(`${Date.now()}_code`);
  return {
    url: `${BASE_URL}${URLS.getImgCode}?key=${KEY}`,
    key: KEY,
  };
};

/**
 * @description: 校验验证码
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const validateImgCode = async (key:string, code:any) => {
  const { data } = await axios({
    url: `/park-system/common/validateImgCode?key=${key}&code=${code}`,
    method: 'get',
  });
  console.log(data, 'data');
  return data;
};

// type b = typeof a[number]
// type cc = {
//  [k in b]:string
// }

export const listDictByTypes = async (codes: string[]) => {
  // const KEY = btoa(String(Date.now()));
  const { data } = await axios({
    url: URLS.listDictByTypes,
    data: codes,
    // cacheKey: 'dictionaryData',
  });
  // const targetData = data.map((item: SelectOptions) => {
  //   return
  // });
  return (data || {}) as {[propKey:string]:SelectOptionsMap};
};

export const upload = async ({ key, formData, onProgress }:UploadReuqestParams) => {
  const { data } = await axios({
    url: `${URLS.upload}?key=${key}`,
    data: formData,
    onUploadProgress: onProgress,
  });
  return data as string;
};

export const getPreviewUrl = (key:string) => {
  if (!key) return '';
  return `${BASE_URL}/park-common/file/getFile?key=${key}`;
};

// 获取文件的绝对路径

export const getFileUrl = async (key:string) => {
  if (!key) return '';
  const { data } = await axios({
    url: `/park-common/file/getFileUrl?key=${key}`,
    method: 'get',
  });
  return data as string;
};

// 根据key获取文件详细信息
export const getFilesDetails = async (keys:string[]|string) => {
  // if (!keys) return '';
  if ((Array.isArray(keys) && !keys.length) || !keys) return [];
  const { data } = await axios<{[key:string]:FileDetails}>({
    url: '/park-common/file/getFileDetailByKeys',
    data: keys,
  });
  return data;
};
