<template>
  <v-modal
    :visible="true"
    :width="900"
    :title="customerConfig.title"
    @cancel="emit('update:show', false)"
  >
    <a-form
      ref="formRef"
      :model="modelRef"
      :label-col="{ span: 5 }"
    >
      <a-form-item
        label="所属园区"
      >
        <span>{{ modelRef.parkOrgName||'-' }}</span>
      </a-form-item>
      <a-form-item
        label="入驻企业"
      >
        <span>{{ modelRef.companyName||'-' }}</span>
      </a-form-item>

      <a-form-item
        label="结算合同"
      >
        <span>{{ modelRef.contractNo }}</span>
      </a-form-item>

      <a-form-item
        label="结算日期"
      >
        <span>{{ modelRef.leaveDate||'-' }}</span>
      </a-form-item>

      <a-form-item
        v-if="contractType!==4"
        label="押金"
      >
        <span>{{ modelRef.deposit||'-' }}</span>
      </a-form-item>

      <a-form-item
        label="提交时间"
      >
        <span>{{ modelRef.createTime||'-' }}</span>
      </a-form-item>

      <a-form-item
        label="操作人"
      >
        <span>{{ modelRef.createByName||'-' }}</span>
      </a-form-item>

      <a-form-item
        label="备注"
      >
        <span>{{ modelRef.remark||'-' }}</span>
      </a-form-item>
    </a-form>
    <v-table
      row-key="id"
      bordered
      :columns="columns"
      :data-source="modelRef.list"
    />
    <div style="text-align: right;">
      实收总金额（元）<span>{{ modelRef.totalAmount }}</span>
    </div>
    <template #footer>
      <a-button
        type="primary"
        @click="emit('update:show', false)"
      >
        取消
      </a-button>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, onMounted, toRefs, ref, PropType,
} from 'vue';
import { message } from 'ant-design-vue';
import { useHashMapDics } from '~composition/useHashMapDic';
import CompanyMangement, { AddUserParams } from '~models/company';
import { useAddCompany } from '../composition/useForm';
import { getDetailColumns } from '../config';

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
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5>,
      default: 3,
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const columns = getDetailColumns();
    const { customerData } = toRefs(props.customerConfig);
    const houseList = ref<SelectOptions>([]);
    const { type } = toRefs(props.customerConfig);
    const {
      modelRef, validate, validateInfos, fill,
    } = useAddCompany(customerData?.value);
    onMounted(() => {
      if (type.value !== 'add') {
        fill();
      }
    });
    return {
      getDetailColumns,
      columns,
      houseList,
      emit,
      modelRef,
      validateInfos,
    };
  },
});
</script>
