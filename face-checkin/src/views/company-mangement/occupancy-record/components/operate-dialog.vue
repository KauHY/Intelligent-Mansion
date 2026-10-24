<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-27 20:42:16
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
    class="operate-dialog"
    @ok="confirm"
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
      >
        <span>{{ defaultData.companyName }}</span>
      </a-form-item>
      <a-form-item
        label="关联园区"
      >
        <span>{{ defaultData.parkOrgName }}</span>
      </a-form-item>
      <a-form-item
        label="关联楼栋单元"
      >
        <span>{{ defaultData.buildingName }}</span>
      </a-form-item>
      <a-form-item
        label="关联楼层房号"
      >
        <span>{{ defaultData.houseName }}</span>
      </a-form-item>
      <a-form-item
        label="起租时间"
      >
        <span>{{ defaultData.rentStartTime }}</span>
      </a-form-item>
      <a-form-item
        label="到期时间"
      >
        <span>{{ defaultData.rentEndTime }}</span>
      </a-form-item>
      <a-form-item
        label="续租到期时间"
        v-bind="validateInfos.rentRenewEndTime"
      >
        <a-date-picker
          v-model:value="modelRef.rentRenewEndTime"
          placeholder="续租到期时间"
          :disabled-date="disabledDate"
          value-format="YYYY-MM-DD"
        />
      </a-form-item>
    </a-form>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs,
} from 'vue';
import { message } from 'ant-design-vue';
// import addUser from './add-component.vue';
// import resetPwd from './reset-pwd.vue';
import { Dayjs } from 'dayjs';
import CompanyMangement, { HouseBindRecord, RenewHouse } from '~models/company-mangement';
import { useRentRenew, companyTypeNames } from '../config';

const companyMangementInstance = new CompanyMangement();
export default defineComponent({
  name: 'OperateDialog',
  components: {},
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<HouseBindRecord>,
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
    const { defaultData } = toRefs(props);
    const {
      modelRef, validate, validateInfos, rulesRef, fill,
    } = useRentRenew(defaultData.value);
    const disabledDate = (date:Dayjs) => {
      // console.log(currentDate, '---currentDate');
      const endTimes = new Date(defaultData.value.rentEndTime).getTime() + 24 * 3600 * 1000;
      console.log(endTimes, date.valueOf(), '----endTimes');
      return date.valueOf() < endTimes;
    };
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
        await companyMangementInstance.renewHouse(params as RenewHouse);
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
      disabledDate,
    };
  },
});
</script>
