/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-21 10:22:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-02 14:41:42
 */

/**
 * @description:
 * @param {string} formName
 * @return {*}
 * @author: GUOCHAO82
 */
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { REGS } from '@/constants/validate';
import { PatrolLine, PatrolLineLinkPlaceParams, PatrolPlace } from '~models/patrol-inspection';
import useForm from '~utils/useForm';

export const useAddPatrolLine = (() => {
  const createInstance = () => {
    const modelRef = reactive<PatrolLine>({
      id: '',
      circuitName: '', // 巡检路线名称
      orderly: false, // 是否有序
      parkOrgId: '',
      parkOrgName: '',
      remark: '', // 备注
      type: null, // 巡检点类型
      pointIds: [],
      // updateTime: '', // 更新时间
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择园区' }],
      circuitName: [{ required: true, message: '请输入巡检路线名称' }],
      type: [{ required: true, message: '请选择巡检的类型' }],
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

export const useAddLinkPlace = (() => {
  const createInstance = () => {
    const modelRef = reactive<PatrolLineLinkPlaceParams>({
      circuitId: '',
      pointIds: [],
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择园区' }],
      pointName: [{ required: true, message: '请输入巡检名称' }],
      type: [{ required: true, message: '请选择巡检的类型' }],
      location: [{ required: true, message: '巡检点不能为空' }],
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
