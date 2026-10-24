<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-21 10:45:20
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
      :label-col="{ span: 5 }"
    >
      <a-form-item
        label="所属园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择所属园区"
          disabled
          @change="parkOrgChange"
        >
          <a-select-option
            v-for="opt in parkPop"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="企业名称"
        v-bind="validateInfos.name"
      >
        <a-input
          v-model:value="modelRef.name"
          disabled
        />
      </a-form-item>

      <a-form-item
        label="企业类型"
        v-bind="validateInfos.companyType"
      >
        <v-select
          v-model:value="modelRef.companyType"
          placeholder="请选择企业类型"
          disabled
          @change="parkOrgChange"
        >
          <a-select-option
            v-for="opt in parkOpts.COMPANY_TYPE"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="企业领域"
        v-bind="validateInfos.industryField"
      >
        <v-select
          v-model:value="modelRef.industryField"
          placeholder="请选择企业领域"
          :disabled="type==='details'"
          @change="parkOrgChange"
        >
          <a-select-option
            v-for="opt in parkOpts.INDUSTRY_FIELD"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="企业纳税类型"
        v-bind="validateInfos.companyTaxesType"
      >
        <v-select
          v-model:value="modelRef.companyTaxesType"
          :disabled="type==='details'"
          placeholder="请选择企业纳税类型"
        >
          <a-select-option
            v-for="opt in parkOpts.COMPANY_TAXES_TYPE"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="企业税收（万元）"
        v-bind="validateInfos.corporateTax"
      >
        <a-input
          v-model:value="modelRef.corporateTax"
          :disabled="type==='details'"
          :maxlength="8"
          placeholder="请输入企业税收(万元)"
        />
      </a-form-item>
      <a-form-item
        label="企业规模"
        v-bind="validateInfos.enterpriseSize"
      >
        <v-select
          v-model:value="modelRef.enterpriseSize"
          :disabled="type==='details'"
          placeholder="请选择企业规模"
        >
          <a-select-option
            v-for="opt in parkOpts.ENTERPRISE_SIZE"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="附件"
      >
        <v-upload
          v-model:value="modelRef.atts"
          :multiple="true"
          :size="20"
          :disabled="type==='details'"
          :count="10"
          :list-type="'text'"
          accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.png,.jpg,.jpeg,.gif,.zip"
          tip="仅支持以下格式文件，pdf/doc/docx/xls/xlsx/ppt/pptx/png/jpg/jpeg/gif/zip，文件大小不超过20M"
        />
        <!-- </div> -->
      </a-form-item>
      <a-form-item label="备注">
        <a-textarea
          v-model:value="modelRef.remark"
          placeholder="请输入备注"
          :rows="4"
          show-count
          :maxlength="200"
          :disabled="type==='details'"
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
import EnterpriseService, { LaneInfo } from '~models/enterprise-service';
import CompanyMangement from '~models/company-mangement';
import { useLaneAdd } from '../composition/useForm';

const serviceRequest = new EnterpriseService();
// const carPassInstance = new CarPass();
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
      type: Object,
      default: () => ({}),
    },
    parkPop: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
    // parkingLotOpts: {
    //   type: Object as PropType<SelectOptionsMap>,
    //   default: () => ({}),
    // },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { parkOpts, type, visible } = toRefs(props);
    const isReady = ref(false);
    const { defaultData } = toRefs(props);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return ['edit'].includes(type.value) ? isReady.value && visible.value : visible.value;
      },
    });

    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = useLaneAdd();
    const confirm = () => {
      validate().then(async () => {
        console.log('添加', modelRef, validate, validateInfos, fill);
        await serviceRequest.savePerfectMsg(modelRef);

        // if (type.value === 'add') {
        //   // await serviceRequest.laneAdd(modelRef);
        // } else {
        //   // await serviceRequest.laneEdit(modelRef);
        // }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error: any) => {
        message.warning('操作失败');
        console.log('操作失败', error);
      });
    };

    const parkingLotOpts = ref<SelectOptions>([]);
    const parkOrgChange = async (orgId: string) => {
      // if (!orgId) return;
      // parkingLotOpts.value = await carPassInstance.parkingLotDic(orgId);
    };

    // 初始化
    const details = ref();
    const init = async () => {
      details.value = await serviceRequest.queryDetails(defaultData.value.id);
      fill(details.value);
      modelRef.companyType = defaultData.value.companyType;
    };
    isReady.value = true;
    init();
    return {
      currentVisible,
      details,
      confirm,
      modelRef,
      validateInfos,
      parkOrgChange,
      parkingLotOpts,
    };
  },
});
</script>
