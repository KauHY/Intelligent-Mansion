<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-11 16:02:47
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-06-11 16:38:47
-->
<template>
  <a-button
    :type="type"
    :loading="loading"
    @click.stop="$emit('click')"
  >
    <template #icon>
      <v-svg
        v-if="!!icon"
        :name="icon"
      />
    </template>
    <slot />
  </a-button>
</template>
<script lang='ts'>
import {
  reactive, toRefs, onMounted, defineComponent,
} from 'vue';
import type { PropType } from 'vue';

export default defineComponent({
  name: 'VButton',
  props: {
    icon: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: 'primary',
    },
    shape: {
      type: Boolean,
      default: false,
    },
    size: {
      type: String,
      default: 'default',
    },
    loading: {
      type: Boolean,
      default: false,
    },

  },
  emits: ['click'],
  setup() {

  },
});
</script>
