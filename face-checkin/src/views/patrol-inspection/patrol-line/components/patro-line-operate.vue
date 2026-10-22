<!--
 * @Description: 巡检路线操作
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-30 16:37:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-30 18:24:24
-->
<template>
  <a-form
    :label-col="{span:4}"
  >
    <a-form-item
      label="关联园区"
      v-bind="validateInfos.parkOrgId"
    >
      <v-select
        v-model:value="modelRef.parkOrgId"
        :disabled="type==='edit'"
        placeholder="请选择关联园区"
      >
        <a-select-option
          v-for="opt in parkList"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item
      label="路线名称"
      v-bind="validateInfos.circuitName"
    >
      <a-input
        v-model:value="modelRef.circuitName"
        :maxlength="30"
        placeholder="请输入路线名称"
      />
    </a-form-item>
    <a-form-item
      label="巡检类型"
      v-bind="validateInfos.type"
    >
      <a-select
        v-model:value="modelRef.type"
        placeholder="请选择巡检类型"
      >
        <a-select-option
          v-for="opt in inspectionTypes"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </a-select>
    </a-form-item>
    <a-form-item
      v-bind="validateInfos.orderly"
      label="路线顺序"
    >
      <a-radio-group v-model:value="modelRef.orderly">
        <a-radio :value="true">
          有序
        </a-radio>
        <a-radio :value="false">
          无序
        </a-radio>
      </a-radio-group>
    </a-form-item>
    <a-form-item label="备注">
      <a-textarea
        v-model:value="modelRef.remark"
        :rows="4"
        :maxlength="99"
        placeholder="请输入备注"
      />
    </a-form-item>
  </a-form>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, toRefs,
} from 'vue';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { PatrolLine } from '~models/patrol-inspection';
import Park from '~models/park';
import { useAddPatrolLine } from '../composition/useFormConf';

export default defineComponent({
  props: {
    defaultData: {
      type: Object as PropType<PatrolLine>,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  setup(props) {
    const { defaultData, type, inspectionTypes } = toRefs(props);
    const { modelRef, validateInfos, fill } = useAddPatrolLine();
    const parkList = ref<SelectOptions>();
    const init = async () => {
      parkList.value = await new Park().searchParkListName();
      if (type.value === 'edit') fill(defaultData.value);
    };
    init();
    return {
      modelRef,
      validateInfos,
      parkList,
    };
  },
});
</script>
