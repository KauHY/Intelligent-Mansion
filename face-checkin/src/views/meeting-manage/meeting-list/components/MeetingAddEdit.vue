<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-07-05 13:38:53
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <v-modal
    class="meeting-add-edit"
    :visible="true"
    :width="730"
    :title="meetingConfig.title"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div>
      <a-form
        ref="formRef"
        :model="modelRef"
        :rules="rulesRef"
        :label-col="{ span: 5 }"
        :colon="false"
      >
        <a-form-item
          label="所属园区"
          name="parkOrgId"
        >
          <v-select
            v-model:value="modelRef.parkOrgId"
            placeholder="请选择所属园区"
            :disabled="type !== 'add'"
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
            placeholder="请选择所属楼栋单元"
            @change="buildChange($event, modelRef.buildingId)"
          >
            <a-select-option
              v-for="opt in buildingOpts"
              :key="opt.value"
              :value="opt.value"
              @click="buildClick(opt)"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="所在楼层"
          name="floorId"
        >
          <v-select
            v-model:value="modelRef.floorId"
            placeholder="请选择所在楼层"
            @change="floorChange($event, modelRef.buildingId)"
          >
            <a-select-option
              v-for="opt in floorIdOptions"
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
            placeholder="请选择楼层房号"
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
          label="会议室名称"
          name="meetingName"
        >
          <a-input
            v-model:value="modelRef.meetingName"
            placeholder="请输入会议室名称"
            :maxlength="30"
          />
        </a-form-item>
        <a-form-item
          label="会议室类型"
          name="meetingType"
        >
          <v-select
            v-model:value="modelRef.meetingType"
            placeholder="请选择会议室类型"
          >
            <a-select-option
              v-for="opt in options.MEETING_ROOM_TYPE"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="开放时间"
          required
        >
          <div class="flex">
            <a-form-item
              class="wd308"
              name="openTimeStart"
            >
              <a-time-picker
                v-model:value="modelRef.openTimeStart"
                format="HH:mm"
                disabled
                value-format="HH:mm"
                placeholder="开始时间"
              />
            </a-form-item>
            <div class="ml5 mr5">
              __
            </div>
            <a-form-item
              class="wd308"
              name="openTimeEnd"
            >
              <a-time-picker
                v-model:value="modelRef.openTimeEnd"
                format="HH:mm"
                disabled
                value-format="HH:mm"
                placeholder="结束时间"
              />
            </a-form-item>
          </div>
        </a-form-item>
        <!-- <a-form-item
          label="开放时间"
          :name="['openTimeStart','openTimeEnd']"
        >
          <v-range-time
            v-model:startTime="modelRef.openTimeStart"
            v-model:endTime="modelRef.openTimeEnd"
          />
        </a-form-item> -->
        <a-form-item
          label="容纳人数"
          name="capacity"
        >
          <a-input-number
            v-model:value="modelRef.capacity"
            placeholder="请输入容纳人数"
            style="width: 100%;"
            :min="1"
          />
        </a-form-item>
        <a-form-item
          label="会议室设备"
          name="meetingEquipment"
        >
          <v-select
            v-model:value="modelRef.meetingEquipment"
            placeholder="请选择会议室设备"
            mode="multiple"
          >
            <a-select-option
              v-for="opt in options.MEETING_EQUIPMENT"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="预定是否需审核"
          name="needReview"
        >
          <v-select
            v-model:value="modelRef.needReview"
            placeholder="请选择预定是否需审核"
          >
            <a-select-option
              v-for="opt in options.YES_OR_NO"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          v-if="modelRef.needReview === 1"
          label="审核人员"
          name="reviewerListName"
        >
          <!-- <employeeSelect
            v-model:value="modelRef.reviewerList"
            mode="multiple"
            :default-options="defaultReviewerOpts"
          /> -->
          <v-select-employee
            v-model:value="modelRef.reviewerList"
            :default-select-opts="defaultReviewerOpts"
            placeholder="请选择审核人员"
            title="选择人员"
            job-codes="10"
            :company-type="0"
            :park-org-ids="modelRef.parkOrgId"
          />
        </a-form-item>
        <a-form-item
          required
          label="使用权限设置"
        >
          <v-select
            v-model:value="modelRef.companyIds"
            placeholder="请选择公司"
            mode="multiple"
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
          <a-form-item-rest>
            <v-select-employee
              v-model:value="modelRef.employeeIds"
              :default-select-opts="defaultOptions"
              placeholder="请选择人员"
              title="选择人员"
              :park-org-ids="modelRef.parkOrgId"
            />
          </a-form-item-rest>
          <div class="title">
            可选择指定公司或指定人员，公司与人员的选择相互独立，互不影响
          </div>
        </a-form-item>
        <a-form-item
          name="screenDeviceId"
          label="关联会议室信息屏"
        >
          <v-select
            v-model:value="modelRef.screenDeviceId"
            placeholder="请选择会议室信息屏"
          >
            <a-select-option
              v-for="opt in sceenOpts"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="会议室介绍"
          name="remark"
        >
          <a-textarea
            v-model:value="modelRef.remark"
            placeholder="请输入会议室介绍"
            :maxlength="100"
          />
        </a-form-item>
      </a-form>
    </div>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, defineComponent, toRefs,
} from 'vue';
import { useHashMapDics } from '~composition/useHashMapDic';
import Park from '~models/park';
import House from '~models/house';
import Building from '~models/building';
import MeetingVo, { Meeting } from '~models/meeting-manage';
import CompanyMangement from '~models/company-mangement';
import CustomerVo from '~models/smarter-apartment';
import { message } from 'ant-design-vue';
import { meetingForm } from '../composition/useForm';
import { relationTypeOpts } from '../config';

export default defineComponent({
  components: {},
  props: {
    meetingConfig: {
      type: Object,
      default: () => ({
        title: '',
        type: '',
        meetData: null,
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const { modelRef, rulesRef } = meetingForm();
    const { meetData, type } = toRefs(props.meetingConfig);
    const MeetingVoReq = new MeetingVo();
    const options = useHashMapDics<['YES_OR_NO', 'MEETING_EQUIPMENT', 'MEETING_ROOM_TYPE']>([
      'YES_OR_NO',
      'MEETING_EQUIPMENT',
      'MEETING_ROOM_TYPE',
    ]);
    const {
      parkOpts, companyOpts, sceenOpts, houseIdOptions, parkOrgIdChange, floorChange, buildingOpts, floorIdOptions, buildClick, buildChange,
    } = useOptions(meetData.value, type.value, modelRef);
    const formRef = ref();
    const defaultPermissionOpts = ref<SelectOptions>([]);
    let defaultReviewerOpts = ref<SelectOptions>([]);
    const defaultOptions = ref<SelectOptions>([]);
    const confirm = () => {
      console.log(modelRef, 'modelRef----------------------');
      formRef.value.validate().then(async () => {
        if (modelRef.companyIds.length === 0 && modelRef.employeeIds.length === 0) {
          message.warn('请选择使用权限设置');
          return;
        }
        try {
          const apiReq = {
            add: MeetingVoReq.meetingCreateApi,
            edit: MeetingVoReq.meetingModifyApi,
          };
          const employeeIdList = ref<Array<null>>([]);
          const companyIdList = ref<Array<null>>([]);
          Object.keys(modelRef).forEach((key: any) => {
            if (key === 'companyIds') {
              companyIdList.value = modelRef.companyIds?.map((item: any) => ({
                companyId: item,
                relationType: 1,
              }));
            }
            if (key === 'employeeIds') {
              employeeIdList.value = modelRef.employeeIds?.map((item: any) => ({
                employeeId: item,
                relationType: 2,
              }));
            }
          });
          await apiReq[type.value as 'add' | 'edit']({
            ...modelRef,
            meetingEquipment: modelRef.meetingEquipment.toString(),
            companyIds: companyIdList.value,
            employeeIds: employeeIdList.value,
          });
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const fill = async () => {
      // console.log(meetData, 'meetData');
      // if (!meetData.value) return;
      const { companyList, employeeList } = meetData.value;
      Object.entries(modelRef).forEach(([key]) => {
        if ((meetData.value as any)[key]) {
          const formData = modelRef as any;
          if (key === 'meetingEquipment') {
            formData[key] = meetData.value[key].split(',').map((item:any) => Number(item));
          } else {
            formData[key] = meetData.value[key];
          }
        }
      });
      if (companyList.length > 0) {
        modelRef.companyIds = companyList.map((item:any) => item.companyId);
      }
      if (employeeList.length > 0) {
        modelRef.employeeIds = employeeList.map((item:any) => item.employeeId);
        defaultOptions.value = employeeList.map((item:any) => ({ label: item.employeeName, value: item.employeeId }));
      }
      if (meetData.value.reviewerList?.length > 0) {
        modelRef.reviewerList = meetData.value.reviewerList.map((item:any) => item.employeeId);
        defaultReviewerOpts = meetData.value.reviewerList.map((item:any) => ({ label: item.employeeName, value: item.employeeId }));
      }
      buildingOpts.value = await new Building().listBuildingDict(modelRef.parkOrgId);
      const floorNum = buildingOpts.value.find((item:any) => modelRef.buildingId === item.value)?.extraInfo?.totalFloor || 0;
      buildClick(floorNum);
      sceenOpts.value = await new CustomerVo().listDropDownDevices(7, modelRef.parkOrgId);
      companyOpts.value = await new CompanyMangement().listCompanyDict(modelRef.parkOrgId);
      houseIdOptions.value = await new House().searchHouseOpts(`${modelRef.buildingId}/${modelRef.floorId}`);
    };
    const init = async () => {
      modelRef.needReview = 0;
      modelRef.openTimeStart = '08:00';
      modelRef.openTimeEnd = '20:00';
      if (type.value !== 'add') {
        fill();
      }
    };
    init();
    return {
      type,
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      defaultPermissionOpts,
      defaultOptions,
      defaultReviewerOpts,
      relationTypeOpts,
      parkOpts,
      companyOpts,
      sceenOpts,
      buildingOpts,
      floorIdOptions,
      houseIdOptions,
      parkOrgIdChange,
      floorChange,
      buildClick,
      buildChange,
    };
  },
});

function useOptions(meetData:Meeting, type: string, modelRef:any) {
  const FloorInstance = new Building();
  const HouseInstance = new House();
  const parkOpts = ref<SelectOptions>([]);
  const buildingOpts = ref<SelectOptions>([]);
  const floorIdOptions = ref<SelectOptions>([]);
  const houseIdOptions = ref<SelectOptions>([]);
  const companyOpts = ref<SelectOptions>([]);
  const sceenOpts = ref<SelectOptions>([]);
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
  })();
  // 园区切换
  const parkOrgIdChange = async (value: string) => {
    buildingOpts.value = [];
    houseIdOptions.value = [];
    floorIdOptions.value = [];
    modelRef.buildingId = '';
    modelRef.floorId = '';
    modelRef.houseId = '';
    modelRef.screenDeviceId = '';
    buildingOpts.value = await FloorInstance.listBuildingDict(value);
    getCompany(value);
    getSceenOpts(value);
    if (type !== 'add') {
      const floorNum = buildingOpts.value.find((item:any) => {
        console.log(item, meetData.buildingId);
        return meetData.buildingId === item.value;
      })?.extraInfo?.totalFloor || 0;
      buildClick(floorNum);
      floorChange(meetData.floorId, meetData.buildingId);
      getSceenOpts(meetData.parkOrgId);
    }
  };
  // 楼栋切换时
  const buildChange = async (value: string, buildingId: string) => {
    if (!value) {
      floorIdOptions.value = [];
      houseIdOptions.value = [];
    }
    modelRef.floorId = '';
    modelRef.houseId = '';
  };
  // 楼层切换时
  const floorChange = async (value: string, buildingId: string) => {
    modelRef.houseId = '';
    if (!value) {
      houseIdOptions.value = [];
    } else {
      houseIdOptions.value = await HouseInstance.searchHouseOpts(`${buildingId}/${value}`);
    }
  };
  const buildClick = (opt: any) => {
    let totalFloor = null;
    if (typeof (opt) === 'object') {
      totalFloor = opt.extraInfo.totalFloor;
    } else {
      totalFloor = opt;
    }
    // console.log(totalFloor, opt, 'totalFloor');
    floorIdOptions.value = Array.from(new Array(totalFloor)).map((item, index) => ({
      label: `${index + 1}F`,
      value: `${index + 1}`,
    }));
  };
  const getCompany = async (parkOrgId:string) => {
    companyOpts.value = await new CompanyMangement().listCompanyDict(parkOrgId);
  };
  const getSceenOpts = async (parkOrgId:string) => {
    sceenOpts.value = await new CustomerVo().listDropDownDevices(7, parkOrgId);
  };
  return {
    sceenOpts,
    parkOpts,
    companyOpts,
    houseIdOptions,
    parkOrgIdChange,
    floorChange,
    buildingOpts,
    buildClick,
    buildChange,
    floorIdOptions,
  };
}
</script>
<style scoped lang="scss">
.meeting-add-edit {
    .flex {
        display: flex;
        justify-content: flex-start;

        .ml5 {
            margin: 0 5px;
        }
    }

    .cursor {
        cursor: pointer;
    }

    .title {
        margin-top: 5px;
        color: #aaa;
    }
}
</style>
