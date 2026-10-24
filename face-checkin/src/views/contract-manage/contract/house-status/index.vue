<template>
  <div class="park-management-house">
    <div class="left-section">
      <a-select
        v-model:value="parkId"
        class="search-park"
        style="width: 100%;"
        allow-clear
        placeholder="请选择园区"
        @change="handleChange"
      >
        <a-select-option
          v-for="opt in parkList"
          :key="opt.value"
          @click="selectPark(opt)"
        >
          {{ opt.label }}
        </a-select-option>
      </a-select>
      <div class="building-list">
        <a-tree
          :key="dialogData.parkOrgId"
          :selected-keys="[selectTreeNode.key]"
          :tree-data="treeData"
          @select="treeSelect"
        />
      </div>
    </div>
    <div class="right-section">
      <HouseStatusTable
        ref="houseStatusTableRef"
        :select-tree-node="selectTreeNode"
        @on-refresh="getTreeData"
      />
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, toRefs, onMounted, defineComponent, ref, watch,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import type { PropType } from 'vue';
import type { TreeDataItem } from 'ant-design-vue/lib/tree/Tree';
import Park from '~models/park';
import House, { TreeNodeItem } from '~models/house';
import Building, { FloorListItem } from '~models/building';
import HouseStatusTable from './components/house-status-table.vue';

const parkInstance = new Park();
const houseInstance = new House();
const floorInstance = new Building();

export default defineComponent({
  name: '',
  components: { HouseStatusTable },
  setup() {
    // 园区下拉数据
    const parkList = ref<SelectOptions>();
    const parkId = ref<string>('');
    const treeData = ref<TreeNodeItem[]>([]);
    const dialogData = reactive({
      parkOrgName: '',
      parkOrgId: '',
      type: '',
      floorName: '',
      buildingId: '',
    });
    const visible = ref(false); // 弹框显示隐藏
    const selectTreeNode = reactive({ // 当前选中得节点
      name: '',
      key: '',
      type: 1,
    });

    // 获取房屋树
    const getTreeData = async () => {
      if (!parkId.value) {
        treeData.value = [];
        return;
      }
      const data = await houseInstance.searchListBuildingTree(parkId.value);
      console.log(data, '---data');
      const dealTreeData = (baseData:TreeNodeItem) => {
        Object.assign(baseData, {
          isDel: baseData.type !== 1,
          isEdit: baseData.type !== 1,
          key: baseData.value,
          title: baseData.label,
          slots: baseData.type === 3 ? {} : { title: 'operate' },
        });
        if (baseData.children) {
          baseData.children.forEach((item) => {
            dealTreeData(item);
          });
        }
      };
      dealTreeData(data as unknown as TreeNodeItem);
      treeData.value = [data];
    };

    /**
     * @description: 选择园区
     * @param {*} value
     * @return {*}
     * @author: GUOCHAO82
     */
    const handleChange = async (value:string) => {
      getTreeData();
    };
    // 选中树节点时
    const treeSelect = async (keys:string[], e:any) => {
      // debugger;
      if (keys.length) {
        const [key] = keys;
        selectTreeNode.key = key;
        selectTreeNode.name = e?.node?.dataRef?.label || e?.label || '';
        selectTreeNode.type = e?.node?.dataRef?.type || e?.type || '';
      }
      // console.log(selectTreeNode, e.node, '----selectTreeNode');
    };
    const selectPark = (opt:SelectOptions[0]) => { // 选择园区
      // Object.assign(dialogData, opt);
      const { value, label } = opt;
      Object.assign(dialogData, {
        parkOrgId: value,
        parkOrgName: label,
      });
      treeSelect([opt.value], opt);
    };

    const init = async () => {
      parkList.value = await parkInstance.searchParkListName();
      // 默认选中第一个
      const [parkInfo] = parkList.value;
      parkId.value = parkInfo?.value;
      treeSelect([parkInfo.value], parkInfo);
      handleChange(parkId.value);
      selectPark(parkInfo || {});
    };

    // 操作成功，刷新列表
    const operateSuccess = () => {
      getTreeData(); // 刷新房屋树
    };
    // init();
    onMounted(() => {
      init();
    });
    return {
      parkId,
      parkList,
      treeData,
      visible,
      dialogData,
      handleChange,
      selectPark,
      getTreeData,
      treeSelect,
      selectTreeNode,
      operateSuccess,
    };
  },
});
</script>
<style lang="less" scoped>
.park-management-house {
    display: flex;
    justify-content: space-between;
    height: calc(100% - 40px);
    margin: 20px;
    overflow: hidden;
    background: #f3f3f3;

    &_svg {
        width: 24px;
        height: 24px;
        color: var(--ant-primary-color);
    }

    &_svg-btns {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .left-section {
        min-width: 200px;
        padding: 30px 20px 0 20px;
        margin-right: 10px;
        overflow-x: hidden;
        overflow-y: scroll;
        background: #fff;
        border-radius: 4px;

        .search-park {
            width: 100%;
            margin-bottom: 30px;
        }

        :deep(.ant-tree li span.ant-tree-switcher, .ant-tree li span.ant-tree-iconEle) {
            vertical-align: middle;
        }

        :deep(.ant-tree li .ant-tree-node-content-wrapper) {
            height: auto;
            line-height: 34px;
        }
    }

    .right-section {
        flex: 1;
        padding: 30px 20px 0 0;
        background: #fff;
        border-radius: 4px;
    }
}
</style>
