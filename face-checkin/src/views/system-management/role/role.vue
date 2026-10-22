<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-10 17:47:54
-->
<template>
  <div class="menu-list content-section">
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
          <template #roleType="{text}">
            <span>{{ roleTypeOpt[text] }}</span>
          </template>
          <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template>
          <template #operate="{record}">
            <div v-if="!record.adminFlag">
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
              <a-button
                type="link"
                @click="operate('disable',record)"
              >
                {{ record.status===1?'禁用':'启用' }}
              </a-button>
              <a-button
                type="link"
                @click="operate('authorityAllot',record)"
              >
                权限
              </a-button>
            </div>
            <div v-else>
              -
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
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import Role, { RoleListItem, SearchParams } from '~models/role';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import {
  statusName, columns, operateDialogTitleName, statusOpt, roleTypeOpt,
} from './config';
import operateDialog from './components/operate-dialog.vue';

type Operatetypes = 'add'|'edit'|'del'| 'authorityAllot'|'disable'
const userInstance = new Role();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox },
  setup() {
    const searchConf = reactive<SearchConf<['roleName', 'status']>>({
      roleName: {
        tag: 'input',
        value: '',
        placeholder: '请输入角色名称',
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择角色状态',
        options: statusOpt,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<RoleListItem, SearchParams>(userInstance.searchListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:RoleListItem) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.roleName}】吗？`,
            onOk: async () => {
              await userInstance.del(record.id);
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'disable': {
          if (!record) return;
          const status = record.status === 1 ? 0 : 1;
          Modal.confirm({
            content: `确定${status === 1 ? '启用' : '禁用'}用户【${record.roleName}】吗？`,
            onOk: async () => {
              await userInstance.upDateStatus(record.id, status);
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'authorityAllot': { // 权限分配
          operateDialogConf.visible = true;
          operateDialogConf.title = operateDialogTitleName[type];
          // console.log('权限分配');
          operateDialogConf.data = record as RoleListItem;
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.title = operateDialogTitleName[type];
          if (type === 'add') {
            operateDialogConf.data = {};
            return;
          }
          operateDialogConf.data = record as RoleListItem;
          break;
        }
      }
    };
    search(true);
    // userInstance.getUserMenuList();
    onMounted(() => {
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      columns,
      statusName,
      roleTypeOpt,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
    };
  },
});
</script>
