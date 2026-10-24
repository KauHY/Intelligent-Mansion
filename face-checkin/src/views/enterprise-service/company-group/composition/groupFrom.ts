/*
 * @Author: your name
 * @Date: 2022-04-14 10:47:10
 * @LastEditTime: 2022-04-14 15:45:43
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\views\enterprise-service\company-group\composition\useForm copy.ts
 */
/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-01 14:52:41
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import { message } from 'ant-design-vue';
import useForm from '~utils/useForm';
import { GroupLaneInfo } from '~models/company-group';
/**
 * @description: 新增企业群组标签
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const groupAdd = (() => {
  const createInstance = () => {
    const modelRef = reactive<GroupLaneInfo>({
      id: '',
      disabled: false,
      expand: true,
      label: '',
      leaf: false,
      parentId: '',
      parkOrgId: '',
      selectable: true,
      type: 'ENTERPRISE_GROUP',

    });

    const rulesRef = reactive({
      label: [{ required: true, message: '请输入群组标签名称', trigger: 'change' }],
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
