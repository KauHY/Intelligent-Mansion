<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-13 13:46:15
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
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('import')"
        >
          导入
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table>
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="{y: '58vh'}"
          @change="pageChange"
        >
          <!-- <template #status="{text}">
            <span>{{ statusName[text] }}</span>
          </template> -->
          <template #carNum="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
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
      :car-type-opts="options['PARK_TYPE']"
      @operate:success="search(true)"
    />
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      template-key="批量导入模板/车辆导入模板.xlsx"
      :import-excel-config="importExcelConfig"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import CompanyMangement from '~models/company-mangement';
import CarPass, { CarInfo } from '~models/car-pass';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import {
  getColumns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del' |'import'|'details'
const carPassInstance = new CarPass();
// const a:string = 1;
export default defineComponent({
  components: { searchBox, ImportExcel },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    const companyOpts = ref<SelectOptions>([]);
    const importFlag = ref<Boolean>(false);
    const parkingLotOpts = ref<SelectOptions>([]);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = ref<DictionaryMap<['PARK_TYPE', 'CAR_STATUS']>>({
      PARK_TYPE: [],
      CAR_STATUS: [],
    });
    const columns = getColumns(options);
    const searchConf = reactive<SearchConf<['carNum', 'parkingLotId', 'parkOrgId', 'carOwnerName', 'parkType', 'companyId', 'status']>>({
      carNum: {
        tag: 'input',
        value: '',
        placeholder: '请输入车牌号码',
      },
      carOwnerName: {
        tag: 'input',
        value: '',
        placeholder: '请输入车主姓名',
      },
      parkType: {
        tag: 'select',
        value: '',
        placeholder: '请选择停车类型',
        options: () => options.value.PARK_TYPE,
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
      companyId: {
        tag: 'select',
        value: '',
        placeholder: '请选择企业',
        options: () => companyOpts.value,
      },
      parkingLotId: {
        tag: 'select',
        value: '',
        placeholder: '请选择停车场',
        options: () => parkingLotOpts.value,
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: () => options.value.CAR_STATUS,
      },
    });
    searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
      companyOpts.value = await new CompanyMangement().listCompanyDict(parkOrgId);
      parkingLotOpts.value = await carPassInstance.parkingLotDic(parkOrgId);
    };
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<CarInfo>(carPassInstance.carListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:CarInfo) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        // // console.log('删除');
        Modal.confirm({
          content: `确定删除【${record.carNum}】吗？`,
          onOk: async () => {
            await carPassInstance.carDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      if (type === 'import') {
        importFlag.value = true;
        importExcelConfig.value = {
          title: '车辆导入',
          postData: 'addCar',
          onConfim: carPassInstance.importCar,
        };
        return;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      // if (type === 'add') {
      //   operateDialogConf.data = {};
      //   // operateDialogConf.title = ''
      // }
      operateDialogConf.data = record as CarInfo;
      //   break;
      // }
      // }
    };
    const importSuccess = async () => {
      try {
        search(true);
        message.success('操作成功');
      } catch (error) {
        console.log('上传失败');
      }
    };
    search(true);

    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['PARK_TYPE', 'CAR_STATUS']>(['PARK_TYPE', 'CAR_STATUS']);
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
      importFlag,
      importExcelConfig,
      importSuccess,
      parkingLotOpts,
    };
  },
});
</script>
