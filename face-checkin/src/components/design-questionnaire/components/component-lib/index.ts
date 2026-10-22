/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 18:16:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-26 09:57:20
 */
import { defineAsyncComponent, CreateAppFunction } from 'vue';

// 异步加载
export const components = {
  radio: defineAsyncComponent(() => import('./radio.vue')),
  input: defineAsyncComponent(() => import('./input.vue')),
  textarea: defineAsyncComponent(() => import('./textarea.vue')),
  upload: defineAsyncComponent(() => import('./upload.vue')),
  checkbox: defineAsyncComponent(() => import('./checkbox.vue')),
};
