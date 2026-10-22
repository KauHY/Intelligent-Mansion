<!--
 * @Description: 重置密码
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-06 17:52:54
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 19:13:30
-->
<template>
  <div class="reset-password">
    <div class="content">
      <a-steps
        :current="currentStep"
        size="small"
        class="step-box"
      >
        <a-step title="校验身份" />
        <a-step title="重置密码" />
        <a-step title="成功" />
      </a-steps>
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
              label="手机号"
              v-bind="validateInfos.mobilePhone"
            >
              <a-input
                v-model:value="modelRef.mobilePhone"
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
              v-bind="validateInfos.telValidateCode"
            >
              <a-input
                v-model:value="modelRef.telValidateCode"
                placeholder="短信验证码"
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
            v-if="currentStep===1"
            class="step2"
          >
            <a-form-item
              label="新密码"
              name="newPassword"
              v-bind="validateInfos.newPassword"
            >
              <a-input-password
                v-model:value="modelRef.newPassword"
                autocomplete="off"
                placeholder="至少8位数字、字母、符号两种以上组合"
              />
            </a-form-item>
            <a-form-item
              label="确认密码"
              v-bind="validateInfos.newPasswordAgain"
            >
              <a-input-password
                v-model:value="modelRef.newPasswordAgain"
                autocomplete="off"
                placeholder="至少8位数字、字母、符号两种以上组合"
              />
            </a-form-item>
          </div>
          <div
            v-if="currentStep===2"
            class="step2"
          >
            <a-result
              status="success"
              title="重置密码成功"
            >
              <template #extra>
                <a-button
                  key="console"
                  type="link"
                  @click="blankLogin"
                >
                  返回平台登录
                </a-button>
              </template>
            </a-result>
          </div>
        </a-form>
        <a-button
          v-if="currentStep<2"
          class="next-btn"
          type="primary"
          @click="next"
        >
          下一步
        </a-button>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  ref, reactive, toRefs, onMounted, defineComponent,
} from 'vue';
import { message } from 'ant-design-vue';
// import router from '@/routers';
import { useRouter } from 'vue-router';
import type { PropType } from 'vue';
import { useImageCode, useSIMCode, checkImageCode } from '~composition/common';
import User, { UserInfo } from '~models/user';
import { useResetPassword } from './validate.conf';

export default defineComponent({
  name: 'ResetPassword',
  components: {},
  setup() {
    const router = useRouter();
    const userInstance = new User();
    const currentStep = ref(0);
    const {
      modelRef, validate, validateInfos,
    } = useResetPassword(currentStep);
    // 图形验证码 Not visible browse
    const { imageCodeUrl, getImageInfo } = useImageCode(
      () => {
        modelRef.validateCode = '';
      },
      (url:string, key:string) => {
        modelRef.validateCodeKey = key;
      },
    );
    // 短信验证码
    const { isSendCode, btnText, sendCode } = useSIMCode();
    // 短信验证码
    const getSmsCode = async () => {
      validate(['mobilePhone', 'username', 'validateCode']).then(async (res) => {
        const result = await checkImageCode(modelRef.validateCodeKey, modelRef.validateCode);
        if (!result.data) {
          modelRef.validateCode = '';
          getImageInfo();
          message.warning('请输入正常的图形验证码！');
          return;
        }
        let data = null;
        sendCode();
        if (window.$localStorage.isCompany === 1) {
          data = await userInstance.getSmsCode(modelRef.mobilePhone);
        } else {
          data = await userInstance.getBusSmsCode(modelRef.mobilePhone);
        }
      }).catch((err) => {
        message.warning('请依次完成必填项！');
      });
    };
    const blankLogin = () => {
      if (window.$localStorage.isCompany === 1) {
        let headOrgIds = null;
        if (window.$localStorage.userInfo) {
          headOrgIds = window.$localStorage.userInfo;
        } else {
          headOrgIds = window.$localStorage.headOrgIds;
        }
        router.push({ path: `/wisdom-park-company/login-company/${headOrgIds}` });
      } else {
        router.push('/login');
      }
    };
    let validateToken = '';
    const next = () => {
      // validateField('username');
      validate().then(async (res) => {
        // debugger;
        const {
          username,
          mobilePhone,
          telValidateCode,
          validateCode,
          validateCodeKey,
          newPassword,
          newPasswordAgain,
        } = modelRef;
        switch (currentStep.value) {
          case 0: {
            try {
              if (window.$localStorage.isCompany === 1) {
                validateToken = await userInstance.validateCompanyIdentity({
                  username,
                  mobilePhone,
                  telValidateCode,
                  validateCode,
                  validateCodeKey,
                });
              } else {
                validateToken = await userInstance.validateIdentity({
                  username,
                  mobilePhone,
                  telValidateCode,
                  validateCode,
                  validateCodeKey,
                });
              }

              currentStep.value = 1;
            } catch (error) {
              await getImageInfo(); // 重新获取图形验证码
            }
            break;
          }
          case 1: {
            if (window.$localStorage.isCompany === 1) {
              await userInstance.resetPwdCompanyForForget({
                newPassword,
                newPasswordAgain,
                validateToken,
              });
            } else {
              await userInstance.resetPwdForForget({
                newPassword,
                newPasswordAgain,
                validateToken,
              });
            }

            currentStep.value = 2;
            break;
          }
          default:
            // console.log('完成');
        }
      }).catch((err) => {
        // console.log(err, '-err');
        message.warning('请依次完成必填项！');
      });
    };
    getImageInfo();
    onMounted(() => {
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
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
      getSmsCode,
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
