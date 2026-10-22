<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-06 16:24:42
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-17 15:34:50
-->
<template>
  <div class="charge-manage">
    <slot name="title" />
    <div class="summary">
      <div>
        <img src="~assets/images/operation-screen/charge-manage_summary_icon1.png">
        <div>
          {{ billInfo?.totalAmount }}<br>
          <span>累计收费(万)</span>
        </div>
      </div>
      <div>
        <img src="~assets/images/operation-screen/charge-manage_summary_icon2.png">
        <div>
          {{ billInfo?.thisYearAmount }}<br>
          <span>当年收费(万)</span>
        </div>
      </div>
    </div>
    <div class="echart-box">
      <echart :option="chartOption" />
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, computed,
} from 'vue';

import { graphic } from 'echarts';
import Echart, { BarECOption, useEchartOpts } from '~components/echart';
import CommandBigScreen, { BillInfo } from '~models/command-big-screen';

export default defineComponent({

  components: { Echart },
  props: {
  },
  setup() {
    const commandBigScreenInstance = new CommandBigScreen();
    commandBigScreenInstance.initPark();

    const { chartOption, setSeries } = useChartOption();
    const billInfo = ref<BillInfo>();
    const init = async () => {
      billInfo.value = await commandBigScreenInstance.getBillForScreen();
      setSeries(billInfo.value);
      // setSeries(houseRentInfo.value);
    };
    init();
    return {
      chartOption,
      setSeries,
      billInfo,
    };
  },
});

function useChartOption() { // 工单统计
  // const orderStatisticsOptions = ref()
  const feeData = {
    1: { // 1月份
      1: 45, // 类型 1租赁费、2物业费、3公寓租金、4企业水电费、4公寓水电、6场地预定
      2: 45,
      3: 45,
      4: 45,
      5: 45,
      6: 45,
    },
    2: { // 2月份
      1: 45,
      2: 45,
      3: 45,
      4: 45,
      5: 45,
      6: 45,
    },
    3: { // 2月份
      1: 50,
    },
  };

  const config = {
    1: {
      color: '#333FFF',
      name: '租赁费',
      data: [],
    },
    2: {
      color: '#3393FF',
      name: '公寓水电费',
      data: [],
    },
    3: {
      color: '#4DDEEA',
      name: '场地预订费',
      data: [],
    },
    4: {
      color: '#01BA85',
      name: '企业水电费',
      data: [],
    },
    5: {
      color: '#77E169',
      name: '物业费',
      data: [],
    },
    6: {
      color: '#7A79FF',
      name: '公寓租金',
      data: [],
    },
  };
  const { chartOption } = useEchartOpts<BarECOption>({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow', // 'shadow' as default; can also be 'line' or 'shadow'
      },
      valueFormatter: (value:unknown) => `${value}w`,
    },
    title: {
      text: '',
    },
    legend: {
      bottom: '-4%',
      show: true,
      itemWidth: 8,
      itemHeight: 8,
      itemGap: 14,
      width: '70%',
      textStyle: {
        color: '#fff',
        rich: {
          a: {
            color: '#fff',
            width: 60,
          },
        },
      },
      formatter: (name) => `{a|${name}} `,
    },
    grid: {
      left: '4%',
      top: '4%',
      right: '6%',
      bottom: '30%',
      borderColor: 'rgb(116, 140, 241,.2)',
      containLabel: true,
    },
    yAxis: {
      type: 'value',
      splitLine: {
        lineStyle: {
          color: 'rgba(116,140,241,.2)',
        },
      },
      axisLabel: {
        formatter: (vlaue:unknown) => `${vlaue}w`,
      },
    //   min: (value) => value.min + 20,
    },
    xAxis: {
      type: 'category',
      data: [],
    },
  });

  const setSeries = (billInfo:BillInfo) => {
    const itemBaseConf:BarECOption['series'] = {
      name: '',
      type: 'bar',
      stack: 'total',
      barWidth: '25%',
      itemStyle: {
        color: '#333FFF',
      },
      emphasis: {
        focus: 'series',
      },
    };

    const series:BarECOption['series'] = [];
    const xAxisData:string[] = [];
    billInfo.billListForScreen.forEach(({ month, amountData }) => {
      xAxisData.push(month);
      Object.entries(amountData).forEach(([key, value]) => {
        config[key].data.push(value);
      });
    });
    Object.values(config).forEach(({ data, name, color }) => {
      series.push({
        ...itemBaseConf,
        itemStyle: {
          color,
        },
        name,
        data,
      });
    });

    // {
    //   series.push({
    //     ...itemBaseConf,
    //     itemStyle: {
    //       color: config[i].color,
    //     },
    //     name: config[i].name,
    //     data: getValues(String(i)),
    //   });
    // }
    // return series;
    (chartOption as any).xAxis.data = xAxisData;
    chartOption.series = series;
  };
  // chartOption.series = series.value;
  return {
    chartOption,
    setSeries,
  };
}
</script>

<style lang='scss' scoped>
.charge-manage {
    height: h(312);
    margin-bottom: h(20);
    background: url('~assets/images/operation-screen/wise-investment_bg.png') no-repeat;
    background-size: 100% 100%;

    .summary {
        display: flex;
        align-items: center;
        padding-top: h(10);
        padding-left: 45px;
        margin-bottom: 10px;
        font-size: 20px;
        line-height: 24px;

        >div {
            position: relative;
            display: flex;
            align-items: center;
            padding-right: 40px;
            margin-left: 40px;

            &::after {
                position: absolute;
                top: 50%;
                right: 0;
                display: block;
                width: 1px;
                height: h(30);
                content: '';
                background: #fff;
                opacity: 0.2;
                transform: translateY(-45%);
            }

            &:last-child::after {
                display: none;
            }

            &:first-child {
                margin: 0;
            }

            img {
                width: 34px;
                height: 34px;
                margin-right: 26px;
            }

            span {
                font-size: 16px;
                color: #6c6e8e;
            }
        }
    }

    .echart-box {
        height: h(180);
    }
}
</style>
