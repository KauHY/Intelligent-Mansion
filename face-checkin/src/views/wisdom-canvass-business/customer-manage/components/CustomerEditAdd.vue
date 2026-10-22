<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-03-09 17:37:13
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
        <div>
          <h2 class="customer-title">
            基础信息
          </h2>
          <div>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="客户名称"
                  name="name"
                >
                  <a-input
                    v-model:value="modelRef.name"
                    :disabled="formDisabled"
                    placeholder="请输入客户名称"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="所属园区"
                  name="parkOrgId"
                >
                  <v-select
                    v-model:value="modelRef.parkOrgId"
                    :disabled="formDisabled"
                    placeholder="请选择所属园区"
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
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="统一社会信用代码"
                  name="code"
                >
                  <a-input
                    v-model:value="modelRef.code"
                    :disabled="formDisabled"
                    placeholder="请输入统一社会信用代码"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="客户类型"
                  name="type"
                >
                  <v-select
                    v-model:value="modelRef.type"
                    :disabled="formDisabled"
                    placeholder="请选择客户类型"
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
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="客户状态"
                  name="status"
                >
                  <v-select
                    v-model:value="modelRef.status"
                    :disabled="formDisabled"
                    placeholder="请选择客户状态"
                  >
                    <a-select-option
                      v-for="opt in options.CUSTOMERINFO_STATUS"
                      :key="opt.value"
                      :value="opt.value"
                    >
                      {{ opt.label }}
                    </a-select-option>
                  </v-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="联系人"
                  name="contacts"
                >
                  <a-input
                    v-model:value="modelRef.contacts"
                    :disabled="formDisabled"
                    placeholder="请输入联系人"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="联系电话"
                  name="contactsTel"
                >
                  <a-input
                    v-model:value="modelRef.contactsTel"
                    :disabled="formDisabled"
                    placeholder="请输入联系电话"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="联系人邮箱"
                  name="contactsMail"
                >
                  <a-input
                    v-model:value="modelRef.contactsMail"
                    :disabled="formDisabled"
                    :maxlength="30"
                    placeholder="请输入联系人邮箱"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="联系人部门"
                  name="contactsDepartment"
                >
                  <a-input
                    v-model:value="modelRef.contactsDepartment"
                    :disabled="formDisabled"
                    placeholder="请输入联系人部门"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="联系人职位"
                  name="contactsPosition"
                >
                  <a-input
                    v-model:value="modelRef.contactsPosition"
                    :disabled="formDisabled"
                    placeholder="请输入联系人职位"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="客户标签"
                  name="customerTagId"
                >
                  <v-select
                    v-model:value="modelRef.customerTagId"
                    :disabled="formDisabled"
                    placeholder="请选择客户标签"
                  >
                    <a-select-option
                      v-for="opt in tagOpts"
                      :key="opt.value"
                      :value="opt.value"
                    >
                      {{ opt.label }}
                    </a-select-option>
                  </v-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="跟进人"
                  name="employeeId"
                >
                  <v-select-employee
                    v-model:value="modelRef.employeeId"
                    :default-select-opts="defaultOptions"
                    placeholder="请选择跟进人"
                    :disabled="formDisabled"
                    title="选择跟进人"
                    job-codes="10"
                    mode="single"
                    :company-type="0"
                    :park-org-ids="modelRef.parkOrgId"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="备注"
                  name="remark"
                >
                  <a-input
                    v-model:value="modelRef.remark"
                    :disabled="formDisabled"
                    :maxlength="30"
                    placeholder="请输入备注"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </div>
        </div>
        <div style="text-align: center;">
          <a-button
            type="primary"
            :disabled="formDisabled"
            @click="addContact"
          >
            <template #icon>
              <v-svg
                class="svg-icon"
                name="add-select"
                style="vertical-align: top !important;"
              />
            </template>
            添加业务联系人
          </a-button>
        </div>
        <div>
          <div
            v-for="(item, index) in modelRef.businessContactsList"
            :key="index"
          >
            <h2 class="customer-title">
              业务联系人{{ index + 1 }} <span
                v-show="!formDisabled"
                @click="delContacts(index)"
              >(删除)</span>
            </h2>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="联系人姓名"
                  :name="['businessContactsList', index, 'contacts']"
                >
                  <a-input
                    v-model:value="item.contacts"
                    :disabled="formDisabled"
                    placeholder="请输入联系人姓名"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="联系电话"
                  :name="['businessContactsList', index, 'tel']"
                  :rules="rulesRef.tel"
                >
                  <a-input
                    v-model:value="item.tel"
                    :disabled="formDisabled"
                    placeholder="请输入联系电话"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="联系人邮箱"
                  :name="['businessContactsList', index, 'mail']"
                  :rules="rulesRef.mail"
                >
                  <a-input
                    v-model:value="item.mail"
                    :disabled="formDisabled"
                    :maxlength="30"
                    placeholder="请输入联系人邮箱"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="联系人职位"
                  :name="['businessContactsList', index, 'position']"
                >
                  <a-input
                    v-model:value="item.position"
                    :disabled="formDisabled"
                    placeholder="请输入联系人职位"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="联系人部门"
                  :name="['businessContactsList', index, 'department']"
                >
                  <a-input
                    v-model:value="item.department"
                    :disabled="formDisabled"
                    placeholder="请输入联系人部门"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="备注"
                  :name="['businessContactsList', index, 'remark']"
                >
                  <a-input
                    v-model:value="item.remark"
                    :disabled="formDisabled"
                    placeholder="请输入备注"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </div>
        </div>
        <div>
          <h2 class="customer-title">
            企业信息
          </h2>
          <div>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="行业领域"
                  name="industryField"
                >
                  <v-select
                    v-model:value="modelRef.industryField"
                    :disabled="formDisabled"
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
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="企业规模"
                  name="companyScale"
                >
                  <a-input
                    v-model:value="modelRef.companyScale"
                    :disabled="formDisabled"
                    placeholder="请输入企业人数"
                    :maxlength="5"
                    style="width: 100%;"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="法人代表"
                  name="legalRepresentative"
                >
                  <a-input
                    v-model:value="modelRef.legalRepresentative"
                    :disabled="formDisabled"
                    placeholder="请输入法人代表"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="企业邮箱"
                  name="companyMail"
                  :rules="rulesRef.companyMail"
                >
                  <a-input
                    v-model:value="modelRef.companyMail"
                    :disabled="formDisabled"
                    :maxlength="30"
                    placeholder="请输入企业邮箱"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="企业纳税类型"
                  name="companyTaxesType"
                >
                  <v-select
                    v-model:value="modelRef.companyTaxesType"
                    :disabled="formDisabled"
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
              </a-col>
            </a-row>
          </div>
        </div>
        <div>
          <h2 class="customer-title">
            工商信息
          </h2>
          <div>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="企业经营状态"
                  name="companyManagementStatus"
                >
                  <v-select
                    v-model:value="modelRef.companyManagementStatus"
                    :disabled="formDisabled"
                    placeholder="请选择企业经营状态"
                  >
                    <a-select-option
                      v-for="opt in options.COMPANY_MANAGEMENT_STATUS"
                      :key="opt.value"
                      :value="opt.value"
                    >
                      {{ opt.label }}
                    </a-select-option>
                  </v-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="地址"
                  name="address"
                >
                  <a-input
                    v-model:value="modelRef.address"
                    :disabled="formDisabled"
                    placeholder="请输入企业地址"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="注册资本"
                  name="registeredCapital"
                >
                  <a-input
                    v-model:value="modelRef.registeredCapital"
                    :disabled="formDisabled"
                    :maxlength="5"
                    placeholder="请输入注册资本"
                    style="width: 100%;"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="营业期限"
                  name="businessTerm"
                >
                  <a-input
                    v-model:value="modelRef.businessTerm"
                    :disabled="formDisabled"
                    :maxlength="5"
                    placeholder="请输入营业期限"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </div>
        </div>
      </a-form>
    </div>
    <template #footer>
      <a-button
        @click="emit('update:show', false)"
      >
        {{ formDisabled ? '关闭':'取消' }}
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

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, PropType, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { string } from 'mathjs';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import CustomerVo, { CustomerInfoKind, BusinessContactsListKind } from '~models/customer-manage';
import { customerForm } from '../composition/useForm';

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
    const { modelRef, rulesRef } = customerForm();
    const { customerData, type } = toRefs(props.customerConfig);
    const {
      parkOpts, options, parkOrgIdChange, tagOpts, defaultOptions, getTagOpts,
    } = useOptions(customerData?.value, modelRef, type.value);
    const CustomerVoReq = new CustomerVo();
    const formDisabled = type.value === 'detail';
    const fill = () => {
      Object.entries(modelRef).forEach(([key]) => {
        if ((customerData.value as any)[key] || (customerData.value as any)[key] === 0) {
          const formData = modelRef as any;
          if (key === 'parkOrgNames') {
            formData.parkOrgIds = customerData.value[key].map((item: any) => item.value);
          } else {
            formData[key] = customerData.value[key];
          }
        }
      });
    };
    const formRef = ref();
    const confirm = () => {
      let { businessContactsList } = modelRef;
      businessContactsList = businessContactsList.filter((item:any) => Object.keys(item).some((sub:string) => item[sub]));
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: CustomerVoReq.customerCreateApi,
            edit: CustomerVoReq.customerModifyApi,
          };
          await apiReq[type.value as 'add' | 'edit']({ ...modelRef, businessContactsList });
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {
      if (type.value !== 'add') {
        fill();
        getTagOpts(modelRef.parkOrgId);
      }
    };
    const delContacts = async (index: number) => {
      modelRef.businessContactsList.splice(index);
    };
    const addContact = async () => {
      if (modelRef.businessContactsList.length > 4) {
        message.warning('最多添加5个业务联系人');
        return;
      }
      modelRef.businessContactsList.push({
        contacts: '', // 联系人
        department: '', // 联系人部门
        mail: '', // 联系人邮箱
        position: '', // 联系人职位
        remark: '', // 备注
        tel: '', // 联系电话
      });
    };
    init();
    return {
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      parkOpts,
      parkOrgIdChange,
      tagOpts,
      formDisabled,
      addContact,
      delContacts,
      defaultOptions,
    };
  },
});

function useOptions(customerData?:any, modelRef?:any, type?:any) {
  const parkOpts = ref<SelectOptions>([]);
  const tagOpts = ref<SelectOptions>([]);
  const defaultOptions = ref<SelectOptions>([]);
  const options = useHashMapDics<
    ['COMPANY_TYPE', 'CUSTOMERINFO_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'COMPANY_MANAGEMENT_STATUS']
  >(['COMPANY_TYPE', 'CUSTOMERINFO_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'COMPANY_MANAGEMENT_STATUS']);
  const getTagOpts = async (value:string) => {
    tagOpts.value = await new CustomerVo().listCustomerTagDictApi(value);
  };
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
    if (type === 'add') {
      // 新增默认选择园区列表第一个
      modelRef.parkOrgId = parkOpts.value[0].value;
      getTagOpts(modelRef.parkOrgId);
    }
  })();
  // 编辑或详情显示跟进人
  if (customerData?.employeeId) {
    const { employeeName, employeeId } = customerData;
    defaultOptions.value = [{
      label: employeeName,
      value: employeeId,
    }];
  }
  const parkOrgIdChange = async (value: string) => {
    modelRef.employeeId = '';
    modelRef.employeeIdName = '';
    defaultOptions.value = [];
    if (!value) {
      modelRef.customerTagId = '';
    }
    getTagOpts(value);
  };
  return {
    parkOpts,
    options,
    tagOpts,
    parkOrgIdChange,
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
