<template>
  <v-modal
    v-model:visible="currentVisible"
    class="operate-dialog"
    :title="customerConfig.title"
    :width="920"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div
      id="details"
      class="jobs-mangement content-section"
    >
      <div class="title">
        {{ customerData?.companyName }}水电明细表
      </div>
      <div class="title-right">
        （吨/度）
      </div>
      <div class="title-right">
        结算月份：{{ customerData?.settledMonth }}
      </div>
      <div>
        <a-table
          row-key="id"
          bordered
          :columns="columns"
          :data-source="customerData?.consumptionDetail"
          :pagination="false"
        >
          <template #footer>
            <div>
              <div class="empty-div" />
              <div class="flex-line">
                <div class="cell-ellipsis">
                  本月室内用电量
                </div>
                <div class="cell-ellipsis add-text">
                  <span> {{ customerData?.consumption[1].roomAmount }}</span>
                </div>
                <div class="cell-ellipsis">
                  本月应摊用电量
                </div>
                <div class="cell-ellipsis add-text">
                  <span> {{ customerData?.consumption[1].shareAmount }} </span>
                </div>
              </div>
              <div class="flex-line">
                <div class="cell-ellipsis">
                  本月公共用电量
                </div>
                <div class="cell-ellipsis add-text">
                  {{ customerData?.consumption[1].publicAmount }}
                </div>
                <div class="cell-ellipsis add-red">
                  本月实际用电总量
                </div>
                <div class="cell-ellipsis add-text">
                  {{ customerData?.consumption[1].totalAmount }}
                </div>
              </div>
              <div class="flex-line">
                <div class="cell-ellipsis">
                  本月室内用水量
                </div>
                <div class="cell-ellipsis add-text">
                  {{ customerData?.consumption[0].roomAmount }}
                </div>
                <div class="cell-ellipsis">
                  本月应摊用水量
                </div>
                <div class="cell-ellipsis add-text">
                  <span> {{ customerData?.consumption[0].shareAmount }} </span>
                </div>
              </div>
              <div class="flex-line">
                <div class="cell-ellipsis">
                  本月公共用水量
                </div>
                <div class="cell-ellipsis add-text">
                  {{ customerData?.consumption[0].shareAmount }}
                </div>
                <div class="cell-ellipsis add-red">
                  本月实际用水总量
                </div>
                <div class="cell-ellipsis add-text">
                  {{ customerData?.consumption[0].totalAmount }}
                </div>
              </div>
            </div>
          </template>
        </a-table>
      </div>
    </div>
    <template #footer>
      <div>
        <a-button
          type="primary"
          @click="toPrint"
        >
          打印
        </a-button>
        <a-button @click="emit('update:show', false)">
          关闭
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang="ts">
import {
  reactive, ref, defineComponent, toRefs,
} from 'vue';
import { useSearchListPage } from '~composition/common';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import Apartment from '~models/smarter-energy';
import printHtml from '../../print';

const CustomerVoReq = new Apartment();

export default defineComponent({
  components: { },
  props: {
    customerConfig: {
      type: Object,
      default: () => ({
        title: '',
        data: {},
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const columns = [
      {
        title: '类别',
        dataIndex: 'categoryName',
        width: 20,
        customRender: ({ record, text }:{record:any, text:any}) => {
          const obj = {
            children: text,
            props: {} as any,
          };
          if (record.rowSpan === 0) {
            obj.props.rowSpan = record.rowSpanLength;
          }
          if (record.rowSpan !== 0) {
            obj.props.rowSpan = record.rowSpanLength;
          }
          return obj;
        },
      },
      {
        title: '表计名称',
        dataIndex: 'deviceName',
        width: 100,
        customRender: ({ record, text }:{record:any, text:any}) => {
          const obj = {
            children: text,
            props: {} as any,
          };
          return obj;
        },
      },
      {
        title: '上月底数',
        dataIndex: 'lastReading',
        width: 20,
        customRender: ({ record, text }:{record:any, text:any}) => {
          const obj = {
            children: text,
            props: {} as any,
          };
          return obj;
        },
      },
      {
        title: '本月示数',
        width: 20,
        dataIndex: 'thisReading',
        customRender: ({ record, text }:{record:any, text:any}) => {
          const obj = {
            children: text,
            props: {} as any,
          };
          return obj;
        },
      },
      {
        title: '本月用量',
        width: 20,
        dataIndex: 'consumption',
        customRender: ({ record, text }:{record:any, text:any}) => {
          const obj = {
            children: text,
            props: {} as any,
          };
          return obj;
        },
      },

    ];
    const searchConf = reactive<SearchConf<[]>>({});
    const { customerData, type } = toRefs(props.customerConfig);
    console.log(customerData.value.consumptionDetail, 'customerData');

    const tableList = ref<Array<any>>([]);
    // const {
    //   pagination, pageChange, tableList, search, download,
    // } = useSearchListPage(
    //   CustomerVoReq.listApartmentPersonPage,
    //   searchConf,
    //   null,
    //   CustomerVoReq.exportApartment,
    // );
    const toPrint = () => {
      console.log('打印');
      const details = document.querySelector('#details');
      console.log(details, '..details');
      printHtml(details?.innerHTML);
      // emit('onClose');
    };

    const confirm = () => {
      emit('update:show', false);
    };
    const init = async () => {
    };

    init();
    return {
      customerData,
      toPrint,
      emit,
      confirm,
      currentVisible: true,
      columns,
      // pagination,
      // pageChange,
      tableList,
      // search,
      searchConf,

    };
  },
});
</script>
<style lang="scss" scoped>
.title {
    margin-bottom: 10px;
    text-align: center;
}

.title-right {
    margin-top: 5px;
    margin-right: 15px;
    text-align: right;
}

:deep(.ant-table-footer) {
    padding: 16px 0 0 0;
    background: white;
}

.flex-line {
    display: flex;
    justify-content: space-between;

    .cell-ellipsis {
        width: 25%;
        padding: 16px;
        font-size: 14px;
        color: rgba(0, 0, 0, 0.85);
        text-align: center;
        border-right: 1px solid #f0f0f0;
        border-bottom: 1px solid #f0f0f0;
    }

    .blue-text {
        margin-left: 10px;
        color: var(--ant-primary-color);
        cursor: pointer;
    }

    .add-red {
        color: #f65321;
    }

    .add-text {
        font-size: 12px;
        color: #424656;
    }
}

.empty-div {
    height: 16px;
    border-bottom: 1px solid #f0f0f0;
}
</style>
