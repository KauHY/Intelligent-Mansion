<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-12 16:34:16
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
          <template #deviceName="{text,record}">
            <span
              class="text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template #status="{text}">
            <a-tag :color="text===0?'red':'green'">
              <span> {{ options['DEVICE_STATUS'][text]?.label }}</span>
            </a-tag>
          </template>
          <template #deviceType="{text}">
            {{ options['DEVICE_TYPE'][text]?.label }}
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
      :options="options"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive,
  ref,
  onMounted,
  defineComponent,
  PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import EquipmentManagement, { EquipmentInfo } from '~models/equipment-management';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'edit'|'del'|'details'
const equipmentManagementInstance = new EquipmentManagement();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkOpts = ref<SelectOptions>([]);
    const options = ref<DictionaryMap<['DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']>>({
      DEVICE_TYPE: [],
      DEVICE_STATUS: [],
      GATEWAY_TYPE: [],
    });
    const searchConf = reactive<SearchConf<['deviceName', 'deviceType', 'deviceId', 'ipAddress', 'parkOrgId', 'status']>>({
      deviceName: {
        tag: 'input',
        value: '',
        placeholder: '请输入设备名称',
      },
      deviceType: {
        tag: 'select',
        value: '',
        placeholder: '请选择设备类型',
        options: () => options.value.DEVICE_TYPE,
        handleChange: (value:string) => {},
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
        placeholder: '请选择园区',
        showSearch: true,
        filterOption: true,
        options: () => parkOpts.value,
        handleChange: (value:string) => {},
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: () => options.value.DEVICE_STATUS,
      },
    });
    // searchConf.parkOrgId.handleChange = async (parkOrgId:string) => {
    //   if (!parkOrgId) return;
    //   parkingLotOpts.value = await carPassInstance.parkingLotDic(parkOrgId);
    // };
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
    const operate = async (type:Operatetypes, record?:EquipmentInfo) => {
      operateDialogConf.type = type;
      if (type === 'del') {
        if (!record) return;
        // // console.log('删除');
        Modal.confirm({
          content: `确定删除【${record.deviceName}】吗？`,
          onOk: async () => {
            await equipmentManagementInstance.equipmentDel(record.id);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      // if (type === 'add') {
      //   operateDialogConf.data = {};
      //   // operateDialogConf.title = ''
      // }
      operateDialogConf.data = record as EquipmentInfo;
      //   break;
      // }
      // }
    };
    search(true);

    // const options = ref<DictionaryMap<['COMPANY_BIND_CLIENT_TYPE']>>({
    //   COMPANY_BIND_CLIENT_TYPE: [],
    // });
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']>(['DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']);
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
      // parkingLotOpts,
      options,
    };
  },
});
</script>
