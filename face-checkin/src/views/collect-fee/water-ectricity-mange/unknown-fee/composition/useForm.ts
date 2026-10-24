/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-21 17:39:38
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import dayjs from 'dayjs';
import useForm from '~utils/useForm';
import { UnknownRentFeeModifyDate } from '~models/collect-fee';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */

export const useModifyDate = (() => {
  const createInstance = () => { // 新增问卷
    const modelRef = reactive<UnknownRentFeeModifyDate>({
      ids: [],
      latestPayDate: dayjs().format('YYYY-MM-DD'),
      dueDate: dayjs().format('YYYY-MM-DD'),
    });
    const rulesRef = reactive({
      latestPayDate: [{ required: true, message: '请选择最晚缴纳日期' }],
      dueDate: [{ required: true, message: '请选择应收日期' }],
    });

    // const fill = (defaultModelRef?: any) => {
    //   if (!defaultModelRef && !modelRef) return;
    //   const copyModelRef = modelRef as any;
    //   Object.entries(copyModelRef).forEach(([key]) => {
    //     debugger;
    //     if (defaultModelRef[key] || defaultModelRef[key] === false) {
    //       copyModelRef[key] = defaultModelRef[key];
    //     }
    //   });
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
