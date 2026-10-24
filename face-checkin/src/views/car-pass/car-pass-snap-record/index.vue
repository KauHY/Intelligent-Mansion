<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-07 12:12:54
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate"
        >
          导出
        </a-button>
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template
            #imgUrl="{text}"
          >
            <v-image
              width="80px"
              height="80px"
              :keys="text"
            />
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import CarPass, { CarPassRecord } from '~models/car-pass';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import {
  getColumns, carRegisteredOpts,
} from './config';

// import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

// type Operatetypes = 'add'|'edit'|'del'
const carPassInstance = new CarPass();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    const laneOpts = ref<SelectOptions>([]);
    const parkingLotOpts = ref<SelectOptions>([]);
    const options = ref<DictionaryMap<['PARK_TYPE', 'ACCESS_RESULT', 'GATEWAY_TYPE']>>({
      PARK_TYPE: [],
      ACCESS_RESULT: [],
      GATEWAY_TYPE: [],
    });
    const columns = getColumns(options);
    const searchConf = reactive<SearchConf<['carNum', 'parkingLotId', 'gatewayType', 'accessResult', 'parkType', 'parkOrgId', 'recognizeTimeStartTime']>>({
      carNum: {
        tag: 'input',
        value: '',
        placeholder: '请输入车牌号',
      },
      parkType: {
        tag: 'select',
        value: '',
        placeholder: '请选择停车类型',
        options: () => options.value.PARK_TYPE,
      },
      // carRegistered: {
      //   tag: 'select',
      //   value: '',
      //   placeholder: '是否在册',
      //   options: carRegisteredOpts,
      // },
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange: (value:string) => {},
      },
      parkingLotId: {
        tag: 'select',
        value: '',
        placeholder: '请选择停车场',
        options: () => parkingLotOpts.value,
      },
      gatewayType: {
        tag: 'select',
        value: '',
        placeholder: '请选择出入口',
        options: () => options.value.GATEWAY_TYPE,
      },
      // carLaneId: {
      //   tag: 'select',
      //   value: '',
      //   placeholder: '请选择企业',
      //   options: () => laneOpts.value,
      // },
      accessResult: {
        tag: 'select',
        value: '',
        placeholder: '请选择识别结果',
        options: () => options.value.ACCESS_RESULT,
      },
      recognizeTimeStartTime: {
        tag: 'date-range',
        value: '',
        placeholder: ['识别开始时间', '识别结束时间'],
        endTimeKey: 'recognizeTimeEndTime',
        startTimeKey: 'recognizeTimeStartTime',
      },
    });
    searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
      parkingLotOpts.value = await carPassInstance.parkingLotDic(parkOrgId);
    };

    // searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
    //   laneOpts.value = await carPassInstance.d;
    // };

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<CarPassRecord>(carPassInstance.carSnapRecordListPage, searchConf, null, carPassInstance.carSnapRecordExport);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async () => {
      console.log('导出');
      await download();
    };
    search(true);
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['PARK_TYPE', 'ACCESS_RESULT', 'GATEWAY_TYPE']>(['PARK_TYPE', 'ACCESS_RESULT', 'GATEWAY_TYPE']);
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      parkingLotOpts,
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      carRegisteredOpts,
      options,
    };
  },
});
</script>
