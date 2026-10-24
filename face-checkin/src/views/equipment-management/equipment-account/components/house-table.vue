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
          @click="operate('import')"
        >
          导入
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('export')"
        >
          导出
        </a-button>
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
          <template #state="{ record }">
            <span>{{ options.LEDGER_STATE[record.state]?.label }}</span>
          </template>
          <template #name="{ record }">
            <span
              class="text-link"
              @click="operate('detail', record)"
            >{{ record.name }}</span>
          </template>
          <template
            #operate="{record}"
          >
            <div>
              <a-button
                v-if="record.state===1"
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
              <a-button
                type="link"
                @click="operate('copy',record)"
              >
                复制
              </a-button>
              <a-button
                v-if="record.state===1"
                type="link"
                @click="operate('stop',record)"
              >
                报废
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :dialog-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      @operate:success="operateSuccess"
    />
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      :import-excel-config="importExcelConfig"
      template-key="批量导入模板/设备台账导入模板.xlsx"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, watch, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import URLS from '~models/urlConstant/equipment-management';
import EquipmentManagement, { HouseInfo, SearchParams } from '~models/equipment-management';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';

import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import operateDialog from './operate-dialog.vue';
import {
  columns, houseDialogTitleNames,
} from '../config';
import importTable from '../../components/index.vue';

type Operatetypes = 'edit'|'del'|'add' | 'copy' | 'export' | 'stop' |'import' |'detail'
export default defineComponent({
  name: 'MenuList',
  components: { searchBox, importTable, ImportExcel },
  props: {
    selectTreeNode: {
      type: Object as PropType<{name:string, key:string, type:number, categoryId:string, hierarchy:number, parentId:string}>,
      default: () => ({}),
    },
    currentValue: {
      type: Object as PropType<{value:string, label:string, type:number}>,
      default: () => ({}),
    },
  },
  emits: ['on-refresh'],
  setup(props, { emit }) {
    const floorInstance = new EquipmentManagement();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const importTableRef = reactive<{visible: boolean}>({
      visible: false,
    });
    const searchConf = reactive<SearchConf<['name', 'parkOrgId', 'buildingId', 'houseId', 'categoryId', 'hierarchy', 'parentId']>>({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入设备名称',
      },
      parkOrgId: {
        tag: 'input',
        value: '',
        hidden: true,
      },
      buildingId: {
        tag: 'input',
        value: '',
        hidden: true,
        placeholder: '我出现了ccc',
      },
      categoryId: {
        tag: 'input',
        value: '',
        hidden: true,
        placeholder: '我出现了ccc',
      },
      hierarchy: {
        tag: 'input',
        value: '',
        hidden: true,
        placeholder: '我出现了ccc',
      },
      parentId: {
        tag: 'input',
        value: '',
        hidden: true,
        placeholder: '我出现了ccc',
      },
      houseId: {
        tag: 'input',
        value: '',
        hidden: true,
      },
    });

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<HouseInfo, SearchParams>(floorInstance.listEquipmentLedgerPage, searchConf, operateDialog, floorInstance.goodsListPageExport);

    const operateSuccess = () => {
      search(true);
      emit('on-refresh');
    };

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:HouseInfo) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          console.log('删除');
          Modal.confirm({
            content: '确定删除吗？',
            onOk: async () => {
              await floorInstance.removeEquipmentLedger(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        }
        case 'export': {
          await download();
          break;
        }
        case 'import': {
          importFlag.value = true;
          importExcelConfig.value = {
            title: '设备导入',
            categoryType: 1,
            onConfim: floorInstance.goodsListPageimport,
          };
          break;
        }
        case 'edit': {
          if (!record) return;
          operateDialogConf.visible = true;
          operateDialogConf.title = houseDialogTitleNames[type];
          Object.assign(operateDialogConf.data, record, {
            label: record?.parkOrgName,
            floorName: record?.buildingName,
            type: 'edit',
          });
          break;
        }
        case 'copy': {
          if (!record) return;
          const data = await floorInstance.newEquipmentFindById(record.id);
          console.log(data);
          operateDialogConf.visible = true;
          operateDialogConf.title = houseDialogTitleNames[type];
          Object.assign(operateDialogConf.data, record, {
            label: record?.parkOrgName,
            floorName: record?.buildingName,
            type: 'copy',
          });
          break;
        }
        case 'stop': {
          if (!record) return;
          Modal.confirm({
            content: '确定要报废此设备？报废后的设备将不能再使用。',
            onOk: async () => {
              await floorInstance.scrapmultistage(
                record.id,
              );
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'detail': {
          if (!record) return;
          const data = await floorInstance.newEquipmentFindById(record.id);
          operateDialogConf.visible = true;
          operateDialogConf.title = houseDialogTitleNames[type];
          Object.assign(operateDialogConf.data, record, {
            label: record?.parkOrgName,
            floorName: record?.buildingName,
            type: 'detail',
          });
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.title = houseDialogTitleNames[type];
          Object.assign(operateDialogConf.data, record, {
            label: record?.parkOrgName,
            floorName: record?.buildingName,
          });
        }
      }
    };

    watch(props.selectTreeNode, (treeNode) => {
      if (!treeNode.key) return;
      const {
        key, hierarchy, parentId,
      } = treeNode;
      console.log(treeNode, hierarchy, 888888888888);
      if (hierarchy === 1 || !hierarchy) { // 选中园区
        searchConf.parkOrgId.value = key;
        searchConf.categoryId.value = '';
      } else {
        searchConf.parkOrgId.value = parentId;
        searchConf.categoryId.value = key;
      }
      search(true);
    }, {
      deep: true,
    });

    const options = ref<DictionaryMap<['LEDGER_STATE']>>({
      LEDGER_STATE: [],
    });
    const importSuccess = async () => {
      try {
        operateSuccess();
        message.success('操作成功');
      } catch (error) {
        console.log('上传失败');
      }
    };
    const init = async () => {
      options.value = await useHashMapDic<['LEDGER_STATE']>(['LEDGER_STATE']);
    };
    init();
    return {
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      importTableRef,
      operateSuccess,
      options,
      URLS,
      importFlag,
      importExcelConfig,
      importSuccess,
    };
  },
});
</script>
