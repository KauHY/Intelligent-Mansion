/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-08 16:16:16
 */

import { Ref } from 'vue';
import { EntranceGuardTable } from '~models/entranceGuard-management';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';
// ['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE','ENTERPRISE_SIZE']
export const getColumns = (options:Ref<DictionaryMap<['ACCESS_DEVICE_TYPE', 'DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']>>) => [

  // export const columns = [
  {
    title: '设备名称',
    dataIndex: 'deviceName',
    slots: { customRender: 'deviceName' },
  }, {
    title: '设备类型',
    dataIndex: 'deviceType',
    slots: { customRender: 'deviceType' },
    customRender: ({ record }:{record:EntranceGuardTable}) => (record.deviceType || record.deviceType === 0 ? options.value.DEVICE_TYPE?.[record.deviceType]?.label : ''),
  }, {
    title: '设备ID',
    dataIndex: 'deviceId',
  }, {
    title: 'IP地址',
    dataIndex: 'ipAddress',
  }, {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
  }, {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
    customRender: ({ record }:{record:EntranceGuardTable}) => (record.status || record.status === 0 ? options.value.DEVICE_STATUS?.[record.status]?.label : ''),
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  perfect: '完善信息',
  del: '删除',

};
