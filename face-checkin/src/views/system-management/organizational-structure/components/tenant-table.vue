<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-24 16:03:08
-->
<template>
  <div class="menu-list">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          v-if="isCheckAllOrg"
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
          <template #orgName="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details', record)"
            >{{ text }}</span>
          </template>
          <template

            #operate="{record}"
          >
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
          <template #orgType="{record}">
            <span>{{ orgTypeNames[record.orgType] }}</span>
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
      @operate:success="operateSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, watch, PropType, computed,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import Organization, { OrganizationListItem, SearchParams } from '~models/organization';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import {
  statusName, columns, operateDialogTitleName, orgTypeNames,
} from '../config';
import operateDialog from './operate-dialog.vue';

type Operatetypes = 'add'|'edit'|'del' |'details'
const organizationInstance = new Organization();
export default defineComponent({
  components: { searchBox },
  props: {
    selectTreeNode: {
      type: Object as PropType<{name:string, key:string, orgType:number}>,
      default: () => ({}),
    },
  },
  emits: ['on-refresh'],
  setup(props, { emit }) {
    const searchConf = reactive<SearchConf<['orgName', 'parentId']>>({
      orgName: {
        tag: 'input',
        value: '',
        placeholder: '请输入组织名称',
      },
      parentId: {
        tag: 'select',
        value: '',
        hidden: true,
      },
    });
    const isCheckAllOrg = computed(() => props.selectTreeNode.orgType === 0);
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<OrganizationListItem, SearchParams>(organizationInstance.searchListPage, searchConf, operateDialog);

    const operateSuccess = () => {
      search(true);
      emit('on-refresh');
    };

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:OrganizationListItem) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.orgName}】吗？`,
            onOk: async () => {
              await organizationInstance.del(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.title = operateDialogTitleName[type];
          if (type === 'add') {
            // debugger;
            operateDialogConf.data = {
              parentOrgName: props.selectTreeNode.name,
              parentId: props.selectTreeNode.key,
            };
            return;
          }
          operateDialogConf.data = record as OrganizationListItem;
          // console.log(type, record, '----type');
          break;
        }
      }
    };

    watch(() => props.selectTreeNode.key, () => {
      // console.log(props.selectTreeNode.key, '----props.selectTreeNode.key');
      if (!props.selectTreeNode.key) return;
      searchConf.parentId.value = props.selectTreeNode.key;
      search(true);
    });
    // onMounted(() => {
    //   // console.log('3.-组件挂载到页面之后执行-------onMounted');
    // });
    return {
      columns,
      statusName,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      isCheckAllOrg,
      operateSuccess,
      orgTypeNames,
    };
  },
});
</script>
