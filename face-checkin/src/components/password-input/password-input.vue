<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 10:10:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-07 12:21:55
-->
<template>
  <div class="password">
    <div
      :class="{
        'show-text':true,
        'show-placeholder':!currentPwd.length}"
    >
      {{ showPwdText }}<span
        v-show="isFocus"
        class="cursor-blink"
      >&nbsp;</span>
    </div>
    <div class="pwd-input-box">
      <a-input
        v-model:value="currentPwd"
        :type="InputType"
        class="pwd-input"

        @focus="isFocus=true"
        @blur="isFocus=false"
      />
      <!-- <template #suffix> -->
      <span
        class="operate-icon"
        @click="isShowPwdValue=!isShowPwdValue"
      >
        <v-svg
          v-if="isShowPwdValue"
          name="browse"
        />
        <v-svg
          v-else
          name="Notvisible1"
        />
      </span>
    <!-- </template> -->
    </div>
  </div>
</template>
<script lang='ts'>
import {
  ref, onMounted, defineComponent, computed,
} from 'vue';
import type { PropType } from 'vue';

export default defineComponent({
  name: 'Password',
  props: {
    value: {
      type: String as PropType<string>,
      default: '',
    },
    isRememberPwd: {
      type: Boolean,
      default: false,
    },
    placeholder: {
      type: String,
      default: '请输入密码',
    },
  },
  emits: ['update:value'],
  setup(props, { emit }) {
    const currentPwd = computed<string>({
      set(value) {
        emit('update:value', value);
      },
      get() {
        return props.value;
      },
    });
    const isShowPwdValue = ref(false); // 是否展示真实密码
    const isFocus = ref(false);
    const showPwdText = computed(() => {
      if (isShowPwdValue.value) return props.value;
      return props.value.length ? ''.padEnd(props.value.length, '*') : props.placeholder;
    });
    const InputType = computed(() => (props.isRememberPwd ? 'password' : 'text'));
    // const switchPwdShowMode = ()=>{} // 切换密码展示形式
    onMounted(() => {
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      currentPwd,
      showPwdText,
      InputType,
      isShowPwdValue,
      isFocus,
    };
  },
});
</script>
<style lang="less">
.password {
    .show-text {
        width: 100%;
        height: 40px;
        padding-left: 18px;
        line-height: 40px;

        &.show-placeholder {
            color: #d9d9d9;

            .cursor-blink {
                position: absolute;
                top: 0;
                left: 26px;
            }
        }
    }

    .pwd-input-box {
        position: absolute;
        top: 0;
        left: 0;
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 100%;

        .pwd-input {
            height: 40px;
            line-height: 40px;
            color: transparent;
            background-color: transparent;
            border: none;
            opacity: 0;
        }

        .operate-icon {
            cursor: pointer;
        }
    }

    .cursor-blink {
        display: inline-block;
        width: 1px;
        animation: blink 1s infinite steps(1, start);
    }

    /* 这里设置动画blink */
    @keyframes blink {
        0%,
        100% {
            color: #aaa;
            background-color: transparent;
        }

        50% {
            color: #000;
            background-color: #000; /* not #aaa because it's seem there is Google Chrome bug */
        }
    }

    .ant-input-affix-wrapper:focus,
    .ant-input-affix-wrapper-focused,
    .ant-input:focus {
        box-shadow: none;
    }
}
</style>
