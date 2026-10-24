/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-28 12:05:53
 */
// import { isOverTempNames } from '@/constants/config';
// import { PersonPassRecord } from '~models/person-pass';

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
export const columns = [
  {
    title: '姓名',
    dataIndex: 'userName',
  }, {
    title: '手机号',
    dataIndex: 'mobile',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '登记照片',
    dataIndex: 'faceImageUrl',
    ellipsis: false,
    slots: { customRender: 'faceImageUrl' },
    // customRender: ({ record }:{record:CarPassRecord}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '抓拍照片',
    dataIndex: 'sceneImageUrl',
    ellipsis: false,
    slots: { customRender: 'sceneImageUrl' },
  },
  {
    title: '健康码',
    dataIndex: 'healthCode',
    slots: { customRender: 'healthCode' },
  },
  {
    title: '体温(℃)',
    dataIndex: 'isOverTemp',
  },
  {
    title: '标签',
    dataIndex: 'labels',
  },
  {
    title: '识别时间',
    dataIndex: 'recognizeTime',
  },
  {
    title: '设备名称',
    dataIndex: 'deviceName',
  },
  {
    title: '设备关联园区',
    dataIndex: 'parkOrgName',
  },
  {
    title: '设备位置',
    dataIndex: 'deviceLocation',
  },
  {
    title: '识别结果',
    dataIndex: 'isPass',
  },
];

export const operateDialogTitleName = {
  add: '新增停车场',
  edit: '编辑停车场',
  del: '删除',
};
