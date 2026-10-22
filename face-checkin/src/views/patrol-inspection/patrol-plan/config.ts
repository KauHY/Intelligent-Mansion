/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-06 16:41:08
 */

import { Ref } from 'vue';
import { PatrolPlan } from '~models/patrol-inspection';
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
  add: '新增巡检计划',
  edit: '编辑巡检计划',
  details: '巡检计划详情',
  link: '关联巡检人',
};

export const weeks = [
  '星期一',
  '星期二',
  '星期三',
  '星期四',
  '星期五',
  '星期六',
  '星期日',
];

export const getColumns = (options:Ref<DictionaryMap<['INSPECTION_TYPE', 'CYCLE_TYPE']>>) => [
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
    fixed: 'left',
  },
  {
    title: '计划ID',
    dataIndex: 'id',
    // slots: { customRender: 'title' },
  },
  {
    title: '计划名称',
    dataIndex: 'planName',
    // slots: { customRender: 'circuitName' },
  }, {
    title: '巡检类型',
    dataIndex: 'type',
    customRender: ({ record }:{record:PatrolPlan}) => (record.type || record.type === 0 ? options.value.INSPECTION_TYPE?.[record.type]?.label : ''),
  },
  {
    title: '巡检路线',
    dataIndex: 'circuitName',
  },
  {
    title: '执行周期',
    dataIndex: 'cycleType',
    customRender: ({ record }:{record:PatrolPlan}) => options.value.CYCLE_TYPE?.[record.cycleType]?.label,
  }, {
    title: '执行时间',
    // dataIndex: 'circuitName',
    customRender: ({ record }:{record:PatrolPlan}) => (`${record.executionTimeStart}-${record.executionTimeEnd}`),
  }, {
    title: '生效日期',
    // dataIndex: 'circuitName',
    customRender: ({ record }:{record:PatrolPlan}) => (`${record.effectDateStart}-${record.effectDateEnd}`),
  }, {
    title: '计划状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
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
    fixed: 'right',
    slots: { customRender: 'operate' },
  }];

export const getSelectColumns = (options:SelectOptionsMap, isOperate = true) => {
  // const columns = [
  //   {
  //     title: '顺序',
  //     customRender: ({ index }:{index:number}) => index + 1,
  //   },
  //   {
  //     title: '巡检点名称',
  //     dataIndex: 'pointName',
  //   }, {
  //     title: '巡检类型',
  //     dataIndex: 'type',
  //     customRender: ({ record }:{record:PatrolPlace}) => (record.type || record.type === 0 ? options[record.type]?.label : ''),
  //   }, {
  //     title: '巡检位置',
  //     dataIndex: 'location',
  //   }];
  // if (isOperate) {
  //   (columns as any).push({
  //     title: '操作',
  //     dataIndex: 'operate',
  //     width: '200px',
  //     slots: { customRender: 'operate' },
  //   });
  // }
  // return columns;
};
