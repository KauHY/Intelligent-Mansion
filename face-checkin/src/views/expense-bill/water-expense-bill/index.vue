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
          <template #billNo="{ record }">
            <span
              class="text-link"
              @click="operate('detail', record)"
            >{{ record.billNo }}</span>
          </template>
          <template
            #operate="{record}"
          >
            <div>
              <a-button
                type="link"
                @click="operate('export',record)"
              >
                上传凭证
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
import Park from '~models/park';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Fee from '~models/company';
import {
  columns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'detail'|'export'
const rentFeeInstance = new Fee();

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
    const informationFlag = ref<Boolean>(false);
    const informationConfig = ref<any>();
    const options = useHashMapDics<['METER_BILL_FEE_TYPE_COMPANY', ]>(['METER_BILL_FEE_TYPE_COMPANY']);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['accountPeriod', 'billNo', 'chargeName', 'type']>>({
      billNo: {
        tag: 'input',
        value: '',
        placeholder: '请输入账单编号',
      },

      accountPeriod: {
        tag: 'month-picker',
        value: '',
        placeholder: '请选择所在账期',
      },
      chargeName: {
        tag: 'input',
        value: '',
        placeholder: '请输入收费项',
      },
      type: {
        tag: 'select',
        options: () => options.METER_BILL_FEE_TYPE_COMPANY,
        value: '',
        placeholder: '请选择费用类型',
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
      searchMethod: rentFeeInstance.listMeterPortBillPage,
      searchConf,
      operateDialogCp: operateDialog,
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const { addCachePage } = useCachePage();
    const router = useRouter();
    // const { isShowImport, importExcelConfig, contractType } = useImport();
    const operate = async (type:Operatetypes, record?:any) => {
      operateDialogConf.type = type;
      let data = null;
      switch (type) {
        case 'detail':
        case 'export':
          data = await rentFeeInstance.findByIdConsumptionBill(record.id);
          operateDialogConf.visible = true;
          operateDialogConf.data = data;
          operateDialogConf.title = type === 'export' ? '上传付款凭证' : '详情';
          break;
        default:
          break;
      }
    };

    const refresh = () => {
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
      informationFlag,
      informationConfig,
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
      refresh,
    };
  },
});
</script>
