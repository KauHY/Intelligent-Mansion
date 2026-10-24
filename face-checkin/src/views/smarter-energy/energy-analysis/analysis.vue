<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-01 12:40:33
-->
<template>
  <div class="menu-list content-section">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="searchMethod(activeKey)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate()"
        >
          导出
        </a-button>
      </template>
    </search-box>
    <div
      v-if="isExitData"
      class="button-box"
    >
      <div
        v-for="(item,index) in listArr"
        :key="index"
        class="listCss"
        :class="{'activeCss':activeKey===item.type}"
        @click="chooseComponent(item.type)"
      >
        <img
          :src="item.icon"
          class="icon-img"
          alt=""
        >
        {{ item.title }}
      </div>
    </div>
    <div
      v-if="isExitData"
      class="demo"
    >
      <Echart :option="chartOption" />
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import Building from '~models/building';
import Echart, { LineECOption, BarECOption, useEchartOpts } from '~components/echart';
import { useHashMapDics } from '~composition/useHashMapDic';
import SmarterMeter from '~models/smarter-energy';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useCharts } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';
import { useDownload } from '~composition/useDownload';
import { getYearsToNow, getMonths } from '~utils/tool';
import CustomerVo from '~models/smarter-apartment';
import {
  categoryOpt, timeTypeOpt,
} from './config';

const userInstance = new SmarterMeter();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox, Echart },
  setup() {
    const {
      parkOpts,
      buildingOpts,
      getParkOpts,
      getBuildingOpts,
    } = useParkInfoOpts();
    const meterNameOpts = ref<SelectOptions>([]);
    const isExitData = ref<Boolean>(false);
    // 初始化
    const {
      monthToNow,
      activeKey,
      activeMeterType,
      listArr,
      yearsToNow,
      dateMonth,
      options,
      lineRef,
      BarRef,
    } = useInit();

    // 搜索配置项目
    const searchConf = reactive<SearchConf<['parkOrgId', 'buildingId', 'meterId', 'deviceType', 'month', 'year', 'timeType']>>({
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        options: parkOpts,
        placeholder: '请选择所属园区',
        handleChange: (value:string) => {
        },
      },
      buildingId: {
        tag: 'select',
        value: '',
        placeholder: '所属楼栋',
        options: buildingOpts,
        handleChange: (value:string) => {},
      },
      deviceType: {
        tag: 'select',
        value: 6,
        placeholder: '请选择表类别',
        options: categoryOpt,
        allowClear: false,
        handleChange: (value:number) => {
          searchConf.meterId.value = '';
          changeMeterType(searchConf.parkOrgId.value);
          activeMeterType.value = value;
        },
      },
      meterId: {
        tag: 'select',
        value: '',
        placeholder: '请选择表计名称',
        filterOption: true,
        showSearch: true,
        options: () => meterNameOpts.value,
      },

      timeType: {
        tag: 'select',
        value: 2,
        placeholder: '请选择',
        options: timeTypeOpt,
        handleChange: (value:string) => {},
      },
      month: {
        tag: 'select',
        value: new Date().getMonth() + 1,
        hidden: false,
        placeholder: '请选择月份',
        options: () => monthToNow.value,
      },
      year: {
        tag: 'select',
        value: new Date().getFullYear(),
        hidden: true,
        placeholder: '请选择年份',
        options: () => yearsToNow.value,
      },
    });
    searchConf.parkOrgId.handleChange = (parkOrgId:string) => {
      searchConf.meterId.value = '';
      searchConf.buildingId.value = '';
      getBuildingOpts(parkOrgId);
      changeMeterType(parkOrgId);
    };
    searchConf.timeType.handleChange = (parkOrgId?:number) => {
      if (parkOrgId === 1) {
        searchConf.year.hidden = false;
        searchConf.month.hidden = true;
        yearsToNow.value = getYearsToNow(1990);
      } else {
        searchConf.year.hidden = true;
        searchConf.month.hidden = false;
      }
    };

    const { chartOption } = useEchartOpts<LineECOption|BarECOption>({
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow',
        },
      },
      title: {
        text: '',
      },
      yAxis: {
        type: 'value',
      },
    });
    // 图标查询
    const {
      chartData,
      search,
    } = useCharts({
      searchMethod: userInstance.listMeterConsumptionAnalysis,
      searchConf,
    });

    const searchMethod = async (chartType:string, noRequest = false) => {
      if (searchConf.timeType.value === 1) {
        searchConf.month.value = '';
        if (!searchConf.year.value) {
          message.warn('请先选择年份');
          return;
        }
      }
      if (searchConf.timeType.value === 2) {
        if (!searchConf.month.value) {
          message.warn('请先选择月份');
          return;
        }
      }
      if (!noRequest) { // 是否调用接口
        await search(true);
      }
      chartOption.xAxis = {
        type: 'category',
        data: chartData.value?.times,
      };
      chartOption.series = [{ // 折线
        name: activeMeterType.value === 6 ? 'kw.h' : '吨',
        barWidth: '60%',
        type: chartType as any,
        data: chartData.value?.consumptions,
      }];
      // console.log(chartOption, chartData.value?.consumptions.length, '----chartOption');
      if (chartData.value?.consumptions.length && chartData.value?.consumptions.length > 0) isExitData.value = true;
    };
    const chooseComponent = async (type:string) => {
      activeKey.value = type;
      searchMethod(type, true);
    };
    const operate = async () => {
      const data = await userInstance.listanalysisdownload({
        buildingId: searchConf.buildingId.value,
        deviceType: searchConf.deviceType.value,
        meterId: searchConf.meterId.value,
        month: searchConf.month.value,
        parkOrgId: searchConf.parkOrgId.value,
        year: searchConf.year.value,
      });
      useDownload(data);
    };
    const changeMeterType = async (value?:any) => {
      meterNameOpts.value = await new CustomerVo().listDropDownDevices(searchConf.deviceType.value, value);
    };
    onMounted(async () => {
      monthToNow.value = getMonths();
      await getParkOpts();
      searchConf.parkOrgId.value = parkOpts.value[0]?.value;
      buildingOpts.value = await new Building().listBuildingDict(parkOpts.value[0]?.value);
      changeMeterType(searchConf.parkOrgId.value);
      searchMethod('line');
    });
    return {
      getMonths,
      monthToNow,
      isExitData,
      meterNameOpts,
      changeMeterType,
      operate,
      searchMethod,
      dateMonth,
      activeMeterType,
      activeKey,
      chooseComponent,
      listArr,
      yearsToNow,
      options,
      searchConf,
      lineRef,
      BarRef,
      chartOption,
      search,
    };
  },
});

function useInit() {
  const yearsToNow = ref<SelectOptions>([]);
  const monthToNow = ref<SelectOptions>([]);
  const dateMonth = ref<SelectOptions>([]);

  const activeKey = ref('line');
  const activeMeterType = ref(6);
  const listArr = [
    {
      title: '折线图',
      type: 'line',
      icon: require('@/assets/images/line.png'),
    }, {
      title: '圆柱图',
      type: 'bar',
      icon: require('@/assets/images/bar.png'),
    },
  ];
  const options = useHashMapDics<['METER_TYPE', 'MONTH']>(['METER_TYPE', 'MONTH']);
  const lineRef = shallowRef();
  const BarRef = shallowRef();
  const lineOpts = ref<LineECOption>({
    title: {
      text: '',
    },
    tooltip: {},
    xAxis: {
      data: [],
    },
    yAxis: {
    },
    series: [
      {
        name: 'kw·h',
        type: 'line',
        data: [],
      },
    ],
  });

  const barOpts = ref<BarECOption>({
    title: {
      text: '',
    },
    tooltip: {},
    xAxis: {
      data: [],
    },
    yAxis: {},
    series: [
      {
        name: '销量',
        type: 'bar',
        data: [],
      },
    ],
  });
  const init = async () => {
  };
  init();
  return {
    monthToNow,
    activeMeterType,
    activeKey,
    listArr,
    yearsToNow,
    dateMonth,
    options,
    lineOpts,
    barOpts,
    lineRef,
    BarRef,
  };
}
</script>
<style lang="scss">
.button-box {
    display: flex;
    justify-content: flex-end;
    margin-right: 10px;
}

.icon-img {
    width: 14px;
    height: 14px;
    margin-right: 3px;
}

.listCss {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100px;
    height: 40px;
    font-size: 12px;
    line-height: 20px;
    color: #7d8292;
    text-align: center;
    cursor: pointer;
    border: 1px solid #eaecf1;
    border-radius: 14px;
}
// 选中时的样式
.activeCss {
    color: #fff;
    text-shadow: 0 2px 3px rgba(15, 44, 104, 0.3);
    background: linear-gradient(137deg, #41a1ff 0%, #376fe4 100%);
}

.demo {
    min-width: 500px;
    height: 80%;
}
</style>
