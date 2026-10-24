<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: xucaizhi3
 * @LastEditTime: 2022-03-16 15:56:52
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
          <template #reserveNo="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >{{ record.reserveNo }}</span>
          </template>
          <template #operate="{ record }">
            <div>
              <a-button
                v-if="record.reviewStatus===1"
                type="link"
                @click="manipulate('verify', record)"
              >
                审核
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <OrderDetail
      v-if="orderFlag"
      ref="order"
      v-model:show="orderFlag"
      :order-config="orderConfig"
      @refresh="operateSuccess"
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
import MeetingRentalVo, { MeetingRentalListKind } from '~models/meeting-rental';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { getOrderColumns } from './config';
import OrderDetail from './components/OrderDetail.vue';

interface OrderConfig {
  title: string
  type: string
  orderData?: object
}

type Operatetypes = 'detail' | 'verify' | 'export'
const MeetingRentalVoReq = new MeetingRentalVo();
export default defineComponent({
  components: { searchBox, OrderDetail },
  setup() {
    const orderFlag = ref<Boolean>(false);
    const orderConfig = ref<OrderConfig>();
    const options = useHashMapDics<['CUS_MEETING_AUDIT_STATUS', ]>(['CUS_MEETING_AUDIT_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['meetingName', 'parkOrgId', 'mobile', 'rentalTime', 'reviewStatus']>>({
      meetingName: {
        tag: 'input',
        placeholder: '请输入会议室名称',
        value: '',
      },
      mobile: {
        tag: 'input',
        placeholder: '请输入联系人手机号',
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
      rentalTime: {
        tag: 'date-range',
        value: '',
        placeholder: ['申请开始时间', '申请结束时间'],
        endTimeKey: 'applicantTimeEnd',
        startTimeKey: 'applicantTimeStart',
      },
      reviewStatus: {
        tag: 'select',
        placeholder: '请选择状态',
        value: '',
        options: () => options.CUS_MEETING_AUDIT_STATUS,
      },
    });
    const columns = getOrderColumns(options);
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      MeetingRentalVoReq.rentalVerifyListPage,
      searchConf,
      null,
      MeetingRentalVoReq.orderExportApi,
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: MeetingRentalListKind) => {
      let data = null;
      switch (type) {
        case 'verify':
        case 'detail':
          if (!record) return;
          data = await MeetingRentalVoReq.rentalVerifyFindByIdApi(record.id);
          orderConfig.value = {
            title: type === 'verify' ? '会议室预定审核' : '会议室预定详情',
            type,
            orderData: data,
          };
          orderFlag.value = true;
          console.log(data, 10058);
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
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      download,
      operateSuccess,
      manipulate,
      orderFlag,
      orderConfig,
    };
  },
});
</script>
