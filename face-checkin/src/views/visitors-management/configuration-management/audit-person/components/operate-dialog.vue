<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 14:56:55
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
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择关联园区"
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
        label="企业名称"
        v-bind="validateInfos.companyId"
      >
        <v-select
          v-model:value="modelRef.companyId"
          placeholder="请选择企业名称"
          @change="companyOrgChange"
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
        label="审批人"
        v-bind="validateInfos.invitorEmployeeAudit"
      >
        <!-- <employee-select
          v-model:value="modelRef.auditEmployeeIds"
          placeholder="请选择审批人"
          mode="multiple"
          :default-options="defaultOptions"
          :company-id="modelRef.companyId"
        /> -->
        <!-- <v-select-employee
          v-model:value="modelRef.auditEmployeeIds"
          :park-org-ids="modelRef.parkOrgId"
          :company-ids="modelRef.companyId"
          :default-select-opts="defaultOptions"
          placeholder="请选择考勤规则适用员工"
        /> -->
        <a-radio-group v-model:value="modelRef.invitorEmployeeAudit">
          <a-radio :value="1">
            被访人
          </a-radio>
          <a-radio :value="0">
            自定义审批人
          </a-radio>
        </a-radio-group>
      </a-form-item>

      <a-row v-if="modelRef.invitorEmployeeAudit===0">
        <a-col
          :span="19"
          :offset="5"
        >
          <a-form-item v-bind="validateInfos.auditEmployeeIds">
            <v-select-employee
              v-model:value="modelRef.auditEmployeeIds"
              :disabled="!modelRef.parkOrgId||!modelRef.companyId"
              :park-org-ids="modelRef.parkOrgId"
              :company-ids="modelRef.companyId"
              :default-select-opts="defaultOptions"
              placeholder="请选择审批人"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <!-- <a-form-item
        v-else
        label="企业名称"
        v-bind="validateInfos.auditEmployeeId"
      >
        <employee-select
          v-model:value="modelRef.auditEmployeeId"
          placeholder="请选择审批人"
          :default-options="defaultOptions"
          :company-id="modelRef.companyId"
        />
      </a-form-item> -->
    </a-form>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import VisitorsManagement, { AuditPerson } from '~models/visitors-management';
import CompanyMangement from '~models/company-mangement';
import employeeSelect from '~components/employee-select';
import { useAuditPerson } from '../composition/useForm';

const visitorsManagementInstance = new VisitorsManagement();
export default defineComponent({
  name: 'OperateDialog',
  // components: { employeeSelect },
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
      type: String as PropType<'add'|'edit'>,
      default: '',
    },
    parkOpts: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { type, visible } = toRefs(props);
    const isReady = ref(false);

    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return ['edit'].includes(type.value) ? isReady.value && visible.value : visible.value;
      },
    });

    const { defaultData } = toRefs(props);

    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = useAuditPerson(type);

    const companyOpts = ref<SelectOptions>([]);
    const parkOrgChange = async (orgId:string) => {
      if (!orgId) return;
      modelRef.companyId = '';
      modelRef.auditEmployeeIds = [];
      defaultOptions.value = [];
      companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
    };
    const companyOrgChange = async () => {
      modelRef.auditEmployeeIds = [];
      defaultOptions.value = [];
    };

    const confirm = () => {
      validate().then(async () => {
        if (modelRef.invitorEmployeeAudit === 1) {
          modelRef.auditEmployeeIds = [];
        }
        if (type.value === 'add') {
          await visitorsManagementInstance.auditPersonAdd(modelRef);
        } else {
          await visitorsManagementInstance.auditPersonEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    // 初始化
    const details = ref<AuditPerson>();
    const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (type.value === 'edit') {
        details.value = await visitorsManagementInstance.auditPersonDetails(defaultData.value.id);
        const { auditEmployeeIds, auditEmployeeNames } = details.value;
        if (auditEmployeeIds?.length && auditEmployeeNames) { // 设置默认数据
          defaultOptions.value = auditEmployeeIds.map((id, index) => ({
            label: auditEmployeeNames[index],
            value: id,
          }));
        }
        fill(details.value);
        companyOpts.value = await new CompanyMangement().listCompanyDict(modelRef.parkOrgId as string);
        isReady.value = true;
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
      companyOrgChange,
      defaultOptions,
    };
  },
});
</script>
