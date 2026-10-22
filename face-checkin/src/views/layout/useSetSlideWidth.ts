/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-04 16:04:04
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 11:31:15
 */

import {
  ref, computed, onMounted, onBeforeUnmount,
} from 'vue';
//
import { useCommonStore } from '@/store/common';

let timeId:NodeJS.Timeout;
export const useSetSlideWidth = () => {
  const collapsedWidth = ref(60);
  const slideWidth = ref(256);
  const commonStore = useCommonStore();
  const collapsed = computed({
    get() {
      return commonStore.collapsed;
    },
    set(value:boolean) {
      // store.dispatch('common/setCollapsed', value);
      commonStore.setCollapsed(value);
    },
  });
  const setSlideWidth = () => {
    const { innerWidth } = window;
    console.log('innerWidth', innerWidth);
    if (innerWidth < 992) {
      collapsed.value = true; // 收起导航
      collapsedWidth.value = innerWidth < 768 ? 0 : 60;
      slideWidth.value = 256;
    } else {
      collapsed.value = false;
      collapsedWidth.value = 60;
      if (innerWidth >= 1680) {
        slideWidth.value = 244;
      } else if (innerWidth >= 1440) {
        slideWidth.value = 208;
      } else {
        slideWidth.value = 188;
      }
    }
  };
  const handleResize = () => {
    clearTimeout(timeId);
    timeId = setTimeout(() => {
      setSlideWidth();
    });
  };
  onMounted(() => {
    setSlideWidth();
    window.addEventListener('resize', handleResize);
  });
  onBeforeUnmount(() => {
    clearTimeout(timeId);
    window.removeEventListener('resize', handleResize);
  });
  return {
    collapsedWidth,
    slideWidth,
    collapsed,
  };
};
