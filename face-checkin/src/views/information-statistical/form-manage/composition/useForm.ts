/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-01 15:08:39
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { FormManageInfo } from '~models/information-statistical';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useFormManage = (() => {
  const createInstance = () => { // 新增问卷
    const modelRef = reactive<FormManageInfo>({
      id: '',
      title: '',
      parkOrgId: '',
      templateId: '', // 模板id
      companyIds: [],
      employeeIds: [], // 收集对象,公司
      cycle: null, // 周期，1每日、2每周、3每月、4自定义
      reminded: false, // 是否提醒，false不提醒，1true
      description: '', // 描述
      deadlineNum: null, // 截止时间的周几或第几天
      deadlineTime: '', // 截止时间
      isChooseTarget: false,
      updateTime: '',
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择所属园区' }],
      title: [{ required: true, message: '请输入表单名称' }],
      isChooseTarget: [{
        validator: (rules:unknown, value:boolean) => {
          if (!value) return Promise.reject('请选择收集对象');
          return Promise.resolve();
        },
        required: true,
        // type: 'boolean',
        // message: '请选择收集对象',
      }],
      deadlineTime: [{ required: true, message: '请选择有效时间' }],
      cycle: [{ required: true, message: '请选择收集周期' }],
      // reminded: [{ required: true, message: '请选择提交提醒' }],
      templateId: [{ required: true, message: '请选择表单模板' }],
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
