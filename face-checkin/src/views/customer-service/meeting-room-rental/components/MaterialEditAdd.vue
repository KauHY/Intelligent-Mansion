<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-03-09 18:11:00
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    class="customer-add-edit"
    :visible="true"
    :width="500"
    :title="tagConfig.title"
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
          label="标签名称"
          name="name"
        >
          <a-input
            v-model:value="modelRef.name"
            placeholder="请输入标签名称"
          />
        </a-form-item>
      </a-form>
    </div>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, PropType, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import CustomerVo, { CustomerInfoKind } from '~models/customer-manage';
import { materialForm } from '../composition/useForm';

export default defineComponent({
  props: {
    tagConfig: {
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
    const { parkOpts, options } = useOptions();
    const { modelRef, rulesRef } = materialForm();
    const { tagData, type } = toRefs(props.tagConfig);
    const CustomerVoReq = new CustomerVo();
    const fill = () => {
      Object.entries(modelRef).forEach(([key]) => {
        if ((tagData.value as any)[key] || (tagData.value as any)[key] === 0) {
          const formData = modelRef as any;
          formData[key] = tagData.value[key];
        }
      });
    };
    if (type.value !== 'add') {
      fill();
    }
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: CustomerVoReq.customerTagCreateApi,
            edit: CustomerVoReq.customerTagModifyApi,
          };
          // await apiReq[type.value as 'add' | 'edit'](modelRef);
          message.success('操作成功');
          emit('refresh', {});
          emit('update:show', false);
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
      options,
      parkOpts,
    };
  },
});

function useOptions() {
  const parkOpts = ref<SelectOptions>([]);
  const options = useHashMapDics<['CUSTOMERINFO_TYPE', 'CUSTOMERINFO_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'COMPANY_MANAGEMENT_STATUS']>(['CUSTOMERINFO_TYPE', 'CUSTOMERINFO_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'COMPANY_MANAGEMENT_STATUS']);
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
  })();
  return {
    parkOpts,
    options,
  };
}
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
