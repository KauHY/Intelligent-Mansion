<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-17 11:23:15
 * @LastEditTime: 2022-07-01 10:24:55
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <v-modal
    v-model:visible="currentVisible"
    :width="730"
    :height="600"
    :title="houseConfig.title"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div>
      <search-box
        :search-conf="searchConf"
      >
        <template #searchOperate>
          <a-button
            type="primary"
            @click="search(true)"
          >
            查询
          </a-button>&nbsp;&nbsp;
        </template>
        <template #table="{scroll}">
          <v-table
            :row-selection="{ selectedRowKeys,onChange: onSelectChange, getCheckboxProps: getCheckboxProps}"
            row-key="houseId"
            bordered
            :columns="columns"
            :pagination="pagination"
            :data-source="tableList"
            :scroll="scroll"
            @change="pageChange"
          >
            <template #houseFunction="{ record }">
              <span>
                {{ options.HOUSE_FUNCTION[record.houseFunction]?.label||'-' }}
              </span>
            </template>
          </v-table>
        </template>
      </search-box>
    </div>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, toRefs, PropType,
} from 'vue';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import House, { HouseInfo } from '~models/house';
import Building, { FloorListItem } from '~models/building';
import { useHashMapDics } from '~composition/useHashMapDic';
import Apartment from '~models/smarter-apartment';
import { getHouseColumns } from '../config';

export default defineComponent({
  components: { searchBox },
  props: {
    houseConfig: {
      type: Object,
      default: () => ({
        title: '',
        parkId: '',
        unavailableHouseIds: [],
        checkedKeys: [],
      }),
    },
  },
  emits: {
    'update:show': (show:boolean) => true,
    getHouseData: (data:object) => true,
  },
  setup(props, { emit }) {
    const HouseInstance = new House();
    const ApartmentInstance = new Apartment();
    const FloorInstance = new Building();
    const {
      parkId, checkedKeys, unavailableHouseIds,
    } = toRefs(props.houseConfig);
    const options = useHashMapDics<['HOUSE_FUNCTION']>(['HOUSE_FUNCTION']);
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['buildingId', 'houseId', 'parkOrgId']>>({
      buildingId: {
        tag: 'select',
        value: '',
        hidden: false,
        showSearch: true,
        filterOption: (input: string, option: any) => {
          const label = option.key.split('_')[0];
          return label.includes(input);
        },
        options: buildingIdOptions,
        handleChange: () => {},
        placeholder: '请输入楼栋单元',
      },
      houseId: {
        tag: 'select',
        value: '',
        hidden: false,
        showSearch: true,
        filterOption: (input: string, option: any) => {
          const label = option.key.split('_')[0];
          return label.includes(input);
        },
        options: houseIdOptions,
        placeholder: '请输入房号',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        hidden: true,
      },
    });
    const selectedRowKeys = ref<string[]>([]);
    const selectedRow = ref<object>([]);
    const columns = getHouseColumns();
    columns.pop();
    const {
      pagination,
      pageChange,
      tableList,
      search,
    } = useSearchListPage(ApartmentInstance.searchHouseListPage, { ...searchConf });
    const confirm = () => {
      emit('getHouseData', selectedRow);
      emit('update:show', false);
    };
    const onSelectChange = (keys: [], selectedRows:[]) => {
      selectedRowKeys.value = keys;
      selectedRow.value = selectedRows;
    };
    const getCheckboxProps = (record: HouseInfo) => ({
      defaultChecked: checkedKeys.value.indexOf(record.houseId) !== -1,
      // disabled: checkedKeys.value.indexOf(record.id) !== -1,
    });
    // 楼栋切换时
    const buildingChange = async (value:string) => {
      // console.log('楼栋变换了', value);
      searchConf.houseId.value = '';
      if (!value) {
        houseIdOptions.value = [];
        return;
      }
      houseIdOptions.value = await HouseInstance.searchHouseOpts(value);
    };
    searchConf.buildingId.handleChange = buildingChange;
    const init = async () => {
      searchConf.parkOrgId.value = parkId.value;
      buildingIdOptions.value = await FloorInstance.listBuildingDict(parkId.value);
      // 公寓楼
      buildingIdOptions.value.forEach((element) => {
        if (element.value === '1557666216028688385') {
          searchConf.buildingId.value = '1557666216028688385';
          buildingChange('1557666216028688385');
        }
      });
      search(true);
    };
    onMounted(() => {
      init();
    });
    return {
      options,
      buildingChange,
      confirm,
      emit,
      columns,
      selectedRowKeys,
      onSelectChange,
      getCheckboxProps,
      selectedRow,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      currentVisible: true,
    };
  },
});
</script>
