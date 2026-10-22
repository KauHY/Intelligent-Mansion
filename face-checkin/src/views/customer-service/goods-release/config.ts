/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-28 14:06:12
 */

// import {  } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { GoodsReleaseInfo } from '~models/customer-service';

export const getColumns = (options:DictionaryMap<['GOODS_RELEASE_STATUS']>) => [
  {
    title: '申请编号',
    dataIndex: 'goodsNo',
    slots: { customRender: 'goodsNo' },
  },
  {
    title: '申请人',
    dataIndex: 'submitterName',
  },
  {
    title: '手机号',
    dataIndex: 'submitterMobile',
  },
  {
    title: '所在园区',
    dataIndex: 'parkOrgName',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '所在公司',
    dataIndex: 'company',
  },
  {
    title: '放行时间',
    customRender: ({ record }:{record:GoodsReleaseInfo}) => (record.releaseDatetimeStart && record.releaseDatetimeEnd ? `${record.releaseDatetimeStart}-${record.releaseDatetimeEnd}` : '-'),

  },
  {
    title: '放行物品',
    customRender: ({ record }:{record:GoodsReleaseInfo}) => {
      const goods = record.goodsList?.map(({ name, count }) => `${name}*${count}`);
      return goods ? goods.join('，') : '-';
    },
  },
  {
    title: '申请时间',
    dataIndex: 'createTime',
  },
  {
    title: '状态',
    customRender: ({ record }:{record:GoodsReleaseInfo}) => (record.status ? options.GOODS_RELEASE_STATUS[record.status].label : '-'),

  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  audit: '放行审核',
  details: '物品放行详情',
};
