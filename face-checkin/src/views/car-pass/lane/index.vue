<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-26 19:49:19
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
        <!-- <a-button
          type="primary"
          @click="operate('add')"
        >
          新增
        </a-button> -->
        <a-button
          type="primary"
          @click="operate('sync')"
        >
          设备数据同步
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
          <!-- <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template> -->
          <!-- <template #bodyCell="{ column }">
            <template v-if="column.key === 'operation'">
              <a>action</a>
            </template>
          </template> -->
          <template #operate="{record}">
            <div>
              <!-- <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>&nbsp;&nbsp; -->
              <a-button
                type="link"
                @click="operate('del',record)"
              >
                删除
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
      :menu-list="tableList"
      :park-opts="parkOpts"
      :parking-lot-opts="parkingLotOpts"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import CarPass, { LaneInfo } from '~models/car-pass';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del'|'sync'
const carPassInstance = new CarPass();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    const parkingLotOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['laneName', 'parkOrgId', 'parkingLotId']>>({
      laneName: {
        tag: 'input',
        value: '',
        placeholder: '请输入车道名称',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange: (value:string) => {},
      },
      parkingLotId: {
        tag: 'select',
        value: '',
        placeholder: '请选择停车场',
        options: () => parkingLotOpts.value,
      },
    });
    searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
      if (!parkOrgId) return;
      parkingLotOpts.value = await carPassInstance.parkingLotDic(parkOrgId);
    };
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<LaneInfo>(carPassInstance.laneListPage, searchConf, operateDialog);
    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:LaneInfo) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        // // console.log('删除');
        Modal.confirm({
          content: `确定删除【${record.laneName}】吗？`,
          onOk: async () => {
            await carPassInstance.laneDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      if (type === 'sync') {
        await carPassInstance.syncParkingLot();
        message.success('操作成功');
        search(true);
        return;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      operateDialogConf.data = record as LaneInfo;
    };
    search(true);

    // const options = ref<DictionaryMap<['COMPANY_BIND_CLIENT_TYPE']>>({
    //   COMPANY_BIND_CLIENT_TYPE: [],
    // });
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      // options.value = await useHashMapDic<['COMPANY_BIND_CLIENT_TYPE']>(['COMPANY_BIND_CLIENT_TYPE']);
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
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
      parkOpts,
      parkingLotOpts,
      // options,
    };
  },
});
</script>
