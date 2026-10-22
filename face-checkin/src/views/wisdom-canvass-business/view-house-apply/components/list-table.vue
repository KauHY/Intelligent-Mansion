<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-24 15:33:41
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
          <template #title="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template

            #operate="{record}"
          >
            <div>
              <a-button
                v-if="record.status===1"
                type="link"
                @click="operate('accept',record)"
              >
                受理
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
      @operate:success="operateSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, watch, onActivated,
} from 'vue';

import { message } from 'ant-design-vue';
// import { useRouter } from 'vue-router';
import WisdomCanvassBusiness, { ViewHouseApplyInfo } from '~models/wisdom-canvass-business';
import Park from '~models/park';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import operateDialog from './operate-dialog.vue';

import {
  statusName,
  getColumns,
  // operateDialogTitleName,
  orgTypeNames,
} from '../config';

type Operatetypes = 'accept'|'details' //
const wisdomCanvassBusinessInstance = new WisdomCanvassBusiness();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox },
  props: {
    selectTreeNode: {
      type: Object as PropType<{name:string, key:string}>,
      default: () => ({}),
    },
    refresh: {
      type: String as PropType<'refresh'|''>,
      default: '',
    },
  },
  emits: ['on-refresh'],
  setup(props) {
    // const { addCachePage } = useCachePage();
    // const router = useRouter();
    const options = ref<DictionaryMap<['APPLY_FOR_HOUSE_STATUS']>>({
      APPLY_FOR_HOUSE_STATUS: [],
    });
    const columns = getColumns(options);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['status', 'times', 'keyword', 'orgId']>>({
      orgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        options: () => parkOpts.value,
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择受理状态',
        options: () => options.value.APPLY_FOR_HOUSE_STATUS,
      },
      times: {
        tag: 'date-range',
        value: [],
        startTimeKey: 'createStartTime',
        endTimeKey: 'createEndTime',
      },
      keyword: {
        tag: 'input',
        value: '',
        placeholder: '请输入关键字(招商标题/用户姓名/咨询顾问)',
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(wisdomCanvassBusinessInstance.houseApplyForListPage, searchConf, operateDialog);
    const operateSuccess = () => {
      search(true);
      // emit('on-refresh');
    };
    onActivated(() => { // 返回时刷新列表数据
      // console.log('激活了-----', props.refresh);
      if (props.refresh)operateSuccess();
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:ViewHouseApplyInfo) => {
      operateDialogConf.type = type;
      if (type === 'accept') {
        if (!record) return;
        await wisdomCanvassBusinessInstance.houseApplyForAccept(record.id as string);
        message.success('受理成功');
        operateSuccess();
        return;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = '申请详情';
      operateDialogConf.data = record as ViewHouseApplyInfo;
    };
    search(true);
    // watch(() => props.selectTreeNode.key, () => {
    //   // console.log(props.selectTreeNode.key, '----props.selectTreeNode.key');
    //   if (!props.selectTreeNode.key) return;
    //   searchConf.orgId.value = props.selectTreeNode.key;
    //   search(true);
    // });

    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['APPLY_FOR_HOUSE_STATUS']>(['APPLY_FOR_HOUSE_STATUS']);
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
    };
  },
  // async mounted() {
  //   console.log('xxxx');
  // },
});
</script>
