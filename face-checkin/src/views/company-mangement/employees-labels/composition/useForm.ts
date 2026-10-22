/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-27 11:54:18
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { TagInfo } from '~models/company-mangement';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddEmployees = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<TagInfo>({
      id: '',
      companyId: '', // 公司id
      companyName: '', // 公司名称
      parkOrgName: '', // 园区名称
      parkOrgId: '', // 园区id
      tagName: '', // 标签名
      // updateTime: '', // 更新时间
    });
    const rulesRef = reactive({
      tagName: [{ required: true, message: '请输入员工标签' }],
      parkOrgId: [{ required: true, message: '请选择园区' }],
      companyId: [{ required: true, message: '请选择所属企业' }],
    });

    const fill = () => {
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
  return (defaultModelRef?: any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
