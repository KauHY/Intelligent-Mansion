/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-08 15:15:23
 */

import { Ref } from 'vue';
import { PatrolTask } from '~models/patrol-inspection';
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
  details: '巡检任务详情',
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

export const getColumns = (options:Ref<DictionaryMap<['INSPECTION_TYPE', 'INSPECTION_TASK_STATUS']>>) => [
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '巡检任务ID',
    dataIndex: 'id',
    // slots: { customRender: 'title' },
  },
  {
    title: '巡检任务',
    dataIndex: 'taskName',
    slots: { customRender: 'taskName' },
  }, {
    title: '巡检类型',
    dataIndex: 'type',
    customRender: ({ record }:{record:PatrolTask}) => (record.type || record.type === 0 ? options.value.INSPECTION_TYPE?.[record.type]?.label : ''),
  }, {
    title: '巡检人',
    // dataIndex: 'employees',
    customRender: ({ record }:{record:any}) => {
      const { employees, actualEmployeeName, status } = record;
      return status === 1 ? employees?.join(';') : actualEmployeeName;
    },
  }, {
    title: '任务时间',
    dataIndex: 'taskTime',
  }, {
    title: '巡检完成时间',
    dataIndex: 'completionTime',
  },
  {
    title: '巡检状态',
    dataIndex: 'status',
    customRender: ({ record }:{record:PatrolTask}) => (record.status || record.status === 0 ? options.value.INSPECTION_TASK_STATUS?.[record.status]?.label : ''),
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 160,
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
