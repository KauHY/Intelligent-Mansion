/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-21 10:22:38
 * @LastEditors: your name
 * @LastEditTime: 2022-03-14 12:25:33
 */

/**
 * @description:
 * @param {''} formName
 * @return {*}
 * @author: GUOCHAO82
 */
import {
  onMounted, onUnmounted, reactive, computed, Ref,
} from 'vue';
import { REGS } from '@/constants/validate';
import { PatrolLine, LinkEmployee, PatrolPlan } from '~models/patrol-inspection';
import useForm from '~utils/useForm';

export const useAddPatrolPlan = (() => {
  const createInstance = () => {
    const modelRef = reactive<PatrolPlan>({
      planName: '', // 计划名称
      canDelay: false, // 节假日是否顺延
      circuitId: '', // 巡检路线ID
      cycleType: 0, // 执行周期类型,0天，1周
      effectDateEnd: '', // 生效日期
      effectDateStart: '', // 生效日期
      executionTimeEnd: '', // 执行结束时间
      executionTimeStart: '', // 执行开始时间
      executionRule: '',
      type: null,
      remark: '', // 备注
      id: '',
      parkOrgId: '',
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择园区' }],
      planName: [{ required: true, message: '请输入输入巡检计划名称' }],
      circuitId: [{ required: true, message: '请选择巡检路线' }],
      type: [{ required: true, message: '请选择巡检的类型', type: 'number' }],
      effectDateEnd: [{ required: true, message: '请选择生效日期' }],
      executionTimeEnd: [{ required: true, message: '请选择执行结束时间' }],
      executionTimeStart: [{ required: true, message: '请选择执行开始时间' }],
      executionRule: [{ required: true, message: '请选择执行规则' }],
    });
    const fill = (defaultModelRef:any) => {
      // debugger;
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
        // copyModelRef.houseIds = Object.keys(defaultModelRef.businessHouseVO);
      });
      // copyModelRef.
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

export const useLinkEmployee = (() => {
  const createInstance = () => {
    const modelRef = reactive<LinkEmployee>({
      planId: '',
      employeeIds: [],
    });
    const rulesRef = reactive({
      employeeIds: [{ required: true, message: '巡检人不能为空' }],
    });
    // const fill = (defaultModelRef:any) => {
    //   // debugger;
    //   modelRef.tableData = defaultModelRef.points || [];
    //   modelRef.id = defaultModelRef.id;
    // };
    const { validateInfos, validate } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      // fill,
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
