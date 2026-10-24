<template>
  <v-modal
    :visible="true"
    :width="900"
    :title="customerConfig.title"
    @cancel="emit('update:show', false)"
  >
    <a-form
      :model="modelRef"
      :label-col="{span:6}"
    >
      <a-form-item
        label="企业名称"
        v-bind="validateInfos.name"
      >
        <a-input
          v-model:value="modelRef.name"
          :maxlength="30"
          placeholder="请输入企业名称"
        />
      </a-form-item>
      <a-form-item
        label="企业类型"
        v-bind="validateInfos.companyType"
      >
        <v-select
          v-model:value="modelRef.companyType"
          placeholder="企业类型"
        >
          <a-select-option
            v-for="opt in $sort(options.COMPANY_TYPE)"
            :key="opt.value"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="统一社会信用代码"
        v-bind="validateInfos.code"
      >
        <a-input
          v-model:value="modelRef.code"
          :maxlength="18"
          :disabled="customerConfig.type==='edit'"
          placeholder="请输入统一社会信用代码"
        />
      </a-form-item>
      <a-form-item
        label="营业执照电子版"
        v-bind="validateInfos.licenses"
      >
        <v-upload
          v-model:value="modelRef.licenses"
          accept=".jpg,.bmp,.png,.gif"
          :size="5"
        />
      </a-form-item>
      <a-form-item
        label="行业领域"
        v-bind="validateInfos.industryField"
      >
        <v-select
          v-model:value="modelRef.industryField"

          placeholder="请选择行业领域"
        >
          <a-select-option
            v-for="opt in options.INDUSTRY_FIELD"
            :key="opt.value"
            :value="opt.value"
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

          placeholder="请选择企业纳税类型"
        >
          <a-select-option
            v-for="opt in options.COMPANY_TAXES_TYPE"
            :key="opt.value"
            :value="opt.value"
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

          placeholder="请选择企业规模"
        >
          <a-select-option
            v-for="opt in options.ENTERPRISE_SIZE
            "
            :key="opt.value"
            :value="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="注册地址"
      >
        <a-input
          v-model:value="modelRef.address"
          :maxlength="30"
          placeholder="请输入地址"
        />
      </a-form-item>
      <a-form-item
        label="附件"
      >
        <v-upload
          v-model:value="modelRef.atts"
          :size="20"
          :count="10"
          accept=".png,.jpg,.jpeg,.gif,.doc,.pdf,.docx,.xls,.xlsx,.ppt,.pptx"
          list-type="text"
          tip="支持pdf/doc/docx/xls/xlsx/ppt/pptx/png/jpg/jpeg/gif格式文件，单个文件大小不超过20M"
          placeholder="请输上传附件"
        >
          <a-button>
            <v-svg name="upload">
              上传
            </v-svg>
          </a-button>
        </v-upload>
      </a-form-item>
      <a-form-item
        label="备注"
        v-bind="validateInfos.remark"
      >
        <a-textarea
          v-model:value="modelRef.remark"
          show-count
          :maxlength="200"
          placeholder="请输入备注"
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button
        type="primary"
        @click="confirm"
      >
        提交审核
      </a-button>
      <a-button
        @click="emit('update:show', false)"
      >
        取消
      </a-button>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, onMounted, toRefs,
} from 'vue';
import { message } from 'ant-design-vue';
import { useHashMapDics } from '~composition/useHashMapDic';
import CompanyMangement, { AddUserParams } from '~models/company';
import { useAddCompany, companyTypeNames } from '../composition/useForm';

const userInstance = new CompanyMangement();
export default defineComponent({
  name: 'OperateDialog',
  components: {},
  props: {
    customerConfig: {
      type: Object,
      default: () => ({
        title: '',
        data: {},
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const { customerData } = toRefs(props.customerConfig);
    const { type } = toRefs(props.customerConfig);
    const options = useHashMapDics<
    ['CUSTOMERINFO_TYPE', 'ENTERPRISE_SIZE', 'COMPANY_TYPE', 'CUSTOMERINFO_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'COMPANY_MANAGEMENT_STATUS']
  >(['CUSTOMERINFO_TYPE', 'ENTERPRISE_SIZE', 'COMPANY_TYPE', 'CUSTOMERINFO_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'COMPANY_MANAGEMENT_STATUS']);
    const {
      modelRef, validate, validateInfos, rulesRef, fill,
    } = useAddCompany(customerData?.value);
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      const params = { ...modelRef };

      validate().then(async () => {
        if (type.value === 'add') {
          await userInstance.update(params as AddUserParams);
        } else {
          await userInstance.modifyInfos(params as AddUserParams);
        }
        message.success('操作成功');
        emit('refresh', {});
      }).catch((error:any) => {
        message.warning('请完成表单的必填项');
      });
    };
    onMounted(() => {
      if (type.value !== 'add') {
        fill();
      }
    });
    return {
      emit,
      confirm,
      modelRef,
      validateInfos,
      companyTypeNames,
      options,
    };
  },
});
</script>
