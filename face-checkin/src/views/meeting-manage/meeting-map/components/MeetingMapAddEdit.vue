<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-01-26 17:44:06
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    class="meeting-map-add-edit"
    :visible="true"
    :width="730"
    :title="meetingMapConfig.title"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div>
      <a-form
        ref="formRef"
        :model="modelRef"
        :rules="rulesRef"
        :label-col="{ span: 5 }"
      >
        <a-form-item
          label="所属园区"
          name="parkOrgId"
        >
          <v-select
            v-model:value="modelRef.parkOrgId"
            placeholder="请选择所属园区"
            :disabled="meetingMapConfig.type !=='add'"
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
            :disabled="meetingMapConfig.type !=='add'"
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
            :disabled="meetingMapConfig.type !=='add'"
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
          label="会议室分布图"
          name="meetingPlaneFigure"
        >
          <v-upload
            v-model:value="modelRef.meetingPlaneFigure"
            :size="5"
            accept=".jpg,.bmp,.png,.gif"
            tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸800*800PX"
          />
        </a-form-item>
      </a-form>
    </div>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, PropType, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import Building, { FloorListItem } from '~models/building';
import MeetingVo, { MeetingMap } from '~models/meeting-manage';
import { meetingMapForm } from '../composition/useForm';

export default defineComponent({
  props: {
    meetingMapConfig: {
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
    const {
      parkOpts, houseIdOptions, parkOrgIdChange, buildingOpts,
    } = useOptions();
    const { modelRef, rulesRef } = meetingMapForm();
    const { mapData, type } = toRefs(props.meetingMapConfig);
    const MeetingVoReq = new MeetingVo();
    const floorIdOptions = ref<SelectOptions>([]);
    const fill = () => {
      Object.entries(modelRef).forEach(([key]) => {
        if ((mapData.value as any)[key]) {
          const formData = modelRef as any;
          formData[key] = mapData.value[key];
        }
      });
      parkOrgIdChange(modelRef.parkOrgId);
      floorIdOptions.value = [{ label: `${modelRef.floorId}F`, value: modelRef.floorId }];
    };
    if (type.value !== 'add') {
      fill();
    }
    const options = useHashMapDics<['YES_OR_NO', 'MEETING_EQUIPMENT', 'MEETING_ROOM_TYPE']>([
      'YES_OR_NO',
      'MEETING_EQUIPMENT',
      'MEETING_ROOM_TYPE',
    ]);
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: MeetingVoReq.meetingMapCreateApi,
            edit: MeetingVoReq.meetingMapModifyApi,
          };
          await apiReq[type.value as 'add' | 'edit'](modelRef);
          emit('refresh', {});
          emit('update:show', false);
        } catch (error:any) {
          console.log(error);
        }
      });
    };
    const buildClick = (opt: any) => {
      floorIdOptions.value = Array.from(new Array(opt.extraInfo.totalFloor)).map((item, index) => ({
        label: `${index + 1}F`,
        value: `${index + 1}`,
      }));
    };
    const init = async () => {
    };
    init();
    return {
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      parkOpts,
      buildingOpts,
      floorIdOptions,
      houseIdOptions,
      parkOrgIdChange,
      buildClick,
    };
  },
});

function useOptions() {
  const FloorInstance = new Building();
  const parkOpts = ref<SelectOptions>([]);
  const buildingOpts = ref<SelectOptions>([]);
  const houseIdOptions = ref<SelectOptions>([]);
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
  })();
  // 园区切换
  const parkOrgIdChange = async (value: string) => {
    buildingOpts.value = await FloorInstance.listBuildingDict(value);
  };
  return {
    parkOpts,
    houseIdOptions,
    parkOrgIdChange,
    buildingOpts,
  };
}
</script>
<style scoped lang="scss">
.meeting-map-add-edit {
    .cursor {
        cursor: pointer;
    }
}
</style>
