<!--
 * @Description: 模板字段库
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-12 15:37:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-13 14:20:18
-->
<template>
  <div class="template-field-lib content-section">
    <div class="content-scroll">
      <div class="search-box">
        <v-select
          v-model:value="type"
          style="width: 210px;"
          :allow-clear="false"
          placeholder="请选择合同类型"
        >
          <a-select-option
            v-for="opt in options.CONTRACT_TYPE"
            :key="opt.value"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
        &nbsp;&nbsp;
        <a-button
          type="primary"
          @click="search"
        >
          查询
        </a-button>
      </div>
      <div class="content-box">
        <a-collapse
          v-model:activeKey="activeKey"
          expand-icon-position="right"
        >
          <a-collapse-panel
            v-for="(filedItems,key) in fileds"
            :key="key"
            :header="options.CON_LIBRARY_TYPE[key]?.label"
          >
            <div class="item-content-box">
              <div
                v-for="(filedItem,index) in filedItems"
                :key="index"
                class="filed"
              >
                <a-button v-copy="filedItem.value">
                  {{ filedItem.label }}
                </a-button>
              </div>
            </div>
          </a-collapse-panel>
        </a-collapse>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import { use } from 'echarts';
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';

import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import ContractManage, { ContractType } from '~models/contract-manage';

export default defineComponent({
  props: {
  },
  setup() {
    const options = useHashMapDics<['CONTRACT_TYPE', 'CON_LIBRARY_TYPE']>(['CONTRACT_TYPE', 'CON_LIBRARY_TYPE']);
    // const activeKey = ref<string[]>(['1']);
    return {
      options,
      ...useSearch(),
    };
  },
});
function useSearch() {
  const type = ref<ContractType>(3);
  const fileds = ref<SelectOptionsMap>({});
  const activeKey = ref<string[]>(['1']);
  const search = async () => {
    fileds.value = await new ContractManage().getTemplateField(type.value);
    activeKey.value = Object.keys(fileds.value);
  };
  search();
  return {
    type,
    fileds,
    activeKey,
    search,
  };
}
</script>
<style lang="less" scoped>
.template-field-lib {
    padding: 20px;

    .search-box {
        display: flex;
        gap: 12px;
        align-items: center;
        padding: 14px 16px;
        margin-bottom: 20px;
        background: linear-gradient(180deg, rgba(255, 255, 255, 0.78) 0%, rgba(244, 249, 255, 0.9) 100%);
        border: 1px solid rgba(var(--theme-primary-rgb), 0.12);
        border-radius: 20px;
        box-shadow:
            inset 0 1px 0 rgba(255, 255, 255, 0.84),
            0 16px 30px rgba(8, 24, 40, 0.06);
        backdrop-filter: blur(16px);
    }

    .content-box {
        padding-top: 0;

        .item-content-box {
            display: flex;
            flex-wrap: wrap;

            >div {
                padding: 10px;
            }
        }

        :deep(.ant-collapse) {
            border-color: #eaecf1;

            .ant-collapse-header {
                background-color: #fafafa;
            }

            .ant-collapse-content,
            .ant-collapse-item {
                border-color: #eaecf1;
            }
        }
    }
}
</style>
