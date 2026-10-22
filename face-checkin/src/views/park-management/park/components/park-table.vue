<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-24 14:08:28
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
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="parkOrgId"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #name="{ record }">
            <span
              class="text-link"
              @click="operate('detail', record)"
            >{{ record.name }}</span>
          </template>
          <template
            #operate="{record}"
          >
            <div>
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                完善信息
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <!-- <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :menu-list="tableList"
      @operate:success="search(true)"
    /> -->
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, watchEffect, PropType, computed, onActivated,
} from 'vue';

// import Organization, { OrganizationListItem, SearchParams } from '~models/organization';
import { useRouter, useRoute } from 'vue-router';
//
// import { useRoute } from 'vue-router';
import Park, { ParkListItem, SearchParams } from '~models/park/index';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import {
  statusName, columns, operateDialogTitleName,
} from '../config';

type Operatetypes = 'add'|'edit'|'del'|'detail'
const parkInstance = new Park();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox },
  props: {
    selectTreeNode: {
      type: Object as PropType<{orgName:string, orgId:string}>,
      default: () => ({}),
    },
  },
  setup(props) {
    const router = useRouter();
    // const store = useStore();
    const { addCachePage } = useCachePage();
    const searchConf = reactive<SearchConf<['name', 'orgId']>>({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入园区名称',
      },
      orgId: {
        tag: 'select',
        value: '',
        hidden: true,
      },
    });
    const isCheckAllOrg = computed(() => props.selectTreeNode.orgId === '0');
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<ParkListItem, SearchParams>(parkInstance.searchListPage, searchConf, null);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record:ParkListItem) => {
      addCachePage('ParkList');
      router.push({ path: '/park-management/park-details', query: { ...props.selectTreeNode, id: record.parkOrgId, type } });
      // }, 5000);
    };

    // watchEffect(() => {
    //   if (!props.selectTreeNode.orgId) return;
    //   searchConf.orgId.value = props.selectTreeNode.orgId;
    // });
    search(true);
    onActivated(() => { // 从列表页面返回
      // console.log('组件激活了------', useRoute().params);
      if (useRoute().params.refresh === 'refresh')search(false);
    });
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
    };
  },
});
</script>
