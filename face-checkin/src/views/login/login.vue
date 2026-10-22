<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 10:07:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 13:58:26
-->
<template>
  <div class="login-box">
    <!-- <img
      class="logo-icon"
      src="~assets/images/login/logo_icon.png"
      alt=""
    > -->
    <div class="login-content">
      <div class="system-title">
        <!-- <img
          src="~assets/images/login/logo.png"
          alt=""
        > -->
        智慧园区运营管理平台
      </div>
      <a-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        @keyup.enter="login"
      >
        <a-form-item
          name="username"
        >
          <a-input
            v-model:value.trim="formData.username"
            placeholder="请输入用户名"
          />
        </a-form-item>
        <a-form-item
          name="password"
        >
          <a-form-item-rest
            v-show="false"
            v-model:value.trim="formData.password"
            style="display: none;"
            type="password"
          />
          <a-input-password
            v-model:value.trim="formData.password"
            :autocomplete="isRememberPassWord?'on':'new-password'"
            placeholder="请输入密码"
          />
        </a-form-item>
        <a-form-item
          name="validateCode"
        >
          <a-input
            v-model:value.trim="formData.validateCode"
            autocomplete="off"
            placeholder="请输入验证码"
          />
          <img
            :src="imageCodeUrl"
            @click="getImageInfo"
          >
        </a-form-item>
      </a-form>
      <a-button
        type="primary"
        @click="login"
      >
        登录
      </a-button>
      <div class="operate-tool">
        <a-checkbox v-model:checked="isRememberPassWord">
          记住密码
        </a-checkbox>
        <a-button
          type="link"
          @click="jumpPage"
        >
          忘记密码?
        </a-button>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted,
} from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';

import User, { LoginFormData } from '~models/user';
// import { getImageCode } from '~models/common/common';
import { useImageCode } from '~composition/common';
import passwordInput from '~components/password-input/password-input.vue';
// import SearchModel from '~models/park/park';
// const searchInstace = new SearchModel();
const userInstance = new User();
export default defineComponent({
  // components: { passwordInput },
  setup() {
    const formRef = ref();
    const router = useRouter();
    const formData = reactive({
      password: '',
      username: '',
      validateCode: '',
      validateCodeKey: '',
      isRememberPassWord: window.$localStorage.isRememberPassWord,
    });
    const password = computed(() => ''.padEnd(formData.password.length, '*'));
    const isRememberPassWord = computed({
      set(value) {
        const isRememberPassWord = value ? '1' : '0';
        formData.isRememberPassWord = isRememberPassWord;
        window.$localStorage.isRememberPassWord = isRememberPassWord;
      },
      get() {
        return formData.isRememberPassWord === '1';
      },
    });
    const rules = {
      username: { required: true, message: '请输入用户名', trigger: 'change' },
      password: { required: true, message: '请输入密码', trigger: 'change' },
      validateCode: { required: true, message: '请输入图形验证码', trigger: 'change' },
    };
    // 验证码 Not visible browse
    const { imageCodeUrl, getImageInfo } = useImageCode(
      () => {
        formData.validateCode = '';
      },
      (url:string, key:string) => {
        formData.validateCodeKey = key;
      },
    );
    const login = async () => {
      formRef.value.validate().then(async () => {
        try {
          console.log(formData.password, 'password');
          const userInfo = await userInstance.login(formData);
          window.$localStorage.Authorization = userInfo.accessToken;
          window.$localStorage.userInfo = userInfo;
          window.$localStorage.removeItem('isCompany');
          router.replace('/');
        } catch (error) {
          getImageInfo();
        }
      }).catch(() => {
        message.warning('操作失败');
      });
    };
    const jumpPage = () => {
      router.push({ name: 'resetPassword' });
    };
    onMounted(() => { // 进入登录页面移除token
      window.$localStorage.removeItem('Authorization');
    });
    getImageInfo();
    return {
      formRef,
      login,
      getImageInfo,
      imageCodeUrl,
      formData,
      password,
      rules,
      jumpPage,
      isRememberPassWord,
    };
  },
});
</script>
<style lang="scss">
.login-box {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background: url('assets/images/login/bg.png') no-repeat;
    background-size: cover;

    .logo-icon {
        position: absolute;
        top: 39px;
        left: 40px;
        width: 207px;
        height: 27px;
    }

    .login-content {
        width: 420px;
        min-width: 300px;
        height: 480px;
        min-height: 344px;
        padding: 35px;
        background: #fff;
        border-radius: 16px;
        box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);

        .system-title {
            margin-bottom: 60px;
            font-family: MicrosoftYaHeiUI;
            font-size: 24px;
            color: #333;
            text-align: center;
        }

        .logo {
            display: flex;
            justify-content: center;
            margin-bottom: 60px;

            img {
                width: 220px;
                height: 50px;
            }
        }

        .ant-form-item-control-input-content {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: flex-start;
            border-bottom: 1px solid #e6e4e3;

            .label {
                white-space: nowrap;
            }

            input,
            .ant-input-password {
                border: none;
            }

            .ant-input-affix-wrapper:focus,
            .ant-input-affix-wrapper-focused,
            .ant-input:focus {
                box-shadow: none;
            }

            img {
                width: 140px;
                height: 50px;
                cursor: pointer;
            }
        }

        .ant-btn-primary {
            width: 100%;
        }

        .operate-tool {
            display: flex;
            align-items: center;
            justify-content: space-between;

            .ant-checkbox-wrapper,
            .ant-btn-link {
                font-size: 12px;
                color: #999;
            }

            .ant-checkbox {
                font-size: 12px;
            }
        }
    }
}
</style>
