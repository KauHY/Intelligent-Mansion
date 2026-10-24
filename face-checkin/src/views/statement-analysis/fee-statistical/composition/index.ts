/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-16 10:10:46
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-28 15:37:48
 */
import { graphic } from 'echarts';
import {
  computed, reactive, ComputedRef, watchEffect, ref, shallowRef, onMounted,
} from 'vue';
import dayjs, { Dayjs } from 'dayjs';
import {
  PieECOption, BarECOption, LineECOption, useEchartOpts,
} from '~components/echart';
import StatementAnalysis, {
  SearchPaymentTrend,
  PaymentTrendItem,
  LateCustomerItem,
  TopStatistic,
} from '~models/statement-analysis';

import { getPercent } from '~utils/tool';

const statementAnalysisInstance = new StatementAnalysis();

export const useTimes = <T extends Object>(params:T, startMonthKey:string, endMonthKey:string) => {
  const times = computed({
    set(value:string[]|null) {
      console.log(value, 'value');
      const [startMonth, endMonth] = value || [];
      params[startMonthKey] = startMonth;
      params[endMonthKey] = endMonth;
    },
    get() {
      const startMonth = params[startMonthKey] as string;
      const endMonth = params[endMonthKey] as string;
      return startMonth ? [startMonth, endMonth] : [];
    },
  });
  return times;
};

// const getPercent = (num:number, totalNum:number) => {
//   if (!totalNum) return 0;
//   return (divide(num, totalNum) * 100).toFixed(2);
// };

export const usePayTrend = (title:string, parkOrgId: ComputedRef<string>, type:'overdueTrend'|'payTrend') => {
  const params = reactive<SearchPaymentTrend>({
    endMonth: dayjs(Date.now()).format('YYYY-MM'),
    startMonth: dayjs(Date.now()).subtract(11, 'month').format('YYYY-MM'),
    chargeType: 0,
  });
  const payTrendSeries:any = [
    {
      name: '待收金额',
      type: 'bar',
      barWidth: 16,
      emphasis: {
        focus: 'series',
      },
      itemStyle: {
        color: new graphic.LinearGradient(0, 0, 1, 1, [{
          offset: 0,
          color: '#91E9FF',
        },
        {
          offset: 1,
          color: '#58CFFF',
        },
        ]),
      },
      tooltip: {
        valueFormatter: (value:unknown) => `${value}w`,
      },
      data: [120, 132, 101, 134, 90, 230, 210],
    },
    {
      name: '已收金额',
      type: 'bar',
      emphasis: {
        focus: 'series',
      },
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
      tooltip: {
        valueFormatter: (value:unknown) => `${value}w`,
      },
      data: [220, 182, 191, 234, 290, 330, 310],
    },

    {
      name: '应收金额',
      type: 'line',
      // smooth: true,
      // symbol: 'none',
      itemStyle: {
        color: new graphic.LinearGradient(1, 0, 0, 1, [{
          offset: 0,
          color: '#4867E5',
        },
        {
          offset: 1,
          color: '#32CCFF',
        },
        ]),
      },
      tooltip: {
        valueFormatter: (value:unknown) => `${value}w`,
      },
      data: [250, 432, 401, 154, 190, 330, 310],
    },

  ];

  const overdueTrendSeries:any = [
    {
      name: '逾期金额',
      type: 'bar',
      barWidth: 16,
      emphasis: {
        focus: 'series',
      },
      itemStyle: {
        color: new graphic.LinearGradient(0, 0, 1, 1, [{
          offset: 0,
          color: '#FFB779',
        },
        {
          offset: 1,
          color: '#F08647',
        },
        ]),
      },
      tooltip: {
        valueFormatter: (value:unknown) => `${value}w`,
      },
      data: [220, 182, 191, 234, 290, 330, 310],
    },

    {
      name: '逾期客户数量',
      type: 'line',
      yAxisIndex: 1,
      itemStyle: {
        color: new graphic.LinearGradient(1, 0, 0, 1, [{
          offset: 0,
          color: '#4CE1E1',
        },
        {
          offset: 1,
          color: '#32F5FF',
        },
        ]),
      },
      tooltip: {
        valueFormatter: (value:unknown) => `${value}个`,
      },
      data: [250, 432, 401, 154, 190, 330, 310],
    },

  ];

  const { chartOption } = useEchartOpts<BarECOption>({
    title: {
      text: title,
      left: 10,
      top: 10,
      textStyle: {
        fontSize: 14,
        color: '#1F2E4D',
      },
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
      },
      // valueFormatter: (value:unknown) => `${value}w`,
    },
    legend: {
      left: 'center',
      bottom: '1%',
      icon: 'circle',
    },
    grid: {
      left: '1%',
      right: '4%',
      bottom: '14%',
      containLabel: true,
    },
    xAxis: [
      {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        axisLine: {
          lineStyle: {
            color: '#7D8292',
          },
        },
      },
    ],
    yAxis: [
      {
        type: 'value',
        splitLine: {
          lineStyle: {
            color: '#C8D0D7',
            type: 'dashed',
          },
        },
        axisLine: {
          lineStyle: {
            color: '#7D8292',
          },
        },
        axisLabel: {
          formatter: (vlaue:unknown) => `${vlaue}w`,
        },
      },
      {
        type: 'value',
        splitLine: {
          lineStyle: {
            color: '#C8D0D7',
            type: 'dashed',
          },
        },
        axisLine: {
          lineStyle: {
            color: '#7D8292',
          },
        },
        minInterval: 1,
        axisLabel: {
          formatter: (vlaue:unknown) => `${vlaue}个`,
        },
      },
    ],
    series: [],
  });

  const disabledDate = (currentDate:Dayjs) => {
    const { endMonth, startMonth } = params;
    if (!endMonth && !startMonth) return false;
    const startIsDisable = startMonth && currentDate.diff(startMonth, 'month') > 11;
    const endIsDisable = dayjs(endMonth).diff(currentDate, 'month') > 10;
    return startIsDisable || endIsDisable;
  };

  const search = async (searchData:SearchPaymentTrend) => {
    if (type === 'payTrend') {
      const data = await statementAnalysisInstance.paymentTrend(searchData);
      const xData:string[] = [];
      const dueAmounts:number[] = [];
      const receivableAmounts:number[] = [];
      const receivedAmounts:number[] = [];
      data.forEach(({
        month, dueAmount, receivableAmount, receivedAmount,
      }) => {
        xData.push(month);
        dueAmounts.push(dueAmount);
        receivableAmounts.push(receivableAmount);
        receivedAmounts.push(receivedAmount);
      });
      payTrendSeries[0].data = dueAmounts;
      payTrendSeries[1].data = receivedAmounts;
      payTrendSeries[2].data = receivableAmounts;
      (chartOption as any).xAxis[0].data = xData;
      (chartOption as any).series = payTrendSeries;
      return;
    }
    const xData:string[] = [];
    const customerss:number[] = [];
    const dueAmounts:number[] = [];
    const data = await statementAnalysisInstance.overdueTrend(searchData);
    data.forEach(({
      month, dueAmount, customers,
    }) => {
      xData.push(month);
      customerss.push(customers);
      dueAmounts.push(dueAmount);
    });
    overdueTrendSeries[0].data = dueAmounts;
    overdueTrendSeries[1].data = customerss;
    (chartOption as any).xAxis[0].data = xData;
    (chartOption as any).series = overdueTrendSeries;
  };
  watchEffect(async () => {
    if (!parkOrgId.value) return;
    const data = {
      ...params,
      parkOrgId: parkOrgId.value,
    };
    await search(data);
    // console.log(data, parkOrgId.value, 'data---');
  });
  return {
    chartOption,
    params,
    times: useTimes(params, 'startMonth', 'endMonth'),
    disabledDate,
  };
};

export const useIncomeNnalysis = (parkOrgId: ComputedRef<string>, feeTypes:SelectOptionsMap) => {
  const incomeNnalysisParams = reactive<SearchPaymentTrend>({
    month: dayjs(Date.now()).format('YYYY-MM'),
  });

  const baseConf = {
    1: {
      itemStyle: {
        color: '#7A79FF',
      },
    },
    2: {
      itemStyle: {
        color: '#FFE1FF',
      },
    },
    3: {
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
    4: {
      itemStyle: {
        color: new graphic.LinearGradient(0, 0, 1, 1, [{
          offset: 0,
          color: '#91E9FF',
        },
        {
          offset: 1,
          color: '#58CFFF',
        },
        ]),
      },
    },
    5: {
      itemStyle: {
        color: '#B8FFF2',
      },
    },
  };
  const series:PieECOption['series'] = [
    {
      name: '收入分析',
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
  const { chartOption: incomeNnalysisOption } = useEchartOpts<PieECOption>({
    tooltip: {
      trigger: 'item',
      valueFormatter: (value:unknown) => `${value}元`,
    },
    title: {
      text: '收入分析',
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

  const search = async (searchData:SearchPaymentTrend) => {
    const { billData, totalAmount } = await statementAnalysisInstance.incomeDistribution(searchData);
    (series as any)[0].data = [];
    Object.entries(baseConf).forEach(([index, item]) => {
      (series as any)[0].data.push({
        ...item,
        value: billData[index],
        name: feeTypes[Number(index)].label,
      });
    });
    // incomeNnalysisOption.series = series.slice();
    (incomeNnalysisOption as any).legend.formatter = (name:string) => {
      const key = Object.values(feeTypes).find((item) => item.label === name)?.value;
      return `{a|${name}}${getPercent(billData[Number(key)], totalAmount)}%`;
    };
  };
  watchEffect(async () => {
    if (!parkOrgId.value) return;
    const data = {
      ...incomeNnalysisParams,
      parkOrgId: parkOrgId.value,
    };
    await search(data);
    console.log(data, parkOrgId.value, 'data---');
  });
  return {
    incomeNnalysisOption,
    incomeNnalysisParams,
  };
};

export const useOverdueCustomers = (parkOrgId:ComputedRef<string>) => {
  const overdueCustomersParams = reactive<SearchPaymentTrend>({
    month: dayjs(Date.now()).format('YYYY-MM'),
    chargeType: 0,
  });

  const columns = [
    {
      title: '排名',
      dataIndex: 'index',
      slots: { customRender: 'index' },
    },
    {
      title: '客户名称',
      dataIndex: 'companyName',
    },
    {
      title: '逾期天数',
      dataIndex: 'dueDays',
    },
    {
      title: '逾期金额（元）',
      dataIndex: 'dueAmount',
    },
  ];
  const overdueTrend = ref<LateCustomerItem[]>([]);
  const search = async (searchData:SearchPaymentTrend) => {
    overdueTrend.value = await statementAnalysisInstance.lateCustomers(searchData);
  };

  watchEffect(async () => {
    if (!parkOrgId.value) return;
    const data = {
      ...overdueCustomersParams,
      parkOrgId: parkOrgId.value,
    };
    await search(data);
    console.log(data, parkOrgId.value, 'data---');
  });

  const tableBoxRef = shallowRef();
  const tableMaxHeight = ref('20vh');
  onMounted(() => {
    // console.log(tableBoxRef.value.offsetHeight, '-xxxxxxxxx');
    tableMaxHeight.value = `${tableBoxRef.value?.offsetHeight - 60}px` || '20vh';
  });

  return {
    columns,
    overdueCustomersParams,
    overdueTrend,
    tableBoxRef,
    tableMaxHeight,
  };
};

export const useTopStatistic = (parkOrgId: ComputedRef<string>) => {
  const type = ref<1|2>(1);
  const topStatistic = ref<TopStatistic>();

  const search = async (params:SearchPaymentTrend) => {
    topStatistic.value = await statementAnalysisInstance.topStatistic(params);
  };

  watchEffect(async () => {
    if (!parkOrgId.value) return;
    const data = {
      parkOrgId: parkOrgId.value,
      type: type.value,
    };
    await search(data);
    console.log(data, parkOrgId.value, 'data---');
  });

  return {
    type,
    topStatistic,
  };
};
