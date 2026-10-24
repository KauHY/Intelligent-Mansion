<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-30 18:27:32
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
        label="员工标签"
        v-bind="validateInfos.tagName"
      >
        <a-input
          v-model:value="modelRef.tagName"
          :maxlength="20"
          placeholder="请输入员工标签"
        />
      </a-form-item>
      <a-form-item
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
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
import { companyTypeNames } from '../config';
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
    const { type, visible } = toRefs(props);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });
    const { defaultData } = toRefs(props);
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
      parkOrgIdChange,
      companyChange,
    } = useOpts(modelRef);
    // const parkOrgIdChange = async (parkOrgId:string) => { // 切换园区
    //   companyOpts.value = await companyMangementInstance.listCompanyDict(parkOrgId);
    //   modelRef.companyId = '';
    // };
    // const companyChange = async (companyId:string) => {
    //   modelRef.jobIds = [];
    //   modelRef.tagIds = [];
    //   jobOpts.value = await companyMangementInstance.listJobDict(companyId);
    //   tagOpts.value = await companyMangementInstance.listTagDict(companyId);
    // };
    const companyTypeName = ref<string>();
    const selectCompany = (companyOpt:SelectOptions[0]) => {
      console.log((companyTypeNames as any), [companyOpt.type]);
      companyTypeName.value = (companyTypeNames as any)[companyOpt.type].label;
    };
    // const contentCp = props.type === 'resetPwd' ? resetPwd : addUser;
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      // const form = props.type === 'resetPwd' ? useResetPwdForm() : useAddCompany();
      const params = { ...modelRef };

      validate().then(async () => {
        if (type.value === 'add') {
          await companyMangementInstance.tagAdd(modelRef);
        } else {
          await companyMangementInstance.tagEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };
    onMounted(() => {
      fill();
      if (type.value === 'edit') {
        parkOrgIdChange(modelRef.parkOrgId, false);
      }
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
