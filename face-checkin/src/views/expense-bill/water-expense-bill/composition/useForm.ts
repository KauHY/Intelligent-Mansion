/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-21 17:39:38
 */
import { onUnmounted, reactive } from 'vue';
import useForm from '~utils/useForm';
/**
 * @description: 上传凭证接口
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */

export const useModifyDate = (() => {
  const createInstance = () => {
    const modelRef = reactive<{companyFiles:string, id:string, payType:number}>({
      id: '',
      companyFiles: '',
      payType: 1,
    });
    const rulesRef = reactive({
      companyFiles: [{ required: true, message: '请上传文件' }],
    });

    const fill = (defaultModelRef:any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
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
