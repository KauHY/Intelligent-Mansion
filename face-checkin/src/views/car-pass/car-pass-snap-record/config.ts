/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-05 15:59:23
 */
import { Ref } from 'vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { CarPassRecord } from '~models/car-pass';

export const carRegisteredOpts = {
  1: {
    label: '是',
    value: 1,
  },
  0: {
    label: '否',
    value: 0,
  },
};
// export const getColumns = (options:Ref<DictionaryMap<['CAR_TYPE']>>) => [
export const getColumns = (options:Ref<DictionaryMap<['PARK_TYPE', 'ACCESS_RESULT', 'GATEWAY_TYPE']>>) => [
  {
    title: '抓拍照片',
    dataIndex: 'imgUrl',
    width: 120,
    slots: { customRender: 'imgUrl' },
  },
  // {
  //   title: '抓拍照片（出）',
  //   dataIndex: 'exitImgUrl',
  //   width: 120,
  //   slots: { customRender: 'exitImgUrl' },
  // },
  {
    title: '车牌号',
    dataIndex: 'carNum',
  }, {
    title: '停车类型',
    dataIndex: 'parkType',
    customRender: ({ record }:{record:any}) => (record.parkType || record.parkType === 0 ? options.value.PARK_TYPE?.[record.parkType]?.label : ''),
  },
  // {
  //   title: '是否在册车辆',
  //   dataIndex: 'carRegisteredLabel',
  //   // customRender: ({ record }:{record:CarPassRecord}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  // },
  {
    title: '识别时间',
    dataIndex: 'recognizeTime',
  },
  // {
  //   title: '设备名称',
  //   dataIndex: 'deviceName',
  // },
  {
    title: '所在园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '停车场',
    dataIndex: 'parkingLot',
  },
  {
    title: '车道',
    dataIndex: 'carLaneName',
  },
  {
    title: '出入口',
    dataIndex: 'gatewayTypeName',
  },
  {
    title: '识别结果',
    dataIndex: 'accessResultName',
  },
];

export const operateDialogTitleName = {
  add: '新增停车场',
  edit: '编辑停车场',
  del: '删除',
};
