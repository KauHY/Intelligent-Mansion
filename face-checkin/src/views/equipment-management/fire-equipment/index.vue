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
          @click="dataSyncHandle"
        >
          设备数据同步
        </a-button>
        <!-- <a-button
          type="primary"
          @click="manipulate('add')"
        >
          新增
        </a-button>&nbsp;&nbsp; -->
      </template>
      <template #table="{ scroll }">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <!-- <template #name="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >{{ record.name }}</span>
          </template> -->
          <template #status="{ record }">
            <span>{{ options.DEVICE_STATUS[record.status]?.label || '-' }}</span>
          </template>
          <!-- <template #floor="{ record }">
            <span>{{ record.floor }}层</span>
          </template> -->
          <!-- <template #operate="{ record }">
            <div>
              <a-button
                type="link"
                @click="manipulate('edit', record)"
              >
                编辑
              </a-button>
              <a-button
                type="link"
                @click="manipulate('del', record)"
              >
                删除
              </a-button>
            </div>
          </template> -->
        </v-table>
      </template>
    </search-box>
    <FireEquiEditAdd
      v-if="customerFlag"
      ref="customer"
      v-model:show="customerFlag"
      :customer-config="customerConfig"
      @refresh="operateSuccess"
    />
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      :import-excel-config="importExcelConfig"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, onMounted, defineComponent,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useHashMapDics } from '~composition/useHashMapDic';
import CustomerVo, { AddParams } from '~models/equipment-management';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import { useParkInfoOpts } from '~composition/park';
import VehicleManagement, { VehicleInfo } from '~models/vehicle-management';
import { getCustomerColumns } from './config';
import FireEquiEditAdd from './components/FireEquiEditAdd.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import'
const CustomerVoReq = new CustomerVo();
const vehicleManagementInstance = new VehicleManagement();
export default defineComponent({
  components: { searchBox, FireEquiEditAdd, ImportExcel },
  setup() {
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = useHashMapDics<['DEVICE_STATUS']>(['DEVICE_STATUS']);
    const {
      parkOpts,
      buildingOpts,
      houseOpts,
      getParkOpts,
      getHouseOpts,
      getBuildingOpts,
    } = useParkInfoOpts();
    const searchConf = reactive<SearchConf<['deviceName', 'status', 'deviceType']>>({
      // parkOrgId: {
      //   tag: 'select',
      //   value: '',
      //   placeholder: '请选择园区',
      //   filterOption: true,
      //   showSearch: true,
      //   options: () => parkOpts.value,
      //   handleChange: (parkOrgId:string) => {
      //     getBuildingOpts(parkOrgId);
      //     searchConf.buildingId.value = '';
      //     searchConf.houseId.value = '';
      //   },
      // },
      // buildingId: {
      //   tag: 'select',
      //   value: '',
      //   placeholder: '请选楼栋',
      //   filterOption: true,
      //   showSearch: true,
      //   options: () => buildingOpts.value,
      //   handleChange: (buildingId:string) => {
      //     getHouseOpts(buildingId);
      //     searchConf.houseId.value = '';
      //   },
      // },
      // houseId: {
      //   tag: 'select',
      //   value: '',
      //   placeholder: '请选房屋',
      //   filterOption: true,
      //   showSearch: true,
      //   options: () => houseOpts.value,
      // },
      deviceType: {
        tag: 'input',
        value: 8,
        placeholder: '请选择类型',
        hidden: true,
      },
      deviceName: {
        tag: 'input',
        value: '',
        placeholder: '请输入设备名称',
      },
      status: {
        tag: 'select',
        value: '',
        options: () => options.DEVICE_STATUS,
        placeholder: '请选择状态',
      },

    });
    const columns = getCustomerColumns(options);
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      vehicleManagementInstance.equipmentListPage,
      searchConf,
      null,
      CustomerVoReq.exchangeFireControlDevice,
    );
    const manipulate = async (type: Operatetypes, record?: AddParams) => {
      let data = null;
      switch (type) {
        case 'add':
          customerConfig.value = {
            title: '新增设备',
            type,
          };
          customerFlag.value = true;
          break;
        case 'import':
          importFlag.value = true;
          importExcelConfig.value = {
            title: '设备导入',
            onConfim: CustomerVoReq.equipmentListPage,
          };
          break;
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await CustomerVoReq.findFireControlDeviceId(record.id);
          customerConfig.value = {
            title: type === 'edit' ? '编辑设备' : '设备详情',
            type,
            customerData: data,
          };
          customerFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.name}】吗？`,
            onOk: async () => {
              await CustomerVoReq.removeFireControlDevice(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;

        default:
          break;
      }
    };
    const dataSyncHandle = async () => {
      const data = await CustomerVoReq.fireDeviceSync();
      if (data) {
        message.success('同步成功');
        search(true);
      }
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
    search(true);
    const init = async () => {
      getParkOpts();
    };
    onMounted(() => {
      init();
    });
    return {
      dataSyncHandle,
      options,
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      download,
      operateSuccess,
      manipulate,
      customerFlag,
      customerConfig,
      importFlag,
      importExcelConfig,
      importSuccess,
    };
  },
});
</script>
