<template>
  <div class="reset-password">
    <div class="content">
      <div
        v-if="currentStep===0"
        class="header-box"
      >
        <span>企业注册</span>
        <span
          class="login-class"
          @click="blankLogin"
        >企业登录 ></span>
      </div>
      <div class="step-content">
        <a-form
          ref="formRef"
          label-align="left"
          :label-col="{span: 5}"
        >
          <div
            v-if="currentStep===0"
            class="step1"
          >
            <a-form-item
              label="用户名"
              v-bind="validateInfos.username"
            >
              <a-input
                v-model:value="modelRef.username"
                placeholder="请输入用户名"
              />
            </a-form-item>
            <a-form-item
              label="密码"
              name="newPassword"
              v-bind="validateInfos.password"
            >
              <a-input
                v-model:value="modelRef.password"
                placeholder="至少八位数字、字母、符号两种或以上组合"
              />
            </a-form-item>
            <a-form-item
              label="手机号"
              v-bind="validateInfos.mobile"
            >
              <a-input
                v-model:value="modelRef.mobile"
                placeholder="请输入手机号"
              />
            </a-form-item>
            <a-form-item
              label="验证码"
              v-bind="validateInfos.validateCode"
            >
              <a-input
                v-model:value="modelRef.validateCode"
                placeholder="请输入图形验证码"
              />
              <img
                :src="imageCodeUrl"
                @click="getImageInfo"
              >
            </a-form-item>
            <a-form-item
              label="短信验证码"
              v-bind="validateInfos.smsCode"
            >
              <a-input
                v-model:value="modelRef.smsCode"
                placeholder="请输入短信验证码"
              />
              <a-button
                class="sms-code-btn"
                :disabled="!isSendCode"
                @click="getSmsCode"
              >
                {{ btnText }}
              </a-button>
            </a-form-item>
          </div>
          <div
            v-else
            class="step2"
          >
            <a-result
              class="result-no"
              status="success"
              title=""
            />
            <div
              style="line-height: 36px;"
            >
              <div
                key="console"
                type="link"
                style="color: #394662;"
              >
                注册成功
              </div>
              <div
                key="console"
                type="link"
                style="font-size: 12px; color: #87b9f1; cursor: pointer;"
                @click="blankLogin"
              >
                立即登录
              </div>
            </div>
          </div>
        </a-form>
        <a-button
          v-if="currentStep===0"
          class="next-btn"
          type="primary"
          @click="next"
        >
          注册
        </a-button>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  ref, onMounted, defineComponent,
} from 'vue';
import { message } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { useImageCode, useSIMCode } from '~composition/common';
import User from '~models/company';
import { companyForm } from './validate.conf';

export default defineComponent({
  name: 'ResetPassword',
  components: {},
  props: {
    headOrgId: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const router = useRouter();
    const userInstance = new User();
    const currentStep = ref(0);
    const {
      modelRef, validate, validateInfos,
    } = companyForm(currentStep);
    const { imageCodeUrl, getImageInfo } = useImageCode(
      () => {
        modelRef.validateCode = '';
      },
      (url:string, key:string) => {
        modelRef.validateCodeKey = key;
      },
    );
    const { isSendCode, btnText, sendCode } = useSIMCode();
    // 短信验证码
    const getSmsCode = async () => {
      if (!modelRef.mobile) {
        message.info('请先填写正确的手机号');
      } else {
        sendCode();
        const data = await userInstance.getSmsCode(modelRef.mobile);
      }
    };

    const blankLogin = () => {
      router.push({ path: `/wisdom-park-company/login-company/${props.headOrgId}` });
    };
    let validateToken = '';
    const next = () => {
      validate().then(async (res:any) => {
        const {
          username,
          mobile,
          smsCode,
          validateCode,
          validateCodeKey,
          password,
          headOrgId,
          id,
        } = modelRef;

        switch (currentStep.value) {
          case 0: {
            try {
              validateToken = await userInstance.register({
                password,
                headOrgId: props.headOrgId,
                id,
                username,
                mobile,
                smsCode,
                validateCode,
                validateCodeKey,
              });
              console.log(validateToken, 'validateToken');
              currentStep.value = 1;
            } catch (error) {
              // await getImageInfo(); // 重新获取验证码
            }
            break;
          }
          default:
        }
      }).catch((err:any) => {
        console.log(err, '-err');
        message.warning('操作失败');
      });
    };
    getImageInfo();
    onMounted(() => {
    });
    return {
      getSmsCode,
      modelRef,
      validateInfos,
      next,
      currentStep,
      imageCodeUrl,
      getImageInfo,
      blankLogin,
      isSendCode,
      btnText,
      sendCode,
    };
  },
});
</script>
<style lang="less" scoped>
.reset-password {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;

    .header-box {
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;

        .login-class {
            font-size: 12px;
            color: #4a8fee;
            cursor: pointer;
        }
    }

    .result-no {
        padding: 0;
        margin-right: 20px;
    }

    .step2 {
        display: flex;
        height: 72px;
    }

    :deep(.ant-form-item-control-input-content) {
        display: flex;

        img {
            min-width: 103px;
            max-width: 103px;
            margin-left: 10px;
            cursor: pointer;
        }

        .sms-code-btn {
            min-width: 103px;
            max-width: 103px;
            margin-left: 10px;
        }
    }

    >.content {
        width: 700px;

        .step-box {
            margin-bottom: 80px;
        }

        .step-content {
            .next-btn {
                margin-left: 150px;
            }
        }
    }
}
</style>
