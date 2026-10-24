/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-28 13:53:48
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { GoodsReleaseAudit } from '~models/customer-service';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useGoodsReleaseAudit = (() => {
  const createInstance = () => {
    const modelRef = reactive<GoodsReleaseAudit>({
      checkOpinion: '',
      checkStatus: null,
      goodsReleaseId: '',
    });
    const rulesRef = reactive({
      checkStatus: [{ required: true, message: '请选择审核结果' }],
      checkOpinion: [{ required: true, message: '请输录入审核意见' }],
    });

    // const fill = (defaultModelRef?: any) => {
    //   if (!defaultModelRef && !modelRef) return;
    //   const copyModelRef = modelRef as any;
    //   Object.entries(copyModelRef).forEach(([key]) => {
    //     if (defaultModelRef[key]) {
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
