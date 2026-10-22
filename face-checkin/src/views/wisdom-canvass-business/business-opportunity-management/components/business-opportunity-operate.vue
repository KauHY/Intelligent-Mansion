<!--
 * @Description: 新增商机
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 18:10:06
 * @LastEditors: your name
 * @LastEditTime: 2022-03-10 17:23:05
-->
<template>
  <div class="business-opportunity-add">
    <a-form
      :model="modelRef"
      :label-col="{span:4}"
    >
      <a-form-item
        v-if="[2].includes(recordType)"

        label="关联园区"
      >
        <span>{{ defaultData.parkOrgName }}</span>
      </a-form-item>
      <!-- <a-form-item
        v-if="[3].includes(recordType)"
        v-bind="validateInfos.companyId"
        label="所属公司"
      >
        <v-select
          v-model:value="modelRef.companyId"
          placeholder="请选择所属公司"
        >
          <a-select-option
            v-for="opt in companyOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item> -->
      <a-form-item
        v-if="[2,7].includes(recordType)"
        v-bind="validateInfos.followEmployeeId"
        label="跟进人"
      >
        <v-select-employee
          v-model:value="modelRef.followEmployeeId"
          :default-select-opts="defaultOptions"
          placeholder="请选择跟进人"
          title="选择跟进人"
          job-codes="10"
          mode="single"
          :company-type="0"
          :park-org-ids="defaultData.parkOrgId"
        />
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang='ts'>
import {
  ref, defineComponent, PropType, toRefs, computed,
} from 'vue';
// import useOpts from '../composition/useOpts';
import { BusinessInfo } from '~models/wisdom-canvass-business';
import CompanyMangement from '~models/company-mangement';
// import employeeSelect from '~components/employee-select';
import { useOperateBussInfo } from '../composition/useFormConf';

export default defineComponent({
  // components: { employeeSelect },
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
    // const {} = useParkInfoOpts();
    const defaultOptions = ref<SelectOptions>([]);
    const recordType = computed(() => defaultData.value.recordType);

    const {
      modelRef,
      validateInfos,
      fill,
    } = useOperateBussInfo();
    const companyOpts = ref<SelectOptions>([]);
    const init = async () => {
      if ([2, 7].includes(recordType.value as number)) { // 分配
        if (defaultData.value.followEmployeeId && defaultData.value.followEmployeeName) {
          defaultOptions.value = [{
            label: defaultData.value.followEmployeeName,
            value: defaultData.value.followEmployeeId,
          }];
        }
      } else if (recordType.value === 3) {
        companyOpts.value = await new CompanyMangement().curUserPropertyCompanyDict();
      }
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
      recordType,
      companyOpts,
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
