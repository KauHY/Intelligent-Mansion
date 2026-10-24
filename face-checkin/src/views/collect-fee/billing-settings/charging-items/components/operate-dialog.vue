<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-25 09:55:47
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
          v-bind="validateInfos.parkOrgId"
        >
          <v-select
            v-model:value="modelRef.parkOrgId"
            :disabled="type==='details'"
            filter-option
            placeholder="请选择关联园区"
          >
            <a-select-option
              v-for="opt in parkList"
              :key="opt.label"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="费用项名称"
          v-bind="validateInfos.chargeName"
        >
          <a-input
            v-model:value="modelRef.chargeName"
            :disabled="type==='details'"
            :maxlength="30"
            placeholder="请输入费用项名称"
          />
        </a-form-item>
        <a-form-item
          label="费用类型"
          v-bind="validateInfos.feeType"
        >
          <a-select
            v-model:value="modelRef.feeType"
            :disabled="type==='details'"
            :options="groupFeeTypes"
            placeholder="请选择费用项类型"
          />
        </a-form-item>
        <a-form-item
          label="税率"
          v-bind="validateInfos.taxRate"
        >
          <a-input
            v-model:value="modelRef.taxRate"
            :disabled="type==='details'"
            suffix="%"
            placeholder="请输入税率"
          />
        </a-form-item>

        <a-form-item
          label="收款方"
          v-bind="validateInfos.payeeId"
        >
          <v-select
            v-model:value="modelRef.payeeId"
            :disabled="type==='details'"
            placeholder="请选择收款方"
          >
            <a-select-option
              v-for="opt in options.BENEFICIARY"
              :key="String(opt.value)"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
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
import { message } from 'ant-design-vue';
import CollectFee, { ChargeItem } from '~models/collect-fee';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { useAddChargeItem } from '../composition/useFormConf';
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
      type: Object as PropType<ChargeItem>,
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
    groupFeeTypes: {
      type: Array as PropType<SelectOptions>,
      default: () => ([]),
    },
    parkList: {
      type: Array as PropType<SelectOptions>,
      default: () => ([]),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const collectFeeInessInstance = new CollectFee();
    const {
      type, visible, orderType, defaultData,
    } = toRefs(props);
    const options = useHashMapDics<['BENEFICIARY']>(['BENEFICIARY']);
    // const operateType = type.value as 'add'|'other'|'follow'|'remind'|'convertToCustomer';
    // const cp = shallowRef();

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
    } = useAddChargeItem();

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
          await collectFeeInessInstance.chargeItemModify(modelRef);
        } else {
          await collectFeeInessInstance.chargeItemAdd(modelRef);
        }
        emit('operate:success');
        message.success('保存成功');
        currentVisible.value = false;
      } catch (error) {
        console.error(error, 'error');
      }
    };
    const payeeOpts = ref<SelectOptions>([]); // 收款方
    // const parkList = ref<SelectOptions>();
    // const equipmentList = ref<SelectOptions>(); // 没接口
    const init = async () => {
      // parkList.value = await new Park().searchParkListName();
      // payeeOpts.value = [
      //   {
      //     label: '暂无接口',
      //     value: '111',
      //   },
      // ];
      if (['edit', 'details'].includes(type.value)) {
        const chargeItemDetails = await collectFeeInessInstance.chargeItemFindById(defaultData.value.id as string);
        fill(chargeItemDetails);
      }
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
      options,
      // parkList,
      // equipmentList,
      confirm,
      payeeOpts,
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
