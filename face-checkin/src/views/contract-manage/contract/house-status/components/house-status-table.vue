<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-12 10:11:39
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
        <a-button
          type="primary"
          @click="download()"
        >
          导出
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
import { getHouseStatusColumns, endTypeOpts } from '../config';

const ContractVoReq = new ContractVo();
export default defineComponent({
  components: { searchBox },
  props: {
    selectTreeNode: {
      type: Object as PropType<{name:string, key:string, type:number}>,
      default: () => ({}),
    },
  },
  setup(props) {
    const router = useRouter();
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<SelectOptions>([]);
    const houseInstance = new House();
    const floorInstance = new Building();
    const options = useHashMapDics<['HOUSE_STATUS', 'CONTRACT_STATE', 'CONTRACT_TYPE_FOR_HOUSE']>(['HOUSE_STATUS', 'CONTRACT_STATE', 'CONTRACT_TYPE_FOR_HOUSE']);
    const searchConf = reactive<SearchConf<['buildingId', 'houseId', 'parkOrgId', 'houseStatus', 'type', 'state', 'endType' ]>>({
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
      houseStatus: {
        tag: 'select',
        placeholder: '请选择房屋状态',
        value: '',
        options: () => options.HOUSE_STATUS,
      },
      type: {
        tag: 'select',
        placeholder: '请选择合同类型',
        value: '',
        options: () => options.CONTRACT_TYPE_FOR_HOUSE,
      },
      state: {
        tag: 'select',
        placeholder: '请选择合同状态',
        value: '',
        options: () => options.CONTRACT_STATE,
      },
      endType: {
        tag: 'select',
        placeholder: '请选择合同到期状态',
        value: '',
        options: () => endTypeOpts,
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        hidden: true,
      },
    });

    const columns = getHouseStatusColumns(options);
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      ContractVoReq.houseStatusListPageApi,
      searchConf,
      null,
      ContractVoReq.houseStatusExportApi,
    );
    watch(props.selectTreeNode, (treeNode) => {
      if (!treeNode.key) return;
      const { key, type } = treeNode;
      if (type === 1) { // 选中园区
        searchConf.parkOrgId.value = key;
        searchConf.buildingId.value = '';
        parkChange();
      } else if (type === 2) { // 选中楼栋
        searchConf.buildingId.value = key;
        buildingChange(searchConf.buildingId.value);
      } else { // 选中房屋
        searchConf.houseId.value = key;
        searchConf.buildingId.value = '';
      }
      searchConf.houseId.hidden = !(type < 3);
      searchConf.buildingId.hidden = !(type < 2);
      search(true);
    }, {
      deep: true,
    });
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
    const operateSuccess = () => {
      search(true);
    };
    const goClock = (record: any) => {
      router.push({
        path: '/contract-manage/contract-draft-edit',
        query: {
          id: record.contractId, handleType: 'detail', contractType: record.type, state: record.state,
        },
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
      download,
      goClock,
    };
  },
});
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
