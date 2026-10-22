/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-15 10:41:41
 */

import { Ref } from 'vue';
import { HousingInfo } from '~models/wisdom-canvass-business';
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

export const operateDialogTitleName = {
  add: '新增组织',
  edit: '编辑组织',
};

export const getColumns = (options:Ref<DictionaryMap<['EFFECT_TYPE', 'BUSINESS_STATUS']>>) => [
  {
    title: '序号',
    // dataIndex: 'parkOrgName',
    customRender: ({ index }:{index:number}) => index + 1,
  },
  {
    title: '楼层房号',
    dataIndex: 'houseVO',
    slots: { customRender: 'houseVO' },
  },
  {
    title: '楼栋单元',
    dataIndex: ['houseVO', 'buildingName'],
  },
  {
    title: '关联园区',
    dataIndex: ['houseVO', 'parkOrgName'],
  },
  {
    title: '招商类型',
    // dataIndex: 'effectType',
    customRender: ({ record }:{record:HousingInfo}) => (record.effectType || record.effectType === 0 ? options.value.EFFECT_TYPE?.[record.effectType]?.label : ''),
  },
  {
    title: '单位租金（元/㎡/月）',
    dataIndex: 'unitRent',
  }, {
    title: '最早租期',
    dataIndex: 'earliestRentTime',
  },
  // {
  //   title: '截止时间',
  //   dataIndex: 'deadline',
  // },
  {
    title: '招商状态',
    dataIndex: 'businessStatus',
    customRender: ({ record }:{record:HousingInfo}) => (record.businessStatus || record.businessStatus === 0 ? options.value.BUSINESS_STATUS?.[record.businessStatus]?.label : ''),
  }, {
    title: '咨询顾问',
    dataIndex: 'employeeName',
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 180,
    fixed: 'right',
    slots: { customRender: 'operate' },
  }];

export const houseColumns = [
  // {
  //   title: '关联园区',
  //   dataIndex: 'parkOrgName',
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
