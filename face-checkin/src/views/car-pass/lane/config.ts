/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-30 18:37:12
 */

import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CompanyBindInfo } from '~models/visitors-management';

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [
  {
    title: '车道名称',
    dataIndex: 'laneName',
  },
  {
    title: '出入口',
    dataIndex: 'carDirectStr',
  },
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '关联停车场',
    dataIndex: 'parkingLot',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
  }, {
    title: '操作',
    dataIndex: 'operate',
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增车道',
  edit: '编辑车道',
  del: '删除',
};
