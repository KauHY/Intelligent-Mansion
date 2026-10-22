<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-26 14:26:43
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
          <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('del',record)"
              >
                详情
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
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, onMounted, defineComponent,
} from 'vue';

import VisitorsManagement, { VisitorInfo } from '~models/visitors-management';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import {
  statusName, columns,
} from './config';
import operateDialog from './components/operate-dialog.vue';
import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del'
const visitorsManagementInstance = new VisitorsManagement();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    // const a:string = 1;
    // const { parkOpts, getParkOpts } = useParkInfoOpts();
    const {
      parkOpts,
      tagOpts,
      jobOpts,
      companyOpts,
    } = useOpts();
    const searchConf = reactive<SearchConf<['visitorName']>>({
      visitorName: {
        tag: 'input',
        value: '',
        placeholder: '请输入访客姓名',
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<VisitorInfo>(visitorsManagementInstance.historyVisitorListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:VisitorInfo) => {
      operateDialogConf.type = type;
      operateDialogConf.visible = true;
      operateDialogConf.title = '访客详情';
      operateDialogConf.data = record as VisitorInfo;
    };
    search(true);
    // getParkOpts();
    // companyMangementInstance.getUserMenuList();
    onMounted(() => {
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
      tagOpts,
      jobOpts,
      companyOpts,
      statusName,
    };
  },
});
</script>
