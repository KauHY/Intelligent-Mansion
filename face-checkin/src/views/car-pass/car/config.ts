/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-31 14:47:02
 */

import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CarInfo } from '~models/car-pass';

export const getColumns = (options:Ref<DictionaryMap<['PARK_TYPE', 'CAR_STATUS']>>) => [
  {
    title: '车牌号码',
    dataIndex: 'carNum',
    slots: { customRender: 'carNum' },
  },
  {
    title: '车主姓名',
    dataIndex: 'carOwnerName',
  },
  {
    title: '手机号',
    dataIndex: 'mobile',
  },
  {
    title: '停车类型',
    // dataIndex: 'carTypeName',
    customRender: ({ record }:{record:CarInfo}) => (record.parkType || record.parkType === 0 ? options.value.PARK_TYPE?.[record.parkType]?.label : ''),
  },
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '关联企业',
    dataIndex: 'companyName',
  }, {
    title: '授权开始时间',
    dataIndex: 'authStartTime',
  }, {
    title: '授权结束时间',
    dataIndex: 'authEndTime',
  }, {
    title: '关联停车场',
    dataIndex: 'parkingLots',
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
  }, {
    title: '状态',
    // dataIndex: 'statusName',
    customRender: ({ record }:{record:CarInfo}) => (record.status || record.status === 0 ? options.value.CAR_STATUS?.[record.status]?.label : ''),

  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 180,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增车辆',
  edit: '编辑车辆',
  details: '车辆详情',
  del: '删除',
};
