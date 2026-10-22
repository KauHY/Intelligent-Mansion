/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-09 10:50:44
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 11:30:16
 */
// import type { LocationQuery, RouteParams } from 'vue-router';

import { LocationQuery, RouteParams } from 'vue-router';

declare module 'vue-router' {
    interface RouteMeta {
        staticMenu?: boolean | {
            parentName?: string
            icon?: string
        }
    }
}

// 使用import 后要使用下面这种方法声明全局类型，不然会使全局类型失效
declare global {
     interface MenuItem { // 菜单
        apiUrl:string
        parentId:string
        id:string // 当前路由id
        route?:string; // 路由路径
        routeName:string; // 路由名称
        sort?:number // 排序
        name:string|null // 菜单名称
        status:number // 启用禁用
        type: 0|1|2 // 菜单类型
        isMenu?: number,
        icon?:string // 当前菜单的图标
        headOrgId?:number // 组织的顶级组织的id,即租户
        children?:Array<MenuItem>
        [propName:string]:any
        query?: LocationQuery, // 查询参数 LocationQuery
        params?:RouteParams, //  查询参数
    }

    interface BaseSearchParams { // 分页查询入参
        // param:T
        pageSize:number
        pageNum:number
        [propName:string]:any
    }

    //  type SearchParams<T> = T extends BaseSearchParams;

    interface SearchResult<T> { // 分页查询出参
        total:number,
        list:Array<T>
    }

    interface DownloadReturnData{ // 下载接口返回数据格式
        data: BlobPart,
        contentDisposition: string,
        postData?:any,
    }

    type SelectOptions = Array<{
        label:string,
        value:any,
        type?:any
        extraInfo?:{[props:string]:any}
    }>

    type SelectOptionsMap = {
        [prop:number]: {
            label:string,
            value:string,
            type?:any
            extraInfo?:{[props:string]:any}
        }
    }

    let AMapUI:any;
    let compileConf:{
        publicPath:string
        projectName:string
        serverBaseUrl:string
    };
}
