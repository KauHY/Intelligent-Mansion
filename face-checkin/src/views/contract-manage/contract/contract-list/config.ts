/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-12 10:51:37
 * @LastEditTime: 2022-04-24 15:34:46
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import { Ref } from 'vue';
import router from '@/routers';
import { ContractList, ContractType } from '~models/contract-manage';
import { DictionaryMap } from '~composition/useHashMapDic';

export const getColumns = (options:DictionaryMap<['CONTRACT_TYPE', 'CONTRACT_STATE', 'CONTRACT_ATTRIBUTES', 'PROTOCOL_ATTRIBUTES']>, contractType:ContractType) => {
  const fieldPrefix = getFieldPrefix(contractType);
  return [
    {
      title: `${fieldPrefix}编号`,
      dataIndex: 'no',
      slots: { customRender: 'no' },
    },
    {
      title: '所属园区',
      dataIndex: 'parkOrgName',
    },
    {
      title: '客户名称',
      dataIndex: 'nameB',
    },
    {
      title: `${fieldPrefix}属性`,
      dataIndex: 'attributes',
      customRender: ({ record }:{record: ContractList}) => (contractType === 1 ? options.PROTOCOL_ATTRIBUTES?.[record.attributes as number]?.label || '' : options.CONTRACT_ATTRIBUTES?.[record.attributes as number]?.label || ''),
    },
    {
      title: `${fieldPrefix}日期`,
      hidden: () => contractType === 2,
      customRender: ({ record }:{record: ContractList}) => `${record.startDate}至${record.endDate}`,
    },
    {
      title: `${fieldPrefix}状态`,
      dataIndex: 'state',
      hidden: () => contractType === 2,
      customRender: ({ record }:{record: ContractList}) => options.CONTRACT_STATE?.[record.state as number]?.label || '',
    },
    {
      title: '操作',
      dataIndex: 'operate',
      width: 300,
      slots: { customRender: 'operate' },
    },
  ];
};

export const getFieldPrefix = (contractType:ContractType) => {
  let fieldPrefix = '合同';
  if (contractType === 1) {
    fieldPrefix = '协议';
  }
  return fieldPrefix;
};

export const contractConfig = {
  1: {
    path: 'enterParkContract',
    name: '入园协议',
  },
  2: {
    path: 'attractContract',
    name: '招引合同',
  },
  3: {
    path: 'leaseContract',
    name: '租赁合同',
  },
  4: {
    path: 'propertyContract',
    name: '物业合同',
  },
  5: {
    path: 'apartmentContract',
    name: '公寓合同',
  },
};
