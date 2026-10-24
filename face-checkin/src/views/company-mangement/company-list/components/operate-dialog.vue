<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-29 15:53:11
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
    class="operate-dialog"
  >
    <!-- <component
      :is="contentCp"
      :type="type"
      :park-opts="parkOpts"
      :default-form-data="defaultData"
    /> -->
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
          :disabled="formDisabled"
          :maxlength="30"
          placeholder="请输入企业名称"
        />
      </a-form-item>
      <a-form-item
        label="企业类型"
        v-bind="validateInfos.companyType"
      >
        <a-select
          v-model:value="modelRef.companyType"
          :disabled="formDisabled"
        >
          <a-select-option
            v-for="opt in $sort(options.COMPANY_TYPE)"
            :key="opt.value"
            :value="Number(opt.value)"
          >
            {{ opt.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="关联园区"
        v-bind="validateInfos.parkOrgId"
      >
        <a-select
          v-model:value="modelRef.parkOrgId"
          :disabled="formDisabled || type=== 'edit'"
          placeholder="请选择关联园区"
        >
          <a-select-option
            v-for="opt in parkOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="统一社会信用代码"
        v-bind="validateInfos.code"
      >
        <a-input
          v-model:value="modelRef.code"
          :disabled="formDisabled"
          placeholder="请输入统一社会信用代码"
        />
      </a-form-item>
      <a-form-item
        label="营业执照电子版"
        v-bind="validateInfos.licenses"
      >
        <v-upload
          v-model:value="modelRef.licenses"
          :disabled="formDisabled"
          accept=".jpg,.bmp,.png"
          tip="需加盖公章扫描后上传，支持jpg、bmp、png格式文件"
          :size="5"
        />
      </a-form-item>
      <a-form-item
        label="注册地址"
      >
        <a-input
          v-model:value="modelRef.address"
          :disabled="formDisabled"
          :maxlength="30"
          placeholder="请输入注册地址"
        />
      </a-form-item>
      <a-form-item
        label="联系人"
      >
        <a-input
          v-model:value="modelRef.contacts"
          :disabled="formDisabled"
          :maxlength="30"
          placeholder="请输入联系人"
        />
      </a-form-item>
      <a-form-item
        label="联系人电话"
        v-bind="validateInfos.tel"
      >
        <a-input
          v-model:value="modelRef.tel"
          :disabled="formDisabled"
          placeholder="请输入联系人电话"
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button @click="currentVisible=false">
        {{ formDisabled ? "关闭" : "取消" }}
      </a-button>
      <a-button
        v-show="!formDisabled"
        type="primary"
        @click="confirm"
      >
        确定
      </a-button>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs,
} from 'vue';
import { message } from 'ant-design-vue';
// import addUser from './add-component.vue';
// import resetPwd from './reset-pwd.vue';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { useAddCompany, companyTypeNames } from '../config';

const userInstance = new CompanyMangement();
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
    parkOpts: {
      type: Array as PropType<SelectOptions>,
      default: () => ([]),
    },

  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    const { defaultData, type } = toRefs(props);
    const formDisabled = type.value === 'detail';
    const options = useHashMapDics<['COMPANY_TYPE']>(['COMPANY_TYPE']);
    const {
      modelRef, validate, validateInfos, rulesRef, fill,
    } = useAddCompany(defaultData.value);

    // const contentCp = props.type === 'resetPwd' ? resetPwd : addUser;
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      const { type } = props;
      // const form = props.type === 'resetPwd' ? useResetPwdForm() : useAddCompany();
      const params = { ...modelRef };

      validate().then(async () => {
        // if (type === 'bind') {
        //   return;
        // }
        // if (type === 'add') {
        await userInstance.companySave(params as CompanyInfo);
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };
    onMounted(() => {
      fill();
    });
    return {
      currentVisible,
      confirm,
      modelRef,
      validateInfos,
      companyTypeNames,
      formDisabled,
      options,
    };
  },
});
</script>
