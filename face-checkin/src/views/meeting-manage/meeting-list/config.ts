/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-12 10:51:37
 * @LastEditTime: 2022-01-26 17:48:21
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import { ContractList } from '~models/contract-manage';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getMeetingListColumns = (options:DictionaryMap<['YES_OR_NO', 'SCREEN_DEVICE_STATUS', 'MEETING_USED_STATE']>) => [
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
    dataIndex: 'houseName',
  },
  {
    title: '会议室名称',
    dataIndex: 'meetingName',
  },
  {
    title: '开放时间',
    dataIndex: 'openTimeStart',
    slots: { customRender: 'openTimeStart' },
  },
  {
    title: '容纳人数',
    dataIndex: 'capacity',
  },
  {
    title: '预定是否需审核',
    dataIndex: 'needReview',
    customRender: ({ record }:{record:any}) => (options.YES_OR_NO?.[record.needReview]?.label),
  },
  {
    title: '会议室状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
  },
  {
    title: '信息屏状态',
    dataIndex: 'screenStatus',
    customRender: ({ record }:{record:any}) => (options.SCREEN_DEVICE_STATUS?.[record.screenStatus]?.label),
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 330,
    slots: { customRender: 'operate' },
  },
];

// 权限类型 1公司 2员工
export const relationTypeOpts = [
  { label: '选公司', value: 1 },
  { label: '选员工', value: 2 },
];
