<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-01 10:34:10
 * @LastEditors: guochao82 guochao82@crc.com.hk
 * @LastEditTime: 2022-11-30 17:45:15
-->
<template>
  <div
    class="process-bar"
  >
    <div class="bg">
      <div
        v-for="i in totalNum"
        :Key="i"
      />
    </div>
    <div
      class="activited"
      :style="{width:`${percent}%`,background}"
    >
      <div
        v-for="i in activeNum"
        :Key="i"
      />
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, computed,
} from 'vue';

export default defineComponent({
  props: {
    background: {
      type: String,
      default: 'linear-gradient(270deg, #D74177 0%, #FFE98A 100%)',
    },
    percent: {
      type: Number,
      default: 0,
    },
  },
  setup(props) {
    const totalNum = 50;
    const activeNum = computed(() => {
      const percent = props.percent / 100 || 0;
      return Math.floor(totalNum * percent);
    });
    return {
      totalNum,
      activeNum,
    };
  },
});
</script>
<style lang="scss" scoped>
.process-bar {
    position: relative;

    >div {
        display: flex;
        justify-content: space-between;

        >div {
            flex: 1;
            height: h(12);
            border-right: 3px solid #20236c;
        }

        &.bg {
            >div {
                background: rgba(31, 42, 73, 0.6);
            }
        }

        &.activited {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
        }
    }
}
</style>
