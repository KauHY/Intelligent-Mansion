<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-11 14:39:55
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
          @click="operate('batchPrevent')"
        >
          批量免除滞纳金
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
          :row-selection="{ selectedRowKeys: selectKeys, onSelect: onSelectChange,onSelectAll:onSelectAll,getCheckboxProps }"
          @change="pageChange"
        >
          <template #billNo="{text,record}">
            <span
              :class="contractType===6 || contractType===7 ?'table-text-link':''"
              @click="operate('details',record,contractType)"
            >{{ text }}</span>
          </template>

          <template #status="{record}">
            <span>{{ options.RENT_LATE_STATUS?options.RENT_LATE_STATUS[record.status]?.label:'-' }}</span>
          </template>
          <template #operate="{record}">
            <div v-if="record.status===1">
              <a-button
                type="link"
                @click="operate('prevent',record)"
              >
                免除
              </a-button>
            </div>
            <span v-else>-</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :other-data="contractType"
      :title="operateDialogConf.title"
      :park-opts="parkOpts"
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
import { RentFee, RentLateFeeInfo } from '~models/collect-fee';

import {
  getColumns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'export'|'batchPrevent'|'details'|'prevent'
const rentFeeInstance = new RentFee();

// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  props: {
    contractType: {
      type: Number as PropType<3|4|5|6|7>,
      default: 3,
    },
  },
  setup(props) {
    const options = useHashMapDics<['FEE_TYPE', 'RENT_LATE_STATUS']>(['FEE_TYPE', 'RENT_LATE_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['companyName', 'parkOrgId', 'status', 'contractType']>>({
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
      status: {
        tag: 'select',
        value: '',
        options: () => options.RENT_LATE_STATUS,
        placeholder: '请选择费用状态',
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
    } = useSearchListPage({
      searchMethod: props.contractType === 6 || props.contractType === 7 ? rentFeeInstance.listMeterLateFeePage : rentFeeInstance.rentLateFeeListPage,
      searchConf,
      operateDialogCp: operateDialog,
      downloadMethod: props.contractType === 6 || props.contractType === 7 ? rentFeeInstance.exportMeterLateFee : rentFeeInstance.rentLateFeeExport,
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
    // const { isShowImport, importExcelConfig, contractType } = useImport();
    const operate = async (type:Operatetypes, record?:RentLateFeeInfo, contractType?:any) => {
      if (type === 'export') {
        await download();
        message.success('导出成功');
        return;
      }
      if (['batchPrevent', 'prevent'].includes(type)) { // 批量免除滞纳金
        // if (!record) return;
        const ids = type === 'batchPrevent' ? selectKeys.value : [record?.id as string];
        const apiReq = props.contractType === 6 || props.contractType === 7 ? rentFeeInstance.preventLateFee : rentFeeInstance.rentLateFeePrevent;
        Modal.confirm({
          content: '确认是否免除滞纳金？滞纳⾦免除后，到客户缴费时，将不再产⽣滞纳⾦',
          onOk: async () => {
            await apiReq(ids);
            message.success('操作成功');
            refresh();
          },
        });
        return;
      }
      if ([6, 7].includes(contractType)) {
        operateDialogConf.data = record as RentLateFeeInfo;
        operateDialogConf.visible = true;
        operateDialogConf.title = '滞纳金详情';
      }
    };

    const refresh = () => {
      selectKeys.value = [];
      search(true);
    };

    refresh();
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
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
      selectKeys,
      onSelectAll,
      onSelectChange,
      getCheckboxProps,
      refresh,
    };
  },
});
// 批量操作
function useQuantityOperate() {
  const selectKeys = ref<string[]>([]);
  const onSelectChange = (selectedRow:RentLateFeeInfo, selected:Boolean) => {
    if (!selected) {
      const index = selectKeys.value.findIndex((id) => selectedRow.id === id);
      selectKeys.value.splice(index, 1);
      return;
    }
    selectKeys.value.push(selectedRow.id);
    console.log(selectedRow, selected, '----xxxx');
  };
  const onSelectAll = (selected:boolean, selectedRows:RentLateFeeInfo[], changeRows:unknown) => {
    console.log(selectedRows, changeRows, selected);
    selectKeys.value = selectedRows.map(({ id }) => id);
    // console.log(selectKeys, '---selectKeys');
  };
  const getCheckboxProps = (selectedRows:RentLateFeeInfo) => ({
    disabled: selectedRows.status !== 1,
    name: selectedRows.id,
  });
  return {
    selectKeys,
    onSelectChange,
    onSelectAll,
    getCheckboxProps,
  };
}
</script>
