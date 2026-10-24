/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-28 14:21:07
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-02 15:43:06
 */
import { render, h } from 'vue';
import { Options, GlobalOptions } from './loading.type';
import { createdLoading } from './createLoading';

const defaultsOptions:Options = {
  fullscreen: true,
  // tip: '请求中....',
};
const globalOptions:GlobalOptions = {
  num: 0,
};
export const Loading = (options?:Options) => {
  options = {
    ...defaultsOptions,
    ...options,
  };
  if (typeof options.target === 'string') {
    options.target = document.querySelector(options.target) as HTMLElement;
  }
  const parent = options.target || document.body;
  const instance = createdLoading({
    options,
    globalOptions,
  });
  parent.appendChild(instance.$el);
  return instance;
};
