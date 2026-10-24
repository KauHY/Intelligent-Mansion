<!--
 * @Description:详情
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-12 16:33:11
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-28 16:51:39
-->
<template>
  <div class="details">
    <a-form
      :label-col="{span:2.5}"
    >
      <a-form-item
        label="反馈人"
      >
        <span>{{ orderDetails.orderOwnerName }}</span>
      </a-form-item>
      <a-form-item
        label="手机号"
      >
        <span>{{ orderDetails.orderOwnerMobile }}</span>
      </a-form-item>
      <a-form-item
        label="地址"
        class="address-info"
      >
        <span>{{ orderDetails.parkOrgName }}{{ orderDetails.orderAddress }}</span>
      </a-form-item>

      <a-form-item
        label="类型"
      >
        <!-- {{ orderTypeOpts }}{{ orderDetails.orderTypeItem }} -->
        <span>{{ orderTypeOpts[orderDetails.orderTypeItem]?.label }}</span>
      </a-form-item>
      <a-form-item
        label="照片"
      >
        <!-- <span>{{ orderDetails.orderUrl }}</span> -->
        <v-upload
          v-model:value="orderDetails.orderUrl"
          accept=".jpg,.bmp,.png,.gif"
          disabled
        />
      </a-form-item>
      <a-form-item
        :label="`${orderType===1?'反馈':'报修'}内容`"
      >
        <!-- <v-editor
          v-model:value="orderDetails.orderContent"
          disabled
          output-format="text"
        /> -->
        {{ orderDetails.orderContent }}
      </a-form-item>
    </a-form>
    <div class="record-list">
      <a-timeline>
        <a-timeline-item
          v-for="record in orderDetails.recordVOList"
          :key="record.id"
          color="#2578ED"
        >
          <template #dot>
            <v-svg
              name="clock"
              class="clock-svg"
            />
          </template>
          <div class="content-box">
            <!-- <div class="create" > -->
            <div
              class="record-item"
            >
              <div
                v-for="(label, fieldKey) in recordItemConf[record.state]"
                :key="fieldKey"
                class="field"
              >
                <template v-if="typeof label === 'string'">
                  <p class="label">
                    {{ label }}
                  </p>
                  <p>{{ record[fieldKey] }}</p>
                </template>
                <template v-else>
                  <p class="label">
                    {{ label.label }}
                  </p>
                  <p>{{ label.getValue(record[fieldKey]) }}</p>
                </template>
              </div>
            </div>
          </div>
        </a-timeline-item>
      </a-timeline>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, computed,
} from 'vue';
import CustomerService, { OrderDetails } from '~models/customer-service';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { orderTypeRepair, orderTypeComplaint } from '../config';

export default defineComponent({
  name: '',
  components: {},
  props: {
    orderType: {
      type: Number as PropType<1|2>,
      default: 1,
    },
    defaultData: {
      type: Object,
      default: () => ({}),
    },
  },
  setup(props) {
    const customerServiceInstance = new CustomerService();
    const { defaultData, orderType } = toRefs(props);
    const options = ref<DictionaryMap<['ORDERTYPE-1', 'ORDERTYPE-2', 'CANCELORDERREASON']>>({
      'ORDERTYPE-1': [],
      'ORDERTYPE-2': [],
      CANCELORDERREASON: [],
    });
    const orderTypeOpts = ref<SelectOptions>([]);
    const orderDetails = ref<Partial<OrderDetails>>({});
    const recordItemConf = {
      1: { // 待派单
        createTime: '创建',
        createByName: '发布人',
      },
      2: { // 派单
        createTime: '派单',
        createByName: '派单人',
        employeeName: '处理人',
        mobile: '处理手机号',
        askFinishTime: '要求完成时间',
      },
      3: { // 退单
        createTime: '退单',
        createByName: '退单人',
        returnOrderReason: '退单原因',
      },
      4: { // 结单
        createTime: '结单',
        createByName: '结单人',
        finishTime: '完成时间',
        remark: '备注',
      },
      5: { // 撤单
        createTime: '撤单',
        createByName: '撤单人',
        cancelReason: {
          label: '撤单原因',
          getValue: (cancelReason:number) => options.value.CANCELORDERREASON[cancelReason]?.label,
        },
      },
      6: {
        createTime: '转办',
        employeeName: '转办员',
        mobile: '手机号',
        remark: '备注',
      },
      7: {
        createTime: '转办处理',
        result: '处理结果',
      },
    };
    // const orderTypeKey = `ORDERTYPE-${orderType.value}`
    const init = async () => {
      orderDetails.value = await customerServiceInstance.orderFindById(defaultData.value.id);
      options.value = await useHashMapDic<['ORDERTYPE-1', 'ORDERTYPE-2', 'CANCELORDERREASON']>(['ORDERTYPE-1', 'ORDERTYPE-2', 'CANCELORDERREASON']);
      // const a = orderType.value as 1|2;
      orderTypeOpts.value = (options.value as any)[`ORDERTYPE-${orderType.value}`];
    };
    init();
    return {
      orderDetails,
      orderTypeOpts,
      recordItemConf,
      // options,
    };
  },
});
</script>
<style lang="scss" scoped>
.record-list {
    padding-top: 20px;
    border-top: 1px solid #d8d8d8;

    .clock-svg {
        width: 30px;
        height: 30px;
        color: var(--ant-primary-color);
    }
}

.content-box {
    min-width: 300px;
    max-width: 300px;
    margin-left: 20px;

    .record-item {
        .field {
            display: flex;
            align-items: center;
            justify-content: space-between;

            &:nth-child(n+2) {
                .label {
                    color: #ccc;
                }
            }

            p {
                line-height: 32px;

                &:first-child {
                    flex: 1;
                }

                &:last-child {
                    flex: 2;
                }
            }
        }
    }
}
</style>
