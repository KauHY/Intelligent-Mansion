<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-15 11:26:02
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <div>
    <a-form
      ref="formRef"
      :model="modelRef"
      :rules="rulesRef"
      :label-col="{ span: 5 }"
    >
      <a-row>
        <a-col :span="12">
          <a-form-item
            label="审核结果"
            name="auditResult"
          >
            <a-radio-group
              v-model:value="modelRef.auditResult"
            >
              <a-radio :value="1">
                通过
              </a-radio>
              <a-radio :value="2">
                不通过
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
      </a-row>
      <!-- <a-row>
        <a-col :span="12">
          <a-form-item
            label="审核意见"
            name="auditOpinion"
          >
            <a-textarea
              v-model:value="modelRef.auditOpinion"
              placeholder="请填写审核意见"
              :auto-size="{ minRows: 2, maxRows: 5 }"
              :maxlength="99"
            />
          </a-form-item>
        </a-col>
      </a-row> -->
    </a-form>
  </div>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, PropType, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import ContractVo, { ContractList, ContractType } from '~models/contract-manage';
import { verifyForm } from '../composition/useForm';

export default defineComponent({
  props: {
    id: {
      type: String,
      default: () => '',
    },
  },
  emits: {
    goBack: (data: object) => true,
  },
  setup(props, { emit }) {
    const { modelRef, rulesRef } = verifyForm();
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          await new ContractVo().contractReviewApi({ ...modelRef, id: props.id });
          emit('goBack', {});
          message.success('操作成功');
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {};
    init();
    return {
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
    };
  },
});
</script>
