/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:43:20
 * @LastEditTime: 2022-03-26 16:26:35
 * @LastEditors: xucaizhi3
 * @Description:
 */
/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:36:36
 * @LastEditTime: 2022-01-13 10:38:09
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import CompanyMangementVo, { CompanyCheckData } from '~models/company-mangement';

export const checkForm = (() => {
  const modelRef = reactive<CompanyCheckData>({
    id: '',
    auditMsg: '',
    auditStatus: null,
  });
  const rulesRef = reactive({
    auditStatus: { required: true, message: '请选择审核结果' },
    auditMsg: { required: true, message: '请输入审核意见' },
  });
  return {
    modelRef,
    rulesRef,
  };
});
