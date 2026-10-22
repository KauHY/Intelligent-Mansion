/*
 * @Description: 巡检管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-29 13:46:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-07 16:11:32
 */

interface GoodsCard {
  // 巡检点
  id: string;
  parkOrgId: string;
  parkOrgName: string;
  remark: string; // 备注
  goodsType: number | null; // 物品类别
  goodsName: string; // 物品名称
  unit: string; // 物品名称
  brand: string; // 物品名称
  model: string; // 型号
  unitPrice: number | null; // 入库单价(元)
  minStock?: number;
  goodsId?: string | null;
  stockNum?: number;
  stockMode?: number;
}

interface SearchManagerParams extends BaseSearchParams {
  type: string; // 巡检点类型
  pointName: string; // 巡检点名称
  orgId: string; // 组织id
}

interface PatrolDetails {
  state: string; // 情况
  description: string; // 情况说明
  id: string;
  picture: string; // 巡检图片
  pointName: string; // 巡检点名称
  status: boolean; // 巡检点巡检状态
  taskId: string; // 任务id
  updateTime: string; // 更新时间
}

interface SetWarning {
  minStock: number | null;
  id: string;
}

interface GoodsInStock {
  remark?: string;
  id: string;
  parkOrgId?: string;
  parkOrgName?: string;
  goodsName?: string; // 物品名称
  stockInUnitPrice?: number | null; // 入库单价(元)
  stockMode?: number | null;
  stockInNum?: number | null;
  goodsId?: string;
  stockNum?: number | null;
  averagePrice?: number | null;
  goodsNo?: string;
  goodsType?: number | null; // 物品类别
  unit?: string;
  unitPrice?: number | null
}
interface HouseInfo {
  buildingName: string; // 楼栋名称
  parkOrgName?: string; // 园区名称
  id: string;
  buildingId: string; // 楼栋id
  name: string; // 楼层房号
  code: string; // 房屋编码
  floor: number | null; // 所在楼层
  coverArea: number | null; // 套内面积
  buildArea: number | null; // 建筑面积
  parkOrgId: string;
  rent: string; // 租赁状态：false：未租 true：已租
  floorType?: number | null; // 楼层属性
  houseStatus?: number | null; // 房屋状态
  houseProperty?: number | null; // 房屋属性
  houseFunction?: number | null; // 房屋功能
  orientation?: number | null; // 朝向
}

interface SelectHouseInfo {
  [props: string]: {
    buildArea?: number | null;
    houseId: string;
    name: string;
  };
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
interface SearchParams extends BaseSearchParams {
  // 园区分页查询
  buildingId?: string; // 园区名称
  houseId?: string; // 组织id
  parkOrgId?: string;
  notHouseProperty?: string;
}

//  1.入库、2.出库、3.修改
enum StockOperation {
  IN_STOCK = 1,
  OUT_STOCK = 2,
  EDIT_STOCK = 3,
}
enum NeedReturnType {
  '否',
  '是',
}

interface ReceiveItem {
  applyNumber?: string;
  applyTime?: string;
  auditState?: number | null;
  employeeId?: string;
  employeeName?: string;
  goodsName?: string;
  id?: string;
  needReturn?: number | null;
  parkOrgId?: string;
  parkOrgName?: string;
  receiveNum?: string;
  returnNumTotal?: number | null;
  stockInTime?: string;
  stockOutTime?: string;
  stockState?: number | null;
}
interface ReceiveDetails extends ReceiveItem{
  auditMsg?: string
  auditRemark?: string
  auditState?: number | null
  auditTime?: string
  employeeId?: string
  employeeName?: string
  needReturn?: number | null
  stockNo?: string
  mobile?: string
  remark?: string
}

interface ReceiveAuditSetting {
  companyIds: []
  companyId?: string
  employeeIds: string[]
  employeeId?: string
  parkOrgId: string
  id:string
  parkOrgName: string;
}
export {
  PatrolDetails,
  SetWarning,
  GoodsInStock,
  HouseInfo,
  SelectHouseInfo,
  TreeNodeItem,
  SearchParams,
  StockOperation,
  SearchManagerParams,
  GoodsCard,
  NeedReturnType,
  ReceiveItem,
  ReceiveDetails,
  ReceiveAuditSetting,
};
