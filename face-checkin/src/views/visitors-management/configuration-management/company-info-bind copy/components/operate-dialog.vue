<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-04 09:46:07
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
        v-bind="validateInfos.id"
      >
        <v-select
          v-model:value="modelRef.id"
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
        label="链接类型"
        v-bind="validateInfos.visitorClientType"
      >
        <a-radio-group
          v-model:value="modelRef.visitorClientType"
          :options="Object.values(visitorClientTypeOpts)"
          @change="visitorClientTypeChange"
        />
      </a-form-item>
      <a-form-item
        label="地址"
        v-bind="validateInfos.visitorClientValue"
      >
        <a-input
          v-model:value="modelRef.visitorClientValue"
        />
      </a-form-item>
      <a-form-item
        label="照片"
        v-bind="validateInfos.visitorClientPicUrl"
      >
        <v-upload
          v-model:value="modelRef.visitorClientPicUrl"
          accept=".jpg,.bmp,.png,.gif"
          tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸1035*471PX"
          :size="1"
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
import VisitorsManagement, { CompanyBindInfo } from '~models/visitors-management';
import CompanyMangement from '~models/company-mangement';
import { useCompanyBindInfo } from '../composition/useForm';

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
      type: String as PropType<'add'|'edit'>,
      default: '',
    },
    parkOpts: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
    visitorClientTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
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
    } = useCompanyBindInfo();
    const confirm = () => {
      validate().then(async () => {
        await visitorsManagementInstance.companyBindInfoEdit(modelRef, type.value);
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

    const visitorClientTypeChange = () => {
      modelRef.visitorClientValue = '';
    };

    // 初始化
    const details = ref<CompanyBindInfo>();
    const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (type.value === 'edit') {
        details.value = await visitorsManagementInstance.companyBindInfoDetails(defaultData.value.id);
        fill(details.value);
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
      visitorClientTypeChange,
    };
  },
});
</script>
