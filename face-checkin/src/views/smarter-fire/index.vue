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
                v-if="record.status===1"
                type="link"
                @click="manipulate('detail', record)"
              >
                处理详情
              </a-button>
              <a-button
                v-if="record.status===0"
                type="link"
                @click="manipulate('edit', record)"
              >
                报警处理
              </a-button>
            </div>
          </template>
          <template #status="{record}">
            <span>{{ record.status===0 ?'待处理':'已处理' }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <FireEditAdd
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
  reactive, ref, defineComponent,
} from 'vue';
import { message } from 'ant-design-vue';
import Park from '~models/park';
import { useHashMapDics } from '~composition/useHashMapDic';
import Fire, { AddParams } from '~models/smarter-fire';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import { getCustomerColumns } from './config';
import FireEditAdd from './components/FireEditAdd.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import'
const CustomerVoReq = new Fire();
export default defineComponent({
  components: { searchBox, FireEditAdd, ImportExcel },
  setup() {
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = useHashMapDics<['WARN_TYPE', 'SYSTEM_TYPE', 'FILE_DEVICE_WARN_TYPE']>(['WARN_TYPE', 'SYSTEM_TYPE', 'FILE_DEVICE_WARN_TYPE']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['deviceName', 'parkOrgId', 'warnType', 'warnTime']>>({
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择所属园区',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
      },
      // deviceId: {
      //   tag: 'input',
      //   value: '',
      //   placeholder: '请输入设备ID',
      // },
      deviceName: {
        tag: 'input',
        value: '',
        placeholder: '请输入设备名称',
      },
      warnType: {
        tag: 'select',
        placeholder: '请选择报警类型',
        value: '',
        options: () => options.FILE_DEVICE_WARN_TYPE,
      },
      warnTime: {
        tag: 'date-range',
        placeholder: ['开始时间', '结束时间'],
        endTimeKey: 'warnTimeEnd',
        startTimeKey: 'warnTimeStart',
        value: '',
      },

    });
    const columns = getCustomerColumns(options);
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(
      CustomerVoReq.listMeter,
      searchConf,
      null,
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: AddParams) => {
      let data = null;
      if (!record) return;
      data = await CustomerVoReq.findListMeterById(record.id);
      customerConfig.value = {
        title: type === 'detail' ? '报警详情' : '报警处理',
        type,
        customerData: data,
      };
      customerFlag.value = true;
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
    init();
    return {
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
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
