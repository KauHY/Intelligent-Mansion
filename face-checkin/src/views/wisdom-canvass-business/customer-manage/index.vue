<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-08 19:13:31
-->
<template>
  <div class="jobs-mangement content-section">
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
          @click="manipulate('add')"
        >
          新增
        </a-button>&nbsp;&nbsp;
        <a-button
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
          <template #customerName="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >{{ record.name }}</span>
          </template>
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
    <CustomerEditAdd
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
      template-key="批量导入模板/客户导入模板.xlsx"
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
import CustomerVo, { CustomerListKind } from '~models/customer-manage';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import { getCustomerColumns } from './config';
import CustomerEditAdd from './components/CustomerEditAdd.vue';

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
export default defineComponent({
  components: { searchBox, CustomerEditAdd, ImportExcel },
  setup() {
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = useHashMapDics<['COMPANY_TYPE', 'CUSTOMERINFO_STATUS']>(['COMPANY_TYPE', 'CUSTOMERINFO_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['name', 'parkOrgId']>>({
      name: {
        tag: 'input',
        placeholder: '请输入客户名称',
        value: '',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
      },
    });
    const columns = getCustomerColumns(options);
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      CustomerVoReq.customerListPageApi,
      searchConf,
      null,
      CustomerVoReq.customerExportApi,
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: CustomerListKind) => {
      let data = null;
      switch (type) {
        case 'add':
          customerConfig.value = {
            title: '新增客户',
            type,
          };
          customerFlag.value = true;
          break;
        case 'import':
          importFlag.value = true;
          importExcelConfig.value = {
            title: '客户导入',
            onConfim: CustomerVoReq.listCustomerImport,

          };
          // console.log(importExcelConfig.value.getdownloadUrl());
          break;
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await CustomerVoReq.customerFindByIdApi(record.id);
          customerConfig.value = {
            title: type === 'edit' ? '编辑客户' : '客户详情',
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
              await CustomerVoReq.customerRemoveApi(record.id);
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
      operateSuccess();
    };
    init();
    return {
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
