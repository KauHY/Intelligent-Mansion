/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-28 14:19:38
 */

import { onUnmounted, reactive, ref } from 'vue';
// import useForm from '~composition/useForm';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { RenewHouse } from '~models/company-mangement';

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
// 入住企业table columns
export const columns = [
  {
    title: '企业名称',
    dataIndex: 'companyName',
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
    title: '解绑时间',
    dataIndex: 'unbindTime',
  }, {
    title: '操作',
    dataIndex: 'operate',
    width: 330,
    slots: { customRender: 'operate' },
  },
];

export const renewalRecorRColumns = [{
  title: '企业名称',
  dataIndex: 'companyName',
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
  title: '起租时间',
  dataIndex: 'rentStartTime',
}, {
  title: '到期时间',
  dataIndex: 'rentEndTime',
}, {
  title: '续租到期时间',
  dataIndex: 'renewEndTime',
}, {
  title: '登记时间',
  dataIndex: 'createTime',
}];

/**
 * @description: 续租
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useRentRenew = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<RenewHouse>({
      id: '',
      rentRenewEndTime: '', // 地址
    });
    const rulesRef = reactive({
      rentRenewEndTime: [{ required: true, message: '请输入企业名称' }],
    });

    const fill = () => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key]) {
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
