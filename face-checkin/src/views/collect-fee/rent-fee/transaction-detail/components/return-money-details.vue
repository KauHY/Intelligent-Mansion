<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-12 17:29:20
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    title="退款订单详情"
    :width="920"
    class="operate-dialog"
    @ok="confirm"
  >
    <div class="form-item-title">
      详情
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
          原订单编号
        </div>
        <div class="content">
          {{ details?.extraTradeNo }}
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
          退款方式
        </div>
        <div class="content">
          {{ payTypeOpts[details?.payType]?.label }}
        </div>
      </div>
    </div>
    <div class="box-item">
      <div class="item">
        <div class="label">
          订单类型
        </div>
        <div class="content">
          {{ options.RENT_TRADE_TYPE?options.RENT_TRADE_TYPE[details?.type]?.label:'-' }}
        </div>
      </div>
      <div class="item">
        <div class="label">
          退款日期
        </div>
        <div class="content">
          {{ details?.returnTime }}
        </div>
      </div>
    </div>
    <div class="box-item">
      <div class="item">
        <div class="label">
          操作人
        </div>
        <div class="content">
          {{ details?.createByName }}
        </div>
      </div>
      <div class="item">
        <div class="label">
          操作时间
        </div>
        <div class="content">
          {{ details?.createTime }}
        </div>
      </div>
    </div>
    <div class="box-item">
      <div class="item">
        <div class="label">
          退款原因
        </div>
        <div class="content">
          {{ details?.returnReason }}
        </div>
      </div>
      <!-- <div class="item">
        <div class="label">
          退款状态
        </div>
        <div class="content">
          退款成功
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
      <div
        v-if="contractType===5"
        class="item"
      >
        <div class="label">
          入驻人员
        </div>
        <div class="content">
          {{ details?.employeeIdNames?.join(';') }}
        </div>
      </div>
    </div>

    <div class="form-item-title record">
      退款内容
    </div>

    <!-- {{ details?.returnMoneyContent }} -->
    <v-table
      row-key="extraTradeId"
      :columns="returnMoneyContentsColumns"
      :data-source="details.returnMoneyContent"
    />

    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          关闭
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect, reactive,
} from 'vue';
import { message } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
// import InformationStatistical, { FormManageInfo } from '~models/information-statistical';
import { RentFee, RentFeeTransactionDetail } from '~models/collect-fee';
import { useModifyDate } from '../composition/useForm';
import { returnMoneyContentsColumns } from '../config';

const rentFeeInstance = new RentFee();
export default defineComponent({
  name: 'OperateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    id: {
      type: String as PropType<string>,
      default: '',
    },
    parkOpts: {
      type: Object as PropType<SelectOptions>,
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
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { id, visible, contractType } = toRefs(props);

    const options = useHashMapDics<['RENT_TRADE_TYPE']>(['RENT_TRADE_TYPE']);
    // const isReady = ref(false);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    const details = reactive<Partial<RentFeeTransactionDetail>>({
      returnMoneyContent: [],
    });
    const init = async () => {
      const api = contractType.value === 6 || contractType.value === 7 ? rentFeeInstance.findByIdConsumptionPage : rentFeeInstance.rentFeeTransactionDetailFindById;
      const data = await api(id.value);
      Object.assign(details, data);
      console.log(details, '---------------details.value');
    };
    init();
    const confirm = () => {
      currentVisible.value = false;
      //   emit('operate:success');
      // validate().then(async () => {
      //   // await contractManageInstance.templateAdd(modelRef);
      //   message.success('操作成功');
      //   currentVisible.value = false;
      //   emit('operate:success');
      // }).catch((error:any) => {
      //   message.warning('操作失败');
      // });
    };
    return {
      currentVisible,
      confirm,
      options,
      details,
      returnMoneyContentsColumns,
    };
  },
});
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
    margin-top: 30px;
}
</style>
