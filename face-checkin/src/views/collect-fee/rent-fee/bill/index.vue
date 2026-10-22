<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-13 14:58:06
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="refresh"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('export')"
        >
          导出
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          :disabled="!selectKeys.length"
          @click="operate('batchReceive')"
        >
          批量收款
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="{...scroll,x:2400}"
          :row-selection="{ selectedRowKeys: selectKeys, onSelect: onSelectChange,onSelectAll,getCheckboxProps }"
          @change="pageChange"
        >
          <template #billNo="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template
            v-if="contractType===5"
            #employeeIdNames="{text}"
          >
            {{ text?.join(';')||'/' }}
          </template>
          <template #dataSource="{text}">
            <span>{{ options.RENT_DATA_SOURCE[text]?.label }}</span>
          </template>
          <template #status="{text}">
            <span>{{ options.RENT_BILL_STATUS[text]?.label }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>&nbsp;
              <a-button
                v-if="isShowDeduction(record)"
                type="link"
                @click="operate('deduction',record)"
              >
                减免
              </a-button>
              <a-button
                v-if="record.status===0"
                type="link"
                @click="operate('receive',record)"
              >
                收款
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
      :select-keys="selectKeys"
      :other-data="contractType"
      :contract-type="contractType"
      :bill-status-opts="options['RENT_BILL_STATUS']"
      :source-opts="options['RENT_DATA_SOURCE']"
      :pay-type-opts="options['RENT_FEE_PAY_TYPE']"
      @operate:success="refresh"
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
import { RentFee, RentFeeBillInfo } from '~models/collect-fee';

import {
  getColumns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'batchReceive' |'details'|'edit'|'deduction'|'receive'|'export'
const rentFeeInstance = new RentFee();

// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  props: {
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5|6|7>,
      default: 3,
    },
  },
  setup(props) {
    const options = useHashMapDics<['RENT_FEE_PAY_TYPE', 'RENT_DATA_SOURCE', 'RENT_BILL_STATUS']>(['RENT_FEE_PAY_TYPE', 'RENT_DATA_SOURCE', 'RENT_BILL_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['companyName', 'parkOrgId', 'billNo', 'dueDate', 'latestPayDate', 'status', 'contractType']>>({
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
      dueDate: {
        tag: 'date-range',
        value: [],
        startTimeKey: 'dueDateStart',
        endTimeKey: 'dueDateEnd',
        placeholder: ['应收日期开始时间', '应收日期结束时间'],
      },
      latestPayDate: {
        tag: 'date-range',
        value: [],
        startTimeKey: 'latestPayDateStart',
        endTimeKey: 'latestPayDateEnd',
        placeholder: ['最晚缴纳日期开始时间', '最晚缴纳日期结束时间'],
      },
      status: {
        tag: 'select',
        value: '',
        options: () => options.RENT_BILL_STATUS,
        placeholder: '请选收款状态',
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
    } = useSearchListPage<RentFeeBillInfo>({
      searchMethod: props.contractType === 6 || props.contractType === 7 ? rentFeeInstance.listMeterConsumptionBillPage : rentFeeInstance.rentFeeBillListPage,
      operateDialogCp: operateDialog,
      searchConf,
      downloadMethod: props.contractType === 6 || props.contractType === 7 ? rentFeeInstance.exportConsumptionBill : rentFeeInstance.rentFeeBillExport,
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const { addCachePage } = useCachePage();
    const router = useRouter();
    const {
      selectKeys, onSelectAll, onSelectChange, getCheckboxProps,
    } = useQuantityOperate();

    // 是否可以执行减免： 当状态为“未收”时，且当前日期小于等于“最晚缴纳日期”，支持“减免”操作
    const isShowDeduction = computed(() => ({ status, latestPayDate }:RentFeeBillInfo) => {
      if (status !== 0) return false;
      if (!latestPayDate) return true;
      return !(Date.now() > new Date(`${latestPayDate} 23:59:59`).getTime());
    });
    const operate = async (type:Operatetypes, record?:RentFeeBillInfo) => {
      if (type === 'export') {
        await download();
        message.success('导出成功');
        return;
      }
      operateDialogConf.type = type;
      if (!['batchReceive'].includes(type)) {
        operateDialogConf.data = record as RentFeeBillInfo;
      }
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
    };

    const refresh = () => {
      selectKeys.value = [];
      search(true);
    };
    refresh();
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
      console.log(props.contractType, 'props.contractType');
    });
    return {
      columns: getColumns(props.contractType),
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      parkOpts,
      options,
      selectKeys,
      onSelectAll,
      onSelectChange,
      getCheckboxProps,
      isShowDeduction,
      refresh,
    };
  },
});
// 批量操作
function useQuantityOperate() {
  const selectKeys = ref<string[]>([]);
  const onSelectChange = (selectedRow:RentFeeBillInfo, selected:Boolean) => {
    if (!selected) {
      const index = selectKeys.value.findIndex((id) => selectedRow.id === id);
      selectKeys.value.splice(index, 1);
      return;
    }
    selectKeys.value.push(selectedRow.id);
    console.log(selectedRow, selected, '----xxxx');
  };
  const onSelectAll = (selected:boolean, selectedRows:RentFeeBillInfo[], changeRows:unknown) => {
    console.log(selectedRows, changeRows, selected);
    selectKeys.value = selectedRows.map(({ id }) => id);
    // console.log(selectKeys, '---selectKeys');
  };
  const getCheckboxProps = (selectedRows:RentFeeBillInfo) => ({
    disabled: !!selectedRows.status,
    name: selectedRows.billNo,
  });
  return {
    selectKeys,
    onSelectChange,
    onSelectAll,
    getCheckboxProps,
  };
}
</script>
