<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-07-06 17:06:38
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <div class="month-attendance">
    <BaseTable :base-config="baseConfig" />
  </div>
</template>

<script lang="ts">
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';
import * as dayjs from 'dayjs';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { SearchConf } from '~components/search-box/search-box.vue';
import { AttendanceBaseConfig } from '~models/attendance';
import BaseTable from './base.vue';
import { getClockInColumns } from './config';

export default defineComponent({
  name: 'MonthAttendance',
  components: { BaseTable },
  props: {
    employeeName: {
      type: String,
      default: '',
    },
    clockDay: {
      type: String,
      default: '',
    },
    parkOrgId: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    console.log(props, props.employeeName, 'props');
    const baseConfig = ref<AttendanceBaseConfig>({
      api: '', exportApi: '', columns: {}, searchConf: {},
    });
    const startTime = `${dayjs().format('YYYY-MM-')}01`;
    const endTime = dayjs().format('YYYY-MM-DD');
    // const clockDay = props.clockDay;
    const options = useHashMapDics<['ATTENDANCE_RESULT_TYPE', 'PUNCH_TYPE']>(['ATTENDANCE_RESULT_TYPE', 'PUNCH_TYPE']);
    const columns = getClockInColumns(options);
    const searchConf = reactive<SearchConf<['date', 'type']>>({
      date: {
        tag: 'date-range',
        value: props.clockDay ? [props.clockDay, props.clockDay] : [startTime, endTime],
        placeholder: ['打卡开始时间', '打卡结束时间'],
        endTimeKey: 'punchDateTimeEnd',
        startTimeKey: 'punchDateTimeStart',
      },
      type: {
        tag: 'select',
        value: '',
        placeholder: '请选择打卡方式',
        options: () => options.PUNCH_TYPE,
      },
    });
    baseConfig.value = {
      searchConf,
      columns,
      exportApi: 'punchRecordExportApi',
      api: 'clockRecordListPageApi',
      clockInfo: props,
      type: 'clock-record',
    };
    return {
      baseConfig,
    };
  },
});
</script>
