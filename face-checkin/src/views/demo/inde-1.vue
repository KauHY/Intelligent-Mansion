<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-01 17:22:35
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-08 10:51:40
-->
<template>
  <div class="demo">
    <a-button v-copy>
      <span>asdfasdfasdfasfd</span>
    </a-button>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';

export default defineComponent({
  props: {
  },
  setup() {
    const data = ref<string>('start');
    setTimeout(() => {
      data.value = 'end';
    }, 5000);
    return {
      data,
    };
  },
});
</script>
