<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-07-05 11:32:16
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <div class="menu-list">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <!-- <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp; -->
        <a-button
          type="primary"
          @click="operate('add')"
        >
          会议室新增
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
                @click="operate('edit', record)"
              >
                编辑
              </a-button>
              <a-button
                v-if="record.status === 0"
                type="link"
                @click="operate('del', record)"
              >
                删除
              </a-button>
              <a-button
                type="link"
                @click="operate('book', record)"
              >
                预定概览
              </a-button>
              <a-button
                v-if="record.screenStatus === 2"
                type="link"
                @click="operate('look', record)"
              >
                查看屏幕
              </a-button>
              <a-button
                v-if="record.screenStatus === 2"
                type="link"
                @click="operate('close', record)"
              >
                关闭屏
              </a-button>
              <a-button
                v-if="record.screenStatus === 1"
                type="link"
                @click="operate('wake', record)"
              >
                唤醒屏
              </a-button>
            </div>
          </template>
          <template #openTimeStart="{ record }">
            <span v-if="record.openTimeStart">
              {{ record.openTimeStart+ '-' + record.openTimeEnd }}
            </span>
            <span v-else>-</span>
          </template>
          <template #status="{ record }">
            <a-switch
              :checked="record.status===1"
              @change="statusChange(record, $event)"
            />
          </template>
        </v-table>
      </template>
    </search-box>
    <MeetingAddEdit
      v-if="meetingFlag"
      ref="meeting"
      v-model:show="meetingFlag"
      :meeting-config="meetingConfig"
      @refresh="operateSuccess"
    />
    <LookScreen
      v-if="screenFlag"
      v-model:show="screenFlag"
      :meeting-config="meetingConfig"
      @refresh="operateSuccess"
    />
  </div>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, onActivated, PropType,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import MeetingVo, { MeetingList } from '~models/meeting-manage';
import { useSearchListPage, useCachePage } from '~composition/common';
import MeetingAddEdit from './MeetingAddEdit.vue';
import LookScreen from './LookScreen.vue';
import { getMeetingListColumns } from '../config';

type Operatetypes = 'add' | 'edit' | 'del' |'book' | 'look' | 'close' |'wake'
interface MeetingConfig {
  title: string,
  type: string
  meetData?: object
}
export default defineComponent({
  components: { searchBox, MeetingAddEdit, LookScreen },
  props: {
    refresh: {
      type: String as PropType<'refresh' | ''>,
      default: '',
    },
  },
  setup(props) {
    const router = useRouter();
    const MeetingVoReq = new MeetingVo();
    const meetingFlag = ref<boolean>(false);
    const screenFlag = ref<boolean>(false);
    const meetingConfig = ref<MeetingConfig>();
    const options = useHashMapDics<['YES_OR_NO', 'SCREEN_DEVICE_STATUS', 'MEETING_USED_STATE']>([
      'YES_OR_NO', 'SCREEN_DEVICE_STATUS', 'MEETING_USED_STATE',
    ]);
    const columns = getMeetingListColumns(options);
    const searchConf = reactive<SearchConf<['name']>>({
      name: {
        tag: 'input',
        value: '',
        hidden: true,
      },
    });
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(MeetingVoReq.meetingListPageApi, searchConf);
    const operate = async (type: Operatetypes, record?: MeetingList) => {
      let data = null;
      let url = null;
      switch (type) {
        case 'add':
          console.log('add');
          meetingConfig.value = {
            title: '新增会议室',
            type,
            meetData: {},
          };
          meetingFlag.value = true;
          break;
        case 'edit':
          if (!record) return;
          console.log('edit');
          data = await MeetingVoReq.meetingFindApi(record.id);
          meetingConfig.value = {
            title: '编辑会议室',
            type,
            meetData: data,
          };
          meetingFlag.value = true;
          break;
        case 'del':
          console.log('del');
          if (!record) return;
          Modal.confirm({
            content: '确定删除该会议室？',
            onOk: async () => {
              await MeetingVoReq.meetingRemoveApi(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'wake':
          if (!record) return;
          Modal.confirm({
            content: `确定唤醒${record.meetingName}的信息屏？`,
            onOk: async () => {
              await MeetingVoReq.wakeUpDevice(record.screenDeviceId);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'close':
          if (!record) return;
          Modal.confirm({
            content: `关闭${record.meetingName}的信息屏？`,
            onOk: async () => {
              await MeetingVoReq.sleepDevice(record.screenDeviceId);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'look':
          if (!record) return;
          data = await MeetingVoReq.getMeetingScreenshot(record.screenDeviceId);
          meetingConfig.value = {
            title: '',
            type,
            meetData: data,
          };
          screenFlag.value = true;
          break;
        case 'book':
          if (!record) return;
          url = `${window.location.origin}${compileConf.publicPath}#/scheduled-overview?id=${record.id}&&type`;
          // console.log(url, 'url');
          window.open(url, '_blank');
          break;
        default:
          break;
      }
    };
    const init = async () => {
    };
    const statusChange = async (record: MeetingList, e?:Boolean) => {
      await MeetingVoReq.meetingChangeApi({ id: record.id, status: e ? 1 : 0 });
      message.success('操作成功');
      record.status = e ? 1 : 0;
    };
    const operateSuccess = () => {
      search(true);
    };
    onActivated(() => {
      // 返回时刷新列表数据
      console.log('激活了-----', props.refresh);
      if (props.refresh) operateSuccess();
    });
    onMounted(() => {
      search(true);
      init();
    });
    return {
      operateSuccess,
      statusChange,
      searchConf,
      search,
      operate,
      pagination,
      pageChange,
      tableList,
      columns,
      options,
      meetingFlag,
      screenFlag,
      meetingConfig,
    };
  },
});
</script>
<style scoped lang="scss">
.d {
    text-shadow: 0;
}
</style>
