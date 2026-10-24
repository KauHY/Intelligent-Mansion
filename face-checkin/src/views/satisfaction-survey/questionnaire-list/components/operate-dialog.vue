<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-01 17:00:02
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="820"
    class="operate-dialog"
    @ok="confirm"
  >
    <a-form
      :model="modelRef"
      :label-col="{span:5}"
    >
      <a-form-item
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择关联园区"
          show-search
          container-body

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
        label="问卷标题"
        v-bind="validateInfos.title"
      >
        <a-input
          v-model:value="modelRef.title"
          :maxlength="30"
          placeholder="请输入问卷标题"
        />
      </a-form-item>

      <a-form-item
        label="收集对象"
        v-bind="validateInfos.isChooseTarget"
      >
        <a-row>
          <!-- <a-input
            v-show="false"
            :value="isChooseTarget"
          /> -->
          <a-col :span="10">
            <v-select
              v-model:value="modelRef.companyIds"
              container-body
              mode="multiple"
              show-search
              placeholder="请选择企业"
            >
              <a-select-option
                v-for="opt in companyOpts"
                :key="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-col>
          <a-col
            :span="10"
            :offset="1"
          >
            <v-select-employee
              v-model:value="modelRef.employeeIds"
              :disabled="!modelRef.parkOrgId"
              :park-org-ids="modelRef.parkOrgId"
              :default-select-opts="defaultEmployeeSelectOpts"
              placeholder="请选择要收集的员工"
            />
          </a-col>
        </a-row>
        <div class="title">
          可选择指定公司或指定人员，公司与人员的选择相互独立，互不影响
        </div>
      </a-form-item>

      <a-form-item
        label="生效开始时间"
        v-bind="validateInfos.startTime"
      >
        <a-date-picker
          v-model:value="modelRef.startTime"
          show-time
          placeholder="请选择生效开始时间"
          :disabled-date="disabledStartDate"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </a-form-item>

      <a-form-item
        label="生效截止时间"
      >
        <a-date-picker
          v-model:value="modelRef.deadlineTime"
          :disabled-date="disabledEndDate"
          show-time
          placeholder="请选择生效截止时间"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </a-form-item>
      <a-form-item
        label="是否匿名"
        v-bind="validateInfos.anonymous"
      >
        <a-radio-group v-model:value="modelRef.anonymous">
          <a-radio
            :value="true"
          >
            是
          </a-radio>
          <a-radio
            :value="false"
          >
            否
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        label="封面图"
        v-bind="validateInfos.coverPhoto"
      >
        <v-upload
          v-model:value="modelRef.coverPhoto"
          accept=".jpg,.bmp,.png,.gif"
          tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸360*270PX"
          :size="1"
        />
      </a-form-item>
    </a-form>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect,
} from 'vue';
import { message } from 'ant-design-vue';
import { Dayjs } from 'dayjs';
import CompanyMangement from '~models/company-mangement';
import DesignQuestionnaire, { QuestionnaireInfo, QuestionnaireSet } from '~models/satisfaction-survey';
import { useQuestionnaireSet } from '../composition/useForm';

const designQuestionnaireInstance = new DesignQuestionnaire();
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
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { type, visible, defaultData } = toRefs(props);

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
      validateInfos,
      fill,
    } = useQuestionnaireSet();

    // 校验收集对象
    watchEffect(() => {
      modelRef.isChooseTarget = !!(modelRef.companyIds.length || modelRef.employeeIds.length);
    });
    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'add') {
          await designQuestionnaireInstance.questionnaireAdd(modelRef);
        } else {
          await designQuestionnaireInstance.questionnaireModify(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    const companyOpts = ref<SelectOptions>([]);
    const parkOrgChange = async (orgId:string) => {
      companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
      modelRef.companyIds = [];
      modelRef.employeeIds = [];
      defaultEmployeeSelectOpts.value = [];
    };

    // 初始化
    const details = ref<QuestionnaireInfo>();
    const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    // const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      // modelRef.id = defaultData.value.id;
      if (type.value === 'edit') {
        details.value = await designQuestionnaireInstance.questionnaireFindById(defaultData.value.id);
        fill(details.value);
        companyOpts.value = await new CompanyMangement().listCompanyDict(modelRef.parkOrgId);
        const { employeeIds, employeeNames } = details.value;
        if (employeeIds && employeeNames) {
          defaultEmployeeSelectOpts.value = employeeIds.map((id, index) => ({
            label: employeeNames[index],
            value: id,
          }));
        }
        // isReady.value = true;
      }
    };
    init();
    return {
      currentVisible,
      details,
      confirm,
      modelRef,
      validateInfos,
      companyOpts,
      parkOrgChange,
      defaultEmployeeSelectOpts,
      ...useDisabledDate(modelRef),
      // isChooseTarget,
      // defaultOptions,
      // visitorClientTypeChange,
    };
  },
});
function useDisabledDate(modelRef:QuestionnaireSet) {
  const disabledStartDate = (e:Dayjs) => (modelRef.deadlineTime ? e.valueOf() > new Date(modelRef.deadlineTime).getTime() : false);
  const disabledEndDate = (e:Dayjs) => {
    console.log(e.valueOf(), Math.max(Date.now(), new Date(modelRef.startTime || Date.now()).getTime()), '-----');
    return e.valueOf() < (modelRef.startTime ? Math.max(Date.now(), new Date(modelRef.startTime).getTime()) : Date.now());
  };
  return {
    disabledStartDate,
    disabledEndDate,
  };
}
</script>
<style scoped>
.title {
    margin-top: 5px;
    color: #aaa;
}
</style>
