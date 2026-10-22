/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-09 18:52:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-23 16:05:14
 */
 interface OrganizationItem{
    id:string
    orgName:string
    pId?:number
    orgType?:number
}

interface SearchParams extends BaseSearchParams{ // 组织分页查询入参
    orgName: string, // 组织名称
    parentId:string, // 父级组织id
    orgType?: 0,
    endTime?: string,
}

interface OrganizationListItem { // 分页查询出参
    createTime: string
    id: string
    orgName: string
    orgTypeName: string
    orgType:0|1, // 0 平台|1机构
    parentId: string
    parentOrgName: string
}

interface AddOrganization{
    description: string,
    id?: string,
    orgName: string,
    orgType: 0|1,
    parentId: string,
}

export {
  OrganizationItem,
  SearchParams,
  OrganizationListItem,
  AddOrganization,
};
