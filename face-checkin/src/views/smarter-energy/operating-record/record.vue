<template>
  <div class="menu-list content-section">
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
          @click="operate('export')"
        >
          导出
        </a-button>
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="userId"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #effectProperty="{ record }">
            <span>{{ options.METER_EFFECT_PROPERTY[record.effectProperty]?.label }}</span>
          </template>
          <template #effectType="{ record }">
            <span>{{ record.deviceType===6?options.ELECTRICITY_METER_EFFECT_TYPE[record.effectType]?.label:'水表' }}</span>
          </template>
          <template #deviceType="{ record }">
            <span>{{ record.deviceType===6? '电能耗':'水能耗' }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent,
} from 'vue';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { useParkInfoOpts } from '~composition/park';
import SmarterMeter, { RecordPageParams } from '~models/smarter-energy';
import {
  columns,
} from './config';

type Operatetypes = 'add'|'edit'|'del'| 'resetPwd' | 'disable' | 'export'
const userInstance = new SmarterMeter();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox },
  setup() {
    const options = ref<DictionaryMap<['ELECTRICITY_METER_EFFECT_TYPE', 'METER_EFFECT_PROPERTY']>>({
      ELECTRICITY_METER_EFFECT_TYPE: [],
      METER_EFFECT_PROPERTY: [],
    });
    const {
      parkOpts,
      buildingOpts,
      houseOpts,
      getParkOpts,
      getHouseOpts,
      getBuildingOpts,
    } = useParkInfoOpts();
    const searchConf = reactive<SearchConf<['parkOrgId', 'deviceId', 'buildingId', 'houseId', 'deviceName', 'settledMonth', 'deviceType']>>({
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        options: parkOpts,
        placeholder: '请选择所属园区',
      },
      buildingId: {
        tag: 'select',
        value: '',
        placeholder: '所属楼栋',
        options: buildingOpts,
        handleChange: (value:string) => {},
      },
      houseId: {
        tag: 'select',
        value: '',
        options: houseOpts,
        filterOption: true,
        showSearch: true,
        placeholder: '所属房屋',
      },
      deviceName: {
        tag: 'input',
        value: '',
        placeholder: '请输入表计名称',
      },
      deviceId: {
        tag: 'input',
        value: '',
        placeholder: '请输入设备ID',
      },
      settledMonth: {
        tag: 'month-picker',
        value: '',
        placeholder: '请选择结算月份',
      },
      deviceType: {
        tag: 'input',
        hidden: true,
        value: 6,
      },
    });
    searchConf.parkOrgId.handleChange = (parkOrgId:string) => {
      searchConf.buildingId.value = '';
      searchConf.houseId.value = '';
      getBuildingOpts(parkOrgId);
    };
    searchConf.buildingId.handleChange = (buildingId:string) => {
      searchConf.houseId.value = '';
      getHouseOpts(buildingId);
    };
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<RecordPageParams>(userInstance.listMeterConsumptionRecordPage, searchConf, null, userInstance.downloadConsumptionRecord);
    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'export': {
          await download();
          break;
        }
        default: {
          break;
        }
      }
    };
    const init = async () => {
      await getParkOpts();
      // searchConf.parkOrgId.value = parkOpts.value[0]?.value;
      // getBuildingOpts(parkOpts.value[0]?.value);
      search(true);
      options.value = await useHashMapDic<['ELECTRICITY_METER_EFFECT_TYPE', 'METER_EFFECT_PROPERTY']>(['ELECTRICITY_METER_EFFECT_TYPE', 'METER_EFFECT_PROPERTY']);
    };
    init();
    onMounted(() => {
    });
    return {
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operate,
      options,
      operateDialogConf,
    };
  },
});
</script>
