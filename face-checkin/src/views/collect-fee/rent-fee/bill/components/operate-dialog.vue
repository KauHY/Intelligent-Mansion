<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-27 20:31:42
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="['batchReceive','deduction'].includes(type)?520:920"
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
      <component
        :is="cp"
        :type="type"
        v-bind="$attrs"
        :select-keys="selectKeys"
        :default-data="defaultData"
      />
    </a-form>
    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          {{ type ==='details'?'关闭':'取消' }}
        </a-button>
        <a-button
          v-if="type!=='details'"
          type="primary"
          @click="confirm"
        >
          确认
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect,
} from 'vue';
import { message } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
// import InformationStatistical, { FormManageInfo } from '~models/information-statistical';
import { RentFee, RentFeeBillInfo } from '~models/collect-fee';
import { useOperateBill } from '../composition/useForm';
import details from './details.vue';
import batchReceive from './batch-receive.vue';
import deduction from './deduction.vue';

const rentFeeInstance = new RentFee();
export default defineComponent({
  name: 'OperateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<RentFeeBillInfo>,
      default: () => ({}),
    },
    title: {
      type: String,
      default: '',
    },
    otherData: {
      type: Number,
      default: 0,
    },
    type: {
      type: String as PropType<'batchReceive' |'details'|'edit'|'deduction'|'receive'>,
      default: '',
    },
    selectKeys: {
      type: Array as PropType<string[]>,
      default: () => ([]),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const {
      type, visible, defaultData, selectKeys, otherData,
    } = toRefs(props);

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

    const cp = shallowRef();
    let useForm:any;
    const formRef = shallowRef();
    if (['details', 'edit', 'receive'].includes(type.value)) {
      cp.value = details;
    } else if (type.value === 'batchReceive') {
      cp.value = batchReceive;
    } else {
      cp.value = deduction;
    }

    const {
      modelRef,
      rulesRef,
      // validate,
      // validateInfos,
      // fill,
    } = useOperateBill();
    // const templateKeyChange = (keys:string[], names:string) => {
    //   modelRef.templateName = names;
    // };
    const confirm = () => {
      formRef.value.validate().then(async () => {
        if (['receive', 'batchReceive'].includes(type.value)) { // 收款
          const api = otherData.value === 6 || otherData.value === 7 ? rentFeeInstance.checkout : rentFeeInstance.rentFeeBillCheckout;
          await api({
            ids: type.value === 'batchReceive' ? selectKeys.value : [defaultData.value.id],
            remark: modelRef.remark,
            checkoutDate: modelRef.checkoutDate,
            payType: 1,
          });
        } else if (type.value === 'edit') { // 编辑
          const api = otherData.value === 6 || otherData.value === 7 ? rentFeeInstance.modifyConsumptionBill : rentFeeInstance.rentFeeBillModify;
          await api({
            id: defaultData.value.id,
            accountPeriod: modelRef.accountPeriod,
          });
        } else if (type.value === 'deduction') { // 减免
          const api = otherData.value === 6 || otherData.value === 7 ? rentFeeInstance.deductionFee : rentFeeInstance.rentFeeBillDeduction;
          await api({
            id: defaultData.value.id,
            file: modelRef.file,
            remark: modelRef.remark,
            deductionAmount: modelRef.deductionAmount,
          });
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };
    return {
      currentVisible,
      confirm,
      modelRef,
      rulesRef,
      options,
      cp,
      formRef,
    };
  },
});
</script>
