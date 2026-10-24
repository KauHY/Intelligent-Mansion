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
          label="设备类型"
        >
          <span>会议室信息屏</span>
        </a-form-item>
        <a-form-item
          label="设备名称"
          name="deviceName"
          class="update-card"
        >
          <a-input
            v-model:value="modelRef.deviceName"
            :maxlength="30"
            :disabled="formDisabled"
            placeholder="请输入设备名称"
          />
        </a-form-item>
        <a-form-item
          label="设备ID"
          name="deviceId"
          class="update-card"
        >
          <a-input
            v-model:value="modelRef.deviceId"
            :maxlength="30"
            :disabled="formDisabled"
            placeholder="请输入设备ID"
          />
        </a-form-item>

        <a-form-item

          label="关联园区"
          name="parkOrgId"
        >
          <v-select
            v-model:value="modelRef.parkOrgId"
            placeholder="请选择所属园区"
            :disabled="formDisabled"
            @change="parkOrgIdChange"
          >
            <a-select-option
              v-for="opt in parkOpts"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>

        <a-form-item
          label="关联企业"
          name="companyId"
        >
          <v-select
            v-model:value="modelRef.companyId"
            filter-option
            :disabled="formDisabled"
            placeholder="请选择企业"
            @change="changeCompany"
          >
            <a-select-option
              v-for="opt in tagOpts"
              :key="opt.label"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>

        <a-form-item

          label="设备位置"
          name="location"
        >
          <a-textarea
            v-model:value="modelRef.location"
            :disabled="formDisabled"
            :maxlength="30"
            placeholder="请输入设备位置"
          />
        </a-form-item>
        <a-form-item
          label="备注"
          name="remark"
        >
          <a-textarea
            v-model:value="modelRef.remark"
            :disabled="formDisabled"
            :maxlength="200"
            placeholder="请输入备注"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <a-button
        v-show="customerConfig.type!=='detail'"
        type="primary"
        @click="confirm"
      >
        保存
      </a-button>
      <a-button
        @click="emit('update:show', false)"
      >
        {{ formDisabled ? '关闭':'取消' }}
      </a-button>
    </template>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, computed, defineComponent, toRefs,
} from 'vue';
import { message } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics } from '~composition/useHashMapDic';
import Park from '~models/park';
import CustomerVo from '~models/equipment-management';
import { customerForm } from '../composition/useForm';

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
    const jumpType = ref();
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<SelectOptions>([]);
    const floorOptions = ref<SelectOptions>([]);
    const { customerData, type } = toRefs(props.customerConfig);
    const { modelRef, rulesRef, fill } = customerForm();
    const {
      parkOpts, options, tagOpts, defaultOptions, parkOrgIdChange,
    } = useOptions(customerData?.value, modelRef, type.value);
    const CustomerVoReq = new CustomerVo();
    const formDisabled = type.value === 'detail';
    const changeJumpType = async (value: string) => {
      jumpType.value = value;
    };
    const formRef = ref();
    const changeCompany = () => {
      defaultOptions.value = [];
    };
    const readCard = () => {};
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: CustomerVoReq.equipmentAdd,
            edit: CustomerVoReq.equipmentModify,
          };

          await apiReq[type.value as 'add' | 'edit']({ ...modelRef });

          message.success('操作成功');
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {
      if (type.value !== 'add') {
        fill(customerData.value);
        const { employeeId, employeeName } = customerData.value;
        defaultOptions.value = [{ label: employeeName || '', value: employeeId || '' }];
      }
    };
    init();
    return {
      changeCompany,
      readCard,
      changeJumpType,
      houseIdOptions,
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      parkOpts,
      tagOpts,
      formDisabled,
      defaultOptions,
      buildingIdOptions,
      floorOptions,
      parkOrgIdChange,
    };
  },
});

function useOptions(customerData?:any, modelRef?:any, type?:any) {
  const parkOpts = ref<SelectOptions>([]);
  const tagOpts = ref<SelectOptions>([]);
  const defaultOptions = ref<SelectOptions>([]);
  const options = useHashMapDics<
    ['CARD_TYPE', 'ADVERTISEMENT_JUMP_TYPE']
  >(['CARD_TYPE', 'ADVERTISEMENT_JUMP_TYPE']);
  const getTagOpts = async (value:string) => {
    tagOpts.value = await new CompanyMangement().listCompanyDict(value);
  };
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
    if (type === 'add') {
      // 新增默认选择园区列表第一个
      modelRef.parkOrgId = parkOpts.value[0].value;
      getTagOpts(modelRef.parkOrgId);
    } else {
      getTagOpts(customerData.parkOrgId);
    }
  })();
  const parkOrgIdChange = async (value: string) => {
    modelRef.companyId = '';
    defaultOptions.value = [];
    getTagOpts(value);
  };
  return {
    parkOrgIdChange,
    parkOpts,
    options,
    tagOpts,
    defaultOptions,
    getTagOpts,
  };
}
</script>
<style scoped lang="scss">
.customer-add-edit {
    .cursor {
        cursor: pointer;
    }

    .card-blue {
        color: #19aff1;
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
