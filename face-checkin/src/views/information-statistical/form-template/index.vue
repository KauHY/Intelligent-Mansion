<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 20:10:38
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
          <!-- <template #status="{text}">
            <span>{{ options.QUESTIONNAIRE_STATUS[text].label }}</span>
          </template> -->
          <template #operate="{record}">
            <div>
              <!-- <a-button
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
              <a-button
                v-if="record.status!=1"
                type="link"
                @click="operate('dataStatistics',record)"
              >
                数据统计
              </a-button>&nbsp;
              <template v-if="record.status!==2">
                <a-button
                  type="link"
                  @click="operate('edit',record)"
                >
                  问卷设置
                </a-button>&nbsp;
              </template> -->
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>&nbsp;
              <a-button
                type="link"
                @click="operate('design',record)"
              >
                设计模板
              </a-button>&nbsp;
              <a-button
                type="link"
                @click="operate('del',record)"
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
import InformationStatistical, { FormTemplateInfo, FormTemplateSet } from '~models/information-statistical';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'design' | 'add'|'edit'|'del'|'details'
const informationStatisticalInstance = new InformationStatistical();
// const a:string = 1;
export default defineComponent({
  name: 'InformationStatisticalFormTemplate',
  components: { searchBox },
  setup() {
    const options = useHashMapDics<['QUESTIONNAIRE_STATUS']>(['QUESTIONNAIRE_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['title', 'parkOrgId']>>({
      title: {
        tag: 'input',
        value: '',
        placeholder: '请输入模板名称',
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
    } = useSearchListPage(informationStatisticalInstance.questionnaireListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const { addCachePage } = useCachePage();
    const router = useRouter();

    const operate = async (type:Operatetypes, record?:FormTemplateSet) => {
      operateDialogConf.type = type;
      if (type === 'design' || type === 'details') {
        router.push({ path: '/information-statistical/design-questionnaire', query: { id: record?.id, type } });
        addCachePage('InformationStatisticalFormTemplate');
        return;
      }
      if (type === 'del') {
        if (!record) return;
        Modal.confirm({
          content: `确定删除【${record.title}】吗？`,
          onOk: async () => {
            await informationStatisticalInstance.formTemplateDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }

      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      operateDialogConf.data = record as FormTemplateSet;
      // if (type === 'add') {
      //   operateDialogConf.data = {};
      //   // operateDialogConf.title = ''
      // }
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
      // isShowPublishBtn,
    };
  },
});
</script>
