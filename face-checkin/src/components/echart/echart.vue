<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-02 10:02:05
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-08 15:09:26
-->
<template>
  <div
    id="v-echart"
    ref="chartRef"
    class="echart"
  >
    vue3
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, onMounted, watch, toRefs, nextTick,
} from 'vue';
import * as echarts from 'echarts/core';
import {
  LineChart, BarChart, PieChart, FunnelChart,
} from 'echarts/charts';

import {
  TitleComponent,
  // 组件类型的定义后缀都为 ComponentOption
  TooltipComponent,
  GridComponent,
  // 数据集组件
  DatasetComponent,
  // 内置数据转换器组件 (filter, sort)
  TransformComponent,
  LegendComponent,
} from 'echarts/components';// 组件

import { LabelLayout, UniversalTransition } from 'echarts/features';
import { CanvasRenderer } from 'echarts/renderers';
import { LineECOption, BarECOption, Options } from './type';

echarts.use([
  FunnelChart,
  LineChart,
  BarChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  TransformComponent,
  LabelLayout,
  UniversalTransition,
  CanvasRenderer,
  LegendComponent,
]);

export default defineComponent({
  name: 'Echart',
  props: {
    option: {
      type: Object as PropType<Options>,
      default: () => ({}),
    },
  },
  setup(props, { expose }) {
    const chartRef = ref(null);
    const { option } = toRefs(props);
    let chart:echarts.ECharts;
    onMounted(() => {
      chart = echarts.init(chartRef.value as unknown as HTMLElement);
      setTimeout(() => {
        window.onresize = () => {
          chart.resize();
        };
      }, 200);
    });
    watch(option, (value) => {
      nextTick(() => {
        // console.log(value, '---value');
        chart?.setOption(value);
      });
    }, {
      immediate: true,
      deep: true,
    });
    return {
      chartRef,
    };
  },
});

</script>
<style >
.echart {
    width: 100%;
    height: 100%;
}
</style>
