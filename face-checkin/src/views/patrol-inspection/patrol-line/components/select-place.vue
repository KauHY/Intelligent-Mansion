<!--
 * @Description:选择巡检点
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-02 15:23:10
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    title="选择巡检点"
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
      </template>
      <template #table="{scroll}">
        <v-table
          :row-selection="{ selectedRowKeys: selectKeys, onSelect: onSelectChange,onSelectAll:onSelectAll }"
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
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, reactive, ref, toRefs, watch,
} from 'vue';
import { message } from 'ant-design-vue';
// import House, { HouseInfo, SearchParams } from '~models/house';
import PatrolInspection, { PatrolPlace } from '~models/patrol-inspection';
import { useSearchListPage } from '~composition/common';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { DictionaryMap } from '~composition/useHashMapDic';
import { getSelectColumns } from '../config';

export default defineComponent({
  name: 'OperateDialog',
  components: { searchBox },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    baseSelectData: {
      type: Object as PropType<PatrolPlace[]>,
      default: () => ({}),
    },
    parkOrgId: {
      type: String,
      default: '',
    },
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    patrolType: {
      type: String,
      default: '',
    },
  },
  emits: {
    'update:visible': (payload:boolean) => true,
    select: (selectData:PatrolPlace[]) => true,
  },
  setup(props, { emit }) {
    const {
      parkOrgId, baseSelectData, inspectionTypes, patrolType,
    } = toRefs(props);
    const currentVisible = computed({
      set(show:boolean) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    // const options = ref<DictionaryMap<['INSPECTION_TYPE']>>({ INSPECTION_TYPE: inspectionTypes.value });
    const patrolInspectionInstance = new PatrolInspection();
    const searchConf = reactive<SearchConf<['pointName', 'orgId', 'type']>>({
      pointName: {
        tag: 'input',
        value: '',
        placeholder: '请输入巡检点名称',
      },
      type: {
        tag: 'input',
        value: '',
        hidden: true,
      },
      orgId: {
        tag: 'input',
        value: '',
        hidden: true,
      },
    });

    searchConf.orgId.value = parkOrgId.value;
    searchConf.type.value = patrolType.value;
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(patrolInspectionInstance.patrolPlaceListPage, searchConf, null);

    // 勾选巡检点
    const currentSelectData = ref<PatrolPlace[]>([]);
    const selectKeys = computed(() => currentSelectData.value.map(({ id }) => id));

    const onSelectChange = (record:PatrolPlace, selected:Boolean, selectedRows:any) => {
      const {
        id,
      } = record;
      if (selected) {
        // currentSelectData[id] = {
        //   ...record,
        // };
        currentSelectData.value.push({ ...record });
        return;
      }
      const index = currentSelectData.value.findIndex((item) => item.id === id);
      currentSelectData.value.splice(index, 1);

      console.log(record, selected, selectedRows, '----selectedRowKeysxxxx---');
    };
    const onSelectAll = (selected:boolean, selectedRows:PatrolPlace[]) => {
      if (selected) {
        currentSelectData.value = selectedRows;
        return;
      }
      currentSelectData.value = [];
    };
    const init = async () => {
      currentSelectData.value = baseSelectData.value.slice();
      console.log(currentSelectData.value, '----currentSelectData');
      search(true);
    };
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      emit('select', currentSelectData.value.slice());
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
      pagination,
      pageChange,
      tableList,
      search,
      selectKeys,
      onSelectChange,
      onSelectAll,
      columns: getSelectColumns(inspectionTypes.value, false),
    };
  },
});
</script>
