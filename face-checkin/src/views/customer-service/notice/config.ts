/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-21 16:53:06
 */

// import { onUnmounted, reactive } from 'vue';
// // import useForm from '~composition/useForm';
// import { REGS } from '@/constants/validate';
// import useForm from '~utils/useForm';
import { NoticeInfo } from '~models/customer-service';
import { DictionaryMap } from '~composition/useHashMapDic';

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
export const contentTypeOpts = {
  1: {
    label: '新闻资讯',
    value: 1,
  },
  2: {
    label: '优惠政策',
    value: 2,
  },
};

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

export const columns = (options:DictionaryMap<['PUT_INTO_TYPE', 'INFOMATION_TYPE']>) => [{
  title: '标题',
  dataIndex: 'title',
  fixed: 'left',
  slots: { customRender: 'title' },
}, {
  title: '类型',
  customRender: ({ record }:{record :NoticeInfo}) => (record.type ? contentTypeOpts[record.type].label : ''),
}, {
  title: '推送范围',
  // dataIndex: 'parkOrgName',
  customRender: ({ record }:{record :NoticeInfo}) => record.parkOrgNames.join(','),
}, {
  title: '投放小程序',
  customRender: ({ record }:{record :NoticeInfo}) => {
    let typeNames:string[] = [];
    if (Array.isArray(record.putIntoType)) {
      typeNames = record.putIntoType.map((key) => options.PUT_INTO_TYPE[key]?.label || '');
    }
    return typeNames.length ? typeNames.join(';') : '-';
  },
}, {
  title: '生效时间',
  width: 220,
  customRender: ({ record }:{record :NoticeInfo}) => {
    const { effectTimeStart } = record;
    return effectTimeStart ? `${effectTimeStart}` : '/';
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
  customRender: ({ record }:{record :NoticeInfo}) => (record.status !== undefined ? statusOpt[record.status].label : ''),
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 220,
  fixed: 'right',
  slots: { customRender: 'operate' },
}];

export const operateDialogTitleName = {
  add: '新增资讯',
  edit: '编辑资讯',
  detail: '资讯详情',
};
