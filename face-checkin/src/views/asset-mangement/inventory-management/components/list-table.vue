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
          @click="operate('add')"
        >
          物品入库
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('import')"
        >
          导入
        </a-button>&nbsp;&nbsp;
        <!-- <a-button
          type="primary"
          @click="operate('export')"
        >
          批量导出
        </a-button>&nbsp;&nbsp; -->
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
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template
            #operate="{record}"
          >
            <div>
              <a-button
                type="link"
                @click="operate('outInStock',record)"
              >
                出入库
              </a-button>
              <a-button
                type="link"
                @click="operate('stockEdit',record)"
              >
                修改库存
              </a-button>
            </div>
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
    <component
      :is="inAndOutStockConf.cp"
      v-model:visible="inAndOutStockConf.visible"
      :default-data="inAndOutStockConf.data"
      :type="inAndOutStockConf.type"
      :title="inAndOutStockConf.title"
      :stock-mode="stockMode"
      :menu-list="tableList"
      :inspection-types="options['INSPECTION_TYPE']"
      @operate:success="operateSuccess"
    />
    <import-table
      :import-url="URLS.inventoryListPageImport"
      :visible="importTableRef.visible"
      template-key="批量导入模板/物品入库模板.xlsx"
      @operate:success="importTableRef.visible = false;search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, computed, watch, onActivated, nextTick,
} from 'vue';
import { useRouter } from 'vue-router';
import PatrolInspection, { GoodsCard, StockOperation } from '~models/asset-mangement';
import Park from '~models/park';

import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import URLS from '~models/urlConstant/asset-mangement';
import operateDialog from './operate-dialog.vue';
import outInStockDialog from './out-in-stock-dialog.vue';
import importTable from '../../components/import-table/index.vue';
import {
  statusName,
  getColumns,
  orgTypeNames,
  stockTitleName,
} from '../config';

type Operatetypes = 'add'|'edit'|'del' | 'details' |'import'|'export'|'outInStock'|'stockEdit'//
const patrolInspectionInessInstance = new PatrolInspection();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox, outInStockDialog, importTable },
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
    const stockMode = ref<number | null>(null);
    const searchConf = reactive<SearchConf<['goodsName', 'parkOrgId', 'isMinStock']>>({
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
      isMinStock: {
        tag: 'select',
        value: '',
        placeholder: '≤库存下限',
        options: () => [{ label: '≤库存下限', value: 1 }, { label: '>库存下限', value: 0 }],
      },
    });

    const setWarningConf = reactive<{visible: boolean, goodsName?: string, id?: string, minStock: number | null }>({
      visible: false,
      goodsName: '',
      id: '',
      minStock: null,
    });
    const importTableRef = reactive<{visible: boolean}>({
      visible: false,
    });

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(patrolInspectionInessInstance.inventoryListPage, searchConf, operateDialog);
    const {
      operateDialogConf: inAndOutStockConf,
    } = useSearchListPage(patrolInspectionInessInstance.inventoryListPage, searchConf, outInStockDialog);
    const operateSuccess = () => {
      search(true);
    };
    onActivated(() => { // 返回时刷新列表数据
      // console.log('激活了-----', props.refresh);
      if (props.refresh)operateSuccess();
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:GoodsCard) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'export': {
          console.log('导出');
          break;
        }
        case 'import': {
          importTableRef.visible = true;
          break;
        }
        case 'add':
        case 'details': {
          operateDialogConf.visible = true;
          operateDialogConf.data = record as GoodsCard;
          operateDialogConf.title = stockTitleName[type];
          break;
        }
        case 'outInStock': {
          inAndOutStockConf.visible = true;
          inAndOutStockConf.data = record as GoodsCard;
          inAndOutStockConf.title = stockTitleName[type];
          stockMode.value = null;
          break;
        }
        case 'stockEdit': {
          inAndOutStockConf.visible = true;
          inAndOutStockConf.data = record as GoodsCard;
          inAndOutStockConf.title = stockTitleName[type];
          stockMode.value = StockOperation.EDIT_STOCK as number;
          break;
        }
        default: break;
      }
    };

    const setWarningOperateClose = (isSave?: boolean) => {
      if (isSave) search(true);
      setWarningConf.visible = false;
    };

    search(true);
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['INSPECTION_TYPE']>(['INSPECTION_TYPE']);
      // console.log(options.value, '-------options.value');
    };
    onMounted(() => {
      init();
    });
    return {
      columns,
      statusName,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      operateSuccess,
      orgTypeNames,
      options,
      setWarningConf,
      setWarningOperateClose,
      inAndOutStockConf,
      stockMode,
      importTableRef,
      URLS,
    };
  },
  // async mounted() {
  //   console.log('xxxx');
  // },
});
</script>
