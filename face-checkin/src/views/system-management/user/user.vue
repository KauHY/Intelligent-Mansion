<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-06 17:01:47
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
          row-key="userId"
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
              <template v-if="!record.adminFlag">
                <a-button
                  type="link"
                  @click="operate('edit',record)"
                >
                  编辑
                </a-button>
                <a-button
                  v-if="!record.adminFlag"
                  type="link"
                  @click="operate('del',record)"
                >
                  删除
                </a-button>
                <a-button
                  v-if="!record.adminFlag"
                  type="link"
                  @click="operate('disable',record)"
                >
                  {{ record.status===1?'禁用':'启用' }}
                </a-button>
              </template>
              <a-button
                type="link"
                @click="operate('resetPwd',record)"
              >
                重置密码
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
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import User, { UserListItem } from '~models/user';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import {
  menuTypeName, statusName, columns, operateDialogTitleName, statusOpt,
} from './config';
import operateDialog from './components/operate-dialog.vue';

type Operatetypes = 'add'|'edit'|'del'| 'resetPwd' | 'disable'
const userInstance = new User();
// const a:string = 1;
export default defineComponent({
  name: 'MenuList',
  components: { searchBox },
  setup() {
    // const a:string = 1;
    const searchConf = reactive<SearchConf<['username', 'mobile', 'realName', 'status']>>({
      username: {
        tag: 'input',
        value: '',
        placeholder: '请输入用户名',
      },
      mobile: {
        tag: 'input',
        value: '',
        placeholder: '请输入手机号',
      },
      realName: {
        tag: 'input',
        value: '',
        placeholder: '请输入姓名',
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选用户状态',
        options: statusOpt,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<UserListItem>(userInstance.searchListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:UserListItem) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          // // console.log('删除');
          Modal.confirm({
            content: `确定删除【${record.username}】吗？`,
            onOk: async () => {
              await userInstance.del(record.userId);
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
            content: `确定${status === 1 ? '启用' : '禁用'}用户【${record.username}】吗？`,
            onOk: async () => {
              await userInstance.upDateStatus(record.userId, status);
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.title = operateDialogTitleName[type];
          if (type === 'add') {
            operateDialogConf.data = {};
            return;
          }
          operateDialogConf.data = record as UserListItem;
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
      menuTypeName,
      statusName,
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
