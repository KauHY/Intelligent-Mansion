/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 16:39:07
 * @LastEditTime: 2022-04-06 15:38:15
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import MeetingRentalVo, { MeetingRentalListKind, TimeArr } from '~models/meeting-rental';
import { DictionaryMap } from '~composition/useHashMapDic';

export const goodsTypeOpts = {
  1: {
    label: '购买型',
    value: 1,
  },
  2: {
    label: '租用型',
    value: 2,
  },
};
export const meetingRoomStatus = [
  {
    label: '已启用',
    value: 1,
  },
  {
    label: '已禁用',
    value: 0,
  },
];

export const enableStatus = [
  {
    label: '已禁用',
    value: 0,
  },
  {
    label: '已启用',
    value: 1,
  },
];

export const getMeetingRoomColumns = (
  options: DictionaryMap<['CUSTOMERINFO_TYPE', 'CUSTOMERINFO_STATUS']>,
) => [
  {
    title: '会议室名称',
    dataIndex: 'meetingName',
    slots: { customRender: 'meetingName' },
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '所属楼栋单元',
    dataIndex: 'buildingName',
  },
  {
    title: '楼层房号',
    dataIndex: 'houseName',
  },
  {
    title: '状态',
    dataIndex: 'status',
    customRender: ({ record }: { record: MeetingRentalListKind }) => enableStatus?.[record.status as number]?.label,
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 200,
    slots: { customRender: 'operate' },
  },
];

export const getMaterialColumns = (
  options: DictionaryMap<['CUSTOMERINFO_TYPE', 'CUSTOMERINFO_STATUS']>,
) => [
  {
    title: '物资名称',
    dataIndex: 'goodsName',
    slots: { customRender: 'goodsName' },
  },
  {
    title: '价格(元）',
    dataIndex: 'unitPrice',
  },
  {
    title: '计量单位',
    dataIndex: 'unit',
  },
  {
    title: '物资类型',
    dataIndex: 'goodsType',
    slots: { customRender: 'goodsType' },
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];

export const getChooseMaterialColumns = () => [
  {
    title: '物资名称',
    dataIndex: 'goodsName',
  },
  {
    title: '价格(元）',
    dataIndex: 'unitPrice',
  },
  {
    title: '计量单位',
    dataIndex: 'unit',
  },
  {
    title: '物资类型',
    dataIndex: 'goodsType',
    customRender: ({ record }: { record: any }) => (record.goodsType === 1 ? '购买型' : '租用型'),
  },
];

export const getOrderColumns = (
  options: DictionaryMap<['CUS_MEETING_AUDIT_STATUS']>,
) => [
  {
    title: '申请编号',
    dataIndex: 'reserveNo',
    slots: { customRender: 'reserveNo' },
  },
  {
    title: '会议室名称',
    dataIndex: 'meetingName',
  },
  {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '预定时间',
    dataIndex: 'reserveTimeStr',
  },
  {
    title: '提交订单金额(元)',
    dataIndex: 'estimatedAmount',
  },
  {
    title: '实际订单金额(元)',
    dataIndex: 'reviewAmount',
  },
  {
    title: '申请单位',
    dataIndex: 'applicantUnit',
  },
  {
    title: '联系人',
    dataIndex: 'applicantName',
  },
  {
    title: '手机号码',
    dataIndex: 'mobile',
  },
  {
    title: '提交时间',
    dataIndex: 'createTime',
  },
  {
    title: '状态',
    dataIndex: 'reviewStatus',
    customRender: ({ record }: { record: any }) => options.CUS_MEETING_AUDIT_STATUS?.[record.reviewStatus as number]?.label,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 100,
    slots: { customRender: 'operate' },
  },
];

export const timeTypeName = {
  1: '上午',
  2: '中午',
  3: '晚上',
};
export const reserveTime:TimeArr[] = [
  {
    timeType: 1,
    startTime: '08:00',
    endTime: '12:00',
  },
  {
    timeType: 2,
    startTime: '14:00',
    endTime: '18:00',
  },
  {
    timeType: 3,
    startTime: '18:00',
    endTime: '22:00',
  },
];
