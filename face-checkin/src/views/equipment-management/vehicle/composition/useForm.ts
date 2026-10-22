/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-22 14:06:30
 */
import {
  onUnmounted, reactive, Ref, ref,
} from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { VehicleInfo, Extension } from '~models/vehicle-management';
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
  const modelRef = reactive<VehicleInfo>({
    id: '',
    deviceType: null, // 设备类型名
    deviceId: '', // 设备id
    deviceName: '', // 设备名称
    ipAddress: '', // ip地址
    parkOrgId: '',
    remark: '',
    location: '', // 设备位
    extension: {
      // ------- 道闸 ----------
      parkingLotId: '', // 关联停车场
      carLaneId: '', // 车道
      gatewayType: null, // 出入口
    },
  });

  const rulesRef = reactive({
    // deviceType: [{ required: true, message: '请选择设备类型' }],
    // deviceName: [{ required: true, message: '请输入设备名称' }],
    // deviceId: [{ required: true, message: '请输入设备ID' }],
    // ipAddress: [{ required: true, message: '请输入设备IP地址' }],
    // parkOrgId: [{ required: true, message: '请选择园区' }],
    // extension: {
    //   parkingLotId: [{
    //     required: true,
    //     message: '请选择停车场',
    //   }],
    //   gatewayType: [{ required: true, message: '请选择出入口', type: 'number' }],
    //   carLaneId: [{ required: true, message: '请选择车道' }],
    // },
  });

  return {
    modelRef,
    rulesRef,
  };
};

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
export const useFormFieldConfs = (formData:VehicleInfo, options: Ref<DictionaryMap<['DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']>>) => {
  const formConfs = ref<FormConfs>({});
  console.log('formConfs', formConfs);

  const laneLotOpts = ref<SelectOptions>([]);
  const parkingLotChange = async (parkingLotId:string, isInit = false) => {
    const tempFormConfs = formConfs.value as any;
    if (!parkingLotId) tempFormConfs[1].carLaneId.value = '';
    if (isInit) {
      tempFormConfs[1].carLaneId.value = '';
      // laneLotOpts.value = await new CarPass().laneListDic(parkingLotId);
    }
    if (parkingLotId) laneLotOpts.value = await new CarPass().laneListDic(parkingLotId);
    // if (isInit) return;
  };

  const parkingLotOpts = ref<SelectOptions>([]);
  const parkOrgChange = async (orgId:string, isInit = false) => {
    const tempFormConfs = formConfs.value as any;

    if (isInit) {
      tempFormConfs[1].parkingLotId.value = '';
      tempFormConfs[1].carLaneId.value = '';
      laneLotOpts.value = [];
    }
    parkingLotOpts.value = await new CarPass().parkingLotDic(orgId);
  };

  formConfs.value = {
    1: {
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
        formLabel: '关联停车场',
        options: () => parkingLotOpts.value,
        onChange: parkingLotChange,
      },
      carLaneId: {
        tag: 'select',
        // value: '',
        set value(val:string) {
          formData.extension.carLaneId = val;
        },
        get value() {
          return formData.extension.carLaneId;
        },
        fieldKey: 'extension.carLaneId',
        formLabel: '关联车道',
        formItemName: ['extension', 'carLaneId'],
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
