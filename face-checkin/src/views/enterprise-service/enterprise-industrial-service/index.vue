<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-01 14:23:23
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
          <template #name="{record}">
            <span
              class="table-text-link"
              @click="operate('details', record)"
            >{{ record.name }}</span>
          </template>

          <template #operate="{ record }">
            <div>
              <a-button
                type="link"
                @click="operate('add', record)"
              >
                完善信息
              </a-button>&nbsp;&nbsp;
              <!-- <a-button
                type="link"
                @click="operate('del',record)"
              >
                删除
              </a-button>-->
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
import EnterpriseService, { LaneInfo } from '~models/enterprise-service';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { operateDialogTitleName, getColumns } from './config';
import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'add' | 'details'
const serviceRequest = new EnterpriseService();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const parkPop = ref<SelectOptions>([]);
    const options = ref<DictionaryMap<['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'ENTERPRISE_SIZE']>>({
      COMPANY_TYPE: [],
      INDUSTRY_FIELD: [],
      COMPANY_TAXES_TYPE: [],
      ENTERPRISE_SIZE: [],
    });
    const columns = getColumns(options);
    const searchConf = reactive<SearchConf<['parkOrgId', 'name']>>({
      parkOrgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        options: () => parkPop.value,
        handleChange: (value: string) => { },
      },
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入企业名称',
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
    } = useSearchListPage(serviceRequest.equipmentListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type: Operatetypes, record?: LaneInfo) => {
      operateDialogConf.visible = true;
      operateDialogConf.type = type;
      operateDialogConf.data = record as LaneInfo;
      operateDialogConf.title = type === 'add' ? '企业信息编辑' : '企业信息详情';

      console.log('operateDialogConf', operateDialogConf);
    };
    search(true);

    // const options = ref<DictionaryMap<['COMPANY_BIND_CLIENT_TYPE']>>({
    //   COMPANY_BIND_CLIENT_TYPE: [],
    // });
    onMounted(async () => {
      options.value = await useHashMapDic<['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'ENTERPRISE_SIZE']>(['COMPANY_TYPE', 'INDUSTRY_FIELD', 'COMPANY_TAXES_TYPE', 'ENTERPRISE_SIZE']);
      parkPop.value = await new Park().searchParkListName();
      console.log('3.-组件挂载到页面之后执行-------onMounted', operateDialogConf);
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
    };
  },
});
</script>
