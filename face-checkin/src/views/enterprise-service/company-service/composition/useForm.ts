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
import { LaneInfo } from '~models/company-service';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useLaneAdd = (() => {
  const createInstance = () => {
    const modelRef = reactive<LaneInfo>({
      id: '',
      name: '',
      pushRange: [],
      groupType: null,
      remark: '',
      summary: '',
      detail: '',
      url: '',
      tel: '',
      banner: '',
      att: '',

    });
    const valid = (rules:unknown, value:string) => {
      const pattern = /^[+]{0,1}(\d+)$/;
      if (!value) return Promise.resolve();
      if (!pattern.test(value)) return Promise.reject('请输入正确的电话号码或者手机号');
      return Promise.resolve();
    };
    const rulesRef = reactive({
      name: [{ required: true, message: '请输入服务名称', trigger: 'change' }],
      pushRange: [{ required: true, message: '请选择推送范围' }],
      groupType: [{ required: true, message: '请选择服务分组' }],
      detail: [{ required: true, message: '请输入服务详情' }],
      tel: [{ validator: valid, trigger: ['change', 'blur'] }],
      remark: [{ required: false, message: '超出限制字符长度(30)', max: 200 }],
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
