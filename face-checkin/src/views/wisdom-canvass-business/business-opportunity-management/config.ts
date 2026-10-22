/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-05 15:20:21
 */

import { Ref } from 'vue';
import { BusinessInfo, FollowRecord } from '~models/wisdom-canvass-business';
import { DictionaryMap } from '~composition/useHashMapDic';
// import useForm from '~composition/useForm';

export const statusName = {
  0: '禁用',
  1: '启用',
};

export const orgTypeNames = {
  0: '机构',
  1: '园区',
};

export const remindTypeOpts = [
  {
    label: '天',
    value: 1,
  }, {
    label: '周',
    value: 2,
  },
];

export const remindByWeekOpts = [
  {
    label: '全部',
    value: 0,
  }, {
    label: '星期一',
    value: 1,
  },
  {
    label: '星期二',
    value: 2,
  },
  {
    label: '星期三',
    value: 3,
  },
  {
    label: '星期四',
    value: 4,
  },
  {
    label: '星期五',
    value: 5,
  },
  {
    label: '星期六',
    value: 6,
  },
  {
    label: '星期日',
    value: 7,
  },
];

export const getColumns = (options:Ref<DictionaryMap<['BUSINESS_CHANCE_STAGE', 'BUSINESS_CHANCE_SOURCE']>>, type:number) => [
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '商机名称',
    slots: { customRender: 'chanceName' },
    dataIndex: 'chanceName',
  },
  {
    title: '商机阶段',
    // dataIndex: 'effectType',
    customRender: ({ record }:{record:BusinessInfo}) => (record.chanceStage || record.chanceStage === 0 ? options.value.BUSINESS_CHANCE_STAGE?.[record.chanceStage]?.label : ''),
  },
  {
    title: '跟进人',
    dataIndex: 'followEmployeeName',
  },
  {
    title: '商机来源',
    // dataIndex: 'effectType',
    customRender: ({ record }:{record:BusinessInfo}) => (record.businessSource || record.businessSource === 0 ? options.value.BUSINESS_CHANCE_SOURCE?.[record.businessSource]?.label : ''),
  },
  {
    title: '最近跟进时间',
    dataIndex: 'latestFollowTime',
  },
  {
    title: '最近更新时间',
    dataIndex: 'updateTime',
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: type === 2 ? 370 : 230,
    fixed: 'right',
    slots: { customRender: 'operate' },
  }];

export const houseColumns = [
  // {
  //   title: '关联园区',
  //   dataIndex: 'parkName',
  // },
  {
    title: '所属楼栋单元',
    dataIndex: 'buildingName',
  },
  {
    title: '楼层房号',
    dataIndex: 'name',
  },
];

export const getFollowRecordColumns = (options:Ref<DictionaryMap<['BUSINESS_FOLLOW_TYPE', 'BUSINESS_CHANCE_STAGE']>>) => [
  {
    title: '序号',
    customRender: ({ index }:{index:number}) => index + 1,
  },
  {
    title: '商机阶段',
    dataIndex: 'chanceStage',
    customRender: ({ record }:{record:FollowRecord}) => options.value.BUSINESS_CHANCE_STAGE[record.chanceStage]?.label,
  },
  {
    title: '跟进人',
    dataIndex: 'followEmployeeName',
  },

  {
    title: '跟进类型',
    dataIndex: 'followType',
    customRender: ({ record }:{record:FollowRecord}) => options.value.BUSINESS_FOLLOW_TYPE[record.followType]?.label,
  },
  {
    title: '跟进内容',
    dataIndex: 'followContent',
  },
  {
    title: '跟进时间',
    dataIndex: 'followTime',
  },

];

export const operateBtnConf = {
  0: [
    {
      btnText: '分配',
      operateType: 'other',
      title: '分配商机',
      typeValue: 2,
    },
    {
      btnText: '删除',
      operateType: 'del',
    },
    {
      btnText: '编辑',
      operateType: 'edit',
      title: '编辑商机',
      typeValue: 3,
    },
  ],
  1: [
    {
      btnText: '分配',
      operateType: 'other',
      title: '分配商机',
      typeValue: 2,
    },
    {
      btnText: '领取',
      operateType: 'receive',
      title: '领取商机',
      typeValue: 3,
    },
    // {
    //   btnText: '删除',
    //   operateType: 'del',
    // },
  ],
  2: [
    {
      btnText: '跟进商机',
      title: '跟进商机',
      operateType: 'follow',
    },
    // {
    //   btnText: '提醒',
    //   title: '商机提醒',
    //   operateType: 'remind',
    // },
    {
      btnText: '转成客户',
      title: '转成客户',
      operateType: 'convertToCustomer',
    },
    {
      btnText: '转移商机',
      title: '转移商机',
      operateType: 'other',
      typeValue: 7,
    },
    {
      btnText: '释放商机',
      operateType: 'release',
      title: '释放商机',
      typeValue: 8,
    },
    {
      btnText: '删除商机',
      operateType: 'del',
    },
  ],
};

// export const operateDialogTitleName = {
//   add: '新增商机',
//   details: '商机详情',
//   del: '删除商机',
//   other: '',
// };
