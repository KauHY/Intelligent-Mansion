/*
 * @Description: 公共类
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-11 11:18:51
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-09 10:56:01
 */
// export interface SearchParams<T> {
//   // param:T
//   pageSize:number
//   pageNum:number
//   [propName:string]:any
// }

// export interface SearchResult<T> {
//   total:number,
//   list:Array<T>
// }

export default abstract class SearchModel {
  pageInfo = {
    pageSize: 0,
    pageNum: 1,
  }

  // abstract searchListPage<T>(searchData:T, init:Boolean): Promise<SearchResult>; // 必须在派生类中实现

  // changePage(type:'pageSize'|'pageNum'|null, num:number|null, init?:boolean) {
  //   if (init) {
  //     this.resetPageInfo(); // 初始化页面清0
  //     return;
  //   }
  //   this.pageInfo[type as 'pageSize'|'pageNum'] = <number>num;
  // }

  // private resetPageInfo() {
  //   this.pageInfo.pageSize = 0;
  //   this.pageInfo.pageNum = 1;
  // }
}
