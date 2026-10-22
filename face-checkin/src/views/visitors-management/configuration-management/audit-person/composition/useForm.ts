/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-31 16:34:56
 */
import { onUnmounted, reactive, Ref } from 'vue';
import { REGS } from '@/constants/validate';
import { number } from 'mathjs';
import useForm from '~utils/useForm';
import { AuditPerson } from '~models/visitors-management';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAuditPerson = (() => {
  const createInstance = (type:Ref<'add'|'edit'>) => {
    const modelRef = reactive<AuditPerson>({
      id: '',
      companyId: '', // 公司id
      // auditEmployeeId: '', // 员工id
      invitorEmployeeAudit: 1,
      parkOrgId: '',
      auditEmployeeIds: [],
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择关联园区' }],
      companyId: [{ required: true, message: '请选择所属企业' }],
      invitorEmployeeAudit: [{ required: true, message: '请选审批人', type: 'number' }],
      auditEmployeeIds: [{
        validator(rules:unknown, value:string[]) {
          // debugger;
          if (modelRef.invitorEmployeeAudit === 0 && !value.length) return Promise.reject('请选择审批人');
          return Promise.resolve();
        },
        required: true,
      }],
    });

    const fill = (defaultModelRef?: any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
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
  return (type:Ref<'add'|'edit'>) => {
    if (!userFormInstance) {
      userFormInstance = createInstance(type);
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
