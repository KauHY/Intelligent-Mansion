/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-06 11:15:53
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
  add: '新增巡检路线',
  edit: '编辑巡检路线',
  details: '巡检路线详情',
  link: '关联巡检点',
};

export const getColumns = (options:Ref<DictionaryMap<['INSPECTION_TYPE']>>) => [
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '路线ID',
    dataIndex: 'id',
    // slots: { customRender: 'title' },
  },
  {
    title: '路线名称',
    dataIndex: 'circuitName',
    slots: { customRender: 'circuitName' },
  }, {
    title: '巡检类型',
    dataIndex: 'type',
    customRender: ({ record }:{record:PatrolPlace}) => (record.type || record.type === 0 ? options.value.INSPECTION_TYPE?.[record.type]?.label : ''),
  }, {
    title: '更新日期',
    dataIndex: 'updateTime',
  }, {
    title: '备注',
    dataIndex: 'remark',
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 240,
    slots: { customRender: 'operate' },
  }];

export const getSelectColumns = (options:SelectOptionsMap, isOperate = true) => {
  const columns = [
    {
      title: '顺序',
      customRender: ({ index }:{index:number}) => index + 1,
    },
    {
      title: '巡检点名称',
      dataIndex: 'pointName',
    }, {
      title: '巡检类型',
      dataIndex: 'type',
      customRender: ({ record }:{record:PatrolPlace}) => (record.type || record.type === 0 ? options[record.type]?.label : ''),
    }, {
      title: '巡检位置',
      dataIndex: 'location',
    }];
  if (isOperate) {
    (columns as any).push({
      title: '操作',
      dataIndex: 'operate',
      width: 200,
      slots: { customRender: 'operate' },
    });
  }
  return columns;
};
