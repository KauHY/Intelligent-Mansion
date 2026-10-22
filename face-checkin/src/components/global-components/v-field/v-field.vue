<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-08-04 16:04:50
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-21 10:58:54
-->
<template>
  <div
    v-if="!fieldConf.hidden"
    class="form-field"
  >
    <a-select
      v-if="fieldConf.tag==='select'"
      v-model:value="currentValue"
      allow-clear
      :disabled="currentDisabled"
      :show-search="showSearch"
      :filter-option="fieldConf.filterOption"
      :placeholder="currentPlaceholder"
      @change="fieldConf.onChange"
    >
      <a-select-option
        v-for="opt in selectOptions"
        :key="`${opt[labelKey]}_${opt[valueKey]}`"
        :value="opt[valueKey]"
      >
        {{ opt[labelKey] }}
      </a-select-option>
    </a-select>
    <a-range-picker
      v-else-if="fieldConf.tag==='date-range'"
      v-model:value="currentValue"
      :disabled="currentDisabled"
      :value-format="valueFormat"
      :placeholder="currentPlaceholder"
    />
    <a-input
      v-else
      v-model:value="currentValue"
      :disabled="currentDisabled"
      allow-clear
      :placeholder="currentPlaceholder"
    />
  </div>
</template>
<script lang='ts'>
import {
  onMounted, defineComponent, computed, PropType, toRefs,
} from 'vue';
// import type { PropType } from 'vue';

export default defineComponent({
  name: 'VField',
  props: {
    value: {
      type: [String, Array, Number],
      default: '',
    },
    fieldConf: {
      type: Object as PropType<FormFieldConf>,
      default: () => ({
        labelKey: 'label',
        valueKey: 'value',
        placeholder: '请选择',
      }),
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  emits: ['update:value', 'change'],
  setup(props, { emit }) {
    // const {fieldConf} = props
    const {
      labelKey = 'label',
      valueKey = 'value',
      valueFormat = 'YYYY-MM-DD HH:mm:ss',
      showSearch = false,
      placeholder,
      formLabel,
      options,
      disabled,
      tag,
    } = toRefs(props.fieldConf);
    const currentValue = computed({
      set(value) {
        emit('update:value', value);
        emit('change', value); // 由于ant-design 的骚操作，必须要出发chage时间
      },
      get() {
        return props.value || props.value === 0 ? props.value : null;
      },
    });
    const selectOptions = computed(() => {
      // debugger;
      if (!options) return [];
      return typeof options.value === 'function' ? options.value() : options.value;
    });
    const currentPlaceholder = computed(() => {
      const defaultPlaceholder = {
        input: formLabel.value ? `请输入${formLabel.value}` : '请输入',
        select: formLabel.value ? `请选择${formLabel.value}` : '请选择',
        'date-range': ['开始日期', '结束日期'],
        'month-picker': '请选择',
        date: '请选择日期',
      };
      return placeholder?.value || defaultPlaceholder[tag.value];
    });
    const currentDisabled = computed(() => (props.disabled !== undefined ? props.disabled : disabled?.value));
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
      currentDisabled,
    };
  },
});
</script>
