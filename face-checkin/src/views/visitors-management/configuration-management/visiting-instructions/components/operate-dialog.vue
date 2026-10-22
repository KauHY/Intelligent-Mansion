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
        label="须知名称"
        v-bind="validateInfos.noticeName"
      >
        <a-input
          v-model:value="modelRef.noticeName"
          :maxlength="30"
          placeholder="请输入须知名称"
          :disabled="type==='details'"
        />
      </a-form-item>

      <a-form-item

        label="关联园区"
        v-bind="type==='details'?'':validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          :disabled="type==='details'"
          show-search
          placeholder="请选择关联园区"
          filter-option
          @change="parkOrgChange"
        >
          <a-select-option
            v-for="opt in parkOpts"
            :key="opt.label"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <!-- <a-form-item
        v-else
        label="关联园区"
        v-bind="validateInfos.parkOrgName"
      >
        <a-input
          v-model:value="modelRef.parkOrgName"

          :disabled="type==='details'"
        />
      </a-form-item> -->
      <a-form-item
        label="来访须知内容"
        v-bind="validateInfos.noticeContent"
      >
        <v-editor
          v-model:value="modelRef.noticeContent"
          :disabled="type==='details'"
          :insert-picture-adaptation="true"
        />
        <div class="form-tip">
          来访须知内容信息展示于小程序的访客须知页面
        </div>
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
import VisitorsManagement, { visitingInstructions } from '~models/visitors-management';
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
          await visitorsManagementInstance.visitingInstructionsAdd(modelRef);
        } else {
          await visitorsManagementInstance.visitingInstructionsEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error) => {
        message.warning('操作失败');
      });
    };

    const companyOpts = ref<SelectOptions>([]);
    // const parkOrgChange = async (orgId:string) => {
    //   companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
    // };
    // 园区切换
    const parkOrgChange = async (value: string) => {
      // houseIdOptions.value = [];
      // materialDataList.value = [];
      defaultOptions.value = [];
      // defaultOptions.value = [];
      if (value) {
        getCompany(value);
        // getBuilding(value);
      }
    };

    const getCompany = async (parkOrgId: string) => {
      companyOpts.value = await new CompanyMangement().listCompanyDict(parkOrgId);
    };

    // 初始化
    const details = ref<visitingInstructions>();
    const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (['edit', 'details'].includes(type.value)) {
        details.value = await visitorsManagementInstance.visitingInstructionsDetails(defaultData.value.id);
        console.log(details, 'details');
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
    };
  },
});
</script>
