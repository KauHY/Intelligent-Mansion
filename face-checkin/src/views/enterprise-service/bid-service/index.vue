<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-03-31 14:35:19
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box :search-conf="{}">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="exportEvent"
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
          <template #operate="{ record }">
            <div>
              <a-button
                v-show="record.marginPaid === 0"
                type="link"
                @click="operate('confirm', record)"
              >
                确认
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :park-opts="options"
      :park-pop="parkPop"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { useRoute } from 'vue-router';
import BidService, { LaneInfo } from '~models/bid-service';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { useDownload } from '~composition/useDownload';
import { operateDialogTitleName, getColumns } from './config';
import operateDialog from './components/operate-dialog.vue';

type Operatetypes = 'confirm'
const serviceRequest = new BidService();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup(props) {
    const route = useRoute();
    const parkPop = ref<SelectOptions>([]);
    const options = ref<DictionaryMap<['PAYMENT', 'CONTRIBUTE']>>({
      PAYMENT: [],
      CONTRIBUTE: [],
    });
    const columns = getColumns(options);
    const searchConf = reactive<SearchConf<['id', 'purchaseId']>>({
      id: {
        tag: 'input',
        value: route.params.id,
        hidden: true,
      },
      purchaseId: {
        tag: 'input',
        value: route.params.id,
        hidden: true,
      },

    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage(serviceRequest.listTenderRecordPage, searchConf, null, serviceRequest.bidExport);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type: Operatetypes, record:LaneInfo) => {
      Modal.confirm({
        content: '是否确认？',
        onOk: async () => {
          await serviceRequest.bidConfirm(record.id);
          message.success('操作成功');
          search(true);
        },
      });
    };
    search(true);

    const exportEvent = async () => {
      await download();
    };
    onMounted(async () => {
      options.value = await useHashMapDic<['PAYMENT', 'CONTRIBUTE']>(['PAYMENT', 'CONTRIBUTE']);
      parkPop.value = await new Park().searchParkListName();
    });
    return {
      columns,
      // searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkPop,
      options,
      exportEvent,
    };
  },
});
</script>
