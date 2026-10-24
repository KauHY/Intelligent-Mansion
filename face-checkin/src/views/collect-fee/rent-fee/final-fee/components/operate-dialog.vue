<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-05 18:20:14
-->

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
            合同编号
          </div>
          <div class="content">
            {{ defaultData.contractNo }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            房源名称
          </div>
          <div class="content">
            {{ defaultData.houseName?.join(';') }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            面积(㎡)
          </div>
          <div class="content">
            {{ defaultData.totalArea }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            计费周期
          </div>
          <div class="content">
            {{ defaultData.startDate }}-{{ defaultData.endDate }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            应收日期
          </div>
          <div class="content">
            {{ defaultData.dueDate }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            最晚缴纳日期
          </div>
          <div class="content">
            {{ defaultData.latestPayDate }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            数据来源
          </div>
          <div class="content">
            {{ sourceOpts[defaultData.dataSource].label }}
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
            已收金额（元）
          </div>
          <div class="content">
            {{ defaultData.receivedAmount }}
          </div>
        </div>
      </div>
      <div class="box-item">
        <div class="item">
          <div class="label">
            待收金额（元）
          </div>
          <div class="content">
            {{ defaultData.dueAmount }}
          </div>
        </div>
        <div class="item">
          <div class="label">
            减免金额（元）
          </div>
          <div class="content">
            {{ defaultData.deductionAmount }}
          </div>
        </div>
      </div>
      <div class="form-item-title record">
        减免记录
      </div>

      <v-table
        row-key="id"
        :pagination="false"
        bordered
        :columns="deductionRecordColumns"
        :data-source="details?.deductionRecordList"
      />

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
import { deductionRecordColumns } from '../config';

const rentFeeInstance = new RentFee();
export default defineComponent({
  name: 'OperateDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<RentFeeInfo>,
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
    const { type, visible, defaultData } = toRefs(props);
    const responsing = ref(true);
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

    const details = ref();
    const init = async () => {
      details.value = await rentFeeInstance.rentFeeFindById(defaultData.value.id);
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
      currentVisible,
      confirm,
      options,
      details,
      deductionRecordColumns,
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
