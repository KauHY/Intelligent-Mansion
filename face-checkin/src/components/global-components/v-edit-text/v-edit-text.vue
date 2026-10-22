<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-23 13:49:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-28 20:45:02
-->
<template>
  <div
    class="v-edit-text"
    :style="{display}"
  >
    <span
      v-if="!isEdit||currentDisabled"
      @click="isFocus=true"
    >{{ currentValue||'请输入内容' }}</span>
    <a-input
      v-else
      v-model:value="currentValue"
      v-focus
      style="width: auto;"
      placeholder="请输入内容"
      @blur="isFocus=false"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, computed, toRefs, inject, Ref,
} from 'vue';

export default defineComponent({
  name: 'VEditText',
  props: {
    value: {
      type: String,
      default: '',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    display: {
      type: String,
      default: 'inline-block',
    },
  },
  emits: {
    'update:value': (value:string) => true,
  },
  setup(props, { emit }) {
    const { value, disabled } = toRefs(props);
    const currentValue = computed({
      set(value:string) {
        emit('update:value', value);
      },
      get() {
        return value.value;
      },
    });
    const parentDisabled = inject<Ref<boolean>>('parentDisabled');
    const currentDisabled = computed(() => disabled.value || parentDisabled?.value);
    // console.log(parentDisabled, '---parentDisabled');
    // const isEdit = ref(false)
    const isFocus = ref(false);
    const isEdit = computed(() => !disabled.value && isFocus.value);
    return {
      currentValue,
      isFocus,
      isEdit,
      currentDisabled,
    };
  },
});
</script>
<style lang='scss' scoped>
.v-edit-text {
    >span {
        cursor: pointer;
    }
}
</style>
