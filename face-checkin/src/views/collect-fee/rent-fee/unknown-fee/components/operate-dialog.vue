<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-12 14:40:26
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
    class="operate-dialog"
    @ok="confirm"
  >
    <a-form
      :model="modelRef"
      :label-col="{span:5}"
    >
      <a-form-item
        label="应收日期"
        v-bind="validateInfos.dueDate"
      >
        <a-date-picker
          v-model:value="modelRef.dueDate"
          :disabled-date="disabledDate"
          value-format="YYYY-MM-DD"
          placeholder="请选择应收日期"
          @change="changeContractEndDate"
        />
      </a-form-item>
      <a-form-item
        label="最晚缴纳日期"
        v-bind="validateInfos.latestPayDate"
      >
        <a-date-picker
          v-model:value="modelRef.latestPayDate"
          :disabled-date="disabledDueDate"
          value-format="YYYY-MM-DD"
          placeholder="请选择最晚缴纳日期"
        />
      </a-form-item>
    </a-form>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect,
} from 'vue';
import { message } from 'ant-design-vue';
import { Dayjs } from 'dayjs';
import * as dayjs from 'dayjs';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
// import InformationStatistical, { FormManageInfo } from '~models/information-statistical';
import ContractManage, { ContractTemplateListInfo, ContractType } from '~models/contract-manage';
import { RentFee, RentFeeInfo } from '~models/collect-fee';
import { useModifyDate } from '../composition/useForm';

const rentFeeInstance = new RentFee();

export default defineComponent({
  name: 'OperateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object,
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
    ids: {
      type: Array as PropType<string[]>,
      default: () => ([]),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const {
      type, visible, defaultData, ids,
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

    const {
      modelRef,
      validate,
      validateInfos,
      // fill,
    } = useModifyDate();

    const changeContractEndDate = async (value: any) => {
      modelRef.latestPayDate = '';
    };

    const disabledDate = (time: any) => {
      if (!time) return false;
      const times1 = dayjs().format('YYYY-MM-DD');
      const times = dayjs(time).format('YYYY-MM-DD');
      return times < times1;
    };
    const disabledDueDate = (time: any) => {
      if (!time) return false;
      const { dueDate } = modelRef;
      const times1 = dayjs(dueDate).format('YYYY-MM-DD');
      const times = dayjs(time).format('YYYY-MM-DD');
      return times < times1;
    };

    const confirm = () => {
      validate().then(async () => {
        await rentFeeInstance.rentFeeModifyDate({
          ...modelRef,
          ids: ids.value,
        });
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
      validateInfos,
      options,
      disabledDate,
      disabledDueDate,
      changeContractEndDate,
    };
  },
});
</script>

<style lang="less" scoped>
.flex {
    display: flex;
    align-items: center;
    justify-content: flex-start;
}
</style>
