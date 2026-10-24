<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-20 14:35:20
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
          <template #policyName="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template #allowVisitor="{text}">
            <span>{{ options['ALLOW_VISITOR'][text]?.label }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('setStatus',record)"
              >
                {{ record.status===1?'禁用':'启用' }}
              </a-button>&nbsp;&nbsp;
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
      :allow-visitor-opts="options['ALLOW_VISITOR']"
      :week-day-opts="options['Week_Day']"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';

import PersonPass, { AccessPolicy } from '~models/person-pass';
import Park from '~models/park';
import CompanyMangement from '~models/company-mangement';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del'|'details'|'setStatus'
const personPassInstance = new PersonPass();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    const companyOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['policyName', 'parkOrgId', 'companyId']>>({
      policyName: {
        tag: 'input',
        value: '',
        placeholder: '请输入策略名称',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
        handleChange: (parkOrgid:string) => {},
      },
      companyId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择公司',
        options: () => companyOpts.value,
      },
    });
    searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
      companyOpts.value = await new CompanyMangement().listCompanyDict(parkOrgId);
      searchConf.companyId.value = '';
    };
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<AccessPolicy>(personPassInstance.accessPolicyListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:AccessPolicy) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        // // console.log('删除');
        Modal.confirm({
          content: `确定删除【${record.policyName}】吗？`,
          onOk: async () => {
            await personPassInstance.accessPolicyDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      if (type === 'setStatus') {
        if (!record) return;
        const status = record.status === 0 ? 1 : 0;
        Modal.confirm({
          content: `确定${status === 1 ? '启用' : '禁用'}【${record.policyName}】吗？`,
          onOk: async () => {
            await personPassInstance.accessPolicySetState({
              id: record.id as string,
              status,
            });
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
      operateDialogConf.data = record as AccessPolicy;
      //   break;
      // }
      // }
    };
    search(true);

    const options = ref<DictionaryMap<['ALLOW_VISITOR', 'Week_Day']>>({
      ALLOW_VISITOR: [],
      Week_Day: [],
    });
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['ALLOW_VISITOR', 'Week_Day']>(['ALLOW_VISITOR', 'Week_Day']);
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
      options,
    };
  },
});
</script>
