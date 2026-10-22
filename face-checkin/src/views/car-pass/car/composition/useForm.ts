/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-31 14:47:59
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { CarInfo } from '~models/car-pass';
/**
 * @description: 新增车辆
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useCarAdd = (() => {
  const createInstance = () => {
    const modelRef = reactive<CarInfo>({
      id: '',
      authEndTime: '', // 授权结束时间
      authStartTime: '', // 授权开始时间
      carNum: '', // 车牌号
      parkType: null, // 车辆类型
      companyId: '', // 企业id
      parkOrgId: '',
      parkingLotIds: '',
      employeeId: '',
      carOwnerName: '',
    });
    const rulesRef = reactive({
      carNum: [{
        validator(rules:unknown, value:string) {
          if (!REGS.carNumber.test(value)) return Promise.reject('请输入正确车牌号');
          return Promise.resolve();
        },
        required: true,
      }],
      // carOwnerName: [{ required: true, message: '请输入车主姓名' }],
      parkType: [{
        validator(rules:unknown, value:number) {
          if (value === 3) {
            rulesRef.companyId[0].required = false;
            rulesRef.employeeId[0].required = false;
          } else {
            rulesRef.companyId[0].required = true;
            rulesRef.employeeId[0].required = true;
          }
          return Promise.resolve();
        },
        required: true,
      }],
      parkingLotIds: [{ required: true, message: '请选择停车场' }],
      employeeId: [{ required: true, message: '请选择员工' }],
      // parkType: [{ required: true, message: '请选择停车类型' }],
      authStartTime: [{ required: true, message: '请选择授权有效期' }],
      parkOrgId: [{ required: true, message: '请选择园区' }],
      companyId: [{ required: true, message: '请选择企业' }],
    });

    const fill = (defaultModelRef?: any) => {
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
