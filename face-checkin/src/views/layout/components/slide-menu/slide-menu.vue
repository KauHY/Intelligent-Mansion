<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-25 17:18:49
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-27 16:09:41
-->
<template>
  <div class="slideMenu">
    <div
      class="brand-panel"
      :class="{ 'is-collapsed': collapsed }"
    >
      <div class="brand-mark">
        <span class="mark-core" />
        <span class="mark-line mark-line-top" />
        <span class="mark-line mark-line-middle" />
        <span class="mark-line mark-line-bottom" />
      </div>
      <div
        v-if="!collapsed"
        class="brand-copy"
      >
        <strong>智慧园区</strong>
        <span>Witpark Operation Hub</span>
      </div>
    </div>
    <div class="menu-shell">
      <a-menu
        ref="slideMenu"
        v-model:selectedKeys="selectedKeys"
        :open-keys="openKeys"
        :inline-indent="24"
        mode="inline"
        class="a-menu-vertical-demo"
        @select="select"
      >
        <menuItem
          v-for="menuItem in menuList"
          :key="menuItem.routeName"
          :menu-item="menuItem"
        />
      </a-menu>
    </div>
  </div>
</template>
<script lang="ts">
import { useRoute, useRouter } from 'vue-router';
import {
  defineComponent, computed, ref, watchEffect,
} from 'vue';

import { useUserStore } from '@/store/user';
import { useCommonStore } from '@/store/common';
import type { PropType } from 'vue';
import { MenuItem as Menu } from '~models/meun';
import menuItem from './components/menu-item.vue';

export default defineComponent({
  components: { menuItem },
  props: {
    menuList: {
      type: Array as PropType<Menu[]>,
      default: () => ([]),
    },
    openKeys: {
      type: Array as PropType<Array<string>>,
      default: () => ([]),
    },
  },
  setup() {
    const userStore = useUserStore();
    const commonStore = useCommonStore();

    const selectedKeys = ref<Array<string>>([]);
    const route = useRoute();
    const router = useRouter();
    const baseMenuList = computed(() => userStore.baseMenuList);
    const collapsed = computed(() => commonStore.collapsed);
    const select = (opt:any) => {
      const { key: keyStr, item: { path } } = opt;
      const key = keyStr.split('_')[0];
      const paths = path.split('?');
      let params:{[key:string]:string} = {};
      if (paths[1]) {
        params = Object.fromEntries(new URLSearchParams(paths[1]).entries());
      }
      if (params?.external_link) {
        const tempPath = `${compileConf.serverBaseUrl}${path}`;
        window.open(tempPath);
        return;
      }
      if (params?.new_blank) {
        const tempPath = path.replace('/', '');
        const fullPath = `${compileConf.publicPath}#${tempPath}`;
        window.open(fullPath);
        return;
      }
      router.push({ name: key });
    };

    const getSelectKey = (name:string) => {
      // debugger;
      const currentMenu = (baseMenuList.value as Array<Menu>).find((item) => item.routeName === name);

      if (currentMenu?.type === 0) return [`${name}_${currentMenu.id}`];
      const parentMenu = (baseMenuList.value as Array<Menu>).find((item) => item.id === currentMenu?.parentId);
      // debugger;
      return (parentMenu?.routeName) ? [`${parentMenu?.routeName}_${parentMenu?.id}`] : [];
    };
    watchEffect(() => {
      const name = route.name as string;
      selectedKeys.value = getSelectKey(name);
    });
    return {
      selectedKeys,
      select,
      collapsed,
    };
  },
});

</script>
<style lang="less" scoped>
.slideMenu {
    position: relative;
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
    padding: 18px 14px 16px;
    overflow: hidden;
    background: var(--theme-sidebar-bg);
    border-right: 1px solid var(--theme-sidebar-border);
    box-shadow: var(--theme-sidebar-shadow);

    &::before,
    &::after {
        position: absolute;
        inset: 0;
        pointer-events: none;
        content: '';
    }

    &::before {
        background:
            radial-gradient(circle at 24% 12%, rgba(var(--theme-primary-rgb), 0.32), transparent 28%),
            radial-gradient(circle at 86% 16%, rgba(var(--theme-primary-rgb), 0.16), transparent 18%);
        opacity: 0.8;
    }

    &::after {
        background-image:
            linear-gradient(rgba(255, 255, 255, 0.28) 1px, transparent 1px),
            linear-gradient(90deg, rgba(255, 255, 255, 0.28) 1px, transparent 1px);
        background-size: 28px 28px;
        opacity: 0.1;
        mask-image: linear-gradient(180deg, rgba(0, 0, 0, 0.64), transparent 92%);
    }

    .brand-panel {
        position: relative;
        z-index: 1;
        display: flex;
        gap: 12px;
        align-items: center;
        min-width: 0;
        padding: 14px;
        margin-bottom: 14px;
        overflow: hidden;
        background: linear-gradient(180deg, rgba(255, 255, 255, 0.12) 0%, rgba(255, 255, 255, 0.04) 100%);
        border: 1px solid rgba(137, 188, 255, 0.16);
        border-radius: 20px;
        box-shadow:
            inset 0 1px 0 rgba(255, 255, 255, 0.08),
            0 18px 32px rgba(5, 14, 28, 0.24);
        backdrop-filter: blur(20px);

        &.is-collapsed {
            justify-content: center;
            padding: 14px 0;
        }

        .brand-mark {
            position: relative;
            flex: 0 0 44px;
            width: 44px;
            height: 44px;
            background: linear-gradient(180deg, rgba(var(--theme-primary-rgb), 0.22) 0%, rgba(255, 255, 255, 0.02) 100%);
            border-radius: 14px;
            box-shadow:
                inset 0 0 0 1px rgba(255, 255, 255, 0.08),
                0 10px 24px rgba(var(--theme-primary-rgb), 0.18);

            .mark-core,
            .mark-line {
                position: absolute;
                left: 50%;
                background: linear-gradient(180deg, #eff7ff 0%, #8fd1ff 100%);
                border-radius: 999px;
                box-shadow: 0 0 14px rgba(var(--theme-primary-rgb), 0.45);
                transform: translateX(-50%);
            }

            .mark-core {
                top: 10px;
                width: 8px;
                height: 24px;
            }

            .mark-line {
                width: 20px;
                height: 2px;
            }

            .mark-line-top {
                top: 14px;
            }

            .mark-line-middle {
                top: 22px;
                width: 24px;
            }

            .mark-line-bottom {
                top: 30px;
                width: 18px;
            }
        }

        .brand-copy {
            display: flex;
            flex: 1;
            flex-direction: column;
            min-width: 0;

            strong {
                overflow: hidden;
                font-size: 15px;
                font-weight: 700;
                color: var(--theme-sidebar-text-strong);
                text-overflow: ellipsis;
                letter-spacing: 0.04em;
                white-space: nowrap;
            }

            span {
                margin-top: 4px;
                overflow: hidden;
                font-size: 11px;
                color: rgba(231, 243, 255, 0.62);
                text-overflow: ellipsis;
                text-transform: uppercase;
                letter-spacing: 0.08em;
                white-space: nowrap;
            }
        }
    }

    .menu-shell {
        position: relative;
        z-index: 1;
        flex: 1;
        min-width: 95px;
        min-height: 0;
        padding: 12px 8px;
        overflow: auto;
        background: linear-gradient(180deg, rgba(5, 18, 30, 0.6) 0%, rgba(7, 23, 38, 0.92) 100%);
        border: 1px solid rgba(137, 188, 255, 0.12);
        border-radius: 22px;
        box-shadow:
            inset 0 1px 0 rgba(255, 255, 255, 0.04),
            inset 0 -1px 0 rgba(255, 255, 255, 0.02);
    }

    :deep(.ant-menu) {
        width: 100%;
        padding-right: 1px;
        color: var(--theme-sidebar-text);
        background: transparent;
        border: none;

        .ant-menu-item,
        .ant-menu-submenu-title {
            position: relative;
            display: flex;
            align-items: center;
            width: 100%;
            height: 44px;
            padding: 0 14px !important;
            margin: 6px 0;
            overflow: hidden;
            line-height: 44px;
            color: var(--theme-sidebar-text);
            border-radius: 14px;
            transition: all 0.24s ease;

            &:hover {
                color: var(--theme-sidebar-text-strong);
                background: var(--theme-sidebar-hover-bg);
            }
        }

        .ant-menu-submenu-title {
            padding-right: 40px !important;
        }

        .ant-menu-sub {
            padding-top: 4px;
            padding-bottom: 2px;
            background: transparent;

            .ant-menu-item,
            .ant-menu-submenu-title {
                height: 40px;
                padding-left: 46px !important;
                margin: 4px 0;
                line-height: 40px;
            }

            .ant-menu-sub {
                .ant-menu-item,
                .ant-menu-submenu-title {
                    padding-left: 58px !important;
                }
            }
        }

        .ant-menu-item-selected,
        .ant-menu-submenu-selected > .ant-menu-submenu-title {
            color: var(--theme-sidebar-text-strong);
            background: linear-gradient(90deg, rgba(var(--theme-primary-rgb), 0.2) 0%, rgba(var(--theme-primary-rgb), 0.08) 100%);
            box-shadow:
                inset 0 0 0 1px rgba(var(--theme-primary-rgb), 0.18),
                0 10px 18px rgba(var(--theme-primary-rgb), 0.12);

            &::before {
                position: absolute;
                top: 10px;
                bottom: 10px;
                left: 0;
                width: 3px;
                content: '';
                background: linear-gradient(180deg, #a8dbff 0%, var(--ant-primary-color) 100%);
                border-radius: 0 999px 999px 0;
                box-shadow: 0 0 16px rgba(var(--theme-primary-rgb), 0.42);
            }
        }

        .ant-menu-item-active:not(.ant-menu-item-selected) {
            color: var(--theme-sidebar-text-strong);
        }

        .ant-menu-submenu-arrow {
            right: 16px;
            left: auto;
            color: rgba(231, 243, 255, 0.68);
        }

        .ant-menu-title-content {
            display: flex;
            flex: 1;
            align-items: center;
            min-width: 0;
            overflow: hidden;
            color: inherit;
        }

        &.ant-menu-inline-collapsed {
            .ant-menu-title-content span {
                display: none;
            }

            .ant-menu-submenu-arrow {
                display: none;
            }
        }
    }
}
</style>
