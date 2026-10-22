/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-31 17:43:13
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { QuestionnaireSet } from '~models/satisfaction-survey';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useQuestionnaireSet = (() => {
  const createInstance = () => { // 新增问卷
    const modelRef = reactive<QuestionnaireSet>({
      id: '',
      title: '', // 问卷标题
      deadlineTime: '',
      parkOrgId: '',
      startTime: '',
      employeeIds: [], // 收集对象,员工
      companyIds: [], // 收集对象,公司
      coverPhoto: '', // 封面图片
      isChooseTarget: false,
      anonymous: null, // 是否匿名
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择所属园区' }],
      title: [{ required: true, message: '请选择问卷标题' }],
      isChooseTarget: [{
        validator: (rules:unknown, value:boolean) => {
          if (!value) return Promise.reject('请选择收集对象');
          return Promise.resolve();
        },
        required: true,
        // type: 'boolean',
        // message: '请选择收集对象',
      }],
      // deadlineTime: [{ required: true, message: '请选择生效开始时间' }],
      startTime: [{ required: true, message: '请选择生效开始时间' }],
      anonymous: [{ required: true, message: '请选择是否匿名' }],
      // coverPhoto: [{ required: true, message: '请上传封面图' }],
    });

    const fill = (defaultModelRef?: any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        // debugger;
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
