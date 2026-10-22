/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-21 16:53:54
 */

// import { onUnmounted, reactive } from 'vue';
// // import useForm from '~composition/useForm';
// import { REGS } from '@/constants/validate';
// import useForm from '~utils/useForm';
import { AnnouncementInfo } from '~models/customer-service';
import { DictionaryMap } from '~composition/useHashMapDic';

export const putIntoTypeOpts = {
  1: {
    label: 'C端',
    value: 1,
  },
  2: {
    label: '运营端',
    value: 2,
  },
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
    label: '待生效',
    value: 0,
  }, {
    label: '已生效',
    value: 1,
  }, {
    label: '已过期',
    value: 2,
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
// export const contentTypeOpts = {
//   1: {
//     label: '维修通知',
//     value: 1,
//   },
//   2: {
//     label: '停水停电',
//     value: 2,
//   },
//   3: {
//     label: '安全提醒',
//     value: 3,
//   },
//   4: {
//     label: '公益服务',
//     value: 4,
//   },
//   5: {
//     label: '温馨提示',
//     value: 5,
//   },
//   6: {
//     label: '应急广播',
//     value: 6,
//   },
//   7: {
//     label: '收费通知',
//     value: 7,
//   },
//   8: {
//     label: '其他',
//     value: 8,
//   },
// };

export const getColumns = (options:DictionaryMap<['PUT_INTO_TYPE', 'NOTICE_TYPE']>) => [{
  title: '标题',
  dataIndex: 'title',
  fixed: 'left',
}, {
  title: '类型',
  dataIndex: 'type',
  customRender: ({ record }:{record :AnnouncementInfo}) => (record.type ? options.NOTICE_TYPE[record.type]?.label : ''),
}, {
  title: '所属园区',
  dataIndex: 'parkOrgName',
  // customRender: ({ record }:{record :AnnouncementInfo}) => record.parkOrgNames.join(','),
},

{
  title: '投放小程序',
  dataIndex: 'putIntoType',
  customRender: ({ record }:{record :AnnouncementInfo}) => {
    let typeNames:string[] = [];
    if (Array.isArray(record.putIntoType)) {
      typeNames = record.putIntoType.map((key) => options.PUT_INTO_TYPE[key]?.label || '');
    }
    return typeNames.length ? typeNames.join(';') : '-';
  },

},
{
  title: '公告时间',
  width: 280,
  customRender: ({ record }:{record :AnnouncementInfo}) => {
    const { effectTimeStart, effectTimeEnd } = record;
    return effectTimeStart && effectTimeEnd ? `${effectTimeStart}至${effectTimeEnd}` : '/';
  },
}, {
  title: '发布人',
  dataIndex: 'createByName',
}, {
  title: '创建时间',
  dataIndex: 'createTime',
}, {
  title: '状态',
  // dataIndex: 'parkOrgName',
  customRender: ({ record }:{record :AnnouncementInfo}) => (record.status !== undefined ? statusOpt[record.status].label : ''),
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 220,
  fixed: 'right',
  slots: { customRender: 'operate' },
}];

export const operateDialogTitleName = {
  add: '新增公告',
  edit: '编辑公告',
  detail: '公告详情',
};
