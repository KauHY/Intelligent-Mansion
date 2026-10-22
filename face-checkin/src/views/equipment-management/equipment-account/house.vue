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
        >
          <template #operate="row">
            <div class="park-management-house_svg-btns">
              <a-tooltip>
                <template #title>
                  {{ row.title }}
                </template>
                <span class="tree-node-hidden">{{ row.title }}</span>
              </a-tooltip>
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
            </div>
          </template>
        </a-tree>
      </div>
    </div>
    <div class="right-section">
      <houseTable
        ref="houseTableRef"
        :select-tree-node="selectTreeNode"
        :current-value="currentValue"
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
  reactive, onMounted, defineComponent, ref,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import House from '~models/house';
import Building from '~models/building';
import Equipment, { TreeNodeItem } from '~models/equipment-management';
import houseTable from './components/house-table.vue';
import operateDialogFloor from './components/operate-dialog.vue';

const parkInstance = new Park();
const equipmentInstance = new Equipment();
export default defineComponent({
  name: '',
  components: { operateDialogFloor, houseTable },
  setup() {
    // 园区下拉数据
    const parkList = ref<SelectOptions>();
    const parkId = ref<string>('');
    const parkLabel = ref<string>();
    const treeData = ref<TreeNodeItem[]>([]);
    const currentValue = reactive({
      value: '',
      label: '',
      type: '',
    });
    const dialogData = reactive({
      parkOrgName: '',
      parkOrgId: '',
      hierarchy: '',
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
      categoryId: '',
      parentId: '',
      hierarchy: 1,
    });

    // 获取房屋树
    const getTreeData = async () => {
      if (!parkId.value) {
        treeData.value = [];
        return;
      }
      const params = {
        parkOrgId: parkId.value,
        categoryType: 'TZ',
      };
      const data = await equipmentInstance.listMultistageCategoryTreemultistage(params);
      console.log(data, '---data');
      const dealTreeData = (baseData:TreeNodeItem) => {
        Object.assign(baseData, {
          isDel: baseData.hierarchy !== 1,
          isEdit: baseData.hierarchy !== 1,
          key: baseData.value,
          title: baseData.label,
          // slots: baseData.type === 3 ? {  } : { title: 'operate' },
          slots: { title: 'operate' },
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
      Object.assign(currentValue, {
        value,
        label: parkLabel.value,
      });
      getTreeData();
    };
    // 选中树节点时
    const treeSelect = async (keys:string[], e:any) => {
      console.log(keys, e, currentValue, '-------');
      // debugger;
      if (keys.length) {
        const [key] = keys;
        if (e?.node?.dataRef?.hierarchy !== 1) {
          selectTreeNode.categoryId = key;
          selectTreeNode.parentId = currentValue.value;
        }
        selectTreeNode.key = key;
        selectTreeNode.name = e?.node?.dataRef?.label || e?.label || '';
        selectTreeNode.hierarchy = e?.node?.dataRef?.hierarchy || e?.hierarchy || '';
      }
      // console.log(selectTreeNode, '----selectTreeNode');
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
      const {
        hierarchy, key, value, title,
      } = treeNodeItem;
      currentOperateTreeNode.value = treeNodeItem;
      visible.value = true;
      currentType.value = operateType;
      Object.assign(dialogData, {
        floorName: title,
        buildingId: key,
        hierarchy,
        curentId: value,
        operateType,
      });
    };

    const del = (treeNode:TreeNodeItem) => {
      // console.log(treeNode, '---treenode');
      const { type, key, title } = treeNode;
      Modal.confirm({
        content: `确定删除【${treeNode.title}】吗？`,
        onOk: async () => {
          // await
          await equipmentInstance.removemultistage(treeNode.value);
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
      // console.log(parkInfo, 'parkInfo');
      parkId.value = parkInfo?.value;
      parkLabel.value = parkInfo?.label;
      Object.assign(currentValue, {
        value: parkId.value,
        label: parkLabel.value,
      });
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
    });
    return {
      parkLabel,
      currentValue,
      parkId,
      parkList,
      treeData,
      visible,
      dialogData,
      isRenderDialog,
      currentType,
      handleChange,
      selectPark,
      getTreeData,
      treeSelect,
      selectTreeNode,
      operate,
      del,
      houseTableRef,
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

    .tree-node-hidden {
        width: 85px;
        overflow: hidden;
        text-overflow: ellipsis;
    }

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
        width: 240px;
        padding: 30px 20px 0 20px;
        margin-right: 10px;
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
