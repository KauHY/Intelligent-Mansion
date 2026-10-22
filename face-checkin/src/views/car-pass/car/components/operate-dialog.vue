<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-12 20:17:50
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
        label="车牌号"
        v-bind="validateInfos.carNum"
      >
        <a-input
          v-model:value="modelRef.carNum"
          :disabled="type==='details'|| type==='edit'"
          placeholder="请输入车牌号（示例：辽A0475警、川A123AB）"
        >
          <template #suffix>
            <a-tooltip title="请省略车牌中'.'或者'-'等连接符">
              <v-svg name="prompt-fill" />
            </a-tooltip>
          </template>
        </a-input>
      </a-form-item>
      <!-- <a-form-item
        label="车主姓名"
        v-bind="validateInfos.carOwnerName"
      >
        <a-input
          v-model:value="modelRef.carOwnerName"
          :disabled="type==='details'"
          placeholder="请输入车主姓名"
          :maxlength="20"
        />
      </a-form-item> -->
      <!-- <a-form-item
        label="手机号"
        v-bind="validateInfos.tel"
      >
        <a-input
          v-model:value="modelRef.tel"
          :disabled="type==='details'"
          placeholder="请输入手机号"
        />
      </a-form-item> -->
      <a-form-item
        label="停车类型"
        v-bind="validateInfos.parkType"
      >
        <v-select
          v-model:value="modelRef.parkType"
          :disabled="type==='details' || type==='edit'"
          placeholder="请选择停车类型"
        >
          <a-select-option
            v-for="opt in carTypeOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          :disabled="type==='details'"
          placeholder="请选择关联园区"
          filter-option
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
        v-bind="validateInfos.companyId"
      >
        <v-select
          v-model:value="modelRef.companyId"
          filter-option
          :disabled="type==='details'"
          placeholder="请选择企业名称"
          @change="changeCompany"
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
        label="授权有效期"
        v-bind="validateInfos.authStartTime"
      >
        <a-range-picker
          v-model:value="rangeTime"
          :disabled="type==='details'"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </a-form-item>
      <a-form-item
        label="关联员工"
        v-bind="validateInfos.employeeId"
      >
        <v-select-employee
          v-model:value="modelRef.employeeId"
          mode="single"
          :park-org-ids="[modelRef.parkOrgId]"
          :company-ids="modelRef.companyId"
          :disabled="type==='details' || !modelRef.parkOrgId || !modelRef.companyId"
          :default-select-opts="defaultEmployeeSelectOpts"
          placeholder="请选择要关联的员工"
        />
      </a-form-item>
      <a-form-item
        label="关联停车场"
        v-bind="validateInfos.parkingLotIds"
      >
        <v-select
          v-model:value="modelRef.parkingLotIds"
          filter-option
          :disabled="type==='details'"
          mode="multiple"
          placeholder="请选择停车场"
        >
          <a-select-option
            v-for="opt in parkingLotOpts"
            :key="opt.label"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
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
import CarPass, { CarInfo } from '~models/car-pass';
import * as dayjs from 'dayjs';
import CompanyMangement from '~models/company-mangement';
import { useCarAdd } from '../composition/useForm';

const carPassInstance = new CarPass();
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
    carTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    const parkingLotOpts = ref<SelectOptions>([]);
    const { type, visible } = toRefs(props);
    const isReady = ref(false);
    const { defaultData } = toRefs(props);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return ['edit'].includes(type.value) ? isReady.value && visible.value : visible.value;
      },
    });
    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = useCarAdd();

    const rangeTime = computed({
      set(value:string[]) {
        const [authStartTime, authEndTime] = value;
        if (!authStartTime || !authEndTime) return;
        modelRef.authEndTime = authEndTime;
        modelRef.authStartTime = authStartTime;
      },
      get() {
        const { authStartTime, authEndTime } = modelRef;
        return authStartTime && authEndTime ? [authStartTime, authEndTime] : [];
      },
    });
    const confirm = () => {
      validate().then(async () => {
        modelRef.authEndTime = dayjs(modelRef.authEndTime).format('YYYY-MM-DD 23:59:59');
        modelRef.authStartTime = dayjs(modelRef.authStartTime).format('YYYY-MM-DD 00:00:00');
        if (type.value === 'add') {
          await carPassInstance.carAdd(modelRef);
        } else {
          await carPassInstance.carEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        console.log(error, 'error');
        message.warning('操作失败');
      });
    };

    const companyOpts = ref<SelectOptions>([]);
    const changeCompany = () => {
      modelRef.employeeId = '';
      modelRef.carOwnerName = '';
      defaultEmployeeSelectOpts.value = [];
    };
    const parkOrgChange = async (orgId:string, isInit = false) => {
      companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
      parkingLotOpts.value = await carPassInstance.parkingLotDic(orgId);
      modelRef.companyId = '';
      modelRef.employeeId = '';
      modelRef.carOwnerName = '';
      defaultEmployeeSelectOpts.value = [];
    };

    // 初始化
    const details = ref<CarInfo>();
    const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (type.value === 'edit' || type.value === 'details') {
        details.value = await carPassInstance.carDetails(defaultData.value.id);
        fill(details.value);
        companyOpts.value = await new CompanyMangement().listCompanyDict(modelRef.parkOrgId);
        parkingLotOpts.value = await carPassInstance.parkingLotDic(modelRef.parkOrgId);
        isReady.value = true;
        const { carOwnerName, employeeId } = details.value;
        defaultEmployeeSelectOpts.value = [{
          label: carOwnerName,
          value: employeeId,
        }];
      }
    };
    init();
    return {
      changeCompany,
      currentVisible,
      details,
      confirm,
      modelRef,
      validateInfos,
      companyOpts,
      parkOrgChange,
      defaultOptions,
      rangeTime,
      parkingLotOpts,
      defaultEmployeeSelectOpts,
    };
  },
});
</script>
