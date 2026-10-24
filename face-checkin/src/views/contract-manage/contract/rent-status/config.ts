/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 16:39:07
 * @LastEditTime: 2022-04-12 10:31:07
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { HouseStatusInfo } from '~models/contract-manage';

export const getHouseStatusColumns = (options:DictionaryMap<['HOUSE_STATUS', 'CONTRACT_STATE', 'CONTRACT_TYPE']>) => [
  {
    title: '楼层房号',
    dataIndex: 'houseName',
    slots: { customRender: 'houseName' },
  },
  {
    title: '所属楼栋单元',
    dataIndex: 'buildingName',
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
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
    title: '合同编号',
    dataIndex: 'contractNo',
    slots: { customRender: 'contractNo' },
  },
  {
    title: '合同类型',
    dataIndex: 'type',
    customRender: ({ record }:{record:HouseStatusInfo}) => options.CONTRACT_TYPE?.[record.type as number]?.label,
  },
  {
    title: '合同有效期',
    dataIndex: 'contractNo',
    customRender: ({ record }:{record:HouseStatusInfo}) => `${record.startDate || ''}-${record.endDate || ''}`,
  },
  {
    title: '合同状态',
    dataIndex: 'state',
    customRender: ({ record }:{record:HouseStatusInfo}) => options.CONTRACT_STATE?.[record.state as number]?.label,
  },
];
