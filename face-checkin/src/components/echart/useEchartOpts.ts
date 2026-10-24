/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-01 10:05:56
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-08 14:54:52
 */

import { reactive, shallowRef } from 'vue';
import {
  LineECOption, BarECOption, BaseOption, Options,
} from './type';

interface Params<T extends Options> {
    chartType?:'line'|'bar',
    xAxis?:T['xAxis'],
    yAxis?:T['yAxis']
    title?:T['title']
    series?:T['series']
    tooltip?:T['tooltip']
    legend?:T['legend']
    grid?:T['grid']
    graphic?:T['graphic']
}

function useEchartOpts<T extends Options>({
  chartType, xAxis, yAxis, title, series = [], tooltip = {}, legend, grid, graphic,
}:Params<T>) {
  const shallowSeries = shallowRef(series);

  // toolip添加默认配置
  Object.assign(tooltip, {
    position(pos:[number, number], params:unknown, el:unknown, elRect:unknown, size:any) {
      const obj:any = { top: 10 };
      // 鼠标在canvas左侧
      if (pos[0] < size.viewSize[0] / 2) {
        obj.left = pos[0] + 10;
      } else {
        obj.right = size.viewSize[0] - pos[0];
      }
      obj.top = pos[1] + 10;
      return obj;
    },
  });

  const chartOption = reactive({
    xAxis,
    yAxis,
    title,
    tooltip,
    legend,
    series: shallowSeries,
    grid,
    graphic,
  });
  return {
    chartOption: chartOption as T,
  };
}
export {
  useEchartOpts,
};
