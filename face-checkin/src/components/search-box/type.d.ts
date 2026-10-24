/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 16:16:21
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-16 14:27:13
 */
import { Ref } from 'vue';

type Options = Array<any>|Ref<Array<any>>

type OptionsFun = ()=>Options

export interface SearchConfItem{
    value: string|Array<any>|number|boolean,
    tag: 'input'|'select'|'date-range'|'month-picker'|'date',
    name?:string,
    labelKey?:string,
    valueKey?:string
    endTimeKey?:string
    startTimeKey?:string
    placeholder?:string|string[]
    valueFormat?:string
    handleChange?:Function
    hidden?:boolean | (()=>boolean) // 隐藏字段
    width?:string,
    allowClear?:boolean
    options?:Array<any>|Ref<Array<any>>|OptionsFun|Object
    showSearch?:boolean,
    filterOption?:((input:string, option:any)=>boolean)|boolean
}

type KEYS<T> = T extends Array<infer R>? R: string

export type SearchConf<T extends string[]> = {
    [k in KEYS<T>] : SearchConfItem
}

declare global {
   interface FormFieldConf extends SearchConfItem {
      fieldKey: string, // 字段key，用于嵌套
      formLabel: string,
      formItemName?:string|string[]
      disabled?:boolean,
      onChange?: (...args: any[]) => any;
   }
}
