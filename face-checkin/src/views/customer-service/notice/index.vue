<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-07 12:11:00
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
      :menu-list="tableList"
      :park-opts="parkOpts"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import CustomerService, { NoticeInfo } from '~models/customer-service';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import {
  putIntoTypeOpts,
  contentTypeOpts,
  statusName,
  columns,
  operateDialogTitleName,
  statusOpt,
} from './config';
import operateDialog from './components/operate-dialog.vue';
import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del'|'detail'
const customerServiceInstance = new CustomerService();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const options = useHashMapDics<['INFOMATION_TYPE', 'PUT_INTO_TYPE', 'INFOMATION_STATUS', 'INFOMATION_TYPE']>(['INFOMATION_TYPE', 'PUT_INTO_TYPE', 'INFOMATION_STATUS', 'INFOMATION_TYPE']);
    const {
      parkOpts,
    } = useOpts();

    const searchConf = reactive<SearchConf<['title', 'putIntoType', 'parkOrgId', 'times', 'effectStatus', 'type']>>({
      title: {
        tag: 'input',
        value: '',
        placeholder: '请输入标题',
      },
      type: {
        tag: 'select',
        value: '',
        placeholder: '请选择类型',
        options: options.INFOMATION_TYPE,
      },

      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
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
        placeholder: ['生效开始时间', '生效结束时间'],
        startTimeKey: 'effectTimeStart',
        endTimeKey: 'effectTimeEnd',
      },
      effectStatus: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: options?.INFOMATION_STATUS,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<NoticeInfo>(customerServiceInstance.noticeSearchListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:NoticeInfo) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        // // console.log('删除');
        Modal.confirm({
          content: `确定删除【${record.title}】吗？`,
          onOk: async () => {
            await customerServiceInstance.noticeDel(record.id as string);
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
      operateDialogConf.data = record as NoticeInfo;
    };

    const init = async () => {
      search(true);
    };
    init();
    onMounted(() => {

      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      columns: columns(options),
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      options,
      // jobOpts,
      // companyOpts,
      statusName,
    };
  },
});
</script>
