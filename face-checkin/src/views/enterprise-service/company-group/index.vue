<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-21 17:28:01
-->
<template>
  <div class="pages">
    <div class="left-section">
      <a-select
        v-model:value="parkId"
        class="search-park"
        style="width: 100%;"
        placeholder="请选择园区"
        @change="handleChange"
      >
        <a-select-option
          v-for="opt in parkPop"
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
                style="margin: 0 3px;"
                name="add-fill"
                @click.stop="operate('groupAdd', row)"
              />
              <v-svg
                v-if="row.hierarchy !== 1"
                style="margin: 0 3px;"
                class="park-management-house_svg"
                name="editor"
                @click.stop="operate('groupEdit', row)"
              />
              <v-svg
                v-if="row.hierarchy !== 1"
                style="margin: 0 3px;"
                class="park-management-house_svg"
                name="ashbin-fill"
                @click.stop="del(row)"
              />
            </div>
          </template>
        </a-tree>
      </div>
    </div>
    <div class="right-section">
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
            @click="addCompanyIssue('add')"
          >
            新增
          </a-button>
        </template>
        <template #table="{ scroll }">
          <v-table
            row-key="id"
            bordered
            :columns="columns"
            :pagination="pagination"
            :data-source="tableList"
            :scroll="scroll"
            @change="pageChange"
          >
            <template #groupName="{ record }">
              <span>{{
                record.groupName
              }}</span>
            </template>
            <template #companyName="{ record }">
              <span
                class="table-text-link"
                @click="operate('details', record)"
              >{{
                record.companyName
              }}</span>
            </template>

            <template #operate="{ record }">
              <div>
                <!-- <a-button
                  type="link"
                  @click="operate('edit', record)"
                >
                  编辑
                </a-button>&nbsp;&nbsp; -->
                <a-button
                  type="link"
                  @click="operate('del', record)"
                >
                  移除
                </a-button>
              </div>
            </template>
          </v-table>
        </template>
      </search-box>
    </div>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :park-opts="options"
      :park-pop="parkPop"
      :default-group-data="defaultGroupData"
      @operate:success="operateSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, watch,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import CompanyGroupService, { LaneInfo, TreeNodeItem, GroupAddInfo } from '~models/company-group';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import EnterpriseService from '~models/supply-service';
import { operateDialogTitleName, getColumns } from './config';
import operateDialog from './components/operate-dialog.vue';
import groupDialog from './components/group-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'groupAdd' | 'groupEdit' | 'add' | 'release' | 'edit' | 'del' | 'blockUp' | 'details'
const parkInstance = new Park();
const serviceGroupRequest = new CompanyGroupService();
const serviceRequest = new EnterpriseService();
// const a:string = 1;
export default defineComponent({
  components: { searchBox, groupDialog, operateDialog },
  setup() {
    const parkPop = ref<SelectOptions>([]);
    const options = ref<DictionaryMap<['COMPANY_TYPE', 'INDUSTRY_FIELD', 'ENTERPRISE_SIZE']>>({
      COMPANY_TYPE: [],
      INDUSTRY_FIELD: [],
      ENTERPRISE_SIZE: [],
    });
    const defaultGroupData = ref({});
    const columns = getColumns(options);
    const enterpriseId = ref();
    const parkId = ref();
    const searchConf = reactive<SearchConf<['businessId', 'categoryId', 'parkOrgId']>>({
      businessId: {
        tag: 'select',
        value: '',
        placeholder: '请选择企业',
        options: () => parkingLotOpts.value,
        handleChange: (value: string) => { },
      },
      categoryId: {
        tag: 'input',
        value: enterpriseId.value,
        placeholder: '',
        hidden: true,
      },
      parkOrgId: {
        tag: 'input',
        value: '',
        placeholder: '',
        hidden: true,
      },
    });
    const treeData = ref<TreeNodeItem[]>([]);
    const dialogData = reactive({
      parkOrgName: '',
      parkOrgId: '',
      hierarchy: '',
      floorName: '',
      buildingId: '',
    });
    const selectTreeNode = reactive({ // 当前选中得节点
      name: '',
      key: '',
      categoryId: '',
      parentId: '',
      hierarchy: 1,
      type: '',
    });
    const currentValue = reactive({
      value: '',
      label: '',
    });
    const parkLabel = ref<string>();

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(serviceGroupRequest.listGroupCompanyPage, searchConf, null);
    const parkingLotOpts = ref<SelectOptionsMap>([]);
    watch(parkId, async (val) => {
      searchConf.businessId.value = '';
      searchConf.categoryId.value = '';
      searchConf.parkOrgId.value = val;
      parkingLotOpts.value = await serviceRequest.companyName(val);
      search(true);
    }, { immediate: false });
    // // 选中树节点时
    const treeSelect = (keys: string[], e: any) => {
      searchConf.categoryId.value = '';
      if (keys.length) {
        const [key] = keys;
        if (e?.node?.dataRef?.hierarchy !== 1) {
          selectTreeNode.categoryId = key;
          enterpriseId.value = key;
          selectTreeNode.parentId = currentValue.value;
          searchConf.categoryId.value = key;
        }
        search(true);
        selectTreeNode.key = key;
        selectTreeNode.name = e?.node?.dataRef?.label || e?.label || '';
        selectTreeNode.hierarchy = e?.node?.dataRef?.hierarchy || e?.hierarchy || '';
      }
    };

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type: Operatetypes, record: any) => {
      console.log('用户操作', record);

      operateDialogConf.type = type;
      const {
        value, label, hierarchy, groupName, key, title,
      } = record;
      Object.assign(dialogData, {
        floorName: title,
        buildingId: key,
        hierarchy,
        curentId: value,
        operateType: type,
      });

      if (type === 'groupAdd' && hierarchy === 1) {
        operateDialogConf.title = '新增群组标签';
        operateDialogConf.cp = 'groupDialog';
        operateDialogConf.visible = true;
        defaultGroupData.value = {
          id: '',
          disabled: false,
          expand: true,
          label: '',
          leaf: false,
          parentId: hierarchy === 1 ? '' : value,
          parkOrgId: parkId.value,
          selectable: true,
          type: 'ENTERPRISE_GROUP',
        };
      } else if (type === 'groupAdd' && hierarchy > 1) {
        operateDialogConf.cp = 'operateDialog';
        operateDialogConf.visible = true;
        operateDialogConf.type = type;
        operateDialogConf.title = '新增企业群组';
        const datas = {
          parkOrgId: parkId.value,
          businessIds: [],
          categoryId: record.value,
          remark: '',
          hierarchy,
        };
        defaultGroupData.value = { ...datas };
      }
      if (type === 'groupEdit') {
        operateDialogConf.title = '编辑群组标签';
        operateDialogConf.cp = 'groupDialog';
        operateDialogConf.visible = true;
        defaultGroupData.value = {
          id: value,
          disabled: false,
          expand: true,
          label,
          leaf: false,
          parentId: hierarchy === 2 ? parkId.value : enterpriseId.value,
          parkOrgId: parkId.value,
          selectable: true,
          type: 'ENTERPRISE_GROUP',
        };
      }
      if (type === 'details') {
        operateDialogConf.title = '企业群组详情';
        operateDialogConf.cp = 'operateDialog';
        operateDialogConf.visible = true;
        const datas = {
          parkOrgId: parkId.value,
          businessIds: record.companyId?.split(','),
          categoryId: record.groupName,
          remark: record.remark,
        };
        defaultGroupData.value = { ...datas };
      }

      if (type === 'del') {
        Modal.confirm({
          content: `确定移除企业【${record.companyName}】吗？`,
          onOk: async () => {
            // await serviceRequest.companyRemove(record.id);
            const data = {
              businessId: record.companyId,
              categoryId: record.groupId,
              parkOrgId: record.parkOrgId,
            };
            await serviceGroupRequest.disassociate(data);
            message.success('操作成功');
            search(true);
          },
        });
      }
    };
    // search(true);

    const addCompanyIssue = (type: Operatetypes) => {
      operateDialogConf.cp = 'operateDialog';
      operateDialogConf.visible = true;
      operateDialogConf.type = type;
      operateDialogConf.title = '新增企业群组';
      const datas = {
        parkOrgId: parkId.value,
        businessIds: [],
        categoryId: '',
        remark: '',
      };
      defaultGroupData.value = { ...datas };
    };
    // 获取房屋树
    const getTreeData = async () => {
      if (!parkId.value) {
        treeData.value = [];
        return;
      }
      const params = {
        parkOrgId: parkId.value,
        categoryType: 'ENTERPRISE_GROUP',
      };
      const data = await serviceGroupRequest.listMultistageCategoryTreemultistage(params);
      console.log(data, '---data');
      const dealTreeData = (baseData: TreeNodeItem) => {
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
     * @author:
     */
    const handleChange = async (value: string) => {
      Object.assign(currentValue, {
        value,
        label: parkLabel.value,
      });
      getTreeData();
      // search(true);
    };

    const selectPark = (opt: SelectOptions[0]) => { // 选择园区
      const { value, label } = opt;
      Object.assign(dialogData, {
        parkOrgId: value,
        parkOrgName: label,
      });
      treeSelect([opt.value], opt);
    };
    const del = async (treeNode: TreeNodeItem) => {
      console.log(treeNode, '---treenode');
      Modal.confirm({
        content: `确定删除【${treeNode.label}】吗？`,
        onOk: async () => {
          await serviceGroupRequest.removemultistage(treeNode.value);
          getTreeData();
          search(true);
          message.success('删除成功');
        },
      });
    };

    const init = async () => {
      parkPop.value = await parkInstance.searchParkListName();
      // 默认选中第一个
      const [parkInfo] = parkPop.value;
      parkId.value = parkInfo?.value;
      parkLabel.value = parkInfo?.label;
      Object.assign(currentValue, {
        value: parkId.value,
        label: parkLabel.value,
      });
      treeSelect([parkInfo.value], parkInfo);
      handleChange(parkId.value);
      // selectPark(parkInfo || {});
    };

    // 操作成功，刷新列表
    const operateSuccess = () => {
      getTreeData(); // 刷新房屋树
      search(true);
    };
    onMounted(async () => {
      options.value = await useHashMapDic<['COMPANY_TYPE', 'INDUSTRY_FIELD', 'ENTERPRISE_SIZE']>(['COMPANY_TYPE', 'INDUSTRY_FIELD', 'ENTERPRISE_SIZE']);
      parkPop.value = await new Park().searchParkListName();
    });
    init();
    return {
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      defaultGroupData,
      operate,
      parkPop,
      options,
      addCompanyIssue,
      treeSelect,
      del,
      treeData,
      parkId,
      dialogData,
      selectTreeNode,
      selectPark,
      handleChange,
      operateSuccess,
      parkingLotOpts,
    };
  },
});
</script>
<style lang="scss" scoped>
.pages {
    display: flex;
    justify-content: space-between;
    height: calc(100% - 40px);
    margin: 20px;
    overflow: hidden;
    background: #f3f3f3;

    .park-management-house_svg-btns {
        display: flex;
        align-items: center;
        justify-content: space-between;

        .tree-node-hidden {
            display: inline-block;
            width: 85px;
            overflow: hidden;
            text-overflow: ellipsis;
        }
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
