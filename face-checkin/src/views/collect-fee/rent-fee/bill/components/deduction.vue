<!--
 * @Description: 减免
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-25 16:08:16
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-05 15:50:21
-->
<template>
  <div class="batch-receive">
    <a-form-item label="入驻企业名称">
      {{ defaultData.companyName }}
    </a-form-item>
    <a-form-item label="收费项">
      {{ defaultData.chargeName }}
    </a-form-item>
    <a-form-item label="待收金额(元)">
      {{ defaultData.dueAmount }}
    </a-form-item>
    <a-form-item
      label="减免金额(元)"
      name="deductionAmount"
      :rules="deductionAmount"
    >
      <a-input
        v-model:value="modelRef.deductionAmount"
        placeholder="请输入减免金额"
      />
    </a-form-item>

    <a-form-item
      label="附件"
    >
      <v-upload
        v-model:value="modelRef.file"
        list-type="text"
        accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.png,.jpg,.jpeg,.gif,.zip"
        tip="支持20M以内的 .pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.png,.jpg,.jpeg,.gif,.zip格式文件"
        :size="20"
        :count="10"
      />
    </a-form-item>
    <a-form-item
      name="remark"
      label="备注"
    >
      <a-textarea
        v-model:value="modelRef.remark"
        show-count
        :maxlength="200"
      />
    </a-form-item>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs,
} from 'vue';
import { Dayjs } from 'dayjs';
import { REGS } from '@/constants/validate';
import {
  RentFee, RentFeeBillInfo, RentFeeBillDetail, BeforeCheckoutRentFee,
} from '~models/collect-fee';
import { useOperateBill } from '../composition/useForm';

const rentFeeInstance = new RentFee();

export default defineComponent({
  props: {
    defaultData: {
      type: Object as PropType<RentFeeBillInfo>,
      default: () => ({}),
    },
    selectKeys: {
      type: Array as PropType<string[]>,
      default: () => ([]),
    },
  },
  setup(props) {
    const { defaultData, selectKeys } = toRefs(props);
    const { modelRef } = useOperateBill();
    // const details = ref<BeforeCheckoutRentFee>();
    // const init = async () => {
    //   console.log(selectKeys, '----------selectKeys');
    //   // details.value = await rentFeeInstance.beforeBatchCheckoutRentFee(selectKeys.value);
    // };
    // init();
    return {
      // details,
      modelRef,
      disabledCheckoutDate: (e:Dayjs) => e.valueOf() > Date.now(),
      deductionAmount: [{
        validator: (rules:unknown, val:string) => {
          const value = modelRef.deductionAmount ?? 0;
          if (value === 0) return Promise.reject('请输入减免金额，只能输入大于0的数字，最多两位小数');
          if (!value) return Promise.reject('请输入减免金额，只能输入大于0的数字，最多两位小数');
          if (!REGS.area.test(String(value))) return Promise.reject('只能输入9位整数，最多两位小数');
          if (value > defaultData.value.dueAmount) return Promise.reject('减免金额不能大于待收金额');
          return Promise.resolve();
        },
        trigger: 'change',
        required: true,
      }],
    };
  },
});
</script>
