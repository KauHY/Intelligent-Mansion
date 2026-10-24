<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-04-02 10:19:35
-->
<template>
  <div class="jobs-mangement content-section">
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
          @click="manipulate('add')"
        >
          新增
        </a-button>&nbsp;&nbsp;
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
          <template #operate="{ record }">
            <div>
              <a-button
                type="link"
                @click="manipulate('edit', record)"
              >
                编辑
              </a-button>
              <a-button
                type="link"
                @click="manipulate('del', record)"
              >
                删除
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <CustomerTagEditAdd
      v-if="tagFlag"
      ref="tag"
      v-model:show="tagFlag"
      :tag-config="tagConfig"
      @refresh="operateSuccess"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import CustomerVo, { CustomerListKind } from '~models/customer-manage';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { getCustomerTagColumns } from './config';
import CustomerTagEditAdd from './components/CustomerTagEditAdd.vue';

interface TagConfig {
  title: string
  type: string
  tagData?: object
}
// CUSTOMERINFO_TYPE  客户类型
// CUSTOMERINFO_STATUS 客户状态
// INDUSTRY_FIELD 行业领域
// COMPANY_TAXES_TYPE 企业纳税类型
// COMPANY_MANAGEMENT_STATUS 企业经营状态

type Operatetypes = 'add' | 'edit' | 'del'
const CustomerVoReq = new CustomerVo();
export default defineComponent({
  components: { searchBox, CustomerTagEditAdd },
  setup() {
    const tagFlag = ref<Boolean>(false);
    const tagConfig = ref<TagConfig>();
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['name', 'parkOrgId']>>({
      name: {
        tag: 'input',
        placeholder: '请输入标签名称',
        value: '',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        options: () => parkOpts.value,
        showSearch: true,
        filterOption: true,
      },
    });
    const columns = getCustomerTagColumns();
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(CustomerVoReq.customerTagListPageApi, searchConf);
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: CustomerListKind) => {
      switch (type) {
        case 'add':
          tagConfig.value = {
            title: '新增标签',
            type,
          };
          tagFlag.value = true;
          break;
        case 'edit':
          if (!record) return;
          tagConfig.value = {
            title: '编辑标签',
            type,
            tagData: record,
          };
          tagFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.name}】吗？`,
            onOk: async () => {
              await CustomerVoReq.customerTagRemoveApi(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;

        default:
          break;
      }
    };
    const operateSuccess = () => {
      search(true);
    };
    init();
    return {
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateSuccess,
      manipulate,
      tagFlag,
      tagConfig,
    };
  },
});
</script>
