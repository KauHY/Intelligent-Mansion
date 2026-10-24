<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-27 16:09:41
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-21 18:00:09
-->
<template>
  <div class="demo">
    vue3 {{ userInfo }} {{ routerInitComplete }}
    <a-button @click="handleClick">
      点击 一下
    </a-button>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';
import { useUserStore } from '@/store/user';
import { storeToRefs } from 'pinia';

export default defineComponent({
  props: {
  },
  setup() {
    const store = useUserStore();
    const { userInfo, routerInitComplete } = storeToRefs(store);
    console.log(userInfo, store.setRouterInitComplete, '----userInfo');
    const handleClick = () => {
      store.setRouterInitComplete(true);
      console.log(routerInitComplete, '---baseMenuList');
    };
    return {
      userInfo,
      routerInitComplete,
      handleClick,
    };
  },
});
</script>
