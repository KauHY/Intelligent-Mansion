<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-20 17:57:33
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    v-model:visible="currentVisible"
    :width="730"
    :title="partyConfig.title"
    class="select-partyA-dialog"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div>
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
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
import BusinessInvitation, { CustomerManagementInfo } from '~models/wisdom-canvass-business';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';

export default defineComponent({
  components: { searchBox },
  props: {
    partyConfig: {
      type: Object as PropType<{ partyStr: 'partyA' | 'partyB'; title: string, parkOrgId: string }>,
      default: () => ({}),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    getPartyData: (data: CustomerManagementInfo | CompanyInfo) => true,
  },
  setup(props, { emit }) {
    const options = useHashMapDics(['COMPANY_TYPE']);
    const companyInstance = new CompanyMangement();
    const businessInvitationInstance = new BusinessInvitation();
    const { partyStr } = toRefs(props.partyConfig);
    const searchConf = reactive<SearchConf<['name', 'parkOrgId', 'companyType']>>({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入企业名称',
      },
      parkOrgId: {
        tag: 'input',
        value: props.partyConfig.parkOrgId,
        hidden: true,
      },
      companyType: {
        tag: 'select',
        value: '',
        options: options?.COMPANY_TYPE,
        placeholder: '请选择企业类型',
      },
    });
    const selectedRowKeys = ref<string[]>([]);
    const selectedRow = ref<CustomerManagementInfo | CompanyInfo>();
    const columns = [
      {
        title: '企业名称',
        dataIndex: 'name',
      },
      {
        title: '企业类型',
        dataIndex: 'companyType',
        customRender: ({ record }: { record: CompanyInfo }) => options.COMPANY_TYPE?.[record.companyType as number]?.label || '',
      },
    ];
    const apiStr = {
      partyA: companyInstance.searchListPage,
      partyB: businessInvitationInstance.customerManagementListpage,
    };
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage<CompanyInfo | CustomerManagementInfo>(
      companyInstance.searchListPage,
      searchConf,
    );
    const confirm = () => {
      if (!selectedRow.value) return;
      emit('getPartyData', selectedRow.value);
      emit('update:show', false);
    };
    const onSelectChange = (keys: []) => {
      selectedRowKeys.value = keys;
    };
    const rowSelect = (record: CustomerManagementInfo | CompanyInfo) => {
      selectedRow.value = record;
    };
    const init = () => {
      search(true);
    };
    onMounted(() => {
      init();
    });
    return {
      confirm,
      emit,
      columns,
      selectedRowKeys,
      onSelectChange,
      selectedRow,
      rowSelect,
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
<style scoped lang="scss">
.select-partyA-dialog {
    text-shadow: 0;
}
</style>
