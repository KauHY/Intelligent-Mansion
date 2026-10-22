<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 15:38:13
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
          <template #title="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template #status="{text}">
            <span>{{ options.FORM_STATUS[text].label }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                v-if="isShowPublishBtn(record)"
                type="link"
                @click="operate('updateState',record)"
              >
                发布
              </a-button>&nbsp;
              <a-button
                v-if="record.status===2"
                type="link"
                @click="operate('updateState',record)"
              >
                停用
              </a-button>&nbsp;
              <template v-if="[1,3].includes(record.status)">
                <a-button
                  type="link"
                  @click="operate('del',record)"
                >
                  删除
                </a-button>
                <a-button
                  type="link"
                  @click="operate('edit',record)"
                >
                  编辑
                </a-button>
              </template>
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
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, onActivated, computed,
} from 'vue';
import { useRouter } from 'vue-router';

import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import InformationStatistical, { FormManageInfo } from '~models/information-statistical';
import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del'|'details'| 'updateState'
const informationStatisticalInstance = new InformationStatistical();

// const a:string = 1;
export default defineComponent({
  name: 'InformationStatisticalFormManage',
  components: { searchBox },
  setup() {
    const options = useHashMapDics<['FORM_STATUS']>(['FORM_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['title', 'parkOrgId']>>({
      title: {
        tag: 'input',
        value: '',
        placeholder: '请输入表单名称',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        options: () => parkOpts.value,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(informationStatisticalInstance.formManageListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const { addCachePage } = useCachePage();
    const router = useRouter();
    const isShowPublishBtn = computed(() => (record:FormManageInfo) => [1, 3].includes(record.status as number) && (record.cycle !== 4 || Date.now() < new Date(record.deadlineTime).getTime()));
    const operate = async (type:Operatetypes, record?:FormManageInfo) => {
      operateDialogConf.type = type;
      // if (type === 'design' || type === 'details') {
      //   router.push({ path: '/satisfaction-survey/design-questionnaire', query: { id: record?.id, type } });
      //   addCachePage('SatisfactionSurveyQuestionnaireList');
      //   return;
      // }
      // if (type === 'dataStatistics') {
      //   router.push({ path: '/satisfaction-survey/data-statistics', query: { id: record?.id, type } });
      //   addCachePage('SatisfactionSurveyQuestionnaireList');
      //   return;
      // }
      if (type === 'del') {
        if (!record) return;
        Modal.confirm({
          content: `确定删除【${record.title}】吗？`,
          onOk: async () => {
            await informationStatisticalInstance.formManageDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      if (type === 'updateState') { // 发布/停止
        if (!record) return;
        await informationStatisticalInstance.formManageUpdateStatus({
          id: record.id as string,
          status: record.status === 2 ? 3 : 2,
        });
        message.success('操作成功');
        search(true);
        return;
      }
      // if (type === 'details') {
      //   router.push({ path: '/information-statistical/design-questionnaire', query: { id: record?.templateId, type } });
      //   addCachePage('InformationStatisticalFormManage');
      //   return;
      // }
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      // if (type === 'add') {
      //   operateDialogConf.data = {};
      //   // operateDialogConf.title = ''
      // }
      operateDialogConf.data = record as FormManageInfo;
      //   break;
      // }
      // }
    };
    search(true);
    onActivated(() => {
      search(true);
    });
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
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
      options,
      isShowPublishBtn,
    };
  },
});
</script>
