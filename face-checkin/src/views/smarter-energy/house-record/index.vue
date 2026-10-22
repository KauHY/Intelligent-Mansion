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
          row-key="_id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          custom-pagination
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
    <HouseEditAdd
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
import { useRouter } from 'vue-router';
import Park from '~models/park';
import { useHashMapDics } from '~composition/useHashMapDic';
import Apartment from '~models/smarter-energy';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import CompanyMangement from '~models/company-mangement';
import { useParkInfoOpts } from '~composition/park';
import { statusName } from './config';
import HouseEditAdd from './components/MaterialEditAdd.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import' |'disable' |'export'
const CustomerVoReq = new Apartment();
export default defineComponent({
  components: { searchBox, HouseEditAdd },
  setup() {
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const router = useRouter();
    const options = useHashMapDics<['HOUSE_FUNCTION', 'CONTRACT_TYPE']>(['HOUSE_FUNCTION', 'CONTRACT_TYPE']);
    const {
      parkOpts,
      buildingOpts,
      houseOpts,
      getParkOpts,
      getHouseOpts,
      getBuildingOpts,
    } = useParkInfoOpts();
    const searchConf = reactive<SearchConf<['buildingId', 'type', 'houseId', 'parkOrgId', 'companyName', 'settledMonth']>>({
      settledMonth: {
        tag: 'month-picker',
        value: '',
        placeholder: '请选择结算月份',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择所属园区',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange: (value: string) => {
          searchConf.buildingId.value = '';
          searchConf.houseId.value = '';
          getBuildingOpts(value);
        },
      },
      companyName: {
        tag: 'input',
        placeholder: '请输入企业名称',
        value: '',
      },

      buildingId: {
        tag: 'select',
        options: buildingOpts,
        value: '',
        placeholder: '请选择所属楼栋',
        handleChange: (value:string) => {
          searchConf.houseId.value = '';
          getHouseOpts(value);
        },
      },
      houseId: {
        tag: 'select',
        value: '',
        options: houseOpts,
        filterOption: true,
        showSearch: true,
        placeholder: '请选择所属房屋',
      },
      type: {
        tag: 'input',
        value: 0,
        hidden: true,
        placeholder: '',
      },

    });
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
      getParkOpts();
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: any) => {
      let data = null;
      switch (type) {
        case 'export': {
          await download();
          break;
        }
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
    const changeFunch = async (value:any) => {
      let string = '';
      value.forEach((item: any) => {
        string += `${item.houseName},`;
      });
      console.log(string, 'string');
      return string;
    };
    const operateSuccess = () => {
      search(true);
    };
    init();
    return {
      options,
      changeFunch,
      parkOpts,
      tagOpts,
      statusName,
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
