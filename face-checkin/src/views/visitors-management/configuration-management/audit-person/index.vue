<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 14:55:41
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
          <!-- <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template> -->
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
import VisitorsManagement, { AuditPerson } from '~models/visitors-management';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import {
  statusName, columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del'
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
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['companyName', 'parkOrgId']>>({
      companyName: {
        tag: 'input',
        value: '',
        placeholder: '请输入企业名称',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        options: () => parkOpts.value,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<AuditPerson>(visitorsManagementInstance.auditPersonListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:AuditPerson) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        // // console.log('删除');
        const names = record.auditEmployeeNames ? record.auditEmployeeNames : '被访人';
        Modal.confirm({
          content: `确定删除${names}吗？`,
          onOk: async () => {
            await visitorsManagementInstance.auditPersonDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      // if (type === 'add') {
      //   operateDialogConf.data = {};
      //   // operateDialogConf.title = ''
      // }
      operateDialogConf.data = record as AuditPerson;
      //   break;
      // }
      // }
    };
    search(true);

    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
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
      // tagOpts,
      // jobOpts,
      // companyOpts,
      statusName,
    };
  },
});
</script>
