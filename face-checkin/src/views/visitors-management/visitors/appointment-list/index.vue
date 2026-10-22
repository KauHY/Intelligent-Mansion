<!--
 * @Description:访客预约列表
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-28 15:33:32
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
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('register',record)"
              >
                登记
              </a-button>
              <a-button
                type="link"
                @click="operate('details',record)"
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
      :visit-reason-opts="options['VISIT_REASON']"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import VisitorsManagement, { VisitorInfo } from '~models/visitors-management';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import * as dayjs from 'dayjs';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import operateDialog from './components/operate-dialog.vue';
import {
  statusName, getColumns, operateDialogTitleName,
} from './config';

type Operatetypes = 'register'|'details'
const visitorsManagementInstance = new VisitorsManagement();

// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    // const a:string = 1;
    // const { parkOpts, getParkOpts } = useParkInfoOpts();
    // const {
    //   parkOpts,
    //   tagOpts,
    //   jobOpts,
    //   companyOpts,
    //   parkOrgIdChange,
    //   companyChange,
    // } = useOpts();
    const searchConf = reactive<SearchConf<['visitorName']>>({
      visitorName: {
        tag: 'input',
        value: '',
        placeholder: '请输入姓名',
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<VisitorInfo>(visitorsManagementInstance.visitorSubscribeListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record:VisitorInfo) => {
      if (type === 'register') {
        const currenTime = dayjs().format('YYYY-MM-DD');
        if (currenTime !== record.visitDate) {
          message.warning('未到访问日期，不支持登记操作');
          return;
        }
      }
      operateDialogConf.type = type;
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      operateDialogConf.data = record as VisitorInfo;
      //   break;
      // }
      // }
    };
    search(true);
    const options = ref<DictionaryMap<['VISIT_REASON']>>({
      VISIT_REASON: [],
    });
    const columns = getColumns(options);
    onMounted(async () => {
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
      options.value = await useHashMapDic<['VISIT_REASON']>(['VISIT_REASON']);
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
      // parkOpts,
      // tagOpts,
      // jobOpts,
      // companyOpts,
      options,
      statusName,
    };
  },
});
</script>
