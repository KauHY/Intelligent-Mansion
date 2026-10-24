/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 13:58:57
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 11:30:32
 */
interface SearchParams extends BaseSearchParams {
    headOrgId:string
}
interface MenuItem {
  routeName: string,
  parentId: string,
  id: string,
  name: string,
  isMenu?: number,
  [key:string]:any
}

interface RsMenuTree {
  id:string
  name:string
  parentId?:string
  children?:Array<RsMenuTree>
}

export {
  SearchParams,
  RsMenuTree,
  MenuItem,
};
