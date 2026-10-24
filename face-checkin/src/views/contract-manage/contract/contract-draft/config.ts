/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:36:36
 * @LastEditTime: 2022-04-20 13:42:16
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import { create, all } from 'mathjs';
import { DictionaryMap } from '~composition/useHashMapDic';

export const companyTypeNames = {
  0: {
    label: '物业公司',
    value: 0,
  },
  1: {
    label: '普通企业',
    value: 1,
  },
};

export const getHouseColumns = (options:DictionaryMap<['HOUSE_FUNCTION']>) => [
  {
    title: '所属楼栋单元',
    dataIndex: 'buildingName',
    customRender: ({ record }: { record: any }) => `${record.buildingName}`,
  },
  {
    title: '楼层房号',
    dataIndex: 'name',
  },
  {
    title: '建筑面积（㎡）',
    dataIndex: 'buildArea',
  },
  {
    title: '套内面积（㎡）',
    dataIndex: 'coverArea',
  },
  {
    title: '房屋功能',
    dataIndex: 'houseFunction',
    customRender: ({ record }:{record: any}) => options.HOUSE_FUNCTION?.[record.houseFunction as number]?.label || '',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];

export const getContractColumns = (options:DictionaryMap<['HOUSE_FUNCTION']>) => [
  {
    title: '房源名称',
    dataIndex: 'buildingName',
    customRender: ({ record }: { record: any }) => `${record.buildingName}-${record.houseName || record.name}`,
  },
  {
    title: '建筑面积（㎡）',
    dataIndex: 'buildArea',
  },
  {
    title: '总楼层数',
    dataIndex: 'totalFloor',
  },
  {
    title: '所在楼层',
    dataIndex: 'floor',
  },
  {
    title: '房屋功能',
    dataIndex: 'houseFunction',
    customRender: ({ record }:{record: any}) => options.HOUSE_FUNCTION?.[record.houseFunction as number]?.label || '',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];

export const getBillSchemeColumns = (options:DictionaryMap<['FEE_CHARGING_CYCLE']>) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '方案名称',
    dataIndex: 'schemeName',
  },
  {
    title: '收费项',
    dataIndex: 'chargeItemDTO.chargeName',
    customRender: ({ record }: { record: any }) => (record.chargeItemDTO.chargeName || ''),
  },
  {
    title: '收费周期',
    customRender: ({ record }: { record: any }) => (record.chargingCycle || record.chargingCycle === 0 ? options.FEE_CHARGING_CYCLE?.[record.chargingCycle]?.label : ''),
  },
  {
    title: '更新日期',
    dataIndex: 'updateTime',
  },
];

const mathjs = create(all);
mathjs.config({
  number: 'BigNumber',
  precision: 64,
});
export const mathAdd = (a: any, b: any) => Number(mathjs.format(mathjs.add(mathjs.bignumber(a), mathjs.bignumber(b))));
export const mathSubtract = (a: any, b: any) => Number(mathjs.format(mathjs.subtract(mathjs.bignumber(a), mathjs.bignumber(b))));
export const mathMultiply = (a: any, b: any) => Number(mathjs.format(mathjs.multiply(mathjs.bignumber(a), mathjs.bignumber(b))));
export const mathDivide = (a: any, b: any) => Number(mathjs.format(mathjs.divide(mathjs.bignumber(a), mathjs.bignumber(b))));

// 1: 入园协议 2: 招引合同 3: 租赁合同管理 4: 物业合同管理 5: 公寓合同管理
// 入园协议
// 基础信息
// 协议编号 所属园区 协议属性 协议类型 协议名称 有效期 总面积 经办人 办理日期 备注 协议附件 11个字段
// 计费信息 无

// 招引合同
// 基础信息
// 合同编号 所属园区 合同属性 合同类型 合同名称 有效期 总面积 经办人 办理日期 备注 合同附件 11个字段
// 乙方信息 经营范围
// 增加 年纳税额（万元）

// 租赁合同管理
// 基础信息
// 合同编号 所属园区 合同属性 合同类型 合同名称 有效期 总面积 经办人 办理日期 备注 合同附件 装修日期 租期(年) 租金(元)

// 物业合同管理
// 基础信息
// 合同编号 所属园区 合同属性 合同类型 合同名称 有效期 总面积 经办人 办理日期 备注 合同附件 租期(年) 物业费(元)

// 公寓合同管理
// 基础信息
// 合同编号 所属园区 合同属性 合同类型 合同名称 有效期  经办人 办理日期 备注 合同附件 租期(天) 租金(元) 客房数(间) 押金(元)
// 房屋信息
// 增加 单间(间) 标间(间) 套房(间)

// 1、租赁合同
//   收费项：1、房屋租金 2、履约保证金
//   计算标准：1、建筑面积*月单价 2、套内面积*月单价
// 2、物业合同
//   收费项：1、物业费
//   计算标准：1、建筑面积*月单价
// 3、公寓合同
//   收费项：1、公寓租金-单间 2、公寓租金-标间 3、公寓租金-套间  4、公寓押金-单间 5、公寓押金-标间 6、公寓押金-套间
//   计算标准：1、天单价 2、房间数*单价
