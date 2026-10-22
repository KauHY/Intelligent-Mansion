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
          离场结算
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
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #companyName="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >{{ record.companyName }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <!-- 企业信息 -->
    <InformationEditAdd
      v-if="informationFlag"
      ref="customer"
      v-model:show="informationFlag"
      :contract-type="contractType"
      :customer-config="informationConfig"
      @refresh="operateSuccess()"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import CustomerVo, { SupplyListItem } from '~models/company';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import Park from '~models/park';
import CollectFee, { ChargeItem } from '~models/collect-fee';
import { getCustomerColumns } from './config';
import InformationEditAdd from './components/operate-dialog.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}

type Operatetypes = 'add' | 'detail' | 'export'
const CustomerVoReq = new CustomerVo();
export default defineComponent({
  components: {
    searchBox, InformationEditAdd,
  },
  props: {
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5>,
      default: 3,
    },
  },
  setup(props) {
    const router = useRouter();
    const visible = ref<boolean>(false);
    const informationFlag = ref<Boolean>(false);
    const informationConfig = ref<AdvertConfig>();
    const parkOpts = ref<SelectOptions>([]);
    const collectFeeInessInstance = new CollectFee();

    const searchConf = reactive<SearchConf<['parkOrgId', 'companyName', 'contractType']>>({
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择所属园区',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
      },
      companyName: {
        tag: 'input',
        placeholder: '请输入企业名称',
        value: '',
      },
      contractType: {
        tag: 'input',
        value: props.contractType,
        hidden: true,
      },
    });
    const columns = getCustomerColumns();
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      collectFeeInessInstance.listRentContractLeavePage,
      searchConf,
      null,
      collectFeeInessInstance.exportsettlement,
    );
    const manipulate = async (type: Operatetypes, record?: SupplyListItem) => {
      let data = null;
      switch (type) {
        case 'add': {
          const names = {
            3: 'departureSettlement', // 租赁
            4: 'propertyDepartureSettlement', // 物业
            5: 'apartmentDepartureSettlement', // 公寓
          };
          router.push({ name: names[props.contractType] });
          break;
        }
        case 'export':
          await download();
          break;
        case 'detail':
          if (!record) return;
          data = await collectFeeInessInstance.findByIdleaveSettlement(record.id);
          informationConfig.value = {
            title: '详情',
            type,
            customerData: data,
          };
          informationFlag.value = true;
          break;
        default:
          break;
      }
    };
    const handleCancel = () => {
      visible.value = false;
    };
    const operateSuccess = (type?:any) => {
      search(true);
    };
    search(true);
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
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
      download,
      operateSuccess,
      manipulate,
      informationFlag,
      informationConfig,
      handleCancel,
      visible,
    };
  },
});
</script>
