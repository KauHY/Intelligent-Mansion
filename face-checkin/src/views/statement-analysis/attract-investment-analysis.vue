<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-15 14:43:58
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-28 15:39:02
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
        </a-button>&nbsp;&nbsp; -->
      <!-- </template> -->
      <template #table>
        <div class="summary">
          <div class="item">
            <div class="left">
              <img src="~assets/images/statement-analysis/attract-investment-analysis_header_icon1.png">
              <div class="text-box">
                <span>{{ wiseInvestmentInfo?.totalBuildArea }}㎡</span><br>
                建筑面积（㎡）
              </div>
            </div>
            <div class="right">
              <span>{{ wiseInvestmentInfo?.totalHouseCount }}</span><br>
              房间总数
            </div>
          </div>
          <div class="item">
            <div class="left">
              <img src="~assets/images/statement-analysis/attract-investment-analysis_header_icon2.png">
              <div class="text-box">
                <span>{{ wiseInvestmentInfo?.companyCount }}</span><br>
                入驻企业数
              </div>
            </div>
            <div class="right">
              <span>{{ getPercent(wiseInvestmentInfo?.occupancyRate||0, 1) }}%</span><br>
              入驻率
            </div>
          </div>
          <div class="item">
            <div class="left">
              <img src="~assets/images/statement-analysis/attract-investment-analysis_header_icon3.png">
              <div class="text-box">
                <span>{{ wiseInvestmentInfo?.rentedHouseCount }}</span><br>
                已租数
              </div>
            </div>
            <div class="right">
              <span>{{ wiseInvestmentInfo?.rentedBuildArea }}㎡</span><br>
              已租面积
            </div>
          </div>
        </div>
        <div class="chart-box">
          <div class="left">
            <echart :option="chartOption" />
          </div>
          <div class="right">
            <echart :option="areaChartOption" />
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
import Echart, { PieECOption, useEchartOpts } from '~components/echart';
import CommandBigScreen, { HouseRentInfo } from '~models/command-big-screen';
import { getPercent } from '~utils/tool';

export default defineComponent({
  components: { searchBox, Echart },
  props: {
  },
  setup() {
    const parkOrgId = ref();

    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['parkOrgId']>>({
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
        allowClear: false,
        handleChange: (parkOrgId:string) => search(parkOrgId),
      },
    });

    const wiseInvestmentInfo = ref<HouseRentInfo>();
    const { chartOption, setChartOptionSeries } = useChartOption('count');
    const { chartOption: areaChartOption, setAreaChartOptionSeries } = useChartOption('area');

    const search = async (parkOrgId:string|undefined) => {
      wiseInvestmentInfo.value = {} as HouseRentInfo;
      if (!parkOrgId) return;
      wiseInvestmentInfo.value = await new CommandBigScreen().getHouseRentData(false, parkOrgId);
      setChartOptionSeries(wiseInvestmentInfo.value);
      setAreaChartOptionSeries(wiseInvestmentInfo.value);
    };

    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      searchConf.parkOrgId.value = parkOpts.value[0]?.value || '';
      search(searchConf.parkOrgId.value as string);
    };
    init();
    return {
      parkOrgId,
      searchConf,
      search,
      chartOption,
      areaChartOption,
      wiseInvestmentInfo,
      getPercent,
    };
  },
});
function useChartOption(type:'count'|'area') {
  const series:PieECOption['series'] = [
    {
      name: type === 'count' ? '房源数量' : '房源面积',
      type: 'pie',
      radius: ['50%', '70%'],
      right: 'center',
      avoidLabelOverlap: false,
      label: {
        show: true,
        position: 'outside',
        // formatter: ['{b|{b}}', '{c|{c}({d}%)}'].join('\n'),
        rich: {
          b: {
            color: '#424656',
            lineHeight: 24,
          },
          c: {
            color: '#7D8292',
            lineHeight: 24,
          },
        },
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
      labelLine: {
        show: true,
        length: 40,
        length2: 60,
      },
      data: [
        {
          value: 40,
          name: type === 'count' ? '已租数' : '已租面积',
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
          name: type === 'count' ? '待租数' : '待租面积',
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
      text: type === 'count' ? '房源数量状态分布（间）' : '房源面积状态分布（㎡）',
    },
    tooltip: {
      trigger: 'item',
      // formatter: '{a}<br />{b}:{c}%',
      valueFormatter: (value:unknown) => `${value}${type === 'count' ? '间' : '㎡'}`,
    },
    legend: {
      left: 'center',
      bottom: '3%',
      // orient: 'vertical',
      // itemGap: 30,
      // itemHeight: 10,
      // itemWidth: 10,
      icon: 'circle',
      textStyle: {
        color: '#424656',
      },
    },
    series,
  });

  const setChartOptionSeries = (wiseInvestmentInfo:HouseRentInfo) => {
    const seriesItem = series[0] as any;
    const { totalHouseCount, rentedHouseCount, rentingHouseCount } = wiseInvestmentInfo;
    seriesItem.label.formatter = (seriesItem:any) => {
      const { value, name } = seriesItem.data;
      return `{b|${name}}\n{c|${value}  (${getPercent(value, totalHouseCount)}%)}`;
    };
    seriesItem.data[0].value = rentedHouseCount;
    seriesItem.data[1].value = rentingHouseCount;
    chartOption.series = series.slice();
  };
  const setAreaChartOptionSeries = (wiseInvestmentInfo:HouseRentInfo) => {
    const { totalBuildArea, rentedBuildArea, rentingBuildArea } = wiseInvestmentInfo;
    const seriesItem = series[0] as any;
    (series[0] as any).label.formatter = (seriesItem:any) => {
      const { value, name } = seriesItem.data;
      return `{b|${name}}\n{c|${value}  (${getPercent(value, totalBuildArea)}%)}`;
    };
    seriesItem.data[0].value = rentedBuildArea;
    seriesItem.data[1].value = rentingBuildArea;
    chartOption.series = series.slice();
  };

  return {
    chartOption,
    setChartOptionSeries,
    setAreaChartOptionSeries,
  };
}
</script>
<style lang='scss' scoped>
.attract-investment-analysis {
    font-size: 14px;

    .summary {
        display: flex;
        align-items: center;

        img {
            width: 50px;
            height: 50px;
        }

        >.item,
        .left {
            display: flex;
            align-items: center;
        }

        >.item {
            flex: 1;
            padding: 16px;
            margin-right: 16px;
            line-height: 28px;
            border-radius: 6px;
            box-shadow: 2px 4px 8px 0 rgba(0, 0, 0, 0.16);

            &:last-child {
                margin: 0;
            }

            .left {
                &::after {
                    display: block;
                    width: 1px;
                    height: 30px;
                    margin: 0 50px;
                    content: "";
                    background-color: #000;
                    opacity: 0.1;
                }

                .text-box {
                    position: relative;
                    margin-left: 14px;
                    color: #7d8292;

                    >span {
                        font-size: 16px;
                        font-weight: 500;
                        color: #f65321;
                    }
                }
            }

            .right {
                color: #7d8292;

                span {
                    font-size: 16px;
                    font-weight: 500;
                    color: #f65321;
                }
            }
        }
    }

    .chart-box {
        display: flex;
        height: 65vh;
        margin-top: 20px;

        >div {
            flex: 1;
            height: 100%;
            padding: 16px;
            border: 1px solid #e6e4e3;
            border-radius: 6px;

            &:first-child {
                margin-right: 16px;
            }
        }
    }
}
</style>
