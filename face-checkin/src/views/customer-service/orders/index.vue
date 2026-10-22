<!--
 * @Description:访客预约列表
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 15:43:47
-->
<template>
  <div
    class="orders content-section"
  >
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
          <template #orderContent="{record}">
            <span
              class="order-content-link"
              @click="operate('details',record)"
            >{{ record.orderContent }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <a-button
                v-if="record.orderState===1"
                type="link"
                @click="operate('sendOrder',record)"
              >
                派单
              </a-button>
              <a-button
                v-if="[1,2,10].includes(record.orderState)"
                type="link"
                @click="operate('cancel',record)"
              >
                撤单
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
      :park-opts="parkOpts"
      :order-type="orderType"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, toRefs,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import CustomerService, { OrderInfo } from '~models/customer-service';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';
import {
  columns,
  operateDialogTitleName,
  orderTypeComplaint,
  orderTypeRepair,
  orderStateOpts,
} from './config';
import operateDialog from './components/operate-dialog.vue';
import useOpts from './composition/useOpts';

type Operatetypes = 'add'|'sendOrder'|'cancel'|'details'
const customerServiceInstance = new CustomerService();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  props: {
    orderType: {
      type: Number as PropType<1|2>,
      default: 1,
    },
  },
  setup(props) {
    // const a:string = 1;
    // const { parkOpts, getParkOpts } = useParkInfoOpts();
    const { orderType } = toRefs(props);

    const orderTypeItemOpt = orderType.value === 1 ? orderTypeComplaint : orderTypeRepair;
    const {
      parkOpts,
    } = useOpts('');
    const repairStateOpts = Object.values(orderStateOpts);
    if (orderType.value === 1) repairStateOpts.pop();
    console.log(repairStateOpts, 'repairStateOpts');
    const searchConf = reactive<SearchConf<['keyword', 'orderTypeItem', 'parkOrgId', 'dateTimes', 'orderState', 'orderType']>>({
      keyword: {
        tag: 'input',
        value: '',
        placeholder: '请输入内容/反馈人/手机号',
      },
      orderTypeItem: {
        tag: 'select',
        value: '',
        placeholder: '请选择类型',
        options: Object.values(orderTypeItemOpt),
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: parkOpts,
      },
      dateTimes: {
        tag: 'date-range',
        value: [],
        startTimeKey: 'createStartTime',
        endTimeKey: 'createEndTime',
        placeholder: ['创建开始时间', '创建结束时间'],
      },
      orderState: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: repairStateOpts,
      },
      orderType: {
        tag: 'input',
        hidden: true,
        value: orderType.value,
        placeholder: '请输入姓名',
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<OrderInfo>(customerServiceInstance.orderSearchListPage, searchConf, operateDialog);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:OrderInfo) => {
      operateDialogConf.type = type;
      operateDialogConf.visible = true;
      operateDialogConf.title = operateDialogTitleName[type];
      if (type === 'add') {
        operateDialogConf.data = {};
        return;
      }
      operateDialogConf.data = record as OrderInfo;
      //   break;
      // }
      // }
    };
    search(true);
    // getParkOpts();
    // companyMangementInstance.getUserMenuList();
    onMounted(() => {
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
    };
  },
});
</script>
<style lang="scss">
.order-content-link {
    display: inline-block;
    width: 100%;
    overflow: hidden;
    color: var(--ant-primary-color);
    text-overflow: ellipsis;

    &:hover {
        text-decoration: underline;
        cursor: pointer;
    }
}
</style>
