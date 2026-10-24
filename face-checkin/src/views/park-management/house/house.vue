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
        <!-- {{ dialogData.parkOrgId }} -->
        <a-tree
          :key="dialogData.parkOrgId"
          :selected-keys="[selectTreeNode.key]"
          :tree-data="treeData"
          @select="treeSelect"
        >
          <template #title="row">
            <div class="park-management-house_svg-btns">
              <span>{{ row.title }}</span>
              <!-- <v-button
                type="link"
                icon="add-fill"
                @click="operate('add',row)"
              />
              <v-button
                v-if="row.dataRef.isEdit"
                type="link"
                icon="editor"
                @click="operate('edit',row)"
              />
              <v-button
                v-if="row.dataRef.isDel"
                type="link"
                icon="ashbin-fill"
                @click="del(row)"
              /> -->
              <template v-if="row.isOperate">
                <v-svg
                  class="park-management-house_svg"
                  name="add-fill"
                  @click="operate('add',row)"
                />
                <v-svg
                  v-if="row.dataRef.isEdit"
                  class="park-management-house_svg"
                  name="editor"
                  @click="operate('edit',row)"
                />
                <v-svg
                  v-if="row.dataRef.isDel"
                  class="park-management-house_svg"
                  name="ashbin-fill"
                  @click="del(row)"
                />
              </template>
            </div>
          </template>
        </a-tree>
      </div>
    </div>
    <div class="right-section">
      <!-- <a-spin size="small" /> -->
      <houseTable
        ref="houseTableRef"
        :select-tree-node="selectTreeNode"
        @on-refresh="getTreeData"
      />
    </div>
    <operate-dialog-floor
      v-if="isRenderDialog&&visible"
      v-model:visible="visible"
      :dialog-data="dialogData"
      :type="currentType"
      @operate:success="operateSuccess"
    />
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
import houseTable from './components/house-table.vue';
import operateDialogFloor from './components/operate-dialog.vue';

const parkInstance = new Park();
const houseInstance = new House();
const floorInstance = new Building();
// interface DialogData {
//   parkOrgName:string,
//   parkOrgId:string,
//   type:number,
//   floorName?:string,
//   buildingId?:string
// }

export default defineComponent({
  name: '',
  components: { operateDialogFloor, houseTable },
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
    const currentType = ref(); // 当前操作类型
    const currentOperateTreeNode = ref<TreeNodeItem>();
    const houseTableRef = ref<InstanceType<typeof houseTable>>(); // 房屋列表组件

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
          isOperate: baseData.type !== 3,
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

    const operate = (operateType:string, treeNodeItem:TreeNodeItem) => {
      // if (type === 'add') {
      const { type, key, title } = treeNodeItem;
      currentOperateTreeNode.value = treeNodeItem;
      visible.value = true;
      currentType.value = operateType;
      // dialogData.value = { type };
      // debugger;
      // if (type === 2) { // 新增房屋时
      Object.assign(dialogData, {
        floorName: title,
        buildingId: key,
        type,
      });
      // }
    };

    const del = (treeNode:TreeNodeItem) => {
      // console.log(treeNode, '---treenode');
      const { type, key, title } = treeNode;
      Modal.confirm({
        title: () => '',
        content: `删除后，与此楼栋相关的业务模块及数据将无法使用，且此操作不可恢复。
确定删除${treeNode.title}吗？`,
        onOk: async () => {
          // await
          await floorInstance.del(key);
          message.success('删除成功！');
          getTreeData();
        },
      });
    };

    const isRenderDialog = ref(false);
    const init = async () => {
      isRenderDialog.value = false;
      parkList.value = await parkInstance.searchParkListName();
      // 默认选中第一个
      const [parkInfo] = parkList.value;
      parkId.value = parkInfo?.value;
      treeSelect([parkInfo.value], parkInfo);
      handleChange(parkId.value);
      selectPark(parkInfo || {});
      isRenderDialog.value = true;
      // console.log(dialogData, '---dialogData');
    };

    // 操作成功，刷新列表
    const operateSuccess = () => {
      getTreeData(); // 刷新房屋树
      houseTableRef.value?.operateSuccess(); // 刷新房屋列表
    };
    // init();
    onMounted(() => {
      init();
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      parkId,
      parkList,
      treeData,
      visible,
      // parkInfo,
      dialogData,
      isRenderDialog,
      currentType,
      handleChange,
      // operateFloorsuccess,
      selectPark,
      // onLoadData,
      // getTreeData,
      getTreeData,
      treeSelect,
      selectTreeNode,
      operate,
      del,
      houseTableRef,
      // add,
      // edit,
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
        overflow: auto;
        background: #fff;
        border-radius: 4px;
    }
}
</style>
