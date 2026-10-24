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
        >
          {{ modelRef.parkOrgName }}
        </a-form-item>
        <a-form-item
          label="所属仓库"
        >
          所属仓库
        </a-form-item>
        <a-form-item
          label="物品名称"
        >
          {{ modelRef.goodsName }}
        </a-form-item>
        <a-form-item
          v-if="stockMode !== StockOperation.EDIT_STOCK"
          label="当前库存"
        >
          {{ modelRef.stockNum || 0 }}
        </a-form-item>
        <a-form-item
          v-if="stockMode !== StockOperation.EDIT_STOCK"
          label="出入库类型"
          v-bind="validateInfos.stockMode"
        >
          <v-select
            v-model:value="modelRef.stockMode"
            placeholder="请选择出入库类型"
          >
            <a-select-option :value="StockOperation.IN_STOCK">
              手动入库
            </a-select-option>
            <a-select-option :value="StockOperation.OUT_STOCK">
              手动出库
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          :label="`${stockMode === StockOperation.EDIT_STOCK ? '库存' : modelRef.stockMode === StockOperation.OUT_STOCK ? '出库数量' : '入库数量'}`"
          v-bind="validateInfos.stockInNum"
        >
          <a-input-number
            v-model:value="modelRef.stockInNum"
            :disabled="type==='details'"
            :placeholder="`请输入${stockMode === StockOperation.EDIT_STOCK ? '库存' : modelRef.stockMode === StockOperation.OUT_STOCK ? '出库数量' : '入库数量'}`"
            style="width: 100%;"
            :min="0"
            :max="99999"
            @change="handleStockNum"
          />
        </a-form-item>
        <a-form-item
          v-if="modelRef.stockMode !== StockOperation.OUT_STOCK "
          label="入库单价(元)"
          v-bind="type==='details'?'':validateInfos.unitPrice "
        >
          <a-input
            v-model:value="modelRef.unitPrice "
            :disabled="type==='details'"
            placeholder="请输入入库单价(元)"
            style="width: 100%;"
          />
        </a-form-item>
        <a-form-item
          label="备注"
          v-bind="type==='details'?'':validateInfos.remark"
        >
          <a-textarea
            v-model:value="modelRef.remark"
            :disabled="type==='details'"
            :maxlength="200"
            placeholder="请输入备注"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          取消
        </a-button>
        <a-button
          v-if="type!=='details'"
          type="primary"
          @click="confirm"
        >
          保存
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import PatrolInspection, {
  HouseInfo,
  GoodsCard,
  SelectHouseInfo,
} from '~models/asset-mangement';
import House, { HouseInfo as GoodsListInfo, StockOperation } from '~models/asset-mangement/Host';
import { useOutInStockEdit } from '../composition/useOutInStockEdit';

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
      type: Object as PropType<PatrolInspection>,
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
    stockMode: {
      type: Number,
      default: null,
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const patrolInspectionInessInstance = new PatrolInspection();
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
    } = useOutInStockEdit();

    const init = async () => {
      fill({ ...defaultData.value, stockMode: props.stockMode });
      modelRef.stockInNum = modelRef.stockNum;
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
        modelRef.stockInUnitPrice = modelRef.unitPrice;
        await patrolInspectionInessInstance.inventoryOutInStockEdit(modelRef);
        emit('operate:success');
        currentVisible.value = false;
      } catch (error) {
        console.error(error, 'error');
      }
    };
    const handleStockNum = () => {
      const str = `${modelRef.stockInNum}`;
      modelRef.stockInNum = +(str.replace(/[^0-9]/ig, ''));
    };
    onMounted(() => {
      init();
    });
    return {
      currentVisible,
      validateInfos,
      modelRef,
      confirm,
      StockOperation,
      handleStockNum,
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
