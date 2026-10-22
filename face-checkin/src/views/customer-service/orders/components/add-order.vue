<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 18:10:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 18:14:19
-->
<template>
  <a-form
    :model="modelRef"
    :label-col="{span:4}"
  >
    <a-form-item
      label="反馈人"
      v-bind="validateInfos.orderOwnerName"
    >
      <a-input
        v-model:value="modelRef.orderOwnerName"
        :maxlength="20"
        placeholder="请输入反馈人姓名"
      />
    </a-form-item>
    <a-form-item
      label="手机号"
      v-bind="validateInfos.orderOwnerMobile"
    >
      <a-input
        v-model:value="modelRef.orderOwnerMobile"
        placeholder="请输入反馈人手机号"
      />
    </a-form-item>
    <a-form-item
      label="地址"
      class="address-info"
    >
      <a-form-item
        class="park"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择关联园区"
          style="width: 120px;"
        >
          <a-select-option
            v-for="opt in parkOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      &nbsp;
      &nbsp;
      <a-form-item
        class="address"
        v-bind="validateInfos.orderAddress"
      >
        <a-input
          v-model:value="modelRef.orderAddress"
          placeholder="请输入补充详细地址"
        />
      </a-form-item>
    </a-form-item>

    <a-form-item
      label="类型"
      v-bind="validateInfos.orderTypeItem"
    >
      <v-select
        v-model:value="modelRef.orderTypeItem"
        placeholder="请选择类型"
      >
        <a-select-option
          v-for="opt in orderTypeOpts"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item
      label="照片"
      v-bind="validateInfos.orderUrl"
    >
      <!-- <a-input v-model:value="modelRef.orderUrl" /> -->
      <v-upload
        v-model:value="modelRef.orderUrl"
        accept=".jpg,.bmp,.png,.gif"
        :count="3"
      />
    </a-form-item>
    <a-form-item
      :label="`${orderType===1?'反馈':'报修'}内容`"
      v-bind="validateInfos.orderContent"
    >
      <v-editor
        v-model:value="modelRef.orderContent"
        output-format="text"
        :length="1000"
      />
    </a-form-item>
  </a-form>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, computed,
} from 'vue';
// import useOpts from '../composition/useOpts';
import { useParkInfoOpts } from '~composition/park';
import { useAddOrder } from '../composition/useForm';
import { orderTypeRepair, orderTypeComplaint } from '../config';

export default defineComponent({
  name: '',
  components: {},
  props: {
    orderType: {
      type: Number as PropType<1|2>,
      default: 1,
    },
  },
  setup(props) {
    const { orderType } = toRefs(props);
    const orderTypeOpts = computed(() => Object.values(orderType.value === 1 ? orderTypeComplaint : orderTypeRepair));
    const { parkOpts, getParkOpts } = useParkInfoOpts();
    getParkOpts();
    const {
      modelRef,
      validateInfos,
    } = useAddOrder();
    return {
      parkOpts,
      modelRef,
      validateInfos,
      orderTypeOpts,
    };
  },
});
</script>
<style lang="scss" scoped>
.address-info {
    :deep(.ant-form-item-control) {
        margin-right: 10px;

        .ant-form-item-control-input-content {
            display: flex;
            align-items: center;
            justify-content: flex-start;
        }
    }

    :deep(.ant-form-item-label>label::before) {
        display: inline-block;
        margin-right: 4px;
        font-family: SimSun, sans-serif;
        font-size: 14px;
        line-height: 1;
        color: #ff4d4f;
        content: "*";
    }
}
</style>
