<template>
  <v-modal
    v-model:visible="currentVisible"
    title="领用详情"
    :width="780"
    :body-style="{ maxHeight: '600px', overflow: 'auto' }"
    class="operate-dialog"
    @cancel="emit('onClose')"
  >
    <div
      id="details"
      style="padding: 30px;"
      class="view-house-apply-details"
    >
      <div class="table-list">
        <div class="table-item">
          <span class="table-name">申请单号：</span>
          <span class="table-content">{{ data.applyNo }}</span>
        </div>
        <div class="table-item">
          <span class="table-name">领用人：</span>
          <span class="table-content">{{ data.employeeName }}</span>
        </div>
        <div class="table-item">
          <span class="table-name">所在园区：</span>
          <span class="table-content">{{ data.parkOrgName }}</span>
        </div>
        <div class="table-item">
          <span class="table-name">所在公司：</span>
          <span class="table-content">{{ data.companyName }}</span>
        </div>
        <div class="table-item">
          <span class="table-name">领用电话：</span>
          <span class="table-content">{{ data.mobile }}</span>
        </div>
        <div class="table-item">
          <span class="table-name">物品名称：</span>
          <span class="table-content">{{ data.goodsName }}</span>
        </div>
        <div class="table-item">
          <span class="table-name">领用数量：</span>
          <span class="table-content">{{ data.receiveNum }}</span>
        </div>
        <div class="table-item">
          <span class="table-name">是否归还：</span>
          <span class="table-content">{{ NeedReturnType[data.needReturn] || '' }}</span>
        </div>
        <div class="table-item">
          <span class="table-name">已归还数量：</span>
          <span class="table-content">{{ data.returnNumTotal }}</span>
        </div>
      </div>
      <div class="record-list">
        <a-timeline>
          <a-timeline-item
            v-for="(item) in data.receiveDetailList"
            :key="item.id"
            :color="item.auditState === 7 ? '#ccc' : '#2578ED'"
          >
            <div
              class="record active"
            >
              <span class="record-name">{{ AuditStateShowType[item.auditState] }}</span>
              <span class="record-value">{{ item.createTime }}</span>
            </div>
            <div
              v-if="item.auditUserName"
              class="record"
            >
              <span class="record-name">处理人</span>
              <span class="record-value">{{ item.auditUserName }}</span>
            </div>
            <div
              v-if="item.auditMsg"
              class="record"
            >
              <span class="record-name">审核意见</span>
              <span class="record-value">{{ item.auditMsg }}</span>
            </div>
            <div
              v-if="item.needReturn !== null && item.needReturn >= 0"
              class="record"
            >
              <span class="record-name">是否需归还</span>
              <span class="record-value">{{ item.needReturn === 0 ? '无需归还' : '需归还' }}</span>
            </div>
            <div
              v-if="item.stockNo"
              class="record"
            >
              <span class="record-name">{{ item.auditState === 2 ? '出库' : '入库' }}单号</span>
              <span class="record-value">{{ item.stockNo }}</span>
            </div>
            <div
              v-if="item.returnNumCurrent"
              class="record"
            >
              <span class="record-name">本次归还数量</span>
              <span class="record-value">{{ item.returnNumCurrent || 0 }}</span>
            </div>
            <div
              v-if="item.remark"
              class="record"
            >
              <span class="record-name">备注</span>
              <span class="record-value">{{ item.remark }}</span>
            </div>
          </a-timeline-item>
        </a-timeline>
      </div>
    </div>
    <template #footer>
      <div>
        <a-button @click="emit('onClose')">
          关闭
        </a-button>
        <a-button
          type="primary"
          @click="toPrint"
        >
          打印
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang="ts">
import {
  reactive,
  ref,
  onMounted,
  defineComponent,
  PropType,
  computed,
  watch,
  onActivated,
  nextTick,
} from 'vue';
import { NeedReturnType, ReceiveDetails } from '~models/asset-mangement';
import { AuditStateType, AuditStateShowType } from '../config';
import printHtml from '../print';

export default defineComponent({
  name: 'DetailsDialog',
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    data: {
      type: Object as any,
      default: () => ({}),
    },
    onClose: {
      type: Function,
      default: () => {},
    },
  },
  emits: ['onClose'],
  setup(props, { emit }) {
    const currentVisible = computed(() => props.visible);

    const toPrint = () => {
      console.log('打印');
      const details = document.querySelector('#details');
      console.log(details, '..details');
      printHtml(details?.innerHTML);
      // emit('onClose');
    };
    return {
      AuditStateShowType,
      NeedReturnType,
      currentVisible,
      AuditStateType,
      toPrint,
      emit,
    };
  },
});
</script>

<style lang="less" scoped>
.view-house-apply-details {
    .item {
        display: flex;
        justify-content: flex-start;
        margin-bottom: 30px;

        .label {
            width: 160px;
            margin-right: 40px;
            text-align: right;
        }
    }
}

.record-list {
    padding-top: 20px;
    padding-left: 120px;
    border-top: 1px solid #d8d8d8;

    .clock-svg {
        width: 30px;
        height: 30px;
        color: var(--ant-primary-color);
    }
}

.record {
    display: flex;
    padding-bottom: 5px;
    font-size: 14px;

    &.active > span {
        font-weight: bold;
    }

    .record-name {
        display: inline-block;
        width: 120px;
        padding-right: 20px;
    }

    .record-value {
        display: inline-block;
        flex: 1;
    }
}

.table-list {
    .table-item {
        margin-bottom: 12px;
        font-size: 14px;
        line-height: 32px;
    }

    .table-name {
        display: inline-block;
        width: 120px;
        color: rgba(0, 0, 0, 0.85);
        text-align: right;
    }

    .table-value {
        display: inline-block;
        color: rgba(0, 0, 0, 0.65);
    }
}
</style>
<!--  -->
