<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-27 11:33:40
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-08-04 18:19:51
-->
<template>
  <div id="park-check-address-map" />
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, onMounted,
} from 'vue';
// import { mapLoad } from '~utils/map';

export default defineComponent({
  name: '',
  components: {},
  props: {
  },
  setup() {
    // const init = async () => {
    //   await mapLoad('park-check-address-map', {
    //     zoom: 12,
    //   });
    // };
    onMounted(() => {
      // init();
    });
    return {
    };
  },
});
</script>
