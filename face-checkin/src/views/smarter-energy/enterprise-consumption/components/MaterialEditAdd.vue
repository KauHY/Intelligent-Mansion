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
        结算月份{{ customerData?.settledMonth }}
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
                <div class="cell-ellipsis add-text spec-style">
                  <span v-if="flag"> {{ customerData?.consumption[1].shareAmount }} </span>
                  <a-input
                    v-if="!flag"
                    v-model:value="customerData.consumption[1].shareAmount"
                    @change="changeConsumption($event,1)"
                  />
                  <v-svg
                    v-if="flag"
                    class="blue-text"
                    name="editor"
                    @click="clickEdit(false,1)"
                  />
                  <span
                    v-if="!flag"
                    class="blue-text"
                    @click="saveData(true,1)"
                  > 保存 </span>
                  <span
                    v-if="!flag"
                    class="blue-text"
                    @click="clickEdit(true,1)"
                  > 取消 </span>
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
                <div class="cell-ellipsis add-text spec-style">
                  <span v-if="flagWater"> {{ customerData?.consumption[0].shareAmount }} </span>
                  <a-input
                    v-if="!flagWater"
                    v-model:value="customerData.consumption[0].shareAmount"
                    @change="changeConsumption($event,2)"
                  />
                  <v-svg
                    v-if="flagWater"
                    class="blue-text"
                    name="editor"
                    @click="clickEdit(false,2)"
                  />
                  <span
                    v-if="!flagWater"
                    class="blue-text"
                    @click="clickEdit(true,2)"
                  > 保存 </span>
                  <span
                    v-if="!flagWater"
                    class="blue-text"
                    @click="clickEdit(true,2)"
                  > 取消 </span>
                </div>
              </div>
              <div class="flex-line">
                <div class="cell-ellipsis">
                  本月公共用水量
                </div>
                <div class="cell-ellipsis add-text">
                  {{ customerData?.consumption[0].publicAmount }}
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
import { message } from 'ant-design-vue';
import { useSearchListPage } from '~composition/common';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import Apartment from '~models/smarter-energy';
import {
  getColumns,
} from '../config';
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
    const flag = ref<Boolean>(true);
    const flagWater = ref<Boolean>(true);
    const searchConf = reactive<SearchConf<[]>>({});
    const { customerData, type } = toRefs(props.customerConfig);
    console.log(customerData.value.consumptionDetail, 'customerData');
    // const {
    //   pagination, pageChange, tableList, search, download,
    // } = useSearchListPage(
    //   CustomerVoReq.findByIdlistMeterConsumption(record.id),
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
    const clickEdit = async (value:boolean, type:number) => {
      if (type === 1) flag.value = value;
      if (type === 2) flagWater.value = value;
    };
    const saveData = async (value:boolean, type:number) => {
      if (type === 1) {
        flag.value = value;
        const data = await CustomerVoReq.modifyShareConsumption({
          deviceType: 6,
          id: customerData.value.id,
          shareAmount: customerData.value.consumption[1].shareAmount,
        });
      } else {
        flagWater.value = value;
        const data = await CustomerVoReq.modifyShareConsumption({
          deviceType: 5,
          id: customerData.value.id,
          shareAmount: customerData.value.consumption[0].shareAmount,
        });
      }
    };
    const changeConsumption = async (e: any, type: any) => {
      if (type === 1) {
        if (e.target.value <= 0 || e.target.value > customerData?.value.consumption[1].publicAmount) {
          customerData.value.consumption[1].shareAmount = null;
          message.warn('应摊用量需大于等于0、小于等于公共用量');
        }
      } else if (e.target.value <= 0 || e.target.value > customerData?.value.consumption[0].publicAmount) {
        customerData.value.consumption[0].shareAmount = null;
        message.warn('应摊用量需大于等于0、小于等于公共用量');
      }
    };

    const operateSuccess = () => {
      // search(true);
    };
    const confirm = () => {
      emit('update:show', false);
    };
    const init = async () => {
    };

    init();
    return {
      saveData,
      clickEdit,
      flag,
      flagWater,
      toPrint,
      emit,
      confirm,
      currentVisible: true,
      columns,
      changeConsumption,
      // pagination,
      // pageChange,
      customerData,
      // search,
      operateSuccess,
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

    .spec-style {
        display: flex;
        align-items: center;
        justify-content: center;

        .ant-input {
            width: 88px;
        }
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
