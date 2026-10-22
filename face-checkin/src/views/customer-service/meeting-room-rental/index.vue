<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-04-02 11:13:14
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
          <template #meetingName="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >{{ record.meetingName }}</span>
          </template>
          <template #operate="{ record }">
            <div>
              <a-button
                v-if="record.status===0"
                type="link"
                @click="manipulate('enable', record)"
              >
                启用
              </a-button>
              <a-button
                v-if="record.status===1"
                type="link"
                @click="manipulate('disable', record)"
              >
                禁用
              </a-button>
              <a-button
                type="link"
                @click="manipulate('edit', record)"
              >
                编辑
              </a-button>
              <a-button
                v-if="record.status===0"
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
    <MeetingRoomEditAdd
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
import MeetingRentalVo, { MeetingRentalListKind, MeetingRentalInfoKind } from '~models/meeting-rental';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { getMeetingRoomColumns, meetingRoomStatus } from './config';
import MeetingRoomEditAdd from './components/MeetingRoomEditAdd.vue';

interface MeetingRoomConfig {
  title: string
  type: string
  meetingData: Partial<MeetingRentalInfoKind>
}

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'disable' | 'enable'
const MeetingRentalVoReq = new MeetingRentalVo();
export default defineComponent({
  components: { searchBox, MeetingRoomEditAdd },
  setup() {
    const rentalFlag = ref<Boolean>(false);
    const meetingRoomConfig = ref<MeetingRoomConfig>({
      title: '',
      type: '',
      meetingData: {},
    });
    const options = useHashMapDics<['CUSTOMERINFO_TYPE', 'CUSTOMERINFO_STATUS']>(['CUSTOMERINFO_TYPE', 'CUSTOMERINFO_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['meetingName', 'parkOrgId', 'status']>>({
      meetingName: {
        tag: 'input',
        placeholder: '请输入会议室名称',
        value: '',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
      },
      status: {
        tag: 'select',
        placeholder: '请选择状态',
        value: '',
        options: () => meetingRoomStatus,
      },
    });
    const columns = getMeetingRoomColumns(options);
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(
      MeetingRentalVoReq.meetingRentalListPageApi,
      searchConf,
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: MeetingRentalListKind) => {
      const data = ref<Partial<MeetingRentalInfoKind>>({});
      let msg = null;
      switch (type) {
        case 'add':
          meetingRoomConfig.value = {
            title: '新增会议室',
            type,
            meetingData: data.value,
          };
          rentalFlag.value = true;
          break;
        case 'disable':
        case 'enable':
          if (!record) return;
          msg = type === 'enable' ? `确定启用会议室【${record.meetingName}】吗？` : `禁用后此会议室将不可预定。确定禁用【${record.meetingName}】吗`;
          // 状态：1开启 0关闭
          Modal.confirm({
            content: msg,
            onOk: async () => {
              await MeetingRentalVoReq.meetingEnableApi({ id: record.id, status: type === 'disable' ? 0 : 1 });
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'edit':
        case 'detail':
          if (!record) return;
          data.value = await MeetingRentalVoReq.meetingRentalFindByIdApi(record.id);
          if (!data.value) return;
          meetingRoomConfig.value = {
            title: type === 'edit' ? '编辑会议室' : '会议室详情',
            type,
            meetingData: data.value,
          };
          rentalFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `确定删除会议室【${record.meetingName}】吗？`,
            onOk: async () => {
              await MeetingRentalVoReq.meetingRentalRemoveApi(record.id);
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
      rentalFlag,
      meetingRoomConfig,
    };
  },
});
</script>
