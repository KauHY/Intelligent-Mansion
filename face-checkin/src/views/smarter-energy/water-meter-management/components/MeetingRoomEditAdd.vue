<template>
  <v-modal
    class="meeting-room-add-edit common-dialog"
    :visible="true"
    :width="900"
    :title="meetingRoomConfig.title"
    @cancel="emit('update:show', false)"
  >
    <div>
      <a-form
        ref="formRef"
        :model="modelRef"
        :label-col="{span:6}"
        :width="980"
        :rules="rulesRef"
      >
        <a-card
          :title="meetingRoomConfig.type==='add'?'表计管理/新增水表':meetingRoomConfig.type==='edit'?'表计管理/编辑水表':'表计管理/水表详情'"
          :bordered="false"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="所属园区"
                name="parkOrgId"
              >
                <v-select
                  v-model:value="modelRef.parkOrgId"
                  placeholder="请选择关联园区"
                  :disabled="formDisabled|| meetingRoomConfig.type==='edit'"
                  @change="parkOrgIdChange"
                >
                  <a-select-option
                    v-for="opt in parkOpts"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="能耗类别"
                name="deviceType"
              >
                <a-input
                  disabled="true"
                  placeholder="水能耗"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="表计属性"
                name="effectProperty"
              >
                <v-select
                  v-model:value="modelRef.effectProperty"
                  :disabled="formDisabled"
                  placeholder="请选择表计属性"
                  @change="changEffectType"
                >
                  <a-select-option
                    v-for="opt in options.METER_EFFECT_PROPERTY"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="表计类别"
              >
                <span>水表</span>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="表类型"
                name="meterType"
              >
                <v-select
                  v-model:value="modelRef.meterType"
                  placeholder="请选择表类型"
                  :disabled="formDisabled|| meetingRoomConfig.type==='edit'"
                  @change="timeChange"
                >
                  <a-select-option
                    v-for="opt in options.METER_TYPE"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="表计名称"
                name="deviceName"
              >
                <a-input
                  v-model:value="modelRef.deviceName"
                  :disabled="formDisabled"
                  placeholder="请输入表计名称"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="最大量程"
                name="maxRange"
              >
                <a-input
                  v-model:value="modelRef.maxRange"
                  placeholder="最大量程"
                  :disabled="formDisabled"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                v-if="modelRef.meterType===2"
                label="设备ID"
                name="deviceId"
              >
                <a-input
                  v-model:value="modelRef.deviceId"
                  placeholder="请输入设备ID"
                  :maxlength="30"
                  :disabled="formDisabled || meetingRoomConfig.type==='edit'"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="安装位置"
                name="location"
              >
                <a-textarea
                  v-model:value="modelRef.location"
                  placeholder="请输入安装位置"
                  :maxlength="30"
                  :disabled="formDisabled"
                />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="备注"
                name="remark"
              >
                <a-textarea
                  v-model:value="modelRef.remark"
                  placeholder="请输入备注"
                  :maxlength="200"
                  :disabled="formDisabled"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card
          title="计量范围"
          :bordered="false"
        >
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="所属楼栋"
                name="buildingId"
              >
                <v-select
                  v-model:value="modelRef.buildingId"
                  placeholder="所属楼栋"
                  :disabled="formDisabled || !modelRef.effectProperty"
                  @change="buildChange"
                >
                  <a-select-option
                    v-for="opt in buildingOpts"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="所属楼层"
                name="floors"
              >
                <v-select
                  v-model:value="modelRef.floors"
                  placeholder="所属楼层"
                  :disabled="formDisabled || !modelRef.effectProperty"
                  :mode="modelRef.effectProperty===1?'':'multiple'"
                  @change="floorChange"
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
                label="所属房屋"
                name="houseIds"
              >
                <v-select
                  v-model:value="modelRef.houseIds"
                  placeholder="所属房屋"
                  :mode="modelRef.effectProperty===1?'':'multiple'"
                  :disabled="formDisabled || !modelRef.effectProperty"
                >
                  <a-select-option
                    v-for="opt in houseIdOptions"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                label="初始读数"
                name="initialReading"
              >
                <a-input
                  v-model:value="modelRef.initialReading"
                  :disabled="formDisabled"
                  placeholder="请输入初始读数"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                label="初始时间"
                name="initialTime"
              >
                <a-date-picker
                  v-model:value="modelRef.initialTime"
                  :disabled="formDisabled"
                  placeholder="请选择初始时间"
                  show-time
                  value-format="YYYY-MM-DD HH:mm:ss"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
      </a-form>
    </div>
    <template #footer>
      <a-button @click="emit('update:show', false)">
        {{ formDisabled ? "关闭" : "取消" }}
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
  ref,
  defineComponent,
  toRefs,
} from 'vue';
import { message } from 'ant-design-vue';
import { useHashMapDics } from '~composition/useHashMapDic';
import Park from '~models/park';
import House from '~models/house';
import Building from '~models/building';
import Floor from '~models/floor';
import SmarterMeter from '~models/smarter-energy';
import { rentalForm } from '../composition/useForm';

const userInstance = new SmarterMeter();
export default defineComponent({
  components: { },
  props: {
    meetingRoomConfig: {
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
    const { modelRef, rulesRef } = rentalForm();
    const { meetingData, type } = toRefs(props.meetingRoomConfig);
    const materialFlag = ref<Boolean>(false);
    const {
      floorChange,
      parkOpts,
      houseIdOptions,
      floorOptions,
      parkOrgIdChange,
      buildingOpts,
      buildChange,
      options,
      defaultOptions,
      companyOpts,
      materialDataList,
      initOpts,
    } = useOptions(meetingData?.value, type.value, modelRef);
    const formDisabled = type.value === 'detail';
    const fill = () => {
      const meetingRentDTO = meetingData.value;
      Object.entries(modelRef).forEach(([key]) => {
        if (
          (meetingRentDTO as any)[key]
          || (meetingRentDTO as any)[key] === 0
        ) {
          const formData = modelRef as any;
          formData[key] = meetingRentDTO[key];
          formData[key] = meetingRentDTO[key];
          if (formData.meterType === 1) {
            rulesRef.deviceId[0].required = false;
          }
          if (formData.effectProperty === 1) {
            rulesRef.houseIds[0].required = true;
          }
        }
      });
    };
    const formRef = ref();
    const changEffectType = async (value:number | null) => {
      modelRef.buildingId = '';
      modelRef.floors = null;
      modelRef.houseIds = null;
    };
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: userInstance.createMeters,
            edit: userInstance.modifyMeter,
          };

          await apiReq[type.value as 'add' | 'edit']({
            ...modelRef,
            maxRange: Number(modelRef.maxRange),
            effectType: null,
            houseIds: typeof (modelRef.houseIds) === 'object' ? modelRef.houseIds : [modelRef.houseIds],
            floors: typeof (modelRef.floors) === 'object' ? modelRef.floors : [modelRef.floors],
            initialReading: Number(modelRef.initialReading),
          });
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      if (type.value !== 'add') {
        fill();
        initOpts(modelRef.parkOrgId, modelRef.buildingId, modelRef.floors);
      }
    };
    const timeChange = async (value: any) => {
      modelRef.deviceId = '';
    };

    init();
    return {
      changEffectType,
      floorChange,
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      parkOpts,
      parkOrgIdChange,
      formDisabled,
      houseIdOptions,
      floorOptions,
      buildingOpts,
      buildChange,
      defaultOptions,
      companyOpts,
      timeChange,
      type,
      materialDataList,
    };
  },
});
function useOptions(meetingData: any, type: string, modelRef: any) {
  const floorInstance = new Floor();
  const floorOptions = ref<number>(0);
  const FloorInstance = new Building();
  const houseInstance = new House();
  const parkOpts = ref<SelectOptions>([]);
  const buildingOpts = ref<SelectOptions>([]);
  const houseIdOptions = ref<SelectOptions>([]);
  const defaultOptions = ref<SelectOptions>([]);
  const companyOpts = ref<SelectOptions>([]);
  const materialDataList = ref<any>([]); // 选取的物资
  const options = useHashMapDics<
    [
      'METER_TYPE_READING_METHOD',
      'METER_EFFECT_PROPERTY',
      'METER_STATUS',
      'METER_TYPE'
    ]
  >([
    'METER_TYPE_READING_METHOD',
    'METER_EFFECT_PROPERTY',
    'METER_STATUS',
    'METER_TYPE',
  ]);
  (async () => {

  })();
  // 园区切换
  const parkOrgIdChange = async (value: string) => {
    houseIdOptions.value = [];
    floorOptions.value = 0;
    modelRef.buildingId = '';
    modelRef.houseIds = [];
    modelRef.floors = [];
    if (value) {
      getBuilding(value);
    }
  };
  // 楼栋切换时
  const buildChange = async (value: string) => {
    houseIdOptions.value = [];
    modelRef.floors = [];
    modelRef.houseIds = [];
    if (value) {
      floorOptions.value = await floorInstance.searchFloorOpts(value);
    }
  };
    // 楼层切换时
  const floorChange = async (value: string) => {
    modelRef.houseIds = [];
    if (value) {
      houseIdOptions.value = await houseInstance.searchHouseOpts(modelRef.buildingId, value);
    }
  };

  const getBuilding = async (parkOrgId: string) => {
    buildingOpts.value = await FloorInstance.listBuildingDict(parkOrgId);
  };
  const initOpts = async (parkOrgId: any, buildingId:any, floor:any) => {
    getBuilding(parkOrgId);
    floorOptions.value = await floorInstance.searchFloorOpts(buildingId);
    houseIdOptions.value = await houseInstance.searchHouseOpts(buildingId, floor);
  };
  return {
    floorChange,
    floorOptions,
    parkOpts,
    houseIdOptions,
    parkOrgIdChange,
    buildingOpts,
    buildChange,
    options,
    defaultOptions,
    companyOpts,
    materialDataList,
    initOpts,
  };
}
</script>
<style scoped lang="scss">
.meeting-room-add-edit {
    .meeting-title {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
        margin-left: 20px;
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
