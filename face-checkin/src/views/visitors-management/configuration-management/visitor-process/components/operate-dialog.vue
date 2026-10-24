<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-26 14:20:04
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
          @change="companyChange"
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
        label="流程名称"
        v-bind="validateInfos.processName"
      >
        <a-input
          v-model:value="modelRef.processName"
          :maxlength="30"
          placeholder="请输入流程名称"
        />
      </a-form-item>
      <a-form-item
        label="流程类型"
        v-bind="validateInfos.processType"
      >
        <v-select
          v-model:value="modelRef.processType"
          placeholder="请选择流程类型"
          @change="processTypeChange"
        >
          <template v-for="opt in processTypeOpts">
            <a-select-option
              v-if="Number(opt.value)!==3"
              :key="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </template>
        </v-select>
      </a-form-item>
      <a-form-item
        label="访客类型"
        v-bind="validateInfos.visitorTypeId"
      >
        <v-select
          v-model:value="modelRef.visitorTypeId"
          :disabled="!modelRef.companyId"
          placeholder="请选择访客类型"
        >
          <a-select-option
            v-for="opt in visitorTypeOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="流程节点"
        v-bind="validateInfos.processNodes"
      >
        <a-checkbox-group
          v-model:value="processNodes"
          :disabled="modelRef.processType===1"
        >
          <a-checkbox
            v-for="opt in currentProcessNodesOpt"
            :key="opt.value"
            :value="opt.value"
            :disabled="Number(opt.value)===3"
          >
            {{ opt.label }}
          </a-checkbox>
        </a-checkbox-group>
      </a-form-item>
      <!-- <a-form-item
        label="访客类型"
        v-bind="validateInfos.visitorTypeId"
      >
        <v-select
          v-model:value="modelRef.invitorAudit"
          placeholder="请选择访客类型"
        >
          <a-select-option
            v-for="opt in invitorAuditOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item> -->
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
  defineComponent, PropType, computed, toRefs, ref,
} from 'vue';
import { message } from 'ant-design-vue';
import VisitorsManagement, { VisitorProcess } from '~models/visitors-management';
import CompanyMangement from '~models/company-mangement';
import { useVisitorProcess } from '../composition/useForm';
import { invitorAuditOpts } from '../config';

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
    processTypeOpts: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
    processNodesOpts: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { type, visible } = toRefs(props);
    const isReady = ref(false);

    // 弹框展示隐藏
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        // 编辑时初始化接口调用成功后再渲染
        return ['edit'].includes(type.value) ? isReady.value && visible.value : visible.value;
      },
    });
    const { defaultData, processNodesOpts } = toRefs(props);

    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = useVisitorProcess();

    // 流程节点下拉数据
    const currentProcessNodesOptValues = {
      1: [1, 2], // 预约流程
      2: [3, 4], // 邀请流程
    };
    const currentProcessNodesOpt = computed(() => {
      if (!modelRef.processType) return [];
      return Object.values(processNodesOpts.value).filter(({ value }) => currentProcessNodesOptValues[modelRef.processType as 1|2].includes(Number(value)));
    });

    // 流程结单值
    const processNodes = computed({
      set(val:number[]) {
        modelRef.processNodes = val.join(',');
      },
      get() {
        return modelRef.processNodes.split(',').map((val) => Number(val));
      },
    });

    // 切换园区
    const companyOpts = ref<SelectOptions>([]);
    const parkOrgChange = async (orgId:string, clear = true) => {
      companyOpts.value = await new CompanyMangement().listCompanyDict(orgId);
      if (clear) {
        modelRef.companyId = '';
        modelRef.visitorTypeId = '';
      }
    };

    // 切换公司
    const visitorTypeOpts = ref<SelectOptions>([]);
    const companyChange = async (id:string, clear = true) => {
      visitorTypeOpts.value = await visitorsManagementInstance.visitorTypeDic(id);
      if (clear) {
        modelRef.visitorTypeId = '';
      }
    };

    // 切换流程流程类型
    const processTypeChange = (value:number) => {
      processNodes.value = value === 1 ? currentProcessNodesOptValues[1] : [3];
    };

    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'add') {
          await visitorsManagementInstance.visitorProcessAdd(modelRef);
        } else {
          await visitorsManagementInstance.visitorProcessEdit(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };
    // 初始化
    const details = ref<VisitorProcess>();
    // const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      modelRef.id = defaultData.value.id;
      if (type.value === 'edit') {
        details.value = await visitorsManagementInstance.visitorProcessDetails(defaultData.value.id);
        fill(details.value);
        parkOrgChange(modelRef.parkOrgId, false);
        companyChange(modelRef.companyId, false);
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
      // defaultOptions,
      companyChange,
      visitorTypeOpts,
      currentProcessNodesOpt,
      processNodes,
      processTypeChange,
      invitorAuditOpts,
    };
  },
});
</script>
