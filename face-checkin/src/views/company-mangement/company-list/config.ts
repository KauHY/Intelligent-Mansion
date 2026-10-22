/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 17:29:43
 */

import { onUnmounted, reactive, ref } from 'vue';
// import useForm from '~composition/useForm';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { CompanyInfo, BindHouseInfo } from '~models/company-mangement';

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
export const statusName = {
  0: '已禁用',
  1: '已启用',
};
export const statusOpt = [
  {
    label: '已禁用',
    value: '0',
  }, {
    label: '已启用',
    value: '1',
  },
];
// 入住企业table columns
export const columns = [
  {
    title: '企业ID',
    dataIndex: 'id',
  }, {
    title: '企业名称',
    dataIndex: 'name',
    slots: { customRender: 'name' },
  }, {
    title: '企业类型',
    dataIndex: 'companyType',
    slots: { customRender: 'companyType' },
  }, {
    title: '所属园区',
    dataIndex: 'parkOrgName',
  }, {
    title: '注册地址',
    dataIndex: 'address',
  }, {
    title: '统一社会信用代码',
    dataIndex: 'code',
  },
  {
    title: '来源',
    dataIndex: 'source',
    slots: { customRender: 'source' },
  }, {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '状态',
    // dataIndex: 'status',
    customRender: ({ record }:{record:any}) => statusName[record.status] || '-',
  },
  // {
  //   title: '绑定房屋数量',
  //   width: 100,
  //   dataIndex: 'houseCount',
  // },
  {
    title: '操作',
    dataIndex: 'operate',
    width: 260,
    fixed: 'right',
    slots: { customRender: 'operate' },
  }];

export const companyBindHouseListPageColumns = [{
  title: '序号',
  dataIndex: 'index',
  customRender: ({ text, record, index }:any) => index + 1,
}, {
  title: '楼层房号',
  dataIndex: 'houseName',
}, {
  title: '所属楼栋单元',
  dataIndex: 'buildingName',
}, {
  title: '关联园区',
  dataIndex: 'parkOrgName',
}, {
  title: '建筑面积（㎡）',
  dataIndex: 'buildArea',
}, {
  title: '套内面积（㎡）',
  dataIndex: 'coverArea',
}, {
  title: '起租时间',
  dataIndex: 'rentStartTime',
}, {
  title: '到期时间',
  dataIndex: 'rentEndTime',
}, {
  title: '操作',
  dataIndex: 'operate',

  slots: { customRender: 'operate' },
}];
export const operateDialogTitleName = {
  bind: '绑定房屋',
  edit: '编辑企业',
  add: '新增企业',
  unbund: '解绑房屋',
  detail: '企业详情',
};

/**
 * @description: 新增用户
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddCompany = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<CompanyInfo>({
      address: '', // 注册地址
      code: '', // 统一社会信用代码
      companyType: null, // 企业类型
      contacts: '', // 联系人
      id: '',
      licenses: '', // 营业执照
      name: '', // 企业名称
      parkOrgId: '', // 园区id
      tel: '', // 联系人电话
    });
    const rulesRef = reactive({
      name: [{ required: true, message: '请输入企业名称' }],
      companyType: [{ required: true, message: '请选择企业类型' }],
      parkOrgId: [{ required: true, message: '请选择关联园区' }],
      code: [{
        validator: (rules:unknown, value:string) => {
          if (!REGS.creditCode.test(value)) return Promise.reject('请输入18位统一社会信用代码');
          return Promise.resolve();
        },
        required: true,
      }],
      // licenses: [{ required: true, message: '请上传营业执照' }],
      tel: [{
        validator: (rules:unknown, value:string) => {
          if (value && !REGS.mobile.test(value)) return Promise.reject('请输入正确手机号');
          return Promise.resolve();
        },
      }],
    });

    const fill = () => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
    };
    const { validateInfos, validate } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      validateInfos,
      validate,
      rulesRef,
    };
  };

  // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return (defaultModelRef?: any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();

/**
 * @description: 新增用户
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useBindHouse = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<{houseList:BindHouseInfo[]}>({
      houseList: [],
    });
    const rulesRef = {
      houseId: {
        required: true,
        trigger: 'change',
        message: '请选择关联楼层房号',
      },
      buildingId: {
        required: true,
        trigger: 'change',
        message: '请选择关联楼栋单元',
      },
      rentTimes: {
        required: true,
        trigger: 'change',
        type: 'array',
        message: '请选择租期时间',
      },
      // houseId: [{ required: true, message: '请选择楼层房号' }],
      // rentEndTime: [{ required: true, message: '请选择起租时间' }],
      // rentStartTime: [{ required: true, message: '请选择到期时间' }],
    };

    const fill = () => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key]) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
    };
    // const { validateInfos, validate } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      // validateInfos,
      // validate,
      rulesRef,
    };
  };

  // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return (defaultModelRef?: any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
