/*
 * @Author: Xucaizhi3
 * @Date: 2022-04-08 14:36:53
 * @LastEditTime: 2022-04-13 16:38:22
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { TerminationAndChange } from '~models/contract-manage';

export const tcForm = () => {
  const modelRef = reactive<TerminationAndChange>({
    id: '',
    changeType: null,
    withdrawDate: '',
    withdrawReason: '',

  });
  const rulesRef = reactive({
    changeType: { required: true, message: '请选择变更类型' },
    withdrawDate: { required: true, message: '请选择退约日期' },
    withdrawReason: { required: true, message: '请输入退约原因' },
  });
  return {
    modelRef,
    rulesRef,
  };
};
