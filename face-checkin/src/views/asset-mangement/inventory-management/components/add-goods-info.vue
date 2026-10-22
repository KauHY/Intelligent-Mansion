<!--
 * @Description:添加招商房屋信息
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-26 14:23:04
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    title="选择物品"
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
          :columns="getGoodsListColumns"
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
import PatrolInspection, { GoodsCard } from '~models/asset-mangement';
import Park from '~models/park';
import { useSearchListPage } from '~composition/common';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { getGoodsListColumns } from '../config';

const patrolInspectionInessInstance = new PatrolInspection();
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
    parkOrgId: {
      type: String,
      default: '',
    },
  },
  emits: {
    'update:visible': (payload:boolean) => true,
    operateSuccess: () => true,
    select: (selectKey:string, selectData:GoodsCard) => true,
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
    const parkOpts = ref<SelectOptions>([]);
    const { selectKey, parkOrgId } = toRefs(props);
    const searchConf = reactive<SearchConf<['goodsName', 'parkOrgId']>>({
      goodsName: {
        tag: 'input',
        value: '',
        placeholder: '请输入物品名称',
      },
      parkOrgId: {
        hidden: true,
        value: parkOrgId.value,
        tag: 'input',
      },
    });

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(patrolInspectionInessInstance.goodsListPage, searchConf, null);
    console.log(operateDialogConf, '..operateDialogConf');

    // 勾选招商房源
    // const selectKeys = ref<string[]>([]);
    const currentSelectData = ref<GoodsCard[]>();
    const currentSelectKey = ref<string>(selectKey.value);

    const onSelectChange = (selectedRowKeys:string[], selectedRows:GoodsCard[]) => {
      currentSelectKey.value = selectedRowKeys[0];
      console.log(selectedRows, '---selectedRows');
      currentSelectData.value = selectedRows;
    };

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      if (!currentSelectKey.value) {
        message.info('请选择物品！');
        return;
      }
      emit('select', currentSelectKey.value, currentSelectData.value as any);
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
      getGoodsListColumns,
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
