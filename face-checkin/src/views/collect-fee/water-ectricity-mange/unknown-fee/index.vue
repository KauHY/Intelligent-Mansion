<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-11 14:29:31
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

        <a-dropdown :disabled="!selectKeys.length">
          <template #overlay>
            <a-menu @click="operate($event.key)">
              <a-menu-item key="estimatedFee">
                批量转为已出账单
              </a-menu-item>
              <a-menu-item key="modifyDate">
                批量修改收款日期
              </a-menu-item>
            </a-menu>
          </template>
          <a-button
            type="primary"
            class="center"
          >
            批量操作&nbsp;&nbsp;
            <v-svg name="arrow-down" />
          </a-button>
        </a-dropdown>
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          :row-selection="{ selectedRowKeys: selectKeys, onSelect: onSelectChange,onSelectAll:onSelectAll,getCheckboxProps: getCheckboxProps }"
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
          <template #billingStatus="{text}">
            <span>{{ options.RENT_FEE_OUT_STATUS[text]?.label }}</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :ids="selectKeys"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :park-opts="parkOpts"
      @operate:success="refresh"
    />
    <DetailDialog
      v-if="customerFlag"
      v-model:visible="customerFlag"
      :default-data="customerConfig.data"
      :type="customerConfig.type"
      :title="customerConfig.title"
      :source-opts="options.RENT_DATA_SOURCE"
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

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { RentFee, RentFeeInfo } from '~models/collect-fee';

import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
import DetailDialog from './components/detail-dialong.vue';

type Operatetypes = 'estimatedFee'|'modifyDate'|'export' |'details'
const rentFeeInstance = new RentFee();

// const a:string = 1;
export default defineComponent({
  components: { searchBox, DetailDialog },
  props: {
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5|1>,
      default: 3,
    },
  },
  setup(props) {
    const options = useHashMapDics<['FEE_TYPE', 'RENT_DATA_SOURCE', 'RENT_FEE_OUT_STATUS']>(['FEE_TYPE', 'RENT_DATA_SOURCE', 'RENT_FEE_OUT_STATUS']);
    const parkOpts = ref<SelectOptions>([]);
    const customerFlag = ref();
    const customerConfig = ref();
    const searchConf = reactive<SearchConf<['companyName', 'parkOrgId', 'settledMonth', 'queryType', 'contractType']>>({
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
      queryType: {
        tag: 'input',
        hidden: true,
        value: 0,
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
    const {
      selectKeys, onSelectAll, onSelectChange,
    } = useQuantityOperate();
    // const { isShowImport, importExcelConfig, contractType } = useImport();
    const operate = async (type:Operatetypes, record?:RentFeeInfo) => {
      operateDialogConf.type = type;
      console.log(type, '---type');
      if (type === 'export') {
        await download();
        message.success('导出成功');
        return;
      }
      if (type === 'details') {
        customerConfig.value = {
          title: '详情',
          type,
          data: record,
        };
        customerFlag.value = true;
        return;
      }
      if (type === 'estimatedFee') { // 批量转为已出费用
        // if (!record) return;
        Modal.confirm({
          content: `是否将【${selectKeys.value.length}】条未出费用转为已出账单？`,
          onOk: async () => {
            // await contractManageInstance.templateDel(record.id || '');
            await rentFeeInstance.transformFee(selectKeys.value);
            selectKeys.value = [];
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      // if (type === 'download') { // 下载
      //   const fileUrl = record?.templateKey ? getPreviewUrl(record.templateKey) : '';
      //   if (fileUrl) window.open(fileUrl, 'blank');
      //   return;
      // }
      operateDialogConf.visible = true;
      operateDialogConf.title = '批量修改收款日期';
    };
    const getCheckboxProps = (record: any) => ({
      disabled: record.billingStatus === 3,
    });

    const refresh = () => {
      selectKeys.value = [];
      search(true);
    };

    refresh();
    onActivated(() => {
      refresh();
    });
    onMounted(async () => {
      parkOpts.value = await new Park().searchParkListName();
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      getCheckboxProps,
      customerFlag,
      customerConfig,
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
      selectKeys,
      onSelectAll,
      onSelectChange,
      refresh,
    };
  },
});
// 批量操作
function useQuantityOperate() {
  const selectKeys = ref<string[]>([]);
  const onSelectChange = (selectedRow:any, selected:Boolean) => {
    if (!selected) {
      const index = selectKeys.value.findIndex((id) => selectedRow.id === id);
      selectKeys.value.splice(index, 1);
    } else {
      selectKeys.value.push(selectedRow.id);
    }
  };
  const onSelectAll = (selected:boolean, selectedRows:RentFeeInfo[], changeRows:unknown) => {
    selectKeys.value = selectedRows.map(({ id }) => id);
  };
  return {
    selectKeys,
    onSelectChange,
    onSelectAll,
  };
}
</script>
