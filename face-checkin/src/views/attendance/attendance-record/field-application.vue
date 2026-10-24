<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-07-05 15:50:24
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
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { SearchConf } from '~components/search-box/search-box.vue';
import { AttendanceBaseConfig } from '~models/attendance';
import BaseTable from './base.vue';
import { getApplicationColumns } from './config';

export default defineComponent({
  name: 'MonthAttendance',
  components: { BaseTable },
  props: {},
  setup() {
    const baseConfig = ref<AttendanceBaseConfig>({
      api: '', exportApi: '', columns: {}, searchConf: {},
    });
    const options = useHashMapDics<['FIELDWORK_APPLY_STATE', 'PUNCH_TYPE']>(['FIELDWORK_APPLY_STATE', 'PUNCH_TYPE']);
    const columns = getApplicationColumns(options);
    const searchConf = reactive<SearchConf<['date', 'createTime', 'state']>>({
      date: {
        tag: 'date-range',
        value: '',
        placeholder: ['外勤开始时间', '外勤结束时间'],
        startTimeKey: 'fieldworkStartTime',
        endTimeKey: 'fieldworkEndTime',
      },
      createTime: {
        tag: 'date-range',
        value: '',
        placeholder: ['申请开始时间', '申请结束时间'],
        startTimeKey: 'applyStartTime',
        endTimeKey: 'applyEndTime',
      },
      state: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: () => options.FIELDWORK_APPLY_STATE,
      },
    });
    baseConfig.value = {
      searchConf,
      columns,
      exportApi: 'applicationRecordExportApi',
      api: 'fieldApplicationListPageApi',
      type: 'field-application',
    };
    return {
      baseConfig,
    };
  },
});
</script>
