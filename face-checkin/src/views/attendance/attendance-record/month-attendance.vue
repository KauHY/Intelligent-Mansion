<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-02-24 14:19:58
 * @LastEditors: Xucaizhi3
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
import { getMonthAttendColumns } from './config';

export default defineComponent({
  name: 'MonthAttendance',
  components: { BaseTable },
  props: {},
  setup() {
    const baseConfig = ref<AttendanceBaseConfig>({
      api: '', exportApi: '', columns: {}, searchConf: {},
    });
    const columns = getMonthAttendColumns();
    const searchConf = reactive<SearchConf<['yearMonth']>>({
      yearMonth: {
        tag: 'month-picker',
        value: dayjs(new Date()).format('YYYY-MM'),
      },
    });
    baseConfig.value = {
      searchConf,
      columns,
      exportApi: 'monthRecordExportApi',
      api: 'monthAttendanceListPageApi',
    };
    return {
      baseConfig,
    };
  },
});
</script>
