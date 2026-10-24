<!--
 * @Description: 详情/编辑/收款页面
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-24 14:01:05
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-26 19:59:05
-->
<template>
  <div>
    <a-skeleton
      :paragraph="{ rows: 4 }"
      :loading="responsing"
    >
      <template v-if="type==='receive'">
        <div class="box-item">
          <div class="item">
            <div class="label">
              收款方式
            </div>
            <div class="content">
              线下银行转账
            </div>
          </div>
        </div>
        <div class="box-item">
          <div class="item">
            <div class="label required">
              收款日期
            </div>
            <div class="content">
              <a-form-item name="checkoutDate">
                <a-date-picker
                  v-model:value="modelRef.checkoutDate"
                  :disabled-date="disabledCheckoutDate"
                  value-format="YYYY-MM-DD"
                  placeholder="请选择收款日期"
                />
              </a-form-item>
            </div>
          </div>
        </div>
        <div
          v-if="contractType===7 || contractType===6"
          class="box-item"
        >
          <div class="item">
            <div class="label">
              房源名称
            </div>
            <div class="content">
              {{ details?.houseName?.join(';') }}
            </div>
          </div>
        </div>
        <div
          v-if="contractType===5"
          class="box-item"
        >
          <div class="item">
            <div class="label">
              入驻人员
            </div>
            <div class="content">
              {{ details?.employeeIdNames?.join(';') }}
            </div>
          </div>
        </div>
      </template>
      <template v-else>
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
              账单编号
            </div>
            <div class="content">
              {{ details?.billNo }}
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
          <div
            v-if="[3,4,5].includes(contractType)"
            class="item"
          >
            <div class="label">
              合同编号
            </div>
            <div class="content">
              {{ details?.contractNo }}
            </div>
          </div>
        <!-- <div class="item">
          <div class="label">
            房源名称
          </div>
          <div class="content">
            {{ details?.houseName?.join(';') }}
          </div>
        </div> -->
        </div>

        <div class="box-item">
          <div class="item">
            <div class="label required">
              所在账期
            </div>
            <div class="content">
              <a-form-item
                v-if="type==='edit'"
                name="accountPeriod"
              >
                <a-date-picker
                  v-model:value="modelRef.accountPeriod"
                  picker="month"
                  value-format="YYYY-MM"
                  placeholder="请选择所在账期"
                />
              </a-form-item>
              <span v-else>{{ defaultData.accountPeriod||'-' }}</span>
            </div>
          </div>
          <div class="item">
            <div class="label">
              应收日期
            </div>
            <div class="content">
              {{ details?.dueDate }}
            </div>
          </div>
        </div>

        <div class="box-item">
          <div class="item">
            <div class="label">
              最晚缴纳日期
            </div>
            <div class="content">
              {{ details?.latestPayDate }}
            </div>
          </div>
          <div class="item">
            <div class="label">
              数据来源
            </div>
            <div class="content">
              {{ sourceOpts[details?.dataSource]?.label }}
            </div>
          </div>
        </div>

        <div class="box-item">
          <div class="item">
            <div class="label">
              账单状态
            </div>
            <div class="content">
              {{ billStatusOpts[details?.status]?.label }}
            </div>
          </div>
          <div
            class="item"
          >
            <div class="label">
              凭证
            </div>
            <div class="content">
              <v-upload
                v-if="details?.companyFiles"
                :value="details?.companyFiles"
                accept=".png,.jpg,.jpeg,.gif,.doc,.pdf,.docx,.xls,.xlsx,.ppt,.pptx"
                list-type="text"
                disabled
              />
              <span v-else>/</span>
            </div>
          </div>
        </div>

        <div class="box-item">
          <div
            v-if="contractType===5"
            class="item"
          >
            <div class="label">
              入住人员
            </div>
            <div class="content">
              {{ details?.employeeIdNames?.join(';') }}
            </div>
          </div>
        </div>
      </template>

      <div class="form-item-title margin">
        费用明细
      </div>
      <div class="table-section">
        <v-table
          v-if="contractType===6 || contractType===7"
          row-key="id"
          bordered
          :pagination="false"
          :columns="waterBillDetailColumns"
          :data-source="details?.chargeDetail"
        />
        <v-table
          v-else
          row-key="id"
          bordered
          :pagination="false"
          :columns="feeDetailsColumns"
          :data-source="details?.feeDetailList"
        />
      </div>
      <template v-if="type==='details'">
        <div class="form-item-title">
          减免记录
        </div>
        <div class="table-section">
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
      </template>
      <!-- <template> -->
      <div class="form-item-title">
        滞纳金明细
      </div>
      <div class="table-section">
        <v-table
          row-key="id"
          :pagination="false"
          bordered
          :columns="lateFeeDetailColumns"
          :data-source="details?.lateFeeDetailList"
        />
      </div>
      <!-- </template> -->
      <template v-if="type==='details'">
        <div class="form-item-title">
          收款记录
        </div>
        <div class="table-section">
          <v-table
            row-key="id"
            :pagination="false"
            bordered
            :columns="checkoutRecordColumns"
            :data-source="details?.checkoutRecordList"
          >
            <template #payType="{text}">
              <span>{{ payTypeOpts[text]?.label }}</span>
            </template>
          </v-table>
        </div>
      </template>

      <template v-if="type==='receive'">
        <div
          class="box-item"
        >
          <div class="item">
            <div class="label">
              实收总金额(元)
            </div>
            <div class="content">
              {{ sumAmount || 0 }}
            </div>
          </div>
        </div>
        <div
          class="box-item"
        >
          <div class="item">
            <div class="label">
              备注
            </div>
            <div class="content">
              <a-form-item name="remark">
                <a-textarea
                  v-model:value="modelRef.remark"
                  show-count
                  :maxlength="200"
                />
              </a-form-item>
            </div>
          </div>
        </div>
      </template>
    </a-skeleton>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs,
} from 'vue';
import { Dayjs } from 'dayjs';
import { number } from 'mathjs';
import { RentFee, RentFeeBillInfo, RentFeeBillDetail } from '~models/collect-fee';
import { getPreviewUrl } from '~models/common';
import {
  feeDetailsColumns, deductionRecordColumns, lateFeeDetailColumns, checkoutRecordColumns, waterBillDetailColumns,
} from '../config';
import { useOperateBill } from '../composition/useForm';

export default defineComponent({
  props: {
    defaultData: {
      type: Object as PropType<RentFeeBillInfo>,
      default: () => ({}),
    },
    type: {
      type: String as PropType<'batchReceive' |'details'|'edit'|'deduction'|'receive'>,
      default: '',
    },
    billStatusOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    sourceOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    payTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5|6|7>,
      default: 3,
    },
  },
  setup(props) {
    const rentFeeInstance = new RentFee();
    const { defaultData, type, contractType } = toRefs(props);
    const { modelRef } = useOperateBill();
    const details = ref<RentFeeBillDetail>();
    const sumAmount = ref<number>(0);
    const responsing = ref(true);
    const init = async () => {
      const api = contractType.value === 6 || contractType.value === 7 ? rentFeeInstance.findByIdConsumptionBill : rentFeeInstance.rentFeeBillDetails;
      details.value = await api(defaultData.value.id);
      modelRef.accountPeriod = details.value.accountPeriod;
      if (type.value === 'receive') { // 收款时调用
        if (contractType.value === 6 || contractType.value === 7) {
          sumAmount.value = Number(details.value.dueAmount) + Number(details.value.lateFeeDetailList.length === 0 ? 0 : details.value.lateFeeDetailList[0]?.dueAmount);
        } else {
          sumAmount.value = (await rentFeeInstance.beforeCheckoutRentFee(defaultData.value.id))?.totalAmount;
        }
      }
      responsing.value = false;
    };
    init();
    return {
      modelRef,
      details,
      feeDetailsColumns,
      deductionRecordColumns,
      waterBillDetailColumns,
      lateFeeDetailColumns,
      checkoutRecordColumns,
      disabledCheckoutDate: (e:Dayjs) => e.valueOf() > Date.now(),
      open: (key:string) => window.open(getPreviewUrl(key)),
      sumAmount,
      responsing,
    };
  },
});
</script>
<style lang="less" scoped>
.box-item {
    display: flex;
    margin-bottom: 26px;

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
