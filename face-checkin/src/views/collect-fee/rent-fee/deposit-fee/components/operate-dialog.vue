<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-12 15:11:34
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="920"
    class="operate-dialog"
    @ok="confirm"
  >
    <div class="form-item-title">
      基础信息
    </div>
    <div class="box-item">
      <div class="item">
        <div class="label">
          所属园区
        </div>
        <div class="content">
          {{ defaultData.parkOrgName }}
        </div>
      </div>
      <div class="item">
        <div class="label">
          入驻企业名称
        </div>
        <div class="content">
          {{ defaultData.companyName }}
        </div>
      </div>
    </div>
    <div class="box-item">
      <div class="item">
        <div class="label">
          合同编号
        </div>
        <div class="content">
          {{ defaultData.contractNo }}
        </div>
      </div>
      <div class="item">
        <div class="label">
          房源名称
        </div>
        <div class="content">
          {{ defaultData.houseName?.join(';') }}
        </div>
      </div>
    </div>
    <div class="box-item ">
      <div class="item">
        <div class="label">
          可退金额（元）
        </div>
        <div class="content">
          {{ defaultData.receivableAmount-defaultData.deductionAmount }}
        </div>
      </div>
    </div>
    <div class="box-item">
      <div class="item">
        <div class="label">
          是否抵扣
        </div>
        <div class="content">
          <a-radio-group v-model:value="modelRef.deduction">
            <a-radio
              :value="1"
            >
              是
            </a-radio>
            <a-radio
              :value="0"
            >
              否
            </a-radio>
          </a-radio-group>
        </div>
      </div>
    </div>

    <template v-if="modelRef.deduction===1">
      <div class="record">
        <div class="form-item-title ">
          抵扣记录
        </div>
        <a-button
          type="link"
          @click="showDeductionContent=true"
        >
          选择抵扣内容
        </a-button>
      </div>
      <v-table
        row-key="id"
        :pagination="false"
        :columns="deductionContentColumns"
        :data-source="Object.values(selectData)"
      >
        <template #remark="{record}">
          <span v-if="record.startDate.year">{{ record.chargeName }}{{ record.startDate.year }}-{{ record.startDate.monthValue }}-{{ record.startDate.dayOfMonth }}-{{ record.endDate.year }}-{{ record.endDate.monthValue }}-{{ record.endDate.dayOfMonth }}</span>
          <span v-else>{{ record.chargeName }}{{ record.startDate }}-{{ record.endDate }}</span>
        </template>
      </v-table>
      <div class="box-item sum">
        <div class="item">
          <div
            class="label"
            style="max-width: none;"
          >
            待抵扣总金额（元）
          </div>
          <div class="content">
            {{ offsetAmount }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            待退款金额（元）
          </div>
          <div class="content">
            {{ refundedAmount }}
          </div>
        </div>
      </div>
    </template>
    <div
      class="box-item remark"
    >
      <div class="item">
        <div class="label">
          备注
        </div>
        <div class="content">
          <a-textarea
            v-model:value="modelRef.remark"
            show-count
            :maxlength="200"
            placeholder="请输入备注"
          />
        </div>
      </div>
    </div>
  </v-modal>
  <deduction-content-list
    v-if="showDeductionContent"
    v-model:visible="showDeductionContent"
    v-model:selectData="selectData"
    :contract-type="contractType"
    :default-data="defaultData"
  />
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect, reactive, Ref,
} from 'vue';

import { message } from 'ant-design-vue';
import { add } from 'mathjs';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
// import InformationStatistical, { FormManageInfo } from '~models/information-statistical';
import {
  RentFee, RentFeeInfo, ReturnRentDepositFee, RentDepositFeeDeductionContentSelectData,
} from '~models/collect-fee';
import { useModifyDate } from '../composition/useForm';
import { deductionContentColumns } from '../config';
import deductionContentList from './deduction-content-list.vue';

const rentFeeInstance = new RentFee();
export default defineComponent({
  name: 'OperateDialog',
  components: { deductionContentList },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<RentFeeInfo>,
      default: () => ({}),
    },
    title: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: '',
    },
    parkOpts: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
    feeTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5>,
      default: 3,
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { type, visible, defaultData } = toRefs(props);
    const options = useHashMapDics<['Week_Day', 'FORM_CYCLE']>(['Week_Day', 'FORM_CYCLE']);
    // const isReady = ref(false);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    const {
      modelRef, selectData, offsetAmount, refundedAmount,
    } = useReturnMoneyData(defaultData);
    const showDeductionContent = ref(false); // 是否展示抵扣内容

    const confirm = async () => {
      if (modelRef.deduction === 1 && !Object.values(selectData.value).length) {
        message.warning('抵扣内容不能为空');
        return;
      }
      await rentFeeInstance.rentDepositFeeReturnMoney({
        ...modelRef,
        deductionMsg: Object.values(selectData.value),
        offsetAmount: offsetAmount.value,
        refundedAmount: refundedAmount.value,
      });
      currentVisible.value = false;
      emit('operate:success');
    };
    return {
      currentVisible,
      confirm,
      options,
      showDeductionContent,
      deductionContentColumns,
      modelRef,
      selectData,
      offsetAmount,
      refundedAmount,
    };
  },
});

function useReturnMoneyData(defaultData:Ref<RentFeeInfo>) {
  const selectData = ref<RentDepositFeeDeductionContentSelectData>({});
  const modelRef = reactive<ReturnRentDepositFee>({
    deduction: 1, // 是否抵扣
    deductionMsg: [],
    id: defaultData.value.id,
    offsetAmount: 0, // 待抵扣总金额
    refundedAmount: 0, // 待退还金额
    remark: '', //
  });
  const offsetAmount = computed(() => Object.values(selectData.value).reduce((sum:any, cur) => {
    sum = add(sum, cur.deAmount);
    return sum;
  }, 0));
  const refundedAmount = computed(() => add(defaultData.value.receivedAmount, -offsetAmount.value)as number);
  return {
    modelRef,
    selectData,
    offsetAmount,
    refundedAmount,
  };
}
</script>

<style lang="less" scoped>
.box-item {
    display: flex;
    padding: 0 20px;
    margin-bottom: 16px;

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
        }

        .content {
            flex: 1;
            word-break: break-all;
        }
    }
}

.record {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 30px;
}

.sum {
    margin-top: 30px;
}

.remark {
    margin-top: 40px;
}
</style>
