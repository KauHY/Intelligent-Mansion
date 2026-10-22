<!--
 * @Description:交易详情
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-26 10:12:43
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-27 14:06:49
-->
<template>
  <div class="details content-section">
    <div
      v-if="responsing"
      class="content-scroll"
    >
      <div class="nav">
        <div class="title">
          详情
        </div>
        <a-button
          v-if="details?.canReturn===1"
          type="primary"
          @click="operate('returnMoney')"
        >
          退款
        </a-button>
      </div>
      <div class="form-item-title">
        基础信息
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            所属园区
          </div>
          <div class="content">
            {{ details?.parkOrgName }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            订单编号
          </div>
          <div class="content">
            {{ details?.tradeNo }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            账单编号
          </div>
          <div class="content">
            {{ details?.billNo }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            入驻企业名称
          </div>
          <div class="content">
            {{ details?.companyName }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            房源名称
          </div>
          <div class="content">
            {{ details?.houseName?.join(';') }}
          </div>
        </div>
        <div
          v-if="[5,6,7].includes(contractType)"
          class="item"
        >
          <div class="label">
            入住人员
          </div>
          <div class="content">
            {{ details?.employeeIdNames?.join(';')||'/' }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            支付方式
          </div>
          <div class="content">
            {{ options.RENT_FEE_PAY_TYPE? options.RENT_FEE_PAY_TYPE[details?.payType]?.label:'-' }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            订单类型
          </div>
          <div class="content">
            {{ options.RENT_TRADE_TYPE?options.RENT_TRADE_TYPE[details?.type]?.label:'-' }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            收款日期
          </div>
          <div class="content">
            {{ details?.checkoutDate }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            操作人
          </div>
          <div class="content">
            {{ details?.createByName }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            操作时间
          </div>
          <div class="content">
            {{ details?.createTime }}
          </div>
        </div>
        <!-- <div class="item">
          <div class="label">
            状态
          </div>
          <div class="content">
            {{ options.RENT_BILL_STATUS? options.RENT_BILL_STATUS[details?.status]?.label:'-' }}
          </div>
        </div> -->
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            备注
          </div>
          <div class="content">
            {{ details?.remark }}
          </div>
        </div>
      </div>

      <div class="form-item-title">
        交易明细
      </div>
      <div class="table-section">
        <v-table
          row-key="tradeNo"
          :pagination="false"
          bordered
          :columns="transactionDetailColumns"
          :data-source="details?.transactionDetailList"
        >
          <template #billNo="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text||'-' }}</span>
          </template>
        </v-table>
      </div>

      <template v-if="details?.canReturn===0">
        <div class="form-item-title">
          退款记录
        </div>
        <div class="table-section">
          <v-table
            row-key="extraTradeId"
            :pagination="false"
            bordered
            :columns="returnMoneyRecordsColumns"
            :data-source="details?.returnMoneyRecords"
          >
            <template #extraTradeNo="{text,record}">
              <span
                class="table-text-link"
                @click="operate('details',record)"
              >{{ text||'-' }}</span>
            </template>
          </v-table>
        </div>
      </template>
    </div>
    <return-money
      v-if="visible"
      v-model:visible="visible"
      :default-data="details"
      :contract-type="contractType"
      @operate:success="init"
    />
    <return-money-details
      v-if="returnMoneyDetailsVisible"
      :id="details?.extraTradeId"
      v-model:visible="returnMoneyDetailsVisible"
      :pay-type-opts="options.RENT_FEE_PAY_TYPE"
      :contract-type="contractType"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs,
} from 'vue';
import {
  RentFee, RentFeeBillInfo, RentFeeBillDetail, RentFeeTransactionDetail,
} from '~models/collect-fee';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { returnMoneyRecordsColumns, transactionDetailColumns } from './config';
import returnMoney from './components/return-money.vue';
import returnMoneyDetails from './components/return-money-details.vue';

// const { addCachePage } = useCachePage();

export default defineComponent({
  components: { returnMoney, returnMoneyDetails },
  props: {
    id: {
      type: String,
      default: '',
    },
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5|6|7>,
      default: 3,
    },
  },
  setup(props) {
    console.log(props.contractType, 'contractType.value');
    const rentFeeInstance = new RentFee();
    const { id, contractType } = toRefs(props);

    const options = useHashMapDics<['RENT_TRADE_TYPE', 'RENT_BILL_STATUS', 'RENT_FEE_PAY_TYPE']>(['RENT_TRADE_TYPE', 'RENT_BILL_STATUS', 'RENT_FEE_PAY_TYPE']);

    const visible = ref(false);
    const returnMoneyDetailsVisible = ref(false);
    const operate = async (type:'returnMoney'|'details', record?:RentFeeTransactionDetail['returnMoneyRecords'][0]) => {
      if (type === 'returnMoney') {
        visible.value = true;
        return;
      }
      returnMoneyDetailsVisible.value = true;
    };

    const details = ref<RentFeeTransactionDetail>();
    const responsing = ref(false);
    const init = async () => {
      responsing.value = false;
      const api = contractType.value === 6 || contractType.value === 7 ? rentFeeInstance.findByIdConsumptionPage : rentFeeInstance.rentFeeTransactionDetailFindById;
      details.value = await api(id.value);
      responsing.value = true;
      console.log(details.value, '------- details.value');
    };
    init();
    return {
      details,
      options,
      operate,
      returnMoneyRecordsColumns,
      transactionDetailColumns,
      visible,
      returnMoneyDetailsVisible,
      init,
      responsing,
    };
  },
});
</script>

<style lang="less" scoped>
.details {
    padding: 0 16px;
    font-size: 16px;

    .nav {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 10px 60px 10px 10px;
        margin-bottom: 30px;
        font-size: 20px;
        font-weight: 900;
    }
}

.box-item {
    display: flex;
    margin-bottom: 36px;

    :deep(.ant-form-item) {
        margin-bottom: 0;
    }

    >.item {
        display: flex;
        flex: 1;
        align-items: center;
        justify-content: flex-start;

        >.label {
            width: 160px;
            min-width: 100px;
            max-width: 120px;
            font-weight: 600;
            color: rgba(0, 0, 0, 0.25);

            &::after {
                content: ':';
            }

            &.required {
                &::before {
                    color: red;
                    content: '*';
                }
            }
        }

        .content {
            flex: 1;
            word-break: break-all;
        }
    }
}

.table-section {
    margin-bottom: 30px;
}

.margin {
    margin-top: 60px;
}
</style>
