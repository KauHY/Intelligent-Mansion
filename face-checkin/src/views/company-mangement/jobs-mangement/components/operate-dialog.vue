<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-27 12:18:03
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
      :label-col="{span:6}"
    >
      <a-form-item
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择关联园区"
          :disabled="type==='edit'"
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
      <a-form-item
        label="所属企业"
        v-bind="validateInfos.companyId"
      >
        <v-select
          v-model:value="modelRef.companyId"
          placeholder="请选择所属企业"
          :disabled="type==='edit'"
          @change="companyChange"
        >
          <a-select-option
            v-for="opt in companyOpts"
            :key="opt.value"
            @click="selectCompany(opt)"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <!-- <a-form-item
        label="企业类型"
      >
        <span>{{ companyTypeName }}</span>
      </a-form-item> -->
      <a-form-item
        label="岗位"
        v-bind="validateInfos.jobCode"
      >
        <v-select
          v-model:value="modelRef.jobCode"
          placeholder="请选择岗位"
          :disabled="type==='edit'"
        >
          <a-select-option
            v-for="opt in jobOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>

      <a-form-item
        label="关联员工"
      >
        <v-select-employee
          v-model:value="modelRef.employeeIds"
          :park-org-ids="[modelRef.parkOrgId]"
          :company-ids="modelRef.companyId"
          :default-select-opts="defaultEmployeeSelectOpts"
          placeholder="请选择要关联的员工"
        />
      </a-form-item>
    </a-form>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref,
} from 'vue';
import { message } from 'ant-design-vue';
// import addUser from './add-component.vue';
// import resetPwd from './reset-pwd.vue';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
import { useRemoteFilterSelect } from '~composition/common';
import { useAddJob } from '../composition/useForm';
import { companyTypeNames } from '../config';
import useOpts from '../composition/useOpts';

export default defineComponent({
  name: 'OperateDialog',
  components: {},
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
    // parkOpts: {
    //   type: Array as PropType<SelectOptions>,
    //   default: () => ([]),
    // },

  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const companyMangementInstance = new CompanyMangement();
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    const { defaultData, type } = toRefs(props);
    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = useAddJob(defaultData.value);

    const {
      loading,
      list: employeesOpts,
      remoteSearch,
      otherParams,
      setDefaultOptios,
    } = useRemoteFilterSelect(companyMangementInstance.selectEmployeesList, {
      disableAutoSearch: false,
    });

    const {
      parkOpts,
      jobOpts,
      companyOpts,
      parkOrgIdChange: baseParkOrgIdChange,
      companyChange,
    } = useOpts(modelRef, otherParams);

    const companyTypeName = ref<string>();
    const selectCompany = (companyOpt:SelectOptions[0]) => {
      console.log((companyTypeNames as any), [companyOpt.type]);
      companyTypeName.value = (companyTypeNames as any)[companyOpt.type]?.label;
    };

    // const contentCp = props.type === 'resetPwd' ? resetPwd : addUser;
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      const { type } = props;
      // const form = props.type === 'resetPwd' ? useResetPwdForm() : useAddCompany();
      const params = { ...modelRef };

      validate().then(async () => {
        if (type === 'add') {
          await companyMangementInstance.jobAdd(modelRef);
        } else {
          await companyMangementInstance.jobEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    const parkOrgIdChange = (parkOrgId:string, isClear = true) => {
      defaultEmployeeSelectOpts.value = [];
      modelRef.jobCode = null;
      baseParkOrgIdChange(parkOrgId);
    };
    onMounted(async () => {
      fill();
      if (type.value === 'edit') {
        // parkOrgIdChange(modelRef.parkOrgId, false); // 查询企业下拉数据
        companyOpts.value = await companyMangementInstance.listCompanyDict(modelRef.parkOrgId);
        companyChange(modelRef.companyId);
        companyTypeName.value = (companyTypeNames as any)[defaultData.value.companyType]?.label;
        defaultEmployeeSelectOpts.value = modelRef.employeeIds.map((employeeId, index) => ({
          label: modelRef.employeeNames[index],
          value: employeeId,
        }));
        console.log(defaultEmployeeSelectOpts.value, '-----defaultEmployeeSelectOpts');
        // setDefaultOptios(defaultEmployeesOpts);
        // remoteSearch('');
      }
    });
    return {
      currentVisible,
      confirm,
      modelRef,
      validateInfos,
      jobOpts,
      parkOpts,
      companyOpts,
      parkOrgIdChange,
      selectCompany,
      companyTypeName,
      defaultEmployeeSelectOpts,
      // loading,
      // employeesOpts,
      // remoteSearch,
      companyChange,
    };
  },
});
</script>
