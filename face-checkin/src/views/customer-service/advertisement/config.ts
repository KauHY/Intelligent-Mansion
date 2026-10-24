/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 16:39:07
 * @LastEditTime: 2022-03-01 14:24:32
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import { AdvertListKind } from '~models/advertisement';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getAdvertColumns = (options:DictionaryMap<['ADVERTISEMENT_STATUS', 'ADVERTISEMENT_POSITION', 'ADVERTISEMENT_JUMP_TYPE']>) => [
  {
    title: '广告名称',
    dataIndex: 'name',
    slots: { customRender: 'advertName' },
  },
  {
    title: '推送范围',
    dataIndex: 'parkOrgNames',
    customRender: ({ record }:{record:AdvertListKind}) => record.parkOrgNames.map((item:any) => item.label).join(','),
  },
  {
    title: '排序',
    dataIndex: 'sort',
  },
  {
    title: '位置',
    dataIndex: 'position',
    customRender: ({ record }:{record:AdvertListKind}) => options.ADVERTISEMENT_POSITION?.[record.position as number]?.label,
  },
  {
    title: '跳转类型',
    dataIndex: 'jumpType',
    customRender: ({ record }:{record:AdvertListKind}) => options.ADVERTISEMENT_JUMP_TYPE?.[record.jumpType as number]?.label,
  },
  {
    title: '生效开始时间',
    dataIndex: 'effectTime',
    customRender: ({ record }:{record:AdvertListKind}) => `${record.effectTimeStart}-${record.effectTimeEnd}`,
  },
  {
    title: '发布人',
    dataIndex: 'createByName',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '状态',
    dataIndex: 'status',
    customRender: ({ record }:{record:AdvertListKind}) => options.ADVERTISEMENT_STATUS?.[record.status as number]?.label,
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 300,
    slots: { customRender: 'operate' },
  },
];
