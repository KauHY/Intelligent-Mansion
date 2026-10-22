<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-22 13:40:33
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
          @click="dataSyncHandle"
        >
          设备数据同步
        </a-button>
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
          <template #deviceName="{record}">
            <span
              class="table-text-link"
              @click="operate('details', record)"
            >{{ record.deviceName }}</span>
          </template>
          <template #operate="{ record }">
            <div>
              <a-button
                type="link"
                @click="operate('perfect', record)"
              >
                完善信息
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
      :park-opts="options"
      :park-pop="parkPop"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
// import { EntranceGuardInfo } from '~models/entranceGuard-management';
import CompanyService from '~models/company-service';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import EquipmentManagement, { EquipmentInfo } from '~models/equipment-management';
import { operateDialogTitleName, getColumns } from './config';
import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'perfect' | 'del' | 'sync' | 'details'
const serviceRequest = new CompanyService();
const equipmentManagementInstance = new EquipmentManagement();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkPop = ref<SelectOptions>([]);
    const options = ref<DictionaryMap <['ACCESS_DEVICE_TYPE', 'DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']>>({
      ACCESS_DEVICE_TYPE: [],
      DEVICE_TYPE: [],
      DEVICE_STATUS: [],
      GATEWAY_TYPE: [],
    });
    const columns = getColumns(options);
    const searchConf = reactive<SearchConf<['deviceName', 'deviceTypes', 'deviceId', 'ipAddress', 'parkOrgId', 'status']>>({
      deviceName: {
        tag: 'input',
        value: '',
        placeholder: '请输入设备名称',
      },
      deviceTypes: {
        tag: 'select',
        value: [4],
        placeholder: '请选择门禁类型',
        options: () => options.value.ACCESS_DEVICE_TYPE,
        handleChange: (value: string) => { },
        hidden: true,
      },
      deviceId: {
        tag: 'input',
        value: '',
        placeholder: '请输入设备ID',
      },
      ipAddress: {
        tag: 'input',
        value: '',
        placeholder: '请输入IP地址',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        options: () => parkPop.value,
        handleChange: (value: string) => { },
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: () => options.value.DEVICE_STATUS,
        handleChange: (value: string) => { },
      },
    });

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<EquipmentInfo>(equipmentManagementInstance.equipmentListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type: Operatetypes, record:EquipmentInfo) => {
      operateDialogConf.type = type;
      operateDialogConf.data = record;
      if (type === 'perfect') {
        operateDialogConf.title = '完善设备信息';
        operateDialogConf.visible = true;
      }
      if (type === 'details') {
        operateDialogConf.title = '视频设备详情';
        operateDialogConf.visible = true;
      }
      if (type === 'del') {
        Modal.confirm({
          content: `确定删除【${record.deviceName}】吗？`,
          onOk: async () => {
            await equipmentManagementInstance.equipmentDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
      }

      console.log('operateDialogConf', operateDialogConf);
    };
    search(true);

    const dataSyncHandle = async () => {
      const data = { deviceTypes: [4] };
      await equipmentManagementInstance.equipmentSync(data);
      message.success('同步成功');
      search(true);
    };
    onMounted(async () => {
      options.value = await useHashMapDic<['ACCESS_DEVICE_TYPE', 'DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']>(['ACCESS_DEVICE_TYPE', 'DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']);
      console.log('字典', options.value);

      parkPop.value = await new Park().searchParkListName();
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
      parkPop,
      options,
      dataSyncHandle,
    };
  },
});
</script>
