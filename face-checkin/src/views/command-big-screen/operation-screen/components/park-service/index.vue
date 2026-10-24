<!--
 * @Description:园区服务
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-01 15:24:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-05 15:06:14
-->
<template>
  <div class="park-service">
    <slot name="title" />
    <div class="echart-box">
      <div class="left">
        <echart :option="orderStatisticsOption" />
      </div>
      <div class="right">
        <echart :option="orderAnswerOption" />
      </div>
    </div>
    <div class="notice">
      <a-carousel
        dot-position="left"
        :dots="false"
        autoplay
      >
        <div
          v-for="(item,index) in activityList"
          :key="index"
          class="notice-item"
        >
          <div class="left">
            <img
              src="~assets/images/operation-screen/park-service_not-ce_icon.png"
              alt=""
            >
            <div>
              {{ item.name }}
            </div>
          </div>
          <div>{{ getActivityTime(item) }}</div>
        </div>
      </a-carousel>
    </div>
  </div>
</template>
<script lang='ts'>

import {
  reactive, ref, defineComponent, PropType, computed,
} from 'vue';

import { graphic } from 'echarts';
import dayjs from 'dayjs';
import Echart, { LineECOption, BarECOption, useEchartOpts } from '~components/echart';
import CommandBigScreen, { ActivityInfo, OrderInfo } from '~models/command-big-screen';

export default defineComponent({
  components: {
    Echart,
  },
  setup() {
    const commandBigScreenInstance = new CommandBigScreen();
    commandBigScreenInstance.initPark();
    const getActivityTime = computed(() => (activityInfo:ActivityInfo) => {
      const { status, activityTimeEnd, activityTimeStart } = activityInfo;
      if (status === 3) return '进行中';
      const time = status === 4 ? activityTimeEnd : activityTimeStart;
      return dayjs(time).format('YYYY-MM-DD');
    });
    const activityList = ref<ActivityInfo[]>([]); // 园区活动
    const orderInfo = ref<OrderInfo>();
    const { orderStatisticsOption, setSeries: orderStatisticsSetSeries } = useStatistics();
    const { orderAnswerOption, setSeries: orderAnswerSetSeries } = useAnswer();
    // const init = async () => {
    //   activityList.value = await commandBigScreenInstance.getActivityList();
    //   orderInfo.value = await commandBigScreenInstance.getOrderInfo();
    //   orderStatisticsSetSeries(orderInfo.value.orderStatistic);
    //   orderAnswerSetSeries(orderInfo.value);
    // };
    // init();
    return {
      orderStatisticsOption,
      orderAnswerOption,
      getActivityTime,
      activityList,
      orderInfo,
    };
  },
});
function useStatistics() { // 工单统计
  // const orderStatisticsOptions = ref()
  const series:BarECOption['series'] = [
    {
      name: '已完成',
      type: 'bar',
      stack: 'total',
      barWidth: '25%',
      itemStyle: {
        color: '#333FFF',
      },
      emphasis: {
        focus: 'series',
      },
      data: [],
    },
    {
      name: '进行中',
      type: 'bar',
      stack: 'total',
      barWidth: '25%',
      emphasis: {
        focus: 'series',
      },
      itemStyle: {
        color: '#23B899',
      },
      data: [],
    },
  ];
  const { chartOption: orderStatisticsOption } = useEchartOpts<BarECOption>({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow', // 'shadow' as default; can also be 'line' or 'shadow'
      },
    },
    title: {
      text: '',
    },
    legend: {
      bottom: '0',
      itemWidth: 8,
      itemHeight: 8,
      show: true,
      textStyle: {
        color: '#fff',
      },
    },
    grid: {
      left: '4%',
      top: '1%',
      right: '6%',
      bottom: '18%',
      borderColor: 'rgb(116, 140, 241,.2)',
      containLabel: true,
    },
    xAxis: {
      type: 'value',
      splitLine: {
        lineStyle: {
          color: 'rgba(116,140,241,.2)',
          type: 'dashed',
        },
      },
    },
    yAxis: {
      type: 'category',
      data: ['报事服务', '报修服务', '物品放行'],
    },
    series,
  });

  const setSeries = (orderStatistic:OrderInfo['orderStatistic']) => {
    const finishCounts:number[] = [];
    const ongoingCounts:number[] = [];
    Object.values(orderStatistic).forEach((item) => {
      finishCounts.push(item.finishCount || 0);
      ongoingCounts.push(item.ongoingCount || 0);
    });
    series[0].data = finishCounts;
    series[1].data = ongoingCounts;
    orderStatisticsOption.series = series.slice();
  };
  return {
    orderStatisticsOption,
    setSeries,
  };
}

function useAnswer() { // 工单响应
  const series:BarECOption['series'] = [
    {
      name: '工单数量',
      type: 'bar',
      barWidth: '25%',
      data: [70, 100],
      itemStyle: {
        color: new graphic.LinearGradient(0, 0, 1, 1, [{
          offset: 0,
          color: '#00E3D2',
        },
        {
          offset: 1,
          color: '#3F77E9',
        },
        ]),
      },
    },
  ];
  const { chartOption: orderAnswerOption } = useEchartOpts<BarECOption>({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow', // 'shadow' as default; can also be 'line' or 'shadow'
      },
    },
    legend: {
      bottom: '0',
      show: true,
      itemWidth: 8,
      itemHeight: 8,
      textStyle: {
        color: '#fff',
      },
    },
    grid: {
      left: '4%',
      top: '1%',
      right: '6%',
      bottom: '18%',
      borderColor: 'rgb(116, 140, 241,.2)',
      containLabel: true,
    },
    xAxis: {
      type: 'value',
      splitLine: {
        lineStyle: {
          color: 'rgba(116,140,241,.2)',
          type: 'dashed',
        },
      },
    },
    yAxis: {
      type: 'category',
      data: ['处理工单', '响应工单'],
    },
    series,
  });
  const setSeries = ({ finishCount, ongoingCount }:OrderInfo) => {
    series[0].data = [finishCount || 0, ongoingCount || 0];
    orderAnswerOption.series = series.slice();
  };
  return {
    orderAnswerOption,
    setSeries,
  };
}
</script>

<style lang='scss' scoped>
.park-service {
    position: relative;
    height: h(312);
    margin-bottom: h(20);
    overflow: hidden;
    background: url('~assets/images/operation-screen/park-service-bg.png') no-repeat;
    background-size: 100% 100%;

    .echart-box {
        position: absolute;
        top: h(82);
        left: 0;
        display: flex;
        justify-content: space-between;
        width: 100%;
        height: h(190);
        padding: 0 15px;

        >div {
            flex: 1;
            height: 100%;
            padding: 10px h(10);

            &.left {
                margin-right: 10px;
            }
        }
    }

    .notice {
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        height: h(50);
        padding: 0 20px;

        .notice-item {
            display: flex !important;
            align-items: center;
            justify-content: space-between;
            height: h(50);
            color: #fff;

            >div:last-child {
                width: 100px;
                white-space: nowrap;
            }

            .left {
                display: flex;
                flex: 1;
                align-items: center;
                justify-content: flex-start;
                overflow: hidden;

                >img {
                    width: 28px;
                    height: 28px;
                    margin-right: 6px;
                }

                >div {
                    flex: 1;
                    margin-right: 30px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                }
            }
        }
    }
}
</style>
