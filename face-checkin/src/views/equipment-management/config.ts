/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-12 13:48:41
 */

import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [
  {
    title: '设备名称',
    dataIndex: 'deviceName',
    slots: { customRender: 'deviceName' },
  },
  {
    title: '设备类型',
    dataIndex: 'deviceType',
    slots: { customRender: 'deviceType' },
  },
  {
    title: '设备ID',
    dataIndex: 'deviceId',
  },
  {
    title: 'IP地址',
    dataIndex: 'ipAddress',
  },
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
  }, {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增设备',
  edit: '编辑设备',
  del: '删除',
  details: '设备详情',
};
