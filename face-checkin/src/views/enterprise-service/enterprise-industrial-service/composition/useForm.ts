/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-03-29 10:24:51
 */
import { onUnmounted, reactive } from 'vue';
import { REGS } from '@/constants/validate';
import { message } from 'ant-design-vue';
import useForm from '~utils/useForm';
import { LaneInfo } from '~models/enterprise-service';
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
      code: '',
      licenses: '',
      parkOrgId: null, // 园区ID
      parkOrgName: '',
      name: '', // 企业名称
      companyType: null, // 企业类型
      industryField: null, // 行业领域
      companyTaxesType: null, // 企业纳税类型
      enterpriseSize: null, // 企业规模
      atts: '', // 附件
      remark: '',
      corporateTax: null,
    });
    const rulesRef = reactive({
      industryField: [{ required: true, message: '请选择企业领域' }],
      companyTaxesType: [{ required: true, message: '请选择企业纳税类型' }],
      enterpriseSize: [{ required: true, message: '请选择企业规模' }],
      remark: [{ required: false, max: 200 }],
      corporateTax: [{ required: true, pattern: /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/, message: '请输入企业税收，只能是数字，最多8位，小数位最多2位' }],
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
