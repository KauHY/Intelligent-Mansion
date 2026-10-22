<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-03 19:43:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 14:22:43
-->
<template>
  <div class="layout">
    <a-layout>
      <a-layout-sider
        v-if="!hiddenMenu"
        :width="slideWidth"
        :collapsed="collapsed"
        collapsible
        :trigger="null"
        :collapsed-width="collapsedWidth"
        breakpoint="md"
        class="slideMenu-box"
      >
        <slideMenu
          :menu-list="menuList"
          :open-keys="openKeys"
        />
      </a-layout-sider>
      <a-layout class="right-section">
        <navbar v-if="!hiddenMenu" />
        <a-layout-content class="content-box">
          <main class="park-content">
            <div
              class="park-content__bg"
              aria-hidden="true"
            >
              <span class="park-content__halo park-content__halo--left" />
              <span class="park-content__halo park-content__halo--right" />
              <span class="park-content__grid" />
              <span class="park-content__scan" />
              <span class="park-content__orbit" />
            </div>
            <div class="park-flow">
              <router-view
                v-slot="{ Component }"
              >
                <transition
                  name="page-fade"
                  mode="out-in"
                  appear
                >
                  <keep-alive :include="cachePageNames">
                    <component
                      :is="Component"
                      :key="$route.fullPath"
                    />
                  </keep-alive>
                </transition>
              </router-view>
            </div>
          </main>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>
<script lang="ts">
import {
  computed, watch, ref, onMounted, defineComponent,
} from 'vue';

import { useRoute, RouteLocationNormalizedLoaded } from 'vue-router';
import { useUserStore } from '@/store/user';
import { useCommonStore } from '@/store/common';
import { useRouterRecord } from '~composition/user';
import slideMenu from './components/slide-menu/slide-menu.vue';
import navbar from './components/navbar/navbar.vue';
import { useSetSlideWidth } from './useSetSlideWidth';

export default defineComponent({
  components: {
    slideMenu,
    navbar,
  },
  setup() {
    const route = useRoute();
    // const store = useStore();
    const userStore = useUserStore();
    const commonStore = useCommonStore();

    const menuList = computed(() => userStore.menuList);
    const cachePageNames = computed(() => commonStore.cachePageNames);
    const baseMenuList = computed<Array<MenuItem>>(() => userStore.baseMenuList);
    const { collapsedWidth, collapsed, slideWidth } = useSetSlideWidth();

    const openKeys = ref<string[]>([]);
    const init = (currentRoute:RouteLocationNormalizedLoaded) => {
      const { openKeys: tempOpenKeys } = useRouterRecord(currentRoute, baseMenuList.value);
      openKeys.value = tempOpenKeys;
    };
    onMounted(() => {
      init(route);
    });
    watch(route, (newRoute) => {
      init(newRoute);
    });
    return {
      menuList,
      openKeys,
      cachePageNames,
      collapsed,
      collapsedWidth,
      slideWidth,
      ...useHiddenMenu(route),
    };
  },
});
// 导航是否隐藏
function useHiddenMenu(route:RouteLocationNormalizedLoaded) {
  const hiddenMenu = computed(() => {
    const { new_blank, external_link } = route.query;
    return new_blank || external_link;
  });
  return {
    hiddenMenu,
  };
}
</script>
<style lang="less" scoped>
.layout {
    min-width: 1320px;
    height: 100%;
    overflow: hidden;

    .ant-layout.ant-layout-has-sider {
        height: 100%;
        padding: 0;
        margin: 0;
        background: transparent;
    }

    .right-section {
        flex: 1;
        min-width: 0;
        min-height: 0;
        background: transparent;
    }

    .slideMenu-box {
        min-width: 0;
        background: transparent;
        box-shadow: none;
    }

    .content-box {
        position: relative;
        display: flex;
        flex-direction: column;
        height: calc(100% - 84px);
        min-height: 0;
        overflow: hidden;
        background: transparent;

        .park-content {
            position: relative;
            display: flex;
            flex: 1;
            flex-direction: column;
            height: 100%;
            min-height: 0;
            margin: 12px 16px 0;
            overflow: hidden;
            background:
                linear-gradient(180deg, rgba(255, 255, 255, 0.24) 0%, rgba(255, 255, 255, 0.08) 100%),
                linear-gradient(180deg, rgba(5, 20, 36, 0.08) 0%, rgba(7, 24, 40, 0.02) 100%);
            isolation: isolate;
            border: 1px solid rgba(255, 255, 255, 0.32);
            border-radius: 28px;
            box-shadow:
                inset 0 1px 0 rgba(255, 255, 255, 0.72),
                0 32px 64px rgba(8, 20, 38, 0.14);
            backdrop-filter: blur(18px);

            &::before,
            &::after {
                position: absolute;
                pointer-events: none;
                content: '';
            }

            &::before {
                top: 0;
                left: 36px;
                width: 240px;
                height: 2px;
                background:
                    linear-gradient(
                        90deg,
                        rgba(var(--theme-primary-rgb), 0),
                        rgba(var(--theme-primary-rgb), 0.92),
                        rgba(var(--theme-primary-rgb), 0)
                    );
                filter: drop-shadow(0 0 10px rgba(var(--theme-primary-rgb), 0.48));
            }

            &::after {
                top: 24px;
                right: 28px;
                width: 140px;
                height: 32px;
                border-top: 1px solid rgba(var(--theme-primary-rgb), 0.28);
                border-right: 1px solid rgba(var(--theme-primary-rgb), 0.28);
                border-radius: 0 18px 0 0;
                opacity: 0.72;
            }

            .park-content__bg {
                position: absolute;
                inset: 0;
                overflow: hidden;
                pointer-events: none;
                border-radius: inherit;
            }

            .park-content__halo,
            .park-content__grid,
            .park-content__scan,
            .park-content__orbit {
                position: absolute;
            }

            .park-content__halo {
                width: 420px;
                height: 420px;
                filter: blur(8px);
                border-radius: 50%;

                &.park-content__halo--left {
                    top: -180px;
                    left: -120px;
                    background: radial-gradient(circle, rgba(var(--theme-primary-rgb), 0.22) 0%, rgba(var(--theme-primary-rgb), 0.08) 42%, transparent 72%);
                }

                &.park-content__halo--right {
                    top: -120px;
                    right: -140px;
                    background: radial-gradient(circle, rgba(123, 214, 255, 0.16) 0%, rgba(var(--theme-primary-rgb), 0.08) 38%, transparent 70%);
                }
            }

            .park-content__grid {
                inset: 0;
                background-image:
                    linear-gradient(rgba(255, 255, 255, 0.14) 1px, transparent 1px),
                    linear-gradient(90deg, rgba(255, 255, 255, 0.14) 1px, transparent 1px);
                background-size: 28px 28px;
                opacity: 0.14;
                mask-image: linear-gradient(180deg, rgba(0, 0, 0, 0.7), transparent 100%);
            }

            .park-content__scan {
                inset: -20% -10%;
                background: linear-gradient(115deg, transparent 44%, rgba(var(--theme-primary-rgb), 0.18) 50%, transparent 56%);
                opacity: 0.78;
            }

            .park-content__orbit {
                right: -140px;
                bottom: -180px;
                width: 420px;
                height: 420px;
                border: 1px solid rgba(var(--theme-primary-rgb), 0.14);
                border-radius: 50%;
                box-shadow:
                    0 0 0 34px rgba(var(--theme-primary-rgb), 0.04),
                    0 0 0 68px rgba(var(--theme-primary-rgb), 0.025);
                opacity: 0.75;
            }

            .park-flow {
                position: relative;
                z-index: 1;
                display: flex;
                flex: 1;
                flex-direction: column;
                height: 100%;
                min-height: 0;
                overflow: auto;
                overscroll-behavior: contain;

                > * {
                    flex-shrink: 0;
                    min-height: 0;
                    border-radius: 22px;
                }
            }
        }
    }
}

@keyframes park-scan {
    0% {
        transform: translate3d(-28%, 0, 0);
    }

    100% {
        transform: translate3d(28%, 0, 0);
    }
}

@keyframes park-orbit {
    0%,
    100% {
        transform: translate3d(0, 0, 0) scale(1);
    }

    50% {
        transform: translate3d(-12px, -10px, 0) scale(1.04);
    }
}
</style>
