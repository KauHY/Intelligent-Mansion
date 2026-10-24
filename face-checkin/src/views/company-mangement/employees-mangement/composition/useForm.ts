/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-29 17:37:48
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { EmployeeInfo } from '~models/company-mangement';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddEmployees = (() => {
  const createInstance = (defaultModelRef?: any) => {
    const modelRef = reactive<EmployeeInfo>({
      companyId: '', // 所属入驻企业ID
      companyName: '', // 企业名称
      id: '',
      jobCode: [], // 岗位名称
      jobIds: [], // 岗位id
      mobile: '',
      name: '', // 员工姓名
      parkOrgId: '', // 园区id
      parkOrgName: '', // 园区名称
      pics: '', // 照片
      status: 1, // 状态
      tagIds: [], // 员工标签 id
      tagNames: [], // 员工标签
      remark: '',
    });
    const rulesRef = reactive({
      name: [{ required: true, message: '用户名不能为空' }],
      mobile: [{
        validator: (rules:unknown, value:string) => {
          if (!REGS.mobile.test(value)) return Promise.reject('请输入正确手机号');
          return Promise.resolve();
        },
        required: true,
      }],
      pics: [{ required: true, message: '请上传照片' }],
      parkOrgId: [{ required: true, message: '请选择关联园区' }],
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
