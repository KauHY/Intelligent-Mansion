/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-07 17:55:07
 */

// export const getColumns = (options:Ref<DictionaryMap<['VISIT_REASON']>>) =>

export const columns = [
  {
    title: '停车场编号',
    dataIndex: 'parkingLotCode',
  },
  {
    title: '停车场名称',
    dataIndex: 'parkingLot',
  },
  {
    title: '关联园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '车位数量（个）',
    dataIndex: 'totalLot',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  }, {
    title: '修改时间',
    dataIndex: 'updateTime',
    // customRender: ({ record }:{record:CompanyBindInfo}) => (record.visitorClientTypeLabel || record.visitReason === 0 ? options.value.VISIT_REASON?.[record.visitReason]?.label : ''),
  },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 220,
    slots: { customRender: 'operate' },
  }];

export const operateDialogTitleName = {
  add: '新增停车场',
  edit: '编辑停车场',
  del: '删除',
};
