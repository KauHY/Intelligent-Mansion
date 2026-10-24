/*
 * @Author: your name
 * @Date: 2022-04-13 11:21:03
 * @LastEditTime: 2022-06-06 12:28:20
 * @LastEditors: GUOCHAO82
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\models\company-group\type.d.ts
 */
interface searchParams extends BaseSearchParams{ // 园区分页查询
    businessId: string,
    categoryId: string,
    parkOrgId: string
  }

interface companyGroupTable {
    groupNames: string,
    companyName: string,
    companyType: number,
    industryField: number,
    enterpriseSize: number,
}

interface LaneInfo {
    id: number,
    companyId: string,
    groupNames: string,
    companyName: string,
    companyType: number,
    industryField: number,
    enterpriseSize: number,
}
interface TreeNodeItem {
  title: string;
  key: string;
  isDel: boolean;
  type: number | undefined;
  slots?: any;
  children?: TreeNodeItem[];
  [propsName: string]: any;
}
interface EquipmentTreeParams { // 车道分页查询
  categoryType: string, // 设备名称
  parkOrgId: string, // 设备类型
}

interface GroupLaneInfo{
  id: string,
  disabled: boolean,
      expand: boolean,
      label: string,
      leaf: boolean,
      parentId: string,
      parkOrgId: string,
      selectable: boolean,
      type: string,
}

interface GroupAddInfo{
  parkOrgId: string,
  businessIds: any,
      categoryId: string,
      remark: string,
}

export {
  searchParams,
  companyGroupTable,
  LaneInfo,
  GroupLaneInfo,
  TreeNodeItem,
  EquipmentTreeParams,
  GroupAddInfo,
};
