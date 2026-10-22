<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-12 11:30:32
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <div class="menu-list">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{ scroll }">
        <v-table
          row-key="houseId"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #contractNo="{ record }">
            <span
              class="text-link"
              @click="goClock(record)"
            >{{ record.contractNo }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
  </div>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, onActivated, PropType, toRef, toRefs, watch,
} from 'vue';
import { useRouter } from 'vue-router';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { useSearchListPage, useCachePage } from '~composition/common';
import ContractVo, { } from '~models/contract-manage';
import House, { HouseInfo, SearchParams, TreeNodeItem } from '~models/house';
import Building, { FloorListItem } from '~models/building';
import { getHouseStatusColumns } from './config';

const ContractVoReq = new ContractVo();
export default defineComponent({
  components: { searchBox },
  setup(props) {
    const router = useRouter();
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<SelectOptions>([]);
    const houseInstance = new House();
    const floorInstance = new Building();
    const options = useHashMapDics<['HOUSE_STATUS', 'CONTRACT_STATE', 'CONTRACT_TYPE']>(['HOUSE_STATUS', 'CONTRACT_STATE', 'CONTRACT_TYPE']);
    const searchConf = reactive<SearchConf<['name', 'buildingId', 'houseId', 'parkOrgId', 'state', 'no' ]>>({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入企业名称',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => options.CONTRACT_STATE,
        placeholder: '请选择园区',
      },
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
        handleChange: (value:string) => {},
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
      no: {
        tag: 'input',
        value: '',
        placeholder: '请输入合同编号',
      },
      state: {
        tag: 'select',
        placeholder: '请选择合同状态',
        value: '',
        options: () => options.CONTRACT_STATE,
      },
    });

    const columns = getHouseStatusColumns(options);
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(
      ContractVoReq.houseStatusListPageApi,
      searchConf,
      null,
    );
    // 园区切换时
    const parkChange = async () => {
      if (!searchConf.parkOrgId.value) {
        buildingIdOptions.value = [];
        return;
      }
      buildingIdOptions.value = await floorInstance.listBuildingDict(searchConf.parkOrgId.value as string);
      console.log(buildingIdOptions, '---buildingIdOptions');
    };
    // 楼栋切换时
    const buildingChange = async (value:string) => {
      // console.log('楼栋变换了', value);
      searchConf.houseId.value = '';
      if (!value) {
        houseIdOptions.value = [];
        return;
      }
      houseIdOptions.value = await houseInstance.searchHouseOpts(value);
    };
    searchConf.buildingId.handleChange = buildingChange;
    searchConf.parkOrgId.handleChange = parkChange;
    const operateSuccess = () => {
      search(true);
    };
    const goClock = (record: any) => {
      router.push({
        path: '/contract-manage/contract-draft-edit',
        query: { id: record.id, handleType: 'detaul', contractType: record.type },
      });
    };
    return {
      searchConf,
      search,
      pagination,
      pageChange,
      tableList,
      columns,
      options,
      operateSuccess,
      goClock,
    };
  },
});
</script>
