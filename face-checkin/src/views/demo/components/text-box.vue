<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-30 15:30:57
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-30 16:04:58
-->
<template>
  <div class="content-box">
    <slot />
    <div class="btns">
      <slot name="footer" />
    </div>
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';

export default defineComponent({
  props: {
  },
  setup() {
    return {
    };
  },
});
</script>
<style lang="scss">
.content-box {
    position: relative;
    height: 100%;
    padding: 24px;
    overflow: hidden;

    .btns {
        position: absolute;
        bottom: 0;
        left: 0;
        display: flex;
        flex-flow: column nowrap;
        justify-content: flex-end;
        width: 100%;
        height: 70vh;
        background-image: linear-gradient(to bottom, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 1));

        .ant-btn {
            width: 100%;
        }
    }
}
</style>
