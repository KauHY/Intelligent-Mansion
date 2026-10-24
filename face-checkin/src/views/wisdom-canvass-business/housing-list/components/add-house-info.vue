<!--
 * @Description:添加招商房屋信息
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-28 11:51:39
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    title="添加招商房屋"
    :width="820"
    class="operate-dialog"
    @ok="confirm"
  >
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <!-- <a-button
          type="primary"
          @click="operate('add')"
        >
          新增
        </a-button> -->
      </template>
      <template #table="{scroll}">
        <v-table
          :row-selection="{ selectedRowKeys: [currentSelectKey], onChange: onSelectChange, type: 'radio' }"
          row-key="id"
          bordered
          :columns="houseColumns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        />
      </template>
    </search-box>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, computed, reactive, ref, toRefs,
} from 'vue';
import { message } from 'ant-design-vue';
import { SelectHouseInfo } from '~models/wisdom-canvass-business';
import House, { HouseInfo, SearchParams } from '~models/house';
import Park from '~models/park';
import Building from '~models/building';
import { useSearchListPage } from '~composition/common';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { houseColumns } from '../config';
// const organizationalInstance = new Organizational();
// import { SelectHouseInfo } from '../type';

export default defineComponent({
  name: 'OperateDialog',
  components: { searchBox },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    selectKey: {
      type: String,
      default: '',
    },
    // parkOrgId: {
    //   type: String,
    //   default: '',
    // },
  },
  emits: {
    'update:visible': (payload:boolean) => true,
    operateSuccess: () => true,
    select: (selectKey:string, selectData:SelectHouseInfo) => true,
  },
  setup(props, { emit }) {
    const currentVisible = computed({
      set(show:boolean) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    const houseInstance = new House();
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<SelectOptions>([]);
    const parkOpts = ref<SelectOptions>([]);
    const { selectKey } = toRefs(props);
    const searchConf = reactive<SearchConf<['buildingId', 'houseId', 'parkOrgId', 'notHouseProperty', 'needPySort']>>({
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        width: '120px',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange: (value:string) => {},
      },
      buildingId: {
        tag: 'select',
        value: '',
        hidden: false,
        width: '120px',
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
        width: '120px',
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
      notHouseProperty: {
        hidden: true,
        value: 0,
        tag: 'input',
      },
      needPySort: {
        hidden: true,
        value: true,
        tag: 'input',
      },
    });
    // 楼栋切换时
    searchConf.buildingId.handleChange = async (value:string) => {
      // console.log('楼栋变换了', value);
      searchConf.houseId.value = '';
      if (!value) {
        houseIdOptions.value = [];
        return;
      }
      houseIdOptions.value = await houseInstance.searchHouseOpts(value);
    };
    searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
      buildingIdOptions.value = await new Building().listBuildingDict(parkOrgId);
      searchConf.buildingId.value = '';
      houseIdOptions.value = [];
    };

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<HouseInfo, SearchParams>(houseInstance.searchListPage, searchConf, null);

    // 勾选招商房源
    // const selectKeys = ref<string[]>([]);
    const currentSelectData = ref<SelectHouseInfo>();
    const currentSelectKey = ref<string>(selectKey.value);

    const onSelectChange = (selectedRowKeys:string[], selectedRows:SelectHouseInfo) => {
      currentSelectKey.value = selectedRowKeys[0];
      console.log(selectedRows, '---selectedRows');
      currentSelectData.value = selectedRows;
    };
    // const onSelectAll = (selected:boolean, selectedRows:SelectHouseInfo) => {
    //   // if (!selected) { // 删除
    //   //   console.log(tableList, '---tableList');
    //   tableList.value.forEach((item) => {
    //     const {
    //       id, buildArea, buildingName, name,
    //     } = item;
    //     if (selected) {
    //       currentSelectData[id] = {
    //         houseId: id,
    //         buildArea,
    //         name: `${buildingName}-${name}`,
    //       };
    //       return;
    //     }
    //     delete currentSelectData[id];
    //   });
    // };

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      if (!currentSelectKey.value) {
        message.warning('请先选择房屋！');
        return;
      }
      emit('select', currentSelectKey.value, currentSelectData.value as SelectHouseInfo);
      currentVisible.value = false;
    };
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    init();
    return {
      searchConf,
      currentVisible,
      confirm,
      houseColumns,
      pagination,
      pageChange,
      tableList,
      search,
      // selectKeys,
      currentSelectKey,
      onSelectChange,
      // onSelectAll,
    };
  },
});
</script>
