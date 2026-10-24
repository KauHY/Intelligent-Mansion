<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-24 15:36:36
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box>
      <template #searchOperate>
        <!-- <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp; -->
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
          <template #enable="{text}">
            <span>{{ text?"启动":'停用' }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>&nbsp;&nbsp;
              <a-button
                type="link"
                @click="operate('del',record)"
              >
                删除
              </a-button>
              <a-button
                type="link"
                @click="operate('disabled',record)"
              >
                {{ record.enable?'停用':'启动' }}
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
      :process-type-opts="options['VISIT_SOURCE_TYPE']"
      :process-nodes-opts="options['VISIT_PROCESS_NODE']"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import VisitorsManagement, { VisitorProcess } from '~models/visitors-management';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { getColumns, operateDialogTitleName } from './config';
import operateDialog from './components/operate-dialog.vue';

type Operatetypes = 'add'|'edit'|'del'|'disabled'
const visitorsManagementInstance = new VisitorsManagement();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    // const searchConf = reactive<SearchConf<['companyName', 'parkOrgId']>>({
    //   companyName: {
    //     tag: 'input',
    //     value: '',
    //     placeholder: '请输入公司名称',
    //   },
    //   parkOrgId: {
    //     tag: 'select',
    //     value: '',
    //     placeholder: '请选择园区',
    //     options: () => parkOpts.value,
    //   },
    // });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<VisitorProcess>(visitorsManagementInstance.visitorProcessListPage, {}, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:VisitorProcess) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        // // console.log('删除');
        Modal.confirm({
          content: `确定删除【${record.processName}】吗？`,
          onOk: async () => {
            await visitorsManagementInstance.removePerson(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      if (type === 'disabled') { // 禁用
        await visitorsManagementInstance.visitorProcessModifyStatus({
          id: record?.id as string,
          enable: record?.enable ? 0 : 1,
        });
        message.success('操作成功');
        search(true);
        return;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      // if (type === 'add') {
      //   operateDialogConf.data = {};
      //   // operateDialogConf.title = ''
      // }
      operateDialogConf.data = record as VisitorProcess;
      //   break;
      // }
      // }
    };

    const options = ref<DictionaryMap<['VISIT_SOURCE_TYPE', 'VISIT_PROCESS_NODE']>>({
      VISIT_SOURCE_TYPE: [],
      VISIT_PROCESS_NODE: [],
    });
    const columns = getColumns(options);
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['VISIT_SOURCE_TYPE', 'VISIT_PROCESS_NODE']>(['VISIT_SOURCE_TYPE', 'VISIT_PROCESS_NODE']);

      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    search(true);
    return {
      columns,
      // searchConf,
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
    };
  },
});
</script>
