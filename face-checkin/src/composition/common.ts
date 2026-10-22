/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 14:07:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-29 11:05:11
 */
import {
  ref, computed, reactive, Ref, watch, markRaw, onMounted,
} from 'vue';

import { SearchConfItem } from '@/components/search-box/type';
import { useCommonStore } from '@/store/common';
import { getImageCode, validateImgCode } from '@/models/common';
import { useDownload } from './useDownload';
// type a = ReturnType<typeof defineComponent>
interface SearchConf {
    [propsName:string]: SearchConfItem
}

interface OperateDialog<T=any>{
    cp: any
    data:Partial<T>
    title:string
    type:string
    visible:boolean
}

interface SearchListPageParams<R, T> {
  searchMethod:(searchParams:T, isInit:boolean)=>Promise<SearchResult<R>>,
  searchConf:SearchConf,
  operateDialogCp?:OperateDialog['cp'],
  downloadMethod?:(searchParams:T)=>Promise<DownloadReturnData>,
}

type SearchMethod<T, R> = (searchParams:T, isInit:boolean)=>Promise<SearchResult<R>>

/**
 * @description: 分页搜素hook
 * @param {*} T
 * @param {*} R
 * @param {T} param
 * @return {*}
 * @author: GUOCHAO82
 */

export const getConfValue = (searchConf:SearchConf) => {
  const param:{[propsName:string]:any} = {};
  Object.entries(searchConf).forEach(([key, item]) => {
    if (item.tag === 'date-range') { // 日期范围选择器
      const value = item.value || [];
      if (item.valueFormat && item.valueFormat === 'YYYY-MM-DD') {
        param[item.startTimeKey as string] = value[0] ? `${value[0]}` : '';
        param[item.endTimeKey as string] = value[1] ? `${value[1]} ` : '';
      } else {
        param[item.startTimeKey as string] = value[0] ? `${value[0]} 00:00:00` : '';
        param[item.endTimeKey as string] = value[1] ? `${value[1]} 23:59:59` : '';
      }
    } else {
      param[key] = item.value;
    }
  });
  // pageNum: pagination.current,
  // pageSize: pagination.pageSize,
  return param;
};

export const useSearchListPage = <R, T extends BaseSearchParams=any>(
  tempSearchMethod:SearchMethod<T, R>|SearchListPageParams<R, T>,
  tempSearchConf?:SearchConf,
  tempOperateDialogCp?:OperateDialog['cp'],
  tempDownloadMethod?:(searchParams:T)=>Promise<DownloadReturnData>,
) => { // 分页查询
  // 兼容之前的传参
  let searchListPageParams:SearchListPageParams<R, T>;
  if (typeof tempSearchMethod === 'function') {
    searchListPageParams = {
      searchMethod: tempSearchMethod as SearchMethod<T, R>,
      searchConf: tempSearchConf as SearchConf,
      operateDialogCp: tempOperateDialogCp,
      downloadMethod: tempDownloadMethod,
    };
  } else {
    searchListPageParams = tempSearchMethod;
  }

  const {
    searchMethod,
    searchConf,
    downloadMethod,
    operateDialogCp,
  } = searchListPageParams;

  const pagination = reactive({ // 分页信息
    total: 0,
    showSizeChanger: true,
    pageSizeOptions: ['10', '20', '30', '40', '100'],
    current: 1,
    pageSize: 10,
  }); // 分页配置
  const tableList = ref<Array<any>>([]);
  /**
   * @description: 提取配置项里的value值
   * @param {*} T
   * @return {*}
   * @author: GUOCHAO82
   */
  const getSearchConfValue = () => {
    const param = getConfValue(searchConf);
    const { current, pageSize } = pagination;
    Object.assign(param, {
      pageNum: current,
      pageSize,
    });
    return param as T;
  };
  /**
   * @description: 搜素方法
   * @param {boolean} init
   * @return {*}
   * @author: GUOCHAO82
   */
  const search = async (init:boolean, current?:Number) => {
    // debugger;
    try {
      const params = getSearchConfValue();
      if (init) { // 初始化查询|点击查询按钮
        params.pageNum = 1;
        pagination.current = 1;
      }
      if (window.$localStorage.detail) {
        pagination.current = window.$localStorage.Pageindex;
      } else {
        pagination.current = 1;
        window.$localStorage.removeItem('Pageindex');
      }
      const { total, list } = await searchMethod({
        ...params,
      }, init);
      window.$localStorage.Pageindex = current;
      tableList.value = list;
      pagination.total = total;
    } catch (error) {
      console.log(error);
    }
  };

  const download = async () => {
    if (!downloadMethod) return;
    const params = getSearchConfValue() as Partial<T>;
    delete params.pageNum;
    delete params.pageSize;
    const data = await downloadMethod({
      ...params as T,
    });
    useDownload(data);
  };

  /**
   * @description:分页
   * @param {object} pageInfo
   * @return {*}
   * @author: GUOCHAO82
   */
  const pageChange = async ({ current, pageSize }:{pageSize:number, current:number}) => {
    // console.log(current, '----current');
    if (pageSize === undefined || pageSize === undefined) return;
    pagination.current = current;
    pagination.pageSize = pageSize;
    search(false, current);
  };

  //   const operateDialogCp: DefineComponent|Ref<null> = ref(null);
  const operateDialogConf = reactive<OperateDialog<R>>({
    cp: null,
    title: '',
    type: '',
    data: {},
    visible: false,
  });

  /**
   * @description: 弹框展示隐藏
   * @param {OperateDialog} cp
   * @param {boolean} isShow
   * @return {*}
   * @author: GUOCHAO82
   */
  const showDialog = (cp:OperateDialog['cp'], isShow:boolean) => {
    if (isShow) { // 显示
      operateDialogConf.cp = markRaw(cp);
      setTimeout(() => {
        operateDialogConf.visible = true;
      }, 200);
      return;
    }
    operateDialogConf.visible = false; // 隐藏
    setTimeout(() => {
      operateDialogConf.cp = null;
    }, 200);
  };

  /**
   * @description: 是否自动控制弹框展示隐藏
   * @param {*} operateDialogCp
   * @return {*}
   * @author: GUOCHAO82
   */
  if (operateDialogCp) {
    watch(() => operateDialogConf.visible, (show) => {
      showDialog(operateDialogCp, show);
    });
  }
  return {
    search,
    tableList: (tableList as unknown) as Ref<R[]>,
    pagination,
    pageChange,
    showDialog,
    operateDialogConf,
    getSearchConfValue,
    download,
  };
};

// 图标操作

type SearchChartMethod<T, R> = (searchParams:T, isInit:boolean)=>Promise<R>
export const useCharts = <R, T=any>({ searchMethod, searchConf }:{
  searchMethod:SearchChartMethod<T, R>
  searchConf?:SearchConf,
}) => {
  /**
   * @description: 提取配置项里的value值
   * @param {*} T
   * @return {*}
   * @author: GUOCHAO82
   */
  const getSearchConfValue = () => {
    const param:{[propsName:string]:any} = {};
    if (searchConf) {
      Object.entries(searchConf).forEach(([key, item]) => {
        if (item.tag === 'date-range') { // 日期范围选择器
          const value = item.value as Array<string>;
          param[item.startTimeKey as string] = value[0] ? `${value[0]} 00:00:00` : '';
          param[item.endTimeKey as string] = value[1] ? `${value[1]} 23:59:59` : '';
        } else {
          param[key] = item.value;
        }
      });
    }
    return param as T;
  };

  const chartData = ref<R>();
  /**
     * @description: 搜素方法
     * @param {boolean} init
     * @return {*}
     * @author: GUOCHAO82
     */
  const search = async (init:boolean) => {
    try {
      const data = await searchMethod({
        ...getSearchConfValue(),
      }, init);
      chartData.value = data;
    } catch (error) {
      console.log(error);
    }
  };
  return {
    chartData,
    search,
  };
};

/**
 * @description: 远程搜索下拉框
 * @param {*} remoteSearchMethod 远程搜索方法
 * @param {string} optionsValueKey  下拉框的下拉选项options的value key，用于去重
 * @param {boolean} disableAutoSearch
 * @return {*}
 * @author: GUOCHAO82
 */
export const useRemoteFilterSelect = <T>(remoteSearchMethod:(params:any)=>Promise<T>, conf:{optionsValueKey?:string, disableAutoSearch?:boolean} = {}) => {
  const { optionsValueKey = 'value', disableAutoSearch = true } = conf;
  const loading = ref(false);
  const list = ref<any>([]);
  const defaultOptions = ref<any[]>([]); // 初始下拉项
  let defaultOptionsValues:any[] = []; // 初始下拉项value值
  const otherParams = ref<{[props:string]:any}>({});
  let timeId:NodeJS.Timeout;
  /**
   * @description:
   * @param {string} value 搜索内容
   * @param {boolean} isInit 是否是初始化
   * @return {*}
   * @author: GUOCHAO82
   */
  const remoteSearch = (value:string, isInit?:boolean) => {
    try {
      // debugger;
      // if (!value) return;
      clearTimeout(timeId);
      timeId = setTimeout(async () => {
        loading.value = true;
        const params = Object.values(otherParams.value).length ? Object.assign(otherParams.value, {
          value,
        }) : value;
        // debugger;
        const optLists = await remoteSearchMethod(params) as unknown as any[];
        loading.value = false;
        if (!isInit) {
          list.value = optLists;
          return;
        }
        // 初始化时才入默认值
        const tempAry = optLists.filter((item) => {
          console.log(defaultOptionsValues.includes(String(item[optionsValueKey])), item[optionsValueKey]);
          return !defaultOptionsValues.includes(item[optionsValueKey]);
        });
        list.value = [...tempAry, ...defaultOptions.value];
      }, 500);
    } catch (error) {
      console.log(error, '-error');
      loading.value = false;
    }
  };
  const setDefaultOptios = (defaultOpts:any[]) => {
    defaultOptions.value = defaultOpts;
    const values:any[] = [];
    defaultOpts.forEach((item) => {
      const findListItem = (list.value as unknown as any[])?.find((listItem) => listItem[optionsValueKey] === item[optionsValueKey]);
      if (!findListItem)list.value?.push(item);
      values.push(item[optionsValueKey]);
    });
    defaultOptionsValues = values;
  };
  if (!disableAutoSearch) {
    onMounted(() => {
      console.log('调用了----');
      remoteSearch('', true);
    });
  }
  return {
    remoteSearch,
    loading,
    list: <Ref<T>>list,
    otherParams,
    setDefaultOptios,
  };
};

/**
 * @description: 图形验证码
 * @param {Function} beforeCb // 点击获取图形验证码前的回调
 * @param {Function} afterCb  // 点击获取图形验证码后的回调
 * @return {*}
 * @author: GUOCHAO82
 */
export const useImageCode = (beforeCb?:Function, afterCb?:Function) => {
  const imageCodeUrl = ref();
  const getImageInfo = async () => {
    if (beforeCb)beforeCb();
    const { url, key } = await getImageCode();
    imageCodeUrl.value = url;
    // formData.validateCodeKey = key;
    if (afterCb)afterCb(url, key);
  };
  return {
    imageCodeUrl,
    getImageInfo,
  };
};

/**
 * @description: 校验图形验证码
 * @param {Function} beforeCb // 点击获取图形验证码前的回调
 * @param {Function} afterCb  // 点击获取图形验证码后的回调
 * @return {*}
 * @author: GUOCHAO82
 */
export const checkImageCode = async (key:string, code:any) => {
  const data = await validateImgCode(key, code);
  return {
    data,
  };
};

/**
 * @description: 发送短信验证码
 * @param {*} baseCountdown // 短信验证码发送间隔时间
 * @param {*} startBtnText // 发送短信验证码btn展示的文字
 * @return {*}
 * @author: GUOCHAO82
 */
export const useSIMCode = (baseCountdown = 60, startBtnText = '点击获取') => {
  const isSendCode = ref(true); // 是否可以发送短信验证码
  const countdown = ref(0);
  const sendCode = () => {
    if (!isSendCode.value) return;
    // 发送验证码
    //
    isSendCode.value = false;
    countdown.value = baseCountdown;
    const timeId = setInterval(() => {
      if (countdown.value > 1) {
        countdown.value -= 1;
        return;
      }
      isSendCode.value = true;
      countdown.value = 0;
      clearInterval(timeId);
    }, 1000);
  };
  const btnText = computed(() => (countdown.value === 0 ? startBtnText : `${String(countdown.value).padStart(2, '0')}s`));
  return {
    isSendCode,
    sendCode,
    btnText,
  };
};

export const useCachePage = () => {
  const commonStore = useCommonStore();
  const addCachePage = (pageName:string) => {
    commonStore.addCachePageNames(pageName); // 增加缓存
  };
  const remoteCachePage = (pageName:string) => {
    commonStore.delCachePageNames(pageName);
  };
  return {
    addCachePage,
    remoteCachePage,
  };
};
