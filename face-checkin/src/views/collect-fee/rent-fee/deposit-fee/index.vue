<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-23 10:40:16
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
          :scroll="{...scroll,x:2500}"
          @change="pageChange"
        >
          <template #contractNo="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template #cashStatus="{text}">
            <span>{{ options.RENT_CASH_STATUS[text]?.label }}</span>
          </template>
          <template
            #operate="{record}"
          >
            <div>
              <a-button
                v-if="record.cashStatus===2"
                type="link"
                @click="operate('returnMoney',record)"
              >
                退还押金
              </a-button>
              <span v-else>-</span>
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
      :fee-type-opts="options.FEE_TYPE"
      :title="operateDialogConf.title"
      :contract-type="contractType"
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
  getColumns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'returnMoney'|'export'|'details'
const rentFeeInstance = new RentFee();

// const a:string = 1;
export default defineComponent({
  name: 'RentFeeDepositFee',
  components: { searchBox },
  props: {
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5>,
      default: 3,
    },
  },
  setup(props) {
    const options = useHashMapDics<['FEE_TYPE', 'RENT_CASH_STATUS']>(['FEE_TYPE', 'RENT_CASH_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['companyName', 'parkOrgId', 'contractNo', 'times', 'cashStatus', 'queryType', 'contractType']>>({
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
      contractNo: {
        tag: 'input',
        value: '',
        placeholder: '请输入合同编号',
      },
      times: {
        tag: 'date-range',
        value: [],
        placeholder: ['请选择退租开始时间', '请选择退租结束时间'],
        startTimeKey: 'leaseDateStart',
        endTimeKey: 'leaseDateEnd',
      },
      cashStatus: {
        tag: 'select',
        value: '',
        options: () => options.RENT_CASH_STATUS,
        placeholder: '请选择押金状态',
      },
      queryType: {
        tag: 'input',
        hidden: true,
        value: 2, // 押金
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
      searchMethod: rentFeeInstance.rentFeeListPage,
      searchConf,
      operateDialogCp: operateDialog,
      downloadMethod: rentFeeInstance.rentFeeExport,
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
        addCachePage('RentFeeDepositFee');
        const urls = {
          3: '/collect-fee/rent-feeMange/deposit-fee/details',
          5: '/collect-fee/apartment/rent-feeMange/deposit-fee/details',
        };
        router.push({
          path: urls[props.contractType],
          query: { id: record.id },
        });
        return;
      }
      operateDialogConf.data = record as RentFeeInfo;
      operateDialogConf.type = type;
      operateDialogConf.visible = true;
      operateDialogConf.title = '退还押金';
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
      columns: getColumns(options),
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
// 批量操作
// function useQuantityOperate() {
//   const selectKeys = ref<string[]>([]);
//   const onSelectChange = (selectedRow:RentFeeInfo, selected:Boolean) => {
//     if (!selected) {
//       const index = selectKeys.value.findIndex((id) => selectedRow.id === id);
//       selectKeys.value.splice(index, 1);
//       return;
//     }
//     selectKeys.value.push(selectedRow.id);
//     console.log(selectedRow, selected, '----xxxx');
//   };
//   const onSelectAll = (selected:boolean, selectedRows:RentFeeInfo[], changeRows:unknown) => {
//     console.log(selectedRows, changeRows, selected);
//     selectKeys.value = selectedRows.map(({ id }) => id);
//     // console.log(selectKeys, '---selectKeys');
//   };
//   return {
//     selectKeys,
//     onSelectChange,
//     onSelectAll,
//   };
// }
</script>
