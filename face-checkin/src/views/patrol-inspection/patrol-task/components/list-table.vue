<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-24 15:08:49
-->
<template>
  <div class="menu-list">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <!-- <a-button
          type="primary"
          @click="operate('add')"
        >
          新增
        </a-button>&nbsp;&nbsp; -->
      </template>
      <template #table="{ scroll }">
        <VTable
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #taskName="{ text, record }">
            <span
              class="table-text-link"
              @click="operate('details', record)"
            >{{ text }}</span>
          </template>
          <!-- <template #status="{text}">
            <a-switch
              checked-children="开"
              un-checked-children="关"
              :checked="text===1"
            />
          </template> -->
          <template #operate="{ record }">
            <div>
              <a-button
                v-if="record.status===1"
                type="link"
                @click="operate('link', record)"
              >
                流转
              </a-button>
            </div>
          </template>
        </VTable>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :menu-list="tableList"
      :task-status-opts="options['INSPECTION_TASK_STATUS']"
      :inspection-types="options['INSPECTION_TYPE']"
      @operate:success="operateSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, computed, watch, onActivated,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import PatrolInspection, { PatrolTask } from '~models/patrol-inspection';

import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import operateDialog from './operate-dialog.vue';
import {
  statusName,
  getColumns,
  operateDialogTitleName,
  orgTypeNames,
} from '../config';

type Operatetypes = 'add' | 'edit' | 'del' | 'details' | 'link'
const patrolInspectionInessInstance = new PatrolInspection();
export default defineComponent({
  components: { searchBox },
  props: {
    selectTreeNode: {
      type: Object as PropType<{ name: string, key: string }>,
      default: () => ({}),
    },
    refresh: {
      type: String as PropType<'refresh' | ''>,
      default: '',
    },
  },
  emits: ['on-refresh'],
  setup(props, { emit }) {
    const { addCachePage } = useCachePage();
    const router = useRouter();
    const options = ref<DictionaryMap<[ 'INSPECTION_TYPE', 'INSPECTION_TASK_STATUS']>>({
      INSPECTION_TYPE: [],
      INSPECTION_TASK_STATUS: [],
    });

    const parkOpts = ref<SelectOptions>([]);
    const columns = getColumns(options);
    const searchConf = reactive<SearchConf<['taskName', 'employeeName', 'taskStatus', 'times', 'orgId']>>({
      orgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
      },
      taskName: {
        tag: 'input',
        value: '',
        placeholder: '请输入巡检任务',
      },
      employeeName: {
        tag: 'input',
        value: '',
        placeholder: '请输入巡检人',
      },
      taskStatus: {
        tag: 'select',
        value: '',
        placeholder: '请选择巡检状态',
        options: () => options.value.INSPECTION_TASK_STATUS,
      },
      times: {
        tag: 'date-range',
        placeholder: ['巡检完成开始时间', '巡检完成结束时间'],
        startTimeKey: 'completeStart',
        endTimeKey: 'completeEnd',
        value: [],
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(patrolInspectionInessInstance.patrolTaskListPage, searchConf, operateDialog);
    const operateSuccess = () => {
      search(true);
      // emit('on-refresh');
    };
    onActivated(() => { // 返回时刷新列表数据
      // console.log('激活了-----', props.refresh);
      if (props.refresh) operateSuccess();
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type: Operatetypes, record?: PatrolTask) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.taskTime}】吗？`,
            onOk: async () => {
              await patrolInspectionInessInstance.patrolLineDel(record.id as string);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.data = record as PatrolTask;
          operateDialogConf.title = operateDialogTitleName[type];
        }
      }
      // if (type === 'accept') {
      //   if (!record) return;
      //   await patrolInspectionInessInstance.houseApplyForAccept(record.id as string);
      //   message.success('受理成功');
      //   operateSuccess();
      //   return;
      // }
      // operateDialogConf.visible = true;
      // operateDialogConf.title = '申请详情';
      // operateDialogConf.data = record as ViewHouseApplyInfo;
    };

    // watch(() => props.selectTreeNode.key, () => {
    //   // console.log(props.selectTreeNode.key, '----props.selectTreeNode.key');
    //   if (!props.selectTreeNode.key) return;
    //   searchConf.orgId.value = props.selectTreeNode.key;
    //   search(true);
    // });

    search(true);
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<[ 'INSPECTION_TYPE', 'INSPECTION_TASK_STATUS']>(['INSPECTION_TYPE', 'INSPECTION_TASK_STATUS']);
    };
    onMounted(() => {
      init();
    });
    return {
      columns,
      statusName,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      operateSuccess,
      orgTypeNames,
      options,
    };
  },
  // async mounted() {
  //   console.log('xxxx');
  // },
});
</script>
