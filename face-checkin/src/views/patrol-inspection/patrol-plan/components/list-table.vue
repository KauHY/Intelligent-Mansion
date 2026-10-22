<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 14:01:24
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
        <a-button
          type="primary"
          @click="operate('add')"
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
          <template #planName="{ text, record }">
            <span
              class="table-text-link"
              @click="operate('details', record)"
            >{{ text }}</span>
          </template>
          <template #status="{text}">
            <!-- <a-switch
              disabled
              checked-children="开"
              un-checked-children="关"
              :checked="text===1"
            /> -->
            <span>{{ text===1?'已开启':'已关闭' }}</span>
          </template>
          <template #operate="{ record }">
            <div>
              <a-button
                type="link"
                @click="operate('link', record)"
              >
                关联巡检人
              </a-button>
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
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :menu-list="tableList"
      :cycle-type-opts="options['CYCLE_TYPE']"
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
import PatrolInspection, { PatrolPlan } from '~models/patrol-inspection';
import Park from '~models/park';

import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

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
    const options = ref<DictionaryMap<['INSPECTION_TYPE', 'CYCLE_TYPE']>>({
      INSPECTION_TYPE: [],
      CYCLE_TYPE: [],
    });
    const columns = getColumns(options);
    const parkOpts = ref<SelectOptions>([]);

    const searchConf = reactive<SearchConf<['planName', 'status', 'orgId']>>({
      orgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
      },
      planName: {
        tag: 'input',
        value: '',
        placeholder: '请输入计划名称',
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择计划状态',
        options: [
          {
            label: '已开启',
            value: 1,
          },
          {
            label: '已关闭',
            value: 0,
          },
        ],
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(patrolInspectionInessInstance.patrolPlanListPage, searchConf, operateDialog);
    const operateSuccess = () => {
      search(false);
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
    const operate = async (type: Operatetypes, record?: PatrolPlan) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.planName}】吗？`,
            onOk: async () => {
              await patrolInspectionInessInstance.patrolPlanDel(record.id as string);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.data = record as PatrolPlan;
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
      options.value = await useHashMapDic<['INSPECTION_TYPE', 'CYCLE_TYPE']>(['INSPECTION_TYPE', 'CYCLE_TYPE']);
      // console.log(options.value, '-------options.value');
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
