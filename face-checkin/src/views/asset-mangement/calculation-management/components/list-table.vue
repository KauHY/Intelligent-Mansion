<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-21 16:28:48
-->
<template>
  <div class="menu-list">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('export')"
        >
          导出
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #goodsName="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details', record)"
            >{{ text }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :menu-list="tableList"
      :inspection-types="options['INSPECTION_TYPE']"
      @operate:success="operateSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, computed, watch, onActivated, nextTick,
} from 'vue';
import { useRouter } from 'vue-router';
import dayjs from 'dayjs';
import OutInStock, { ReceiveDetails } from '~models/asset-mangement';
import Park from '~models/park';

import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import operateDialog from './operate-dialog.vue';
import {
  getColumns, stockTypeList,
} from '../config';

type Operatetypes = 'setting' | 'export' | 'details'//
const outInStockInspectionInessInstance = new OutInStock();

export default defineComponent({
  name: 'MenuList',
  components: { searchBox },
  props: {
    selectTreeNode: {
      type: Object as PropType<{name:string, key:string}>,
      default: () => ({}),
    },
    refresh: {
      type: String as PropType<'refresh'|''>,
      default: '',
    },
  },
  emits: ['on-refresh'],
  setup(props, { emit }) {
    const router = useRouter();
    const options = ref<DictionaryMap<['INSPECTION_TYPE']>>({
      INSPECTION_TYPE: [],
    });
    const columns = getColumns();
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['goodsName', 'parkOrgId', 'month']>>({
      goodsName: {
        tag: 'input',
        value: '',
        placeholder: '请输入物品名称',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
      },
      month: {
        tag: 'month-picker',
        value: dayjs(new Date()).format('YYYY-MM'),
      },
    });

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage(outInStockInspectionInessInstance.inventoryManagerListPage, searchConf, operateDialog, outInStockInspectionInessInstance.inventoryManagerListPageExport);
    const operateSuccess = () => {
      search(true);
    };
    onActivated(() => { // 返回时刷新列表数据
      // console.log('激活了-----', props.refresh);
      if (props.refresh) operateSuccess();
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?: any) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'export': {
          await download();
          break;
        }
        case 'details': {
          const { data } = await outInStockInspectionInessInstance.inventoryManagerDetails(record.goodsId);

          operateDialogConf.data = data as any;
          operateDialogConf.visible = true;
          break;
        }
        default: break;
      }
    };

    search(true);
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['INSPECTION_TYPE']>(['INSPECTION_TYPE']);
    };
    onMounted(() => {
      init();
    });
    return {
      columns,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      operateSuccess,
      options,
      searchConf,
    };
  },
});
</script>
