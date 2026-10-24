<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 10:33:13
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box>
      <template #searchOperate>
        <!-- <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp; -->
        <a-button
          type="primary"
          @click="operate('analysisReport')"
        >
          数据分析
        </a-button>
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="questionnaireId"
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
            <span>{{ options.QUESTIONNAIRE_STATUS[text].label }}</span>
          </template>
          <!-- <template #operate="{record}">
            <div>
              <a-button
                v-if="record.status!==2"
                type="link"
                @click="operate('design',record)"
              >
                设计问卷
              </a-button>&nbsp;
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
                停止
              </a-button>&nbsp;
              <a-button
                type="link"
                @click="operate('copy',record)"
              >
                复制
              </a-button>&nbsp;
              <template v-if="record.status!==2">
                <a-button
                  type="link"
                  @click="operate('edit',record)"
                >
                  问卷设置
                </a-button>&nbsp;
                <a-button
                  type="link"
                  @click="operate('del',record)"
                >
                  删除
                </a-button>
              </template>
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
import DesignQuestionnaire, { StatisticsListInfo, QuestionnaireSet } from '~models/satisfaction-survey';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'details'|'analysisReport'
const designQuestionnaireInstance = new DesignQuestionnaire();
// const a:string = 1;
export default defineComponent({
  name: 'SatisfactionSurveyQuestionnaireList',
  components: { searchBox },
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const { id } = toRefs(props);
    const options = useHashMapDics<['QUESTIONNAIRE_STATUS']>(['QUESTIONNAIRE_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['param']>>({
      param: {
        tag: 'input',
        value: id.value,
        hidden: true,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(designQuestionnaireInstance.questionnaireStatistics, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const { addCachePage } = useCachePage();
    const router = useRouter();
    // TODO: 要添加截止时间判断
    const operate = async (type:Operatetypes, record?:StatisticsListInfo) => {
      operateDialogConf.type = type;
      if (type === 'analysisReport') {
        router.push({ path: '/satisfaction-survey/analysis-report', query: { id: id.value } });
        // addCachePage('SatisfactionSurveyQuestionnaireList');
        return;
      }

      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      // if (type === 'add') {
      //   operateDialogConf.data = {};
      //   // operateDialogConf.title = ''
      // }
      operateDialogConf.data = record as StatisticsListInfo;
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
      // searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      options,
      // isShowPublishBtn,
    };
  },
});
</script>
