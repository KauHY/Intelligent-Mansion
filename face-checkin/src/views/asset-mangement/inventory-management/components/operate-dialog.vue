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
          v-bind="type === 'details'? '' :validateInfos.parkOrgId"
        >
          <v-select
            v-if="type !=='details'"
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
          <p
            v-if="type=== 'details'"
            class="item-value"
          >
            {{ (parkList||[]).find(i => i.value === modelRef.parkOrgId)?.label }}
          </p>
        </a-form-item>
        <a-form-item
          label="所属仓库"
          v-bind="type==='details'?'':validateInfos.goodsType"
        >
          公寓仓库
        </a-form-item>
        <a-form-item
          v-if="type==='details'"
          label="物品编号"
        >
          {{ modelRef.goodsNo }}
        </a-form-item>
        <a-form-item
          v-if="type==='details'"
          label="物品类别"
        >
          <p class="item-value">
            {{ modelRef.goodsType ? goodsTypeList[modelRef.goodsType - 1].label : '' }}
          </p>
        </a-form-item>
        <a-form-item
          label="物品名称"
          v-bind="type === 'details'?'':validateInfos.goodsName"
          @click="type !== 'details' && toSelectGoods()"
        >
          <a-input-search
            v-if="type !== 'details'"
            v-model:value="modelRef.goodsName"
            :disabled="type==='details'"
            placeholder="请输入物品名称"
          />
          <p
            v-if="type === 'details'"
            class="item-value"
          >
            {{ modelRef.goodsName }}
          </p>
        </a-form-item>
        <a-form-item
          :label="type==='details'? '库存':'入库数量'"
          v-bind="type==='details'?'':validateInfos.stockInNum"
        >
          <a-input-number
            v-if="type !== 'details'"
            v-model:value="modelRef.stockInNum"
            :disabled="type==='details'"
            placeholder="请输入入库数量"
            style="width: 100%;"
            :min="0"
            :max="99999"
            @change="handleStockNum"
          />
          <p
            v-if="type === 'details'"
            class="item-value"
          >
            {{ modelRef.stockNum }}
          </p>
        </a-form-item>
        <a-form-item
          v-if="type === 'details'"
          label="单位"
        >
          {{ modelRef.unit }}
        </a-form-item>
        <a-form-item
          :label="type==='details' ? '入库均价(元)': '入库单价(元)'"
          v-bind="type==='details'?'':validateInfos.stockInUnitPrice"
        >
          <a-input-number
            v-if="type !== 'details'"
            v-model:value="modelRef.stockInUnitPrice"
            :disabled="type==='details'"
            placeholder="请输入入库均价(元)"
            style="width: 100%;"
            @change="handleNumber"
          />
          <p
            v-if="type === 'details'"
            class="item-value"
          >
            {{ modelRef.averagePrice }}
          </p>
        </a-form-item>
        <!-- <a-form-item
          label="备注"
          v-bind="type==='details'?'':validateInfos.remark"
        >
          <a-textarea
            v-model:value="modelRef.remark"
            :disabled="type==='details'"
            :maxlength="30"
            placeholder="请输入备注"
          />
        </a-form-item> -->
      </a-form>
    </div>

    <add-goods-info
      v-if="goodsVisible"
      v-model:visible="goodsVisible"
      :park-org-id="modelRef.parkOrgId"
      @select="goodsListInfo"
    />
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
import { message } from 'ant-design-vue';
import PatrolInspection, {
  HouseInfo,
  GoodsCard,
  SelectHouseInfo,
} from '~models/asset-mangement';
import Park from '~models/park';
import House, { HouseInfo as GoodsListInfo, StockOperation } from '~models/asset-mangement/Host';
import { useAddPatrolPlace } from '../composition/useFormConf';
import { goodsTypeList } from '../config';
import addGoodsInfo from './add-goods-info.vue';

export default defineComponent({
  name: 'OperateDialog',
  components: { addGoodsInfo },
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
          // patrolInspectionInessInstance.patrolPlaceEdit(modelRef);
        } else {
          await patrolInspectionInessInstance.inventoryInGoodsAdd({ ...modelRef, stockMode: StockOperation.IN_STOCK });
        }
        emit('operate:success');
        currentVisible.value = false;
      } catch (error) {
        console.error(error, 'error');
      }
    };
    const goodsVisible = ref(false);
    const goodsListInfo = async (id:string, selectHouse: GoodsCard[]) => {
      modelRef.goodsName = selectHouse[0].goodsName as string;
      modelRef.goodsId = selectHouse[0].id as string;
      modelRef.stockInUnitPrice = selectHouse[0].unitPrice as number;
    };

    const toSelectGoods = () => {
      if (!modelRef.parkOrgId) {
        message.info('请先选择园区!');
        return;
      }
      goodsVisible.value = true;
    };
    const handleNumber = () => {
      let stockInUnitPrice: number | string = `${modelRef.stockInUnitPrice}`;
      stockInUnitPrice = +(parseFloat(stockInUnitPrice).toFixed(2));
      if (stockInUnitPrice) modelRef.stockInUnitPrice = +stockInUnitPrice;
    };
    const handleStockNum = () => {
      const str = `${modelRef.stockInNum}`;
      modelRef.stockInNum = +(str.replace(/[^0-9]/ig, ''));
    };

    onMounted(() => {
      // fill();
      // if (type.value === 'edit') {
      //   // parkOrgIdChange(modelRef.parkOrgId, false);
      // }
      // jobOpts.value =
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
      goodsVisible,
      goodsListInfo,
      toSelectGoods,
      handleNumber,
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

    .item-value {
        line-height: 32px;
    }
}
</style>
