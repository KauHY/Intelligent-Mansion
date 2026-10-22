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
        <!-- <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="manipulate('add')"
        >
          新增
        </a-button>&nbsp;&nbsp; -->
        <!-- <a-button
          type="primary"
          @click="manipulate('import')"
        >
          导入
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="download"
        >
          导出
        </a-button> -->
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
          <template #operate="{ record }">
            <div>
              <a-button
                type="link"
                @click="manipulate('edit', record)"
              >
                编辑
              </a-button>
              <a-button
                type="link"
                @click="manipulate('del', record)"
              >
                删除
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <EntryLogAddEdit
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
import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import CustomerVo, { AddParams } from '~models/equipment-management';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import { useParkInfoOpts } from '~composition/park';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
import { getCustomerColumns } from './config';
import EntryLogAddEdit from './components/EntryLogAddEdit.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}
// CUSTOMERINFO_TYPE  客户类型
// CUSTOMERINFO_STATUS 客户状态
// INDUSTRY_FIELD 行业领域
// COMPANY_TAXES_TYPE 企业纳税类型
// COMPANY_MANAGEMENT_STATUS 企业经营状态

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import'
const CustomerVoReq = new CustomerVo();
const companyInstance = new CompanyMangement();
export default defineComponent({
  components: { searchBox, EntryLogAddEdit, ImportExcel },
  setup() {
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = useHashMapDics<['ADD_DATA_SOURCE']>(['ADD_DATA_SOURCE']);
    const searchConf = reactive<SearchConf<[]>>({

    });
    const columns = getCustomerColumns(options);
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage<CompanyInfo>(
      companyInstance.searchListPage,
      searchConf,
      null,
      CustomerVoReq.exchangeFireControlDevice,
    );
    const manipulate = async (type: Operatetypes, record?: AddParams) => {
      let data = null;
      switch (type) {
        case 'add':
          customerConfig.value = {
            title: '新增设备',
            type,
          };
          customerFlag.value = true;
          break;
        case 'import':
          importFlag.value = true;
          importExcelConfig.value = {
            title: '设备导入',
            onConfim: CustomerVoReq.equipmentListPage,
          };
          // console.log(importExcelConfig.value.getdownloadUrl());
          break;
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await CustomerVoReq.findFireControlDeviceId(record.id);
          customerConfig.value = {
            title: type === 'edit' ? '编辑设备' : '设备详情',
            type,
            customerData: data,
          };
          customerFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.name}】吗？`,
            onOk: async () => {
              await CustomerVoReq.removeFireControlDevice(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
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
        // await CustomerVoReq.customerRemoveApi('55555');
        operateSuccess();
        message.success('操作成功');
      } catch (error) {
        console.log('上传失败');
      }
    };
    search(true);
    const init = async () => {
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
    };
  },
});
</script>
