<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-22 16:19:38
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box :search-conf="searchConf">
      <template
        v-if="type !== 'file'"
        #searchOperate
      >
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('export')"
        >
          导出
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="number"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #key="{text}">
            <!-- <span
              class="table-text-link"
              @click="operate('download',record)"
            >{{ text }}</span> -->
            <v-image
              :keys="text"
              width="45px"
              height="45px"
            />
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
  reactive, ref, onMounted, defineComponent, PropType, onActivated, computed, toRefs,
} from 'vue';
import { useRouter } from 'vue-router';

import { message, Modal } from 'ant-design-vue';
import DesignQuestionnaire, { QuestionnaireAnswerDetails, QuestionnaireSet } from '~models/satisfaction-survey';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { getPreviewUrl } from '~models/common';
import {
  columns, textQuestionnaireColumns, fileQuestionnaireColumns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';
type Operatetypes = 'details'|'download'|'export'
const designQuestionnaireInstance = new DesignQuestionnaire();
// const a:string = 1;
export default defineComponent({
  name: 'SatisfactionSurveyQuestionnaireList',
  components: { searchBox },
  props: {
    editorId: {
      type: String,
      default: '',
    },
    questionnaireId: {
      type: String,
      default: '',
    },
    type: {
      type: String as PropType<'file'|'text'>,
      default: '',
    },
  },
  setup(props) {
    const { editorId, type, questionnaireId } = toRefs(props);
    // const options = useHashMapDics<['QUESTIONNAIRE_STATUS']>(['QUESTIONNAIRE_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['editorId', 'keyword', 'questionnaireId']>>({
      editorId: {
        tag: 'input',
        value: editorId.value,
        hidden: true,
      },
      questionnaireId: {
        tag: 'input',
        value: questionnaireId.value,
        hidden: true,
      },
      keyword: {
        tag: 'input',
        value: '',
        hidden: () => type.value === 'file',
        placeholder: '请输入答案关键字',
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage({
      searchMethod: designQuestionnaireInstance.questionnaireAnswerDetailsListPage,
      searchConf,
      operateDialogCp: operateDialog,
      downloadMethod: designQuestionnaireInstance.questionnaireAnswerDetailsExport,
    });
    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const { addCachePage } = useCachePage();
    const router = useRouter();
    const operate = async (type:Operatetypes, record?:QuestionnaireAnswerDetails) => {
      operateDialogConf.type = type;
      if (type === 'export') {
        await download();
        message.success('导出成功');
        return;
      }
      if (!record) return;
      const fileUrl = getPreviewUrl(record.key);
      if (fileUrl) {
        window.open(fileUrl, 'blank');
      }
      // if (type === 'analysisReport') {
      //   router.push({ path: '/satisfaction-survey/analysis-report', query: { id: id.value } });
      //   // addCachePage('SatisfactionSurveyQuestionnaireList');
      //   // return;
      // }

      // operateDialogConf.visible = true;
      // operateDialogConf.title = operateDialogTitleName[type];
      // // if (type === 'add') {
      // //   operateDialogConf.data = {};
      // //   // operateDialogConf.title = ''
      // // }
      // operateDialogConf.data = record as StatisticsListInfo;
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
      columns: type.value === 'text' ? textQuestionnaireColumns : fileQuestionnaireColumns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      // options,
      // isShowPublishBtn,
    };
  },
});
</script>
