<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-29 13:56:52
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
        label="访客类型名称"
        v-bind="validateInfos.visitorTypeName"
      >
        <a-input
          v-model:value="modelRef.visitorTypeName"
          placeholder="请输入访客类型名称"
        />
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
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import VisitorsManagement, { VisitorType } from '~models/visitors-management';
import CompanyMangement from '~models/company-mangement';
import { useAuditPerson } from '../composition/useForm';

const visitorsManagementInstance = new VisitorsManagement();
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
    } = useAuditPerson();
    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'add') {
          await visitorsManagementInstance.visitorTypeAdd(modelRef);
        } else {
          await visitorsManagementInstance.visitorTypeEdit(modelRef);
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

    // 初始化
    const details = ref<VisitorType>();
    const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (type.value === 'edit') {
        details.value = await visitorsManagementInstance.visitorTypeDetails(defaultData.value.id);
        fill(details.value);
        companyOpts.value = await new CompanyMangement().listCompanyDict(modelRef.parkOrgId);
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
      defaultOptions,
    };
  },
});
</script>
