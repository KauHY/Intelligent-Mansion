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
          <template #pointName="{text,record}">
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
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      :import-excel-config="importExcelConfig"
      template-key="批量导入模板/巡检点导入模板.xlsx"
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
import PatrolInspection, { PatrolPlace } from '~models/patrol-inspection';
import Park from '~models/park';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import operateDialog from './operate-dialog.vue';
import {
  statusName,
  getColumns,
  operateDialogTitleName,
  orgTypeNames,
} from '../config';

type Operatetypes = 'add'|'edit'|'del' | 'details' |'import'|'export'//
const patrolInspectionInessInstance = new PatrolInspection();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox, ImportExcel },
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
    const options = ref<DictionaryMap<['INSPECTION_TYPE']>>({
      INSPECTION_TYPE: [],
    });
    const columns = getColumns(options);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['pointName', 'type', 'orgId']>>({
      orgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
      },
      pointName: {
        tag: 'input',
        value: '',
        placeholder: '请输入巡检点名称',
      },
      type: {
        tag: 'select',
        value: '',
        placeholder: '请选择巡检点类型',
        options: () => options.value.INSPECTION_TYPE,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage(patrolInspectionInessInstance.patrolPlaceListPage, searchConf, operateDialog, patrolInspectionInessInstance.patrolPlaceExport);
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
    const operate = async (type:Operatetypes, record?:PatrolPlace) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.pointName}】吗？`,
            onOk: async () => {
              await patrolInspectionInessInstance.patrolPlaceDel(record.id as string);
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
            title: '巡检点导入',
            onConfim: patrolInspectionInessInstance.patrolPlaceImportPoint,
          };
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.data = record as PatrolPlace;
          operateDialogConf.title = operateDialogTitleName[type];
        }
      }
      // if (type === 'accept') {
      //   if (!record) return;
      //   await patrolInspectionInessInstance.houseApplyForAccept(record.id as string);
      //   message.success('受理成功');
      //   operateSuccess();
      //   return;
      // }
      // operateDialogConf.visible = true;
      // operateDialogConf.title = '申请详情';
      // operateDialogConf.data = record as ViewHouseApplyInfo;
    };

    // watch(() => props.selectTreeNode.key, () => {
    //   // console.log(props.selectTreeNode.key, '----props.selectTreeNode.key');
    //   if (!props.selectTreeNode.key) return;
    //   searchConf.orgId.value = props.selectTreeNode.key;
    //   search(true);
    // });
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
      importFlag,
      importExcelConfig,
      importSuccess,
    };
  },
  // async mounted() {
  //   console.log('xxxx');
  // },
});
</script>
