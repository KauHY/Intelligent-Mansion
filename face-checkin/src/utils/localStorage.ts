/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-10 11:40:10
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-09 11:11:15
 */
import { UserInfo } from '~models/user';

let localStorageKey = '';
const urlHash = window.location.hash;
if (urlHash.indexOf('wisdom-park-company') === -1) {
  localStorageKey = 'WISDOM-PARK';
} else {
  localStorageKey = 'WISDOM-PARK-COMPANY';
}
sessionStorage.localStorageKey = localStorageKey;
interface CustomStore {
  userInfo?:UserInfo
  Authorization?:string
  routerRecordList?:MenuItem[],
  theme?:string,
  [key:string]:any
}

interface LocationStorage extends CustomStore {
  removeItem:(key:string)=>void,
  setItem:(key:string, value:string)=>void,
  storage:CustomStore,
  length:number,
  getItem:(key:string)=>string,
}

const $localStorage:LocationStorage = {
  get storage() {
    let storage;
    try {
      storage = window.localStorage[localStorageKey] ? JSON.parse(window.localStorage[localStorageKey]) : undefined;
    } catch (error) {
      console.error('storage->pass报错', error);
    }
    return storage;
  },
  get length() {
    return this.storage ? Object.keys(this.storage)?.length : 0;
  },
  removeItem(key:string) {
    const storage = { ...this.storage };
    delete storage[key];
    window.localStorage[localStorageKey] = JSON.stringify(storage);
  },
  setItem(key:string, value:string) {
    const storage = { ...this.storage };
    storage[key] = value;
    window.localStorage[localStorageKey] = JSON.stringify(storage);
  },
  getItem(key:string) {
    const storage = { ...this.storage };
    // debugger;
    return storage[key];
  },
};

const $localStorageProxy = new Proxy($localStorage, {
  get(target, key:string) {
    // eslint-disable-next-line no-prototype-builtins
    if (target.hasOwnProperty(key)) return (target as any)[key];
    return target.getItem(key);
  },
  set(target, key:string, value) {
    try {
      target.setItem(key, value);
      return true;
    } catch (error) {
      return false;
    }
  },
});

Object.defineProperty(window, '$localStorage', {
  get() {
    return $localStorageProxy;
  },
});

declare global {
  interface Window {
    /** 操作localStorage务必使用此属性 */
    $localStorage: LocationStorage;
    /** 高德地图code */
    _AMapSecurityConfig:{
      securityJsCode:string
    }
  }
}
