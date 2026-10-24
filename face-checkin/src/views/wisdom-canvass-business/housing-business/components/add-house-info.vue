<!--
 * @Description:添加招商房屋信息
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-15 10:45:08
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
          :row-selection="{ selectedRowKeys: selectKeys, onSelect: onSelectChange,onSelectAll:onSelectAll }"
          row-key="houseInfoId"
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
  defineComponent, PropType, computed, onMounted, reactive, ref, toRefs,
} from 'vue';
import WisdomCanvassBusiness, { HousingSelectBusinessInfo, SelectHouseInfo } from '~models/wisdom-canvass-business';
import House, { HouseInfo } from '~models/house';
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
    defaultSelectData: {
      type: Object as PropType<HouseInfo>,
      default: () => ({}),
    },
    parkOrgId: {
      type: String,
      default: '',
    },
    businessInfoId: {
      type: String,
      default: '',
    },
  },
  emits: {
    'update:visible': (payload:boolean) => true,
    select: (selectKeys:string[], selectData:SelectHouseInfo) => true,
  },
  setup(props, { emit }) {
    const wisdomCanvassBusinessInstance = new WisdomCanvassBusiness();
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
    const { parkOrgId, defaultSelectData, businessInfoId } = toRefs(props);
    const searchConf = reactive<SearchConf<['buildingId', 'houseId', 'parkOrgId', 'businessInfoId']>>({
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
        hidden: true,
      },
      businessInfoId: {
        tag: 'select',
        value: businessInfoId.value,
        hidden: true,
      },
    });

    // 楼栋切换时
    const buildingChange = async (value:string) => {
      // console.log('楼栋变换了', value);
      searchConf.houseId.value = '';
      if (!value) {
        houseIdOptions.value = [];
        return;
      }
      houseIdOptions.value = await wisdomCanvassBusinessInstance.businessHouseInfoDict(value);
    };
    searchConf.buildingId.handleChange = buildingChange;
    searchConf.parkOrgId.value = parkOrgId.value;
    const {
      pagination,
      pageChange,
      tableList,
      search,
    } = useSearchListPage<HousingSelectBusinessInfo>(wisdomCanvassBusinessInstance.selectListHouseInfoPage, searchConf, null);

    // 勾选招商房源
    // const selectKeys = ref<string[]>([]);
    const currentSelectData = reactive<SelectHouseInfo>({});
    const selectKeys = computed(() => Object.keys(currentSelectData));
    const onSelectChange = (record:HousingSelectBusinessInfo, selected:Boolean, selectedRows:any) => {
      const {
        houseInfoId, buildArea, buildingName, name,
      } = record;
      if (selected) {
        currentSelectData[houseInfoId] = {
          houseInfoId,
          buildArea,
          name: `${buildingName}-${name}`,
        };
        return;
      }
      delete currentSelectData[houseInfoId];

      console.log(record, selected, selectedRows, '----selectedRowKeysxxxx---');
    };
    const onSelectAll = (selected:boolean, selectedRows:SelectHouseInfo) => {
      // if (!selected) { // 删除
      //   console.log(tableList, '---tableList');
      tableList.value.forEach((item) => {
        const {
          houseInfoId, buildArea, buildingName, name,
        } = item;
        if (selected) {
          currentSelectData[houseInfoId] = {
            houseInfoId,
            buildArea,
            name: `${buildingName}-${name}`,
          };
          return;
        }
        delete currentSelectData[houseInfoId];
      });
    };
    const init = async () => {
      buildingIdOptions.value = await wisdomCanvassBusinessInstance.businessBuildingDict(parkOrgId.value);
      Object.assign(currentSelectData, defaultSelectData.value);
      search(true);
    };
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      emit('select', selectKeys.value, { ...currentSelectData });
      currentVisible.value = false;
    };
    onMounted(() => {
      // fill();
      init();
    });
    return {
      searchConf,
      currentVisible,
      confirm,
      houseColumns,
      pagination,
      pageChange,
      tableList,
      search,
      selectKeys,
      onSelectChange,
      onSelectAll,
    };
  },
});
</script>
