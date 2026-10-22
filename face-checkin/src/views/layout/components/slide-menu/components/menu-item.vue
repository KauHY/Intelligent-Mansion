<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-25 17:51:54
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 16:57:06
-->
<template>
  <div
    class="menu-item"
  >
    <template v-if="!menuItem.children || !menuItem.children.length">
      <a-menu-item
        :key="`${menuItem.routeName}_${menuItem.id}`"
        :path="menuItem.route"
      >
        <v-svg
          v-if="menuItem.icon"
          :name="menuItem.icon"
          class="menu-icon"
        />
        <span class="menu-label">{{ menuItem.name }}</span>
      </a-menu-item>
    </template>
    <template v-else>
      <a-sub-menu :key="`${menuItem.routeName}_${menuItem.id}`">
        <template #title>
          <v-svg
            v-if="menuItem.icon"
            :name="menuItem.icon"
            class="menu-icon"
          />
          <span class="menu-label">{{ menuItem.name }}</span>
        </template>
        <menuItem
          v-for="menuChild in menuItem.children"
          :key="`${menuChild.routeName}_${menuChild.id}`"
          :menu-item="menuChild"
        />
      </a-sub-menu>
    </template>
  </div>
</template>
<script>
export default {
  name: 'MenuItem',
  props: {
    menuItem: {
      type: Object,
      default() {
        return {};
      },
    },
  },
  data() {
    return {};
  },
};
</script>
<style lang="scss" scoped>
.menu-item {
    .menu-label {
        flex: 1;
        min-width: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .menu-icon {
        flex: 0 0 21px;
        min-width: 18px;
        height: 21px;
        min-height: 18px;
        margin-right: 10px;
        opacity: 0.92;
        transition: transform 0.24s ease, opacity 0.24s ease;
    }

    :deep(.ant-menu-item),
    :deep(.ant-menu-submenu-title) {
        display: flex;
        align-items: center;

        .ant-menu-title-content {
            display: flex;
            flex: 1;
            align-items: center;
            min-width: 0;
            overflow: hidden;
        }
    }

    :deep(.ant-menu-item-selected),
    :deep(.ant-menu-submenu-selected > .ant-menu-submenu-title),
    :deep(.ant-menu-item:hover),
    :deep(.ant-menu-submenu-title:hover) {
        .menu-icon {
            opacity: 1;
            transform: translateX(1px);
        }
    }
}
</style>
