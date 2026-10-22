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
          v-if="customerConfig.type==='add'"
          label="卡号"
          name="cardNo"
          class="update-card"
        >
          <a-input
            id="valueInput"
            v-model:value="modelRef.cardNo"
            v-Alphabet
            :maxlength="16"
            :disabled="formDisabled"
            placeholder="请输入卡号"
          >
            <template
              v-if="!formDisabled"
              #addonAfter
            >
              <span
                class="card-blue"
                @click="readCardNo"
              >
                读卡
              </span>
            </template>
          </a-input>
        </a-form-item>
        <a-form-item
          v-if=" ['edit','detail'].includes(customerConfig.type)"
          label="卡号"
          name="cardNo"
          class="update-card"
        >
          <span>{{ modelRef.cardNo }}</span>
        </a-form-item>
        <a-form-item
          v-if="customerConfig.type!=='bind'"
          label="卡介质"
        >
          <span>IC/ID卡</span>
        </a-form-item>
        <a-form-item
          v-if="customerConfig.type!=='bind'"
          label="卡类型"
          name="cardType"
        >
          <v-select
            v-model:value="modelRef.cardType"
            :disabled="formDisabled"
            placeholder="请选择卡片类型"
          >
            <a-select-option
              v-for="opt in options.CARD_TYPE"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          v-if="customerConfig.type!=='bind'"
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
          label="关联人员"
          name="employeeId"
        >
          <v-select-employee
            v-model:value="modelRef.employeeId"
            :default-select-opts="defaultOptions"
            placeholder="请选择人员"
            title="选择人员"
            :disabled="!modelRef.companyId || formDisabled"
            mode="single"
            :company-ids="modelRef.companyId"
            :park-org-ids="modelRef.parkOrgId"
          />
        </a-form-item>
        <a-form-item
          v-if="customerConfig.type!=='bind'"
          label="授权有效期"
          name="effectStartDate"
        >
          <a-range-picker
            v-model:value="contractDate"
            style="width: 100%;"
            :disabled="customerConfig.type==='detail'"
            :placeholder="['开始时间', '结束时间']"
            value-format="YYYY-MM-DD"
          />
        </a-form-item>
        <a-form-item
          v-if="customerConfig.type!=='bind'"
          label="备注"
          name="remark"
        >
          <a-textarea
            v-model:value="modelRef.remark"
            :disabled="formDisabled"
            :maxlength="30"
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
import { typeOf } from 'mathjs';
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
    const isSetServe = ref<boolean>(false);
    const ws = ref();
    const { customerData, type } = toRefs(props.customerConfig);
    const { modelRef, rulesRef, fill } = customerForm();
    const {
      parkOpts, options, tagOpts, defaultOptions, parkOrgIdChange,
    } = useOptions(customerData?.value, modelRef, type.value);
    const CustomerVoReq = new CustomerVo();
    const formDisabled = type.value === 'detail' || type.value === 'edit';
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
    const OpenSocket = async () => {
      ws.value = new WebSocket('ws://localhost:12000');
      ws.value.onopen = function a() {
        console.log('Connection open ...');
      };
      ws.value.onmessage = function b(evt: { data: any; }) {
        console.log(`Recevied message: ${evt.data}`);
        const data = JSON.parse(evt.data);
        if (data.data) {
          message.success('读取成功！');
          modelRef.cardNo = data.data.cardNO;
        } else if (data.code === 7) {
          modelRef.cardNo = '';
          message.warning('读取卡号失败,请在读卡器上正确放上IC卡！');
        } else {
          modelRef.cardNo = '';
          message.warning('打开设备失败,请正确连接读卡设备！');
        }
      };
      ws.value.onclose = function c() {
        isSetServe.value = true;
        console.log('Connection closed.');
      };
    };
    const changeCompany = () => {
      defaultOptions.value = [];
    };
    const readCardNo = () => {
      let msg = '';
      msg = '{"id": 1, "session": 0, "method": "card.read","info": {"cardType":1} }';
      // console.log(`Connection send: ${msg}`);
      ws.value.send(msg);
      if (isSetServe.value) {
        message.warning('请先安装读卡插件,安装后请刷新当前页面，再进行操作！');
      }
    };

    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: CustomerVoReq.createCard,
            edit: CustomerVoReq.modifyCard,
            bind: CustomerVoReq.boundCard,
          };
          if (type.value === 'bind') {
            await apiReq[type.value as 'bind']({
              companyId: modelRef.companyId,
              employeeId: modelRef.employeeId,
              id: modelRef.id,
            });
            message.success('操作成功');
            emit('refresh', {});
            emit('update:show', false);
          } else {
            modelRef.cardMedium = 1;
            await apiReq[type.value as 'add' | 'edit']({ ...modelRef });
            message.success('操作成功');
            emit('refresh', {});
            emit('update:show', false);
          }
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {
      // StartUp();
      OpenSocket();
      if (type.value !== 'add') {
        fill(customerData.value);
        if (type.value !== 'bind') {
          const { employeeId, employeeName } = customerData.value;
          if (!employeeId) {
            defaultOptions.value = [];
            return;
          }
          defaultOptions.value = [{ label: employeeName || '', value: employeeId || '' }];
        }
      }
    };
    init();
    return {
      readCardNo,
      // StartUp,
      OpenSocket,
      changeCompany,
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
    :deep(.update-card .ant-input-group .ant-input) {
        cursor: pointer;
    }

    .cursor {
        cursor: pointer;
    }

    .card-blue {
        color: #19aff1;
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
