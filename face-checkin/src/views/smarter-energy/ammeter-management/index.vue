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
        <a-button
          type="primary"
          @click="operate('export')"
        >
          导出
        </a-button>&nbsp;&nbsp;
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
          <template #effectProperty="{ record }">
            <span>{{ options.METER_EFFECT_PROPERTY[record.effectProperty]?.label }}</span>
          </template>
          <template #effectType="{ record }">
            <span>{{ options.ELECTRICITY_METER_EFFECT_TYPE[record.effectType]?.label }}</span>
          </template>
          <template #meterType="{ record }">
            <span>{{ options.METER_TYPE_READING_METHOD[record.meterType]?.label }}</span>
          </template>
          <template #deviceName="{ record }">
            <span
              class="text-link"
              @click="operate('detail', record)"
            >{{ record.deviceName }}</span>
          </template>
          <template #status="{ record }">
            <span>{{ options.DEVICE_STATUS[record.status]?.label }}</span>
          </template>
          <template #usingStatus="{ record }">
            <span>{{ options.METER_STATUS[record.usingStatus]?.label }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                v-if="record.usingStatus===2 || record.usingStatus===1"
                type="link"
                @click="operate('edit',record)"
              >
                修改
              </a-button>
              <a-button
                v-if="record.usingStatus===2"
                type="link"
                @click="operate('stop',record)"
              >
                报废
              </a-button>
              <a-button
                v-if="record.usingStatus===3"
                type="link"
                @click="operate('resetPwd',record)"
              >
                换表
              </a-button>
              <a-button
                v-if="record.usingStatus===3"
                type="link"
                @click="operate('del',record)"
              >
                删除
              </a-button>
              <a-button
                v-if="record.usingStatus!==3"
                type="link"
                @click="operate('disable',record)"
              >
                {{ record.usingStatus===1?'停用':'启用' }}
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <ChangeMeter
      v-if="changeFlag"
      ref="customer"
      v-model:show="changeFlag"
      :change-meter-config="changeMeterConfig"
      @refresh="operateSuccess"
    />
    <MeetingRoomEditAdd
      v-if="rentalFlag"
      ref="customer"
      v-model:show="rentalFlag"
      :meeting-room-config="meetingRoomConfig"
      @refresh="operateSuccess"
    />
    <ImportExcel
      v-if="importFlag"
      v-model:show="importFlag"
      :import-excel-config="importExcelConfig"
      template-key="批量导入模板/电表设备导入模板.xlsx"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, defineComponent,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { MeetingRentalListKind } from '~models/meeting-rental';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import SmarterMeter from '~models/smarter-energy';
import { useParkInfoOpts } from '~composition/park';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import { columns } from './config';
import MeetingRoomEditAdd from './components/MeetingRoomEditAdd.vue';
import ChangeMeter from './components/MaterialEditAdd.vue';

interface MeetingRoomConfig {
  title: string
  type: string
  meetingData?: object
}

interface changeMeterConfig {
  title: string
  data?: object
}

type Operatetypes = 'add'|'edit'|'del' | 'disable'| 'stop' | 'resetPwd' | 'export' | 'import' |'detail'
export default defineComponent({
  components: {
    searchBox, MeetingRoomEditAdd, ChangeMeter, ImportExcel,
  },
  setup() {
    const userInstance = new SmarterMeter();
    const rentalFlag = ref<Boolean>(false);
    const changeFlag = ref<Boolean>(false);
    const meetingRoomConfig = ref<MeetingRoomConfig>();
    const changeMeterConfig = ref<changeMeterConfig>();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = ref<DictionaryMap<['METER_STATUS', 'METER_TYPE', 'METER_EFFECT_PROPERTY', 'ELECTRICITY_METER_EFFECT_TYPE', 'METER_TYPE_READING_METHOD', 'DEVICE_STATUS']>>({
      METER_TYPE: [],
      METER_EFFECT_PROPERTY: [],
      ELECTRICITY_METER_EFFECT_TYPE: [],
      METER_TYPE_READING_METHOD: [],
      METER_STATUS: [],
      DEVICE_STATUS: [],
    });
    const {
      parkOpts,
      buildingOpts,
      houseOpts,
      getParkOpts,
      getHouseOpts,
      getBuildingOpts,
    } = useParkInfoOpts();
    const searchConf = reactive<SearchConf<['parkOrgId', 'status', 'deviceId', 'buildingId', 'houseId', 'deviceName', 'meterType', 'effectProperty', 'deviceType', 'usingStatus']>>({
      parkOrgId: {
        tag: 'select',
        value: '',
        options: parkOpts,
        filterOption: true,
        showSearch: true,
        placeholder: '请选择所属园区',
        handleChange: (value:string) => {},
      },
      buildingId: {
        tag: 'select',
        value: '',
        placeholder: '所属楼栋',
        options: buildingOpts,
        handleChange: (value:string) => {},
      },
      houseId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        options: houseOpts,
        placeholder: '所属房屋',
      },
      deviceName: {
        tag: 'input',
        value: '',
        placeholder: '请输入表计名称',
      },
      deviceId: {
        tag: 'input',
        value: '',
        placeholder: '请输入设备ID',
      },
      meterType: {
        tag: 'select',
        value: '',
        placeholder: '请选择表类型',
        options: () => options.value?.METER_TYPE,
      },
      effectProperty: {
        tag: 'select',
        value: '',
        placeholder: '请选择表计属性',
        options: () => options.value?.METER_EFFECT_PROPERTY,
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择运行状态',
        options: () => options.value?.DEVICE_STATUS,
      },
      usingStatus: {
        tag: 'select',
        value: '',
        placeholder: '请选择表计状态',
        options: () => options.value?.METER_STATUS,
      },
      deviceType: {
        tag: 'input',
        hidden: true,
        value: 6,
      },
    });
    searchConf.parkOrgId.handleChange = (parkOrgId:string) => {
      searchConf.buildingId.value = '';
      searchConf.houseId.value = '';
      getBuildingOpts(parkOrgId);
    };
    searchConf.buildingId.handleChange = (buildingId:string) => {
      searchConf.houseId.value = '';
      getHouseOpts(buildingId);
    };
    const {
      pagination, pageChange, tableList, search, operateDialogConf, download,
    } = useSearchListPage(
      userInstance.meterListPageApi,
      searchConf,
      MeetingRoomEditAdd,
      userInstance.listMeterdownload,
    );

    const operate = async (type: Operatetypes, record?: any) => {
      let data = null;
      const msg = null;
      switch (type) {
        case 'add':
          meetingRoomConfig.value = {
            title: '新增表计',
            type,
          };
          rentalFlag.value = true;
          break;
        case 'resetPwd':
          changeMeterConfig.value = {
            title: '换表',
            data: {
              initialReading: record?.initialReading,
              id: record?.id,
              meterType: record?.meterType,
            },

          };
          changeFlag.value = true;
          break;
        case 'export': {
          await download();
          break;
        }
        case 'import': {
          importFlag.value = true;
          importExcelConfig.value = {
            title: '电表导入',
            postData: 6,
            onConfim: userInstance.listMeterimportPoint,
          };
          break;
        }
        case 'disable': {
          if (!record) return;
          const statusone = record.usingStatus === 1 ? 2 : 1;
          Modal.confirm({
            content: `确定${statusone === 1 ? '启用' : '停用'}该表计吗？`,
            onOk: async () => {
              await userInstance.updateMeterStatus({
                id: record.id,
                usingStatus: statusone,
              });
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'stop': {
          if (!record) return;
          Modal.confirm({
            content: '确定报废表计？报废的表计将无法再录入读数，历史记录仍然保存，此操作无法撤销，确定继续？',
            onOk: async () => {
              await userInstance.updateMeterStatus({
                id: record.id,
                usingStatus: 3,
              });
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await userInstance.findListMeterById(record.id);
          meetingRoomConfig.value = {
            title: type === 'edit' ? '编辑表计' : '表计详情',
            type,
            meetingData: data,
          };
          rentalFlag.value = true;
          break;
        case 'del': {
          if (!record) return;
          Modal.confirm({
            title: () => '',
            content: `删除后，与此设备相关的业务模块及数据将无法使用（如：抄表、用量统计、收费等），且此操作不可恢复。
确定删除${record.deviceName}吗？`,
            onOk: async () => {
              await userInstance.removeMeters(record.id);
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }

        default:
          break;
      }
    };
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
      // eslint-disable-next-line max-len
      options.value = await useHashMapDic<['DEVICE_STATUS', 'METER_TYPE', 'METER_STATUS', 'METER_EFFECT_PROPERTY', 'ELECTRICITY_METER_EFFECT_TYPE', 'METER_TYPE_READING_METHOD']>(['DEVICE_STATUS', 'METER_TYPE', 'METER_STATUS', 'METER_EFFECT_PROPERTY', 'ELECTRICITY_METER_EFFECT_TYPE', 'METER_TYPE_READING_METHOD']);
    };
    const importSuccess = async () => {
      try {
        message.success('操作成功');
        operateSuccess();
      } catch (error) {
        console.log('上传失败');
      }
    };
    const operateSuccess = () => {
      search(true);
    };
    init();
    return {
      operateDialogConf,
      options,
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateSuccess,
      operate,
      rentalFlag,
      changeFlag,
      meetingRoomConfig,
      changeMeterConfig,
      importFlag,
      importExcelConfig,
      importSuccess,
    };
  },
});
</script>
