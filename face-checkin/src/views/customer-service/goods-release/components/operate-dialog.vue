<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-28 14:55:29
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
    class="operate-dialog"
    @ok="confirm"
  >
    <component
      :is="cp"
      :details="detailsData"
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
  defineComponent, PropType, computed, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import CustomerService, { GoodsReleaseInfo } from '~models/customer-service';
import { useGoodsReleaseAudit } from '../composition/useForm';
import audit from './audit.vue';
import details from './details.vue';
// const carPassInstance = new CarPass();
export default defineComponent({
  name: 'OperateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object,
      default: () => ({}),
    },
    title: {
      type: String,
      default: '',
    },
    type: {
      type: String as PropType<'details'|'audit'>,
      default: '',
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { type, defaultData, visible } = toRefs(props);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    const customerServiceInstance = new CustomerService();

    const {
      validate,
      modelRef,
      cp,
      validateInfos,
      detailsData,
    } = useInit(customerServiceInstance, type.value, defaultData.value.id);

    /**
     * @description: 保存
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      validate().then(async () => {
        modelRef.goodsReleaseId = defaultData.value.id;
        await new CustomerService().goodsReleaseAudit(modelRef);
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };
    return {
      currentVisible,
      detailsData,
      confirm,
      modelRef,
      validateInfos,
      cp,
    };
  },
});

/**
 * @description: 初始化
 * @param {*} customerServiceInstance
 * @param {*} type
 * @param {*} id
 * @return {*}
 * @author: GUOCHAO82
 */
function useInit(customerServiceInstance: CustomerService, type:'details'|'audit', id:string) {
  const cp = shallowRef();
  const {
    modelRef,
    validate,
    validateInfos,
  } = useGoodsReleaseAudit();
  const detailsData = ref<GoodsReleaseInfo>();
  const cpConf = {
    audit,
    details,
  };
  const init = async () => {
    if (type === 'details') {
      detailsData.value = await customerServiceInstance.goodsReleaseFindById(id);
    }
    cp.value = cpConf[type];
  };
  init();
  return {
    modelRef,
    validate,
    validateInfos,
    detailsData,
    cp,
  };
}
</script>
