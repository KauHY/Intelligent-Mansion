<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-07 14:25:48
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
        label="停车场编号"
        v-bind="validateInfos.parkingLotCode"
      >
        <a-input
          v-model:value="modelRef.parkingLotCode"
          :maxlength="30"
          :disabled="type === 'edit'"
          placeholder="请输入停车场编号"
        />
      </a-form-item>
      <a-form-item
        label="停车场名称"
        v-bind="validateInfos.parkingLot"
      >
        <a-input
          v-model:value="modelRef.parkingLot"
          :maxlength="30"
          disabled
          placeholder="请输入停车场名称"
        />
      </a-form-item>
      <a-form-item
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          :disabled="type === 'edit'"
          placeholder="请选择关联园区"
        >
          <a-select-option
            v-for="opt in parkOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="车位数量（个）"
        v-bind="validateInfos.totalLot"
      >
        <a-input-number
          v-model:value="modelRef.totalLot"
          :formatter="formatter"
          :disabled="type === 'edit'"
          placeholder="请输入车位数量"
          :min="1"
          :max="999999999"
          style="width: 100%;"
        />
      </a-form-item>
      <a-form-item
        label="访客通行"
        v-bind="validateInfos.allowVisitor"
      >
        <a-radio-group
          v-model:value="modelRef.allowVisitor"
        >
          <a-radio :value="1">
            适用
          </a-radio>
          <a-radio :value="0">
            不适用
          </a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import CarPass, { ParkingLotInfo } from '~models/car-pass';
import CompanyMangement from '~models/company-mangement';
import { useParkingLot } from '../composition/useForm';

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

    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = useParkingLot();
    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'add') {
          await carPassInstance.parkingLotAdd(modelRef);
        } else {
          await carPassInstance.parkingLotEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    const companyOpts = ref<SelectOptions>([]);
    // const parkOrgChange = async (orgId:string) => {
    //   companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
    // };

    // const visitorClientTypeChange = () => {
    //   modelRef.visitorClientValue = '';
    // };

    // 初始化
    const details = ref<ParkingLotInfo>();
    // const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (type.value === 'edit') {
        details.value = await carPassInstance.parkingLotDetails(defaultData.value.id);
        fill(details.value);
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
      companyOpts,
      formatter: (value:string) => parseInt(value, 10) || null,
      // parkOrgChange,
      // defaultOptions,
      // visitorClientTypeChange,
    };
  },
});
</script>
