<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-07 16:51:22
-->
<template>
  <div class="menu-list content-section">
    <search-box>
      <template #searchOperate>
        <a-button
          type="primary"
          @click="operate('resetMenu')"
        >
          重置菜单
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
          :scroll="scroll"
          :pagination="false"
          :data-source="tableList"
          @change="pageChange"
        >
          <template #type="{text}">
            <span>{{ menuTypeName[text] }}</span>
          </template>
          <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('add',record)"
              >
                新增
              </a-button>
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
                @click="operate('hidden',record)"
              >
                {{ record.status===1?'隐藏':'显示' }}
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
      @operate:success="refresh"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, defineProps, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { refreshRouter } from '@/routers';
import Menu from '~models/meun';
import searchBox from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import {
  menuTypeName, statusName, columns, operateDialogTitleName,
} from './config';
import operateDialog from './components/operate-dialog.vue';
// import Tenant, { TenantsOptListItem } from '~models/tenant';

type Operatetypes = 'edit'| 'hidden'|'resetMenu'
const menuInstance = new Menu();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox },
  setup() {
    // const headOrgIdOptions = ref<TenantsOptListItem[]>([]);
    // const searchConf = reactive({
    //   headOrgId: {
    //     tag: 'select',
    //     value: '',
    //     placeholder: '请选择租户',
    //     labelKey: 'orgName',
    //     valueKey: 'headOrgId',
    //     options: headOrgIdOptions,
    //   },
    // });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(menuInstance.searchListPage, {}, operateDialog);

    // searchConf.headOrgId.options = await new Tenant().getRsTenantListt();
    /**
       * @description: 刷新操作
       * @param {*}
       * @return {*}
       * @author: GUOCHAO82
       */
    const refresh = () => {
      search(true);
      refreshRouter();
    };

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:MenuItem) => {
      console.log('type', type, record);
      debugger;
      operateDialogConf.type = type;
      switch (type) {
        case 'resetMenu': {
          // // console.log('删除');
          Modal.confirm({
            content: '确定重置菜单么吗？',
            onOk: async () => {
              await menuInstance.resetMenu();
              message.success('重置成功');
              refresh();
            },
          });
          break;
        }
        case 'hidden': {
          if (!record) return;
          const status = record.status === 1 ? 0 : 1;
          Modal.confirm({
            content: `确定${status === 1 ? '展示' : '隐藏'}【${record.name}】吗？`,
            onOk: async () => {
              await menuInstance.upDateStatus(record.id, status);
              message.success('操作成功');
              refresh();
            },
          });
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.title = operateDialogTitleName[type];
          // @ts-ignore
          if (type === 'add') {
            operateDialogConf.data = {
              parentId: record?.id || '0',
            };
            return;
          }
          operateDialogConf.data = record as MenuItem;
          break;
        }
      }
      // console;
    };
    const init = async () => {
      search(true);
      menuInstance.getUserMenuList();
      // headOrgIdOptions.value = await new Tenant().getRsTenantListt();
    };
    onMounted(() => {
      init();
    });
    return {
      columns,
      menuTypeName,
      statusName,
      // searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      refresh,
    };
  },
});
</script>
