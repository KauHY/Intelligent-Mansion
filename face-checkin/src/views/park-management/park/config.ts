/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-27 18:26:12
 */

import { ParkInfo } from '@/models/park';
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
// eslint-disable-next-line no-undef
// console.log(__VUE_OPTIONS_API__, '----process.env.UNI_USING_VUE3_OPTIONS_API');

export const statusName = {
  0: '禁用',
  1: '启用',
};

export const orgTypeNames = {
  0: '平台',
  1: '机构',
};

export const operateDialogTitleName = {
  add: '新增组织',
  edit: '编辑组织',
};

export const columns = [{
  title: '园区ID',
  dataIndex: 'parkOrgId',
}, {
  title: '园区名称',
  dataIndex: 'name',
  slots: { customRender: 'name' },
}, {
  title: '省份',
  dataIndex: 'province',
}, {
  title: '城市',
  dataIndex: 'city',
}, {
  title: '详细地址',
  dataIndex: 'address',
}, {
  title: '经度',
  dataIndex: 'latitude',
}, {
  title: '纬度',
  dataIndex: 'longitude',
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 180,
  fixed: 'right',
  slots: { customRender: 'operate' },
}];

/**
 * @description:
 * @param {string} formName
 * @return {*}
 * @author: GUOCHAO82
 */
export const useCompleteParkInfoForm = (() => {
  const createInstance = () => {
    const modelRef = reactive<ParkInfo>({
      address: '',
      buildTime: '',
      buildingArea: 0,
      companyContacts: '',
      companyName: '',
      companyTel: '',
      coverArea: 0,
      description: '',
      fireExamineStatus: 0,
      firePerson: '',
      firePersonTel: '',
      parkOrgId: '',
      latitude: 0,
      longitude: 0,
      managementContacts: '',
      managementName: '',
      managementTel: '',
      name: '',
      parkFunction: null,
      parkType: 1,
      pics: '',
      property: 1,
      province: '',
      city: '',
    });
    const rulesRef = reactive({
      name: [{
        validator: (rules:unknown, value:string) => {
          if (!value) return Promise.reject('请输入园区名称');
          if (value.length > 30) return Promise.reject('最多只能输入30个字符');
          return Promise.resolve();
        },
        required: true,
      }],
      coverArea: [{
        pattern: REGS.area,
        message: '请输入总占地面积，最多输入9位整数+两位小数',
      }],
      buildingArea: [{
        pattern: REGS.area,
        message: '请输入总建筑面积，最多输入9位整数+两位小数',
      }],
      address: [{
        validator: (rules:unknown, value:string) => {
          if (!value) return Promise.reject('请输入详细地址');
          if (value.length > 200) return Promise.reject('最多只能输入200个字符');
          return Promise.resolve();
        },
        required: true,
      }],
      latitude: [{ required: true, message: '组织类型能为空' }],
      longitude: [{ required: true, message: '组织类型能为空' }],
      firePersonTel: [{
        validator: (rules:unknown, value:string) => {
          if (value && !REGS.mobile.test(value)) return Promise.reject('请输入正确的手机号码');
          return Promise.resolve();
        },
      }],
      companyTel: [{
        validator: (rules:unknown, value:string) => {
          if (value && !REGS.mobile.test(value)) return Promise.reject('请输入正确的手机号码');
          return Promise.resolve();
        },
      }],
      managementTel: [{
        validator: (rules:unknown, value:string) => {
          if (value && !REGS.mobile.test(value)) return Promise.reject('请输入正确的手机号码');
          return Promise.resolve();
        },
      }],
    });
    const instance = useForm(modelRef, rulesRef);
    return instance;
  };
  // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return () => {
    if (!userFormInstance) {
      userFormInstance = createInstance();
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();

// let instance:any = null;
// export const Model = (() => {
//   console.log('Loading调用了---------');
//   return (params:any) => {
//     if (!instance) {
//       instance = params;
//     }
//     return instance;
//   };
// })();
