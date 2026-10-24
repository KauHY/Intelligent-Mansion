<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-14 15:29:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 13:54:47
-->
<template>
  <a-config-provider
    :locale="locale"
    :form="formConf"
    :get-popup-container="getPopupContainer"
  >
    <router-view />
  </a-config-provider>
</template>
<script>
import { defineComponent } from 'vue';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import { useSwitchTheme } from '@/theme';

dayjs.locale('zh-cn');
// console.log(zhCN, '----zhCN');
export default defineComponent({
  setup() {
    const getPopupContainer = (el, dialogContext) => {
      if (dialogContext) {
        return dialogContext.getDialogWrap();
      }
      return document.body;
    };
    // const currentThemeKey = window.$localStorage.getItem('themeColor') || '#409EFF';
    const { currentThemeColor, switchTheme } = useSwitchTheme();
    switchTheme(currentThemeColor.value, true);
    return {
      locale: zhCN,
      getPopupContainer,
      formConf: {
        scrollToFirstError: true,
      },
    };
  },
});
</script>
