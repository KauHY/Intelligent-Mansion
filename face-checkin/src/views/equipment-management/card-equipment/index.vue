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
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #cardNo="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >
              {{ record.cardNo }}
            </span>
          </template>

          <template #effectStartDate="{ record }">
            <a-tooltip>
              <template #title>
                {{ record.effectStartDate }}—{{ record.effectEndDate }}
              </template>
              <span>
                {{ record.effectStartDate }}—{{ record.effectEndDate }}
              </span>
            </a-tooltip>
          </template>
          <template #operate="{ record }">
            <div>
              <!-- <a-button
                v-if="record.status===1"
                type="link"
                @click="manipulate('loss', record)"
              >
                挂失
              </a-button>
              <a-button
                v-if="record.status===2"
                type="link"
                @click="manipulate('unloss', record)"
              >
                取消挂失
              </a-button> -->
              <a-button
                v-if="record.status===4"
                type="link"
                @click="manipulate('bind', record)"
              >
                绑定
              </a-button>
              <a-button
                v-if="record.status===1"
                type="link"
                @click="manipulate('unbound', record)"
              >
                解绑
              </a-button>
              <a-button
                v-if="record.status===1 || record.status===2"
                type="link"
                @click="manipulate('change', record)"
              >
                换卡
              </a-button>
              <a-button
                type="link"
                @click="manipulate('edit',record)"
              >
                编辑
              </a-button>
              <a-button
                v-if="record.status===3 || record.status===4"
                type="link"
                @click="manipulate('del', record)"
              >
                删除
              </a-button>
            </div>
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
      :import-excel-config="importExcelConfig"
      @importSuccess="importSuccess"
    />
    <v-modal
      v-model:visible="visible"
      title="换卡"
      @ok="handleOk"
    >
      <div>
        <a-form
          ref="formRef"
          :model="formState"
          :label-col="{span:6}"
          :rules="ruleRef"
        >
          <a-form-item
            label="原卡号"
          >
            <span>{{ formState.oldcardNo }}</span>
          </a-form-item>

          <a-form-item
            label="新卡号"
            name="cardNo"
          >
            <a-input
              id="valueInput"
              v-model:value="formState.cardNo"
              v-Alphabet
              :maxlength="30"
              placeholder="请输入卡号"
            >
              <template #addonAfter>
                <span
                  style="color: #19aff1; cursor: pointer;"
                  @click="readCardNo"
                >
                  读卡
                </span>
              </template>
            </a-input>
          </a-form-item>
        </a-form>
      </div>
    </v-modal>
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, defineComponent,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import { useHashMapDics } from '~composition/useHashMapDic';
import Equipment from '~models/equipment-management';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import ImportExcel, { ImportExcelConfig } from '~components/import-excel/index.vue';
import CompanyMangement from '~models/company-mangement';
import { getCustomerColumns } from './config';
import ApartmentEditAdd from './components/CardEditAdd.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}
type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import' |'loss' | 'change' |'bind' |'unloss'|'unbound'|'export'
const CustomerVoReq = new Equipment();
export default defineComponent({
  name: 'CardEquipemnt',
  components: { searchBox, ApartmentEditAdd, ImportExcel },
  setup() {
    const formRef = ref();
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const tagOpts = ref<SelectOptions>([]);
    const importExcelConfig = ref<ImportExcelConfig>();
    const options = useHashMapDics<['CARD_TYPE', 'CARD_MEDIUM', 'CARD_STATUS']>(['CARD_TYPE', 'CARD_MEDIUM', 'CARD_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const formState = reactive({
      id: '',
      cardNo: '',
      oldcardNo: '',
    });
    const ruleRef = {
      cardNo: [{
        validator: (rules:unknown, value:string) => {
          // const patrn = /^(^[0-9]*$)|(^[A-Za-z]+$)/;
          // if (!patrn.test(value)) {
          //   return Promise.reject('卡号由8-16位数字或字母组成，输入不合法');
          // }
          if (value.length < 8) {
            return Promise.reject('请输入8-16位大写字母(A-F)或数字');
          }
          return Promise.resolve();
        },
        required: true,
      }],
    };
    const getTagOpts = async (value:string) => {
      tagOpts.value = await new CompanyMangement().listCompanyDict(value);
    };
    const isSetServe = ref<boolean>(false);
    const ws = ref();
    const visible = ref<boolean>(false);
    const searchConf = reactive<SearchConf<['cardNo', 'parkOrgId', 'dateTimes', 'name', 'companyId', 'status']>>({
      cardNo: {
        tag: 'input',
        value: '',
        placeholder: '请输入卡号',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择所属园区',
        value: '',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange(value:string) {
          getTagOpts(value);
        },
      },
      dateTimes: {
        tag: 'date-range',
        value: [],
        startTimeKey: 'effectStartDate',
        endTimeKey: 'effectEndDate',
        valueFormat: 'YYYY-MM-DD',
        placeholder: ['开始时间', '结束时间'],
      },
      name: {
        tag: 'input',
        placeholder: '请输入持卡人',
        value: '',
      },
      companyId: {
        tag: 'select',
        placeholder: '请选择关联企业',
        value: '',
        options: () => tagOpts.value,
      },
      status: {
        tag: 'select',
        placeholder: '请选择状态',
        value: '',
        options: () => options.CARD_STATUS,
      },

    });
    const columns = getCustomerColumns(options);
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      CustomerVoReq.listCardPageCard,
      searchConf,
      null,
      CustomerVoReq.exportCard,
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      search(true);
      // StartUp();
      OpenSocket();
    };
    const manipulate = async (type: Operatetypes, record?: any) => {
      let data = null;
      switch (type) {
        case 'export':
          await download();
          break;
        case 'add':
          customerConfig.value = {
            title: '新增卡片',
            type,
          };
          customerFlag.value = true;
          break;
        case 'unbound':
          if (!record) return;
          Modal.confirm({
            content: '是否确认解绑？',
            onOk: async () => {
              await CustomerVoReq.updateStatus({ id: record.id, type: 4 });
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'loss':
          if (!record) return;
          Modal.confirm({
            content: '是否确认挂失该卡片？',
            onOk: async () => {
              await CustomerVoReq.updateStatus({ id: record.id, type: 2 });
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'unloss':
          if (!record) return;
          Modal.confirm({
            content: `确认取消挂失${record.cardNo}吗？`,
            onOk: async () => {
              await CustomerVoReq.updateStatus({ id: record.id, type: 1 });
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'bind':
          if (!record) return;
          customerConfig.value = {
            title: '绑定',
            type,
            customerData: {
              id: record.id,
              employeeId: record.employeeId,
              companyId: record.companyId,
              parkOrgId: record.parkOrgId,
            },
          };
          customerFlag.value = true;
          break;
        case 'change':
          if (!record) return;
          formState.oldcardNo = record.cardNo;
          formState.id = record.id;
          visible.value = true;
          break;
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await CustomerVoReq.findByIdCard(record.id);
          customerConfig.value = {
            title: type === 'detail' ? '卡片详情' : '编辑卡片',
            type,
            customerData: data,
          };
          customerFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.cardNo}】吗？`,
            onOk: async () => {
              await CustomerVoReq.removeCard(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        default:
          break;
      }
    };
    const handleOk = (e: MouseEvent) => {
      formRef.value
        .validate()
        .then(async () => {
          await CustomerVoReq.exchangeCard({
            id: formState.id,
            cardNo: formState.cardNo,
          });
          message.success('操作成功');
          visible.value = false;
          operateSuccess();
        })
        .catch((error: any) => {
          console.log('error', error);
        });
    };
    const OpenSocket = async () => {
      ws.value = new WebSocket('ws://localhost:12000');
      ws.value.onopen = function a() {
        console.log('Connection open ...');
      };
      ws.value.onmessage = function b(evt: { data: any; }) {
        console.log(`Recevied message: ${evt.data}`);
        const data = JSON.parse(evt.data);
        if (data.data) {
          message.success('读取成功！');
          formState.cardNo = data.data.cardNO;
        } else if (data.code === 7) {
          formState.cardNo = '';
          message.warning('读取卡号失败,请在读卡器上正确放上IC卡！');
        } else {
          formState.cardNo = '';
          message.warning('打开设备失败,请正确连接读卡设备！');
        }
      };
      ws.value.onclose = function c() {
        isSetServe.value = true;
        console.log('Connection closed.');
      };
    };
    const readCardNo = () => {
      let msg = '';
      msg = '{"id": 1, "session": 0, "method": "card.read","info": {"cardType":1} }';
      // console.log(`Connection send: ${msg}`);
      ws.value.send(msg);
      if (isSetServe.value) {
        message.warning('请先安装读卡插件,安装后请刷新当前页面，再进行操作！');
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
    // const StartUp = async () => {
    //   window.open('DHLocalServer://');
    // };
    init();
    return {
      readCardNo,
      // StartUp,
      OpenSocket,
      // StartUp,
      getTagOpts,
      handleOk,
      formRef,
      visible,
      ruleRef,
      formState,
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
    };
  },
});
</script>
