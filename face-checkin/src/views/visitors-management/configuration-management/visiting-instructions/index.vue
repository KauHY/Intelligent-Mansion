<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-29 16:48:52
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
          <template #noticeName="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
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
import VisitorsManagement, { visitingInstructions, VisitorType } from '~models/visitors-management';
import Park from '~models/park';
import CompanyMangement from '~models/company-mangement';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import {
  columns, operateDialogTitleName,
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
    const companyOpts = ref<SelectOptions>([]);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['parkOrgId']>>({
      // companyName: {
      //   tag: 'input',
      //   value: '',
      //   placeholder: '请输入公司名称',
      // },
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        options: () => parkOpts.value,
      },
    });
    searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
      companyOpts.value = await new CompanyMangement().listCompanyDict(parkOrgId);
    };
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<visitingInstructions>(visitorsManagementInstance.visitingInstructionsPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:visitingInstructions) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        console.log('删除', record.id);
        Modal.confirm({
          content: `确定删除【${record.noticeName}】吗？`,
          onOk: async () => {
            await visitorsManagementInstance.visitingInstructionsDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }

      // switch (type) {
      //   case 'details': {
      //     break;
      //   }
      //   case 'setWarn': {
      //     break;
      //   }
      //   case 'setWarn': {
      //     break;
      //   }
      //   default: {
      //     operateDialogConf.visible = true;
      //     operateDialogConf.title = operateDialogTitleName[type];

      //     operateDialogConf.data = record as visitingInstructions;
      //     break;
      //   }
      // }
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];

      operateDialogConf.data = record as visitingInstructions;
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
    };
  },
});
</script>
