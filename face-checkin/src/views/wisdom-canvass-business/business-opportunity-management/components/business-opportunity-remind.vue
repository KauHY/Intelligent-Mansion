<!--
 * @Description: 商机提醒
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 18:10:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-08 16:30:28
-->
<template>
  <div class="business-opportunity-add">
    <a-form
      :model="modelRef"
      :label-col="{span:4}"
    >
      <a-form-item
        v-bind="validateInfos.remindFrequency"
        label="提醒频次"
      >
        <v-select
          v-model:value="modelRef.remindFrequency"
          placeholder="请选择提醒频次"
          @change="remindFrequencyChange"
        >
          <a-select-option
            v-for="opt in remindTypeOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        v-if="modelRef.remindFrequency===1"
        v-bind="validateInfos.remindByDay"
        label="按天提醒"
      >
        <a-input-number
          v-model:value="modelRef.remindByDay"
          style="width: 50%;"
          placeholder="请输入提醒天数"
          :min="1"
        />
      </a-form-item>
      <a-form-item
        v-if="modelRef.remindFrequency===2"
        v-bind="validateInfos.remindByWeek"
        label="按周提醒"
      >
        <v-select
          v-model:value="modelRef.remindByWeek"
          mode="multiple"
          allow-clear
          placeholder="请选择"
        >
          {{ modelRef.remindByWeek.includes(0) }}
          <a-select-option
            v-for="opt in remindByWeekOpts"
            :key="opt.value"
            :disabled="getDisabledRemindByWeekOpt(opt)"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        v-bind="validateInfos.remindTime"
        label="提醒时间"
      >
        <a-time-picker
          v-model:value="modelRef.remindTime"
          value-format="HH:mm:ss"
          style="width: 50%;"
          placeholder="请选择提醒时间"
        />
      </a-form-item>
      <a-form-item
        v-bind="validateInfos.remindContent"
        label="提醒内容"
      >
        <a-textarea
          v-model:value="modelRef.remindContent"
          :maxlength="999"
          placeholder="请输入提醒内容"
        />
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang='ts'>
import {
  ref, defineComponent, PropType, toRefs,
} from 'vue';
import { BusinessInfoFollow } from '~models/wisdom-canvass-business';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { remindTypeOpts, remindByWeekOpts } from '../config';
import { useRemind } from '../composition/useFormConf';

export default defineComponent({
  // components: { employeeSelect },
  props: {
    orderType: {
      type: Number as PropType<1|2>,
      default: 1,
    },
    defaultData: {
      type: Object as PropType<BusinessInfoFollow>,
      default: () => ({}),
    },
  },
  setup(props) {
    const { defaultData } = toRefs(props);
    // const {} = useParkInfoOpts();
    const defaultOptions = ref<SelectOptions>([]);
    // const recordType = computed(() => defaultData.value.recordType);
    const options = ref<DictionaryMap<['BUSINESS_CHANCE_STAGE', 'BUSINESS_FOLLOW_TYPE']>>({
      BUSINESS_CHANCE_STAGE: [],
      BUSINESS_FOLLOW_TYPE: [],
    });
    const {
      modelRef,
      validateInfos,
      // fill,
    } = useRemind();
    const init = async () => {
      options.value = await useHashMapDic<['BUSINESS_CHANCE_STAGE', 'BUSINESS_FOLLOW_TYPE']>(['BUSINESS_CHANCE_STAGE', 'BUSINESS_FOLLOW_TYPE']);
      modelRef.id = defaultData.value.id;
      // fill(props.defaultData);
    };
    const getDisabledRemindByWeekOpt = (opt:{value:number}) => {
      // if(opt.value===0&&modelRef.remindByWeek.i)
      if (!modelRef.remindByWeek.length) return false;
      if (opt.value === 0) {
        return !modelRef.remindByWeek.includes(0);
      }
      return modelRef.remindByWeek.includes(0);
    };
    const remindFrequencyChange = (value:number) => {
      if (value === 1) {
        modelRef.remindByWeek = [];
        return;
      }
      modelRef.remindByDay = null;
    };
    init();
    // onMounted(() => {
    // });
    return {
    //   parkOpts,
      modelRef,
      validateInfos,
      defaultOptions,
      // recordType,
      options,
      remindTypeOpts,
      remindByWeekOpts,
      getDisabledRemindByWeekOpt,
      remindFrequencyChange,
    };
  },
});
</script>
<style lang="scss" scoped>
.business-opportunity-add {
    :deep(.ant-row) {
        display: flex;
        flex-wrap: nowrap;
    }
}
</style>
