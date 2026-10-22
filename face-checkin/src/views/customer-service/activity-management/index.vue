<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 18:46:59
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
          @click="operate('add')"
        >
          新增
        </a-button>
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #name="{text,record}">
            <span
              class="text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template #status="{text}">
            <span>{{ options['ACTIVITY_STATUS']? options['ACTIVITY_STATUS'][text]?.label:'-' }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>&nbsp;&nbsp;
              <a-button
                type="link"
                @click="operate('del',record)"
              >
                删除
              </a-button>
              <a-button
                type="link"
                @click="operate('record',record)"
              >
                报名记录
              </a-button>
              <a-button
                type="link"
                @click="operate('topping',record)"
              >
                {{ record.topping?'取消置顶':'置顶' }}
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
      :park-opts="parkOpts"
      :options="options"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive,
  ref,
  onMounted,
  defineComponent,
  PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import ActivityManagement, { ActivityInfo } from '~models/activity-management';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import {
  columns, operateDialogTitleName,
} from './config';
import operateDialog from './components/operate-dialog.vue';

type Operatetypes = 'add'|'edit'|'del'|'details'| 'topping' | 'record'
const activityManagementInstance = new ActivityManagement();
// const a:string = 1;
export default defineComponent({
  name: 'ActivityManagementList',
  components: { searchBox },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    const options = ref<DictionaryMap<['ACTIVITY_STATUS']>>({
      ACTIVITY_STATUS: [],
    });
    const searchConf = reactive<SearchConf<['name', 'parkOrgId', 'effectTime', 'activityTime', 'confRegister', 'status']>>({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入活动名称',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange: (value:string) => {},
      },
      effectTime: {
        tag: 'date-range',
        value: [],
        placeholder: ['生效开始时间', '生效结束时间'],
        endTimeKey: 'effectTimeEnd',
        startTimeKey: 'effectTimeStart',
      },
      activityTime: {
        tag: 'date-range',
        value: [],
        placeholder: ['活动开始时间', '活动结束时间'],
        endTimeKey: 'activityTimeEnd',
        startTimeKey: 'activityTimeStart',
      },
      confRegister: {
        tag: 'select',
        value: '',
        placeholder: '请选择是否需要报名',
        options: [
          {
            label: '是',
            value: true,
          }, {
            label: '否',
            value: false,
          },
        ],
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: () => options.value.ACTIVITY_STATUS,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<ActivityInfo>(activityManagementInstance.activityListPage, searchConf, operateDialog);
    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const { addCachePage } = useCachePage();
    const router = useRouter();
    const operate = async (type:Operatetypes, record?:ActivityInfo) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `删除操作不可撤销，确定删除【${record.name}】吗？`,
            onOk: async () => {
              await activityManagementInstance.activityDel(record.id);
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'topping': { // 置顶
          const { id, topping } = record as ActivityInfo;
          await activityManagementInstance.toppingActivity({
            activityId: id,
            topping: !topping,
          });
          const messageInfo = topping ? '取消置顶成功' : '置顶成功';
          message.success(messageInfo);
          search(true);
          break;
        }
        case 'record': {
          addCachePage('ActivityManagementList');
          router.push({
            name: 'customerServiceActivitySignUpRecord',
            params: {
              id: (record as ActivityInfo).id,
            },
          });
          console.log('啊手动阀手动阀');
          break;
        }
        default:
          operateDialogConf.visible = true;
          operateDialogConf.title = operateDialogTitleName[type];
          operateDialogConf.data = record as ActivityInfo;
      }

      //   break;
      // }
      // }
    };
    search(true);

    // const options = ref<DictionaryMap<['COMPANY_BIND_CLIENT_TYPE']>>({
    //   COMPANY_BIND_CLIENT_TYPE: [],
    // });
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['ACTIVITY_STATUS']>(['ACTIVITY_STATUS']);
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      // parkingLotOpts,
      options,
    };
  },
});
</script>
