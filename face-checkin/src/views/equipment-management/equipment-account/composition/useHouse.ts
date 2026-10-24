/**
 * @description: 编辑新增房屋
 * @param {string} formName
 * @return {*}
 * @author: GUOCHAO82
 */
import {
  onUnmounted, reactive,
} from 'vue';
import { FloorInfo } from '@/models/equipment-management';
import useForm from '~utils/useForm';

export const useHouse = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<FloorInfo>({
      disabled: false,
      expand: true,
      hierarchy: undefined,
      label: '',
      leaf: false,
      parentId: '',
      parkOrgId: '',
      selectable: true,
      type: 'TZ',
    });
    const rulesRef = reactive({
      label: [{
        validator: (rules:unknown, value:string) => {
          if (!value) return Promise.reject('请输入分类名称');
          if (value.length > 30) return Promise.reject('最多只能输入30个字符');
          return Promise.resolve();
        },
        required: true,
      }],
    });
    const fill = (targetModelRef:any, baseModelRef:any) => {
      Object.keys(targetModelRef).forEach((key) => {
        if (baseModelRef[key] || baseModelRef[key] === 0) { targetModelRef[key] = baseModelRef[key]; }
      });
    };
    const { validateInfos, validate, validateField } = useForm(modelRef, rulesRef);
    return {
      fill,
      modelRef,
      validateInfos,
      validate,
      validateField,
    };
  };
    // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return (defaultModelRef?:any) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(defaultModelRef);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
