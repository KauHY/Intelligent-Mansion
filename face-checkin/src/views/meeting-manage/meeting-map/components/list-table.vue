<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-01-26 17:45:41
 * @LastEditors: Xucaizhi3
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
          新增分布图
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
                type="link"
                @click="operate('del', record)"
              >
                删除
              </a-button>
            </div>
          </template>
          <template #meetingPlaneFigure="{text}">
            <v-image
              width="80px"
              height="80px"
              :keys="text"
            />
          </template>
        </v-table>
      </template>
    </search-box>
    <MeetingMapAddEdit
      v-if="meetingMapFlag"
      ref="meeting"
      v-model:show="meetingMapFlag"
      :meeting-map-config="meetingMapConfig"
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
import MeetingVo, { MeetingMap, MeetingMapList } from '~models/meeting-manage';
import { useSearchListPage, useCachePage } from '~composition/common';
import MeetingMapAddEdit from './MeetingMapAddEdit.vue';
import { getMeetingMapColumns } from '../config';

interface MeetingMapConfig {
  title?: string,
  mapData?: object,
  type?: string
}

type Operatetypes = 'add' | 'edit' | 'del'
const MeetingVoReq = new MeetingVo();
export default defineComponent({
  components: { searchBox, MeetingMapAddEdit },
  props: {
    refresh: {
      type: String as PropType<'refresh' | ''>,
      default: '',
    },
  },
  setup(props) {
    const router = useRouter();
    const meetingMapFlag = ref<boolean>(false);
    const meetingMapConfig = ref<MeetingMapConfig>({});
    const options = useHashMapDics<['YES_OR_NO', 'MEETING_EQUIPMENT', 'MEETING_ROOM_TYPE']>([
      'YES_OR_NO',
      'MEETING_EQUIPMENT',
      'MEETING_ROOM_TYPE',
    ]);
    const columns = getMeetingMapColumns(options);
    const searchConf = reactive<SearchConf<['name']>>({
      name: {
        tag: 'input',
        value: '',
        hidden: true,
      },
    });
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(
      MeetingVoReq.meetingMapListPageApi,
      searchConf,
    );
    const operate = (type: Operatetypes, record?: MeetingMapList) => {
      switch (type) {
        case 'add':
          console.log('add');
          meetingMapConfig.value = {
            title: '新增分布图',
            type,
          };
          meetingMapFlag.value = true;
          break;
        case 'edit':
          if (!record) return;
          console.log('edit');
          meetingMapConfig.value = {
            title: '编辑分布图',
            mapData: record,
            type,
          };
          meetingMapFlag.value = true;
          break;
        case 'del':
          console.log('del');
          if (!record) return;
          Modal.confirm({
            content: '确定删除该分布图？',
            onOk: async () => {
              await MeetingVoReq.meetingMapRemoveApi(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        default:
          break;
      }
    };
    const init = async () => {
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
      searchConf,
      search,
      operate,
      pagination,
      pageChange,
      tableList,
      columns,
      options,
      operateSuccess,
      meetingMapFlag,
      meetingMapConfig,
    };
  },
});
</script>
<style scoped lang="scss">
.d {
    text-shadow: 0;
}
</style>
