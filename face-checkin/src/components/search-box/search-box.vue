<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 15:28:15
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-17 10:15:16
-->
<template>
  <div class="table-search-box">
    <div
      v-show="showHeader"
      class="search-header"
    >
      <a-form layout="inline">
        <a-form-item
          v-for="fieldConf in searchConf"
          :key="fieldConf.name"
          :style="{width:fieldConf.width||''}"
          :name="fieldConf.name"
        >
          <form-field
            v-model:value="fieldConf.value"
            :field-conf="fieldConf"
          />
        </a-form-item>
        <slot name="searchOperate" />
      </a-form>
    </div>
    <div class="search-content">
      <slot
        name="table"
        :scroll="{y: '58vh'}"
      />
    </div>
  </div>
</template>
<script lang='ts'>
import { defineComponent } from 'vue';
import formField from './components/form-field.vue';

export * from './type';
export default defineComponent({
  name: 'SearchBox',
  components: { formField },
  props: {
    searchConf: {
      type: Object,
      default() {
        return {};
      },
    },
    columns: {
      type: Array,
      default() {
        return [];
      },
    },
    dataSource: {
      type: Array,
      default() {
        return [];
      },
    },
    tableRowKey: {
      type: String,
      default: 'id',
    },
    showHeader: {
      type: Boolean,
      default: true,
    },
  },
});
</script>
<style lang="less" scoped>
.table-search-box {
    display: flex;
    flex-direction: column;
    height: 100%;
    min-height: 0;
    background: transparent;

    .search-header {
        position: relative;
        padding: 14px 16px 16px;
        overflow: hidden;
        background: linear-gradient(180deg, rgba(255, 255, 255, 0.78) 0%, rgba(244, 249, 255, 0.9) 100%);
        border: 1px solid rgba(var(--theme-primary-rgb), 0.12);
        border-radius: 22px;
        box-shadow:
            inset 0 1px 0 rgba(255, 255, 255, 0.84),
            0 16px 30px rgba(8, 24, 40, 0.06);
        backdrop-filter: blur(16px);

        &::before,
        &::after {
            position: absolute;
            pointer-events: none;
            content: '';
        }

        &::before {
            top: 0;
            left: 20px;
            width: 132px;
            height: 2px;
            background:
                linear-gradient(
                    90deg,
                    rgba(var(--theme-primary-rgb), 0),
                    rgba(var(--theme-primary-rgb), 0.7),
                    rgba(var(--theme-primary-rgb), 0)
                );
            box-shadow: 0 0 12px rgba(var(--theme-primary-rgb), 0.14);
        }

        &::after {
            top: 10px;
            right: 12px;
            width: 34px;
            height: 18px;
            background: none;
            border-top: 1px solid rgba(var(--theme-primary-rgb), 0.14);
            border-right: 1px solid rgba(var(--theme-primary-rgb), 0.14);
            border-radius: 0 10px 0 0;
            opacity: 0.8;
        }
    }

    :deep(.ant-form) {
        position: relative;
        z-index: 1;
        display: flex;
        flex-wrap: wrap;
        gap: 12px 16px;
        align-items: flex-start;
    }

    :deep(.ant-form-item) {
        margin-right: 0;
        margin-bottom: 0;
    }

    .search-content {
        display: flex;
        flex: 1;
        flex-direction: column;
        min-height: 0;
        padding: 18px 0 0;
    }

    :deep(.search-content > .v-table-box) {
        flex: 1;
        min-height: 0;
    }
}

</style>
