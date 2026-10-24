<!--
 * @Description: 抵扣内容
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-04-27 13:40:40
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-12 15:11:49
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    title="选择抵扣费用"
    :width="920"
    class="operate-dialog"
    @ok="confirm"
  >
    <search-box
      :search-conf="searchConf"
      :show-header="false"
    >
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="selectDeductionContentColumns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="{scroll}"
          :row-selection="{ selectedRowKeys: selectKeys, onSelect: onSelectChange,onSelectAll,getCheckboxProps }"
          @change="pageChange"
        />
      </template>
    </search-box>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, Ref, watchEffect, reactive,
} from 'vue';
import { message } from 'ant-design-vue';
import { string } from 'mathjs';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
// import InformationStatistical, { FormManageInfo } from '~models/information-statistical';
import {
  RentFee, RentFeeInfo, RentFeeBillInfo, RentDepositFeeDeductionContentSelectData,
} from '~models/collect-fee';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useModifyDate } from '../composition/useForm';
import { selectDeductionContentColumns } from '../config';

const rentFeeInstance = new RentFee();
export default defineComponent({
  name: 'OperateDialog',
  components: { searchBox },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<RentFeeInfo>,
      default: () => ({}),
    },
    selectData: {
      type: Object as PropType<RentDepositFeeDeductionContentSelectData>,
      default: () => ({}),
    },
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5>,
      default: 3,
    },
  },
  emits: ['update:visible', 'update:selectData'],
  setup(props, { emit }) {
    const { visible, defaultData, selectData } = toRefs(props);
    // const options = useHashMapDics<['Week_Day', 'FORM_CYCLE']>(['Week_Day', 'FORM_CYCLE']);
    // const isReady = ref(false);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });
    const searchConf = reactive<SearchConf<['companyName', 'contractNo', 'status', 'contractType']>>({
      // parkOrgId: {
      //   tag: 'select',
      //   value: '',
      //   placeholder: '请选择园区',
      //   options: () => parkOpts.value,
      // },
      companyName: {
        tag: 'input',
        value: defaultData.value.companyName,
        hidden: true,
      },
      contractNo: {
        tag: 'input',
        value: defaultData.value.contractNo,
        hidden: true,

      },
      status: {
        tag: 'input',
        value: 0,
        hidden: true,
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
      // operateDialogConf,
    } = useSearchListPage(rentFeeInstance.rentFeeBillListPage, searchConf);

    const {
      selectKeys,
      onSelectChange,
      onSelectAll,
      getCheckboxProps,
      currentSelectData,
      sumDueAmount,
    } = useSelect(defaultData, selectData);
    const confirm = () => {
      if (sumDueAmount.value > defaultData.value.receivedAmount) {
        message.warning('抵扣金额大于已收金额，请检查所勾选的抵扣内容');
        return;
      }
      emit('update:selectData', currentSelectData);
      currentVisible.value = false;
      //   emit('operate:success');
      // validate().then(async () => {
      //   // await contractManageInstance.templateAdd(modelRef);
      //   message.success('操作成功');
      //   currentVisible.value = false;
      //   emit('operate:success');
      // }).catch((error:any) => {
      //   message.warning('操作失败');
      // });
    };
    search(true);
    return {
      currentVisible,
      confirm,
      // options,
      // details,
      selectDeductionContentColumns,
      // modelRef,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      selectKeys,
      onSelectChange,
      onSelectAll,
      getCheckboxProps,
      sumDueAmount,
    };
  },
});

function useSelect(defaultData:Ref<RentFeeInfo>, selectData: Ref<RentDepositFeeDeductionContentSelectData>) {
  const currentSelectData = reactive({ ...selectData.value });
  const selectKeys = computed(() => Object.keys(currentSelectData));
  const sumDueAmount = computed(() => Object.values(currentSelectData).reduce((sum, cur) => {
    sum += cur.deAmount || 0;
    return sum;
  }, 0));
  const onSelectChange = ({
    chargeName, dueAmount, endDate, startDate, id,
  }:RentFeeInfo, selected:Boolean) => {
    if (!selected) {
      delete currentSelectData[id];
      return;
    }
    currentSelectData[id] = {
      chargeName,
      deAmount: dueAmount,
      endDate,
      startDate,
      id,
    };
  };
  const onSelectAll = (selected:boolean, selectedRows:RentFeeInfo[], changeRows:unknown) => {
    if (selected) {
      selectedRows.forEach((changeRow) => {
        const {
          chargeName, dueAmount, endDate, startDate, id,
        } = changeRow;
        currentSelectData[id] = {
          chargeName, deAmount: dueAmount, endDate, startDate, id,
        };
      });
      return;
    }
    Object.keys(currentSelectData).forEach((id) => {
      delete currentSelectData[id];
    });
    console.log(defaultData.value, '----selectDataxxxxx');
    // selectKeys.value = selectedRows.map(({ id }) => id);
    // console.log(selectKeys, '---selectKeys');
  };
  const getCheckboxProps = (selectedRows:RentFeeInfo, a:unknown) => {
    console.log(selectedRows, a);
    return {
      // disabled: defaultData.value.receivableAmount < (sumDueAmount.value + selectedRows.dueAmount || 0), // 勾选的抵扣金额（待收金额）必须小于已收金额
      disabled: false,
      name: selectedRows.id,
    };
  };
  return {
    selectKeys,
    onSelectChange,
    onSelectAll,
    getCheckboxProps,
    currentSelectData,
    sumDueAmount,
  };
}

</script>

<style lang="less" scoped>
.box-item {
    display: flex;
    padding: 0 20px;
    margin-bottom: 16px;

    >.item {
        display: flex;
        flex: 1;
        align-items: center;
        justify-content: flex-start;

        >.label {
            width: 160px;
            min-width: 100px;
            max-width: 120px;
            font-weight: 600;
            color: rgba(0, 0, 0, 0.25);

            &::after {
                content: ':';
            }
        }

        .content {
            flex: 1;
            word-break: break-all;
        }
    }
}

.record {
    margin-top: 30px;
}
</style>
