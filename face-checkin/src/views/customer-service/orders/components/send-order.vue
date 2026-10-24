<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 18:10:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 18:10:24
-->
<template>
  <a-form
    :model="modelRef"
    :label-col="{span:4}"
  >
    <a-form-item
      label="关联园区"
    >
      <span>{{ defaultData.parkOrgName }}</span>
    </a-form-item>

    <a-form-item
      label="选择物业公司"
      v-bind="validateInfos.companyId"
    >
      <v-select
        v-model:value="modelRef.companyId"
        placeholder="请选择物业公司"
        @change="companyChange"
      >
        <a-select-option
          v-for="opt in companyOpts"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>

    <a-form-item
      label="处理人"
      v-bind="validateInfos.handleUserId"
    >
      <v-select
        v-model:value="modelRef.handleUserId"
        show-search
        :disabled="!modelRef.companyId"
        placeholder="请输入姓名关键字搜索"
      >
        <a-select-option
          v-for="opt in employeesOpts"
          :key="opt.value"
          :label="opt.label"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item
      label="要求完成时间"
      v-bind="validateInfos.askFinishTime"
    >
      <a-date-picker
        v-model:value="modelRef.askFinishTime"
        value-format="YYYY-MM-DD"
        placeholder="请选择要求完成时间"
      />
    </a-form-item>
  </a-form>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, computed, onMounted,
} from 'vue';
import { useParkInfoOpts } from '~composition/park';
import useOpts from '../composition/useOpts';
import { useSendOrder } from '../composition/useForm';
import { orderTypeRepair, orderTypeComplaint, cancelOrderReason } from '../config';

export default defineComponent({
  name: '',
  components: {},
  props: {
    orderType: {
      type: Number as PropType<1|2>,
      default: 1,
    },
    defaultData: {
      type: Object,
      default: () => ({}),
    },
  },
  setup(props) {
    const { orderType, defaultData } = toRefs(props);
    const {
      companyOpts,
      companyChange,
      employeesOpts,
      // remoteSearch,
      loading,
    } = useOpts(defaultData.value.parkOrgId, orderType.value);
    // const orderTypeOpts = computed(() => Object.values(orderType.value === 1 ? orderTypeComplaint : orderTypeRepair));
    // const { parkOpts, getParkOpts } = useParkInfoOpts();
    // getParkOpts();
    const {
      modelRef,
      validateInfos,
      fill,
    } = useSendOrder(defaultData.value);
    console.log(defaultData.value, '---defaultData.value');
    onMounted(() => {
      fill();
    });
    return {
    //   parkOpts,
      modelRef,
      validateInfos,
      cancelOrderReason,
      companyOpts,
      companyChange,
      employeesOpts,
      // remoteSearch,
      loading,
    };
  },
});
</script>
