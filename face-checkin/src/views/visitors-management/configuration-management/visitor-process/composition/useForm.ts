/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-29 16:01:59
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { VisitorProcess } from '~models/visitors-management';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useVisitorProcess = (() => {
  const createInstance = () => {
    const modelRef = reactive<VisitorProcess>({
      id: '',
      companyId: '', // 公司id
      parkOrgId: '',
      visitorTypeId: '', // 访客类型
      processName: '', // 流程名称
      processType: null, // 流程类型
      processNodes: '', // 流程节点
      invitorAudit: null,
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择园区' }],
      companyId: [{ required: true, message: '请选择企业' }],
      visitorTypeName: [{ required: true, message: '请输入访客类型' }],
      processName: [{ required: true, message: '请输入流程名称' }],
      processType: [{ required: true, message: '请选择流程类型' }],
      visitorTypeId: [{ required: true, message: '请选择访客类型' }],
      processNodes: [{ required: true, message: '请选择流程节点' }],
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
