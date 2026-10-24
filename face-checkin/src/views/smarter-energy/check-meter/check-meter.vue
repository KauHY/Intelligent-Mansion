<template>
  <div class="menu-list content-section">
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
          @click="operate('import')"
        >
          导入读数
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
          row-key="userId"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #deviceName="{ record }">
            <span
              class="text-link"
              @click="operate('detail', record)"
            >{{ record.deviceName }}</span>
          </template>
          <template #deviceType="{ record }">
            <span>{{ record.deviceType===6? '电能耗':'水能耗' }}</span>
          </template>
          <template #effectType="{ record }">
            <span>{{ record.deviceType===6?options.ELECTRICITY_METER_EFFECT_TYPE[record.effectType]?.label:'水表' }}</span>
          </template>
          <template #effectProperty="{ record }">
            <span>{{ options.METER_EFFECT_PROPERTY[record.effectProperty]?.label }}</span>
          </template>
          <template #thisMonth="{ record }">
            <span>{{ record.meterType===1?'手动抄表':'自动抄表' }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                v-if="record.readingTime.substr(0,7)===startTime"
                type="link"
                @click="operate('edit',record.id)"
              >
                修正读数
              </a-button>
              <a-button
                v-if="record.readingTime.substr(0,7)===startTime"
                type="link"
                @click="operate('del',record.id)"
              >
                删除
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <a-modal
      v-model:visible="visible"
      title="修正读数"
      @ok="handleOk"
    >
      <a-form
        ref="formRef"
        :model="formState"
        :label-col="{span:6}"
        :rules="ruleRef"
      >
        <a-form-item
          label="修正后读数"
          name="correctedReading"
        >
          <a-input
            v-model:value="formState.correctedReading"
            placeholder="请输入当前读数"
          />
        </a-form-item>
        <a-form-item
          label="备注"
        >
          <a-textarea
            v-model:value="formState.remark"
            placeholder="请输入备注"
          />
        </a-form-item>
      </a-form>
    </a-modal>
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
      template-key="批量导入模板/水电表读数导入模板.xlsx"
      @importSuccess="importSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, UnwrapRef,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import * as dayjs from 'dayjs';
import URLS from '~models/urlConstant/smarter-energy';
import SmarterMeter, { RoleListItem } from '~models/smarter-energy';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { useParkInfoOpts } from '~composition/park';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import MeetingRoomEditAdd from './components/checkMeterEditAdd.vue';
import {
  statusName, columns, sourceOpt, categoryOpt,
} from './config';

interface MeetingRoomConfig {
  title: string
  type: string
  meetingData?: object
}
interface FormState {
  correctedReading: number | undefined;
  id: string | undefined;
  remark: string;
}
type Operatetypes = 'add'|'edit'|'del'|'import'|'export' |'detail'
const userInstance = new SmarterMeter();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox, ImportExcel, MeetingRoomEditAdd },
  setup() {
    const formRef = ref();
    const formState: UnwrapRef<FormState> = reactive({
      id: '',
      remark: '',
      correctedReading: undefined,
    });
    const ruleRef = {
      correctedReading: [
        {
          required: true, pattern: /^(([0-9]{1}\d{0,7})|(0{0}))(\.\d{0,2})?$|^0?\.\d{0,2}$/, message: '仅支持输入大于等于0的数字，小数点前最多8位，小数点最多保留两位', trigger: 'change',
        },
      ],
    };
    const startTime = `${dayjs().format('YYYY-MM')}`;

    const rentalFlag = ref<Boolean>(false);
    const meetingRoomConfig = ref<MeetingRoomConfig>();
    const importFlag = ref<Boolean>(false);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = ref<DictionaryMap<['METER_TYPE', 'METER_EFFECT_PROPERTY', 'ELECTRICITY_METER_EFFECT_TYPE']>>({
      METER_TYPE: [],
      METER_EFFECT_PROPERTY: [],
      ELECTRICITY_METER_EFFECT_TYPE: [],
    });
    const visible = ref<boolean>(false);
    const {
      parkOpts,
      getParkOpts,
    } = useParkInfoOpts();
    const searchConf = reactive<SearchConf<['parkOrgId', 'deviceName', 'deviceId', 'effectProperty', 'deviceType', 'meterType', 'month', ]>>({
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择所属园区',
        options: parkOpts,
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
      effectProperty: {
        tag: 'select',
        value: '',
        placeholder: '请选择表计属性',
        options: () => options.value?.METER_EFFECT_PROPERTY,
      },
      deviceType: {
        tag: 'select',
        value: '',
        placeholder: '请选择能耗类别',
        options: categoryOpt,
      },
      meterType: {
        tag: 'select',
        value: '',
        placeholder: '请选择数据来源',
        options: () => sourceOpt,
      },
      month: {
        tag: 'date-range',
        value: '',
        placeholder: ['开始时间', '结束时间'],
        endTimeKey: 'readingTimeEnd',
        startTimeKey: 'readingTimeStart',
      },
    });
    const importTableRef = reactive<{visible: boolean}>({
      visible: false,
    });

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<RoleListItem>(userInstance.listMeterReadingRecordPage, searchConf, null, userInstance.downloadMeterReading);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:any) => {
      formState.id = record;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: '确定删除该表计吗？',
            onOk: async () => {
              await userInstance.removeMeterReadingMeters(record);
              message.success('操作成功');
              search(true);
            },
          });
          break;
        }
        case 'detail': {
          if (!record) return;
          const data = await userInstance.findListMeterById(record.meterId);
          meetingRoomConfig.value = {
            title: '表计详情',
            type,
            meetingData: data,
          };
          rentalFlag.value = true;
          break;
        }
        case 'export': {
          await download();
          break;
        }
        case 'import': {
          importFlag.value = true;
          importExcelConfig.value = {
            title: '导入读数',
            onConfim: userInstance.importMeterReadingRecord,
          };
          break;
        }
        default: {
          formState.correctedReading = undefined;
          visible.value = true;
          break;
        }
      }
    };

    const handleOk = (e: MouseEvent) => {
      formRef.value
        .validate()
        .then(() => {
          userInstance.modifyMeterReading(formState);
          visible.value = false;
          operateSuccess();
        })
        .catch((error: any) => {
          console.log('error', error);
        });
    };
    const operateSuccess = () => {
      search(true);
    };
    const importSuccess = async () => {
      try {
        message.success('操作成功');
        search(true);
      } catch (error) {
        console.log('上传失败');
      }
    };
    const init = async () => {
      getParkOpts();
      search(true);
      options.value = await useHashMapDic<['METER_TYPE', 'METER_EFFECT_PROPERTY', 'ELECTRICITY_METER_EFFECT_TYPE']>(['METER_TYPE', 'METER_EFFECT_PROPERTY', 'ELECTRICITY_METER_EFFECT_TYPE']);
    };
    init();
    onMounted(() => {
    });
    return {
      startTime,
      rentalFlag,
      meetingRoomConfig,
      options,
      formRef,
      operateSuccess,
      handleOk,
      columns,
      statusName,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      visible,
      formState,
      importTableRef,
      URLS,
      ruleRef,
      importFlag,
      importExcelConfig,
      importSuccess,
    };
  },
});
</script>
