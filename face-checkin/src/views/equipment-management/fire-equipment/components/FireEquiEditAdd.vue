<template>
  <v-modal
    class="customer-add-edit"
    :visible="true"
    :width="900"
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
        <div>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="所属园区"
                name="parkOrgId"
              >
                <v-select
                  v-model:value="modelRef.parkOrgId"
                  :disabled="formDisabled"
                  placeholder="请选择所属园区"
                  show-search
                  @change="parkChange"
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
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="所属系统"
                name="systemType"
              >
                <v-select
                  v-model:value="modelRef.systemType"
                  :disabled="formDisabled"
                  placeholder="请选择所属系统"
                >
                  <a-select-option
                    v-for="opt in options.SYSTEM_TYPE"
                    :key="opt.value"
                    :value="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="设备名称"
                name="deviceName"
              >
                <a-input
                  v-model:value="modelRef.deviceName"
                  :maxlength="30"
                  :disabled="formDisabled"
                  placeholder="请输入设备名称"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="设备SN"
                name="serialNumber"
              >
                <a-input
                  v-model:value="modelRef.serialNumber"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入设备SN"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="IP地址"
                name="ip"
              >
                <a-input
                  v-model:value="modelRef.ip"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入ip地址"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="端口号"
                name="port"
              >
                <a-input
                  v-model:value="modelRef.port"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入端口号"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="通讯端口"
                name="communicationPort"
              >
                <a-input
                  v-model:value="modelRef.communicationPort"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入通讯端口"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="设备ID"
                name="deviceId"
              >
                <a-input
                  v-model:value="modelRef.deviceId"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入设备ID"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="安装楼栋"
                name="buildingId"
              >
                <v-select
                  v-model:value="modelRef.buildingId"
                  :disabled="formDisabled"
                  placeholder="请选择安装楼栋"
                  show-search
                  @change="buildingChange"
                >
                  <a-select-option
                    v-for="opt in buildingIdOptions"
                    :key="opt.value"
                    :value="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="安装楼层"
                name="floor"
              >
                <v-select
                  v-model:value="modelRef.floor"
                  :disabled="formDisabled"
                  show-search
                  placeholder="请选择安装楼层"
                  @change="houseChange"
                >
                  <a-select-option
                    v-for="k in floorOptions"
                    :key="k"
                  >
                    {{ k }}楼
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="安装房屋"
                name="houseId"
              >
                <v-select
                  v-model:value="modelRef.houseId"
                  :disabled="formDisabled"
                  show-search
                  placeholder="请选择安装房屋"
                >
                  <a-select-option
                    v-for="opt in houseIdOptions"
                    :key="opt.value"
                    :value="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="安装位置"
                name="installationLocation"
              >
                <a-textarea
                  v-model:value="modelRef.installationLocation"
                  :disabled="formDisabled"
                  :maxlength="30"
                  placeholder="请输入安装位置"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </div>
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
import { useHashMapDics } from '~composition/useHashMapDic';
import Park from '~models/park';
import CustomerVo from '~models/equipment-management';
import Building from '~models/building';
import House from '~models/house';
import Floor from '~models/floor';
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
    const floorOptions = ref<number>(0);
    const buildingInstance = new Building();
    const houseInstance = new House();
    const floorInstance = new Floor();
    const { modelRef, rulesRef } = customerForm();
    const { customerData, type } = toRefs(props.customerConfig);
    const {
      parkOpts, options, parkOrgIdChange, tagOpts, defaultOptions, getTagOpts,
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
      /**
     * @description: 选择园区
     * @param {*} value
     * @return {*}
     * @author: GUOCHAO82
     */
    const parkChange = async (value:string) => {
      if (!value) {
        buildingIdOptions.value = [];
        return;
      }
      buildingIdOptions.value = await buildingInstance.listBuildingDict(value as string);
      modelRef.floor = null;
      modelRef.buildingId = null;
      modelRef.houseId = null;
    };

    // 楼栋切换时
    const buildingChange = async (value:string) => {
      if (!value) {
        houseIdOptions.value = [];
        return;
      }
      floorOptions.value = await floorInstance.searchFloorOpts(value);
      modelRef.floor = null;
      modelRef.houseId = null;
    };

    // 楼层切换时
    const houseChange = async (value:string) => {
      if (!value) {
        houseIdOptions.value = [];
        return;
      }
      houseIdOptions.value = await houseInstance.searchHouseOpts(modelRef.buildingId, value);
      modelRef.houseId = null;
    };
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: CustomerVoReq.createFireControlDevice,
            edit: CustomerVoReq.modifyFireControlDevice,
          };
          await apiReq[type.value as 'add' | 'edit']({ ...modelRef });
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const initOpts = async (parkOrgId: any, buildingId:any, floor:any) => {
      buildingIdOptions.value = await buildingInstance.listBuildingDict(parkOrgId as string);
      floorOptions.value = await floorInstance.searchFloorOpts(buildingId);
      houseIdOptions.value = await houseInstance.searchHouseOpts(buildingId, floor);
    };
    const init = async () => {
      parkOpts.value = await parkInstance.searchParkListName();
      if (type.value !== 'add') {
        fill();
        initOpts(modelRef.parkOrgId, modelRef.buildingId, modelRef.floor);
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
      parkChange,
      buildingChange,
      houseChange,
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
  const getTagOpts = async (value:string) => {
  };
  (async () => {
  })();
  const parkOrgIdChange = async (value: string) => {
    if (customerData) {
      defaultOptions.value = [];
    }
    getTagOpts(value);
  };
  return {
    parkOpts,
    options,
    tagOpts,
    parkOrgIdChange,
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
