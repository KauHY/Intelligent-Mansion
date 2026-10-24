<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-27 09:59:46
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-27 15:48:04
-->
<template>
  <div class="switch-theme">
    <div
      class="theme-wrapper"
      @click="showSelectColor"
    >
      <div class="theme-icon-container">
        <span class="theme-orbit" />
        <div
          class="theme-color-preview"
          :style="{ backgroundColor: currentThemeColor }"
        />
      </div>
      <span class="theme-text">主题</span>
      <input
        ref="inputRef"
        type="color"
        class="color-input-hidden"
        :value="currentThemeColor"
        @input="switchTheme($event?.target?.value)"
      >
    </div>
  </div>
</template>
<script lang='ts'>
import {
  ref, defineComponent,
} from 'vue';
import { useSwitchTheme } from '@/theme';

export default defineComponent({
  setup() {
    const inputRef = ref<HTMLInputElement>();

    const showSelectColor = () => {
      inputRef.value?.click();
    };

    return {
      ...useSwitchTheme(),
      inputRef,
      showSelectColor,
    };
  },
});
</script>
<style lang="less" scoped>

.switch-theme {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;

    .theme-wrapper {
        position: relative;
        display: flex;
        gap: 10px;
        align-items: center;
        padding: 8px 14px 8px 12px;
        cursor: pointer;
        user-select: none;
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
            left: 16px;
            width: 58px;
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
            width: 22px;
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

            .theme-icon-container {
                transform: scale(1.04);
            }

            .theme-text {
                color: var(--theme-primary-deep);
            }
        }

        &:active {
            box-shadow: 0 8px 18px rgba(var(--theme-primary-rgb), 0.12);
            transform: translateY(0);
        }

        .theme-icon-container {
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            width: 32px;
            height: 32px;
            background: radial-gradient(circle at 30% 30%, rgba(var(--theme-primary-rgb), 0.18) 0%, rgba(var(--theme-primary-rgb), 0.08) 58%, rgba(255, 255, 255, 0.04) 100%);
            border: 1px solid rgba(var(--theme-primary-rgb), 0.08);
            border-radius: 12px;
            transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);

            .theme-orbit {
                position: relative;
                width: 18px;
                height: 18px;
                border: 1px solid rgba(var(--theme-primary-rgb), 0.4);
                border-radius: 50%;
                box-shadow: 0 0 14px rgba(var(--theme-primary-rgb), 0.2);

                &::before,
                &::after {
                    position: absolute;
                    content: '';
                    background: linear-gradient(180deg, #eef7ff 0%, var(--ant-primary-color) 100%);
                    border-radius: 50%;
                    box-shadow: 0 0 10px rgba(var(--theme-primary-rgb), 0.3);
                }

                &::before {
                    top: -3px;
                    right: -2px;
                    width: 5px;
                    height: 5px;
                }

                &::after {
                    top: 50%;
                    left: 50%;
                    width: 6px;
                    height: 6px;
                    transform: translate(-50%, -50%);
                }
            }

            .theme-color-preview {
                position: absolute;
                right: -2px;
                bottom: -2px;
                width: 12px;
                height: 12px;
                border: 2px solid #fff;
                border-radius: 50%;
                box-shadow: 0 4px 10px rgba(8, 24, 40, 0.18);
                transition: all 0.3s ease;
            }
        }

        .theme-text {
            font-size: 14px;
            font-weight: 600;
            line-height: 1;
            color: var(--theme-text-primary);
            transition: color 0.3s ease;
        }

        .color-input-hidden {
            position: absolute;
            width: 0;
            height: 0;
            pointer-events: none;
            opacity: 0;
        }
    }
}
</style>
