<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-27 17:57:33
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-28 15:05:29
-->
<template>
  <a-form
    :model="modelRef"
    :label-col="{span:5}"
  >
    <a-form-item
      label="签出方式"
      v-bind="validateInfos.shiftOutType"
    >
      <v-select
        v-model:value="modelRef.shiftOutType"
        placeholder="请选择签出方式"
        @change="changeShiftOutType"
      >
        <a-select-option
          v-for="opt in shiftOutOpts"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item
      v-if="modelRef.shiftOutType==='2'"
      label="签出时间"
      v-bind="validateInfos.signOutTime"
    >
      <a-date-picker
        v-model:value="modelRef.signOutTime"
        value-format="YYYY-MM-DD"
        :disabled-date="disabledCheckoutDate"
        placeholder="请选择签出时间"
      />
    </a-form-item>
  </a-form>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, onMounted, toRefs,
} from 'vue';
import { Dayjs } from 'dayjs';
import { shiftOutOpts } from '../config';
import { useShiftOut } from '../composition/useForm';

export default defineComponent({
  name: '',
  components: {},
  props: {
    visitReasonOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    details: {
      type: Object,
      default: () => ({}),
    },
  },
  setup(props) {
    const { details } = toRefs(props);
    const { visitDate } = details.value;
    const endTimes = new Date(visitDate).getTime();
    const {
      modelRef,
      //   validate,
      validateInfos,
      fill,
    } = useShiftOut();
    const changeShiftOutType = async (value:any) => {
      if (value === 1) {
        modelRef.signOutTime = '';
      }
    };
    onMounted(() => {
      fill();
    });
    return {
      changeShiftOutType,
      disabledCheckoutDate: (e:Dayjs) => e.valueOf() < endTimes,
      modelRef,
      validateInfos,
      shiftOutOpts,
    };
  },
});
</script>
