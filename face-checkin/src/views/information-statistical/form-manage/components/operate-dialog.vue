<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-30 13:51:23
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
        label="所属园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择所属园区"
          show-search
          :disabled="type==='details'"
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
        label="表单名称"
        v-bind="validateInfos.title"
      >
        <a-input
          v-model:value="modelRef.title"
          :maxlength="30"
          :disabled="type==='details'"
          placeholder="请输入表单名称"
        />
      </a-form-item>

      <a-form-item
        label="收集对象"
        v-bind="validateInfos.isChooseTarget"
      >
        <a-row>
          <a-col :span="10">
            <v-select
              v-model:value="modelRef.companyIds"
              container-body
              mode="multiple"
              :disabled="type==='details'"
              show-search
              placeholder="请选择企业"
              @change="changeCompanyIds"
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
              :company-ids="modelRef.companyIds"
              :disabled="type==='details'|| !modelRef.parkOrgId "
              :park-org-ids="modelRef.parkOrgId"
              :default-select-opts="defaultEmployeeSelectOpts"
              placeholder="请选择要收集的员工"
            />
          </a-col>
        </a-row>
      </a-form-item>

      <a-form-item
        label="收集周期"
        v-bind="validateInfos.cycle"
      >
        <a-radio-group
          v-model:value="modelRef.cycle"
          :disabled="type==='details'"
        >
          <a-radio
            v-for="opt in options.FORM_CYCLE"
            :key="opt.value"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        label="截止时间"
        v-bind="validateInfos.deadlineTime"
      >
        <div class="flex">
          <template v-if="modelRef.cycle===1">
            <a-time-picker
              v-model:value="modelRef.deadlineTime"
              :disabled="type==='details'"
              value-format="HH:mm:ss"
            />
          </template>
          <template v-else-if="modelRef.cycle===2">
            <v-select
              v-model:value="modelRef.deadlineNum"
              style="width: 150px;"
              placeholder="请选择星期"
              :disabled="type==='details'"
              show-search
              container-body
            >
              <a-select-option
                v-for="opt in options.Week_Day"
                :key="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>&nbsp;&nbsp;
            <a-time-picker
              v-model:value="modelRef.deadlineTime"
              :disabled="type==='details'"
              value-format="HH:mm:ss"
            />
          </template>
          <template v-else-if="modelRef.cycle === 3">
            <v-select
              v-model:value="modelRef.deadlineNum"
              style="width: 150px;"
              placeholder="请选第几天"
              :disabled="type==='details'"
              show-search
              container-body
            >
              <a-select-option
                v-for="index in 28"
                :key="index"
              >
                第{{ index }}天
              </a-select-option>
            </v-select>&nbsp;&nbsp;
            <a-time-picker
              v-model:value="modelRef.deadlineTime"
              :disabled="type==='details'"
              value-format="HH:mm:ss"
            />
          </template>
          <a-date-picker
            v-else
            v-model:value="modelRef.deadlineTime"
            show-time
            :disabled="type==='details'"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择截止时间"
          />
        </div>
      </a-form-item>

      <!-- <a-form-item
        label="提交提醒"
        v-bind="validateInfos.reminded"
      >
        <a-radio-group v-model:value="modelRef.reminded">
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
      </a-form-item> -->
      <a-form-item
        label="表单模板"
        v-bind="validateInfos.templateId"
      >
        <v-select
          v-model:value="modelRef.templateId"
          placeholder="请选择表单模板"
          show-search
          :disabled="type==='details'"
          container-body
        >
          <a-select-option
            v-for="opt in formTemplateOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
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
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect,
} from 'vue';
import { message } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import InformationStatistical, { FormManageInfo } from '~models/information-statistical';

import { useFormManage } from '../composition/useForm';

const informationStatisticalInstance = new InformationStatistical();
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

    const options = useHashMapDics<['Week_Day', 'FORM_CYCLE']>(['Week_Day', 'FORM_CYCLE']);
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
    } = useFormManage();

    // 校验收集对象
    watchEffect(() => {
      modelRef.isChooseTarget = !!(modelRef.companyIds.length || modelRef.employeeIds.length);
    });
    const changeCompanyIds = () => {
      modelRef.employeeIds = [];
      defaultEmployeeSelectOpts.value = [];
    };
    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'add') {
          await informationStatisticalInstance.formManageAdd(modelRef);
        } else {
          await informationStatisticalInstance.formManageModify(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    const companyOpts = ref<SelectOptions>([]);
    const formTemplateOpts = ref<SelectOptions>([]);
    const parkOrgChange = async (orgId:string) => {
      modelRef.companyIds = [];
      defaultEmployeeSelectOpts.value = [];
      companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
      formTemplateOpts.value = await informationStatisticalInstance.formTemplateDic(orgId);
    };

    // const visitorClientTypeChange = () => {
    //   modelRef.visitorClientValue = '';
    // };

    // 初始化
    const details = ref<FormManageInfo>();
    const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    // const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      // modelRef.id = defaultData.value.id;
      if (type.value === 'edit' || type.value === 'details') {
        details.value = await informationStatisticalInstance.formManageFindById(defaultData.value.id);
        fill(details.value);
        // parkOrgChange(modelRef.parkOrgId);
        companyOpts.value = await new CompanyMangement().listCompanyDict(modelRef.parkOrgId);
        formTemplateOpts.value = await informationStatisticalInstance.formTemplateDic(modelRef.parkOrgId);
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
      changeCompanyIds,
      // isChooseTarget,
      defaultEmployeeSelectOpts,
      options,
      formTemplateOpts,
      // defaultOptions,
      // visitorClientTypeChange,
    };
  },
});
</script>

<style lang="less" scoped>
.flex {
    display: flex;
    align-items: center;
    justify-content: flex-start;
}
</style>
