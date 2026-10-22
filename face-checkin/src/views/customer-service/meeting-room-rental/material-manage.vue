<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-09 10:57:44
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
          <template #goodsName="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >{{ record.goodsName }}</span>
          </template>
          <template #goodsType="{ record }">
            <span>{{ record.goodsType===1 ? '购买型' : '租用型' }}</span>
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
    <MaterialManageEditAdd
      v-if="rentalFlag"
      ref="customer"
      v-model:show="rentalFlag"
      :meeting-room-config="meetingRoomConfig"
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
import MeetingRentalVo, { MeetingRentalListKind } from '~models/meeting-rental';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { getMaterialColumns, goodsTypeOpts } from './config';
import MaterialManageEditAdd from './components/MaterialManageEditAdd.vue';

interface MeetingRoomConfig {
  title: string
  type: string
  meetingData?: object
}

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'disable' | 'enable'
const MeetingRentalVoReq = new MeetingRentalVo();
export default defineComponent({
  components: { searchBox, MaterialManageEditAdd },
  setup() {
    const rentalFlag = ref<Boolean>(false);
    const meetingRoomConfig = ref<MeetingRoomConfig>();
    const options = useHashMapDics<['CUSTOMERINFO_TYPE', 'CUSTOMERINFO_STATUS']>(['CUSTOMERINFO_TYPE', 'CUSTOMERINFO_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['goodsName', 'parkOrgId', 'goodsType']>>({
      goodsName: {
        tag: 'input',
        placeholder: '请输入物资名称',
        value: '',
      },
      goodsType: {
        tag: 'select',
        placeholder: '请选择类型',
        value: '',
        options: () => goodsTypeOpts,
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
      },

    });
    const columns = getMaterialColumns(options);
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(
      MeetingRentalVoReq.materialListPageApi,
      searchConf,
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: MeetingRentalListKind) => {
      let data = null;
      switch (type) {
        case 'add':
          meetingRoomConfig.value = {
            title: '新增物资',
            type,
          };
          rentalFlag.value = true;
          break;
        case 'disable':
          if (!record) return;
          console.log(11);
          break;
        case 'enable':
          if (!record) return;
          console.log(11);
          break;
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await MeetingRentalVoReq.materialFindByIdApi(record.id);
          meetingRoomConfig.value = {
            title: type === 'edit' ? '编辑物资' : '物资详情',
            type,
            meetingData: data,
          };
          rentalFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.goodsName}】吗？`,
            onOk: async () => {
              await MeetingRentalVoReq.materialRemoveApi(record.id);
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
      goodsTypeOpts,
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateSuccess,
      manipulate,
      rentalFlag,
      meetingRoomConfig,
    };
  },
});
</script>
