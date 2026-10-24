<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-27 10:20:55
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
          <template #tradeNo="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text||'-' }}</span>
          </template>
          <template #payType="{text}">
            <span>{{ options.RENT_FEE_PAY_TYPE[text]?.label }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      :id="operateDialogConf.data.id"
      v-model:visible="operateDialogConf.visible"
      :pay-type-opts="options.RENT_FEE_PAY_TYPE"
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
import { RentFee, RentFeeInfo, RentFeeTransactionDetail } from '~models/collect-fee';

import {
  getColumns, operateDialogTitleName,
} from './config';

import operateDialog from './components/return-money-details.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'estimatedFee'|'modifyDate'|'details'|'export'
const rentFeeInstance = new RentFee();

// const a:string = 1;
export default defineComponent({
  name: 'RentFeeTransactionDetail',
  components: { searchBox },
  props: {
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5|6|7>,
      default: 3,
    },
  },
  setup(props) {
    const options = useHashMapDics<['FEE_TYPE', 'RENT_FEE_PAY_TYPE', 'RENT_TRADE_TYPE']>(['FEE_TYPE', 'RENT_FEE_PAY_TYPE', 'RENT_TRADE_TYPE']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['companyName', 'parkOrgId', 'billNo', 'times', 'type', 'contractType']>>({
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
      billNo: {
        tag: 'input',
        value: '',
        placeholder: '请输入账单编号',
      },
      times: {
        tag: 'date-range',
        value: [],
        placeholder: ['请选择支付开始时间', '请选择支付结束时间'],
        startTimeKey: 'payTimeStart',
        endTimeKey: 'payTimeEnd',

      },
      type: {
        tag: 'select',
        value: '',
        options: () => options.RENT_TRADE_TYPE,
        placeholder: '请选择订单类型',
      },
      contractType: {
        tag: 'input',
        // eslint-disable-next-line no-nested-ternary
        value: props.contractType === 6 ? 3 : props.contractType === 7 ? 5 : props.contractType,
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
    } = useSearchListPage<RentFeeTransactionDetail>({
      searchMethod: props.contractType === 6 || props.contractType === 7 ? rentFeeInstance.listMeterConsumptionPage : rentFeeInstance.rentFeeTransactionDetailListPage,
      searchConf,
      operateDialogCp: operateDialog,
      downloadMethod: props.contractType === 6 || props.contractType === 7 ? rentFeeInstance.exportConsumptionPage : rentFeeInstance.rentFeeTransactionDetailExport,
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

    const operate = async (type:Operatetypes, record?:RentFeeTransactionDetail) => {
      if (type === 'export') {
        await download();
        message.success('导出成功');
        return;
      }
      if (type === 'details' && record) {
        if (record.type === 2) { // 退款订单
          operateDialogConf.visible = true;
          operateDialogConf.data = record;
          return;
        }
        addCachePage('RentFeeTransactionDetail');
        const urls = {
          3: '/collect-fee/rent-feeMange/transaction-detail/details', // 租赁
          4: '/collect-fee/property/rent-feeMange/transaction-detail/details', // 物业
          5: '/collect-fee/apartment/rent-feeMange/transaction-detail/details', // 公寓
          6: '/collect-fee/water-ectricity-mange/transaction-detail/details', // 企业水电
          7: '/collect-fee/apartment-ectricity-mange/transaction-detail/details', // 公寓水电
        };
        router.push({
          path: urls[props.contractType],
          query: { id: record.id },
        });
      }
    };
    search(true);

    onActivated(() => {
      search(true);
    });
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      columns: getColumns(options, props.contractType),
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
