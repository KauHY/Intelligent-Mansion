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
          <template #applyNo="{text,record}">
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
                v-if="AuditStateType[record.auditState] === '归还待审核'"
                type="link"
                @click="operate('auditInStock',record)"
              >
                审核入库
              </a-button>
              <a-button
                v-else-if="AuditStateType[record.auditState] === '领用待审核'"
                type="link"
                @click="operate('auditOutStock',record)"
              >
                审核出库
              </a-button>
              <p v-else>
                -
              </p>
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
    <details-dialog
      :visible="detailsDialogConf.visible"
      :data="detailsDialogConf.data"
      @onClose="onDetailsDialogClose"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, computed, watch, onActivated, nextTick,
} from 'vue';
import { useRouter } from 'vue-router';
import * as dayjs from 'dayjs';
import PatrolInspection, { GoodsCard, ReceiveDetails } from '~models/asset-mangement';
import Park from '~models/park';

import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import operateDialog from './operate-dialog.vue';
import detailsDialog from './details-dialog.vue';
import {
  AuditStateType,
  getColumns,
  stockTitleName,
  StockStateType,
  receiveDetailList,
  testReceiveDetailList,
} from '../config';

type Operatetypes = 'add'|'edit'|'del' | 'details' |'import'|'export'|'outInStock'|'stockEdit'|'auditInStock'|'auditOutStock'//
const patrolInspectionInessInstance = new PatrolInspection();

export default defineComponent({
  name: 'MenuList',
  components: { searchBox, detailsDialog },
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
    const startTime = `${dayjs().format('YYYY')}-${dayjs().subtract(1, 'day').format('MM')}-01`;
    const endTime = dayjs().subtract(1, 'day').format('YYYY-MM-DD');
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['employeeName', 'goodsName', 'parkOrgId', 'needReturn', 'auditState', 'stockState', 'date']>>({
      employeeName: {
        tag: 'input',
        value: '',
        placeholder: '请输入领用人',
      },
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
      needReturn: {
        tag: 'select',
        value: '',
        placeholder: '请选择是否需归还',
        options: () => [{ label: '否', value: 0 }, { label: '是', value: 1 }],
      },
      auditState: {
        tag: 'select',
        value: '',
        placeholder: '请选择审核状态',
        options: () => AuditStateType.filter(Boolean).map((item, index) => ({ label: item, value: index + 1 })),
      },
      stockState: {
        tag: 'select',
        value: '',
        placeholder: '请选择出入库状态',
        options: () => Object.values(StockStateType).filter(Boolean).map((item, index) => ({ label: item, value: index + 1 })),
      },
      date: {
        tag: 'date-range',
        value: [null, null],
        placeholder: ['申请开始时间', '申请结束时间'],
        startTimeKey: 'applyTimeStart',
        endTimeKey: 'applyTimeEnd',
      },
    });

    const detailsDialogConf = reactive({
      visible: false,
      data: {},
    });

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(patrolInspectionInessInstance.receiveListPage, searchConf, operateDialog);
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
    const operate = async (type:Operatetypes, record?: any) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'export': {
          console.log('导出');
          break;
        }
        case 'import': {
          console.log('导入');
          break;
        }
        case 'details':
        case 'auditOutStock':
        case 'auditInStock': {
          const { mobile, receiveDetailList } = await patrolInspectionInessInstance.getReceiveDetail(record.id);
          const remark = receiveDetailList[receiveDetailList.length - 1].remark; // 备注
          const thisApplyTime = receiveDetailList[receiveDetailList.length - 1].createTime; // 本次申请时间
          const data = {
            ...record, mobile, remark, thisApplyTime, receiveDetailList,
          } as any;
          if (type === 'details') {
            detailsDialogConf.visible = true;
            detailsDialogConf.data = data;
          } else {
            operateDialogConf.data = data;
            operateDialogConf.visible = true;
            operateDialogConf.title = stockTitleName[type];
          }
          console.log(data, '..data');
          break;
        }

        default: break;
      }
    };

    const onDetailsDialogClose = () => {
      detailsDialogConf.visible = false;
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
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      operateSuccess,
      options,
      detailsDialogConf,
      onDetailsDialogClose,
      StockStateType,
      AuditStateType,
      // inAndOutStockConf,
    };
  },
  // async mounted() {
  //   console.log('xxxx');
  // },
});
</script>
