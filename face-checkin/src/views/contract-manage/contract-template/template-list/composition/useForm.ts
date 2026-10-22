/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-12 15:25:33
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { ContractTemplateListInfo } from '~models/contract-manage';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddTemplate = (() => {
  const createInstance = () => { // 新增问卷
    const modelRef = reactive<ContractTemplateListInfo>({
      type: null,
      parkOrgId: '',
      templateKey: '', // 合同模板key
      templateName: '', // 合同模板名称
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择所属园区' }],
      type: [{ required: true, message: '请选择合同类型' }],
      templateKey: [{ required: true, message: '请上传文件' }],
      // coverPhoto: [{ required: true, message: '请上传封面图' }],
    });

    const fill = (defaultModelRef?: any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        debugger;
        if (defaultModelRef[key] || defaultModelRef[key] === false) {
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
