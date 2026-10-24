/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-02 10:21:33
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-02 17:45:48
 */
import * as echarts from 'echarts/core';
import { XAXisComponentOption, YAXisComponentOption } from 'echarts';
import {
  TitleComponentOption,
  TooltipComponentOption,
  GridComponentOption,
  DatasetComponentOption,
  AriaComponentOption,
  AxisPointerComponentOption,
  LegendComponentOption,
} from 'echarts/components';// 组件
import {
  // 系列类型的定义后缀都为 SeriesOption
  BarSeriesOption,
  LineSeriesOption,
  PieSeriesOption,
  FunnelSeriesOption,
} from 'echarts/charts';

type BaseOptionType = XAXisComponentOption|YAXisComponentOption|TitleComponentOption|TooltipComponentOption|LegendComponentOption|GridComponentOption

type BaseOption = echarts.ComposeOption<BaseOptionType>

type LineECOption = echarts.ComposeOption<LineSeriesOption|BaseOptionType>

type BarECOption = echarts.ComposeOption<BarSeriesOption|BaseOptionType>

type PieECOption = echarts.ComposeOption<PieSeriesOption|BaseOptionType>

type FunnelOption = echarts.ComposeOption<FunnelSeriesOption|BaseOptionType>

type Options = LineECOption|BarECOption|PieECOption|FunnelOption

export {
  LineECOption, //
  BarECOption,
  Options,
  BaseOption,
  PieECOption,
  FunnelOption,
};
