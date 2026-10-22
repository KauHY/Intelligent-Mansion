<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-07-07 18:48:11
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
import { getDayAttendColumns } from './config';

export default defineComponent({
  name: 'MonthAttendance',
  components: { BaseTable },
  props: {
    clockDay: {
      type: String,
      default: '',
    },
    employeeName: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    let startTime;
    let endTime;
    if (props.clockDay) {
      startTime = dayjs(props.clockDay).format('YYYY-MM-DD');
      endTime = dayjs(props.clockDay).endOf('month').format('YYYY-MM-DD');
    } else {
      startTime = `${dayjs().format('YYYY')}-${dayjs().subtract(1, 'day').format('MM')}-01 00:00:00`;
      endTime = `${dayjs().subtract(1, 'day').format('YYYY-MM-DD')} 23:59:59`;
    }
    const baseConfig = ref<AttendanceBaseConfig>({
      api: '', exportApi: '', columns: {}, searchConf: {},
    });
    const options = useHashMapDics<['ATTENDANCE_RESULT_TYPE', 'PUNCH_TYPE']>(['ATTENDANCE_RESULT_TYPE', 'PUNCH_TYPE']);
    const columns = getDayAttendColumns(options);
    const searchConf = reactive<SearchConf<['punchInTime', 'punchOutTime', 'attendanceResult', 'punchType']>>({
      punchInTime: {
        tag: 'date',
        value: startTime,
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        placeholder: '开始时间',
      },
      punchOutTime: {
        tag: 'date',
        value: endTime,
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        placeholder: '结束时间',
      },
      attendanceResult: {
        tag: 'select',
        value: '',
        placeholder: '请选择考勤结果',
        options: () => options.ATTENDANCE_RESULT_TYPE,

      },
      punchType: {
        tag: 'select',
        value: '',
        placeholder: '请选择打卡方式',
        options: () => options.PUNCH_TYPE,
      },
    });
    baseConfig.value = {
      searchConf,
      columns,
      exportApi: 'dayRecordExportApi',
      api: 'dayAttendanceListPageApi',
      clockInfo: { employeeName: props.employeeName },
    };
    return {
      baseConfig,
    };
  },
});
</script>
