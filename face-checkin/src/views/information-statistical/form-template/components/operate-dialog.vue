<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 18:52:24
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
        label="模板名称"
        v-bind="validateInfos.title"
      >
        <a-input
          v-model:value="modelRef.title"
          :maxlength="30"
          placeholder="请输入模板名称"
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
import InformationStatistical, { FormTemplateInfo, FormTemplateSet } from '~models/information-statistical';

import { useFormTemplateSet } from '../composition/useForm';

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
    } = useFormTemplateSet();

    // 校验收集对象
    // watchEffect(() => {
    //   modelRef.isChooseTarget = !!(modelRef.companyIds.length || modelRef.employeeIds.length);
    // });
    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'add') {
          await informationStatisticalInstance.formTemplateAdd(modelRef);
        } else {
          await informationStatisticalInstance.formTemplateModify(modelRef);
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
    };

    // const visitorClientTypeChange = () => {
    //   modelRef.visitorClientValue = '';
    // };

    // 初始化
    const details = ref<FormTemplateInfo>();
    const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    // const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      // modelRef.id = defaultData.value.ids;
      if (type.value === 'edit') {
        fill(defaultData.value);
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
      // isChooseTarget,
      defaultEmployeeSelectOpts,
      // defaultOptions,
      // visitorClientTypeChange,
    };
  },
});
</script>
