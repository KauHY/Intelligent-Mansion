<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-27 15:51:28
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
        label="姓名"
        v-bind="validateInfos.name"
      >
        <a-input
          v-model:value="modelRef.name"
          :maxlength="20"
          placeholder="请输入姓名"
        />
      </a-form-item>
      <a-form-item
        label="手机号"
        v-bind="validateInfos.mobile"
      >
        <a-input
          v-model:value="modelRef.mobile"
          :maxlength="30"
          placeholder="请输入手机号"
        />
      </a-form-item>
      <a-form-item
        label="照片"
        v-bind="validateInfos.pics"
      >
        <v-upload
          v-model:value="modelRef.pics"
          accept=".jpg,.jpeg,.png,"
          tip="支持4M以内的jpg、jpeg、png格式文件，照片大小推荐 500*500"
          :size="4"
          :count="1"
        />
      </a-form-item>
      <a-form-item
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          :disabled="type==='edit'"
          placeholder="请选择关联园区"
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
          filter-option
          :disabled="type==='edit'"
          @change="companyChange"
        >
          <a-select-option
            v-for="opt in companyOpts"
            :key="opt.label"
            :value="opt.value"

            @click="selectCompany(opt)"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="企业类型"
      >
        <span>{{ companyTypeName }}</span>
      </a-form-item>
      <a-form-item
        label="员工标签"
        v-bind="validateInfos.tagIds"
      >
        <a-select
          v-model:value="modelRef.tagIds"
          mode="multiple"
          placeholder="请选择员工标签"
        >
          <a-select-option
            v-for="opt in tagOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="岗位"
        v-bind="validateInfos.jobIds"
      >
        <a-select
          v-model:value="modelRef.jobIds"
          mode="multiple"
          placeholder="请选择岗位"
        >
          <a-select-option
            v-for="opt in jobOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="备注"
        v-bind="validateInfos.remark"
      >
        <a-input
          v-model:value="modelRef.remark"
          :maxlength="200"
          placeholder="请输入备注"
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
import { useAddEmployees } from '../composition/useForm';
import { options } from '../config';
import useOpts from '../composition/useOpts';

const companyMangementInstance = new CompanyMangement();
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
    } = useAddEmployees(defaultData.value);

    const {
      parkOpts,
      tagOpts,
      jobOpts,
      companyOpts,
      parkOrgIdChange: baseParkOrgIdChange,
      companyChange,
    } = useOpts(modelRef);
    const companyTypeName = ref<string>();
    const selectCompany = (companyOpt:SelectOptionsMap[0]) => {
      console.log(options.COMPANY_TYPE, [companyOpt.type]);
      companyTypeName.value = options.COMPANY_TYPE[companyOpt.type].label;
    };

    const parkOrgIdChange = (parkOrgId:string, isClear = true) => {
      tagOpts.value = [];
      jobOpts.value = [];
      baseParkOrgIdChange(parkOrgId, isClear);
    };
    // const contentCp = props.type === 'resetPwd' ? resetPwd : addUser;
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      // const { type } = props;
      // const form = props.type === 'resetPwd' ? useResetPwdForm() : useAddCompany();
      const params = { ...modelRef };

      validate().then(async () => {
        if (type.value === 'add') {
          await companyMangementInstance.employeeAdd(modelRef);
        } else {
          await companyMangementInstance.employeeEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };
    const init = async () => {
      if (type.value === 'edit') {
        fill();
        await parkOrgIdChange(modelRef.parkOrgId, false);
        await companyChange(modelRef.companyId, false);
        const selectCompanyOpts = companyOpts.value.find((opt) => opt.value === modelRef.companyId);
        selectCompany(selectCompanyOpts as SelectOptionsMap[0]);
      }
    };
    onMounted(() => {
      init();
      // jobOpts.value =
    });
    return {
      currentVisible,
      confirm,
      modelRef,
      validateInfos,
      tagOpts,
      jobOpts,
      parkOpts,
      companyOpts,
      parkOrgIdChange,
      selectCompany,
      companyTypeName,
      companyChange,
    };
  },
});
</script>
