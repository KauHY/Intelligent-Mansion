<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 11:19:57
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="980"
    class="operate-dialog"
    @ok="confirm"
  >
    <div class="billing-scheme-operate-dialog">
      <a-form
        :label-col="{span:6}"
        scroll-to-first-error
      >
        <a-card
          title="基本信息"
          :bordered="false"
        >
          <a-row>
            <a-col :span="11">
              <a-form-item
                label="方案名称"
                v-bind="validateInfos.schemeName"
              >
                <a-input
                  v-model:value="modelRef.schemeName"
                  :disabled="type==='details'"
                  :maxlength="30"
                  placeholder="请输入方案名称"
                />
              </a-form-item>
            </a-col>
            <a-col
              :span="11"
              :offset="1"
            >
              <a-form-item
                label="所属园区"
                v-bind="validateInfos.parkOrgId"
              >
                <v-select
                  v-model:value="modelRef.parkOrgId"
                  :disabled="type==='details'"
                  placeholder="请选择关联园区"
                  filter-option
                  @change="parkChange"
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
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="11">
              <a-form-item
                label="收费项"
                v-bind="validateInfos.chargeItemId"
              >
                <v-select
                  v-model:value="modelRef.chargeItemId"
                  :disabled="type==='details'"
                  placeholder="请选择收费项"
                >
                  <a-select-option
                    v-for="opt in chargeItemOpts"
                    :key="opt.value"
                    @click="selectChargeItem(opt)"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col
              :span="11"
              :offset="1"
            >
              <a-form-item
                label="税率"
              >
                <span v-if="modelRef.taxRate||modelRef.taxRate===0">{{ modelRef.taxRate }}%</span>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card
          title="计费规则"
          :bordered="false"
        >
          <a-row>
            <a-col :span="11">
              <a-form-item
                label="计费周期"
                v-bind="validateInfos.billingCycle"
              >
                <v-select
                  v-model:value="modelRef.billingCycle"
                  :disabled="type==='details' || onlyDisabled"
                  placeholder="请选择计费周期"
                  @change="changeBillingCycle"
                >
                  <a-select-option
                    v-for="opt in billingCycleOpts"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col
              :span="11"
              :offset="1"
            >
              <a-form-item
                label="保留位数"
                v-bind="validateInfos.reservedDigits"
              >
                <v-select
                  v-model:value="modelRef.reservedDigits"
                  disabled
                  placeholder="请选择保留位数"
                >
                  <a-select-option
                    v-for="opt in reservedDigitsOpts"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="11">
              <a-form-item
                label="进位方式"
                v-bind="validateInfos.carryMode"
              >
                <v-select
                  v-model:value="modelRef.carryMode"
                  disabled
                  placeholder="请选择进位方式"
                >
                  <a-select-option
                    v-for="opt in carryModeOpts"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
            <a-col
              :span="11"
              :offset="1"
            >
              <a-form-item
                label="公式类型"
                v-bind="validateInfos.formulaType"
              >
                <v-select
                  v-model:value="modelRef.formulaType"
                  :allow-clear="false"
                  :disabled="type==='details'"
                  placeholder="请选择公式类型"
                >
                  <a-select-option
                    v-for="opt in formulaTypeOpts"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row v-if="modelRef.formulaType!==2">
            <a-col :span="11">
              <a-form-item
                label="固定金额"
                v-bind="validateInfos.amountWithoutTax"
              >
                <a-input
                  v-model:value="modelRef.amountWithoutTax"
                  suffix="元（不含税）"
                  :disabled="!modelRef.chargeItemId || type==='details'"
                  placeholder="请输入固定金额（不含税）"
                  @change="addUnitPriceWithoutTax($event,'amountWithoutTax')"
                />
              </a-form-item>
            </a-col>
            <a-col
              :span="11"
              :offset="1"
            >
              <a-form-item
                label="固定金额"
                v-bind="validateInfos.amountWithTax"
              >
                <a-input
                  v-model:value="modelRef.amountWithTax"
                  suffix="元（含税）"
                  disabled
                  placeholder="请输入固定金额（含税）"
                />
              </a-form-item>
            </a-col>
          </a-row>

          <template v-if="modelRef.formulaType!==1">
            <a-row>
              <a-col :span="11">
                <a-form-item
                  label="计算标准"
                  v-bind="validateInfos.calculationStandard"
                >
                  <v-select
                    v-model:value="modelRef.calculationStandard"
                    :disabled="type==='details'"
                    placeholder="请选择计算标准"
                  >
                    <a-select-option
                      v-for="opt in calculationOpts"
                      :key="opt.value"
                    >
                      {{ opt.label }}
                    </a-select-option>
                  </v-select>
                </a-form-item>
              </a-col>
              <a-col
                :span="11"
                :offset="1"
              >
                <a-form-item
                  label="单价"
                  v-bind="validateInfos.unitPriceWithoutTax"
                >
                  <a-input
                    v-model:value="modelRef.unitPriceWithoutTax"

                    suffix="元（不含税）"
                    :disabled="!modelRef.chargeItemId || type==='details'"
                    placeholder="请输入单价（不含税）"
                    @change="addUnitPriceWithoutTax($event,'unitPriceWithoutTax')"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col
                :span="11"
              >
                <a-form-item
                  label="单价"
                  v-bind="validateInfos.unitPriceWithTax"
                >
                  <a-input
                    v-model:value="modelRef.unitPriceWithTax"
                    suffix="元（含税）"
                    disabled
                    placeholder="请输入单价元（含税）"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </template>
        </a-card>
        <a-card
          title="收费规则"
          :bordered="false"
        >
          <a-row>
            <a-col :span="11">
              <a-form-item
                label="收费类型"
                :label-col="{ flex: 'width: 120px' }"
                v-bind="validateInfos.chargeType"
              >
                <v-select
                  v-model:value="modelRef.chargeType"
                  :disabled="type==='details'"
                  placeholder="请选择收费类型"
                >
                  <a-select-option
                    v-for="opt in chargeTypeOpts"
                    :key="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row
            type="flex"
            justify="start"
          >
            <a-col :span="11">
              <a-form-item
                label="收费周期"
                :label-col="{ flex: 'width: 120px' }"
                v-bind="validateInfos.chargingCycle"
              >
                <a-radio-group
                  v-model:value="modelRef.chargingCycle"
                  :disabled="type==='details'"
                >
                  <a-radio :value="1">
                    与计费周期一致
                  </a-radio>
                  <!-- <a-radio :value="2">
                    多计费周期
                  </a-radio> -->
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col
              :span="8"
            >
              <a-form-item
                v-if="modelRef.chargingCycle===2"
                label=""
                v-bind="validateInfos.cumulativeCycle"
              >
                <a-input
                  v-model:value="modelRef.cumulativeCycle"
                  prefix="累计"
                  suffix="个计费周期"
                  :disabled="type==='details'"
                  placeholder="请输入累计计费周期"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row
            type="flex"
            justify="start"
          >
            <a-col
              :span="11"
            >
              <a-form-item
                label="账单生成日期"
                v-bind="validateInfos.dayOfMonth"
                :label-col="{ flex: 'width: 120px' }"
                class="cost-flex"
              >
                <span
                  v-if="(modelRef.billingCycle===3 || modelRef.billingCycle===4) && (![1,4].includes(selectChargeType))"
                  class="ml-10"
                >第</span>
                <v-select
                  v-if="(modelRef.billingCycle===3 || modelRef.billingCycle===4) && (![1,4].includes(selectChargeType))"
                  v-model:value="modelRef.monthBy"
                  placeholder="请选择月份"
                  :disabled="type==='details'"
                >
                  <a-select-option
                    v-for="opt in monthOpts"
                    :key="opt"
                  >
                    {{ opt }}
                  </a-select-option>
                </v-select>
                <span
                  v-if="(modelRef.billingCycle===3 || modelRef.billingCycle===4) && (![1,4].includes(selectChargeType) )"
                  class="m-10"
                >月</span>
                <span class="ml-10">第</span>
                <a-input
                  v-model:value="modelRef.dayOfMonth"
                  :disabled="type==='details'"
                  placeholder="请输入"
                  suffix="天"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row
            type="flex"
            justify="start"
          >
            <a-col
              :span="11"
            >
              <a-form-item
                label="最晚缴纳日期"
                :label-col="{ flex: 'width: 120px' }"
                v-bind="validateInfos.daysForDelay"
              >
                <a-input
                  v-model:value="modelRef.daysForDelay"
                  prefix="生成日期后第"
                  suffix="天"
                  :disabled="type==='details'"
                  placeholder="请输入"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card
          title="滞纳金规则"
          :bordered="false"
        >
          <a-row
            type="flex"
            justify="start"
          >
            <a-col :span="11">
              <a-form-item
                label="是否收取滞纳金"
                :label-col="{ flex: 'width: 120px' }"
                v-bind="validateInfos.lateCharge"
              >
                <a-radio-group
                  v-model:value="modelRef.lateCharge"
                  :disabled="type==='details'"
                  @change="changeLateCharge"
                >
                  <a-radio :value="1">
                    是
                  </a-radio>
                  <a-radio :value="0">
                    否
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row
            v-if="modelRef.lateCharge===1"
            type="flex"
            justify="start"
          >
            <a-col :span="12">
              <a-form-item
                label="滞纳率计算规则"
                :label-col="{ flex: 'width: 120px' }"
                v-bind="validateInfos.lateType"
              >
                <a-radio-group
                  v-model:value="modelRef.lateType"
                  :disabled="type==='details'"
                  @change="changeRadio"
                >
                  <a-radio
                    :value="1"
                  >
                    逾期金额*逾期天数*滞纳率
                  </a-radio>
                  <a-radio
                    v-if="ruleType"
                    :value="2"
                  >
                    每日滞纳金*逾期天数*建筑面积
                  </a-radio>
                  <a-radio
                    v-if="ruleType"
                    :value="3"
                  >
                    每日滞纳金*逾期天数*套内面积
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row
            type="flex"
            justify="start"
          >
            <template v-if="modelRef.lateCharge===1">
              <a-col :span="11">
                <a-form-item
                  v-if="modelRef.lateType===1 || !ruleType"
                  key="lateRate"
                  label="滞纳率"
                  v-bind="validateInfos.lateRate"
                >
                  <a-input
                    v-model:value="modelRef.lateRate"
                    suffix="%"
                    style="width: 100%;"
                    :disabled="type==='details'"
                    placeholder="请输入滞纳率"
                  />
                </a-form-item>

                <a-form-item
                  v-if="ruleType && (modelRef.lateType===2 || modelRef.lateType===3) "
                  label="每日滞纳金"
                  v-bind="validateInfos.lateDayAmount"
                >
                  <a-input
                    v-model:value="modelRef.lateDayAmount"
                    suffix="元"
                    :disabled="type==='details'"
                    placeholder="请输入每日滞纳金"
                  />
                </a-form-item>
              </a-col>
              <a-col
                :span="11"
                :offset="1"
              >
                <a-form-item
                  label="豁免天数"
                  v-bind="validateInfos.exemptDays"
                >
                  <a-input
                    v-model:value="modelRef.exemptDays"
                    suffix="天"
                    :disabled="type==='details'"
                    placeholder="请输入"
                  />
                </a-form-item>
              </a-col>
            </template>
          </a-row>
        </a-card>
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
import { useAddChargeItem } from '../composition/useFormConf';

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
    billingCycleOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    parkList: {
      type: Array as PropType<SelectOptions>,
      default: () => ([]),
    },
    reservedDigitsOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    carryModeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    formulaTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    calculationStandardOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    chargeTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const collectFeeInessInstance = new CollectFee();

    const {
      type, visible, orderType, defaultData,
    } = toRefs(props);
    const operateType = type.value as 'add'|'other'|'follow'|'remind'|'convertToCustomer';
    const cp = shallowRef();
    const monthOpts = ref<Number>(12);
    const selectChargeType = ref<any>();

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
      validate, validateInfos, modelRef, fill, rulesRef,
    } = useAddChargeItem();

    const chargeItemOpts = ref<SelectOptions>();
    const calculationOpts = ref<SelectOptions>();
    const ruleType = ref<boolean>(true);
    const onlyDisabled = ref<boolean>(false);
    const parkChange = async (parkOrgId:string) => {
      modelRef.chargeItemId = '';
      modelRef.calculationStandard = null;
      modelRef.unitPriceWithTax = null;
      modelRef.amountWithoutTax = null;
      chargeItemOpts.value = await collectFeeInessInstance.chargeItemDict(parkOrgId);
    };
    const changeBillingCycle = async (value:number) => {
      modelRef.dayOfMonth = null;
      modelRef.monthBy = null;
      if (value === 4) {
        monthOpts.value = 12;
      } else if (value === 3) {
        monthOpts.value = 3;
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
          await collectFeeInessInstance.billingSchemeModify(modelRef);
        } else {
          await collectFeeInessInstance.billingSchemeAdd(modelRef);
        }
        emit('operate:success');
        currentVisible.value = false;
      } catch (error) {
        console.error(error, 'error');
      }
    };

    const changeRadio = ({ target }:{target:any}) => {
      modelRef.lateDayAmount = null;
      modelRef.lateRate = null;
    };
    const addUnitPriceWithoutTax = (target:any, type:string) => {
      const value = target.target.value;
      const value1 = modelRef.taxRate ? modelRef.taxRate.toString() : '';
      const taxRateToNumber = parseInt(value1, 10) / 100 || 0;
      if (type === 'unitPriceWithoutTax') {
        modelRef.unitPriceWithTax = Number((value * (1 + taxRateToNumber)).toFixed(2));
      } else {
        modelRef.amountWithTax = Number((value * (1 + taxRateToNumber)).toFixed(2));
      }
    };
    const changeLateCharge = ({ target }:{target:any}) => {
      // modelRef.lateDayAmount = null;
      // modelRef.lateRate = null;
      // modelRef.exemptDays = null;
    };
    const selectChargeItem = async ({ extraInfo, type, value }:{extraInfo:number, type:number, value:string}) => {
      modelRef.calculationStandard = null;
      modelRef.taxRate = extraInfo;
      modelRef.lateDayAmount = null;
      modelRef.lateRate = null;
      modelRef.dayOfMonth = null;
      calculationOpts.value = await collectFeeInessInstance.getCalculationListByFeeType(value);
      selectChargeType.value = type;
      modelRef.feeType = type;
      if ([1, 4, 22, 21, 20, 9, 10, 7].includes(type)) {
        onlyDisabled.value = true;
        // 房屋租金/物业费
        if ([1, 4].includes(type)) {
          modelRef.billingCycle = 4;
        }
        // 押金
        if ([22, 21, 20, 7].includes(type)) {
          modelRef.billingCycle = 1;
        }
        if ([9, 10].includes(type)) {
          modelRef.billingCycle = 2;
        }
      } else {
        modelRef.billingCycle = null;
        onlyDisabled.value = false;
      }

      if ([9, 10, 11, 12].includes(type)) {
        ruleType.value = false;
      } else {
        ruleType.value = true;
      }
    };
    const init = async () => {
      modelRef.reservedDigits = 2;
      modelRef.carryMode = 4;
      if (['edit', 'details'].includes(type.value)) {
        const chargeItemDetails = await collectFeeInessInstance.billingSchemeFindById(defaultData.value.id as string);
        fill(chargeItemDetails);
        if (modelRef.billingCycle === 3) {
          monthOpts.value = 3;
        } else if (modelRef.billingCycle === 4) {
          monthOpts.value = 12;
        }
        selectChargeType.value = chargeItemDetails.feeType;
        if ([10, 9].includes(selectChargeType.value)) {
          modelRef.billingCycle = 2;
          onlyDisabled.value = true;
        }
        if ([7, 22, 21, 20].includes(selectChargeType.value)) {
          modelRef.billingCycle = 1;
          onlyDisabled.value = true;
        }
        if ([1, 4].includes(selectChargeType.value)) {
          modelRef.billingCycle = 4;
          onlyDisabled.value = true;
        }
        modelRef.taxRate = chargeItemDetails.chargeItemDTO?.taxRate || 0;
        chargeItemOpts.value = await collectFeeInessInstance.chargeItemDict(modelRef.parkOrgId);
        calculationOpts.value = await collectFeeInessInstance.getCalculationListByFeeType(modelRef.chargeItemId);
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
      addUnitPriceWithoutTax,
      selectChargeType,
      changeLateCharge,
      onlyDisabled,
      changeRadio,
      ruleType,
      changeBillingCycle,
      monthOpts,
      currentVisible,
      validateInfos,
      modelRef,
      confirm,
      parkChange,
      chargeItemOpts,
      calculationOpts,
      selectChargeItem,
      // cp,
    };
  },
});
</script>
<style lang="less" scoped>
.billing-scheme-operate-dialog {
    :deep(.ant-form-item-label) {
        overflow: inherit;
        white-space: normal;
    }

    .ml-10 {
        margin-right: 10px;
    }

    .m-10 {
        margin: 0 10px;
    }

    :deep(.cost-flex .ant-form-item-control-input-content) {
        display: flex;
        align-items: center;
    }
}
</style>
