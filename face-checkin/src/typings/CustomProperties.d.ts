/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-06 17:23:59
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-07 12:39:40
 */

import { ComponentCustomProperties } from 'vue';
// import { AxiosInstance } from "axios"
declare module '@vue/runtime-core' {
    export interface ComponentCustomProperties {
        $sort: (data: any[]| object, sortKey?:string) => any[]| object
        $validate: (data: object, rule: object) => boolean
    }
  }
