/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-12 10:51:37
 * @LastEditTime: 2022-01-26 17:47:34
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import { ContractList } from '~models/contract-manage';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getMeetingMapColumns = (options:DictionaryMap<[]>) => [
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '所属楼栋单元',
    dataIndex: 'buildingName',
  },
  {
    title: '所属楼层',
    dataIndex: 'floorId',
    customRender: ({ record }:{record:any}) => (`${record.floorId}F`),
  },
  {
    title: '会议室分布图',
    dataIndex: 'meetingPlaneFigure',
    slots: { customRender: 'meetingPlaneFigure' },
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 140,
    slots: { customRender: 'operate' },
  },
];
