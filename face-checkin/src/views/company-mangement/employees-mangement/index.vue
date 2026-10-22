<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-28 09:48:14
-->
<template>
  <div class="employees-mangement content-section">
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
          @click="operate('export')"
        >
          导出
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
          <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template>
          <template #pics="{text}">
            <v-image
              :keys="text"
              width="80px"
              height="80px"
            />
          </template>
          <template #operate="{record}">
            <div>
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
              <a-button
                type="link"
                @click="operate('disable',record)"
              >
                {{ record.status===1?'禁用':'启用' }}
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
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      :import-excel-config="importExcelConfig"
      template-key="批量导入模板/员工导入模板.zip"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';
// dmMtdXBsb2FkLTE2NDg2MTAzNDI1NDMtODBfMTY0ODY5OTE1NjM2Nw
import { message, Modal } from 'ant-design-vue';
import CompanyMangement, { EmployeeInfo } from '~models/company-mangement';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';
import ImportExcel, { ImportExcelConfig } from './import-excel/index.vue';
import {
  menuTypeName, statusName, columns, operateDialogTitleName, statusOpt,
} from './config';
import operateDialog from './components/operate-dialog.vue';
import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del' | 'disable'|'export' |'import'
const companyMangementInstance = new CompanyMangement();
// const a:string = 1;

export default defineComponent({
  name: 'MenuList',
  components: { searchBox, ImportExcel },
  setup() {
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const {
      parkOpts,
      tagOpts,
      jobOpts,
      companyOpts,
      parkOrgIdChange,
      companyChange,
    } = useOpts();
    const searchConf = reactive<SearchConf<['name', 'mobile', 'parkOrgId', 'companyId', 'jobId', 'status']>>({
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
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: parkOpts,
        handleChange: parkOrgIdChange,
      },
      companyId: {
        tag: 'select',
        value: '',
        placeholder: '请选择企业',
        options: companyOpts,
        filterOption: true,
        showSearch: true,
        handleChange: companyChange,
      },
      jobId: {
        tag: 'select',
        value: '',
        placeholder: '请选择岗位',
        options: () => jobOpts.value,
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选员工状态',
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
    } = useSearchListPage<EmployeeInfo>(companyMangementInstance.employeeListPage, searchConf, operateDialog, companyMangementInstance.employeeDownload);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:EmployeeInfo) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'export': {
          console.log('导出');
          await download();
          break;
        }
        case 'del': {
          if (!record) return;
          // // console.log('删除');
          Modal.confirm({
            title: () => '',
            content: `删除后，与此员工相关的业务模块及数据将无法使用、员工公共及公寓门禁通行权限及各业务模块权限将收回，且此操作不可恢复。
确定删除员工${record.name}吗？`,
            onOk: async () => {
              await companyMangementInstance.employeeDel(record.id as string);
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'import': {
          importFlag.value = true;
          importExcelConfig.value = {
            title: '员工导入',
            postData: 'addCompany',
            isZip: true,
            onConfim: companyMangementInstance.importEmployeesNew,
          };
          break;
        }
        case 'disable': {
          if (!record) return;
          const status = record.status === 1 ? 0 : 1;
          const modelText = `禁用后，员工公共及公寓门禁通行权限及各业务模块权限将收回。
确定禁用【${record.name}】吗？`;
          const modelStart = `启用后，员工公共及公寓门禁通行权限及各业务模块权限将恢复。
确定启用【${record.name}】吗？`;
          Modal.confirm({
            content: status === 1 ? modelStart : modelText,
            onOk: async () => {
              await companyMangementInstance.employeeUpdateStatus({
                id: record.id as string,
                status,
              });
              message.success('操作成功');
              search(true);
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
          operateDialogConf.data = record as EmployeeInfo;
          break;
        }
      }
    };
    const importSuccess = async () => {
      try {
        // await CustomerVoReq.customerRemoveApi('55555');
        message.success('操作成功');
        search(true);
      } catch (error) {
        console.log('上传失败');
      }
    };
    search(true);
    // getParkOpts();
    // companyMangementInstance.getUserMenuList();
    onMounted(() => {
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      columns,
      // menuTypeName,

      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      tagOpts,
      jobOpts,
      companyOpts,
      statusName,
      importFlag,
      importExcelConfig,
      importSuccess,
    };
  },
});
</script>
