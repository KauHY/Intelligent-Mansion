<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-01 16:50:01
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
          <template #name="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >{{ record.name }}</span>
          </template>
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
    <AdvertEditAdd
      v-if="advertFlag"
      ref="advert"
      v-model:show="advertFlag"
      :advert-config="advertConfig"
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
import AdvertVo, { AdvertListKind } from '~models/advertisement';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { getAdvertColumns } from './config';
import AdvertEditAdd from './components/AdvertEditAdd.vue';

interface AdvertConfig {
  title: string,
  type: string
  advertData?: object
}
type Operatetypes = 'add' | 'edit' | 'del' | 'detail'
const AdvertVoReq = new AdvertVo();
export default defineComponent({
  components: { searchBox, AdvertEditAdd },
  setup() {
    const advertFlag = ref<Boolean>(false);
    const advertConfig = ref<AdvertConfig>();
    const options = useHashMapDics<['ADVERTISEMENT_STATUS', 'ADVERTISEMENT_POSITION', 'ADVERTISEMENT_JUMP_TYPE']>([
      'ADVERTISEMENT_STATUS', 'ADVERTISEMENT_POSITION', 'ADVERTISEMENT_JUMP_TYPE',
    ]);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['name', 'parkOrgId', 'effectTime', 'status']>>({
      name: {
        tag: 'input',
        placeholder: '请输入广告名称',
        value: '',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        value: '',
        options: () => parkOpts.value,
      },
      effectTime: {
        tag: 'date-range',
        value: '',
        placeholder: ['生效开始时间', '生效结束时间'],
        endTimeKey: 'effectTimeEnd',
        startTimeKey: 'effectTimeStart',
      },
      status: {
        tag: 'select',
        placeholder: '请选择状态',
        value: '',
        options: () => options.ADVERTISEMENT_STATUS,
      },
    });
    const columns = getAdvertColumns(options);
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(AdvertVoReq.advertListPageApi, searchConf);
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: AdvertListKind) => {
      let data = null;
      switch (type) {
        case 'add':
          advertConfig.value = {
            title: '新增广告',
            type,
          };
          advertFlag.value = true;
          break;
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await AdvertVoReq.advertFindByIdApi(record.id);
          advertConfig.value = {
            title: type === 'edit' ? '编辑广告' : '广告详情',
            type,
            advertData: data,
          };
          advertFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.name}】吗？`,
            onOk: async () => {
              await AdvertVoReq.advertRemoveApi(record.id);
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
      advertFlag,
      advertConfig,
    };
  },
});

</script>
