<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 18:08:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-26 15:38:01
-->
<template>
  <svg
    class="v-svg-icon"
    :style="{fill:color}"
    aria-hidden="true"
  >
    <use :xlink:href="`#icon-${name}`" />
  </svg>
</template>
<script>
export default {
  name: 'VSvg',
  props: {
    name: {
      type: String,
      default: '',
    },
    color: {
      type: String,
      default: 'currentColor',
    },
  },
};
</script>
<style lang="scss">
.v-svg-icon {
    width: 21px;
    min-width: 12px;
    height: 21px;
    min-height: 12px;
    overflow: hidden;
    vertical-align: -0.78vh !important;
}
</style>
