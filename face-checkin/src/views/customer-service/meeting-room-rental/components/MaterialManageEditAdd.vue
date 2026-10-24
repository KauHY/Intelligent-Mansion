<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-03-10 23:20:16
 * @LastEditors: jmh
 * @Description:
-->
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
        :rules="rulesRef"
        :label-col="{ span: 5 }"
      >
        <div>
          <div class="meeting-title">
            基本信息
          </div>
          <div>
            <a-form-item
              label="物资名称"
              name="goodsName"
            >
              <a-input
                v-model:value="modelRef.goodsName"
                :disabled="formDisabled"
                placeholder="请输入物资名称"
                :maxlength="100"
              />
            </a-form-item>
            <a-form-item
              label="所属园区"
              name="parkOrgId"
            >
              <v-select
                v-model:value="modelRef.parkOrgId"
                :disabled="type === 'detail'"
                placeholder="请选择所属园区"
                show-search
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
              label="物资类型"
              name="goodsType"
            >
              <v-select
                v-model:value="modelRef.goodsType"
                :disabled="formDisabled"
                placeholder="请选择物资类型"
              >
                <a-select-option
                  v-for="opt in goodsTypeOpts"
                  :key="opt.value"
                  :value="opt.value"
                >
                  {{ opt.label }}
                </a-select-option>
              </v-select>
            </a-form-item>
            <a-form-item
              label="物资价格（元）"
              name="unitPrice"
            >
              <a-input
                v-model:value="modelRef.unitPrice"
                :disabled="formDisabled"
                placeholder="请输入价格"
                style="width: 100%;"
              />
            </a-form-item>
            <a-form-item
              label="计量单位"
              name="unit"
            >
              <a-input
                v-model:value="modelRef.unit"
                :disabled="formDisabled"
                placeholder="请输入计量单位"
                :maxlength="20"
              />
            </a-form-item>
            <a-form-item
              label="物资图片"
              name="goodsImage"
            >
              <v-upload
                v-model:value="modelRef.goodsImage"
                :disabled="formDisabled"
                accept=".jpg,.bmp,.png,.gif"
                tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸800*800PX"
                :size="1"
              />
            </a-form-item>
            <a-form-item
              label="说明"
              name="remark"
            >
              <a-textarea
                v-model:value="modelRef.remark"
                :disabled="formDisabled"
                placeholder="请输入说明"
                :maxlength="200"
              />
            </a-form-item>
          </div>
        </div>
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
    <MaterialList
      v-if="materialFlag"
      ref="customer"
      v-model:show="materialFlag"
      :material-config="materialConfig"
      @getMaterialData="getMaterialData"
    />
  </v-modal>
</template>

<script lang="ts">
import {
  ref,
  reactive,
  computed,
  defineComponent,
  onMounted,
  PropType,
  toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import House, { HouseInfo } from '~models/house';
import Building, { FloorListItem } from '~models/building';
import CompanyMangement from '~models/company-mangement';
import MeetingRentalVo, { MeetingRentalListKind } from '~models/meeting-rental';
import { reserveTime, goodsTypeOpts } from '../config';
import { materialForm } from '../composition/useForm';
import MaterialList from './MaterialList.vue';

interface MaterialConfig {
  title?: string;
  parkOrgId?: string;
  checkedKeys?: string[];
}
export default defineComponent({
  components: { MaterialList },
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
    const { modelRef, rulesRef } = materialForm();
    const { meetingData, type } = toRefs(props.meetingRoomConfig);
    const reserveTimePeriod = ref(reserveTime);
    const materialFlag = ref<Boolean>(false);
    const materialConfig = ref<MaterialConfig>({});
    const {
      parkOpts,
      houseIdOptions,
      parkOrgIdChange,
      buildingOpts,
      buildChange,
      options,
      defaultOptions,
      companyOpts,
      materialDataList,
      getMaterialData,
      initOpts,
    } = useOptions(meetingData?.value, type.value, modelRef);
    const MeetingRentalVoReq = new MeetingRentalVo();
    const formDisabled = type.value === 'detail';
    const fill = () => {
      Object.entries(modelRef).forEach(([key]) => {
        if (
          (meetingData.value as any)[key]
          || (meetingData.value as any)[key] === 0
        ) {
          const formData = modelRef as any;
          if (key === 'meetingEquipment') {
            formData[key] = meetingData.value[key]
              .split(',')
              .map((item: any) => Number(item));
          } else {
            formData[key] = meetingData.value[key];
          }
        }
      });
      // 编辑或详情显示指定人
      if (meetingData.value?.employeeId) {
        const { employeeName, employeeId } = meetingData.value;
        defaultOptions.value = [
          {
            label: employeeName,
            value: employeeId,
          },
        ];
      }
    };
    const formRef = ref();
    const confirm = () => {
      console.log(modelRef, reserveTimePeriod);
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: MeetingRentalVoReq.materialCreateApi,
            edit: MeetingRentalVoReq.materialModifyApi,
          };
          await apiReq[type.value as 'add' | 'edit']({
            ...modelRef,
          });
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {
      if (type.value !== 'add') {
        fill();
        initOpts(modelRef.parkOrgId);
      }
    };
    const timeChange = async () => {};
    const showModal = async () => {
      if (type.value === 'detail') {
        return;
      }
      if (!modelRef.parkOrgId) {
        message.warn('请选择园区');
        return;
      }
      materialFlag.value = true;
      materialConfig.value = {
        title: '选择物资',
        checkedKeys: materialDataList.value.map((item: any) => item.id),
        parkOrgId: modelRef.parkOrgId,
      };
    };
    init();
    return {
      goodsTypeOpts,
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
      buildingOpts,
      buildChange,
      defaultOptions,
      companyOpts,
      reserveTimePeriod,
      timeChange,
      showModal,
      materialFlag,
      materialConfig,
      type,
      getMaterialData,
      materialDataList,
    };
  },
});
function useOptions(meetingData: any, type: string, modelRef: any) {
  const FloorInstance = new Building();
  const HouseInstance = new House();
  const parkOpts = ref<SelectOptions>([]);
  const buildingOpts = ref<SelectOptions>([]);
  const houseIdOptions = ref<SelectOptions>([]);
  const defaultOptions = ref<SelectOptions>([]);
  const companyOpts = ref<SelectOptions>([]);
  const materialDataList = ref<any>([]); // 选取的物资
  const options = useHashMapDics<
    [
      'MEETING_EQUIPMENT',
      'CUSTOMERINFO_STATUS',
      'INDUSTRY_FIELD',
      'COMPANY_TAXES_TYPE',
      'COMPANY_MANAGEMENT_STATUS'
    ]
  >([
    'MEETING_EQUIPMENT',
    'CUSTOMERINFO_STATUS',
    'INDUSTRY_FIELD',
    'COMPANY_TAXES_TYPE',
    'COMPANY_MANAGEMENT_STATUS',
  ]);
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
  })();
  // 园区切换
  const parkOrgIdChange = async (value: string) => {
    houseIdOptions.value = [];
    materialDataList.value = [];
    defaultOptions.value = [];
    if (value) {
      getCompany(value);
      getBuilding(value);
    }
  };
  // 楼栋切换时
  const buildChange = async (value: string) => {
    houseIdOptions.value = [];
    modelRef.houseId = '';
    if (value) {
      houseIdOptions.value = await HouseInstance.searchHouseOpts(value);
    }
  };
  const getCompany = async (parkOrgId: string) => {
    companyOpts.value = await new CompanyMangement().listCompanyDict(parkOrgId);
  };
  const getBuilding = async (parkOrgId: string) => {
    buildingOpts.value = await FloorInstance.listBuildingDict(parkOrgId);
  };
  const getMaterialData = async (data: any) => {
    console.log(data, 'getMaterialData');
    modelRef.material = data.value
      .map((item: any) => item.meetingName)
      .join(',');
    materialDataList.value = materialDataList.value.concat(data.value);
  };
  const initOpts = async (value: string) => {
    getCompany(value);
    getBuilding(value);
  };
  return {
    parkOpts,
    houseIdOptions,
    parkOrgIdChange,
    buildingOpts,
    buildChange,
    options,
    defaultOptions,
    companyOpts,
    getMaterialData,
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
