import { string } from 'mathjs';
import {
  onUnmounted, reactive,
} from 'vue';
import { AddParams, changeParams } from '~models/smarter-energy';
import useForm from '~utils/useForm';

export const rentalForm = () => {
  const modelRef = reactive<AddParams>({
    buildingId: '',
    deviceType: 6,
    deviceId: '',
    effectProperty: null,
    effectType: null,
    floors: [],
    houseIds: [],
    id: '',
    initialReading: null,
    initialTime: '',
    location: '',
    maxRange: null,
    meterType: null,
    deviceName: '',
    parkOrgId: '',
    remark: '',
  });
  const rulesRef = reactive({
    effectProperty: [{
      required: true,
      message: '请选择表计属性',
      validator: (rules:unknown, value:string) => {
        if (!value) return Promise.reject('请选择表计属性');
        if (modelRef.effectProperty === 1) {
          rulesRef.houseIds[0].required = true;
          return Promise.resolve();
        }
        rulesRef.houseIds[0].required = false;
        return Promise.resolve();
      },
    }],
    meterType: [{
      required: true,
      message: '请选择表类型',
      validator: (rules:unknown, value:string) => {
        if (!value) return Promise.reject('请选择表类型');
        if (modelRef.meterType === 1) {
          rulesRef.deviceId[0].required = false;
          return Promise.resolve();
        }
        rulesRef.deviceId[0].required = true;
        return Promise.resolve();
      },
    }],
    houseIds: [{
      required: false,
      message: '请选择所属房屋',
    }],
    effectType: [{ required: true, message: '请选择表计类别' }],
    deviceName: [{ required: true, message: '请输入表计名称' }],
    parkOrgId: [{ required: true, message: '请选择园区' }],
    deviceId: [{ required: true, message: '请输入设备ID' }],
    maxRange: [{
      required: true,
      pattern: /^([1-9]\d{0,7})$/,
      message: '请输入最大量程，只能输入最多为8位，且大于0的非负的整数',
    }],

    buildingId: [{ required: true, message: '请选择所属楼栋' }],
    floors: [{ required: true, message: '请选择所属所属楼层' }],
    initialReading: [{
      pattern: /^(([1-9]{1}\d*)|(0{1}))(.\d{1,2})?$/,
      required: true,
      message: '请输入初始读数,只能数字，最多两位小数',
    }],

    initialTime: [{ required: true, message: '请选择初始时间' }],
  });
  return {
    modelRef,
    rulesRef,
  };
};

/**
 * @description: 换表
 * @param {any} defaultModelRef
 * @return {*}
 * @author: GUOCHAO82
 */
export const useResetPwdForm = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<changeParams>({
      oldReading: undefined,
      deviceType: undefined,
      newMaxRange: undefined,
      newReading: undefined,
      newDeviceId: '',
      oldMeterId: undefined,
    });
    let formValidateField:any = null;
    const rulesRef = reactive({
      newReading: [{
        pattern: /^(([1-9]{1}\d*)|(0{1}))(.\d{1,2})?$/,
        required: true,
        message: '请输入新表读数,只能数字，最多两位小数',
      }],
      oldReading: [{
        pattern: /^(([1-9]{1}\d*)|(0{1}))(.\d{1,2})?$/,
        required: true,
        message: '请输入旧表读数,只能数字，最多两位小数',
      }],
      newMaxRange: [{
        required: true,
        pattern: /^([1-9]\d{0,7})$/,
        message: '请输入最大量程，只能输入最多为8位，且大于0的非负的整数',
      }],
      newDeviceId: [{
        required: true,
        message: '请输入设备ID',
      }],
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
    const { validateInfos, validate, validateField } = useForm(modelRef, rulesRef);
    formValidateField = validateField;
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
