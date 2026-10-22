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
          新增
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('import')"
        >
          导入
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
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template
            #operate="{record}"
          >
            <div>
              <a-button
                type="link"
                @click="operate('setWarn',record)"
              >
                设置预警值
              </a-button>
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>
              <a-button
                type="link"
                @click="operate('del',record)"
              >
                删除
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
    <set-warning
      v-if="setWarningConf.visible"
      :id="setWarningConf.id"
      :visible="setWarningConf.visible"
      :goods-name="setWarningConf.goodsName"
      :min-stock="setWarningConf.minStock"
      @operate:close="setWarningOperateClose"
    />
    <import-table
      :import-url="URLS.goodsListPageImport"
      :visible="importTableRef.visible"
      template-key="批量导入模板/物品导入模板.xlsx"
      @operate:success="importTableRef.visible = false;search(true)"
    />
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      :import-excel-config="importExcelConfig"
      template-key="批量导入模板/物品导入模板.xlsx"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, computed, watch, onActivated,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import GoodsInspection, { GoodsCard } from '~models/asset-mangement';
import Park from '~models/park';

import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import URLS from '~models/urlConstant/asset-mangement';
import operateDialog from './operate-dialog.vue';
import setWarning from './set-warning.vue';
import importTable from '../../components/import-table/index.vue';
import {
  statusName,
  getColumns,
  operateDialogTitleName,
  orgTypeNames,
} from '../config';

type Operatetypes = 'add'|'edit'|'del' | 'details' |'import'|'export'|'setWarn'//
const goodsInspectionInessInstance = new GoodsInspection();
export default defineComponent({
  name: 'MenuList',
  components: {
    searchBox, setWarning, importTable, ImportExcel,
  },
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
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const { addCachePage } = useCachePage();
    const router = useRouter();
    const options = ref<DictionaryMap<['INSPECTION_TYPE']>>({
      INSPECTION_TYPE: [],
    });
    const columns = getColumns();
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['goodsName', 'parkOrgId']>>({
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
      download,
    } = useSearchListPage(goodsInspectionInessInstance.goodsListPage, searchConf, operateDialog, goodsInspectionInessInstance.goodsListPageExport);
    const operateSuccess = () => {
      search(true);
      // emit('on-refresh');
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
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.goodsName}】吗？`,
            onOk: async () => {
              await goodsInspectionInessInstance.patrolPlaceDel(record.id as string);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        }
        case 'export': {
          console.log('导出');
          await download();
          break;
        }
        case 'import': {
          importFlag.value = true;
          importExcelConfig.value = {
            title: '电表导入',
            postData: 1,
            onConfim: goodsInspectionInessInstance.goodsListPageImport,
          };
          break;
        }
        case 'setWarn': {
          setWarningConf.id = record?.id;
          setWarningConf.goodsName = record?.goodsName;
          setWarningConf.minStock = record?.minStock || null;
          setWarningConf.visible = true;
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.data = record as GoodsCard;
          operateDialogConf.title = operateDialogTitleName[type];
          break;
        }
      }
    };

    const setWarningOperateClose = (isSave?: boolean) => {
      if (isSave) search(true);
      setWarningConf.visible = false;
    };
    const importSuccess = async () => {
      try {
        // await CustomerVoReq.customerRemoveApi('55555');
        message.success('操作成功');
      } catch (error) {
        console.log('上传失败');
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
      importTableRef,
      URLS,
      importFlag,
      importExcelConfig,
      importSuccess,
    };
  },
});
</script>
