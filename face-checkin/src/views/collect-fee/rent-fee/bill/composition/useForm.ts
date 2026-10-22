/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-25 16:29:32
 */
import { onUnmounted, reactive, Ref } from 'vue';
import { REGS } from '@/constants/validate';
import dayjs from 'dayjs';
import useForm from '~utils/useForm';
// import {  } from '~models/collect-fee';
/**
 * @description: 编辑/收款
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */

 interface RentFeeBillModifyParams { // 操作账单
    checkoutDate:string, // 收款日期
    ids:string[], // 账单id
    remark:string, // 备注
    accountPeriod:string, // 所在账期
    id:string,
    file:string
    deductionAmount:number|null
  }

export const useOperateBill = (() => {
  const createInstance = () => { // 新增问卷
    const modelRef = reactive<RentFeeBillModifyParams>({
      checkoutDate: dayjs().format('YYYY-MM-DD'),
      ids: [],
      remark: '',
      accountPeriod: '',
      id: '',
      file: '',
      deductionAmount: null,
    });
    const rulesRef = reactive({
      checkoutDate: [{ required: true, message: '请选择收款日期' }],
      accountPeriod: [{ required: true, message: '请选择所在账期' }],
    });
    return {
      modelRef,
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
