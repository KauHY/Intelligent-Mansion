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
          @click="manipulate('export')"
        >
          导出
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{ scroll }">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          custom-pagination
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #bodyCell="{ column, text,record }">
            <template v-if="column.dataIndex === 'settledMonth'">
              <a
                href="javascript:;"
                class="text-link"
                @click="manipulate('detail', record)"
              >{{ text }}</a>
            </template>
            <template v-if="column.dataIndex === 'contractType'">
              <span>

                {{ options?.CONTRACT_TYPE[record.contractType]?.label || '-' }}</span>
            </template>
            <template v-if="column.dataIndex === 'houseNames'">
              <span
                href="javascript:;"
              >{{ record.houseNames ? record.houseNames?.join(';') : '-' }}</span>
            </template>
            <template v-if="column.dataIndex === 'houseFunch'">
              <span>
                {{ record.deviceType === 6 ? '电能耗' : '水能耗' }}
              </span>
            </template>
            <template v-if="column.dataIndex === 'consumption'">
              <span>
                {{ record.totalAmount }}
              </span>
            </template>
          </template>
        </v-table>
      </template>
    </search-box>
    <ApartmentEditAdd
      v-if="customerFlag"
      ref="customer"
      v-model:show="customerFlag"
      :customer-config="customerConfig"
      @refresh="operateSuccess"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, defineComponent, h,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import { useHashMapDics } from '~composition/useHashMapDic';
import Apartment from '~models/smarter-energy';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import CompanyMangement from '~models/company-mangement';
import ApartmentEditAdd from './components/MaterialEditAdd.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import' |'disable' |'export'
const CustomerVoReq = new Apartment();
export default defineComponent({
  components: { searchBox, ApartmentEditAdd },
  setup() {
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const options = useHashMapDics<['HOUSE_FUNCTION', 'CONTRACT_TYPE']>(['HOUSE_FUNCTION', 'CONTRACT_TYPE']);
    const searchConf = reactive<SearchConf<[ 'type', 'parkOrgId', 'companyName', 'settledMonth']>>({
      settledMonth: {
        tag: 'month-picker',
        value: '',
        placeholder: '请选择结算月份',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange: (value: string) => {
        },
      },
      companyName: {
        tag: 'input',
        placeholder: '请输入企业名称',
        value: '',
      },
      type: {
        tag: 'input',
        value: 1,
        hidden: true,
        placeholder: '',
      },

    });
    const parkOpts = ref<SelectOptions>([]);
    const tagOpts = ref<SelectOptions>([]);
    const columns = [
      {
        title: '结算月份',
        dataIndex: 'settledMonth',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '所属园区',
        dataIndex: 'parkOrgName',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '企业名称',
        dataIndex: 'companyName',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '合同类型',
        dataIndex: 'contractType',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '房源名称',
        dataIndex: 'houseNames',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '能耗类别',
        dataIndex: 'houseFunch',
      },
      {
        title: '本月用量',
        dataIndex: 'consumption',
      },

    ];
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      CustomerVoReq.listMeterConsumptionPage,
      searchConf,
      null,
      CustomerVoReq.downloadMeterConsumption,
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: any) => {
      let data = null;
      switch (type) {
        case 'export': {
          await download();
          break;
        }
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await CustomerVoReq.findByIdlistMeterConsumption(record.id);
          customerConfig.value = {
            title: '用量明细',
            type,
            customerData: data,
          };
          customerFlag.value = true;
          break;
        default:
          break;
      }
    };
    const operateSuccess = () => {
      search(true);
    };
    init();
    return {
      options,
      parkOpts,
      tagOpts,
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
    };
  },
});
</script>
