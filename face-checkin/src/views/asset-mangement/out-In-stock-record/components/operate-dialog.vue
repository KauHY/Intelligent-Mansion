<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 14:50:58
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    title="设置授权"
    :width="780"
    :body-style="{ maxHeight: '600px', overflow: 'auto' }"
    class="operate-dialog"

    @ok="confirm"
  >
    <div class="view-house-apply-details">
      <a-form
        :label-col="{span:4}"
      >
        <a-form-item
          label="园区"
          v-bind="validateInfos.companyIds"
        >
          {{ modelRef.parkOrgName }}
        </a-form-item>
        <a-form-item
          label="授权给公司"
          v-bind="validateInfos.companyIds"
        >
          <v-select
            v-model:value="modelRef.companyIds"
            mode="multiple"
            placeholder="请选择公司"
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
          label="适用员工"
        >
          <v-select-employee
            v-model:value="modelRef.employeeIds"
            :park-org-ids="[modelRef.parkOrgId]"
            :company-ids="modelRef.companyIds.join(',')"
            :default-select-opts="defaultEmployeeSelectOpts"
            placeholder="请选择考勤规则适用员工"
          />
        </a-form-item>
      </a-form>
    </div>

    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          取消
        </a-button>
        <a-button
          v-if="type!=='details'"
          type="primary"
          @click="confirm"
        >
          保存
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import ReceiveInspection from '~models/asset-mangement';
import CompanyMangement from '~models/company-mangement';
import Attendance, { AttendanceRules } from '~models/attendance';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import { useAddPatrolPlace } from '../composition/useFormConf';

const attendanceInstance = new Attendance();
export default defineComponent({
  name: 'OperateDialog',
  props: {
    title: {
      type: String,
      default: '',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const receiveInspectionInessInstance = new ReceiveInspection();
    const {
      type, visible, defaultData,
    } = toRefs(props);
    const cp = shallowRef();

    // 弹框展示隐藏
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });
    const {
      validate, validateInfos, modelRef, fill,
    } = useAddPatrolPlace();

    // 下拉数据
    const { options, companyOpts, parkOrgChange } = useOptions();

    // 初始化
    const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    const init = async () => {
      modelRef.id = defaultData.value.id;
      parkOrgChange(defaultData.value.parkOrgId);
      defaultEmployeeSelectOpts.value = await attendanceInstance.getAllEmployeeByRuleId(modelRef.id);
      fill({
        ...defaultData.value,
        employeeIds: defaultData.value.employeeId.split(),
        companyIds: defaultData.value.companyId.split(),
      });
    };

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      try {
        await validate();
        receiveInspectionInessInstance.receiveAuditSetting({
          ...modelRef,
          employeeId: modelRef.employeeIds.join(','),
          companyId: modelRef.companyIds.join(','),
        });
        emit('operate:success');
        currentVisible.value = false;
      } catch (error) {
        console.error(error, 'error');
      }
    };
    const goodsVisible = ref(false);
    onMounted(() => {
      init();
    });
    return {
      currentVisible,
      validateInfos,
      modelRef,
      confirm,
      goodsVisible,
      companyOpts,
      defaultEmployeeSelectOpts,
      // cp,
    };
  },
});

function useOptions() {
  const options = useHashMapDics<['ATTENDANCE_RULE_APPLY_TYPE', 'Week_Day']>(['ATTENDANCE_RULE_APPLY_TYPE', 'Week_Day']);
  const companyOpts = ref<SelectOptions>([]);
  const parkOrgChange = async (orgId:string) => {
    companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
  };

  return {
    options,
    companyOpts,
    parkOrgChange,
  };
}
</script>
<style lang="less" scoped>
.view-house-apply-details {
    .item {
        display: flex;
        justify-content: flex-start;
        margin-bottom: 30px;

        .label {
            width: 160px;
            margin-right: 40px;
            text-align: right;
        }
    }
}
</style>
