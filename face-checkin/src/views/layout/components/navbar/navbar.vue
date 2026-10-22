<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 22:21:03
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 13:57:58
-->
<template>
  <a-layout-header class="navbar">
    <span
      class="navbar-ornament navbar-ornament--glow"
      aria-hidden="true"
    />
    <span
      class="navbar-ornament navbar-ornament--corner"
      aria-hidden="true"
    />
    <div class="left-section">
      <navRecord :current-activated-menu-list="routerRecordList" />
    </div>
    <div class="right-section">
      <switch-theme />
      <div class="user-info">
        <div class="user-info-wrapper">
          <a-avatar
            class="user-avatar"
            :style="{ backgroundColor: 'var(--ant-primary-color)', color: '#fff', fontSize: '16px', fontWeight: '500' }"
          >
            {{ realName?.charAt(0)?.toUpperCase() || 'U' }}
          </a-avatar>
          <a-dropdown
            :trigger="['click']"
            size="medium"
          >
            <a
              class="ant-dropdown-link user-name-link"
              @click.prevent
            >
              <span class="user-name">{{ username }}</span>
              <v-svg
                name="arrow-down"
                class="arrow-icon"
              />
            </a>
            <template #overlay>
              <a-menu class="user-dropdown-menu">
                <a-menu-item
                  key="1"
                  @click="visible=true"
                >
                  <span class="menu-item-marker" />
                  <span>修改密码</span>
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item
                  key="2"
                  class="logout-item"
                  @click="loginOut"
                >
                  <span class="menu-item-marker" />
                  <span>退出登录</span>
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </div>
    </div>
    <v-modal
      v-model:visible="visible"
      title="修改密码"
      @ok="modifyPwd"
    >
      <a-form
        ref="formRef"
        :model="formData"
        :label-col="{span:4}"
        :rules="rules"
      >
        <a-form-item
          label="原密码"
          name="oldPassword"
        >
          <a-input-password
            v-model:value="formData.oldPassword"
            autocomplete="off"
            placeholder="请输入原密码"
          />
        </a-form-item>
        <a-form-item
          label="新密码"
          name="newPassword"
        >
          <a-input-password
            v-model:value="formData.newPassword"
            autocomplete="off"
            placeholder="至少8位数字、字母、符号两种以上组合"
          />
        </a-form-item>
        <a-form-item
          label="确认密码"
          name="newPasswordAgain"
        >
          <a-input-password
            v-model:value="formData.newPasswordAgain"
            autocomplete="off"
            placeholder="请再次输入新密码"
          />
        </a-form-item>
      </a-form>
    </v-modal>
  </a-layout-header>
</template>
<script lang="ts">
import {
  defineComponent, ref, reactive,
} from 'vue';
import { useRouter } from 'vue-router';
import { REGS } from '@/constants/validate';
import { message } from 'ant-design-vue';
import { useUserStore } from '@/store/user';
import User, { UserInfo, UserPwdUpdate } from '~models/user';
import navRecord from '../nav-record/nav-record.vue';
import switchTheme from '../switch-theme/switch-theme.vue';

export default defineComponent({
  components: {
    navRecord,
    switchTheme,
  },
  props: {
    routerRecordList: {
      type: Array,
      default: () => ([]),
    },
  },
  setup() {
    const userInstance = new User();
    const router = useRouter();
    const userStore = useUserStore();
    // const store = useStore();
    const visible = ref(false);
    const { username, headOrgId, realName } = window.$localStorage.userInfo as UserInfo;
    const loginOut = async () => {
      // 路由加载状态设置为false
      await userInstance.logout();
      window.$localStorage.removeItem('userInfo');
      window.$localStorage.removeItem('Authorization');
      userStore.setRouterInitComplete(false);
      if (window.$localStorage.isCompany === 1) {
        window.$localStorage.headOrgIds = headOrgId;
        router.push({ path: `/wisdom-park-company/login-company/${headOrgId}` });
      } else {
        router.push('/login');
      }
    };
    const formRef = ref();
    const formData = reactive<UserPwdUpdate>({
      newPassword: '',
      newPasswordAgain: '',
      oldPassword: '',
    });
    const rules = {
      oldPassword: { required: true, message: '原密码不能为空' },
      newPassword: {
        validator: (rules:unknown, value:string) => {
          const { newPassword, newPasswordAgain, oldPassword } = formData;
          if (oldPassword === newPassword) {
            return Promise.reject('新密码不能与原密码相同');
          }
          if (!REGS.passWord.test(value)) {
            return Promise.reject('密码格式为不少于8位的数字、字母、符号两种或以上组合');
          }
          return Promise.resolve();
        },
        required: true,
      },
      newPasswordAgain: {
        validator: (rules:unknown, value:string) => {
          if (!REGS.passWord.test(value)) {
            return Promise.reject('密码格式为不少于8位的数字、字母、符号两种或以上组合');
          }
          if (formData.newPassword !== formData.newPasswordAgain) {
            return Promise.reject('两次输入的密码不一致');
          }
          formRef.value.validateFields('newPassword');
          return Promise.resolve();
        },
        required: true,
      },
    };
    const modifyPwd = async () => {
      await formRef.value.validate(); // 校验
      if (window.$localStorage.isCompany === 1) {
        await userInstance.updateCompanyPwd(formData);
      } else {
        await userInstance.updatePwd(formData);
      }

      visible.value = false;
      message.success('修改成功');
    };
    return {
      loginOut,
      username,
      realName,
      modifyPwd,
      visible,
      formData,
      rules,
      formRef,
    };
  },
});
</script>
<style lang="less" scoped>

.navbar {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 76px;
    min-height: 40px;
    padding: 0 18px 0 14px;
    margin: 12px 16px 0;
    overflow: hidden;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.92) 0%, rgba(247, 250, 254, 0.84) 100%);
    border: 1px solid rgba(var(--theme-primary-rgb), 0.14);
    border-radius: 24px;
    box-shadow:
        inset 0 1px 0 rgba(255, 255, 255, 0.9),
        0 18px 36px rgba(8, 24, 40, 0.08);
    backdrop-filter: blur(16px);

    &::before {
        position: absolute;
        top: 0;
        left: 28px;
        width: 176px;
        height: 2px;
        content: '';
        background:
            linear-gradient(
                90deg,
                rgba(var(--theme-primary-rgb), 0),
                rgba(var(--theme-primary-rgb), 0.72),
                rgba(var(--theme-primary-rgb), 0)
            );
        box-shadow: 0 0 14px rgba(var(--theme-primary-rgb), 0.16);
    }

    &::after {
        position: absolute;
        right: 0;
        bottom: 0;
        left: 0;
        height: 1px;
        content: '';
        background:
            linear-gradient(
                90deg,
                rgba(var(--theme-primary-rgb), 0),
                rgba(var(--theme-primary-rgb), 0.12) 18%,
                rgba(var(--theme-primary-rgb), 0.24) 50%,
                rgba(var(--theme-primary-rgb), 0.12) 82%,
                rgba(var(--theme-primary-rgb), 0)
            );
    }

    .navbar-ornament {
        position: absolute;
        pointer-events: none;
    }

    .navbar-ornament--glow {
        inset: 0;
        background:
            linear-gradient(120deg, transparent 0%, rgba(var(--theme-primary-rgb), 0.06) 42%, transparent 58%),
            radial-gradient(circle at 86% 0%, rgba(var(--theme-primary-rgb), 0.12), transparent 30%);
        opacity: 0.85;
        mask-image: linear-gradient(180deg, rgba(0, 0, 0, 0.9), transparent 100%);
    }

    .navbar-ornament--corner {
        top: 14px;
        right: 18px;
        width: 108px;
        height: 24px;
        border-top: 1px solid rgba(var(--theme-primary-rgb), 0.16);
        border-right: 1px solid rgba(var(--theme-primary-rgb), 0.16);
        border-radius: 0 14px 0 0;
        opacity: 0.72;
    }

    .left-section {
        position: relative;
        display: flex;
        flex: 1;
        align-items: center;
        min-width: 0;
        height: 100%;
        padding-right: 20px;
        font-size: 10px;
        color: var(--theme-text-secondary);

        &::after {
            position: absolute;
            top: 18px;
            right: 0;
            bottom: 18px;
            width: 1px;
            content: '';
            background: linear-gradient(180deg, rgba(var(--theme-primary-rgb), 0), rgba(var(--theme-primary-rgb), 0.16), rgba(var(--theme-primary-rgb), 0));
        }
    }

    .right-section {
        position: relative;
        display: flex;
        gap: 10px;
        align-items: center;
        justify-content: flex-end;
        height: 100%;
        padding-left: 18px;
        font-size: 12px;

        .user-info {
            display: flex;
            align-items: center;
            justify-content: flex-start;
            height: 100%;

            .user-info-wrapper {
                position: relative;
                display: flex;
                gap: 12px;
                align-items: center;
                padding: 8px 14px 8px 12px;
                cursor: pointer;
                background: linear-gradient(180deg, rgba(255, 255, 255, 0.88) 0%, rgba(244, 248, 253, 0.88) 100%);
                border: 1px solid rgba(var(--theme-primary-rgb), 0.12);
                border-radius: 18px;
                box-shadow:
                    inset 0 1px 0 rgba(255, 255, 255, 0.82),
                    0 10px 20px rgba(8, 24, 40, 0.04);
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

                &::before,
                &::after {
                    position: absolute;
                    pointer-events: none;
                    content: '';
                }

                &::before {
                    top: 0;
                    left: 18px;
                    width: 68px;
                    height: 2px;
                    background:
                        linear-gradient(
                            90deg,
                            rgba(var(--theme-primary-rgb), 0),
                            rgba(var(--theme-primary-rgb), 0.72),
                            rgba(var(--theme-primary-rgb), 0)
                        );
                }

                &::after {
                    top: 8px;
                    right: 8px;
                    width: 24px;
                    height: 14px;
                    border-top: 1px solid rgba(var(--theme-primary-rgb), 0.14);
                    border-right: 1px solid rgba(var(--theme-primary-rgb), 0.14);
                    border-radius: 0 10px 0 0;
                    opacity: 0.82;
                }

                &:hover {
                    background: linear-gradient(180deg, #fff 0%, rgba(var(--theme-primary-rgb), 0.08) 100%);
                    border-color: rgba(var(--theme-primary-rgb), 0.2);
                    box-shadow:
                        inset 0 1px 0 rgba(255, 255, 255, 0.92),
                        0 14px 24px rgba(var(--theme-primary-rgb), 0.1);
                    transform: translateY(-1px);
                }

                .user-avatar {
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    width: 32px;
                    height: 32px;
                    line-height: 1;
                    border: 1px solid rgba(255, 255, 255, 0.88);
                    box-shadow: 0 8px 18px rgba(var(--theme-primary-rgb), 0.18);
                    transition: all 0.3s ease;

                    &:hover {
                        box-shadow: 0 12px 22px rgba(var(--theme-primary-rgb), 0.22);
                        transform: scale(1.05);
                    }
                }

                .user-name-link {
                    display: flex;
                    gap: 6px;
                    align-items: center;
                    font-weight: 500;
                    color: var(--theme-text-primary);
                    text-decoration: none;
                    transition: color 0.3s ease;

                    &:hover {
                        color: var(--ant-primary-color);
                    }

                    .user-name {
                        max-width: 120px;
                        overflow: hidden;
                        font-size: 14px;
                        line-height: 1;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                    }

                    .arrow-icon {
                        font-size: 12px;
                        transition: transform 0.3s ease;
                    }

                    &:hover .arrow-icon {
                        transform: translateY(2px);
                    }
                }
            }
        }

        :deep(.switch-theme),
        .user-info-wrapper {
            z-index: 1;
        }

        :deep(.ant-dropdown-link.ant-dropdown-trigger) {
            display: flex;
            align-items: center;
            justify-content: flex-start;
            height: 100%;
        }

        /* 下拉菜单样式优化 */
        :deep(.user-dropdown-menu) {
            min-width: 160px;
            padding: 8px;
            background: rgba(255, 255, 255, 0.94);
            border: 1px solid rgba(var(--theme-primary-rgb), 0.14);
            border-radius: 16px;
            box-shadow:
                inset 0 1px 0 rgba(255, 255, 255, 0.88),
                0 18px 32px rgba(8, 24, 40, 0.12);
            backdrop-filter: blur(18px);

            .ant-dropdown-menu-item {
                display: flex;
                gap: 10px;
                align-items: center;
                padding: 10px 14px;
                font-size: 14px;
                border-radius: 12px;
                transition: all 0.3s ease;

                .menu-item-icon,
                .menu-item-marker {
                    position: relative;
                    flex: 0 0 8px;
                    width: 8px;
                    height: 8px;
                    background: linear-gradient(180deg, var(--theme-primary-hover), var(--ant-primary-color));
                    border-radius: 50%;
                    box-shadow: 0 0 12px rgba(var(--theme-primary-rgb), 0.38);
                }

                &:hover {
                    color: var(--theme-primary-deep);
                    background: linear-gradient(90deg, rgba(var(--theme-primary-rgb), 0.1) 0%, rgba(var(--theme-primary-rgb), 0.04) 100%);
                }
            }

            .logout-item {
                .menu-item-marker {
                    background: linear-gradient(180deg, #ff8b8b, #ff5c5c);
                    box-shadow: 0 0 12px rgba(255, 92, 92, 0.28);
                }

                &:hover {
                    color: #ff4d4f;
                    background: linear-gradient(90deg, rgba(255, 92, 92, 0.12) 0%, rgba(255, 92, 92, 0.04) 100%);
                }
            }

            .ant-dropdown-menu-item-divider {
                margin: 4px 0;
                background-color: rgba(123, 149, 177, 0.14);
            }
        }
    }
}
</style>
