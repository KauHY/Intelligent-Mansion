<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-14 16:15:01
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-26 14:12:00
-->
<template>
  <div class="park-management-park">
    <!-- <div class="organizational-tree">
      <a-input-search
        v-model:value="searchValue"
        :loading="loading"
        placeholder="请输入组织名称"
        @change="getTreeData"
      />
      <a-tree
        :selected-keys="[selectTreeNode.orgId]"
        :replace-fields="{ key: 'id', title: 'orgName' }"
        :tree-data="treeData"
        @select="treeSelect"
      >
        <template #switcherIcon>
          <span><v-svg name="arrow-down" /></span>
        </template>
      </a-tree>
    </div> -->
    <div class="tenant-table-box">
      <tenantTable :select-tree-node="selectTreeNode" />
    </div>
  </div>
</template>

<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, onMounted, onActivated,
} from 'vue';
import { useRoute } from 'vue-router';
import { TreeDataItem } from 'ant-design-vue/es/tree/Tree';
import Organization from '~models/organization';
import tenantTable from './components/park-table.vue';

export default defineComponent({
  name: 'ParkList',
  components: { tenantTable },
  props: {},
  setup() {
    const organizationInstance = new Organization();
    const searchValue = ref('');
    const loading = ref(false);
    const selectTreeNode = reactive({
      orgName: '',
      orgId: '',
    });
    const treeData = ref<Array<TreeDataItem>>([]);
    let searchTimeId: NodeJS.Timeout;
    const getTreeData = async () => {
      clearTimeout(searchTimeId);
      searchTimeId = setTimeout(async () => {
        try {
          loading.value = true;
          treeData.value = await organizationInstance.getOrganizationTreeByorgName(
            searchValue.value,
          ) as unknown as Array<TreeDataItem>;
          // 设置默认选中值
          if (treeData.value.length) {
            const { id, orgName } = treeData.value[0];
            selectTreeNode.orgName = orgName as string;
            selectTreeNode.orgId = id as string;
          }
          loading.value = false;
        } catch (error) {
          loading.value = false;
        }
      }, 300);
    };

    /**
     * @description: 选中组织时触发
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
      */
    // const treeSelect = async (keys: string[], e: any) => {
    //   // debugger;
    //   if (keys.length) {
    //     const [key] = keys;
    //     selectTreeNode.orgId = key;
    //     selectTreeNode.orgName = e?.node?.dataRef?.orgName || '';
    //   }
    //   console.log(selectTreeNode, e.node, '----selectTreeNode');
    // };

    // getTreeData();
    onActivated(() => {
      if (useRoute().params.refresh === 'refresh')getTreeData();
    });
    return {
      // searchValue,
      // loading,
      // treeData,
      // getTreeData,
      // treeSelect,
      selectTreeNode,
    };
  },
});
</script>
<style lang="scss">
.park-management-park {
    display: flex;
    justify-content: space-between;
    height: calc(100% - 40px);
    margin: 20px;
    overflow: hidden;
    background: #f3f3f3;

    .organizational-tree {
        width: 250px;
        padding: 30px 20px 0 20px;
        margin-right: 10px;
        background: #fff;
        border-radius: 4px;
    }

    .tenant-table-box {
        flex: 1;
        padding: 30px 20px 0 10px;
        overflow: hidden;
        background: #fff;
        border-radius: 4px;
    }
}
</style>
