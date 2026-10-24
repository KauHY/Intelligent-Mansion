<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 17:03:09
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
          @click="operate('add')"
        >
          新增
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
          <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template>
          <template #title="{ record }">
            <span
              class="text-link"
              @click="operate('detail', record)"
            >{{ record.title }}</span>
          </template>
          <template #operate="{record}">
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
      :put-into-type-opts="options.PUT_INTO_TYPE"
      :menu-list="tableList"
      :park-opts="parkOpts"
      :content-type-opts="options.NOTICE_TYPE"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import CustomerService, { AnnouncementInfo } from '~models/customer-service';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';

import {
  // contentTypeOpts,
  statusName,
  getColumns,
  operateDialogTitleName,
  statusOpt,
  putIntoTypeOpts,
} from './config';
import operateDialog from './components/operate-dialog.vue';
import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del'|'detail'
const customerServiceInstance = new CustomerService();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    // const a:string = 1;
    // const { parkOpts, getParkOpts } = useParkInfoOpts();
    const options = useHashMapDics<['PUT_INTO_TYPE', 'NOTICE_TYPE']>(['PUT_INTO_TYPE', 'NOTICE_TYPE']);
    const {
      parkOpts,
    } = useOpts();
    const searchConf = reactive<SearchConf<['putIntoType', 'title', 'type', 'parkOrgId', 'times', 'effectStatus']>>({
      title: {
        tag: 'input',
        value: '',
        placeholder: '请输入标题',
      },
      type: {
        tag: 'select',
        value: '',
        placeholder: '请选择类型',
        options: () => options.NOTICE_TYPE,
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        options: parkOpts,
      },
      putIntoType: {
        tag: 'select',
        value: '',
        placeholder: '请选择投放的小程序',
        options: Object.values(putIntoTypeOpts),
      },
      times: {
        tag: 'date-range',
        value: [],
        placeholder: ['公告开始时间', '公告结束时间'],
        startTimeKey: 'effectTimeStart',
        endTimeKey: 'effectTimeEnd',
      },
      effectStatus: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: statusOpt,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<AnnouncementInfo>(customerServiceInstance.announcementSearchListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:AnnouncementInfo) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        // // console.log('删除');
        Modal.confirm({
          content: `确定删除【${record.title}】吗？`,
          onOk: async () => {
            await customerServiceInstance.announcementDel(record.id as string);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }

      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      if (type === 'add') {
        operateDialogConf.data = {};
        // operateDialogConf.title = ''
        return;
      }
      operateDialogConf.data = record as AnnouncementInfo;
      //   break;
      // }
      // }
    };
    search(true);
    // getParkOpts();
    // companyMangementInstance.getUserMenuList();
    onMounted(() => {
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      columns: getColumns(options),
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      options,
      // tagOpts,
      // jobOpts,
      // companyOpts,
      statusName,
    };
  },
});
</script>
