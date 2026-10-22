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
          <template #deviceName="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >
              {{ record.deviceName }}
            </span>
          </template>
          <template #operate="{ record }">
            <div>
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
import { getCustomerColumns } from './config';
import ApartmentEditAdd from './components/CardEditAdd.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}
type Operatetypes = 'add' | 'edit' | 'del' | 'detail'
const CustomerVoReq = new Equipment();
export default defineComponent({
  name: 'CardEquipemnt',
  components: { searchBox, ApartmentEditAdd },
  setup() {
    const formRef = ref();
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();

    const options = useHashMapDics<['SCREEN_DEVICE_STATUS', 'DEVICE_TYPE']>(['SCREEN_DEVICE_STATUS', 'DEVICE_TYPE']);
    const parkOpts = ref<SelectOptions>([]);
    const formState = reactive({
      id: '',
      cardNo: '',
    });
    const ruleRef = {
      cardNo: [
        {
          required: true, message: '请输入新卡号', trigger: 'blur',
        },
      ],
    };
    const visible = ref<boolean>(false);
    const searchConf = reactive<SearchConf<['deviceName', 'parkOrgId', 'deviceId', 'screenDeviceStatus', 'deviceType']>>({
      deviceName: {
        tag: 'input',
        value: '',
        placeholder: '请输入设备名称',
      },
      deviceId: {
        tag: 'input',
        placeholder: '请输入设备ID',
        value: '',
      },
      parkOrgId: {
        tag: 'select',
        placeholder: '请选择所属园区',
        value: '',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
      },
      screenDeviceStatus: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: () => options.SCREEN_DEVICE_STATUS,
        handleChange: (value: string) => { },
      },
      deviceType: {
        tag: 'input',
        value: 7,
        hidden: true,
      },

    });
    const columns = getCustomerColumns(options);
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(
      CustomerVoReq.listScreenDevicePage,
      searchConf,
      null,
    );
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      // searchConf.parkOrgId.value = parkOpts.value[0].value;
      search(true);
    };
    const manipulate = async (type: Operatetypes, record?: any) => {
      let data = null;
      switch (type) {
        case 'add':
          customerConfig.value = {
            title: '新增会议室信息屏',
            type,
          };
          customerFlag.value = true;
          break;
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await CustomerVoReq.equipmentFindById(record.id);
          customerConfig.value = {
            title: type === 'detail' ? '会议室信息屏详情' : '编辑会议室信息屏',
            type,
            customerData: data,
          };
          customerFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `删除信息屏将自动解除与会议室的关联关系，确定删除【${record.deviceName}】吗？`,
            onOk: async () => {
              await CustomerVoReq.equipmentDel(record.id);
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
        .then(() => {
          CustomerVoReq.exchangeCard(formState);
          message.success('操作成功');
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
    init();
    return {
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
    };
  },
});
</script>
