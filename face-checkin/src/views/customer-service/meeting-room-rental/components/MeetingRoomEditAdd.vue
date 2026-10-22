<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-06 15:38:39
 * @LastEditors: Xucaizhi3
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
              label="会议室名称"
              name="meetingName"
            >
              <a-input
                v-model:value="modelRef.meetingName"
                :disabled="formDisabled"
                placeholder="请输入会议室名称"
                :maxlength="30"
              />
            </a-form-item>
            <a-form-item
              label="所属园区"
              name="parkOrgId"
            >
              <v-select
                v-model:value="modelRef.parkOrgId"
                :disabled="type !== 'add'"
                placeholder="请选择所属园区"
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
              label="所属楼栋单元"
              name="buildingId"
            >
              <v-select
                v-model:value="modelRef.buildingId"
                :disabled="formDisabled"
                placeholder="请选择所属楼栋单元"
                @change="buildChange"
              >
                <a-select-option
                  v-for="opt in buildingOpts"
                  :key="opt.value"
                  :value="opt.value"
                >
                  {{ opt.label }}
                </a-select-option>
              </v-select>
            </a-form-item>
            <a-form-item
              label="楼层房号"
              name="houseId"
            >
              <v-select
                v-model:value="modelRef.houseId"
                :disabled="formDisabled"
                placeholder="请选择所属楼层房号"
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
            <a-form-item
              label="会议室设备"
              name="meetingEquipment"
            >
              <v-select
                v-model:value="modelRef.meetingEquipment"
                :disabled="formDisabled"
                placeholder="请选择会议室设备"
                mode="multiple"
              >
                <a-select-option
                  v-for="opt in options.MEETING_DEVICE_TYPE"
                  :key="opt.value"
                  :value="opt.value"
                >
                  {{ opt.label }}
                </a-select-option>
              </v-select>
            </a-form-item>
            <a-form-item
              label="容纳人数"
              name="capacity"
            >
              <a-input-number
                v-model:value="modelRef.capacity"
                :disabled="formDisabled"
                placeholder="请输入容纳人数"
                style="width: 100%;"
                :min="1"
                :max="99999"
                :maxlength="5"
              />
            </a-form-item>
            <a-form-item
              label="咨询电话"
              name="telephone"
            >
              <a-input
                v-model:value="modelRef.telephone"
                :disabled="formDisabled"
                placeholder="请输入电话号码"
                :maxlength="20"
              />
            </a-form-item>
            <a-form-item
              label="封面图片"
              name="coverImage"
            >
              <v-upload
                v-model:value="modelRef.coverImage"
                accept=".jpg,.bmp,.png,.gif"
                :disabled="formDisabled"
              />
            </a-form-item>
            <a-form-item
              label="详情图片"
              tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸1035*471PX"
              name="detailImage"
            >
              <v-upload
                v-model:value="modelRef.detailImage"
                :count="5"
                accept=".jpg,.bmp,.png,.gif"
                tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸1125*844PX"
                :disabled="formDisabled"
              />
            </a-form-item>
            <a-form-item
              label="会议室介绍"
              name="remark"
            >
              <a-textarea
                v-model:value="modelRef.remark"
                :disabled="formDisabled"
                placeholder="请输入会议室介绍"
                :maxlength="1000"
              />
            </a-form-item>
            <a-form-item
              label="预定须知"
              name="reserveInstruct"
            >
              <a-textarea
                v-model:value="modelRef.reserveInstruct"
                :disabled="formDisabled"
                placeholder="请输入预定须知"
                :maxlength="1000"
              />
            </a-form-item>
          </div>
        </div>
        <div>
          <div class="meeting-title">
            预定设置
          </div>
          <div>
            <a-form-item
              label="可预定时段(按半天)"
              name="timeArr"
            >
              <a-checkbox-group
                v-model:value="modelRef.timeArr"
                :disabled="formDisabled"
              >
                <div
                  v-for="(item, index) in reserveTimePeriod"
                  :key="index"
                  style="margin-bottom: 10px;"
                >
                  <a-checkbox :value="item.timeType">
                    {{ timeTypeNameObj[item.timeType] }}
                  </a-checkbox>
                  <a-time-picker
                    v-model:value="item.startTime"
                    placeholder="开始时间"
                    format="HH:mm"
                    value-format="HH:mm"
                    :allow-clear="false"
                    :disabled="
                      modelRef.timeArr.indexOf(item.timeType) === -1 ||
                        formDisabled
                    "
                  />至
                  <a-time-picker
                    v-model:value="item.endTime"
                    placeholder="结束时间"
                    format="HH:mm"
                    value-format="HH:mm"
                    :allow-clear="false"
                    :disabled="
                      modelRef.timeArr.indexOf(item.timeType) === -1 ||
                        formDisabled
                    "
                  />
                </div>
              </a-checkbox-group>
            </a-form-item>
            <a-form-item
              label="使用权限设置"
              name="usePermission"
            >
              <a-radio-group
                v-model:value="modelRef.usePermission"
                :disabled="formDisabled"
              >
                <a-radio :value="0">
                  不限
                </a-radio>
                <a-radio :value="1">
                  指定公司或人员
                </a-radio>
              </a-radio-group>
            </a-form-item>
            <a-form-item
              v-if="modelRef.usePermission === 1"
              label=" "
              :colon="false"
            >
              <v-select
                v-model:value="modelRef.companyIds"
                placeholder="请选择公司"
                mode="multiple"
                :disabled="formDisabled"
              >
                <a-select-option
                  v-for="opt in companyOpts"
                  :key="opt.value"
                  :value="opt.value"
                >
                  {{ opt.label }}
                </a-select-option>
              </v-select>
              <div style="margin: 15px 0;" />
              <v-select-employee
                v-model:value="modelRef.employeeIds"
                :default-select-opts="defaultOptions"
                placeholder="请选择人员"
                :disabled="formDisabled"
                title="选择人员"
                :company-type="0"
                :park-org-ids="modelRef.parkOrgId"
              />
            </a-form-item>
          </div>
        </div>
        <div>
          <div class="meeting-title">
            收费设置
          </div>
          <div>
            <a-form-item
              label="收费设置"
              name="charge"
            >
              <a-radio-group
                v-model:value="modelRef.charge"
                :disabled="formDisabled"
                @change="clearPrice"
              >
                <a-radio :value="1">
                  收费
                </a-radio>
                <a-radio :value="0">
                  免费
                </a-radio>
              </a-radio-group>
            </a-form-item>
            <div v-if="modelRef.charge === 1">
              <a-form-item
                label="租赁价格(元/半天)"
                name="rentPrice"
              >
                <a-input-number
                  v-model:value="modelRef.rentPrice"
                  :disabled="formDisabled"
                  placeholder="请输入租赁价格(元/半天)"
                  :min="0"
                  :max="999999999.99"
                  style="width: 100%;"
                />
              </a-form-item>
              <a-form-item label="支付模式">
                审批后线下支付
              </a-form-item>
            </div>
          </div>
        </div>
        <div>
          <div class="meeting-title">
            物资设置
          </div>
          <div>
            <a-form-item
              label="选择物资"
              name="goodsId"
            >
              <a-input
                v-model:value="modelRef.material"
                disabled
              >
                <template #addonAfter>
                  <span
                    style="cursor: pointer;"
                    @click="showModal"
                  > 选择 </span>
                </template>
              </a-input>
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
import { mod } from 'mathjs';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import House, { HouseInfo } from '~models/house';
import Building, { FloorListItem } from '~models/building';
import CompanyMangement from '~models/company-mangement';
import MeetingRentalVo, { MeetingRentalListKind, EmComKind, MeetingRentalInfoKind } from '~models/meeting-rental';
import { reserveTime, timeTypeName } from '../config';
import { rentalForm } from '../composition/useForm';
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
      type: Object as PropType<{meetingData:Partial<MeetingRentalInfoKind>, title:string, type:string}>,
      default: () => ({}),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const { modelRef, rulesRef } = rentalForm();
    const { meetingData, type } = toRefs(props.meetingRoomConfig);
    const reserveTimePeriod = ref(reserveTime);
    const timeTypeNameObj = ref(timeTypeName);
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
      if (!meetingData.value) return;
      const meetingRentDTO = meetingData.value.meetingRentDTO;
      const listObj = meetingData.value;
      // console.log(listObj?.timeList);
      Object.entries(modelRef).forEach(([key]) => {
        if (
          (meetingRentDTO as any)[key]
          || (meetingRentDTO as any)[key] === 0
        ) {
          const formData = modelRef as any;
          if (key === 'meetingEquipment') {
            formData[key] = meetingRentDTO[key]
              .split(',')
              .map((item: any) => Number(item));
          } else if (key === 'meetingEquipment') {
            formData[key] = meetingRentDTO[key]
              .split(',')
              .map((item: any) => Number(item));
          } else {
            formData[key] = meetingRentDTO[key];
          }
        }
      });
      if (listObj.companyList?.length) {
        modelRef.companyIds = listObj.companyList.map((item:any) => item.companyId);
      }
      if (listObj.goodsList?.length) {
        materialDataList.value = listObj.goodsList;
        modelRef.material = listObj.goodsList
          .map((item: any) => item.label)
          .join(',');
      }
      if (listObj.timeList?.length) {
        modelRef.timeArr = listObj.timeList.map((item:any) => item.timeType);
        reserveTimePeriod.value = reserveTimePeriod.value.map((item) => {
          listObj.timeList?.forEach((sub:any) => {
            if (item.timeType === sub.timeType) {
              item.startTime = sub.startTime;
              item.endTime = sub.endTime;
            }
          });
          return item;
        });
      }
      if (listObj.employeeList?.length) {
        modelRef.employeeIds = listObj.employeeList.map((item) => item.employeeId);
        defaultOptions.value = listObj.employeeList.map((item) => ({ label: item.employeeName || '', value: item.employeeId || '' }));
      }
    };
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        if (modelRef.usePermission === 1) {
          if (modelRef.companyIds.length === 0 && modelRef.employeeIds.length === 0) {
            message.warning('请选择公司或人员！');
            return;
          }
        }
        try {
          const timeReqDTOList = reserveTimePeriod.value.filter((item:any) => modelRef.timeArr.indexOf(item.timeType) !== -1);
          const apiReq = {
            add: MeetingRentalVoReq.meetingRentalCreateApi,
            edit: MeetingRentalVoReq.meetingRentalModifyApi,
          };
          const employeeIdList = ref<Array<EmComKind>>([]);
          const companyIdList = ref<Array<EmComKind>>([]);
          Object.keys(modelRef).forEach((key: any) => {
            if (key === 'companyIds') {
              companyIdList.value = modelRef.companyIds?.map((item) => ({
                companyId: item,
                relationType: 1,
              }));
            }
            if (key === 'employeeIds') {
              employeeIdList.value = modelRef.employeeIds?.map((item) => ({
                employeeId: item,
                relationType: 2,
              }));
            }
          });
          await apiReq[type.value as 'add' | 'edit']({
            ...modelRef,
            meetingEquipment: modelRef.meetingEquipment.toString(),
            timeReqDTOList,
            companyIds: companyIdList.value,
            employeeIds: employeeIdList.value,
            goodsId: materialDataList.value.map((item:any) => item.value).toString(),
          });
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const clearPrice = async () => {
      modelRef.rentPrice = null;
    };
    const init = async () => {
      if (type.value !== 'add') {
        fill();
        initOpts(modelRef.parkOrgId, modelRef.buildingId);
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
        checkedKeys: materialDataList.value.map((item: any) => item.value),
        parkOrgId: modelRef.parkOrgId,
      };
    };
    init();
    return {
      clearPrice,
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
      timeTypeNameObj,
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
      'MEETING_DEVICE_TYPE',
      'CUSTOMERINFO_STATUS',
      'INDUSTRY_FIELD',
      'COMPANY_TAXES_TYPE',
      'COMPANY_MANAGEMENT_STATUS'
    ]
  >([
    'MEETING_DEVICE_TYPE',
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
    modelRef.buildingId = '';
    modelRef.houseId = '';
    modelRef.material = '';
    modelRef.companyIds = [];
    modelRef.employeeIds = [];
    modelRef.employeeIdName = '';
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
    materialDataList.value = [];
    materialDataList.value = materialDataList.value.concat(data.value).map((item:any) => ({
      label: item.goodsName || item.label,
      value: item.id || item.value,
    }));
    modelRef.material = materialDataList.value.map((item:any) => item.goodsName || item.label).toString();
  };
  const initOpts = async (parkOrgId: string, buildingId:string) => {
    getCompany(parkOrgId);
    getBuilding(parkOrgId);
    houseIdOptions.value = await HouseInstance.searchHouseOpts(buildingId);
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
