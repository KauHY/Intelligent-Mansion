/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 18:41:10
 */

import { OrderInfo } from '@/models/customer-service';

export const orderTypeComplaint = { // 报事投诉
  11: {
    label: '环境',
    value: 11,
  },
  12: {
    label: '安全',
    value: 12,
  },
  13: {
    label: '秩序',
    value: 13,
  },
  14: {
    label: '工作人员',
    value: 14,
  },
  15: {
    label: '设备设施',
    value: 15,
  },
  16: {
    label: '供水',
    value: 16,
  },
  17: {
    label: '供电',
    value: 17,
  },
  18: {
    label: '消防',
    value: 18,
  },
  19: {
    label: '便民服务',
    value: 19,
  },
  20: {
    label: '其他',
    value: '20',
  },
  // 报修服务类型

};

export const orderTypeRepair = { // 报事报修
  21: { label: '水电', value: 21 },
  22: { label: '门禁', value: 22 },
  23: { label: '电梯', value: 23 },
  24: { label: '公共设施', value: 24 },
  25: { label: '物业设备', value: 25 },
  26: { label: '其他', value: 26 },
};

// const orderType = {
//   1:
// }

export const orderStateOpts = { // 订单状态
  1: { label: '待派单', value: 1 },
  2: { label: '待处理 ', value: 2 },
  8: { label: '已完成', value: 8 },
  9: { label: '已撤单', value: 9 },
  10: { label: '转办中', value: 10 },

};

export const cancelOrderReason = { // 订单状态
  1: { label: '问题已自行解决', value: 1 },
  2: { label: '误会了，没有这个问题', value: 2 },
  3: { label: '已有人现场处理', value: 3 },
  4: { label: '其他原因', value: 4 },

};
export const menuTypeName = {
  0: '菜单',
  1: '按钮',
};

export const statusName = {
  0: '禁用',
  1: '启用',
};

export const statusOpt = [
  {
    label: '禁用',
    value: '0',
  }, {
    label: '启用',
    value: '1',
  },
];

export const companyTypeNames = {
  0: {
    label: '物业公司',
    value: 0,
  },
  1: {
    label: '普通企业',
    value: '1',
  },
};

export const columns = [{
  title: '工单编号',
  dataIndex: 'orderNo',
  fixed: 'left',
}, {
  title: '内容',
  dataIndex: 'orderContent',
  width: 200,
  // ellipsis: true,
  slots: { customRender: 'orderContent' },
}, {
  title: '反馈人',
  dataIndex: 'orderOwnerName',
}, {
  title: '手机号',
  dataIndex: 'orderOwnerMobile',
}, {
  title: '所在园区',
  dataIndex: 'parkOrgName',
}, {
  title: '详细地址',
  dataIndex: 'orderAddress',
}, {
  title: '类型',
  // dataIndex: 'orderType',
  customRender: ({ record }:{record:OrderInfo}) => {
    const { orderType, orderTypeItem } = record;
    const orderTypeItemOpt = (orderType === 1 ? orderTypeComplaint : orderTypeRepair) as any;
    return orderTypeItemOpt[orderTypeItem as number]?.label;
  },
}, {
  title: '发布人',
  dataIndex: 'createByName',
}, {
  title: '创建时间',
  dataIndex: 'createTime',
}, {
  title: '状态',
  customRender: ({ record }:{record:OrderInfo}) => orderStateOpts[record.orderState].label,
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 220,
  fixed: 'right',
  slots: { customRender: 'operate' },
}];

export const operateDialogTitleName = {
  add: '新增反馈',
  sendOrder: '派单',
  cancel: '撤单',
  details: '订单详情',
};
