<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-12 15:22:46
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
          导入模板
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
          <template #type="{text}">
            <span>{{ options.CONTRACT_TYPE[text]?.label }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('download',record)"
              >
                下载
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
      :contract-type-opts="options.CONTRACT_TYPE"
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
import { getPreviewUrl } from '~models/common';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import ContractManage, { ContractTemplateListInfo, ContractType } from '~models/contract-manage';

import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'download'|'del'|'add'
const contractManageInstance = new ContractManage();

// const a:string = 1;
export default defineComponent({
  name: 'SatisfactionSurveyQuestionnaireList',
  components: { searchBox },
  setup() {
    const options = useHashMapDics<['CONTRACT_TYPE', 'CON_LIBRARY_TYPE']>(['CONTRACT_TYPE', 'CON_LIBRARY_TYPE']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['templateName', 'parkOrgId']>>({
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
      },
      templateName: {
        tag: 'input',
        value: '',
        placeholder: '请输入模板名称',
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(contractManageInstance.templateListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const { addCachePage } = useCachePage();
    const router = useRouter();
    // const { isShowImport, importExcelConfig, contractType } = useImport();
    const operate = async (type:Operatetypes, record?:ContractTemplateListInfo) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        Modal.confirm({
          content: `确定删除【${record.templateName}】吗？`,
          onOk: async () => {
            await contractManageInstance.templateDel(record.id || '');
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      if (type === 'download') { // 下载
        const fileUrl = record?.templateKey ? getPreviewUrl(record.templateKey) : '';
        if (fileUrl) window.open(fileUrl, 'blank');
        return;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = '导入模板';
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
      // isShowImport,
      // importExcelConfig,
      // contractType,
      // isShowPublishBtn,
    };
  },
});
// function useImport() {
//   const isShowImport = ref(false);
//   const contractType = ref<ContractType>();
//   const importExcelConfig = reactive<ImportExcelConfig>({
//     title: '合同模板导入',
//     onConfim: (formData:FormData, otherData) => {
//       console.log(formData, otherData);
//       // customerManageInstance.templateAdd()
//     },
//   });
//   return {
//     isShowImport,
//     importExcelConfig,
//     contractType,
//   };
// }
</script>
