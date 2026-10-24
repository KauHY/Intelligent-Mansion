<!--
 * @Description: 退款
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-26 15:42:00
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-13 14:34:23
-->
<template>
  <v-modal
    v-model:visible="currentVisible"
    title="退款"
    :width="720"
    class="operate-dialog"
    @ok="confirm"
  >
    <a-form
      ref="formRef"
      :model="modelRef"
      scroll-to-first-error
      :rules="rulesRef"
      :label-col="{span:5}"
    >
      <a-form-item label="支付方式">
        线下银行转账
      </a-form-item>
      <a-form-item
        v-if="[5,6,7].includes(contractType)"
        label="入住人员"
      >
        {{ defaultData.employeeIdNames?.join(';') }}
      </a-form-item>
      <a-form-item label="实付金额(元)">
        {{ defaultData.receivedAmount }}
      </a-form-item>
      <a-form-item label="可退金额(元)">
        {{ defaultData.receivedAmount }}
      </a-form-item>
      <a-form-item label="退款方式">
        线下银行转账
      </a-form-item>
      <a-form-item
        label="退款金额(元)"
        name="returnAmount"
      >
        <a-input
          v-model:value="modelRef.returnAmount"
          placeholder="请输入退款金额"
          @change="vaildMoney"
        />
      </a-form-item>
      <a-form-item
        label="退款原因"
        name="returnReason"
      >
        <a-input
          v-model:value="modelRef.returnReason"
          :maxlength="30"
          placeholder="请输入退款原因"
        />
      </a-form-item>
      <a-form-item
        name="remark"
        label="备注"
      >
        <a-textarea
          v-model:value="modelRef.remark"
          show-count
          :maxlength="200"
        />
      </a-form-item>
    </a-form>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect, reactive,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
// import InformationStatistical, { FormManageInfo } from '~models/information-statistical';
import { RentFee, RentFeeTransactionDetail, RentFeeReturnMoney } from '~models/collect-fee';
import { useModifyDate } from '../composition/useForm';
// import { deductionRecordColumns } from '../config';

const rentFeeInstance = new RentFee();
export default defineComponent({
  name: 'OperateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<RentFeeTransactionDetail>,
      default: () => ({}),
    },
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5|6|7>,
      default: 3,
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { visible, defaultData, contractType } = toRefs(props);
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

    const modelRef = reactive<RentFeeReturnMoney>({
      remark: '',
      id: defaultData.value.id,
      returnAmount: null,
      returnReason: '',
      payType: 1,
    });
    const rulesRef = reactive({
      returnReason: [{ required: true, message: '请输入退款原因' }],
      returnAmount: [{
        required: true,
        message: '请输入非零数字,并且不能大于可退金额',
        pattern: /^(([1-9]{1}\d*)|(0{1}))(.\d{1,2})?$/,
      }],
    });
    const vaildMoney = (e: any) => {
      if (e.target.value > defaultData.value.receivedAmount) {
        modelRef.returnAmount = null;
      }
    };

    const formRef = shallowRef();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        Modal.confirm({
          content: '退款仅支持一次操作，不支持退多次，请确认是否退款',
          onOk: async () => {
            const api = contractType.value === 6 || contractType.value === 7 ? rentFeeInstance.returnMoney : rentFeeInstance.rentFeeReturnMoney;
            await api(modelRef);
            currentVisible.value = false;
            emit('operate:success');
          },
        });
      }).catch((err:unknown) => {
        console.log(err, '-err');
        message.warning('校验失败');
      });
    };
    return {
      vaildMoney,
      currentVisible,
      confirm,
      options,
      deductionRecordColumns: [],
      modelRef,
      rulesRef,
      formRef,
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
