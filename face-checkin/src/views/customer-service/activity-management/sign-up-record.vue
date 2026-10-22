<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 10:58:09
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
          @click="operate('exportData')"
        >
          导出
        </a-button>
         &nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('black')"
        >
          返回
        </a-button>
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="signUpRecordColumns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <!-- <template #name="{text,record}">
            <span
              class="text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template> -->
          <template #status="{text}">
            <span
              class="text-link"
            >{{ options['ACTIVITY_STATUS']? options['ACTIVITY_STATUS'][text]?.label:'-' }}</span>
          </template>
          <!-- <template #operate="{record}">
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
          </template> -->
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
  onUnmounted,
} from 'vue';
import { useRoute } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import router from '@/routers';
import ActivityManagement, { SignUpRecord } from '~models/activity-management';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import {
  signUpRecordColumns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';

type Operatetypes = 'exportData'|'black'
const activityManagementInstance = new ActivityManagement();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    const options = ref<DictionaryMap<['ACTIVITY_STATUS']>>({
      ACTIVITY_STATUS: [],
    });
    const route = useRoute();
    const searchConf = reactive<SearchConf<['name', 'mobile', 'id']>>({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入姓名',
      },
      mobile: {
        tag: 'input',
        value: '',
        placeholder: '请输入手机号',
      },
      id: { // 活动id
        tag: 'input',
        value: route.params.id || '',
        hidden: true,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<SignUpRecord>(activityManagementInstance.listActivityRegisterRecordPage, searchConf, null, activityManagementInstance.exportActivityRegisterRecord);
    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:SignUpRecord) => {
      operateDialogConf.type = type;
      if (type === 'exportData') {
        await download();
        message.success('导出成功');
        return;
      }
      router.go(-1);
      // switch (type) {
      //   case 'del': {
      //     if (!record) return;
      //     Modal.confirm({
      //       content: `确定删除【${record.name}】吗？`,
      //       onOk: async () => {
      //         await activityManagementInstance.activityDel(record.id);
      //         message.success('操作成功');
      //         search(true);
      //       },
      //     });
      //     break;
      //   }
      //   case 'topping': { // 置顶
      //     const { id, topping } = record as ActivityInfo;
      //     await activityManagementInstance.toppingActivity({
      //       activityId: id,
      //       topping: !topping,
      //     });
      //     message.success('置顶成功');
      //     search(true);
      //     break;
      //   }
      //   case 'record': {
      //     console.log('啊手动阀手动阀');
      //     break;
      //   }
      //   default:
      //     operateDialogConf.visible = true;
      //     operateDialogConf.title = operateDialogTitleName[type];
      //     operateDialogConf.data = record as ActivityInfo;
      // }

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
    });
    const { remoteCachePage } = useCachePage();

    onUnmounted(() => { // 离开页面删除缓存
      remoteCachePage('ActivityManagementList');
    });
    return {
      signUpRecordColumns,
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
