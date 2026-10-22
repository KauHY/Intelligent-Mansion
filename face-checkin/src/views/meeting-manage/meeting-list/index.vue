<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-02-21 15:51:18
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <div class="organizational-structure">
    <!-- <div class="organizational-tree">
      <a-tree
        :selected-keys="[selectTreeNode.key]"
        :replace-fields="{key:'id',title:'orgName'}"
        :tree-data="treeData"
        @select="treeSelect"
      >
        <template #switcherIcon>
          <span><v-svg name="arrow-down" /></span>
        </template>
      </a-tree>
    </div> -->
    <div class="tenant-table-box">
      <TenantTable
        :refresh="refresh"
        :select-tree-node="selectTreeNode"
      />
    </div>
  </div>
</template>

<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';
import { TreeDataItem } from 'ant-design-vue/es/tree/Tree';
import Organization from '~models/organization';
import TenantTable from './components/list-table.vue';

const organizationInstance = new Organization();
export default defineComponent({
  name: 'HousingBusiness',
  components: { TenantTable },
  props: {
    refresh: {
      type: String as PropType<'refresh'|''>,
      default: '',
    },
  },
  setup() {
    // const searchValue = ref('');
    // const loading = ref(false);
    const selectTreeNode = reactive({
      name: '',
      key: '',
    });
    // const treeData = ref<Array<TreeDataItem>>([]);
    // let searchTimeId:NodeJS.Timeout;
    // const getTreeData = async () => {
    //   clearTimeout(searchTimeId);
    //   searchTimeId = setTimeout(async () => {
    //     try {
    //       loading.value = true;
    //       treeData.value = await organizationInstance.getOrganizationTreeByorgName(searchValue.value);
    //       // 设置默认选中值
    //       if (treeData.value.length) {
    //         const { id, orgName } = treeData.value[0];
    //         selectTreeNode.name = orgName as string;
    //         selectTreeNode.key = id as string;
    //       }
    //       loading.value = false;
    //     } catch (error) {
    //       loading.value = false;
    //     }
    //   }, 300);
    // };

    // // /**
    // //  * @description: 选中组织时触发
    // //  * @param {*}
    // //  * @return {*}
    // //  * @author: GUOCHAO82
    // //  */
    // const treeSelect = async (keys:string[], e:any) => {
    //   if (keys.length) {
    //     const [key] = keys;
    //     selectTreeNode.key = key;
    //     selectTreeNode.name = e?.node?.dataRef?.orgName || '';
    //   }
    //   console.log(selectTreeNode, e.node, '----selectTreeNode');
    // };
    // getTreeData();
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
.organizational-structure {
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
