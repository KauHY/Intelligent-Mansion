<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-15 14:43:58
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-17 15:17:28
-->
<template>
  <div class="attract-investment-analysis content-section">
    <search-box :search-conf="searchConf">
      <!-- <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
      </template> -->
      <template #table>
        <Summary
          v-model:value="type"
          :top-statistic="topStatistic"
        />
        <div class="chart-box">
          <div class="top-section">
            <div class="left">
              <div class="search-header">
                <v-select
                  v-model:value="payTrendParams.chargeType"
                  style="width: 150px;"
                  placeholder="请选择费用类型"
                  :allow-clear="false"
                >
                  <a-select-option
                    v-for="opt in options.FEE_BILL_TYPE"
                    :key="opt.value"
                    :value="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
                &nbsp;
                <a-range-picker
                  v-model:value="payTrendTimes"
                  value-format="YYYY-MM"
                  :allow-clear="false"
                  style="width: 180px;"
                  :disabled-date="payTrendDisabledDate"
                  picker="month"
                />
              </div>
              <echart :option="payTrendOption" />
            </div>
            <div class="right">
              <div class="search-header">
                <a-date-picker
                  v-model:value="incomeNnalysisParams.month"
                  value-format="YYYY-MM"
                  :allow-clear="false"
                  picker="month"
                />
              </div>
              <echart :option="incomeNnalysisOption" />
            </div>
          </div>
          <div class="bottom-section">
            <div class="left">
              <div class="search-header">
                <v-select
                  v-model:value="overdueTrendParams.chargeType"
                  style="width: 150px;"
                  placeholder="请选择费用类型"
                  :allow-clear="false"
                >
                  <a-select-option
                    v-for="opt in options.FEE_BILL_TYPE"
                    :key="opt.value"
                    :value="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
                &nbsp;
                <a-range-picker
                  v-model:value="overdueTrendTimes"
                  style="width: 180px;"
                  value-format="YYYY-MM"
                  :allow-clear="false"
                  :disabled-date="overdueTrendDisabledDate"
                  picker="month"
                />
              </div>
              <echart :option="overdueTrendOption" />
            </div>
            <div class="right ">
              <div class="title">
                逾期客户TOP5
              </div>
              <div class="search-header">
                <v-select
                  v-model:value="overdueCustomersParams.chargeType"
                  style="width: 150px;"
                  placeholder="请选择费用类型"
                  :allow-clear="false"
                >
                  <a-select-option
                    v-for="opt in options.FEE_BILL_TYPE"
                    :key="opt.value"
                    :value="opt.value"
                  >
                    {{ opt.label }}
                  </a-select-option>
                </v-select>
                &nbsp;
                <a-date-picker
                  v-model:value="overdueCustomersParams.month"
                  value-format="YYYY-MM"
                  :allow-clear="false"
                  style="width: 180px;"
                  :disabled-date="overdueTrendDisabledDate"
                  picker="month"
                />
              </div>
              <div
                ref="tableBoxRef"
                class="table-box"
              >
                <v-table
                  bordered
                  :pagination="false"
                  :columns="columns"
                  :scroll="{y: tableMaxHeight}"
                  :data-source="overdueTrend"
                >
                  <template #index="{index}">
                    <img
                      v-if="!index"
                      src="~assets/images/statement-analysis/overdue-customers_ranking1.png"
                    >
                    <img
                      v-if="index===1"
                      src="~assets/images/statement-analysis/overdue-customers_ranking2.png"
                    >
                    <img
                      v-if="index===2"
                      src="~assets/images/statement-analysis/overdue-customers_ranking3.png"
                    >
                    <span>{{ index+1 }}</span>
                  </template>
                </v-table>
              </div>
            </div>
          </div>
        </div>
      </template>
    </search-box>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, computed,
} from 'vue';

import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import Echart, { PieECOption, useEchartOpts } from '~components/echart';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Summary from './components/summary.vue';

import {
  usePayTrend, useIncomeNnalysis, useOverdueCustomers, useTopStatistic,
} from './composition';

export default defineComponent({
  components: { searchBox, Echart, Summary },
  props: {
  },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    const options = useHashMapDics<['FEE_BILL_TYPE']>(['FEE_BILL_TYPE']);
    const searchConf = reactive<SearchConf<['parkOrgId']>>({
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
        allowClear: false,
      },
    });

    const search = async () => {

    };
    const parkOrgId = computed<string>(() => searchConf.parkOrgId.value as string);
    // 缴费趋势
    const {
      chartOption: payTrendOption,
      params: payTrendParams,
      times: payTrendTimes,
      disabledDate: payTrendDisabledDate,
    } = usePayTrend('缴费趋势', parkOrgId, 'payTrend');

    // 逾期趋势
    const {
      chartOption: overdueTrendOption,
      params: overdueTrendParams,
      times: overdueTrendTimes,
      disabledDate: overdueTrendDisabledDate,
    } = usePayTrend('逾期趋势', parkOrgId, 'overdueTrend');
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      searchConf.parkOrgId.value = parkOpts.value[0].value;
    };
    init();
    return {
      parkOrgId,
      searchConf,
      search,
      options,
      // 顶部统计
      ...useTopStatistic(parkOrgId),
      // 缴费趋势
      payTrendOption,
      payTrendParams,
      payTrendTimes,
      payTrendDisabledDate,

      // 收入分析
      ...useIncomeNnalysis(parkOrgId, options.FEE_BILL_TYPE),
      // 逾期趋势
      overdueTrendOption,
      overdueTrendParams,
      overdueTrendTimes,
      overdueTrendDisabledDate,

      // 逾期客户top5
      ...useOverdueCustomers(parkOrgId),
    };
  },
});

</script>
<style lang='scss' scoped>
.attract-investment-analysis {
    padding-top: 16px;
    font-size: 14px;

    .chart-box {
        display: flex;
        flex-direction: column;
        height: 62vh;
        margin-top: 20px;

        >div {
            display: flex;
            flex: 1;
            align-items: center;

            &:first-child {
                margin-bottom: 16px;
            }

            >div {
                position: relative;
                flex: 1;
                height: 100%;
                border: 1px solid #e6e4e3;
                border-radius: 6px;

                &.left {
                    margin-right: 16px;
                }

                >.search-header {
                    position: absolute;
                    top: 10px;
                    right: 26px;
                    z-index: 10;
                    display: flex;
                    gap: 8px;
                    align-items: center;
                    padding: 10px 12px;
                    background: linear-gradient(180deg, rgba(255, 255, 255, 0.78) 0%, rgba(244, 249, 255, 0.9) 100%);
                    border: 1px solid rgba(var(--theme-primary-rgb), 0.12);
                    border-radius: 18px;
                    box-shadow:
                        inset 0 1px 0 rgba(255, 255, 255, 0.84),
                        0 14px 28px rgba(8, 24, 40, 0.06);
                    backdrop-filter: blur(16px);
                }

                >.title {
                    position: absolute;
                    top: 20px;
                    left: 26px;
                    z-index: 10;
                    font-size: 14px;
                    font-weight: 900;
                    color: #1f2e4d;
                }
            }

            &.bottom-section {
                > div:last-child {
                    padding-top: h(60);
                    overflow: hidden;
                }
            }
        }

        .table-box {
            height: 100%;
            padding: 0 20px 10px;

            img {
                width: 18px;
                height: 18px;
            }
        }
    }
}
</style>
