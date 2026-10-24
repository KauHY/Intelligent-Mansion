/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 17:05:26
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-10 17:14:14
 */

import { Ref } from 'vue';
import { HousingBusinessInfo } from '~models/wisdom-canvass-business';
import { DictionaryMap } from '~composition/useHashMapDic';
// import useForm from '~composition/useForm';

export const statusName = {
  0: '禁用',
  1: '启用',
};

export const orgTypeNames = {
  0: '机构',
  1: '园区',
};

export const operateDialogTitleName = {
  add: '新增组织',
  edit: '编辑组织',
};

export const getColumns = (options:Ref<DictionaryMap<['EFFECT_TYPE', 'BUSINESS_STATUS']>>) => [{
  title: '关联园区',
  dataIndex: 'parkOrgName',
  fixed: 'left',
}, {
  title: '招商标题',
  dataIndex: 'title',
  slots: { customRender: 'title' },
  fixed: 'left',

}, {
  title: '招商数量',
  dataIndex: 'amount',
}, {
  title: '招商类型',
  // dataIndex: 'effectType',
  customRender: ({ record }:{record:HousingBusinessInfo}) => (record.effectType || record.effectType === 0 ? options.value.EFFECT_TYPE?.[record.effectType]?.label : ''),
}, {
  title: '招商面积（㎡）',
  dataIndex: 'area',
}, {
  title: '招商状态',
  dataIndex: 'status',
  customRender: ({ record }:{record:HousingBusinessInfo}) => (record.status || record.status === 0 ? options.value.BUSINESS_STATUS?.[record.status]?.label : ''),
}, {
  title: '截止时间',
  dataIndex: 'deadline',
  // customRender: ({ record }:{record:HousingBusinessInfo}) => (record.effectType === 1 ? '长期有效' : record.deadline),
}, {
  title: '创建时间',
  dataIndex: 'createTime',
}, {
  title: '操作',
  dataIndex: 'operate',
  width: 220,
  fixed: 'right',
  slots: { customRender: 'operate' },
}];

export const houseColumns = [
  // {
  //   title: '关联园区',
  //   dataIndex: 'parkOrgName',
  // },
  {
    title: '所属楼栋单元',
    dataIndex: 'buildingName',
  },
  {
    title: '楼层房号',
    dataIndex: 'name',
  },
  {
    title: '单位租金（元/平米/月）',
    dataIndex: 'unitRent',
  },
  {
    title: '最早租期',
    dataIndex: 'earliestRentTime',
  },
];
/**
 * @description:
 * @param {string} formName
 * @param {*} isPlatformAdminRole 是否平台管理员
 * @param {any} defaultModelRef 默认值
 * @return {*}
 * @author: GUOCHAO82
 */
// export const useAddOrgForm = (() => {
//   const createInstance = (defaultModelRef: any) => {
//     console.log(defaultModelRef, '-----defaultModelRef');
//     const modelRef = reactive<AddOrganization>({
//       id: '',
//       description: '', // 描述
//       orgName: '',
//       orgType: 0,
//       parentId: '',
//     });

//     const rulesRef = reactive({
//       orgName: [{ required: true, message: '组织名称能为空' }],
//       orgType: [{ required: true, message: '组织类型能为空' }],
//     });

//     const fill = () => {
//     // debugger;
//       if (!defaultModelRef || !modelRef) return;
//       const copyModelRef = modelRef as any;
//       Object.entries(copyModelRef).forEach(([key]) => {
//         if (defaultModelRef[key]) {
//           copyModelRef[key] = defaultModelRef[key];
//         }
//       });
//       modelRef.parentId = defaultModelRef.parentId;
//     };
//     const { validateInfos, validate } = useForm(modelRef, rulesRef);
//     return {
//       modelRef,
//       fill,
//       validateInfos,
//       rulesRef,
//       validate,
//     };
//   };
//   // 固定写法
//   let userFormInstance:ReturnType<typeof createInstance>|null;
//   return (defaultModelRef?: any) => {
//     if (!userFormInstance) {
//       userFormInstance = createInstance(defaultModelRef);
//       onUnmounted(() => {
//         userFormInstance = null; // 卸载时回收
//       });
//     }
//     return { ...userFormInstance };
//   };
// })();
