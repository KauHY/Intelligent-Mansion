<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-01 13:12:26
-->
<template>
  <div class="jobs-mangement content-section">
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
          @click="supplayIssue('add')"
        >
          供应发布
        </a-button>
      </template>
      <template #table="{ scroll }">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #name="{record}">
            <span
              class="table-text-link"
              @click="operate('details', record)"
            >{{ record.name }}</span>
          </template>
          <template #operate="{ record }">
            <div>
              <a-button
                v-show="record.state === 0"
                type="link"
                @click="operate('release', record)"
              >
                发布
              </a-button>&nbsp;&nbsp;
              <a-button
                v-show="record.state === 0"
                type="link"
                @click="operate('edit', record)"
              >
                编辑
              </a-button>&nbsp;&nbsp;
              <a-button
                v-show="record.state === 0"
                type="link"
                @click="operate('del',record)"
              >
                删除
              </a-button>&nbsp;&nbsp;
              <a-button
                v-show="record.state === 1"
                type="link"
                @click="operate('shelves',record)"
              >
                下架
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :park-opts="options"
      :park-pop="parkPop"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import SupplyService, { LaneInfo } from '~models/supply-service';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { operateDialogTitleName, getColumns } from './config';
import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'add' | 'release' | 'edit' | 'del' | 'shelves' | 'details'
const serviceRequest = new SupplyService();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkPop = ref<SelectOptions>([]);
    const options = ref<DictionaryMap<['SUPPLY_TYPE', 'ADD_DATA_SOURCE', 'SUPPLY_PUBLISH_STATUS', 'JUMP_TYPE']>>({
      SUPPLY_TYPE: [], // 供应类型
      ADD_DATA_SOURCE: [], // 来源
      SUPPLY_PUBLISH_STATUS: [], // 供应状态
      JUMP_TYPE: [], // 跳转类型
    });
    const columns = getColumns(options);
    const searchConf = reactive<SearchConf<['name', 'supplyType']>>({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入服务名称',
      },
      supplyType: {
        tag: 'select',
        value: '',
        placeholder: '请选择供应类型',
        options: () => options.value.SUPPLY_TYPE,
        handleChange: (value: string) => { },
      },
    });
    // searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
    //   if (!parkOrgId) return;
    //   parkingLotOpts.value = await carPassInstance.parkingLotDic(parkOrgId);
    // };

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(serviceRequest.supplyPages, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type: Operatetypes, record:LaneInfo) => {
      operateDialogConf.type = type;
      if (type === 'release') {
        Modal.confirm({
          content: `确定发布【${record.name}】吗？`,
          onOk: async () => {
            await serviceRequest.changeState({ id: record.id, state: 1 });
            message.success('操作成功');
            search(true);
          },
        });
      }
      if (type === 'del') {
        Modal.confirm({
          content: `确定删除【${record.name}】吗？`,
          onOk: async () => {
            await serviceRequest.deletesupplyService(record.id);
            message.success('操作成功');
            search(true);
          },
        });
      }
      if (type === 'shelves') {
        Modal.confirm({
          content: `确定下架【${record.name}】吗？`,
          onOk: async () => {
            await serviceRequest.changeState({ id: record.id, state: 2 });
            message.success('操作成功');
            search(true);
          },
        });
      }
      if (type === 'edit' || type === 'details') {
        operateDialogConf.title = type === 'details' ? '供应详情' : '编辑供应发布';
        operateDialogConf.data = record as LaneInfo;
        operateDialogConf.visible = true;
      }

      console.log('operateDialogConf', operateDialogConf);
    };
    search(true);

    const supplayIssue = (type:Operatetypes) => {
      operateDialogConf.visible = true;
      operateDialogConf.type = type;
      operateDialogConf.title = '新增供应发布';
    };
    onMounted(async () => {
      options.value = await useHashMapDic<['SUPPLY_TYPE', 'ADD_DATA_SOURCE', 'SUPPLY_PUBLISH_STATUS', 'JUMP_TYPE']>(['SUPPLY_TYPE', 'ADD_DATA_SOURCE', 'SUPPLY_PUBLISH_STATUS', 'JUMP_TYPE']);
      parkPop.value = await new Park().searchParkListName();
      console.log('3.-组件挂载到页面之后执行-------onMounted', operateDialogConf);
    });
    return {
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkPop,
      options,
      supplayIssue,
    };
  },
});
</script>
