<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-17 11:23:15
 * @LastEditTime: 2022-03-15 12:08:16
 * @LastEditors: xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    :visible="true"
    :width="700"
    :title="materialConfig.title"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div>
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
            :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, getCheckboxProps: getCheckboxProps }"
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
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import House from '~models/house';
import MeetingRentalVo, { MeetingRentalListKind } from '~models/meeting-rental';
import { getChooseMaterialColumns } from '../config';

export default defineComponent({
  components: { searchBox },
  props: {
    materialConfig: {
      type: Object,
      default: () => ({
        title: '',
        parkOrgId: '',
        checkedKeys: [],
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    getMaterialData: (data: object) => true,
  },
  setup(props, { emit }) {
    const { parkOrgId, checkedKeys } = toRefs(props.materialConfig);
    console.log(props.materialConfig, 'props.materialConfig', parkOrgId);
    const searchConf = reactive<SearchConf<['goodsName', 'parkOrgId']>>({
      goodsName: {
        tag: 'input',
        placeholder: '请输入物资名称',
        value: '',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        hidden: true,
      },
    });
    const selectedRowKeys = ref<string[]>([]);
    const selectedRow = ref<object>([]);
    const columns = getChooseMaterialColumns();
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(new MeetingRentalVo().materialListPageApi, {
      ...searchConf,
    });
    const confirm = () => {
      emit('getMaterialData', selectedRow);
      emit('update:show', false);
    };
    const onSelectChange = (keys: [], selectedRows: []) => {
      selectedRowKeys.value = keys;
      selectedRow.value = selectedRows;
    };
    const getCheckboxProps = (record: any) => ({
      // disabled: checkedKeys.value.indexOf(record.id) !== -1,
    });
    // 楼栋切换时
    const init = async () => {
      selectedRowKeys.value = checkedKeys.value;
      searchConf.parkOrgId.value = parkOrgId.value;
      search(true);
    };
    init();
    return {
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
    };
  },
});
</script>
