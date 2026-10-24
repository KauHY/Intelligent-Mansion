/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-20 16:54:52
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import { message } from 'ant-design-vue';
import useForm from '~utils/useForm';
import { GroupAddInfo } from '~models/company-group';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useLaneAdd = (() => {
  const createInstance = () => {
    const modelRef = reactive<GroupAddInfo>({
      parkOrgId: '',
      businessIds: [],
      categoryId: '',
      remark: '',
    });

    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择所属园区', trigger: 'change' }],
      businessIds: [{ required: true, message: '请选择企业' }],
      categoryId: [{ required: true, message: '请选择所在群组' }],
      remark: [{ required: false, message: '超出限制字符长度(100)', max: 100 }],
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
