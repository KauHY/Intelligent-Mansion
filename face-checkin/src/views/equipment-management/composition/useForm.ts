/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-12 13:40:04
 */
import {
  onUnmounted, reactive, Ref, ref,
} from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { EquipmentInfo, Extension } from '~models/equipment-management';
import { DictionaryMap } from '~composition/useHashMapDic';

import CarPass from '~models/car-pass';
import CompanyMangement from '~models/company-mangement';

/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useEquipmentAdd = () => {
  const modelRef = reactive<EquipmentInfo>({
    id: '',
    deviceType: null, // 设备类型名
    deviceId: '', // 设备id
    deviceName: '', // 设备名称
    ipAddress: '', // ip地址
    parkOrgId: '',
    remark: '',
    extension: {
      location: '', // 设备位
      companyId: '',
      // ------- 道闸 ----------
      parkingLotId: '', // 关联停车场
      carLane: '', // 车道
      gatewayType: null, // 出入口
    },
  });

  const rulesRef = reactive({
    deviceType: [{ required: true, message: '请选择设备类型' }],
    deviceName: [{ required: true, message: '请输入设备名称' }],
    deviceId: [{ required: true, message: '请输入设备ID' }],
    // ipAddress: [{ required: true, message: '请输入设备IP地址' }],
    parkOrgId: [{ required: true, message: '请选择园区' }],
    extension: {
      parkingLotId: [{
        required: true,
        message: '请选择停车场',
      }],
      gatewayType: [{ required: true, message: '请选择出入口', type: 'number' }],
      carLane: [{ required: true, message: '请选择车道' }],
    },
  });

  return {
    modelRef,
    rulesRef,
  };
};

// /**
//  * @description: 新增员工
//  * @param {*}
//  * @return {*}
//  * @author: GUOCHAO82
//  */
// export const useEquipmentAdd = (() => {
//   const createInstance = () => {
//     const modelRef = reactive<EquipmentInfo>({
//       id: '',
//       deviceType: null, // 设备类型名
//       deviceId: '', // 设备id
//       deviceName: '', // 设备名称
//       ipAddress: '', // ip地址
//       parkOrgId: '',
//       remark: '',
//       extension: {
//         location: '', // 设备位
//         companyId: '',
//         // ------- 道闸 ----------
//         parkingLotId: '', // 关联停车场
//         carLane: '', // 车道
//         cntrance: '', // 入口
//         exit: '', // 出口
//       },
//     });
//     const rulesRef = reactive({
//       parkOrgId: [{ required: true, message: '请选择园区' }],
//       laneName: [{ required: true, message: '请输入车道名称' }],
//       parkingLotId: [{ required: true, message: '请选择停车场' }],
//     });

//     const fill = (defaultModelRef?: any) => {
//       if (!defaultModelRef && !modelRef) return;
//       const copyModelRef = modelRef as any;
//       Object.entries(copyModelRef).forEach(([key]) => {
//         if (defaultModelRef[key]) {
//           copyModelRef[key] = defaultModelRef[key];
//         }
//       });
//     };
//     const { validateInfos, validate } = useForm(modelRef, rulesRef);
//     return {
//       modelRef,
//       fill,
//       validateInfos,
//       validate,
//       rulesRef,
//     };
//   };

//   // 固定写法
//   let userFormInstance:ReturnType<typeof createInstance>|null;
//   return () => {
//     if (!userFormInstance) {
//       userFormInstance = createInstance();
//       onUnmounted(() => {
//         userFormInstance = null; // 卸载时回收
//       });
//     }
//     return { ...userFormInstance };
//   };
// })();

/**
 * @description: 设配字段配置项
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
type FormFieldConfs = {
  [key in keyof Partial<Extension>]: FormFieldConf
}
type FormConfs = {[key:number]:FormFieldConfs}
export const useFormFieldConfs = (formData:EquipmentInfo, options: Ref<DictionaryMap<['DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']>>) => {
  const formConfs = ref<FormConfs>({});
  const laneLotOpts = ref<SelectOptions>([]);
  const parkingLotChange = async (parkingLotId:string, isInit = false) => {
    if (!parkingLotId) return;
    laneLotOpts.value = await new CarPass().laneListDic(parkingLotId);
    if (isInit) return;
    const tempFormConfs = formConfs.value as any;
    tempFormConfs[2].carLane.value = '';
  };

  const companyOpts = ref<SelectOptions>([]);
  const parkingLotOpts = ref<SelectOptions>([]);
  const parkOrgChange = async (orgId:string, isInit = false) => {
    const tempFormConfs = formConfs.value as any;
    console.log(formData, '---------formData');
    if (formData?.deviceType === 1) {
      companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
      if (isInit) return;
      tempFormConfs[1].companyId.value = '';
    } else if (formData?.deviceType === 2) {
      parkingLotOpts.value = await new CarPass().parkingLotDic(orgId);
      if (isInit) return;
      debugger;
      tempFormConfs[2].parkingLotId.value = '';
      tempFormConfs[2].carLane.value = '';
      laneLotOpts.value = [];
    }
  };

  formConfs.value = {
    1: {
      location: {
        tag: 'input',
        // value: '',
        set value(val:any) {
          formData.extension.location = val;
        },
        get value() {
          return formData.extension.location as any;
        },
        fieldKey: 'extension.location',
        placeholder: '请输入设备位置',
        formLabel: '设备位置',
      },
      companyId: {
        tag: 'select',
        // value: '',
        set value(val:any) {
          formData.extension.companyId = val;
        },
        get value() {
          return formData.extension.companyId as any;
        },
        fieldKey: 'extension.companyId',
        formLabel: '所属公司',
        placeholder: '请选择所属公司',
        options: () => companyOpts.value,
      },
    },
    2: {
      parkingLotId: {
        tag: 'select',
        set value(val:any) {
          formData.extension.parkingLotId = val;
        },
        get value() {
          return formData.extension.parkingLotId as any;
        },
        fieldKey: 'extension.parkingLotId',
        formItemName: ['extension', 'parkingLotId'],
        formLabel: '停车场',
        options: () => parkingLotOpts.value,
        onChange: parkingLotChange,
      },
      carLane: {
        tag: 'select',
        // value: '',
        set value(val:string) {
          formData.extension.carLane = val;
        },
        get value() {
          return formData.extension.carLane;
        },
        fieldKey: 'extension.carLane',
        formLabel: '关联车道',
        formItemName: ['extension', 'carLane'],
        options: () => laneLotOpts.value,
      },
      gatewayType: {
        tag: 'select',
        // value: '',
        set value(val:any) {
          formData.extension.gatewayType = val;
        },
        get value() {
          return formData.extension.gatewayType as any;
        },
        fieldKey: 'extension.gatewayType',
        formItemName: ['extension', 'gatewayType'],
        formLabel: '出入口',
        options: () => options.value.GATEWAY_TYPE,
      },
    },
  };

  return {
    parkOrgChange,
    parkingLotChange,
    formConfs,
  };
};
