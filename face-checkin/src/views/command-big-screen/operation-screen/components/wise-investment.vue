<!--
 * @Description:智慧招商
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-06 10:55:12
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-29 11:23:51
-->
<template>
  <div class="wise-investment">
    <slot name="title" />
    <div class="summary">
      <div>
        {{ houseRentInfo?.totalHouseCount }}<br>
        <span>房间总数</span>
      </div>
      <div>
        {{ totalBuildArea }}&nbsp;w<br>
        <span>总面积(㎡)</span>
      </div>
      <div>
        {{ houseRentInfo?.chanceCount }}<br>
        <span>总商机(条)</span>
      </div>
    </div>
    <div class="charts-section">
      <div class="tabs">
        <div
          :class="{activited:activited===1}"
          @click="switchChart(1)"
        >
          房间
        </div>
        <div
          :class="{activited:activited===2}"
          @click="switchChart(2)"
        >
          面积
        </div>
      </div>
      <div class="echarts-box">
        <echart :option="chartOption" />
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, computed,
} from 'vue';

import { graphic } from 'echarts';
import Echart, { PieECOption, useEchartOpts } from '~components/echart';
import CommandBigScreen, { HouseRentInfo } from '~models/command-big-screen';
import { getPercent, mathjs } from '~utils/tool';

interface DataConf{
 1:[number, number],
 2:[number, number],
}
export default defineComponent({
  components: { Echart },
  props: {
  },
  setup() {
    const commandBigScreenInstance = new CommandBigScreen();
    commandBigScreenInstance.initPark();

    const houseRentInfo = ref<HouseRentInfo>();
    const dataConf = reactive<DataConf>({
      1: [0, 0],
      2: [0, 0],
    });
    const {
      chartOption, activited, switchChart,
    } = useChartOption(dataConf);
    const totalBuildArea = computed(() => mathjs.divide(houseRentInfo.value?.totalBuildArea || 0, 1000).toFixed(2));
    // const getPercent = (num:number, totalNum:number) => Number(((num / totalNum) * 100).toFixed(2)) || 0;
    const init = async () => {
      houseRentInfo.value = await commandBigScreenInstance.getHouseRentData();
      // setSeries(houseRentInfo.value);
      const {
        rentedBuildArea, rentingBuildArea, rentedHouseCount, rentingHouseCount, totalBuildArea, totalHouseCount,
      } = houseRentInfo.value;

      dataConf[1] = [Number(getPercent(rentedHouseCount, totalHouseCount)), Number(getPercent(rentingHouseCount, totalHouseCount))];
      dataConf[2] = [Number(getPercent(rentedBuildArea, totalBuildArea)), Number(getPercent(rentingBuildArea, totalBuildArea))];
      switchChart(1);
    };

    init();
    return {
      chartOption,
      activited,
      switchChart,
      houseRentInfo,
      totalBuildArea,
    };
  },
});

function useChartOption(dataConf:DataConf) { // 税收分布
  const series:PieECOption['series'] = [
    {
      name: '房间',
      type: 'pie',
      radius: ['35%', '70%'],
      right: '-55%',
      label: {
        show: false,
        position: 'center',
      },
      labelLine: {
        show: false,
      },
      data: [
        {
          value: 40,
          name: '已租率',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#FEF880',
            },
            {
              offset: 1,
              color: '#5BF2BA',
            },
            ]),
          },
        },
        {
          value: 30,
          name: '待租率',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#FDA3C7',
            },
            {
              offset: 1,
              color: '#98B3FF',
            },
            ]),
          },
        },
      ],
    },
  ];

  const { chartOption } = useEchartOpts<PieECOption>({
    tooltip: {
      trigger: 'item',
      // formatter: '{a}<br />{b}:{c}%',
      valueFormatter: (value:unknown) => `${value}%`,
    },
    legend: {
      left: '3%',
      top: 'center',
      orient: 'vertical',
      itemGap: 30,
      itemHeight: 10,
      itemWidth: 10,
      icon: 'circle',
      textStyle: {
        color: '#fff',
        rich: {
          a: {
            color: '#6c6e8e',
            width: 65,
          },
        },
      },
    },
    series,
  });

  // const setSeries = (houseRentInfo:HouseRentInfo) => {
  const { data } = series[0];
  (chartOption as any).legend.formatter = (name:string) => {
    const value = (data as {value:string, name:string}[])?.find((item) => item.name === name)?.value;
    return `{a|${name}}${Number(value || 0).toFixed(0)}%`;
  };
  // };

  const activited = ref(1);
  const switchChart = (type:1|2) => {
    const dataItem = dataConf[type];
    activited.value = type;
    const series = (chartOption as any).series.slice();
    const data = series[0].data;
    data[0].value = dataItem[0];
    data[1].value = dataItem[1];
    chartOption.series = series;
  };
  const loopSwitch = () => {
    setTimeout(() => {
      const type = activited.value === 1 ? 2 : 1;
      switchChart(type);
      loopSwitch();
    }, 1000 * 5);
  };
  loopSwitch();
  return {
    chartOption,
    switchChart,
    activited,
  };
}

</script>
<style lang='scss' scoped>
.wise-investment {
    height: h(312);
    margin-bottom: h(20);
    overflow: hidden;
    background: url('~assets/images/operation-screen/wise-investment_bg.png') no-repeat;
    background-size: 100% 100%;

    .summary {
        display: flex;
        align-items: center;
        padding-top: h(10);
        font-size: 20px;
        line-height: 24px;

        >div {
            position: relative;
            flex: 1;
            text-align: center;

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

            span {
                font-size: 16px;
                color: #6c6e8e;
            }
        }
    }

    .charts-section {
        display: flex;
        align-items: center;
        padding: h(20) 20px;

        >.tabs {
            width: 100px;
            margin-right: 10px;
            font-size: 18px;
            color: #6c6e8e;

            >div {
                width: 98px;
                height: h(30);
                margin-bottom: h(15);
                line-height: h(30);
                text-align: center;
                cursor: pointer;
                background: url('~assets/images/operation-screen/wise-investment_icon1.png') no-repeat 100%/100%;

                &.activited {
                    color: #fff;
                    background: url('~assets/images/operation-screen/wise-investment_icon2.png') no-repeat 100%/100%;
                }
            }
        }

        >.echarts-box {
            flex: 1;
            height: h(150);
            background: url('~assets/images/operation-screen/wise-investment_echart-box_bg.gif') no-repeat;
            background-size: 100% 100%;
        }
    }
}
</style>
