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
    :width="780"
    class="operate-dialog"

    @ok="confirm"
  >
    <div class="view-house-apply-details">
      <a-form
        :label-col="{span:4}"
      >
        <a-form-item
          label="所属园区"
          v-bind="type==='details'?'':validateInfos.parkOrgId"
        >
          <v-select
            v-model:value="modelRef.parkOrgId"
            :disabled="type!=='add'"
            placeholder="请选择所属园区"
          >
            <a-select-option
              v-for="opt in parkList"
              :key="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="物品类别"
          v-bind="type==='details'?'':validateInfos.goodsType"
        >
          <a-select
            v-model:value="modelRef.goodsType"
            :disabled="type==='details'"
            placeholder="请选择物品类别"
          >
            <a-select-option
              v-for="opt in goodsTypeList"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          label="物品名称"
          v-bind="type==='details'?'':validateInfos.goodsName"
        >
          <a-input
            v-model:value="modelRef.goodsName"
            :disabled="type==='details'"
            :maxlength="20"
            placeholder="请输入物品名称"
          />
        </a-form-item>
        <a-form-item
          label="计量单位"
          v-bind="type==='details'?'':validateInfos.unit"
        >
          <a-input
            v-model:value="modelRef.unit"
            :maxlength="20"
            :disabled="type==='details'"
            placeholder="请输入计量单位"
          />
        </a-form-item>
        <a-form-item
          label="品牌"
          v-bind="type==='details'?'':validateInfos.brand"
        >
          <a-input
            v-model:value="modelRef.brand"
            :disabled="type==='details'"
            :maxlength="20"
            placeholder="请输入品牌"
          />
        </a-form-item>
        <a-form-item
          label="型号"
          v-bind="type==='details'?'':validateInfos.model"
        >
          <a-input
            v-model:value="modelRef.model"
            :disabled="type==='details'"
            :maxlength="20"
            placeholder="请输入型号"
          />
        </a-form-item>
        <a-form-item
          label="物品单价(元)"
          v-bind="type==='details'?'':validateInfos.unitPrice"
        >
          <a-input-number
            v-model:value="modelRef.unitPrice"
            :disabled="type==='details'"
            placeholder="请输入物品单价(元)"
            style="width: 100%;"
            :min="0"
            :max="999999999.99"
            :precision="2"
          />
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea
            v-model:value="modelRef.remark"
            :disabled="type==='details'"
            :rows="4"
            :maxlength="99"
            placeholder="请输入备注"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          {{ type==="details"?'关闭':'取消' }}
        </a-button>
        <a-button
          v-if="type!=='details'"
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
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import GoodsInspection from '~models/asset-mangement';
import Park from '~models/park';
import { useAddPatrolPlace } from '../composition/useFormConf';
import { goodsTypeList } from '../config';
// import houseCardList from './house-card-list.vue';

export default defineComponent({
  name: 'OperateDialog',
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
      type: Object as PropType<GoodsInspection>,
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
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const goodsInspectionInessInstance = new GoodsInspection();
    const {
      type, visible, orderType, defaultData,
    } = toRefs(props);
    const operateType = type.value as 'add'|'other'|'follow'|'remind'|'convertToCustomer';
    const cp = shallowRef();

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
      validate, validateInfos, modelRef, fill,
    } = useAddPatrolPlace();
    const parkList = ref<SelectOptions>();
    const equipmentList = ref<SelectOptions>(); // 没接口
    const init = async () => {
      parkList.value = await new Park().searchParkListName();
      if (['edit', 'details'].includes(type.value)) {
        fill(defaultData.value);
      }
    };

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      try {
        await validate();
        if (type.value === 'edit') {
          await goodsInspectionInessInstance.goodsEdit(modelRef);
        } else {
          await goodsInspectionInessInstance.patrolPlaceAdd(modelRef);
        }
        emit('operate:success');
        currentVisible.value = false;
      } catch (error) {
        console.error(error, 'error');
      }
    };
    onMounted(() => {
      init();
    });
    return {
      currentVisible,
      validateInfos,
      modelRef,
      parkList,
      equipmentList,
      confirm,
      goodsTypeList,
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
