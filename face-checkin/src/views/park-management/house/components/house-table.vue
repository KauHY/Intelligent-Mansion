<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-12 17:51:54
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
          @click="operate('importFloor')"
        >
          导入楼栋
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('importHouse')"
        >
          导入房屋
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
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #houseStatus="{text}">
            <span>{{ options['HOUSE_STATUS']? options['HOUSE_STATUS'][text]?.label ||'-':'-' }}</span>
          </template>

          <template
            #operate="{record}"
          >
            <div>
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>
              <a-button
                type="link"
                @click="operate('del',record)"
              >
                删除
              </a-button>
            </div>
            <!-- <span v-else>/</span> -->
          </template>
          <template #orgType="{record}">
            <span>{{ orgTypeNames[record.orgType] }}</span>
          </template>
          <template #name="{ record }">
            <span
              class="text-link"
              @click="operate('detailHouse', record)"
            >{{ record.name }}</span>
          </template>
          <template #buildingName="{ record }">
            <span
              class="text-link"
              @click="operate('detailBuilding', record)"
            >{{ record.buildingName }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :dialog-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      @operate:success="operateSuccess"
    />
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      :import-excel-config="importExcelConfig"
      :template-key="operateDialogConf.type === 'importFloor' ? '批量导入模板/楼栋导入模板.xlsx' : '批量导入模板/房屋导入模板.xlsx'"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, watch, watchEffect, PropType, computed,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import House, { HouseInfo, SearchParams, TreeNodeItem } from '~models/house';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import Building, { FloorListItem } from '~models/building';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import operateDialog from './operate-dialog.vue';
import {
  elevatorNames, columns, houseDialogTitleNames,
} from '../config';

type Operatetypes = 'edit'|'del' |'export' | 'importHouse'| 'importFloor'|'detailHouse'|'detailBuilding'
// const houseInstance = new House();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox, ImportExcel },
  props: {
    selectTreeNode: {
      type: Object as PropType<{name:string, key:string, type:number}>,
      default: () => ({}),
    },
  },
  emits: ['on-refresh'],
  setup(props, { emit }) {
    const houseInstance = new House();
    const floorInstance = new Building();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
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
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        hidden: true,
      },
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

    // 绑定change事件处理函数
    searchConf.buildingId.handleChange = buildingChange;
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<HouseInfo, SearchParams>(houseInstance.searchListPage, searchConf, operateDialog, floorInstance.exportHouses);

    const operateSuccess = () => {
      search(true);
      emit('on-refresh');
    };

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:HouseInfo) => {
      // debugger;
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        Modal.confirm({
          title: () => '',
          content: `删除后，与此房屋相关的业务模块及数据将无法使用，且此操作不可恢复。
确定删除${record.name}吗？`,
          onOk: async () => {
            await houseInstance.del(record.id);
            message.success('操作成功');
            operateSuccess();
          },
        });
        return;
      } if (type === 'export') {
        await download();
        return;
      } if (type === 'importFloor' || type === 'importHouse') {
        // console.log(operateDialogConf.type, '----operateDialogConf.data');
        importFlag.value = true;
        importExcelConfig.value = {
          title: type === 'importFloor' ? '导入楼栋' : '导入房屋',
          onConfim: type === 'importFloor' ? floorInstance.importBuildings : floorInstance.importHouses,
        };
        return;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = houseDialogTitleNames[type];
      Object.assign(operateDialogConf.data, record, {
        label: record?.parkOrgName,
        floorName: record?.buildingName,
      });
    };

    watch(props.selectTreeNode, (treeNode) => {
      if (!treeNode.key) return;
      const { key, type } = treeNode;
      if (type === 1) { // 选中园区
        searchConf.parkOrgId.value = key;
        searchConf.houseId.value = '';
        searchConf.buildingId.value = '';
        parkChange();
      } else if (type === 2) { // 选中楼栋
        searchConf.buildingId.value = key;
        searchConf.parkOrgId.value = '';
        buildingChange(searchConf.buildingId.value);
      } else { // 选中房屋
        searchConf.houseId.value = key;
        searchConf.parkOrgId.value = '';
        searchConf.buildingId.value = '';
      }
      searchConf.houseId.hidden = !(type < 3);
      searchConf.buildingId.hidden = !(type < 2);
      search(true);
    }, {
      deep: true,
    });

    const options = ref<DictionaryMap<['HOUSE_STATUS']>>({
      HOUSE_STATUS: [],
    });
    const init = async () => {
      options.value = await useHashMapDic<['HOUSE_STATUS']>(['HOUSE_STATUS']);
    };
    const importSuccess = async () => {
      search(true);
      emit('on-refresh');
    };
    init();
    return {
      columns,
      // statusName,
      elevatorNames,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      // isCheckAllOrg,
      operateSuccess,
      options,
      importFlag,
      importExcelConfig,
      importSuccess,
    };
  },
});
</script>
