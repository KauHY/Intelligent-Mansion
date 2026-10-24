/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-20 14:40:20
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-07 12:06:43
 */
import { ref, reactive } from 'vue';
import { listDictByTypes } from '~models/common';

const CACHE_KEYS = 'DICTIONARY_MAPS';

type KEYS<T> = T extends Array<infer K> ? K : never

export type DictionaryMap<T extends Array<string>> = {
    [ k in KEYS<T>]: SelectOptionsMap
}

// 字典数据处理
interface GroupTargetData { // 处理后台为了方便返回的数据结构，懒得扯皮了。
  [propKey:string]:{
    label:string,
    value:string|GroupTargetData
  }
}
interface SelectGroupData {
  value:string,
  options?:SelectGroupData,
  label:string
}
function getSelectGroupData(targetData:GroupTargetData) {
  return Object.entries(targetData).map(([index, item]) => ({
    label: item.label,
    options: item.value,
  }));
}

export const useHashMapDic = async <T extends Array<string>>(dicNames:T) => {
  // console.log(sessionStorage[CACHE_KEYS], '-----');
  const dictionaryMap:{[prop:string]:SelectOptionsMap} = sessionStorage[CACHE_KEYS] ? JSON.parse(sessionStorage[CACHE_KEYS]) : {};
  //   const result:DictionaryMap<T> = reactive<DictionaryMap<T>>({});
  const result = reactive<any>({});
  const requestKey = dicNames.filter((key) => {
    // debugger;
    if (dictionaryMap[key]) {
      result[key] = dictionaryMap[key];
      return false;
    }
    return true;
  });
  // console.log(requestKey, '----requestKey');
  if (!requestKey.length) return result as DictionaryMap<T>;
  const remoteData = await listDictByTypes(requestKey);
  if (dicNames.includes('FEE_TYPE') && remoteData.GROUP_FEE_TYPE) { // 兼容后台的狗屎代码
    remoteData.GROUP_FEE_TYPE = getSelectGroupData(remoteData.GROUP_FEE_TYPE) as any;
  }
  Object.assign(result, remoteData);
  Object.assign(dictionaryMap, remoteData);
  sessionStorage[CACHE_KEYS] = JSON.stringify(dictionaryMap);
  return result as DictionaryMap<T>;
};

export const useHashMapDics = <T extends Array<string>>(dicNames:T) => {
  const dictionaryMap:{[prop:string]:SelectOptionsMap} = sessionStorage[CACHE_KEYS] ? JSON.parse(sessionStorage[CACHE_KEYS]) : {};
  const result = reactive<any>({});

  const requestKey = dicNames.filter((key) => {
    // debugger;
    if (dictionaryMap[key]) {
      result[key] = dictionaryMap[key];
      return false;
    }
    // 初始化result（避免响应式丢失）
    result[key] = {};
    return true;
  });

  if (!requestKey.length) return result as DictionaryMap<T>;
  const init = async () => {
    const remoteData = await listDictByTypes(requestKey);
    if (dicNames.includes('FEE_TYPE') && remoteData.GROUP_FEE_TYPE) { // 兼容后台的狗屎代码
      remoteData.GROUP_FEE_TYPE = getSelectGroupData(remoteData.GROUP_FEE_TYPE) as any;
    }
    // Object.assign(result, remoteData);
    requestKey.forEach((key) => {
      Object.assign(result[key], remoteData[key] || {});
    });
    Object.assign(dictionaryMap, remoteData);
    sessionStorage[CACHE_KEYS] = JSON.stringify(dictionaryMap);
  };
  init();
  return result as DictionaryMap<T>;
};
