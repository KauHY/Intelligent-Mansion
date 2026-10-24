<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-04 18:06:47
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="manipulate('export')"
        >
          导出
        </a-button>
      </template>
      <template #table="{ scroll }">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #biddingDocumentsPaid="{ record }">
            <span>{{ record.biddingDocumentsPaid===0?'未支付':'已支付' }}</span>
          </template>
          <template #marginPaid="{ record }">
            <span>{{ record.marginPaid===0?'未缴纳':'已缴纳' }}</span>
          </template>
          <template #operate="{ record }">
            <div>
              <a-button
                type="link"
                @click="manipulate('del', record)"
              >
                确认
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <ProcurementAddEdit
      v-if="customerFlag"
      ref="customer"
      v-model:show="customerFlag"
      :customer-config="customerConfig"
      @refresh="operateSuccess"
    />
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      :import-excel-config="importExcelConfig"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';
import { message } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import CustomerVo, { tender } from '~models/company';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import { useParkInfoOpts } from '~composition/park';
import { getCustomerTagColumns } from './config';
import ProcurementAddEdit from './components/ProcurementAddEdit.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}

type Operatetypes = 'add' | 'export' | 'del' | 'detail' | 'import'
const CustomerVoReq = new CustomerVo();
export default defineComponent({
  components: { searchBox, ProcurementAddEdit, ImportExcel },
  props: {
    purchaseId: {
      type: String,
      default: '',
    },
  },
  setup() {
    const router = useRouter();
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = useHashMapDics<['SYSTEM_TYPE']>(['SYSTEM_TYPE']);
    const {
      getParkOpts,
    } = useParkInfoOpts();
    const searchConf = reactive<SearchConf<[]>>({
    });
    const columns = getCustomerTagColumns(options);
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      CustomerVoReq.listTenderRecordPage,
      searchConf,
      null,
      CustomerVoReq.exportTenderRecord,
    );
    const jumpPage = () => {
      router.push({ path: '/procurement-services/procurement-detail', query: { } });
    };
    const manipulate = async (type: Operatetypes, record?: tender) => {
      const data = null;
      switch (type) {
        case 'export':
          await download();
          break;
        case 'del':
          if (!record) return;
          await CustomerVoReq.confirmTenderRecord(record.id);
          message.success('操作成功');
          operateSuccess();
          break;

        default:
          break;
      }
    };
    const operateSuccess = () => {
      search(true);
    };
    const importSuccess = async () => {
      try {
        operateSuccess();
        message.success('操作成功');
      } catch (error) {
        console.log('上传失败');
      }
    };
    search(true);
    const init = async () => {
      getParkOpts();
    };
    onMounted(() => {
      init();
    });
    return {
      options,
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      download,
      operateSuccess,
      manipulate,
      customerFlag,
      customerConfig,
      importFlag,
      importExcelConfig,
      importSuccess,
      jumpPage,
    };
  },
});
</script>
