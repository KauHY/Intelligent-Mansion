<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-07 14:15:35
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 13:55:08
-->
<template>
  <div class="nav-record">
    <span
      class="collapsed-btn"
      @click="toggleCollapsed"
    >
      <v-svg
        v-if="collapsed"

        name="zhankai"
      />
      <v-svg
        v-else

        name="shouqi"
      />
    </span>
    <!-- </a-button> -->
    <!-- {{ routerRecordList }} -->
    <a-breadcrumb>
      <template #separator>
        <span>></span>
      </template>
      <a-breadcrumb-item
        v-for="currentMenuItem in routerRecordList"
        :key="currentMenuItem?.routeName"
        href=""
        @click="goPage(currentMenuItem)"
      >
        {{ currentMenuItem?.name }}
      </a-breadcrumb-item>
    </a-breadcrumb>
  </div>
</template>
<script  lang="ts">
import { computed, defineComponent } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/store/user';
import { useCommonStore } from '@/store/common';

export default defineComponent({
  // computed: {
  //   ...mapState('user/', [
  //     'routerRecordList',
  //   ]),
  // },
  setup() {
    const router = useRouter();
    const userStore = useUserStore();
    const commonStore = useCommonStore();

    const routerRecordList = computed(() => userStore.routerRecordList);
    const collapsed = computed(() => commonStore.collapsed);
    const goPage = (currentMenuItem:MenuItem) => {
      const {
        route, query, params, routeName,
      } = currentMenuItem;
      if (route && JSON.stringify(query) !== '{}') {
        router.push({ path: route, query });
      } else if (routeName) {
        router.push({ name: routeName, params });
      }
    };
    const toggleCollapsed = () => {
      commonStore.setCollapsed(!collapsed.value);
    };
    return {
      goPage,
      routerRecordList,
      collapsed,
      toggleCollapsed,
    };
  },
});
</script>
<style lang="scss" scoped>
.nav-record {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    width: 100%;
    min-width: 0;

    .collapsed-btn {
        position: relative;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 42px;
        height: 42px;
        margin-right: 14px;
        line-height: 1.5715;
        color: var(--theme-text-secondary);
        cursor: pointer;
        background: linear-gradient(180deg, rgba(255, 255, 255, 0.9) 0%, rgba(244, 248, 253, 0.88) 100%);
        border: 1px solid rgba(var(--theme-primary-rgb), 0.12);
        border-radius: 16px;
        box-shadow:
            inset 0 1px 0 rgba(255, 255, 255, 0.84),
            0 10px 20px rgba(8, 24, 40, 0.04);
        transition: all 0.24s ease;

        &::before,
        &::after {
            position: absolute;
            pointer-events: none;
            content: '';
        }

        &::before {
            top: 0;
            left: 10px;
            width: 20px;
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
            width: 12px;
            height: 12px;
            border-top: 1px solid rgba(var(--theme-primary-rgb), 0.14);
            border-right: 1px solid rgba(var(--theme-primary-rgb), 0.14);
            border-radius: 0 8px 0 0;
            opacity: 0.8;
        }

        &:hover {
            color: var(--ant-primary-color);
            border-color: rgba(var(--theme-primary-rgb), 0.24);
            box-shadow:
                inset 0 1px 0 rgba(255, 255, 255, 0.9),
                0 14px 24px rgba(var(--theme-primary-rgb), 0.1);
            transform: translateY(-1px);
        }

        svg {
            width: 20px;
            min-width: 20px;
            max-width: 20px;
            height: 20px;
        }
    }

    :deep(.ant-breadcrumb) {
        position: relative;
        min-width: 0;
        padding-left: 16px;

        &::before {
            position: absolute;
            top: 50%;
            left: 0;
            width: 8px;
            height: 8px;
            content: '';
            background: linear-gradient(180deg, var(--theme-primary-hover), var(--ant-primary-color));
            border-radius: 50%;
            box-shadow: 0 0 12px rgba(var(--theme-primary-rgb), 0.24);
            transform: translateY(-50%);
        }

        .ant-breadcrumb-link {
            display: inline-flex;
            align-items: center;
            max-width: 180px;
            overflow: hidden;
            font-weight: 500;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .ant-breadcrumb-separator {
            color: rgba(var(--theme-primary-rgb), 0.5);
        }

        > span:last-child {
            .ant-breadcrumb-link,
            a {
                font-weight: 700;
                color: var(--theme-text-primary);
            }
        }
    }
}
</style>
