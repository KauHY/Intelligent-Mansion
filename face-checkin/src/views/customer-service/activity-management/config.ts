/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 17:39:17
 */

import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { ActivityInfo } from '~models/activity-management';

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [
  {
    title: '活动名称',
    dataIndex: 'name',
    fixed: 'left',
    slots: { customRender: 'name' },
  },
  {
    title: '推送范围',
    customRender: ({ record }:{record:ActivityInfo}) => record.parkOrgNames?.join(',') || '',
    // dataIndex: 'deviceType',
    // customRender: ({ record }:{record:ActivityInfo}) => {
    //   const texts = record.parkOrgNames?.map((opt) => opt.label);
    //   return texts && texts.length ? texts.join(',') : '';
    // },
  },
  {
    title: '生效时间',
    dataIndex: 'effectTime',
  },
  {
    title: '活动时间',
    customRender: ({ record }:{record:ActivityInfo}) => (record.activityTimeStart && record.activityTimeEnd ? `${record.activityTimeStart}-${record.activityTimeEnd}` : ''),
  },
  {
    title: '活动地点',
    dataIndex: 'place',
  }, {
    title: '是否需要报名',
    customRender: ({ record }:{record:ActivityInfo}) => (record.confRegister ? '是' : '否'),

  }, {
    title: '已报名人数',
    dataIndex: 'registeredCount',
  }, {
    title: '发布人',
    dataIndex: 'createByName',
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
    width: 340,
    fixed: 'right',
    slots: { customRender: 'operate' },
  }];

export const signUpRecordColumns = [
  {
    title: '姓名',
    dataIndex: 'name',
  },
  {
    title: '手机号',
    dataIndex: 'mobile',
  },
  {
    title: '报名时间',
    dataIndex: 'createTime',
  },
];

export const operateDialogTitleName = {
  add: '新增活动',
  edit: '编辑活动',
  del: '删除',
  details: '活动详情',
};
