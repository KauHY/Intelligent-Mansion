<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 10:44:43
-->
<template>
  <div class="menu-list">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <!-- <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp; -->
        <a-button
          type="primary"
          @click="operate('add', undefined,)"
        >
          新增
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
          <template #schemeName="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template #status="{record}">
            <!-- <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span> -->
            <a-switch
              checked-children="开"
              un-checked-children="关"
              :checked="record.status===1"
              @change="operate('statusChange',record, $event)"
            />
          </template>
          <template
            #operate="{record}"
          >
            <div v-if="record.status!==1">
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>
              <a-button
                type="link"
                @click="operate('del',record)"
              >
                删除
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
      :title="operateDialogConf.title"
      :billing-cycle-opts="options['FEE_BILLING_CYCLE']"
      :reserved-digits-opts="options['FEE_RESERVED_DIGITS']"
      :carry-mode-opts="options['FEE_CARRY_MODE']"
      :formula-type-opts="options['FEE_FORMULA_TYPE']"
      :calculation-standard-opts="options['FEE_CALCULATION_STANDARD']"
      :charge-type-opts="options['FEE_CHARGE_TYPE']"
      :park-list="parkList"
      @operate:success="operateSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, computed, watch, onActivated,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';

import CollectFee, { BillingScheme } from '~models/collect-fee';
import Park from '~models/park';

import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import operateDialog from './operate-dialog.vue';

import {
  statusNames,
  getColumns,
  operateDialogTitleName,
  // orgTypeNames,
} from '../config';

type Operatetypes = 'add'|'edit'|'del' | 'details' |'statusChange'//
const collectFeeInessInstance = new CollectFee();
export default defineComponent({
  name: 'MenuList',
  components: { searchBox },
  props: {
    selectTreeNode: {
      type: Object as PropType<{name:string, key:string}>,
      default: () => ({}),
    },
    refresh: {
      type: String as PropType<'refresh'|''>,
      default: '',
    },
  },
  emits: ['on-refresh'],
  setup(props, { emit }) {
    const { addCachePage } = useCachePage();
    // const router = useRouter();
    const { options, parkList } = useOpts();
    const columns = getColumns(options);
    const searchConf = reactive<SearchConf<['orgId']>>({
      orgId: {
        tag: 'input',
        hidden: true,
        value: '',
        // placeholder: '请选择园区',
        // options: () => parkList.value,
        // filterOption: true,
        // showSearch: true,
      },
    });

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(collectFeeInessInstance.billingSchemeListPage, searchConf, operateDialog);
    const operateSuccess = () => {
      search(true);
      // emit('on-refresh');
    };
    onActivated(() => { // 返回时刷新列表数据
      if (props.refresh)operateSuccess();
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record:BillingScheme|undefined, e?:boolean) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.schemeName}】吗？`,
            onOk: async () => {
              await collectFeeInessInstance.billingSchemeDel(record.id as string);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        }
        case 'statusChange': {
          const tempRecord = record as BillingScheme;
          const tempStatus = e ? 1 : 0;
          await collectFeeInessInstance.billingSchemeSetState({
            id: tempRecord.id as string,
            status: tempStatus,
          });
          tempRecord.status = tempStatus;
          message.success('操作成功');
          break;
        }
        // case 'import': {
        //   console.log('导入');
        //   break;
        // }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.data = record as BillingScheme;
          operateDialogConf.title = operateDialogTitleName[type];
        }
      }
      // if (type === 'accept') {
      //   if (!record) return;
      //   await patrolInspectionInessInstance.houseApplyForAccept(record.id as string);
      //   message.success('受理成功');
      //   operateSuccess();
      //   return;
      // }
      // operateDialogConf.visible = true;
      // operateDialogConf.title = '申请详情';
      // operateDialogConf.data = record as ViewHouseApplyInfo;
    };
    // watch(() => props.selectTreeNode.key, () => {
    //   if (!props.selectTreeNode.key) return;
    //   searchConf.orgId.value = props.selectTreeNode.key;
    //   search(true);
    // });
    search(true);

    return {
      columns,
      statusNames,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      operateSuccess,
      options,
      parkList,
    };
  },
});

// 获取下拉|字典数据
function useOpts() {
  const options = useHashMapDics([
    'FEE_TYPE',
    'FEE_BILLING_CYCLE', // 计费周期
    'FEE_CHARGING_CYCLE', // 收费周期
    'FEE_RESERVED_DIGITS', // 保留位数
    'FEE_CARRY_MODE', // 进位方式
    'FEE_FORMULA_TYPE', // 公式类型
    'FEE_CALCULATION_STANDARD',
    'FEE_CHARGE_TYPE',
  ]);
  const parkList = ref<SelectOptions>();
  const init = async () => {
    parkList.value = await new Park().searchParkListName();
  };
  init();
  return {
    options,
    parkList,
  };
}
</script>
