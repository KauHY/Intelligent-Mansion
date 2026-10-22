<template>
  <v-modal
    class="customer-add-edit"
    :visible="true"
    :width="600"
    :title="customerConfig.title"
    @cancel="emit('update:show', false)"
  >
    <div>
      <a-form
        ref="formRef"
        :model="modelRef"
        :rules="rulesRef"
        :label-col="{ span: 5 }"
      >
        <a-form-item
          label="收款方式"
        >
          <span>线下银行转账</span>
        </a-form-item>
        <a-form-item
          label="实收总金额（元）"
        >
          <span>{{ customerData.totalAmount }}</span>
        </a-form-item>
        <a-form-item
          label="收款日期"
          name="checkoutDate"
        >
          <a-date-picker
            v-model:value="modelRef.checkoutDate"
            :maxlength="30"
            :disabled-date="disabledDate"
            placeholder="请选择收款日期"
            value-format="YYYY-MM-DD"
          />
        </a-form-item>
        <a-form-item
          label="备注"
          name="remark"
        >
          <a-textarea
            v-model:value="modelRef.remark"
            :maxlength="200"
            placeholder="请输入备注"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <a-button
        @click="confirm"
      >
        确认
      </a-button>
      <a-button
        type="primary"
        @click="emit('update:show', false)"
      >
        取消
      </a-button>
    </template>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, defineComponent, toRefs,
} from 'vue';
import { useRouter } from 'vue-router';
import * as dayjs from 'dayjs';
import CustomerVo from '~models/collect-fee';
import { getEntryForm } from '../composition/useForm';

export default defineComponent({
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
    const router = useRouter();
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<SelectOptions>([]);
    const floorOptions = ref<SelectOptions>([]);
    const { modelRef, rulesRef } = getEntryForm();
    const { customerData, type } = toRefs(props.customerConfig);
    const CustomerVoReq = new CustomerVo();

    const formRef = ref();
    const disabledDate = (date:any) => date && date >= dayjs().endOf('day');
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: CustomerVoReq.settlementPay,
          };
          const data = await apiReq[type.value as 'add']({
            ...modelRef,
            payType: 1,
            rentContractLeaveDTO: customerData.value,
          });
          console.log(data, 'data');
          router.push({ name: 'departureSettlementRecord' });
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {
    };
    init();
    return {
      customerData,
      disabledDate,
      houseIdOptions,
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      buildingIdOptions,
      floorOptions,
    };
  },
});

function useOptions(customerData?:any, modelRef?:any, type?:any) {
  return {

  };
}
</script>
<style scoped lang="scss">
.customer-add-edit {
    .cursor {
        cursor: pointer;
    }

    .customer-title {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
        font-size: 18px;
        font-weight: bold;

        span {
            margin-left: 10px;
            font-weight: normal;
            color: var(--ant-primary-color);
            cursor: pointer;
        }
    }
}
</style>
