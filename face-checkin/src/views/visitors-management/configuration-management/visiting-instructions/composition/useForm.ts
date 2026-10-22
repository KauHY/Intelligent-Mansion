/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-29 11:32:48
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { visitingInstructions } from '~models/visitors-management';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAuditPerson = (() => {
  const createInstance = () => {
    const modelRef = reactive<visitingInstructions>({
      id: '',
      noticeContent: '', // 须知内容
      noticeName: '', // 须知名称
      parkOrgId: '', // 园区id
    });
    const rulesRef = reactive({
      noticeName: [{ required: true, message: '请输入须知名称' }],
      parkOrgId: [{ required: true, message: '请选择园区' }],
      noticeContent: [{ required: true, message: '请输入来访须知内容' }],
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
