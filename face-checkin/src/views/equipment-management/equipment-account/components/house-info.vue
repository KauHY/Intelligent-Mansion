<template>
  <a-form
    :label-col="{span:4}"
  >
    <a-form-item
      v-if="dialogData.hierarchy!=='2' && dialogData.operateType!=='edit'"
      label="上级分类"
    >
      <span>{{ dialogData.floorName }}</span>
    </a-form-item>
    <a-form-item
      label="二级分类名称"
      name="name"
      v-bind="validateInfos.label"
    >
      <a-input
        v-model:value="modelRef.label"
        :maxlength="30"
        placeholder="请输入分类名称"
      />
    </a-form-item>
  </a-form>
</template>
<script lang="ts">
import {
  defineComponent,
} from 'vue';

import { useHouse } from '../composition/useHouse';

export default defineComponent({
  props: {
    dialogData: {
      type: Object,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
  },

  setup(props) {
    const {
      modelRef, validateInfos, validate, fill,
    } = useHouse(props.dialogData);
    const init = async () => {
      fill(modelRef, props.dialogData);
    };
    init();
    return {
      modelRef,
      validateInfos,
    };
  },
});
</script>
