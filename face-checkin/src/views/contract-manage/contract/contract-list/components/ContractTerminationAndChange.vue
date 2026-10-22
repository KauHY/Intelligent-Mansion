<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-24 13:53:15
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    class="tc-config"
    :visible="true"
    :width="500"
    :title="tcConfig.title"
    @ok="confirm"
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
          v-if="type === 'change'"
          label="变更类型"
          name="changeType"
        >
          <v-select
            v-model:value="modelRef.changeType"
            placeholder="请选择变更类型"
          >
            <a-select-option
              v-for="opt in changeTypeOpts"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <div v-if="type === 'withdrawal'">
          <a-form-item
            label="退约日期"
            name="withdrawDate"
          >
            <a-date-picker
              v-model:value="modelRef.withdrawDate"
              placeholder="请选择退约日期"
              style="width: 100%;"
              value-format="YYYY-MM-DD"
              :get-calendar-container="getCalendarContainer"
              :disabled-date="disabledDate"
            />
          </a-form-item>
          <a-form-item
            label="退约原因"
            name="withdrawReason"
          >
            <a-textarea
              v-model:value="modelRef.withdrawReason"
              placeholder="请输入退约原因"
              :auto-size="{ minRows: 2, maxRows: 5 }"
              :maxlength="99"
            />
          </a-form-item>
        </div>
      </a-form>
    </div>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, PropType, toRefs,
} from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import * as dayjs from 'dayjs';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import ContractVo, { ContractList, ContractType } from '~models/contract-manage';
import { tcForm } from '../composition/useForm';

export default defineComponent({
  props: {
    tcConfig: {
      type: Object,
      default: () => ({
        title: '',
        type: '',
        tcData: {},
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const router = useRouter();
    const options = useHashMapDics(['CONTRACT_CHANGE_TYPE']);
    const { modelRef, rulesRef } = tcForm();
    const { tcData, type } = toRefs(props.tcConfig);
    const ContractVoReq = new ContractVo();
    const changeTypeOpts = ref(options.CONTRACT_CHANGE_TYPE);
    if (tcData.value.type === 1 || tcData.value.type === 2) {
      delete changeTypeOpts.value[4];
    }
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        switch (type.value) {
          case 'withdrawal':
            try {
              delete modelRef.changeType;
              await ContractVoReq.contractWithdrawalApi({ ...modelRef, id: tcData.value.id });
              message.success('操作成功');
              emit('refresh', {});
              emit('update:show', false);
            } catch (error: any) {
              console.log(error);
            }
            break;
          case 'change':
            router.push({
              path: '/contract-manage/contract-draft-edit',
              query: {
                id: tcData.value.id, handleType: type.value, contractType: tcData.value.type, changeType: modelRef.changeType,
              },
            });
            break;
          default:
            break;
        }
      });
    };
    const getCalendarContainer = (triggerNode:HTMLElement) => document.body;
    const disabledDate = (time: any) => {
      if (!time) return false;
      return dayjs(time).format('YYYY-MM-DD') < dayjs(new Date()).format('YYYY-MM-DD');
    };
    const init = async () => {};
    init();
    return {
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      type,
      changeTypeOpts,
      getCalendarContainer,
      disabledDate,
    };
  },
});
</script>
<style scoped lang="scss">
.customer-add-edit {
    .cursor {
        cursor: pointer;
    }

    .customer-title {
        margin-bottom: 20px;
        font-size: 18px;
        font-weight: bold;
    }
}
</style>
