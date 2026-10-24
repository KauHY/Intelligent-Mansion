<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-17 15:24:03
 * @LastEditTime: 2022-07-01 10:26:18
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-17 11:23:15
 * @LastEditTime: 2022-01-17 14:55:23
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    v-model:visible="currentVisible"
    :width="730"
    :title="billSchemeConfig.title"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div>
      <search-box
        :search-conf="searchConf"
        :show-header="false"
      >
        <template #table="{ scroll }">
          <v-table
            :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, onSelect: rowSelect, type: 'radio' }"
            row-key="id"
            bordered
            :columns="columns"
            :pagination="pagination"
            :data-source="tableList"
            :scroll="scroll"
            @change="pageChange"
          />
        </template>
      </search-box>
    </div>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, toRefs, PropType,
} from 'vue';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import House, { HouseInfo } from '~models/house';
import ContractVo from '~models/contract-manage';
import CollectFee, { ChargeItem } from '~models/collect-fee';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { getBillSchemeColumns } from '../config';

export default defineComponent({
  components: { searchBox },
  props: {
    billSchemeConfig: {
      type: Object,
      default: () => ({
        title: '',
        parkOrgId: '',
        type: '',
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    getBillSchemeData: (data: object) => true,
  },
  setup(props, { emit }) {
    const { parkOrgId, type } = toRefs(props.billSchemeConfig);
    const options = useHashMapDics(['FEE_CHARGING_CYCLE']);
    const searchConf = reactive<SearchConf<['parkOrgId', 'type', 'status']>>({
      parkOrgId: {
        tag: 'input',
        value: parkOrgId.value,
        hidden: true,
      },
      type: {
        tag: 'input',
        value: type.value,
        hidden: true,
      },
      status: {
        tag: 'input',
        value: 1,
        hidden: true,
      },
    });

    const selectedRowKeys = ref<string[]>([]);
    const selectedRow = ref<object>({});
    const columns = getBillSchemeColumns(options);
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(new ContractVo().listBillingSchemePageApi, searchConf);
    const confirm = () => {
      emit('getBillSchemeData', selectedRow.value);
      emit('update:show', false);
    };
    const onSelectChange = (keys: [], selectedRows: []) => {
      selectedRowKeys.value = keys;
    };
    const rowSelect = (record: ChargeItem) => {
      selectedRow.value = record;
    };
    const init = () => {
      search(true);
    };
    onMounted(async () => {
      init();
    });
    return {
      confirm,
      emit,
      columns,
      selectedRowKeys,
      onSelectChange,
      rowSelect,
      selectedRow,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      currentVisible: true,
    };
  },
});
</script>
