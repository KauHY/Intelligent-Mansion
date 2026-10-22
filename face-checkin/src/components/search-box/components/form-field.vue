<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-08-04 16:04:50
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-08 09:58:57
-->
<template>
  <div
    v-if="typeof fieldConf.hidden === 'function'? !fieldConf.hidden():!fieldConf.hidden"
    class="form-field"
  >
    <v-select
      v-if="fieldConf.tag==='select'"
      v-model:value="currentValue"
      :allow-clear="fieldConf.allowClear??true"
      :style="{width:fieldConf.width||''}"
      :show-search="showSearch"
      :filter-option="fieldConf.filterOption"
      :placeholder="currentPlaceholder"
      @change="fieldConf.handleChange"
    >
      <a-select-option
        v-for="opt in $sort(selectOptions)"
        :key="`${opt[labelKey]}`"
        :value="opt[valueKey]"
      >
        {{ opt[labelKey] }}
      </a-select-option>
    </v-select>
    <a-range-picker
      v-else-if="fieldConf.tag==='date-range'"
      v-model:value="currentValue"
      :style="{width:fieldConf.width||''}"
      :value-format="valueFormat"
      :placeholder="currentPlaceholder"
      @change="fieldConf.handleChange"
    />
    <a-date-picker
      v-else-if="fieldConf.tag==='month-picker'"
      v-model:value="currentValue"
      picker="month"
      :style="{width:fieldConf.width||''}"
      :placeholder="currentPlaceholder"
      value-format="YYYY-MM"
      @change="fieldConf.handleChange"
    />
    <a-date-picker
      v-else-if="fieldConf.tag==='date'"
      v-model:value="currentValue"
      :style="{width:'100%'}"
      :value-format="valueFormat"
      :placeholder="currentPlaceholder"
    />
    <a-input
      v-else
      v-model:value="currentValue"
      :style="{width:fieldConf.width||''}"
      allow-clear
      :placeholder="currentPlaceholder"
    />
  </div>
</template>
<script lang='ts'>
import {
  onMounted, defineComponent, computed, PropType, toRefs, reactive, toRef, getCurrentInstance,
} from 'vue';
import { SearchConfItem } from '../type';
// import type { PropType } from 'vue';

export default defineComponent({
  name: 'FormField',
  props: {
    value: {
      type: [String, Array, Number, Boolean],
      default: '',
    },
    fieldConf: {
      type: Object as PropType<NonNullable<SearchConfItem>>,
      default: () => ({
        labelKey: 'label',
        valueKey: 'value',
        placeholder: '请选择',
      }),
    },
  },
  emits: ['update:value'],
  setup(props, { emit }) {
    const defaultFieldConfProps = reactive({
      labelKey: 'label',
      valueKey: 'value',
      showSearch: false,
      valueFormat: 'YYYY-MM-DD',
    });
    const propRefs = { ...toRefs(defaultFieldConfProps), ...toRefs(props.fieldConf) };
    const {
      labelKey,
      valueKey,
      valueFormat,
      showSearch = false,
      placeholder,
      options,
      tag,
      filterOption,
    } = propRefs;
    const currentValue = computed({
      set(value) {
        emit('update:value', value);
        console.log(value, '--------value-----');
      },
      get() {
        return props.value === '' ? null : props.value;
      },
    });
    const selectOptions = computed(() => {
      // debugger;
      if (!options) return [];
      return typeof options.value === 'function' ? options.value() : options.value;
    });
    const currentPlaceholder = computed(() => {
      const defaultPlaceholder = {
        input: '请输入',
        select: '请选择',
        'date-range': ['开始日期', '结束日期'],
        'month-picker': '请选择月份',
        date: '请选择日期',
      };
      return placeholder?.value || defaultPlaceholder[tag.value];
    });

    // const currentFilterOption = computed(() => {
    //   if (!filterOption?.value) return true;
    //   // debugger;
    //   return (value:string, option:{key:string}) => option.key.includes(value);
    // });
    // console.log(currentFilterOption, '----currentFilterOption');
    // const currentFilterOption = (value:string, option:SelectOptions[0]) => {
    //   console.log(value, '-----asdfasdfasdf');
    //   return true;
    //   // return (option as any)[labelKey.value].includes(value);
    // };
    onMounted(() => {
    });
    return {
      currentValue,
      labelKey,
      valueKey,
      valueFormat,
      showSearch,
      selectOptions,
      currentPlaceholder,
    };
  },
});
</script>
<style lang="less" scoped>
.form-field {
    width: 260px;
    margin-bottom: 10px;
}
</style>
