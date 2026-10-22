<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-22 15:01:02
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 18:04:41
-->
<template>
  <div class="employee-select">
    <v-select
      v-model:value="currentValue"
      :mode="mode"
      :disabled="disabled"
      :filter-option="false"
      show-search
      allow-clear
      placeholder="请输入员工姓名称搜索"
      @search="remoteSearch($event)"
    >
      <template
        v-if="loading"
        #notFoundContent
      >
        <a-spin size="small" />
      </template>
      <a-select-option
        v-for="opt in list"
        :key="opt.value"
        @click="clickOpt(opt)"
      >
        {{ getLabelText(opt) }}
      </a-select-option>
    </v-select>
  </div>
</template>
<script lang='ts'>
import {
  nextTick, ref, defineComponent, PropType, toRefs, computed, onMounted, watch,
} from 'vue';
import { useRemoteFilterSelect } from '~composition/common';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';

type Value =string|number
export default defineComponent({
  name: 'EmployeeSelect',
  props: {
    value: {
      type: [Array, String] as PropType<string|string[]>,
      default: '',
    },
    mode: {
      type: String as PropType<'multiple' | 'tags' | 'combobox'>,
      default: undefined,
    },
    defaultOptions: {
      type: Array as PropType<SelectOptions>,
      default: () => ([]),
    },
    companyType: {
      type: [Number, String] as PropType<0|1|''>,
      default: '',
    },
    jobCodes: {
      type: Array as PropType<string[]>,
      default: () => ([]),
    },
    companyId: {
      type: String,
      default: '',
    },
    orgId: {
      type: String,
      default: '',
    },

    disabled: {
      type: Boolean,
      default: false,
    },
    labelJoinKey: {
      type: String as PropType<''|'companyName'|'mobile'>,
      default: '',
    },
  },
  emits: {
    'update:value': (userId:Value|Value[]) => true,
    change: (value:string|string[], options:SelectOptions[number]) => true,
  },
  setup(props, { emit }) {
    const {
      value, defaultOptions, companyId, orgId, jobCodes, companyType, disabled, labelJoinKey,
    } = toRefs(props);

    const currentValue = computed({
      set(value: Value|Value[]) {
        emit('update:value', value);
      },
      get() {
        return value.value as Value|Value[];
      },
    });
    const getLabelText = (opt:SelectOptions[number]) => {
      const { label, extraInfo } = opt;
      if (!labelJoinKey.value || !extraInfo) return opt.label;
      return `${label}/${extraInfo[labelJoinKey.value]}`;
    };
    const searchOtherParams = computed(() => ({
      orgId: orgId.value || '',
      companyType: companyType.value,
      jobCodes: jobCodes.value || '',
      companyId: companyId.value || '',
    }));
    const {
      loading,
      list,
      remoteSearch,
      otherParams,
      setDefaultOptios,
    } = useRemoteFilterSelect(new CompanyMangement().selectEmployeesList, {
      disableAutoSearch: true,
    });
    const clickOpt = (option:SelectOptions[number]) => {
      nextTick(() => {
        if (option.value === value.value) return;
        emit('change', value.value, option);
      });
    };

    watch(searchOtherParams, (value) => {
      otherParams.value = { ...value };
      console.log(value, '---searchOtherParams');
    }, { immediate: true });

    onMounted(() => {
      console.log(defaultOptions.value, '----defaultOptions.value');
      setDefaultOptios(defaultOptions.value);
      console.log(list, '----list');
    });
    return {
      currentValue,
      loading,
      //   list,
      //   loading,
      list,
      remoteSearch,
      // currentDisabled,
      getLabelText,
      clickOpt,
    };
  },
});
</script>
