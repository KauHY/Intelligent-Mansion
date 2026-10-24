<template>
  <div class="menu-list content-section">
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
          row-key="userId"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template>
          <template #warningCode="{ record }">
            <span>{{ options.WARNING_EVENT_CODE[record.warningCode]?.label }}</span>
          </template>
          <template #deviceType="{ record }">
            <span>{{ record.deviceType===6? '电能耗':'水能耗' }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent,
} from 'vue';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { useParkInfoOpts } from '~composition/park';
import SmarterMeter, { warningRecordPageParams } from '~models/smarter-energy';
import {
  statusName, columns,
} from './config';

const userInstance = new SmarterMeter();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox },
  setup() {
    const options = ref<DictionaryMap<['WARNING_EVENT_CODE']>>({
      WARNING_EVENT_CODE: [],
    });
    const {
      parkOpts,
      getParkOpts,
    } = useParkInfoOpts();
    const searchConf = reactive<SearchConf<['parkOrgId', 'warningCode', 'deviceName', 'month']>>({
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择所属园区',
        options: parkOpts,
      },
      deviceName: {
        tag: 'input',
        value: '',
        placeholder: '请输入表计名称',
      },
      warningCode: {
        tag: 'select',
        value: '',
        placeholder: '请选择告警事件',
        options: () => options.value?.WARNING_EVENT_CODE,
      },
      month: {
        tag: 'date-range',
        value: '',
        placeholder: ['开始时间', '结束时间'],
        endTimeKey: 'warningTimeEnd',
        startTimeKey: 'warningTimeStart',
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<warningRecordPageParams>(userInstance.listMeterWarningRecordPage, searchConf);

    const init = async () => {
      options.value = await useHashMapDic<['WARNING_EVENT_CODE']>(['WARNING_EVENT_CODE']);
      getParkOpts();
      search(true);
    };
    init();
    onMounted(() => {
    });
    return {
      columns,
      options,
      statusName,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    };
  },
});
</script>
