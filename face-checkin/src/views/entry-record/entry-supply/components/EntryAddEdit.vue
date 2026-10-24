<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-03-03 11:20:07
 * @LastEditors: Xucaizhi3
 * @Description:
-->
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
          label="申请入驻园区"
          name="parkOrgId"
        >
          <v-select
            v-model:value="modelRef.parkOrgId"
            :disabled="formDisabled"
            placeholder="请选择园区"
            filter-option
          >
            <a-select-option
              v-for="opt in parkOpts"
              :key="opt.label"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="联系人"
          name="contactPerson"
        >
          <a-input
            v-model:value="modelRef.contactPerson"
            :disabled="formDisabled"
            :maxlength="30"
            placeholder="请输入联系人"
          />
        </a-form-item>
        <a-form-item
          label="联系电话"
          name="contactPhone"
        >
          <a-input
            v-model:value="modelRef.contactPhone"
            :disabled="formDisabled"
            :maxlength="30"
            placeholder="请输入联系电话"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <a-button
        @click="emit('update:show', false)"
      >
        {{ formDisabled ? '关闭':'取消' }}
      </a-button>
      <a-button
        v-show="!formDisabled"
        type="primary"
        @click="confirm"
      >
        确定
      </a-button>
    </template>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, defineComponent, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useHashMapDics } from '~composition/useHashMapDic';
import Park from '~models/park';
import CustomerVo from '~models/company';
import { customerForm } from '../composition/useForm';

const parkInstance = new Park();
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
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<SelectOptions>([]);
    const floorOptions = ref<SelectOptions>([]);
    const { modelRef, rulesRef } = customerForm();
    const { customerData, type } = toRefs(props.customerConfig);
    const {
      parkOpts, options, parkOrgIdChange, tagOpts, defaultOptions,
    } = useOptions(customerData?.value, modelRef, type.value);
    const CustomerVoReq = new CustomerVo();
    const formDisabled = type.value === 'detail';
    const fill = () => {
      Object.entries(modelRef).forEach(([key]) => {
        if ((customerData.value as any)[key] || (customerData.value as any)[key] === 0) {
          const formData = modelRef as any;
          formData[key] = customerData.value[key];
        }
      });
    };

    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: CustomerVoReq.createAudit,
            edit: CustomerVoReq.createAudit,
          };
          await apiReq[type.value as 'add' | 'edit']({ ...modelRef });
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {
      if (type.value !== 'add') {
        fill();
      }
    };
    init();
    return {
      houseIdOptions,
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      parkOpts,
      parkOrgIdChange,
      tagOpts,
      formDisabled,
      defaultOptions,
      buildingIdOptions,
      floorOptions,
    };
  },
});

function useOptions(customerData?:any, modelRef?:any, type?:any) {
  const parkOpts = ref<SelectOptions>([]);
  const tagOpts = ref<SelectOptions>([]);
  const defaultOptions = ref<SelectOptions>([]);
  const options = useHashMapDics<
    ['SYSTEM_TYPE']
  >(['SYSTEM_TYPE']);
  (async () => {
    parkOpts.value = await new Park().getTenantParkOrgDict();
    if (type === 'add') {
      // 新增默认选择园区列表第一个
      modelRef.parkOrgId = parkOpts.value[0].value;
    }
  })();
  const parkOrgIdChange = async (value: string) => {
    if (customerData) {
      defaultOptions.value = [];
    }
  };
  return {
    parkOpts,
    options,
    tagOpts,
    parkOrgIdChange,
    defaultOptions,
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
