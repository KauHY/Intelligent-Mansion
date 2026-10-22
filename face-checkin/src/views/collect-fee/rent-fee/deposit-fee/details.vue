<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-27 10:38:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-23 14:28:56
-->
<template>
  <div>
    <a-skeleton
      :paragraph="{ rows: 0 }"
      :loading="responsing"
    >
      <div class="details content-section">
        <div class="content-scroll">
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
                收费项
              </div>
              <div class="content">
                {{ details?.chargeName }}
              </div>
            </div>
          </div>
          <div class="box-item">
            <div class="item">
              <div class="label">
                入驻企业名称
              </div>
              <div class="content">
                {{ details?.companyName }}
              </div>
            </div>
            <div class="item">
              <div class="label">
                合同编号
              </div>
              <div class="content">
                {{ details?.contractNo }}
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
            <div class="item">
              <div class="label">
                计费周期
              </div>
              <div class="content">
                {{ details?.startDate }}-{{ details?.endDate }}
              </div>
            </div>
          </div>

          <div class="box-item">
            <div class="item">
              <div class="label">
                应收日期
              </div>
              <div class="content">
                {{ details?.dueDate }}
              </div>
            </div>
            <div class="item">
              <div class="label">
                数据来源
              </div>
              <div class="content">
                {{ options?.RENT_DATA_SOURCE[details?.dataSource]?.label }}
              </div>
            </div>
          </div>

          <div class="box-item">
            <div class="item">
              <div class="label">
                应收金额（元）
              </div>
              <div class="content">
                {{ details?.receivableAmount }}
              </div>
            </div>
            <div class="item">
              <div class="label">
                已收金额（元）
              </div>
              <div class="content">
                {{ details?.receivedAmount }}
              </div>
            </div>
          </div>
          <div class="box-item">
            <div class="item">
              <div class="label">
                待收金额（元）
              </div>
              <div class="content">
                {{ details?.dueAmount }}
              </div>
            </div>

            <div class="item">
              <div class="label">
                账单状态
              </div>
              <div class="content">
                {{ options?.RENT_CASH_STATUS[details?.cashStatus]?.label }}
              </div>
            </div>
          </div>
          <div class="box-item">
            <div class="item">
              <div class="label">
                退租日期
              </div>
              <div class="content">
                {{ details?.leaseDate }}
              </div>
            </div>

            <div class="item">
              <div class="label">
                办理日期
              </div>
              <div class="content">
                {{ details?.handleTime }}
              </div>
            </div>
          </div>

          <div class="box-item">
            <div class="item">
              <div class="label">
                是否抵扣
              </div>
              <div class="content">
                {{ details?.deduction?'是':'否' }}
              </div>
            </div>
            <div class="item">
              <div class="label">
                减免金额（元）
              </div>
              <div class="content">
                {{ details?.deductionAmount }}
              </div>
            </div>
          </div>

          <div class="record">
            <div class="form-item-title ">
              减免记录
            </div>
            <v-table
              row-key="id"
              :pagination="false"
              bordered
              :columns="deductionRecordColumns"
              :data-source="details?.deductionRecordList"
            >
              <template
                #fileList="{text}"
              >
                <span
                  v-for="(item,index) in text"
                  :key="index"
                  class="table-text-link"
                  @click="open(item.key)"
                >{{ `${item.name}；` }}</span>
              </template>
            </v-table>
          </div>

          <div class="record">
            <div class="form-item-title ">
              抵扣记录
            </div>
            <v-table
              row-key="id"
              :pagination="false"
              bordered
              :columns="deductionContentColumns"
              :data-source="details?.deductionMsg"
            />
            <div class="box-item sum">
              <div class="item">
                <div class="label">
                  已抵扣总金额（元）
                </div>
                <div class="content">
                  {{ details?.offsetAmount }}
                </div>
              </div>
              <div class="item">
                <div class="label">
                  已退款金额（元）
                </div>
                <div class="content">
                  {{ details?.refundedAmount }}
                </div>
              </div>
            </div>
          </div>

          <div class="record">
            <div class="form-item-title record">
              退还记录
            </div>
            <v-table
              row-key="id"
              :pagination="false"
              bordered
              :columns="depositFeeRentrnRecordColumns"
              :data-source="details?.depositFeeRentrnRecord"
            />
          </div>
        </div>
      </div>
    </a-skeleton>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs,
} from 'vue';
import {
  RentFee, RentFeeBillInfo, RentFeeBillDetail, RentFeeInfoDetails,
} from '~models/collect-fee';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { getPreviewUrl } from '~models/common';
import { deductionContentColumns, depositFeeRentrnRecordColumns, deductionRecordColumns } from './config';
// import returnMoney from './components/return-money.vue';
// import returnMoneyDetails from './components/return-money-details.vue'

export default defineComponent({
  // components: { returnMoney, returnMoneyDetails },
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const rentFeeInstance = new RentFee();
    const { id } = toRefs(props);
    const responsing = ref(true);

    const options = useHashMapDics<['RENT_TRADE_TYPE', 'RENT_CASH_STATUS', 'RENT_FEE_PAY_TYPE', 'RENT_DATA_SOURCE']>(['RENT_TRADE_TYPE', 'RENT_CASH_STATUS', 'RENT_FEE_PAY_TYPE', 'RENT_DATA_SOURCE']);

    const visible = ref(false);
    const returnMoneyDetailsVisible = ref(false);

    // const operate = async (type:'returnMoney'|'details', record?:RentFeeInfoDetails['returnMoneyRecords'][0]) => {
    //   if (type === 'returnMoney') {
    //     visible.value = true;
    //     return;
    //   }
    //   returnMoneyDetailsVisible.value = true;
    // };
    const details = ref<RentFeeInfoDetails>();
    const init = async () => {
      details.value = await rentFeeInstance.rentFeeFindById(id.value);
      responsing.value = false;
      console.log(details.value, '------- details.value');
    };
    init();
    return {
      details,
      options,
      // operate,
      depositFeeRentrnRecordColumns,
      deductionContentColumns,
      deductionRecordColumns,
      visible,
      returnMoneyDetailsVisible,
      open: (key:string) => window.open(getPreviewUrl(key)),
      init,
      responsing,
    };
  },
});
</script>

<style lang="less" scoped>
.details {
    padding: 10px 20px;
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

    &.sum {
        margin-top: 20px;
    }
}

.table-section {
    margin-bottom: 30px;
}

.record {
    margin: 60px 0 30px;
}
</style>
