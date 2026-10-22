<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-15 14:43:58
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-28 15:39:02
-->
<template>
  <div class="attract-investment-analysis content-sections">
    <div class="empty-padding" />
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
        <div class="chart-box">
          <div class="top-section">
            <div class="left">
              <echart :option="chartOption" />
            </div>
            <div class="right">
              <echart :option="transitionRateOption" />
            </div>
          </div>
          <div class="bottom-section">
            <div class="left">
              <echart :option="areaChartOption" />
            </div>
            <div class="right">
              <echart :option="customerIndustryOption" />
            </div>
          </div>
        </div>
      </template>
    </search-box>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, Ref,
} from 'vue';
import { graphic } from 'echarts';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import Echart, { PieECOption, useEchartOpts, FunnelOption } from '~components/echart';
import CommandBigScreen, { HouseRentInfo } from '~models/command-big-screen';
import { getPercent } from '~utils/tool';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';

export default defineComponent({
  components: { searchBox, Echart },
  props: {
  },
  setup() {
    const parkOrgId = ref();
    const parkOpts = ref<SelectOptions>([]);
    const options = useHashMapDics<['BUSINESS_CHANCE_STAGE']>(['BUSINESS_CHANCE_STAGE']);
    const searchConf = reactive<SearchConf<['parkOrgId', 'date']>>({
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
        allowClear: false,
        handleChange: (parkOrgId:string) => search(searchConf.date.value, parkOrgId),
      },
      date: {
        tag: 'date-range',
        value: ['', ''],
        placeholder: ['开始时间', '结束时间'],
        endTimeKey: 'createTimeEnd',
        startTimeKey: 'createTimeStart',
        handleChange: (val:string) => search(val, searchConf.parkOrgId.value),
      },
    });

    const wiseInvestmentInfo = ref<HouseRentInfo>();
    const { chartOption, setChartOptionSeries } = useChartOption(1, options.BUSINESS_CHANCE_STAGE);
    const { chartOption: areaChartOption, setAreaChartOptionSeries } = useNnalysisOpts(2);
    const { chartOption: transitionRateOption, setFunnelOptionSeries } = useFunnelOption(); // 转换率
    const { chartOption: customerIndustryOption, setCustomerIndustrySeries } = useNnalysisOpts(3);

    const search = async (time:any, parkOrgId:any) => {
      time = time || [];
      const startTime = time[0] ? `${time[0]} 00:00:00` : '';
      const endTime = time[1] ? `${time[1]} 23:59:59` : '';
      wiseInvestmentInfo.value = {} as HouseRentInfo;
      if (!parkOrgId) return;
      wiseInvestmentInfo.value = await new CommandBigScreen().getChanceOverview(startTime, endTime, parkOrgId);
      const getThreadInfo = await new CommandBigScreen().getThreadSource(startTime, endTime, parkOrgId);
      const getCustomerInfo = await new CommandBigScreen().getCustomerIndustry(startTime, endTime, parkOrgId);
      const TransitionInfo = await new CommandBigScreen().getTransitionRate(startTime, endTime, parkOrgId);
      setChartOptionSeries(wiseInvestmentInfo.value);
      setAreaChartOptionSeries(getThreadInfo);
      setFunnelOptionSeries(TransitionInfo);
      setCustomerIndustrySeries(getCustomerInfo);
    };

    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      searchConf.parkOrgId.value = parkOpts.value[0]?.value || '';
      search(searchConf.date.value, searchConf.parkOrgId.value as string);
    };
    init();
    return {
      parkOrgId,
      searchConf,
      search,
      chartOption,
      areaChartOption,
      transitionRateOption,
      customerIndustryOption,
      wiseInvestmentInfo,
      getPercent,
    };
  },
});
function useFunnelOption() {
  const series:FunnelOption['series'] = [
    {
      name: 'Funnel',
      type: 'funnel',
      left: '10%',
      top: 60,
      bottom: 60,
      width: '80%',
      min: 0,
      max: 100,
      minSize: '20%',
      maxSize: '100%',
      sort: 'descending',
      gap: 0,
      label: {
        formatter: '{b}',
        position: 'left',
      },
      labelLine: {
        show: false,
      },
      itemStyle: {
        borderColor: '#fff',
        borderWidth: 1,
      },
      emphasis: {
        label: {
          // position: 'inside',
          formatter: '{b}: {c}条',
          fontSize: 20,
        },
      },
      data: [
      ],
    },
    {
      name: 'Funnel',
      type: 'funnel',
      left: '10%',
      top: 60,
      bottom: 60,
      width: '80%',
      min: 0,
      max: 100,
      minSize: '20%',
      maxSize: '100%',
      sort: 'descending',
      gap: 0,
      label: {
        formatter: '{c}',
        position: 'inside',
      },
      labelLine: {
        show: false,
      },
      itemStyle: {
        borderColor: '#fff',
        borderWidth: 1,
      },
      emphasis: {
        label: {
          // position: 'inside',
          formatter: '{b}: {c}条',
          fontSize: 20,
        },
      },
      data: [
      ],
    },
  ];
  const { chartOption } = useEchartOpts<FunnelOption>({
    title: {
      text: '商机转换率',
      left: 10,
      top: 10,
      textStyle: {
        fontSize: 14,
        color: '#1F2E4D',
      },
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b} : {c}条',
    },
    legend: {
      data: ['Show', 'Click', 'Visit', 'Inquiry', 'Order'],
    },
    series,
  });
  const setFunnelOptionSeries = (wiseInvestmentInfo:any) => {
    const { businessData } = wiseInvestmentInfo;
    const seriesItem = series[0] as any;
    const seriesItemR = series[1] as any;
    const allData:any[] = [];
    Object.keys(businessData).forEach((key) => {
      allData.push({
        name: key,
        value: Number(businessData[key]),
      });
    });
    allData.sort((a, b) => {
      const curArr = ['机会评估', '需求分析', '报价', '谈判', '达成意向', '签订合同', '赢单关闭'];
      return curArr.indexOf(a.name) - curArr.indexOf(b.name);
    });
    seriesItemR.data = allData;
    seriesItem.data = allData;
    chartOption.series = series.slice();
  };
  return {
    chartOption,
    setFunnelOptionSeries,
  };
}
function useChartOption(type:number, feeTypes:any) {
  const names = {
    1: '商机概览',
  };
  const series:PieECOption['series'] = [
    {
      name: '商机概览',
      type: 'pie',
      radius: ['50%', '70%'],
      left: '-30%',
      avoidLabelOverlap: false,
      label: {
        show: false,
      },
      labelLine: {
        show: false,
      },
      emphasis: {
        label: {
          show: true,

          rich: {
            b: {
              fontSize: '30',
              fontWeight: 'bold',
              lineHeight: 40,
            },
            c: {
              fontSize: '30',
              fontWeight: 'bold',
              lineHeight: 40,
            },
          },
        },
      },
      data: [
        {
          value: 40,
          name: names[type],
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#FBA7A7',
            },
            {
              offset: 1,
              color: '#F56E6E ',
            },
            ]),
          },
        },
        {
          value: 30,
          name: '',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#62A5FF',
            },
            {
              offset: 1,
              color: '#3365FF',
            },
            ]),
          },
        },
      ],
    },
  ];
  const { chartOption } = useEchartOpts<PieECOption>({
    title: {
      text: names[type],
      left: 10,
      top: 10,
      textStyle: {
        fontSize: 14,
        color: '#1F2E4D',
      },
    },
    tooltip: {
      trigger: 'item',
      // formatter: '{a}<br />{b}:{c}%',
      valueFormatter: (value:unknown) => `${value}条`,
    },
    legend: {
      right: '3%',
      top: 'center',
      orient: 'vertical',
      itemGap: 18,
      itemHeight: 10,
      itemWidth: 10,
      icon: 'circle',
      textStyle: {
        color: '#424656',
        rich: {
          a: {
            color: '#7D8292',
            width: 100,
          },
        },
      },
    },
    series,
  });

  const setChartOptionSeries = (wiseInvestmentInfo:any) => {
    const { businessData } = wiseInvestmentInfo;
    const { totalAmount } = wiseInvestmentInfo;
    const seriesItem = series[0] as any;
    const allData:any[] = [];
    Object.keys(businessData).forEach((key) => {
      allData.push({
        name: key,
        value: Number(businessData[key]),
      });
    });
    allData.sort((a, b) => {
      const curArr = ['机会评估', '需求分析', '报价', '谈判', '达成意向', '签订合同', '赢单关闭'];
      return curArr.indexOf(a.name) - curArr.indexOf(b.name);
    });
    seriesItem.data = allData;
    chartOption.series = series.slice();
    chartOption.graphic = [{ // 环形图中间添加文字
      type: 'text',
      left: '33%',
      top: '40%',
      style: {
        text: `${totalAmount}条`,
        textAlign: 'center',
        fill: '#1F2E4D', // 文字的颜色
        fontSize: 20,
        fontWeight: 500,
        fontFamily: 'PingFangSC-Medium, PingFang SC',
      },
    }, {
      type: 'text',
      id: 'text2',
      left: '32%',
      top: '50%',
      style: {
        text: '总商机',
        fill: '#7D8292',
        fontWeight: 400,
        fontSize: 16,
      },
    }];
    (chartOption as any).legend.formatter = (name:string) => `{a|${name}}${getPercent(businessData[name], totalAmount)}%`;
  };

  return {
    chartOption,
    setChartOptionSeries,
  };
}
function useNnalysisOpts(type:number) {
  const names = {
    2: '线索来源分析',
    3: '客户行业分析',
  };
  const series:PieECOption['series'] = [
    {
      name: names[type],
      type: 'pie',
      radius: '70%',
      left: '-30%',
      label: {
        show: false,
        position: 'center',
      },
      labelLine: {
        show: false,
      },
      data: [],
    },
  ];
  const { chartOption } = useEchartOpts<PieECOption>({
    tooltip: {
      trigger: 'item',
      valueFormatter: (value:unknown) => `${value}条`,
    },
    title: {
      text: names[type],
      left: 10,
      top: 10,
      textStyle: {
        fontSize: 14,
        color: '#1F2E4D',
      },
    },
    legend: {
      right: '3%',
      top: 'center',
      orient: 'vertical',
      itemGap: 18,
      itemHeight: 10,
      itemWidth: 10,
      icon: 'circle',
      textStyle: {
        color: '#424656',
        rich: {
          a: {
            color: '#7D8292',
            width: 100,
          },
        },
      },
    },
    series,
  });

  const setAreaChartOptionSeries = (wiseInvestmentInfo:any) => {
    const { businessData } = wiseInvestmentInfo;
    const { totalAmount } = wiseInvestmentInfo;
    const seriesItem = series[0] as any;
    const allData:any[] = [];
    Object.keys(businessData).forEach((key) => {
      allData.push({
        name: key,
        value: Number(businessData[key]),
      });
    });
    seriesItem.data = allData;
    chartOption.series = series.slice();
    (chartOption as any).legend.formatter = (name:string) => `{a|${name}}${getPercent(businessData[name], totalAmount)}%`;
  };

  const setCustomerIndustrySeries = (wiseInvestmentInfo:any) => {
    const { businessData } = wiseInvestmentInfo;
    const { totalAmount } = wiseInvestmentInfo;
    const seriesItem = series[0] as any;
    const allData:any[] = [];
    Object.keys(businessData).forEach((key) => {
      allData.push({
        name: key,
        value: Number(businessData[key]),
      });
    });
    seriesItem.data = allData;
    chartOption.series = series.slice();
    (chartOption as any).legend.formatter = (name:string) => `{a|${name}}${getPercent(businessData[name], totalAmount)}%`;
  };

  return {
    chartOption,
    setAreaChartOptionSeries,
    setCustomerIndustrySeries,
  };
}
</script>
<style lang='scss' scoped>
.attract-investment-analysis {
    padding-top: 16px;
    font-size: 14px;

    .empty-padding {
        height: 20px;
        background: transparent;
    }

    .chart-box {
        display: flex;
        flex-direction: column;
        height: 92vh;

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
