<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-07-06 17:07:55
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <div class="menu-list">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="searchThing"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="download()"
        >
          导出
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{ scroll }">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #employeeName="{ record }">
            <span v-if="['field-application','clock-record'].includes(baseConfig.type)">{{ record.employeeName }}</span>
            <span
              v-else
              class="text-link"
              @click="goClock(record)"
            >{{ record.employeeName }}</span>
          </template>
          <template #punchType="{ record }">
            <span
              :class="record.type === 2 ? 'text-link' : ''"
              @click="openApplication(record)"
            >{{ options.PUNCH_TYPE[record.type]?.label }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <v-modal
      :visible="imgFlag"
      :width="300"
      title="外勤打卡"
      @cancel="imgFlag=false"
    >
      <div>
        <p class="info-css">
          <span>姓名：</span>
          <span>{{ applicatioInfo?.employeeName }}</span>
        </p>
        <p class="info-css">
          <span>外勤事由：</span>
          <span>{{ applicatioInfo?.extension?.reason }}</span>
        </p>
        <p class="info-css">
          <span>外勤照片：</span>
          <v-image
            width="80px"
            height="80px"
            :keys="applicatioInfo?.extension?.picKey"
          />
        </p>
      </div>
      <template #footer>
        <a-button
          type="primary"
          @click="imgFlag=false"
        >
          关闭
        </a-button>
      </template>
    </v-modal>
  </div>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, onActivated, PropType, toRef, toRefs,
} from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import * as dayjs from 'dayjs';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import AttendanceVo, { AttendanceInfo, AttendanceBaseConfig } from '~models/attendance';
import { useSearchListPage, useCachePage } from '~composition/common';
import CompanyMangement from '~models/company-mangement';

type ReqApi = 'monthAttendanceListPageApi' | 'dayAttendanceListPageApi' | 'clockRecordListPageApi' | 'fieldApplicationListPageApi'
type ExportApi = 'monthRecordExportApi' | 'dayRecordExportApi' | 'punchRecordExportApi' | 'applicationRecordExportApi'

const AttendanceVoReq = new AttendanceVo();
export default defineComponent({
  components: { searchBox },
  props: {
    baseConfig: {
      type: Object as PropType<AttendanceBaseConfig>,
      default: () => ({}),
    },
  },
  setup(props) {
    const router = useRouter();
    const { baseConfig } = toRefs(props);
    console.log(baseConfig, 'baseConfig');
    const parkOpts = ref<SelectOptions>([]);
    const applicatioInfo = ref<Partial<AttendanceInfo>>({});
    const imgFlag = ref<Boolean>(false);
    const { parkOrgIdChange, companyOpts } = useOptions();
    const baseSearchConf = reactive<SearchConf<['employeeName', 'companyId', 'parkOrgId']>>({
      parkOrgId: {
        tag: 'select',
        value: baseConfig.value.clockInfo?.parkOrgId || '',
        placeholder: '请选择园区',
        options: () => parkOpts.value,
        handleChange: parkOrgIdChange,
        showSearch: true,
        filterOption: true,
      },
      companyId: {
        tag: 'select',
        value: '',
        placeholder: '请选择企业',
        options: () => companyOpts.value,
        showSearch: true,
        filterOption: true,
      },
      employeeName: {
        tag: 'input',
        value: baseConfig.value.clockInfo?.employeeName || '',
        placeholder: '请输入姓名',
      },
    });

    const options = useHashMapDics<['ADVERTISEMENT_STATUS', 'PUNCH_TYPE']>(['ADVERTISEMENT_STATUS', 'PUNCH_TYPE']);
    const columns = baseConfig.value.columns;
    const searchConf = { ...baseSearchConf, ...baseConfig.value.searchConf };
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      AttendanceVoReq[baseConfig.value.api as ReqApi],
      searchConf,
      null,
      AttendanceVoReq[baseConfig.value.exportApi as ExportApi],
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const operateSuccess = () => {
      search(true);
    };
    const searchThing = () => {
      if (baseConfig.value.api === 'clockRecordListPageApi') {
        if (!baseConfig.value.searchConf.date.value) {
          message.warning('请选择查询时间段');
          return;
        }
        const diff = dayjs(baseConfig.value.searchConf.date.value[0]).add(3, 'month').format('YYYY-MM-DD');
        const endTimes = dayjs(baseConfig.value.searchConf.date.value[1]).format('YYYY-MM-DD');
        const startTimes = dayjs(baseConfig.value.searchConf.date.value[0]).format('YYYY-MM-DD');
        if (endTimes > diff) {
          message.warning('查询时间跨度最多为3个月');
          baseConfig.value.searchConf.date.value = [startTimes, ''];
          return;
        }
        search(true);
      } else {
        search(true);
      }
    };
    // monthAttendance
    const goClock = (record: AttendanceInfo) => {
      if (baseConfig.value.api === 'monthAttendanceListPageApi') {
        router.push({ name: 'dayAttendance', params: { employeeName: record.employeeName, clockDay: `${record.year}-${record.month}`, parkOrgId: record.parkOrgId } });
      } else if (baseConfig.value.api === 'fieldApplicationListPageApi') {
        console.log('');
      } else {
        router.push({ name: 'clockRecord', params: { employeeName: record.employeeName, clockDay: record.date, parkOrgId: record.parkOrgId } });
      }
    };
    const openApplication = (record: AttendanceInfo) => {
      applicatioInfo.value = record;
      imgFlag.value = true;
    };
    init();
    return {
      searchThing,
      searchConf,
      search,
      pagination,
      pageChange,
      tableList,
      columns,
      options,
      operateSuccess,
      download,
      goClock,
      openApplication,
      applicatioInfo,
      imgFlag,
    };
  },
});
function useOptions() {
  const companyOpts = ref<SelectOptions>([]);
  const companyMangementInstance = new CompanyMangement();
  const parkOrgIdChange = async (parkOrgId: string) => {
    // 切换园区
    companyOpts.value = await companyMangementInstance.listCompanyDict(parkOrgId);
  };
  return { parkOrgIdChange, companyOpts };
}
</script>
<style lang="scss">
.info-css {
    margin-bottom: 20px;

    span {
        &:first-child {
            display: inline-block;
            min-width: 100px;
            margin-right: 10px;
            text-align: right;
        }
    }

    img {
        vertical-align: text-top;
    }
}
</style>
