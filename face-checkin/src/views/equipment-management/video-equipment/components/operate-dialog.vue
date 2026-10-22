<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-21 09:55:22
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
      :label-col="{ span: 5 }"
    >
      <a-form-item
        label="设备类型"
        v-bind="validateInfos.deviceType"
      >
        <v-select
          v-model:value="modelRef.deviceType"
          disabled
          placeholder="请选择设备类型"
        >
          <a-select-option
            v-for="opt in parkOpts.DEVICE_TYPE"
            :key="opt.value"
            :label="opt.label"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="设备名称"
        v-bind="validateInfos.deviceName"
      >
        <a-input
          v-model:value="modelRef.deviceName"
          :disabled="type==='details'"
          placeholder="请输入设备名称"
          :maxlength="30"
        />
      </a-form-item>
      <a-form-item
        label="设备ID"
        v-bind="validateInfos.deviceId"
      >
        <a-input
          v-model:value="modelRef.deviceId"
          disabled
          placeholder="请输入设备ID"
          :maxlength="30"
        />
      </a-form-item>
      <a-form-item
        label="IP地址"
        v-bind="validateInfos.ipAddress"
      >
        <a-input
          v-model:value="modelRef.ipAddress"
          disabled
          placeholder="请输入IP地址"
          :maxlength="30"
        />
      </a-form-item>

      <a-form-item
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择关联园区"
          disabled
          option-filter-prop="label"
          show-search
        >
          <a-select-option
            v-for="opt in parkPop"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="设备位置"
        v-bind="validateInfos.location"
      >
        <a-input
          v-model:value="modelRef.location"
          :disabled="type==='details'"
          placeholder="请输入设备位置"
          :maxlength="30"
        />
      </a-form-item>
      <a-form-item label="备注">
        <a-textarea
          v-model:value="modelRef.remark"
          placeholder="请输入备注"
          :rows="4"
          show-count
          :maxlength="200"
          :disabled="type==='details'"
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button @click="currentVisible=false">
        {{ type==='details' ? "关闭" : "取消" }}
      </a-button>
      <a-button
        v-show="type!=='details'"
        type="primary"
        @click="confirm"
      >
        确定
      </a-button>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watch,
} from 'vue';
import { message } from 'ant-design-vue';
import { REGS } from '@/constants/validate';
import EnterpriseService, { LaneInfo } from '~models/company-service';
import CompanyMangement from '~models/company-mangement';
import EquipmentManagement, { EquipmentInfo } from '~models/equipment-management';
import EntranceGuard from '~models/entranceGuard-management';
import { perfectEntranceGuard } from '../composition/useForm';

const equipmentManagementInstance = new EquipmentManagement();
const entranceGuardInstance = new EntranceGuard();

// const carPassInstance = new CarPass();
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
      type: Object,
      default: () => ({}),
    },
    parkPop: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
    // parkingLotOpts: {
    //   type: Object as PropType<SelectOptionsMap>,
    //   default: () => ({}),
    // },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { parkOpts, type, visible } = toRefs(props);
    const isReady = ref(false);
    const { defaultData } = toRefs(props);
    const companyPop = ref<SelectOptionsMap>([]);
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
    } = perfectEntranceGuard();
    const confirm = () => {
      validate().then(async () => {
        console.log('!!!!!!!!!!!', modelRef);
        await entranceGuardInstance.equipmentModify(modelRef);
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error: any) => {
        message.warning('操作失败');
      });
    };

    const parkingLotOpts = ref<SelectOptionsMap>([]);
    const parkOrgChange = async (orgId: string) => {
      console.log('orgId', orgId);
      console.log('parkingLotOpts', parkingLotOpts.value);
    };
    const filterOption = (input:any, option:any) => {
      console.log('optin', option, 'input', input);

      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
    };

    // 初始化
    const details = ref();
    const init = async () => {
      if (type.value === 'perfect' || type.value === 'details') {
        details.value = await equipmentManagementInstance.equipmentFindById(defaultData.value.id);
        console.log('details', details.value);

        fill(details.value);
        // parkOrgChange(defaultData.value.parkOrgId);
        isReady.value = true;
      }
    };
    init();
    return {
      currentVisible,
      details,
      confirm,
      modelRef,
      validateInfos,
      parkOrgChange,
      companyPop,
      parkingLotOpts,
      filterOption,
    };
  },
});
</script>
<style lang="less" scoped>
:deep(.serviceLabel .ant-form-item-label > label::after) {
    display: none;
}
</style>
