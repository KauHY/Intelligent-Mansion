<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-17 11:23:15
 * @LastEditTime: 2022-07-01 10:26:17
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <v-modal
    v-model:visible="currentVisible"
    :width="900"
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
            :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, getCheckboxProps: getCheckboxProps}"
            row-key="id"
            bordered
            :columns="columns"
            :pagination="pagination"
            :data-source="tableList"
            :scroll="scroll"
            @change="pageChange"
          />
        </template>
      </search-box>
    </div>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, toRefs, PropType,
} from 'vue';
import BusinessInvitation, { CustomerManagementInfo } from '~models/wisdom-canvass-business';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import House, { HouseInfo } from '~models/house';
import { HouseMessages } from '~models/contract-manage';
import Building, { FloorListItem } from '~models/building';
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
        houseStatusSet: [],
      }),
    },
  },
  emits: {
    'update:show': (show:boolean) => true,
    getHouseData: (data:HouseMessages[]) => true,
  },
  setup(props, { emit }) {
    const HouseInstance = new House();
    const FloorInstance = new Building();
    const {
      parkId, checkedKeys, unavailableHouseIds, houseStatusSet,
    } = toRefs(props.houseConfig);
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<SelectOptions>([]);
    const options = useHashMapDics([
      'HOUSE_FUNCTION',
    ]);
    const searchConf = reactive<SearchConf<['buildingId', 'houseFunction', 'houseId', 'parkOrgId', 'noIds', 'houseStatusSet']>>({
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
      houseFunction: {
        tag: 'select',
        value: '',
        hidden: false,
        showSearch: true,
        options: options.HOUSE_FUNCTION,
        placeholder: '请选择房屋功能',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        hidden: true,
      },
      noIds: {
        tag: 'input',
        value: '',
        hidden: true,
      },
      houseStatusSet: {
        tag: 'input',
        value: '',
        hidden: true,
      },
    });
    const selectedRowKeys = ref<string[]>([]);
    const selectedRow = ref<HouseMessages[]>([]);
    const columns = getHouseColumns(options);
    columns.pop();
    const {
      pagination,
      pageChange,
      tableList,
      search,
    } = useSearchListPage(HouseInstance.searchListPage, searchConf);
    const confirm = () => {
      emit('getHouseData', selectedRow.value);
      emit('update:show', false);
    };
    const onSelectChange = (keys: [], selectedRows:[]) => {
      selectedRowKeys.value = keys;
      selectedRow.value = selectedRows;
    };
    const getCheckboxProps = (record: HouseInfo) => ({
      disabled: checkedKeys.value.indexOf(record.id) !== -1,
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
      searchConf.noIds.value = unavailableHouseIds.value;
      searchConf.houseStatusSet.value = houseStatusSet.value;
      buildingIdOptions.value = await FloorInstance.listBuildingDict(parkId.value);
      search(true);
    };
    onMounted(() => {
      init();
    });
    return {
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
