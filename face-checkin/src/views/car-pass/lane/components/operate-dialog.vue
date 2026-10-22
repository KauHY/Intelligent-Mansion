<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-30 18:36:31
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
        label="车道名称"
        v-bind="validateInfos.laneName"
      >
        <a-input
          v-model:value="modelRef.laneName"
          :maxlength="30"
          placeholder="请输入车道名称"
        />
      </a-form-item>
      <a-form-item
        label="出入口"
        v-bind="validateInfos.carDirectStr"
      >
        <a-input
          v-model:value="modelRef.carDirectStr"
          :maxlength="30"
          :disabled="type === 'edit'"
          placeholder="请输入出入口"
        />
      </a-form-item>
      <a-form-item
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择关联园区"
          filter-option
          :disabled="type === 'edit'"
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
        label="关联停车场"
        v-bind="validateInfos.parkingLotId"
      >
        <v-select
          v-model:value="modelRef.parkingLotId"
          filter-option
          :disabled="type === 'edit'"
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
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import CarPass, { LaneInfo } from '~models/car-pass';
import CompanyMangement from '~models/company-mangement';
import { useLaneAdd } from '../composition/useForm';

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
    // parkingLotOpts: {
    //   type: Object as PropType<SelectOptionsMap>,
    //   default: () => ({}),
    // },
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
    } = useLaneAdd();
    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'add') {
          await carPassInstance.laneAdd(modelRef);
        } else {
          await carPassInstance.laneEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    const parkingLotOpts = ref<SelectOptions>([]);
    const parkOrgChange = async (orgId:string) => {
      if (!orgId) return;
      parkingLotOpts.value = await carPassInstance.parkingLotDic(orgId);
    };

    // 初始化
    const details = ref<LaneInfo>();
    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (type.value === 'edit') {
        details.value = await carPassInstance.laneDetails(defaultData.value.id);
        fill(details.value);
        parkOrgChange(modelRef.parkOrgId);
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
      parkingLotOpts,
    };
  },
});
</script>
