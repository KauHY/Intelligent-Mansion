<template>
  <div class="menu-list content-section">
    &nbsp;&nbsp;<span>企业端登录地址</span>&nbsp;&nbsp;
    <span
      ref="copyValue"
    >{{ url }}</span>&nbsp;&nbsp;
    <a-button
      v-copy="url"
      type="primary"
    >
      一键复制
    </a-button>
  </div>
</template>
<script lang='ts'>
import {
  defineComponent,
} from 'vue';

import { UserInfo } from '~models/user';

export default defineComponent({
  name: 'MenuList',
  setup() {
    const { headOrgId } = window.$localStorage?.userInfo as UserInfo;
    const url = `${window.location.origin}${compileConf.publicPath}#/wisdom-park-company/login-company/${headOrgId}`;
    return {
      headOrgId,
      url,
    };
  },
});
</script>
