<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-08-02 09:42:22
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-02 10:25:01
-->
<template>
  <svg
    class="loading-icon"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    style="display: block; background: #fff;"
    width="200px"
    height="200px"
    viewBox="0 0 100 100"
    preserveAspectRatio="xMidYMid"
  >
    <circle
      cx="28"
      cy="75"
      r="11"
      fill="var(--ant-primary-7)"
    >
      <animate
        attributeName="fill-opacity"
        repeatCount="indefinite"
        dur="1s"
        values="0;1;1"
        keyTimes="0;0.2;1"
        begin="0s"
      />
    </circle>

    <path
      d="M28 47A28 28 0 0 1 56 75"
      fill="none"
      stroke="var(--ant-primary-6)"
      stroke-width="10"
    >
      <animate
        attributeName="stroke-opacity"
        repeatCount="indefinite"
        dur="1s"
        values="0;1;1"
        keyTimes="0;0.2;1"
        begin="0.1s"
      />
    </path>
    <path
      d="M28 25A50 50 0 0 1 78 75"
      fill="none"
      stroke="var(--ant-primary-4)"
      stroke-width="10"
    >
      <animate
        attributeName="stroke-opacity"
        repeatCount="indefinite"
        dur="1s"
        values="0;1;1"
        keyTimes="0;0.2;1"
        begin="0.2s"
      />
    </path>
  </svg>
</template>
<script>
</script>
<style lang='scss' scoped>
.loading-icon {
    width: 35px;
    height: 35px;
    margin-left: 10px;
}
</style>
