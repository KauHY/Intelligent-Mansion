<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-09 10:01:49
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
          @click="operate('export')"
        >
          导出
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="{...scroll,x:1500}"
          @change="pageChange"
        >
          <template #chargeName="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template #dataSource="{text}">
            <span>{{ options.RENT_DATA_SOURCE[text]?.label }}</span>
          </template>
          <template #feeStatus="{text}">
            <span>{{ options.RENT_BILL_STATUS[text]?.label }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :source-opts="options.RENT_DATA_SOURCE"
      :title="operateDialogConf.title"
      :park-opts="parkOpts"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, onActivated, computed,
} from 'vue';
import { useRouter } from 'vue-router';

import { message, Modal } from 'ant-design-vue';
import Park from '~models/park';
import { getPreviewUrl } from '~models/common';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { RentFee, RentFeeInfo } from '~models/collect-fee';

import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';

type Operatetypes = 'estimatedFee'|'modifyDate'|'details'|'export'
const rentFeeInstance = new RentFee();

// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  props: {
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5>,
      default: 3,
    },
  },
  setup(props) {
    const options = useHashMapDics<['FEE_TYPE', 'RENT_DATA_SOURCE', 'RENT_BILL_STATUS']>(['FEE_TYPE', 'RENT_DATA_SOURCE', 'RENT_BILL_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['companyName', 'parkOrgId', 'settledMonth', 'queryType', 'contractType', 'feeStatus']>>({
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
      },
      companyName: {
        tag: 'input',
        value: '',
        placeholder: '请输入驻企业名称',
      },
      settledMonth: {
        tag: 'month-picker',
        value: '',
        placeholder: '请选择结算月份',
      },
      feeStatus: {
        tag: 'select',
        value: '',
        options: () => options.RENT_BILL_STATUS,
        placeholder: '请选择费用状态',
      },
      queryType: {
        tag: 'input',
        hidden: true,
        value: 1,
      },
      contractType: {
        tag: 'input',
        value: props.contractType,
        hidden: true,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<RentFeeInfo>({
      searchMethod: rentFeeInstance.listMeterFeePage,
      searchConf,
      operateDialogCp: operateDialog,
      downloadMethod: rentFeeInstance.exportFeePage,
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const { addCachePage } = useCachePage();
    const router = useRouter();
    // const { selectKeys, onSelectAll, onSelectChange } = useQuantityOperate();

    const operate = async (type:Operatetypes, record?:RentFeeInfo) => {
      if (type === 'export') {
        await download();
        message.success('导出成功');
        return;
      }
      if (type === 'details' && record) {
        operateDialogConf.data = record;
        operateDialogConf.type = type;
        operateDialogConf.visible = true;
        operateDialogConf.title = '详情';
      }
    };
    search(true);

    onActivated(() => {
      search(true);
    });
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();

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
    };
  },
});
</script>
