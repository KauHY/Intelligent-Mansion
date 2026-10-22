<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 14:51:43
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
        label="规则名称"
        v-bind="validateInfos.ruleName"
      >
        <a-input
          v-model:value="modelRef.ruleName"
          :maxlength="20"
          :disabled="type==='detail'"
          placeholder="请输入规则名称"
        />
      </a-form-item>
      <a-form-item
        label="所属园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择关联园区"
          :disabled="type==='detail'"
          @change="parkOrgChange"
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
        label="考勤公司"
        v-bind="validateInfos.companyId"
      >
        <v-select
          v-model:value="modelRef.companyId"
          :disabled="type==='detail'"
          placeholder="请选择考勤公司"
          @change="companyChange"
        >
          <a-select-option
            v-for="opt in companyOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="考勤人员"
        v-bind="validateInfos.applyType"
      >
        <a-radio-group
          v-model:value="modelRef.applyType"
          :disabled="type==='detail'"
        >
          <a-radio
            v-for="opt in options.ATTENDANCE_RULE_APPLY_TYPE"
            :key="opt.value"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        v-if="modelRef.applyType===1"
        label="适用员工"
      >
        <v-select-employee
          v-model:value="modelRef.employeeIds"
          :disabled="type==='detail'"
          :park-org-ids="[modelRef.parkOrgId]"
          :company-ids="modelRef.companyId"
          :default-select-opts="defaultEmployeeSelectOpts"
          placeholder="请选择考勤规则适用员工"
        />
      </a-form-item>
      <a-form-item
        label="上班时间"
        required
      >
        <v-range-time
          v-model:startTime="modelRef.punchInTime"
          v-model:endTime="modelRef.punchOutTime"
          :disabled="type==='detail'"
          :start-rule="validateInfos.punchInTime"
          :end-rule="validateInfos.punchOutTime"
        />
      </a-form-item>
      <a-form-item
        label="工作日"
        v-bind="validateInfos.workdayType"
      >
        <a-radio-group
          v-model:value="modelRef.workdayType"
          :disabled="type==='detail'"
          name="radioGroup"
          @change="changeRadio()"
        >
          <a-radio
            :value="0"
            :style="radioStyle"
          >
            法定节假日<span
              style="color: #b7b7b7;"
            >（根据法定节假日放假，补班安排自动调整班次）</span>
          </a-radio>
          <a-radio
            :value="1"
            :style="radioStyle"
          >
            自定义
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        v-if="modelRef.workdayType===1"
        label="工作日"
        class="workday"
        v-bind="validateInfos.daysOfWork"
      >
        <a-checkbox-group
          v-model:value="modelRef.daysOfWork"
          :disabled="type==='detail'"
          :options="Object.values(options['Week_Day'])"
        />
      </a-form-item>

      <!-- <a-checkbox-group
      name="daysOfWork"
        :rules="modelRef.workdayType===1?rules.daysOfWork:[{required:false, message: 'Please input', trigger: 'change'}]"
        v-model:value="modelRef.daysOfWork"
        :options="Object.values(options['Week_Day'])"
      /> -->

      <!-- <a-form-item
        label="节假日是否适用"
        v-bind="validateInfos.holiday"
      >
        <a-radio-group v-model:value="modelRef.holiday">
          <a-radio :value="1">
            是
          </a-radio>
          <a-radio :value="0">
            否
          </a-radio>
        </a-radio-group>
      </a-form-item> -->
      <a-form-item
        label="考勤地点"
        v-bind="validateInfos.location"
        @click="mapShow=true"
      >
        <a-input
          v-model:value="modelRef.location"
          placeholder="请选择考勤地点"
          :disabled="type==='detail'"
          readonly
        />
      </a-form-item>
      <a-form-item
        label="考勤范围"
        v-bind="validateInfos.punchRange"
      >
        <a-input
          v-model:value.number="modelRef.punchRange"
          placeholder="请输入考勤范围"
          :disabled="type==='detail'"
          :maxlength="4"
          allow-clear
        >
          <template #suffix>
            <span>米以内</span>
          </template>
        </a-input>
      </a-form-item>
    </a-form>
    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          {{ type==="detail"?'关闭':'取消' }}
        </a-button>
        <a-button
          v-if="type!=='detail'"
          type="primary"
          @click="confirm"
        >
          确认
        </a-button>
      </div>
    </template>
    <gdMap
      v-model:show="mapShow"
      @change="addressInfoChange"
    />
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, reactive, computed, onMounted, toRefs, ref, shallowRef, Ref,
} from 'vue';
import { message } from 'ant-design-vue';
import Attendance, { AttendanceRules } from '~models/attendance';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import gdMap from '~components/map/map.vue';
import { AddressInfo } from '~components/map/type';
import { useAttendanceRule } from '../composition/useForm';

const attendanceInstance = new Attendance();
export default defineComponent({
  name: 'OperateDialog',
  components: { gdMap },
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
    const formRef = ref();
    const rules = {
      daysOfWork: [
        { required: true, message: 'Please input Activity name', trigger: 'blur' },
      ],
    };
    const value = ref<number>(0);
    const radioStyle = reactive({
      display: 'block',
      height: '30px',
      lineHeight: '30px',
    });
    const {
      type, visible, defaultData, parkOpts,
    } = toRefs(props);
    // const isReady = ref(false);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    const {
      modelRef,
      validate,
      clearValidate,
      validateInfos,
      fill,
      validateField,
    } = useAttendanceRule();

    // 下拉数据
    const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    const {
      options, companyOpts, parkOrgChange, companyChange,
    } = useOptions(modelRef, defaultEmployeeSelectOpts);

    // 地图操作
    const { mapShow, addressInfoChange } = useMap(modelRef);

    const details = ref<AttendanceRules>();
    const confirm = () => {
      validate().then(async () => {
        if (modelRef.workdayType === 1 && modelRef.daysOfWork.length === 0) {
          message.warning('请选择工作日');
          return;
        }
        if (modelRef.applyType === 1 && modelRef.employeeIds.length === 0) {
          message.warning('请选择指定的考勤人员');
          return;
        }
        if (type.value === 'add') {
          await attendanceInstance.attendanceAdd(modelRef);
        } else {
          await attendanceInstance.attendanceModify(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        console.log(error);
        message.warning('操作失败');
      });
    };
    const changeRadio = async () => {
      modelRef.daysOfWork = [1, 2, 3, 4, 5];
    };

    // 初始化

    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (type.value === 'add') {
        modelRef.parkOrgId = parkOpts.value[0].value;
        companyOpts.value = await new CompanyMangement().listCompanyDict(modelRef.parkOrgId);
      }
      if (type.value === 'edit' || type.value === 'detail') {
        details.value = await attendanceInstance.attendanceFindById(defaultData.value.id);
        fill(details.value);
        companyOpts.value = await new CompanyMangement().listCompanyDict(modelRef.parkOrgId);
        if (modelRef.applyType === 1) { // 适用范围为指定人员时调用接口
          defaultEmployeeSelectOpts.value = await attendanceInstance.getAllEmployeeByRuleId(modelRef.id);
          const arr: any[] = [];
          defaultEmployeeSelectOpts.value.forEach((item) => {
            if (item.value) {
              arr.push(item.value);
            }
          });
          modelRef.employeeIds = arr;
          // modelRef.employeeIds = [defaultEmployeeSelectOpts.value[0].value];
        }
      }
    };
    init();
    return {
      clearValidate,
      rules,
      currentVisible,
      details,
      confirm,
      modelRef,
      validateInfos,
      companyOpts,
      options,
      parkOrgChange,
      companyChange,
      mapShow,
      addressInfoChange,
      defaultEmployeeSelectOpts,
      changeRadio,
      radioStyle,
    };
  },
});

function useOptions(modelRef:AttendanceRules, defaultEmployeeSelectOpts: Ref<SelectOptions>) {
  const options = useHashMapDics<['ATTENDANCE_RULE_APPLY_TYPE', 'Week_Day']>(['ATTENDANCE_RULE_APPLY_TYPE', 'Week_Day']);
  const companyOpts = ref<SelectOptions>([]);
  const parkOrgChange = async (orgId:string) => {
    modelRef.companyId = '';
    modelRef.employeeIds = [];
    defaultEmployeeSelectOpts.value = [];
    companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
  };
  const companyChange = () => {
    modelRef.employeeIds = [];
    defaultEmployeeSelectOpts.value = [];
  };
  return {
    options,
    companyOpts,
    parkOrgChange,
    companyChange,

  };
}
function useMap(modelRef:AttendanceRules) {
  //  地图操作
  const mapShow = ref(false);
  const addressInfoChange = (addressInfo:AddressInfo) => {
    console.log(addressInfo, '----addressInfo');
    const {
      location, name,
    } = addressInfo;
    modelRef.latitude = location.lat;
    modelRef.longitude = location.lng;
    modelRef.location = name;
  };
  return {
    mapShow,
    addressInfoChange,
  };
}
</script>
<style scoped lang="scss">
:deep .workday .ant-form-item-label > label {
    color: rgba(0, 0, 0, 0);
}
</style>
