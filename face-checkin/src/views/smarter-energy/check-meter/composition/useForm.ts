import {
  onUnmounted, reactive,
} from 'vue';
import { AddParams, changeParams } from '~models/smarter-energy';
import useForm from '~utils/useForm';

export const rentalForm = () => {
  const modelRef = reactive<AddParams>({
    buildingId: '',
    deviceType: 1,
    deviceId: '',
    effectProperty: null,
    effectType: null,
    floors: null,
    houseIds: [],
    id: '',
    initialReading: null,
    initialTime: '',
    location: '',
    maxRange: null,
    meterNo: '',
    meterType: null,
    deviceName: '',
    parkOrgName: '',
    parkOrgId: '',
    remark: '',
  });
  const rulesRef = reactive({
    effectProperty: [{ required: true, message: '请选择表计属性' }],
    meterType: [{
      required: true,
      message: '请选择表类型',
      validator: (rules:unknown, value:string) => {
        if (!value) return Promise.reject('请选择表类型');
        if (modelRef.meterType === 1) {
          rulesRef.deviceId[0].required = false;
          return Promise.resolve('请选择表类型');
        }
        rulesRef.deviceId[0].required = true;
        return Promise.resolve();
      },
    }],
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
