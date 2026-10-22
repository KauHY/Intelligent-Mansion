<!--
 * @Description:企业总览
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-02 14:35:06
 * @LastEditors: guochao82 guochao82@crc.com.hk
 * @LastEditTime: 2022-11-30 16:50:44
-->
<template>
  <div class="enterprise-overview">
    <slot name="title" />
    <div class="summary">
      <div>
        <img src="~assets/images/operation-screen/enterprise-overview_summary_icon1.png">
        <div>
          {{ companyInfo?.companyTotal }}<br>
          <span>入驻企业总数</span>
        </div>
      </div>
      <div>
        <img src="~assets/images/operation-screen/enterprise-overview_summary_icon2.png">
        <div>
          {{ companyInfo?.employeeTotal }}<br>
          <span>入驻人员数</span>
        </div>
      </div>
      <div>
        <img src="~assets/images/operation-screen/enterprise-overview_summary_icon3.png">
        <div>
          {{ ((companyInfo?.occupancyRate||0)*100).toFixed(2) }}%<br>
          <span>入驻率</span>
        </div>
      </div>
    </div>
    <div class="echart-box">
      <div class="left">
        <echart :option="taxDistributionOption" />
      </div>
      <div class="right">
        <echart :option="industryDistributionOption" />
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';
import { graphic } from 'echarts';
import CommandBigScreen, { CompanyOverview } from '~models/command-big-screen';
import { useHashMapDics } from '~composition/useHashMapDic';
import { mathjs } from '@/utils/tool';
import Echart, { PieECOption, useEchartOpts } from '~components/echart';

export default defineComponent({
  components: { Echart },
  props: {
  },
  setup() {
    const commandBigScreenInstance = new CommandBigScreen();
    commandBigScreenInstance.initPark();
    const options = useHashMapDics<['COMPANY_TAX_LEVEL', 'INDUSTRY_FIELD']>(['COMPANY_TAX_LEVEL', 'INDUSTRY_FIELD']);
    const { taxDistributionOption, setSeries: taxDistributionSetSeries } = useTaxDistribution(options.COMPANY_TAX_LEVEL);
    const { industryDistributionOption, setSeries: industryDistributionSetSeries } = useIndustryDistribution(options.INDUSTRY_FIELD);
    const companyInfo = ref<CompanyOverview>();

    const init = async () => {
      companyInfo.value = await commandBigScreenInstance.getCompanyOverview();
      taxDistributionSetSeries(companyInfo.value.taxType);
      industryDistributionSetSeries(companyInfo.value.industryType);
    };
    init();
    return {
      taxDistributionOption,
      industryDistributionOption,
      companyInfo,
    };
  },
});
function useTaxDistribution(options:SelectOptionsMap) { // 税收分布
  const series:PieECOption['series'] = [
    {
      name: '税收分布',
      type: 'pie',
      radius: ['45%', '70%'],
      left: '-55%',
      label: {
        show: false,
        position: 'center',
      },
      labelLine: {
        show: false,
      },
      data: [
        {
          value: 1048,
          name: '500w以上',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#68F3B5',
            },
            {
              offset: 1,
              color: '#E2F78A',
            },
            ]),
          },
        },
        {
          value: 735,
          name: '100-500w',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#4389F1',
            },
            {
              offset: 1,
              color: '#86BCFD',
            },
            ]),
          },
        },
        {
          value: 580,
          name: '50-99w',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#FFFE95',
            },
            {
              offset: 1,
              color: '#F487B0',
            },
            ]),
          },
        },
        {
          value: 484,
          name: '30-49w',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#7AF5E4',
            },
            {
              offset: 1,
              color: '#56B6D6',
            },
            ]),
          },
        },
        {
          value: 300,
          name: '30w以下',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#EEB7E9',
            },
            {
              offset: 1,
              color: '#FDA3C7',
            },
            ]),
          },
        },
      ],
    },
  ];
  const { chartOption: taxDistributionOption } = useEchartOpts<PieECOption>({
    tooltip: {
      trigger: 'item',
      valueFormatter: (value:unknown) => `${value}%`,
    },
    title: {
      text: '税收分布',
      left: '12%',
      top: 'center',
      textStyle: {
        fontSize: 14,
        color: '#6c6e8e',
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
  const setSeries = (taxType:CompanyOverview['taxType']) => {
    const { data } = series[0];
    data?.forEach((item, index) => {
      const tempItem = item as any;
      const key = index + 1;
      tempItem.name = options[key].label;
      tempItem.value = mathjs.multiply((taxType[key] || 0), 100).toFixed(2);
    });

    (taxDistributionOption as any).legend.formatter = (name:string) => {
      const value = (data as {value:string, name:string}[])?.find((item) => item.name === name)?.value;
      return `{a|${name}}${value}%`;
    };
  };

  return {
    taxDistributionOption,
    setSeries,
  };
}

function useIndustryDistribution(options:SelectOptionsMap) { // 行业分布
  const series:PieECOption['series'] = [
    {
      name: '行业分布',
      type: 'pie',
      radius: ['45%', '70%'],
      left: '-60%',
      label: {
        show: false,
      },
      labelLine: {
        show: false,
      },
      data: [
        {
          value: 30,
          name: '全球服务',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#68F3B5',
            },
            {
              offset: 1,
              color: '#E2F78A',
            },
            ]),
          },
        },
        {
          value: 20,
          name: '新能源环保',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#EEB7E9',
            },
            {
              offset: 1,
              color: '#FDA3C7',
            },
            ]),
          },
        },
        {
          value: 10,
          name: '医疗器械',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#7AF5E4',
            },
            {
              offset: 1,
              color: '#56B6D6',
            },
            ]),
          },
        },
        {
          value: 10,
          name: '信息技术',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#FFFE95',
            },
            {
              offset: 1,
              color: '#F487B0',
            },
            ]),
          },
        },
        {
          value: 10,
          name: '生物医药',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#3DD951',
            },
            {
              offset: 1,
              color: '#78E39C',
            },
            ]),
          },
        },
        {
          value: 10,
          name: '通信技术',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#38ACEB',
            },
            {
              offset: 1,
              color: '#84E4F5',
            },
            ]),
          },
        },
        {
          value: 10,
          name: '其他',
          itemStyle: {
            color: new graphic.LinearGradient(0, 0, 1, 1, [{
              offset: 0,
              color: '#786CF8',
            },
            {
              offset: 1,
              color: '#B9B8FF',
            },
            ]),
          },
        },
      ],
    },
  ];
  const { chartOption: industryDistributionOption } = useEchartOpts<PieECOption>({
    tooltip: {
      trigger: 'item',
      valueFormatter: (value:unknown) => `${value}%`,
    },
    title: {
      text: '行业分布',
      left: '13%',
      top: 'center',
      textStyle: {
        fontSize: 14,
        color: '#6c6e8e',
      },
    },
    legend: {
      left: '40%',
      top: 'center',
      orient: 'vertical',
      itemGap: 18,
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

  const setSeries = (industry:CompanyOverview['industryType']) => {
    const { data } = series[0];
    data?.forEach((item, index) => {
      const tempItem = item as any;
      const key = index + 1;
      tempItem.name = options[key].label;
      tempItem.value = ((industry[key] || 0) * 100).toFixed(2);
    });
    (industryDistributionOption as any).legend.formatter = (name:string) => {
      const value = (data as {value:string, name:string}[])?.find((item) => item.name === name)?.value;
      return `{a|${name}}${value}%`;
    };
  };

  return {
    industryDistributionOption,
    setSeries,
  };
}
</script>

<style lang='scss' scoped>
.enterprise-overview {
    height: h(282);
    background: url('~assets/images/operation-screen/enterprise-overview_bg.png') no-repeat;
    background-size: 100% 100%;

    .summary {
        display: flex;
        align-items: center;
        padding-top: h(10);
        padding-left: 45px;
        margin-bottom: h(10);
        font-size: 20px;
        line-height: 24px;

        >div {
            position: relative;
            display: flex;
            align-items: center;
            padding-right: 70px;
            margin-left: 70px;

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
        display: flex;
        align-items: center;
        height: h(170);
        padding-left: 45px;

        >.left {
            flex: 40%;
            margin-right: 60px;
        }

        >div {
            flex: 60%;
            height: 100%;
        }
    }
}
</style>
