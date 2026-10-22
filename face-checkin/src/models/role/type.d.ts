/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-12 14:38:48
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-12 17:34:28
 */
interface RoleListItem {
  id:string // 角色id
  orgName:string // 所属组织
  roleName:string // 角色名称
  roleDescription:string // 角色描述
  status:0|1
}

interface SearchParams extends BaseSearchParams{
    roleName:string
    status:0|1
}

interface AddParams { // 新增接口入参
    orgId: string, // 组织id
    roleDescription: string // 角色描述,
    roleName: string // 真实姓名,
    roleType: number // 角色类型,
    userIds?: Array<string>
    id?:string
}

export {
  RoleListItem,
  SearchParams,
  AddParams,
};
