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
          @click="manipulate('add')"
        >
          新增
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="manipulate('import')"
        >
          导入
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="manipulate('export')"
        >
          导出
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{ scroll }">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          custom-pagination
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #bodyCell="{ column,record }">
            <template v-if="column.dataIndex === 'employeeName'">
              <span
                class="text-link"
                @click="manipulate('detail', record)"
              >
                {{ record.employeeName }}
              </span>
            </template>
            <template v-if="column.dataIndex === 'operate'">
              <div>
                <a-button
                  type="link"
                  @click="manipulate('disable', record)"
                >
                  {{ record.status===1?'禁用': '启用' }}
                </a-button>
                <template v-if="record.status===0">
                  <a-button
                    type="link"
                    @click="manipulate('edit',record)"
                  >
                    编辑
                  </a-button>
                  <a-button
                    type="link"
                    @click="manipulate('del', record)"
                  >
                    删除
                  </a-button>
                  <a-button
                    type="link"
                    @click="manipulate('addFingerprint', record)"
                  >
                    添加指纹
                  </a-button>
                </template>
              </div>
            </template>
            <template
              v-if="column.dataIndex === 'pics'"
            >
              <v-image
                width="80px"
                height="80px"
                :keys="record.pics"
              />
            </template>
            <template
              v-if="column.dataIndex === 'status'"
            >
              <span>
                {{ record.status ===0 ?'已禁用':'已启用' }}
              </span>
            </template>
            <template
              v-if="column.dataIndex === 'houseList'"
            >
              <span>
                {{ record.houseList?.houseName || '-' }}
              </span>
            </template>
            <template v-if="column.dataIndex === 'houseFunch'">
              <span>
                {{ options?.HOUSE_FUNCTION[record.houseList?.houseFunction]?.label||'-' }}
              </span>
            </template>
          </template>
        </v-table>
      </template>
    </search-box>
    <ApartmentEditAdd
      v-if="customerFlag"
      ref="customer"
      v-model:show="customerFlag"
      :customer-config="customerConfig"
      @refresh="operateSuccess"
    />
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      template-key="批量导入模板/公寓人员导入模板.xlsx"
      :import-excel-config="importExcelConfig"
      @importSuccess="importSuccess"
    />
    <add-fingerprint
      v-if="isShowAddFingerprint"
      v-model:visible="isShowAddFingerprint"
      :employee-id="customerConfig?.customerData?.employeeId"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, defineComponent, h,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import { useHashMapDics } from '~composition/useHashMapDic';
import Apartment from '~models/smarter-apartment';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import CompanyMangement from '~models/company-mangement';
import VImage from '~components/global-components/v-image/v-image.vue';
import VButton from '~components/global-components/v-button/v-button.vue';
import { statusName } from './config';
import ApartmentEditAdd from './components/ApartmentEditAdd.vue';
import addFingerprint from './components/add-fingerprint.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import' |'disable' |'export'|'addFingerprint'
const CustomerVoReq = new Apartment();
export default defineComponent({
  components: {
    searchBox, ApartmentEditAdd, ImportExcel, addFingerprint,
  },
  setup() {
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = useHashMapDics<['HOUSE_FUNCTION']>(['HOUSE_FUNCTION']);
    const searchConf = reactive<SearchConf<['name', 'houseName', 'parkOrgId', 'companyId', 'status']>>({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入姓名',
      },
      houseName: {
        tag: 'input',
        value: '',
        placeholder: '请输入房间号',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择园区',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange: (value: string) => {
          searchConf.companyId.value = '';
          getTagOpts(value);
        },
      },
      companyId: {
        tag: 'select',
        placeholder: '请选择企业',
        value: '',
        options: () => tagOpts.value,
      },
      status: {
        tag: 'select',
        placeholder: '请选择人员状态',
        value: '',
        options: () => statusName,
      },

    });
    const parkOpts = ref<SelectOptions>([]);
    const tagOpts = ref<SelectOptions>([]);
    const columns = [
      {
        title: '所属园区',
        dataIndex: 'parkOrgName',
        fixed: 'left',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '所属企业',
        dataIndex: 'companyName',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '姓名',
        dataIndex: 'employeeName',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '手机号',
        dataIndex: 'mobile',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '身份证号',
        dataIndex: 'idCardNumber',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '照片',
        dataIndex: 'pics',
        ellipsis: false,
        width: 120,
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '关联房间',
        dataIndex: 'houseList',
        slots: { customRender: 'houseList' },
      },
      {
        title: '房屋功能',
        dataIndex: 'houseFunch',
        slots: { customRender: 'houseFunch' },
      },
      {
        title: '创建日期',
        dataIndex: 'createDate',
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '状态',
        dataIndex: 'status',
        slots: { customRender: 'status' },
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
      {
        title: '操作',
        dataIndex: 'operate',
        width: 320,
        fixed: 'right',
        slots: { customRender: 'operate' },
        customCell: (_:any) => ({ rowSpan: _.rowSpanLength }),
      },
    ];
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      CustomerVoReq.listApartmentPersonPage,
      searchConf,
      null,
      CustomerVoReq.exportApartment,
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
    };
    const isShowAddFingerprint = ref(false);
    const manipulate = async (type: Operatetypes, record?: any) => {
      let data = null;
      switch (type) {
        case 'add':
          customerConfig.value = {
            title: '新增公寓人员',
            type,
          };
          customerFlag.value = true;
          break;
        case 'disable':
          if (!record) return;
          Modal.confirm({
            content: `确定${record.status === 0 ? '启用' : '禁用'}员工【${record.employeeName}】吗？`,
            onOk: async () => {
              await CustomerVoReq.updateStatusApartment({ id: record.id, status: record.status === 0 ? 1 : 0 });
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'import': {
          importFlag.value = true;
          importExcelConfig.value = {
            title: '公寓人员导入',
            postData: 'addCompany',
            onConfim: CustomerVoReq.importApartment,
          };
          break;
        }
        case 'export': {
          await download();
          break;
        }
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await CustomerVoReq.findByIdApartment(record.id);
          customerConfig.value = {
            title: type === 'detail' ? '公寓人员详情' : '编辑公寓人员',
            type,
            customerData: data,
          };
          customerFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `确定删除员工【${record.employeeName}】吗？`,
            onOk: async () => {
              await CustomerVoReq.removeApartment(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'addFingerprint': {
          console.log(record, '---employeeId');
          customerConfig.value = {
            title: '',
            type,
            customerData: record,
          };
          isShowAddFingerprint.value = true;
          break;
        }
        default:
          break;
      }
    };
    const changeFunch = async (value:any) => {
      let string = '';
      value.forEach((item: any) => {
        string += `${item.houseName},`;
      });
      console.log(string, 'string');
      return string;
    };
    const getTagOpts = async (value:string) => {
      tagOpts.value = await new CompanyMangement().listCompanyDict(value);
    };
    const operateSuccess = () => {
      search(true);
    };
    const importSuccess = async () => {
      try {
        operateSuccess();
        message.success('操作成功');
      } catch (error) {
        console.log('上传失败');
      }
    };
    init();
    return {
      options,
      changeFunch,
      getTagOpts,
      parkOpts,
      tagOpts,
      statusName,
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateSuccess,
      manipulate,
      customerFlag,
      customerConfig,
      importFlag,
      importExcelConfig,
      importSuccess,
      isShowAddFingerprint,
    };
  },
});
</script>
<style lang="scss" scoped>
.img-size {
    width: 80px;
    height: 80px;
}
</style>
