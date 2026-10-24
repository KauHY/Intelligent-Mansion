<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-12 15:25:16
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
          container-body
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
        label="合同类型"
        v-bind="validateInfos.type"
      >
        <v-select
          v-model:value="modelRef.type"
          placeholder="请选择合同类型"
          show-search
          container-body
        >
          <a-select-option
            v-for="opt in contractTypeOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="导入文件"
        v-bind="validateInfos.templateKey"
      >
        <v-upload
          v-model:value="modelRef.templateKey"
          list-type="text"
          accept="application/msword,.docx,.doc"
          tip="请先根据模板字段库 ，完善模板文件的内容，录入数据后提交上传。"
          :size="1"
          @change="templateKeyChange"
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
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
// import InformationStatistical, { FormManageInfo } from '~models/information-statistical';
import ContractManage, { ContractTemplateListInfo, ContractType } from '~models/contract-manage';
import { useAddTemplate } from '../composition/useForm';

const contractManageInstance = new ContractManage();
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
    contractTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
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
    } = useAddTemplate();
    const templateKeyChange = (keys:string[], names:string) => {
      modelRef.templateName = names;
    };
    const confirm = () => {
      validate().then(async () => {
        await contractManageInstance.templateAdd(modelRef);
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    // const companyOpts = ref<SelectOptions>([]);
    // const formTemplateOpts = ref<SelectOptions>([]);
    // const parkOrgChange = async (orgId:string) => {
    //   companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
    //   // formTemplateOpts.value = await informationStatisticalInstance.formTemplateDic(orgId);
    // };

    // const visitorClientTypeChange = () => {
    //   modelRef.visitorClientValue = '';
    // };

    // 初始化
    // const details = ref<FormManageInfo>();
    // const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    // // const defaultOptions = ref<SelectOptions>([]);
    // const init = async () => {
    //   // modelRef.id = defaultData.value.id;
    //   if (type.value === 'edit') {
    //     details.value = await informationStatisticalInstance.formManageFindById(defaultData.value.id);
    //     fill(details.value);
    //     parkOrgChange(modelRef.parkOrgId);
    //     const { employeeIds, employeeNames } = details.value;
    //     if (employeeIds && employeeNames) {
    //       defaultEmployeeSelectOpts.value = employeeIds.map((id, index) => ({
    //         label: employeeNames[index],
    //         value: id,
    //       }));
    //     }
    //     // isReady.value = true;
    //   }
    // };
    // init();
    return {
      currentVisible,
      // details,
      confirm,
      modelRef,
      validateInfos,
      templateKeyChange,
      // companyOpts,
      // parkOrgChange,
      // isChooseTarget,
      // defaultEmployeeSelectOpts,
      options,
      // formTemplateOpts,
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
