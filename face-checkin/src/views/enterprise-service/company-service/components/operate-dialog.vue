<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 17:22:43
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
        label="服务名称"
        v-bind="validateInfos.name"
      >
        <a-input
          v-model:value="modelRef.name"
          :disabled="type==='details'"
          placeholder="请输入服务名称"
          :maxlength="30"
        />
      </a-form-item>

      <a-form-item
        label="推送范围"
        v-bind="validateInfos.pushRange"
      >
        <v-select
          v-model:value="modelRef.pushRange"
          :disabled="type==='details'"
          placeholder="请选择推送范围"
          mode="multiple"
          option-filter-prop="label"
          show-search
        >
          <a-select-option
            v-for="opt in parkPop"
            :key="opt.value"
            :label="opt.label"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>

      <a-form-item
        label="服务分组"
        v-bind="validateInfos.groupType"
      >
        <v-select
          v-model:value="modelRef.groupType"
          placeholder="请选择服务分组"
          :disabled="type==='details'"
        >
          <a-select-option
            v-for="opt in parkOpts.SERVICE_GROUPING"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
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
      <!-- <a-form-item
        class="serviceLabel"
        label="服务设置"
      /> -->

      <a-form-item label="服务摘要">
        <a-textarea
          v-model:value="modelRef.summary"
          placeholder="请输入服务摘要"
          :rows="4"
          show-count
          :maxlength="200"
          :disabled="type==='details'"
        />
      </a-form-item>
      <a-form-item
        label="服务详情"
        v-bind="validateInfos.detail"
      >
        <a-textarea
          v-model:value="modelRef.detail"
          placeholder="请输入服务详情"
          :rows="4"
          show-count
          :disabled="type==='details'"
        />
      </a-form-item>

      <a-form-item
        label="服务链接"
        v-bind="validateInfos.url"
      >
        <a-input
          v-model:value="modelRef.url"
          :disabled="type==='details'"
          placeholder="请输入服务链接"
        />
      </a-form-item>
      <a-form-item
        label="咨询电话"
        v-bind="validateInfos.tel"
        :rules="{
          required: modelRef.tel?true:false,
        }"
      >
        <a-input
          v-model:value="modelRef.tel"
          :disabled="type==='details'"
          :maxlength="12"
          placeholder="请输入咨询电话"
        />
      </a-form-item>

      <a-form-item label="banner图">
        <v-upload
          v-model:value="modelRef.banner"
          :multiple="true"
          :size="1"
          :count="3"
          :disabled="type==='details'"
          accept=".png,.jpg,.jpeg,.gif,.bmp,"
          tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸1125*844PX"
        />
      </a-form-item>
      <a-form-item label="附件">
        <v-upload
          v-model:value="modelRef.att"
          :multiple="true"
          :size="20"
          :disabled="type==='details'"
          :count="10"
          :list-type="'text'"
          accept="image/*,.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.png,.jpg,.jpeg,.gif,.zip"
          tip="仅支持以下格式文件，pdf/doc/docx/xls/xlsx/ppt/pptx/png/jpg/jpeg/gif/zip，文件大小不超过20M。"
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
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watch,
} from 'vue';
import { message } from 'ant-design-vue';
import { REGS } from '@/constants/validate';
import EnterpriseService, { LaneInfo } from '~models/company-service';
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
    const companyPop = ref<SelectOptionsMap>([]);
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
        if (type.value === 'add') {
          console.log('添加', type.value);
          await serviceRequest.addCompanyService(modelRef);
        } else if (type.value === 'edit') {
          await serviceRequest.companyServiceModify(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error: any) => {
        message.warning('操作失败');
      });
    };

    const parkingLotOpts = ref<SelectOptionsMap>([]);
    const parkOrgChange = async (orgId: string) => {
      console.log('orgId', orgId);
      parkingLotOpts.value = await serviceRequest.companyName(orgId);
      console.log('parkingLotOpts', parkingLotOpts.value);
    };
    const filterOption = (input:any, option:any) => {
      console.log('optin', option, 'input', input);

      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      );
    };

    // 初始化
    const details = ref();
    const init = async () => {
      if (type.value === 'edit' || type.value === 'details') {
        details.value = await serviceRequest.companyQuery(defaultData.value.id);
        console.log('details', details.value);

        fill(details.value);
        // parkOrgChange(defaultData.value.parkOrgId);
        modelRef.groupType = defaultData.value.groupType;
        isReady.value = true;
      }
      console.log('表单值', modelRef);
      console.log('下拉值', parkOpts);
    };
    init();
    return {
      currentVisible,
      details,
      confirm,
      modelRef,
      validateInfos,
      parkOrgChange,
      companyPop,
      parkingLotOpts,
      filterOption,
    };
  },
});
</script>
<style lang="less" scoped>
:deep(.serviceLabel .ant-form-item-label > label::after) {
    display: none;
}

:deep(.serviceLabel .ant-form-item-label>label) {
    margin-right: 16px;
    font-size: 18px;
    font-weight: bold;
}
</style>
