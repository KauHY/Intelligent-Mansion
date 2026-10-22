<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-05 18:22:26
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="920"
    class="operate-dialog"
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
          合同编号
        </div>
        <div class="content">
          {{ defaultData.contractNo }}
        </div>
      </div>
      <div class="item">
        <div class="label">
          入驻企业名称
        </div>
        <div class="content">
          {{ defaultData.companyName }}
        </div>
      </div>
    </div>
    <div
      v-if="[3,4,5].includes(otherData)"
      class="box-item"
    >
      <div class="item">
        <div class="label">
          房源名称
        </div>
        <div class="content">
          {{ defaultData.houseName }}
        </div>
      </div>
      <div
        class="item"
      >
        <div class="label">
          面积(㎡)
        </div>
        <div class="content">
          {{ defaultData }}
        </div>
      </div>
    </div>

    <div class="box-item">
      <div class="item">
        <div class="label required">
          所在账期
        </div>
        <div class="content">
          {{ defaultData.accountPeriod }}
        </div>
      </div>
      <div class="item">
        <div class="label">
          逾期金额（元）
        </div>
        <div class="content">
          {{ defaultData.dueAmount }}
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
          数据来源
        </div>
        <div class="content">
          {{ options.RENT_DATA_SOURCE? options.RENT_DATA_SOURCE[defaultData.dataSource]?.label:'-' }}
        </div>
      </div>
    </div>
    <div class="box-item">
      <div class="item">
        <div class="label">
          应收滞纳金（元）
        </div>
        <div class="content">
          {{ defaultData.receivableFine }}
        </div>
      </div>
      <div class="item">
        <div class="label">
          已收滞纳金（元）
        </div>
        <div class="content">
          {{ defaultData.overdueFine }}
        </div>
      </div>
    </div>
    <div class="box-item">
      <div class="item">
        <div class="label">
          待收滞纳金（元）
        </div>
        <div class="content">
          {{ defaultData.receiveFine }}
        </div>
      </div>
      <div class="item">
        <div class="label">
          免除滞纳金（元）
        </div>
        <div class="content">
          {{ defaultData.reduceFine }}
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
      :data-source="deductionRecord"
    />

    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          关闭
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect,
} from 'vue';
import { message } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { RentFee, RentLateFeeInfo } from '~models/collect-fee';
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
      type: Object as PropType<RentLateFeeInfo>,
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
    otherData: {
      type: Number,
      default: null,
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const isReady = ref();
    const {
      type, visible, defaultData, otherData,
    } = toRefs(props);
    const options = useHashMapDics<['RENT_DATA_SOURCE']>(['RENT_DATA_SOURCE']);

    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    const deductionRecord = computed(() => {
      const {
        id,
        deductionAmount,
        createByName,
        createTime,
      } = defaultData.value;
      return [{
        deductionAmount,
        createByName,
        createTime,
        id,
      }];
    });
    return {
      isReady,
      currentVisible,
      options,
      deductionRecordColumns,
      deductionRecord,

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
