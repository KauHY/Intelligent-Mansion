<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-03-03 11:20:07
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    class="customer-add-edit"
    :visible="true"
    :width="900"
    :title="customerConfig.title"
    @cancel="emit('update:show', false)"
  >
    <div>
      <a-form
        ref="formRef"
        :model="modelRef"
        :rules="rulesRef"
        :label-col="{ span: 5 }"
      >
        <a-form-item
          label="所属园区"
          name="parkOrgId"
        >
          <v-select
            v-model:value="modelRef.parkOrgId"
            placeholder="请选择所属园区"
            :disabled="formDisabled"
            @change="parkOrgIdChange"
          >
            <a-select-option
              v-for="opt in parkOpts"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>

        <a-form-item
          label="企业名称"
          name="companyId"
        >
          <v-select
            v-model:value="modelRef.companyId"
            filter-option
            disabled
            placeholder="请选择企业名称"
          >
            <a-select-option
              v-for="opt in tagOpts"
              :key="opt.name"
              :value="opt.id"
            >
              {{ opt.name }}
            </a-select-option>
          </v-select>
        </a-form-item>

        <a-form-item
          label="服务名称"
          name="name"
        >
          <a-input
            v-model:value="modelRef.name"
            :maxlength="30"

            :disabled="formDisabled"
            placeholder="请输入服务名称"
          />
        </a-form-item>

        <a-form-item
          label="供应类型"
          name="supplyType"
        >
          <v-select
            v-model:value="modelRef.supplyType"
            filter-option
            :disabled="formDisabled"
            placeholder="请选择供应类型"
          >
            <a-select-option
              v-for="opt in options.SUPPLY_TYPE"
              :key="opt.label"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>

        <a-form-item
          label="供应详情"
          name="detail"
        >
          <v-editor
            ref="text"
            v-model:value="modelRef.detail"
            :disabled="formDisabled"
            placeholder="请输入供应详情"
            @change="changeQuillEditor"
          />
        </a-form-item>

        <a-form-item
          label="跳转类型"
          name="jumpType"
        >
          <v-select
            v-model:value="modelRef.jumpType"
            filter-option
            :disabled="formDisabled"
            placeholder="请选择跳转类型"
          >
            <a-select-option
              v-for="opt in options.JUMP_TYPE"
              :key="opt.label"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>

        <a-form-item
          v-if="modelRef.jumpType !==0 "
          :label="modelRef.jumpType===2?'小程序AppID':'跳转地址'"
          name="url"
        >
          <a-input
            v-model:value="modelRef.url"
            :disabled="formDisabled"
            :maxlength="modelRef.jumpType===2?20:200"
            :placeholder="modelRef.jumpType===2?'请输入小程序AppID':'请输入跳转地址'"
          />
        </a-form-item>

        <a-form-item
          label="联系人"
          name="contact"
        >
          <a-input
            v-model:value="modelRef.contact"
            :disabled="formDisabled"
            :maxlength="30"
            placeholder="请输入联系人"
          />
        </a-form-item>

        <a-form-item
          label="联系电话"
          name="tel"
        >
          <a-input
            v-model:value="modelRef.tel"
            :disabled="formDisabled"
            :maxlength="30"
            placeholder="请输入联系电话"
          />
        </a-form-item>
        <a-form-item
          label="备注"
          name="remark"
        >
          <a-textarea
            v-model:value="modelRef.remark"
            :disabled="formDisabled"
            :maxlength="30"
            placeholder="请输入备注"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <a-button
        v-show="!formDisabled"
        type="primary"
        @click="confirm"
      >
        保存
      </a-button>
      <a-button
        @click="emit('update:show', false)"
      >
        {{ formDisabled ? '关闭':'取消' }}
      </a-button>
    </template>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, PropType, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import CustomerVo from '~models/company';
import User, { UserInfo, UserPwdUpdate } from '~models/user';
import { customerForm } from '../composition/useForm';

const parkInstance = new Park();
export default defineComponent({
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
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<SelectOptions>([]);
    const floorOptions = ref<SelectOptions>([]);
    const { customerData, type } = toRefs(props.customerConfig);
    const { modelRef, rulesRef, fill } = customerForm(customerData?.value);
    const {
      parkOpts, options, tagOpts, defaultOptions, parkOrgIdChange,
    } = useOptions(customerData?.value, modelRef, type.value);
    const CustomerVoReq = new CustomerVo();
    const formDisabled = type.value === 'detail';
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: CustomerVoReq.createSupply,
            edit: CustomerVoReq.modifySupply,
          };
          await apiReq[type.value as 'add' | 'edit']({ ...modelRef });
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const changeQuillEditor = () => {
      if (modelRef.detail) {
        formRef.value.clearValidate('detail');
      }
    };
    const init = async () => {
      if (type.value !== 'add') {
        fill();
      }
    };
    init();
    return {
      changeQuillEditor,
      houseIdOptions,
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      parkOpts,
      tagOpts,
      formDisabled,
      defaultOptions,
      buildingIdOptions,
      floorOptions,
      parkOrgIdChange,
    };
  },
});

function useOptions(customerData?:any, modelRef?:any, type?:any) {
  const parkOpts = ref<SelectOptions>([]);
  const tagOpts = ref<any>([]);
  const defaultOptions = ref<SelectOptions>([]);
  const { code } = window.$localStorage.userInfo as UserInfo;
  const options = useHashMapDics<
    ['SUPPLY_TYPE', 'JUMP_TYPE']
  >(['SUPPLY_TYPE', 'JUMP_TYPE']);
  const getTagOpts = async (value:string) => {
    tagOpts.value = await new CustomerVo().listCompanyPage({
      parkOrgId: value,
      code,
    });
    modelRef.companyId = tagOpts.value[0].id;
  };
  (async () => {
    parkOpts.value = await new CustomerVo().listParkDictByCode(code);
    if (type === 'add') {
      // 新增默认选择园区列表第一个
      modelRef.parkOrgId = parkOpts.value[0].value;
    }
    getTagOpts(modelRef.parkOrgId);
  })();
  const parkOrgIdChange = async (value: string) => {
    modelRef.companyId = '';

    if (customerData) {
      defaultOptions.value = [];
    }
    getTagOpts(value);
  };
  return {
    parkOrgIdChange,
    parkOpts,
    options,
    tagOpts,
    defaultOptions,
    getTagOpts,
  };
}
</script>
<style scoped lang="scss">
.customer-add-edit {
    .cursor {
        cursor: pointer;
    }

    .customer-title {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
        font-size: 18px;
        font-weight: bold;

        span {
            margin-left: 10px;
            font-weight: normal;
            color: var(--ant-primary-color);
            cursor: pointer;
        }
    }
}
</style>
