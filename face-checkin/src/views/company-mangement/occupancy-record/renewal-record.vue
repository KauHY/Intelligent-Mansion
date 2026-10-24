<!--
 * @Description: 解绑房屋
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-26 16:03:17
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-10-21 16:42:14
-->
<template>
  <div class="renewal-record content-section">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="goBlack"
        >
          返回
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="renewalRecorRColumns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        />
      </template>
    </search-box>
  </div>
</template>

<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, toRefs, onUnmounted,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import router from '@/routers';
import { useRouter } from 'vue-router';

import CompanyMangement, { PageRenewalRecordInfo } from '~models/company-mangement';
import Park, { ParkInfo } from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';

import House from '~models/house';
import operateDialog from './components/operate-dialog.vue';
// import Building from '~models/building';
import {
  renewalRecorRColumns,
} from './config';

export default defineComponent({
  name: 'CompanyMangementPompanyList',
  components: { searchBox },
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    // const a:string = 1;
    const { id } = toRefs(props);
    const router = useRouter();
    const companyInstance = new CompanyMangement();
    const parkInstance = new Park();

    const searchConf = reactive<SearchConf<['rentRecordId']>>({
      rentRecordId: {
        tag: 'input',
        value: '',
        hidden: true,
      },
    });
    searchConf.rentRecordId.value = id.value;
    const goBlack = () => {
      router.push({ name: 'companyMangementOccupancyRecord' });
    };
    const {
      pagination,
      pageChange,
      tableList,
      search,
    //   operateDialogConf,
    } = useSearchListPage<PageRenewalRecordInfo>(companyInstance.pageRenewalRecord, searchConf, null);
    const { remoteCachePage } = useCachePage();

    const init = async () => {
      search(true);
    };
    init();
    onUnmounted(() => {
      remoteCachePage('OccupancyRecord');
    });
    return {
      renewalRecorRColumns,
      pagination,
      pageChange,
      tableList,
      searchConf,
      goBlack,
    //   operate,
    //   parkOpts,
    };
  },
});
</script>
