/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-30 14:17:36
 */

import { Ref } from 'vue';
import { PatrolPlace } from '~models/patrol-inspection';
import { DictionaryMap } from '~composition/useHashMapDic';
// import useForm from '~composition/useForm';
import useForm from '~utils/useForm';

export const statusName = {
  0: '禁用',
  1: '启用',
};

export const orgTypeNames = {
  0: '机构',
  1: '园区',
};

export const operateDialogTitleName = {
  add: '新增巡检点',
  edit: '编辑巡检点',
  details: '巡检点详情',
};

export const getColumns = (options:Ref<DictionaryMap<['INSPECTION_TYPE']>>) => [
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '巡检点ID',
    dataIndex: 'id',
    // slots: { customRender: 'title' },
  },
  {
    title: '巡检点名称',
    dataIndex: 'pointName',
    slots: { customRender: 'pointName' },
  }, {
    title: '巡检点类型',
    dataIndex: 'type',
    customRender: ({ record }:{record:PatrolPlace}) => (record.type || record.type === 0 ? options.value.INSPECTION_TYPE?.[record.type]?.label : ''),
  }, {
    title: '巡检位置',
    dataIndex: 'location',
  }, {
    title: '更新日期',
    dataIndex: 'updateTime',
  }, {
    title: '备注',
    dataIndex: 'remark',
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 160,
    slots: { customRender: 'operate' },
  }];

export const houseColumns = [
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '所属楼栋单元',
    dataIndex: 'buildingName',
  },
  {
    title: '楼层房号',
    dataIndex: 'name',
  },
];
