<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="920"
    class="operate-dialog"
    @ok="confirm"
  >
    <a-skeleton
      :paragraph="{ rows: 4 }"
      :loading="responsing"
    >
      <div class="form-item-title">
        基础信息
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            所属园区
          </div>
          <div class="content">
            {{ defaultData.parkOrgName }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            收费项
          </div>
          <div class="content">
            {{ defaultData.chargeName }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            入驻企业名称
          </div>
          <div class="content">
            {{ defaultData.companyName }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            房源名称
          </div>
          <div class="content">
            {{ defaultData.houseName?.join(';') }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            应收日期
          </div>
          <div class="content">
            {{ defaultData.dueDate }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            最晚缴纳日期
          </div>
          <div class="content">
            {{ defaultData.latestPayDate }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            本期结算时间
          </div>
          <div class="content">
            {{ defaultData.settledTime }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            结算月份
          </div>
          <div class="content">
            {{ defaultData.settledMonth }}
          </div>
        </div>
      </div>

      <div class="box-item">
        <div class="item">
          <div class="label">
            应收金额（元）
          </div>
          <div class="content">
            {{ defaultData.receivableAmount }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            数据来源
          </div>
          <div class="content">
            {{ sourceOpts[defaultData.dataSource]?.label }}
          </div>
        </div>
      </div>
      <div class="form-item-title margin">
        费用明细
      </div>
      <div class="table-section">
        <v-table
          row-key="id"
          bordered
          :pagination="false"
          :columns="getDetailColumns"
          :data-source="chargeDetailList"
        />
      </div>

      <template #footer>
        <div>
          <a-button @click="currentVisible=false ">
            关闭
          </a-button>
        </div>
      </template>
    </a-skeleton>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect,
} from 'vue';
import { message } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
// import InformationStatistical, { FormManageInfo } from '~models/information-statistical';
import { RentFee, RentFeeInfo } from '~models/collect-fee';
import { useModifyDate } from '../composition/useForm';
import { getDetailColumns } from '../config';

const rentFeeInstance = new RentFee();
export default defineComponent({
  name: 'OperateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object,
      default: () => ({}),
    },
    title: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: '',
    },
    parkOpts: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
    sourceOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const responsing = ref(true);
    const {
      type, visible, defaultData, sourceOpts,
    } = toRefs(props);
    // console.log(sourceOpts.value, 'sourceOpts');
    const options = useHashMapDics<['Week_Day', 'FORM_CYCLE']>(['Week_Day', 'FORM_CYCLE']);
    // const isReady = ref(false);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    const details = ref<any>();
    const chargeDetailList = ref<any>();
    const deductionRecordAllList = ref<any>();
    const init = async () => {
      const { deductionRecordList, meterConsumptionFeeDTO } = await rentFeeInstance.findByIdFeePage(defaultData.value.id);
      details.value = meterConsumptionFeeDTO;
      chargeDetailList.value = meterConsumptionFeeDTO.chargeDetail || [];
      deductionRecordAllList.value = deductionRecordList || [];
      responsing.value = false;
    };
    init();
    const confirm = () => {
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
    return {
      responsing,
      deductionRecordAllList,
      chargeDetailList,
      currentVisible,
      confirm,
      options,
      details,
      getDetailColumns,
    };
  },
});
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
