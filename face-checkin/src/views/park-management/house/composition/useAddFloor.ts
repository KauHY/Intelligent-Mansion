/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-25 14:50:05
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-02 14:36:46
 */
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { FloorInfo } from '@/models/building';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
/**
 * @description:
 * @param {string} formName
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddFloor = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<FloorInfo>({
      id: '',
      name: '', // 楼栋名称
      code: '', // 楼栋编码
      totalFloor: '', // 总层数
      buildTime: '', // 建成时间
      elevator: 1, // 电梯：0：无，1：有
      coverArea: '', // 占地面积
      buildArea: '', // 建筑面积
      parkOrgId: '', // 园区ID
      buildingId: '',
      property: 1,
      remark: '',
    });
    const rulesRef = reactive({
      name: [{
        validator: (rules:unknown, value:string) => {
          if (!value) return Promise.reject('请输入楼栋单元名称');
          if (value.length > 30) return Promise.reject('最多只能输入30个字符');
          return Promise.resolve();
        },
        required: true,
      }],
      property: [{
        message: '请选择楼栋属性',
        required: true,
      }],
      coverArea: [{
        pattern: REGS.area,
        message: '请输入总占地面积，最多输入9位整数+两位小数',
        required: false,
      }],
      buildArea: [{
        // validator: (rules:unknown, value:string) => {
        //   if (value && !REGS.numberAndFloat_2.test(value)) return Promise.reject('只能输入数字，最多两位小数');
        //   return Promise.resolve();
        // },
        required: false,
        pattern: REGS.area,
        message: '请输入总建筑面积，最多输入9位整数+两位小数',
      }],

      totalFloor: [{
        pattern: /^[1-9]{1}\d{0,2}$/,
        message: '请输入楼层总数，最多输入三位整数',
        required: true,
        // validator: (rules:unknown, value:string) => {
        //   if (!value || !REGS.nonzeroInteger.test(value)) return Promise.reject('只能输入非零整数');
        //   if (value.length > 3) return Promise.reject('最多只能输入3位数字');
        //   return Promise.resolve();
        // },
      }],
    });
    const fill = (targetModelRef:any, baseModelRef:any) => {
      Object.keys(targetModelRef).forEach((key) => {
        if (baseModelRef[key] || baseModelRef[key] === 0) { targetModelRef[key] = baseModelRef[key]; }
      });
      // targetModelRef.parkOrgId = baseModelRef.value;
      // targetModelRef.buildingId = baseModelRef.buildingId;
      console.log(targetModelRef, baseModelRef, '------baseModelRef---');
    };
    const { validateInfos, validate, validateField } = useForm(modelRef, rulesRef);
    return {
      fill,
      modelRef,
      validateInfos,
      validate,
      validateField,
    };
  };
    // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return (defaultModelRef?:any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
