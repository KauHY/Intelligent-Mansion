/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-18 17:29:35
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { AttendanceRules } from '~models/attendance';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAttendanceRule = (() => {
  const createInstance = () => {
    const modelRef = reactive<AttendanceRules>({
      applyType: null, // 规则适用类型，0：全公司员工适用，1：指定员工
      companyId: '',
      companyName: '',
      daysOfWork: [], // 关联工作日
      holiday: 1,
      id: '',
      latitude: null, // 纬度
      location: '', // 考勤地点
      longitude: null, // 经度
      parkOrgId: '',
      parkOrgName: '',
      punchInTime: '', // 上班打卡时间
      punchOutTime: '', // 下班打卡时间
      punchRange: null, // 考勤范围（米）
      ruleName: '', // 规则名称
      employeeIds: [],
      workdayType: 0,
    });
    const rulesRef = reactive({
      ruleName: [{ required: true, message: '请输入规则名称' }],
      parkOrgId: [{ required: true, message: '请选择园区' }],
      companyId: [{ required: true, message: '请选择考勤公司' }],
      applyType: [{ required: true, message: '请选择考勤人员' }],
      punchInTime: [{ required: true, message: '请选择上班打卡时间' }],
      punchOutTime: [{ required: true, message: '请选择下班打卡时间' }],
      parkingLotName: [{ required: true, message: '请输入停车场名称' }],
      // daysOfWork: [{ required: true, message: '请选择工作日' }],
      location: [{ required: true, message: '请选择考勤地点' }],
      punchRange: [{
        validator: (rule:unknown, value:any) => {
          if (value < 100 || value > 1000) {
            return Promise.reject('请输正确考勤范围，只能输入100-1000之间得数字');
          }
          const pattern = /(^[1-9]\d*$)/;
          if (!pattern.test(value)) {
            return Promise.reject('请输正确考勤范围，只能输入100-1000之间得数字');
          }
          return Promise.resolve();
        },
        required: true,
      }],
    });

    const fill = (defaultModelRef?: any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
      });
    };
    const {
      validateInfos, validate, clearValidate, validateField,
    } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      validateInfos,
      validate,
      validateField,
      rulesRef,
      clearValidate,
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
