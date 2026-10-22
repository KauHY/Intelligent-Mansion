/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-30 14:08:18
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { CompanyBindInfo } from '~models/visitors-management';
/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useCompanyBindInfo = (() => {
  const createInstance = () => {
    const modelRef = reactive<CompanyBindInfo>({
      id: '', // 公司id
      visitorClientValue: '',
      visitorClientType: 0, // 企业绑定访客客户端类型，0：无 1：小程序 2：超链接
      visitorClientPicUrl: '',
      parkOrgId: '',
    });
    const rulesRef = reactive({
      parkOrgId: [{ required: true, message: '请选择园区' }],
      id: [{ required: true, message: '请选择企业' }],
      visitorClientType: [{ required: true, message: '请选择链接类型' }],
      visitorClientValue: [{
        validator: (rules:unknown, value:string) => {
          if (modelRef.visitorClientType === 1 && !REGS.appId.test(value)) return Promise.reject('请输入正确的APPID');
          if (modelRef.visitorClientType === 2 && !REGS.linkUrl.test(value)) return Promise.reject('请输入正确的链接地址');
          return Promise.resolve();
        },
        required: true,
      }],
      visitorClientPicUrl: [{ required: true, message: '请上传封面图片' }],
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
