<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-02 16:41:07
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="980"
    class="operate-dialog"
    :cancel-text="type==='details'?'关闭':'取消'"
    @ok="confirm"
  >
    <component
      :is="cp"
      :default-data="defaultData"
      :order-type="orderType"
      :type="type"
    />
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import WisdomCanvassBusiness, {
  BusinessInfo, OperateBussInfo, BusinessInfoFollow, BusinessInRemind, BusinessInconvertToCustomer,
} from '~models/wisdom-canvass-business';
// import addUser from './add-component.vue';
// import resetPwd from './reset-pwd.vue';
import businessOpportunityAdd from './business-opportunity-add.vue';
import businessOpportunityDistribution from './business-opportunity-operate.vue';
import businessOpportunityFollow from './business-opportunity-follow.vue';
import businessOpportunityRemind from './business-opportunity-remind.vue';
import businessOpportunityConvertToCustomer from './business-opportunity-convert-to-customer.vue';
// import sendOrder from './send-order.vue';
// import cancelOrder from './cancel-order.vue';
// import details from './details.vue';
import {
  useAddBusinessInfo, useOperateBussInfo, useFollow, useRemind, useConvertToCustomer,
} from '../composition/useFormConf';
// import { companyTypeNames } from '../config';
// import CustomerService, { OrderInfo, OrderCancelParams, AssignUser } from '~models/customer-service';
// import useOpts from '../composition/useOpts';

// const customerServiceInstance = new CustomerService();
export default defineComponent({
  name: 'OperateDialog',
  components: {},
  props: {
    title: {
      type: String,
      default: '',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<BusinessInfo>,
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
    const wisdomCanvassBusinessInstance = new WisdomCanvassBusiness();
    const {
      type, visible, defaultData,
    } = toRefs(props);
    const operateType = type.value as 'add'|'other'|'follow'|'remind'|'convertToCustomer';
    const cp = shallowRef();
    const cpConf = {
      add: businessOpportunityAdd, // 新增商机
      edit: businessOpportunityAdd, // 新增商机
      other: businessOpportunityDistribution, // 分配
      follow: businessOpportunityFollow,
      remind: businessOpportunityRemind, // 提醒
      convertToCustomer: businessOpportunityConvertToCustomer, // 转成客户
    };
    const formConf = {
      add: useAddBusinessInfo,
      edit: useAddBusinessInfo,
      other: useOperateBussInfo, // 分配
      follow: useFollow,
      remind: useRemind,
      convertToCustomer: useConvertToCustomer,
    };
    cp.value = cpConf[operateType];

    // 弹框展示隐藏
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });
    const {
      modelRef,
      validate,
    } = formConf[operateType]();

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      validate().then(async () => {
        switch (type.value) {
          case 'add': {
            await wisdomCanvassBusinessInstance.businessInfoAdd(modelRef as BusinessInfo);
            break;
          }
          case 'edit': {
            await wisdomCanvassBusinessInstance.businessInfoEdit(modelRef as BusinessInfo);
            break;
          }
          case 'other': {
            const params = { ...modelRef } as OperateBussInfo;
            params.recordType = defaultData.value.recordType as number;
            await wisdomCanvassBusinessInstance.businessInfoOperate(params);
            break;
          }
          case 'follow': {
            // const params = { ...modelRef } as BusinessInfoFollow;
            // params.recordType = defaultData.value.recordType as number;
            await wisdomCanvassBusinessInstance.businessInfoFollow(modelRef as BusinessInfoFollow);
            break;
          }
          case 'remind': {
            await wisdomCanvassBusinessInstance.businessInfoRemind(modelRef as BusinessInRemind);
            break;
          }
          case 'convertToCustomer': {
            await wisdomCanvassBusinessInstance.businessInfoConvertToCustomer(modelRef as BusinessInconvertToCustomer);
            break;
          }
          default:
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
        console.log(error);
      });
    };
    onMounted(() => {
      // fill();
      if (type.value === 'edit') {
        // parkOrgIdChange(modelRef.parkOrgId, false);
      }
      // jobOpts.value =
    });
    return {
      currentVisible,
      confirm,
      cp,
    };
  },
});
</script>
