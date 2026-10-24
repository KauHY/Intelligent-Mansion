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
          label="所属园区"
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
          label="所属企业"
          name="companyId"
        >
          <v-select
            v-model:value="modelRef.companyId"
            filter-option
            :disabled="formDisabled"
            placeholder="请选择企业名称"
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
          label="选择人员"
          name="employeeId"
        >
          <v-select-employee
            v-model:value="modelRef.employeeId"
            :default-select-opts="defaultOptions"
            placeholder="请选择人员"
            :disabled="!modelRef.companyId || formDisabled"
            title="选择人员"
            mode="single"
            :company-ids="modelRef.companyId"
            :park-org-ids="modelRef.parkOrgId"
          />
        </a-form-item>
        <a-form-item
          label="身份证号"
          name="idCardNumber"
        >
          <a-input
            v-model:value="modelRef.idCardNumber"
            :maxlength="30"
            :disabled="formDisabled"
            placeholder="请输入身份证号"
          />
        </a-form-item>
        <a-form-item
          label="关联房间"
          name="houseIds"
        >
          <v-select-house
            v-model:value="modelRef.houseIds"
            :default-select-opts="modelRef.houseNames"
            :park-org-ids="modelRef.parkOrgId"
            :disabled="formDisabled"
          />
        </a-form-item>
        <a-form-item
          label="通行授权"
          name="deviceIds"
        >
          <v-select
            v-model:value="modelRef.deviceIds"
            filter-option
            :disabled="formDisabled"
            mode="multiple"
            placeholder="请选择通行设备"
          >
            <a-select-option
              v-for="opt in devicesOpts"
              :key="opt.label"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="有效日期"
          name="effectStartDate"
        >
          <a-range-picker
            v-model:value="contractDate"
            style="width: 100%;"
            :disabled="formDisabled"
            :placeholder="['开始日期  ', '结束日期']"
            value-format="YYYY-MM-DD"
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
        v-show="!formDisabled"
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
import { message, Modal } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics } from '~composition/useHashMapDic';
import Park from '~models/park';
import CustomerVo from '~models/smarter-apartment';
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
      parkOpts, options, tagOpts, defaultOptions, parkOrgIdChange, devicesOpts,
    } = useOptions(customerData?.value, modelRef, type.value);
    const CustomerVoReq = new CustomerVo();
    const formDisabled = type.value === 'detail';
    const changeJumpType = async (value: string) => {
      jumpType.value = value;
    };
    const formRef = ref();
    const contractDate = computed({
      set(value: string[]) {
        const [startDate, endDate] = value;
        if (!startDate || !endDate) return;
        modelRef.effectEndDate = endDate;
        modelRef.effectStartDate = startDate;
      },
      get() {
        const { effectStartDate, effectEndDate } = modelRef;
        return effectStartDate && effectEndDate ? [effectStartDate, effectEndDate] : [];
      },
    });
    const changeCompany = () => {
      defaultOptions.value = [];
    };
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: CustomerVoReq.createApartment,
            edit: CustomerVoReq.modifyApartment,
          };
          console.log(modelRef, 'modelRef');
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
        fill(customerData?.value);
        const { employeeId, employeeName } = customerData.value;
        defaultOptions.value = [{ label: employeeName || '', value: employeeId || '' }];
      }
    };
    init();
    return {
      changeCompany,
      devicesOpts,
      contractDate,
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
  const devicesOpts = ref<SelectOptions>([]);
  const options = useHashMapDics<
    ['SUPPLY_TYPE', 'ADVERTISEMENT_JUMP_TYPE']
  >(['SUPPLY_TYPE', 'ADVERTISEMENT_JUMP_TYPE']);
  const getTagOpts = async (value:string) => {
    tagOpts.value = await new CompanyMangement().listCompanyDict(value);
  };
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
    if (type === 'add') {
      // 新增默认选择园区列表第一个
      modelRef.parkOrgId = parkOpts.value[0].value;
    }
    devicesOpts.value = await new CustomerVo().listDropDownDevices(2, modelRef.parkOrgId);
    getTagOpts(modelRef.parkOrgId);
  })();
  const parkOrgIdChange = async (value: string) => {
    modelRef.companyId = '';
    defaultOptions.value = [];
    modelRef.houseNames = [];
    modelRef.deviceIds = [];
    getTagOpts(value);
    devicesOpts.value = await new CustomerVo().listDropDownDevices(2, value);
  };
  return {
    devicesOpts,
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
