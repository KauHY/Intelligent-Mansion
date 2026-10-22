<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 18:10:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-10-12 17:15:29
-->
<template>
  <a-form
    :model="modelRef"
    :label-col="{span:3}"
  >
    <!-- </div> -->

    <a-form-item
      label="撤单原因"
      v-bind="validateInfos.cancelReason"
    >
      <v-select
        v-model:value="modelRef.cancelReason"
        placeholder="请选择撤单原因"
      >
        <a-select-option
          v-for="opt in cancelOrderReason"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
  </a-form>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, computed,
} from 'vue';
// import useOpts from '../composition/useOpts';
// import { useParkInfoOpts } from '~composition/park';
import { useCancelOrder } from '../composition/useForm';
import { orderTypeRepair, orderTypeComplaint, cancelOrderReason } from '../config';

export default defineComponent({
  name: '',
  components: {},
  props: {
    orderType: {
      type: Number as PropType<1|2>,
      default: 1,
    },
  },
  setup(props) {
    const { orderType } = toRefs(props);
    // const orderTypeOpts = computed(() => Object.values(orderType.value === 1 ? orderTypeComplaint : orderTypeRepair));
    // const { parkOpts, getParkOpts } = useParkInfoOpts();
    // getParkOpts();
    const {
      modelRef,
      validateInfos,
    } = useCancelOrder();
    return {
    //   parkOpts,
      modelRef,
      validateInfos,
      cancelOrderReason,
    };
  },
});
</script>
