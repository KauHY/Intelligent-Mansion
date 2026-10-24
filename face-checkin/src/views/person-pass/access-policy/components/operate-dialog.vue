<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-07 15:13:04
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="820"
    class="operate-dialog"
    @ok="confirm"
  >
    <a-form
      ref="formRef"
      :model="modelRef"
      :rules="rulesRef"
      :label-col="{span:3}"
    >
      <a-form-item
        label="策略名称"
        name="policyName"
      >
        <a-input
          v-model:value="modelRef.policyName"
          :maxlength="30"
          :disabled="type==='details'"
          placeholder="请输入策略名称"
        />
      </a-form-item>
      <a-form-item
        label="关联园区"
        name="parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          filter-option
          :disabled="type==='details'"
          placeholder="请选择关联园区"
          @change="parkOrgChange"
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
        label="关联企业"
        name="companyId"
      >
        <v-select
          v-model:value="modelRef.companyId"
          filter-option
          :disabled="type==='details'"
          mode="multiple"
          placeholder="请选择关联企业"
          @change="companyChange"
        >
          <a-select-option
            v-for="opt in companyOpts"
            :key="opt.label"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="允许员工"
        name="tagIds"
      >
        <v-select
          v-model:value="modelRef.tagIds"
          mode="multiple"
          :disabled="type==='details'"
          placeholder="请选择允许员工"
        >
          <a-select-option
            v-for="opt in tagOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
        <p class="form-tip">
          如果不选择员工标签，则默认位选中全部员工
        </p>
      </a-form-item>
      <a-form-item
        label="访客通行"
        name="tagIds"
      >
        <a-radio-group
          v-model:value="modelRef.allowVisitor"
          :disabled="type==='details'"
        >
          <a-radio
            v-for="opt in allowVisitorOpts"
            :key="opt.value"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item
        label="设备"
        name="deviceIds"
      >
        <v-select
          v-model:value="modelRef.deviceIds"
          :disabled="type==='details'"
          mode="multiple"
          placeholder="请选择公共门禁设备"
        >
          <a-select-option
            v-for="opt in deviceOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="有效时间"
        name="policyStartTime"
      >
        <a-range-picker
          v-model:value="rangeTime"
          value-format="YYYY-MM-DD"
          :disabled="type==='details'"
        />
      </a-form-item>
      <a-form-item
        label="通行时间"
        name="allTime"
      >
        <a-radio-group
          v-model:value="modelRef.allTime"
          :disabled="type==='details'"
        >
          <a-radio
            :value="0"
          >
            全时段
          </a-radio>
          <!-- <a-radio
            :value="1"
          >
            指定时段
          </a-radio> -->
        </a-radio-group>
      </a-form-item>
      <a-row v-if="modelRef.allTime===1">
        <a-col
          :span="21"
          :offset="3"
        >
          <a-form-item
            name="specifiedTimePeriod.dayOfWeek"
          >
            <a-checkbox-group
              v-model:value="modelRef.specifiedTimePeriod.dayOfWeek"
              :disabled="type==='details'"
            >
              <a-checkbox
                v-for="opt in weekDayOpts"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </a-checkbox>
            </a-checkbox-group>
          </a-form-item>
          <a-form-item
            :name="['specifiedTimePeriod','startTime']"
          >
            <v-range-time
              v-model:startTime="modelRef.specifiedTimePeriod.startTime"
              v-model:endTime="modelRef.specifiedTimePeriod.endTime"
              :disabled="type==='details'"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          {{ type==="details"?'关闭':'取消' }}
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
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import PersonPass, { AccessPolicy } from '~models/person-pass';
import CompanyMangement from '~models/company-mangement';
import EquipmentManagement from '~models/equipment-management';
import { useAddAccessPolicy } from '../composition/useForm';

const personPassInstance = new PersonPass();
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
    allowVisitorOpts: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
    weekDayOpts: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { type, visible } = toRefs(props);
    const isReady = ref(false);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return ['edit'].includes(type.value) ? isReady.value && visible.value : visible.value;
      },
    });
    const { defaultData } = toRefs(props);
    const formRef = ref();
    const {
      modelRef,
      rulesRef,
      // validate,
      // validateInfos,
      fill,
    } = useAddAccessPolicy();

    const companyOpts = ref<SelectOptions>([]);
    const deviceOpts = ref<SelectOptions>([]);
    const parkOrgChange = async (parkOrgId:string, isClear = true) => {
      companyOpts.value = await new CompanyMangement().listCompanyDict(parkOrgId);
      if (parkOrgId) {
        deviceOpts.value = await new EquipmentManagement().equipmentListDic(parkOrgId, 1);
      } else {
        deviceOpts.value = [];
      }

      // debugger;
      if (isClear) {
        modelRef.companyId = [];
        modelRef.deviceIds = [];
      }
    };
    const rangeTime = computed({
      set(value:string[]) {
        if (!value) value = [];
        const [policyStartTime, policyExpireTime] = value;
        modelRef.policyExpireTime = policyExpireTime || '';
        modelRef.policyStartTime = policyStartTime || '';
      },
      get() {
        const { policyStartTime, policyExpireTime } = modelRef;
        return policyStartTime && policyExpireTime ? [policyStartTime, policyExpireTime] : [];
      },
    });
    const tagOpts = ref<SelectOptions>();
    const companyChange = async (companyIds:string[], isClear = true) => {
      tagOpts.value = await new CompanyMangement().listTagDict({
        companyIds,
      });
      if (isClear) {
        modelRef.tagIds = [];
      }
    };
    const confirm = () => {
      formRef.value.validate().then(async () => {
        if (type.value === 'add') {
          await personPassInstance.accessPolicyAdd(modelRef);
        } else {
          await personPassInstance.accessPolicyModify(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        // message.warning('操作失败');
      });
    };

    // 初始化
    const details = ref<AccessPolicy>();
    // const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (type.value !== 'add') {
        details.value = await personPassInstance.accessPolicyFindById(defaultData.value.id);
        fill(details.value);
        parkOrgChange(modelRef.parkOrgId, false);
        companyChange(modelRef.companyId, false);
        isReady.value = true;
      }
    };
    init();
    return {
      rangeTime,
      currentVisible,
      details,
      confirm,
      modelRef,
      formRef,
      companyOpts,
      tagOpts,
      parkOrgChange,
      companyChange,
      deviceOpts,
      rulesRef,
    };
  },
});
</script>
