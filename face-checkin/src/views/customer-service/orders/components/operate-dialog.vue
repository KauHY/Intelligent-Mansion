<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-05 15:04:12
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="type==='details'?920:720"
    class="operate-dialog"
    :cancel-text="type==='details'?'关闭':'取消'"
  >
    <component
      :is="cp"
      :default-data="defaultData"
      :order-type="orderType"
    />
    <template #footer>
      <div class="btns">
        <a-button
          v-if="type!=='details'"
          type="primary"
          @click="confirm"
        >
          确认
        </a-button>
        <a-button
          v-if="type==='details'"
          @click="currentVisible=false"
        >
          关闭
        </a-button>
        <a-button
          v-else
          @click="currentVisible=false"
        >
          取消
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
// import addUser from './add-component.vue';
// import resetPwd from './reset-pwd.vue';
import CustomerService, { OrderInfo, OrderCancelParams, AssignUser } from '~models/customer-service';
import addOrder from './add-order.vue';
import sendOrder from './send-order.vue';
import cancelOrder from './cancel-order.vue';
import details from './details.vue';
import { useAddOrder, useSendOrder, useCancelOrder } from '../composition/useForm';
// import { companyTypeNames } from '../config';
// import useOpts from '../composition/useOpts';

const customerServiceInstance = new CustomerService();
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
    type: {
      type: String,
      default: '',
    },
    orderType: { // 类型 报事|报修
      type: Number as PropType<1|2>,
      default: 1,
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const {
      type, visible, orderType, defaultData,
    } = toRefs(props);

    const title = computed(() => {
      if (type.value === 'sendOrder') return '派单';
      if (type.value === 'cancel') return '撤单';
      const orderTypeText = orderType.value === 1 ? '反馈' : '报修';
      const operateTypeText = type.value === 'add' ? '新增' : '';
      const operateTypeTextLast = type.value === 'details' ? '详情' : '';
      return `${operateTypeText}${orderTypeText}${operateTypeTextLast}`;
    });
    const cp = shallowRef();
    const cpConf = {
      add: addOrder,
      cancel: cancelOrder,
      sendOrder,
      details,
    };
    cp.value = cpConf[type.value as 'add'|'sendOrder'|'cancel'];
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });
    if (type.value === 'details') {
      return {
        currentVisible,
        cp,
        title,
      };
    }
    const formConf = {
      add: useAddOrder,
      sendOrder: useSendOrder,
      cancel: useCancelOrder,
    };
    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = formConf[type.value as 'add' | 'sendOrder' | 'cancel'](defaultData.value);

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      // const form = props.type === 'resetPwd' ? useResetPwdForm() : useAddCompany();
      const params = { ...modelRef };

      validate().then(async () => {
        // if (type.value === 'add') {
        //   modelRef.orderType = orderType.value;
        // }
        if (type.value === 'add') {
          (modelRef as OrderInfo).orderType = orderType.value;
          await customerServiceInstance.orderAdd(modelRef as OrderInfo); // 新增
        } else if (type.value === 'cancel') {
          await customerServiceInstance.orderCancel(modelRef as OrderCancelParams);
        } else {
          await customerServiceInstance.assignUser(modelRef as AssignUser);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };
    onMounted(() => {
      fill();
      if (type.value === 'edit') {
        // parkOrgIdChange(modelRef.parkOrgId, false);
      }
      // jobOpts.value =
    });
    return {
      currentVisible,
      confirm,
      cp,
      title,
    };
  },
});
</script>
<style lang="scss" scoped>
.btns {
    text-align: center;
}
</style>
