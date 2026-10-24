<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-21 09:57:09
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
    class="operate-dialog"
  >
    <a-form
      ref="formRef"
      :model="modelRef"
      :rules="disabled?'':rulesRef"
      :label-col="{span:5}"
    >
      <a-form-item
        label="设备类型"
        name="deviceType"
      >
        <v-select
          v-model:value="modelRef.deviceType"
          disabled
          placeholder="请选择设备类型"
        >
          <a-select-option
            v-for="opt in parkOpts.DEVICE_TYPE"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="设备名称"
        name="deviceName"
      >
        <a-input
          v-model:value="modelRef.deviceName"
          disabled
          placeholder="请输入设备名称"
        />
      </a-form-item>
      <a-form-item
        label="设备ID"
        name="deviceId"
      >
        <a-input
          v-model:value="modelRef.deviceId"
          disabled
          placeholder="请输入设备ID"
        />
      </a-form-item>
      <a-form-item
        label="IP地址"
      >
        <a-input
          v-model:value="modelRef.ipAddress"
          disabled
          placeholder="请输入IP地址"
        />
      </a-form-item>
      <a-form-item
        label="关联园区"
        name="parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          disabled
          placeholder="请选择关联园区"
          @change="parkOrgChange"
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
        label="关联停车场"
      >
        <span>{{ getByIpData?.parkingLot ||'-' }}</span>
      </a-form-item>
      <a-form-item
        label="关联车道"
      >
        <span>{{ getByIpData?.laneName||'-' }}</span>
      </a-form-item>
      <a-form-item
        label="出入口"
      >
        <span>{{ getByIpData?.carDirectStr||'-' }}</span>
      </a-form-item>
      <!-- <a-form-item
        v-for="(fieldConf,key) in formConfs[1]"
        :key="key"
        :name="fieldConf?.formItemName"
        :label="fieldConf?.formLabel"
      >
        <v-field
          v-model:value="fieldConf.value"
          disabled="disabled"
          :field-conf="fieldConf"
        />
      </a-form-item> -->
      <a-form-item
        label="设备位置"
      >
        <a-input
          v-model:value="modelRef.location"
          :disabled="type==='details'"
          placeholder="请输入设备位置"
          :maxlength="30"
        />
      </a-form-item>
      <a-form-item
        label="备注"
      >
        <a-input
          v-model:value="modelRef.remark"

          :disabled="disabled"
          placeholder="请输入备注"
        />
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
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, toRef,
} from 'vue';
import { message } from 'ant-design-vue';
// import Form, { FormItem } from 'ant-design-vue/lib/form';
import VehicleManagement, { VehicleInfo } from '~models/vehicle-management';
import { getFormCnfValue } from '~utils/tool';
import { DictionaryMap } from '~composition/useHashMapDic';
import { useEquipmentAdd, useFormFieldConfs } from '../composition/useForm';

const vehicleManagementInstance = new VehicleManagement();
export default defineComponent({
  name: 'OperateDialog',
  // components: {
  //   Form,
  //   FormItem,
  // },
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
    options: {
      type: Object as PropType< DictionaryMap<['DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']>>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],

  setup(props, { emit }) {
    const {
      type, visible, defaultData, options,
    } = toRefs(props);
    const isReady = ref(false);
    const getByIpData = ref();

    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return ['edit'].includes(type.value) ? isReady.value && visible.value : visible.value;
      },
    });
    const disabled = computed(() => type.value === 'details');
    const formRef = ref();
    const {
      modelRef,
      rulesRef,
    } = useEquipmentAdd();
    const confirm = () => {
      // console.log('----asdfasfd');
      // console.log(getFormCnfValue(formConfs.value[modelRef.deviceType as number]));
      formRef.value.validate().then(async () => {
        await vehicleManagementInstance.equipmentModify(modelRef);
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    const { formConfs, parkingLotChange, parkOrgChange } = useFormFieldConfs(modelRef, options);

    // 初始化
    const details = ref<VehicleInfo>();
    const fill = async () => {
      modelRef.id = defaultData.value.id;
      details.value = await vehicleManagementInstance.equipmentFindById(defaultData.value.id);
      if (details.value.ipAddress) {
        getByIpData.value = await vehicleManagementInstance.getCarLane(details.value.ipAddress);
      }
      isReady.value = true;
      Object.entries(modelRef).forEach(([key]) => {
        const formData = modelRef as any;
        if ((details.value as any)[key]) {
          formData[key] = (details.value as any)[key];
        }
      });
      parkOrgChange(modelRef.parkOrgId, false);
      parkingLotChange(modelRef.extension.parkingLotId, false);
    };

    // const init = async () => {
    //   if (type.value === 'edit') {
    //     fill();
    //   }
    // };
    // init();
    fill();
    return {
      getByIpData,
      currentVisible,
      details,
      confirm,
      modelRef,
      // validateInfos,
      parkOrgChange,
      formConfs,
      formRef,
      rulesRef,
      disabled,
    };
  },
});
</script>
