<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 14:50:58
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="500"
    class="operate-dialog"
    @cancel="onClose"
  >
    <div class="view-house-apply-details">
      <a-form
        :label-col="{span: 7}"
      >
        <a-form-item
          label="物品名称"
        >
          {{ goodsName }}
        </a-form-item>
        <a-form-item
          label="库存下线预警值"
          v-bind="validateInfos.minStock"
        >
          <a-input
            v-model:value="modelRef.minStock"
            placeholder="请输入预警值"
            style="width: 100%;"
            @change="handleNumber"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <div>
        <a-button @click="onClose">
          取消
        </a-button>
        <a-button
          type="primary"
          @click="confirm"
        >
          确认
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, toRefs, computed, onMounted,
} from 'vue';
import PatrolInspection, {
  GoodsCard,
} from '~models/asset-mangement';
import { useAddPatrolPlace } from '../composition/useWarningFormConf';
import { goodsTypeList } from '../config';

export default defineComponent({
  name: 'OperateDialog',
  props: {
    id: {
      type: String,
      default: '',
    },
    title: {
      type: String,
      default: '',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    goodsName: {
      type: String,
      default: '',
    },
    minStock: {
      type: Number,
      default: null,
    },
  },
  emits: ['update:visible', 'operate:close'],
  setup(props, { emit }) {
    const patrolInspectionInessInstance = new PatrolInspection();
    const { minStock } = toRefs(props);
    const {
      validate, validateInfos, modelRef, fill, resetFields,
    } = useAddPatrolPlace();

    // 弹框展示隐藏
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      try {
        await validate();
        await patrolInspectionInessInstance.settingMinStock({ ...modelRef, id: props.id });
        resetFields();
        emit('operate:close', true);
      } catch (error) {
        console.error(error, 'error');
      }
    };
    const onClose = () => {
      resetFields();
      emit('operate:close');
    };

    const handleNumber = () => {
      // const str = `${modelRef.minStock}`;
      // modelRef.minStock = +(str.replace(/[^0-9]/ig, ''));
    };
    onMounted(() => {
      fill({ minStock: minStock.value });
    });
    return {
      validateInfos,
      modelRef,
      confirm,
      goodsTypeList,
      emit,
      onClose,
      currentVisible,
      handleNumber,
      // cp,
    };
  },
});
</script>
<style lang="less" scoped>
.view-house-apply-details {
    .item {
        display: flex;
        justify-content: flex-start;
        margin-bottom: 30px;

        .label {
            width: 160px;
            margin-right: 40px;
            text-align: right;
        }
    }
}
</style>
