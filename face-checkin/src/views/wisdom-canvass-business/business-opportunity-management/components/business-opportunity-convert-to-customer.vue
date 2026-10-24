<!--
 * @Description: 新增商机
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 18:10:06
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-02-28 18:24:57
-->
<template>
  <div class="business-opportunity-add">
    <a-form
      :model="modelRef"
      :label-col="{span:4}"
    >
      <a-form-item
        label="客户名称"
        name="name"
        v-bind="validateInfos.name"
      >
        <a-input
          v-model:value="modelRef.name"
          placeholder="请输入客户名称"
          :maxlength="30"
        />
      </a-form-item>
      <a-form-item
        v-bind="validateInfos.parkOrdId"
        label="所属园区"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择所属园区"
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
        label="统一社会信用代码"
        name="code"
        v-bind="validateInfos.code"
      >
        <a-input
          v-model:value="modelRef.code"
          placeholder="请输入统一社会信用代码"
          :maxlength="18"
        />
      </a-form-item>
      <a-form-item
        label="客户类型"
        name="type"
        v-bind="validateInfos.type"
      >
        <v-select
          v-model:value="modelRef.type"
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
      <a-form-item
        label="客户状态"
        name="status"
        v-bind="validateInfos.status"
      >
        <v-select
          v-model:value="modelRef.status"
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
      <a-form-item
        label="联系人"
        name="contacts"
        v-bind="validateInfos.contacts"
      >
        <a-input
          v-model:value="modelRef.contacts"
          placeholder="请输入联系人"
          :maxlength="30"
        />
      </a-form-item>
      <a-form-item
        label="联系电话"
        name="contactsTel"
        v-bind="validateInfos.contactsTel"
      >
        <a-input
          v-model:value="modelRef.contactsTel"
          placeholder="请输入联系电话"
          :maxlength="11"
        />
      </a-form-item>
      <a-form-item
        label="联系人邮箱"
        name="contactsMail"
        v-bind="validateInfos.contactsMail"
      >
        <a-input
          v-model:value="modelRef.contactsMail"
          placeholder="请输入联系人邮箱"
          :maxlength="30"
        />
      </a-form-item>
      <a-form-item
        label="跟进人"
        name="employeeId"
        v-bind="validateInfos.employeeId"
      >
        <v-select-employee
          v-model:value="modelRef.employeeId"
          :default-select-opts="defaultOptions"
          placeholder="请选择跟进人"
          title="选择跟进人"
          job-codes="10"
          mode="single"
          :company-type="0"
          :park-org-ids="modelRef.parkOrgId"
        />
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang='ts'>
import {
  ref, defineComponent, PropType, toRefs,
} from 'vue';
import { BusinessInfo, TurnIntoBusiness } from '~models/wisdom-canvass-business';
import { useParkInfoOpts } from '~composition/park';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { useConvertToCustomer } from '../composition/useFormConf';

export default defineComponent({
  props: {
    orderType: {
      type: Number as PropType<1|2>,
      default: 1,
    },
    defaultData: {
      type: Object as PropType<BusinessInfo>,
      default: () => ({}),
    },
  },
  setup(props) {
    const { defaultData } = toRefs(props);
    const { parkOpts, getParkOpts } = useParkInfoOpts();
    const defaultOptions = ref<SelectOptions>([]);
    const options = useHashMapDics<
    ['COMPANY_TYPE', 'CUSTOMERINFO_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'COMPANY_MANAGEMENT_STATUS']
  >(['COMPANY_TYPE', 'CUSTOMERINFO_STATUS', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'COMPANY_MANAGEMENT_STATUS']);
    const {
      modelRef,
      validateInfos,
      fill,
    } = useConvertToCustomer();
    const { followEmployeeName, followEmployeeId } = defaultData.value;
    if (followEmployeeName && followEmployeeId) {
      defaultOptions.value = [{
        label: defaultData.value.followEmployeeName,
        value: defaultData.value.followEmployeeId,
      }];
    }
    const init = async () => {
      getParkOpts();
      fill(props.defaultData);
    };
    init();
    // onMounted(() => {
    // });
    return {
    //   parkOpts,
      modelRef,
      validateInfos,
      defaultOptions,
      // recordType,
      options,
      parkOpts,
    };
  },
});
</script>
<style lang="scss" scoped>
.business-opportunity-add {
    :deep(.ant-row) {
        display: flex;
        flex-wrap: nowrap;
    }
}
</style>
