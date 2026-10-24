<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-01 18:39:20
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-14 10:06:39
-->
<template>
  <i :class="`qi-${name}`" />
</template>
<script lang='ts'>

import 'qweather-icons/font/qweather-icons.css';

import {
  reactive, ref, defineComponent, PropType,
} from 'vue';

export default defineComponent({
  props: {
    name: {
      type: String,
      default: '301',
    },
  },
  setup() {
    return {
    };
  },
});
</script>
<style lang="less" scoped>
i {
    display: inline-block;
    width: 24px;
    height: 24px;
    margin-right: 6px;
}
</style>
