/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-31 17:04:34
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
export const getColumns = (options:Ref<DictionaryMap<['PARK_TYPE']>>) => [
  {
    title: '抓拍照片（进）',
    dataIndex: 'enterImgUrl',
    width: 120,
    slots: { customRender: 'enterImgUrl' },
  },
  {
    title: '抓拍照片（出）',
    dataIndex: 'exitImgUrl',
    width: 120,
    slots: { customRender: 'exitImgUrl' },
  },
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
    title: '驶入车道',
    dataIndex: 'enterCarLaneName',
    // customRender: ({ record }:{record:CarPassRecord}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '驶入时间',
    dataIndex: 'enterRecognizeTime',
  },
  {
    title: '驶离车道',
    dataIndex: 'exitCarLaneName',
  },
  {
    title: '驶离时间',
    dataIndex: 'exitRecognizeTime',
  },
  {
    title: '所在园区',
    dataIndex: 'parkOrgName',
  },
];

export const operateDialogTitleName = {
  add: '新增停车场',
  edit: '编辑停车场',
  del: '删除',
};
