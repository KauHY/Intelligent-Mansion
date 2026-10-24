<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-27 17:57:33
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-27 18:59:07
-->
<template>
  <a-form
    :model="modelRef"
    :label-col="{span:5}"
  >
    <div class="form-item-title">
      访客信息
    </div>
    <div class="form-item-box">
      <a-form-item
        label="访客姓名"
        v-bind="validateInfos.visitorName"
      >
        <a-input
          v-model:value="modelRef.visitorName"
          :maxlength="20"
          placeholder="请输入访客姓名"
        />
      </a-form-item>
      <a-form-item
        label="访客身份证号码"
        v-bind="validateInfos.visitorIdCard"
      >
        <a-input
          v-model:value="modelRef.visitorIdCard"
          :maxlength="20"
          placeholder="请输入访客身份证号码"
        />
      </a-form-item>
      <a-form-item
        label="访客手机号码"
        v-bind="validateInfos.visitorTel"
      >
        <a-input
          v-model:value="modelRef.visitorTel"
          :maxlength="20"
          placeholder="请输入访客手机号码"
        />
      </a-form-item>
      <a-form-item
        label="车牌号"
      >
        <span>{{ modelRef.carNum ? modelRef.carNum:'--' }}</span>
        <!-- <a-input
          v-model:value="modelRef.carNum"
          :maxlength="20"
          disabled
          placeholder="请输入访客车牌号"
        /> -->
      </a-form-item>
    </div>
    <div class="form-item-title">
      被访者信息
    </div>
    <div class="form-item-box">
      <a-form-item
        label="所属园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择所属园区"
          container-body
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
        label="所属企业"
        v-bind="validateInfos.companyId"
      >
        <v-select
          v-model:value="modelRef.companyId"
          placeholder="请选择企业"
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
        label="被访者姓名"
        v-bind="validateInfos.invitorName"
      >
        <a-input
          v-model:value="modelRef.invitorName"
          :maxlength="20"
          placeholder="请输入被访者姓名"
        />
      </a-form-item>
      <a-form-item
        label="被访者手机号"
        v-bind="validateInfos.visitorName"
      >
        <a-input
          v-model:value="modelRef.invitorTel"
          :maxlength="20"
          placeholder="请输入被访者手机号"
        />
      </a-form-item>
      <!-- TODO:后台接口暂不支持 -->
      <a-form-item
        label="访问区域"
        v-bind="validateInfos.accessPolicyId"
      >
        <v-select
          v-model:value="modelRef.accessPolicyId"
          placeholder="请选择访问区域"
        >
          <a-select-option
            v-for="opt in PersonPassOpts"
            :key="opt.id"
          >
            {{ opt.policyName }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="来访事由"
        v-bind="validateInfos.visitReason"
      >
        <v-select
          v-model:value="modelRef.visitReason"
          placeholder="请选择来访事由"
        >
          <a-select-option
            v-for="opt in visitReasonOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="来访人数"
        v-bind="validateInfos.visitorCount"
      >
        <a-input
          v-model:value="modelRef.visitorCount"
          placeholder="请输入来访人数"
        />
      </a-form-item>
      <a-form-item
        label="备注"
        v-bind="validateInfos.visitorRemark"
      >
        <a-input
          v-model:value="modelRef.visitorRemark"
          placeholder="请输入备注"
        />
      </a-form-item>
    </div>
  </a-form>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, onMounted, toRefs,
} from 'vue';
import PersonPass, { AccessPolicy } from '~models/person-pass';
import Park from '~models/park';
import { useRegister } from '../composition/useForm';

const personPassInstance = new PersonPass();
export default defineComponent({
  name: '',
  components: {},
  props: {
    visitReasonOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    details: {
      type: Object,
      default: () => ({}),
    },
  },
  setup(props) {
    const { details } = toRefs(props);
    const PersonPassOpts = ref<any>([]);
    const { parkOpts, parkOrgIdChange, companyOpts } = useOptions(details.value.parkOrgId);
    const {
      modelRef,
      //   validate,
      validateInfos,
      fill,
    } = useRegister();
    onMounted(async () => {
      fill(details.value);
      const { list } = await personPassInstance.accessPolicyListPageAppointment({
        companyId: details.value.companyId,
        parkOrgId: details.value.parkOrgId,
      });
      PersonPassOpts.value = list;
    });
    return {
      PersonPassOpts,
      parkOpts,
      companyOpts,
      parkOrgIdChange,
      modelRef,
      validateInfos,
    };
  },
});
function useOptions(parkOrgId:string) {
  const parkOpts = ref<SelectOptions>([]);
  const companyOpts = ref<SelectOptions>([]);
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
    companyOpts.value = await new Park().listCompanyDictByType(parkOrgId);
  })();
  // 园区切换
  const parkOrgIdChange = async (value: string) => {
    companyOpts.value = await new Park().listCompanyDictByType(value);
  };
  return {
    parkOpts,
    parkOrgIdChange,
    companyOpts,
  };
}
</script>
