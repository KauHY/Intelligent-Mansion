<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-28 14:06:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-28 15:54:55
-->
<template>
  <span>
    <a-image
      v-for="(url,index) in urls"
      :key="index"
      :width="width"
      :height="height"
      :src="url"
      :class="{'fixed-img-size': fixedSize}"
      :fallback="fallbackUrl"
    />

  </span>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, computed, toRefs,
} from 'vue';
import { getPreviewUrl } from '~models/common';

export default defineComponent({
  name: 'VImage',
  props: {
    keys: {
      type: String,
      default: '',
    },
    width: {
      type: [Number, String],
      default: 200,
    },
    height: {
      type: [Number, String],
      default: 200,
    },
    src: {
      type: String,
      default: undefined,
    },
    fixedSize: { // 固定图片尺寸
      type: Boolean,
      default: true,
    },
  },
  setup(props) {
    const { keys, src } = toRefs(props);
    const urls = computed(() => {
      // let urls:string[] = []
      if (!src.value && !keys.value) return [];
      if (src.value !== undefined) return [src.value];
      // if (!keys.value) return [];
      return keys.value.split(',').map((key) => getPreviewUrl(key));
    });
    const fallbackUrl = `${compileConf.publicPath}static/images/image_load_error.png`;
    return {
      urls,
      fallbackUrl,
    };
  },
});
</script>
<style lang='scss' scoped>
span {
    cursor: pointer;

    :deep(.fixed-img-size) {
        height: 100%;
    }
}
</style>
