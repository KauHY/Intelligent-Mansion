<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-29 12:29:34
-->
<template>
  <div class="company-list content-section">
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
        <a-button
          type="primary"
          @click="operate('import')"
        >
          导入
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="download()"
        >
          导出
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
          <template #companyType="{text}">
            <span>{{ options.COMPANY_TYPE[text]?.label }}</span>
          </template>
          <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template>
          <template #source="{record}">
            <span>{{ options.ADD_DATA_SOURCE[record.source]?.label }}</span>
          </template>
          <template #name="{ record }">
            <span
              class="text-link"
              @click="operate('detail', record)"
            >{{ record.name }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <!-- <a-button
                type="link"
                @click="operate('bind',record)"
              >
                绑定房屋
              </a-button>
              <a-button
                type="link"
                @click="operate('unbund',record)"
              >
                解绑房屋
              </a-button> -->
              <a-button
                type="link"
                @click="operate('disable',record)"
              >
                {{ record.status===1?'禁用':'启用' }}
              </a-button>
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>
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
      :park-opts="parkOpts"
      :menu-list="tableList"
      @operate:success="search(true)"
    />
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      template-key="批量导入模板/入驻企业导入模板.xlsx"
      :import-excel-config="importExcelConfig"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, onActivated,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import operateDialog from './components/operate-dialog.vue';
import {
  companyTypeNames,
  columns,
  statusName,
  operateDialogTitleName,
  statusOpt,
} from './config';

type Operatetypes = 'bind'|'unbund'|'edit'| 'del'|'add'|'detail'|'import' |'disable'

// const a:string = 1;
export default defineComponent({
  name: 'CompanyMangementPompanyList',
  components: { searchBox, ImportExcel },
  setup() {
    // const a:string = 1;
    const router = useRouter();
    // const store = useStore();
    const companyInstance = new CompanyMangement();
    // const parkInstance = new Park();
    // const parkOpts = ref<SelectOptions>([]);
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const { parkOpts, getParkOpts } = useParkInfoOpts();
    const options = useHashMapDics<['COMPANY_TYPE', 'ADD_DATA_SOURCE']>(['COMPANY_TYPE', 'ADD_DATA_SOURCE']);
    const searchConf = reactive<SearchConf<['name', 'companyType', 'parkOrgId', 'status']>>({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入企业名称',
      },
      companyType: {
        tag: 'select',
        value: '',
        options: () => options.COMPANY_TYPE,
        placeholder: '请选择企业类型',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        options: parkOpts,
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: statusOpt,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<CompanyInfo>(companyInstance.searchListPage, searchConf, operateDialog, companyInstance.companyExportApi);
    const { addCachePage } = useCachePage();
    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:CompanyInfo) => {
      operateDialogConf.type = type;
      //   let pageName:string = '';
      switch (type) {
        case 'del': {
          if (!record) return;
          // // console.log('删除');
          Modal.confirm({
            title: () => '',
            content: `删除后，与此企业相关的业务模块及数据将无法使用、企业端的发布权限将受限，且此操作不可恢复。
确定删除企业【${record.name}】吗？`,
            onOk: async () => {
              await companyInstance.companyDel(record.id as string);
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'import': {
          importFlag.value = true;
          importExcelConfig.value = {
            title: '导入入驻企业',
            onConfim: companyInstance.importCompany,
          };
          break;
        }
        case 'disable': {
          if (!record) return;
          const status = record.status === 1 ? 0 : 1;
          const modelText = `禁用后，此企业在企业端的发布权限将受限，企业员工置为“已禁用“状态，且员工公共及公寓门禁通行权限及各业务模块权限将收回。
确定禁用【${record.name}】吗？`;
          const modelStart = `启用后，此企业权限恢复、企业员工置为“已启用“状态，并恢复员工原有的公共及公寓门禁通行权限及各业务模块权限。
确定启用【${record.name}】吗？`;
          Modal.confirm({
            title: () => '',
            content: status === 1 ? modelStart : modelText,
            onOk: async () => {
              await companyInstance.updateStatus({
                id: record.id as string,
                status,
              });
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'unbund':
        case 'bind': {
        //   console.log('绑定房屋');
          const pageName = type === 'bind' ? 'companyMangementBindHouse' : 'companyMangementUnbindHouse';
          addCachePage('CompanyMangementPompanyList');
          const {
            id, parkOrgName, name, parkOrgId,
          } = record as CompanyInfo;
          router.push({
            name: pageName,
            params: {
              companyId: id as string,
              name,
              parkOrgName: parkOrgName as string,
              parkOrgId,
            },
          });
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.title = operateDialogTitleName[type];
          if (type === 'add') {
            operateDialogConf.data = {};
            return;
          }
          operateDialogConf.data = record as CompanyInfo;
          break;
        }
      }
    };
    const importSuccess = async () => {
      try {
        search(true);
        message.success('操作成功');
      } catch (error) {
        console.log('上传失败');
      }
    };
    const init = async () => {
    //   searchConf.parkOrgId.options = await parkInstance.searchParkListName();
      getParkOpts();
      search(true);
    };
    onActivated(() => {
      search(false);
    });
    init();
    return {
      importSuccess,
      importFlag,
      importExcelConfig,
      columns,
      //   menuTypeName,
      //   statusName,
      companyTypeNames,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      download,
      options,
    };
  },
});
</script>
