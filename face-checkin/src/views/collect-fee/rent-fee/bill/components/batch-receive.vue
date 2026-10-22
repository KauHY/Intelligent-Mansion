<!--
 * @Description: 批量收款
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-25 14:54:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-25 16:21:17
-->
<template>
  <div class="batch-receive">
    <a-form-item label="收款方式">
      线下银行转账
    </a-form-item>
    <a-form-item label="待收金额(元)">
      {{ details?.dueAmount }}
    </a-form-item>
    <a-form-item label="待收滞纳金(元)">
      {{ details?.receiveFine }}
    </a-form-item>
    <a-form-item label="待收总金额(元)">
      {{ details?.totalAmount }}
    </a-form-item>
    <a-form-item
      label="收款日期"
      name="checkoutDate"
    >
      <a-date-picker
        v-model:value="modelRef.checkoutDate"
        :disabled-date="disabledCheckoutDate"
        value-format="YYYY-MM-DD"
        placeholder="请选择收款日期"
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
    const details = ref<BeforeCheckoutRentFee>();
    const init = async () => {
      console.log(selectKeys, '----------selectKeys');
      details.value = await rentFeeInstance.beforeBatchCheckoutRentFee(selectKeys.value);
    };
    init();
    return {
      details,
      ...useOperateBill(),
      disabledCheckoutDate: (e:Dayjs) => e.valueOf() > Date.now(),
    };
  },
});
</script>
