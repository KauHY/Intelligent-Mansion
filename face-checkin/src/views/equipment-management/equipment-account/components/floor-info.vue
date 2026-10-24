<template>
  <a-form
    :label-col="{span:4}"
  >
    <a-form-item
      label="一级分类名称"
      name="label"
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
import Building from '~models/building';
import { useAddFloor } from '../composition/useAddFloor';

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
      modelRef, validateInfos,
    } = useAddFloor(props.dialogData);
    console.log(props.dialogData, 'props.dialogData');
    const init = async () => {
    };
    init();
    return {
      modelRef,
      validateInfos,
    };
  },
});
</script>
