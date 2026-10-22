/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 16:39:07
 * @LastEditTime: 2022-03-29 10:15:01
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { HouseStatusInfo } from '~models/contract-manage';

export const getHouseStatusColumns = (options:DictionaryMap<['HOUSE_STATUS', 'CONTRACT_STATE', 'CONTRACT_TYPE_FOR_HOUSE']>) => [
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
    title: '所在楼层',
    dataIndex: 'floor',
  },
  {
    title: '房屋状态',
    dataIndex: 'houseStatus',
    customRender: ({ record }:{record:HouseStatusInfo}) => options.HOUSE_STATUS?.[record.houseStatus as number]?.label,
  },
  {
    title: '合同编号',
    dataIndex: 'contractNo',
    slots: { customRender: 'contractNo' },
  },
  {
    title: '合同类型',
    dataIndex: 'type',
    customRender: ({ record }:{record:HouseStatusInfo}) => options.CONTRACT_TYPE_FOR_HOUSE?.[record.type as number]?.label,
  },
  {
    title: '合同状态',
    dataIndex: 'state',
    customRender: ({ record }:{record:HouseStatusInfo}) => options.CONTRACT_STATE?.[record.state as number]?.label,
  },
];

export const endTypeOpts = {
  0: {
    label: '合同30天（含）内到期',
    value: 1,
  },
};
