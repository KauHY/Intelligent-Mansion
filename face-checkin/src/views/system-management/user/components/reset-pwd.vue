<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-09 15:25:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-29 12:12:06
-->
<template>
  <div class="add-user">
    <a-form
      :model="modelRef"
      :label-col="{span:5}"
    >
      <a-form-item
        label="用户名"
      >
        <span>{{ modelRef.username }}</span>
      </a-form-item>
      <a-form-item
        v-if="type !== 'edit'"
        label="新密码"
        v-bind="validateInfos.newPassword"
      >
        <a-input-password
          v-model:value="modelRef.newPassword"
          autocomplete="new-password"
          placeholder="至少8位数字、字母、符号两种或以上组合"
        />
      </a-form-item>
      <a-form-item
        v-if="type !== 'edit'"
        label="确认密码"
        v-bind="validateInfos.newPasswordAgain"
      >
        <a-input-password
          v-model:value="modelRef.newPasswordAgain"
          autocomplete="new-password"
          placeholder="请再次输入密码"
        />
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang='ts'>
import {
  defineComponent, toRefs, computed, onMounted,
} from 'vue';
import { useResetPwdForm } from '../config';

//   const organizationInstance = new Organization();
export default defineComponent({
  name: '',
  components: {},
  props: {
    defaultFormData: {
      type: Object,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const { defaultFormData } = toRefs(props);
    const {
      modelRef, validateInfos, fill,
    } = useResetPwdForm(defaultFormData.value);
    onMounted(() => {
      fill();
    });
    return {
      modelRef,
      validateInfos,
      // roleIds,
      // organizationOpts,
      // onLoadData,
    };
  },
});
</script>
