/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-08 10:55:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-27 18:29:45
 */
interface Column {
    dataIndex:string,
    title:string,
    align:'left' | 'right' | 'center'
    ellipsis:Boolean
    slots?:Object,
    key?:string,
    width?:string|number
    [key:string]:any
}

export {
  Column,
};
