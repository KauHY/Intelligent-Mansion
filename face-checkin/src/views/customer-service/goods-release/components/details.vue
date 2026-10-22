<!--
 * @Description:详情
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-12 16:33:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-28 15:56:18
-->
<template>
  <div class="details">
    <a-form
      :label-col="{span:2.5}"
    >
      <a-form-item
        label="申请人"
      >
        <span>{{ details.submitterName }}</span>
      </a-form-item>
      <a-form-item
        label="手机号"
      >
        <span>{{ details.submitterMobile }}</span>
      </a-form-item>
      <a-form-item
        label="所在园区"
        class="address-info"
      >
        <span>{{ details.parkOrgName }}</span>
      </a-form-item>
      <a-form-item
        label="所在公司"
        class="address-info"
      >
        <span>{{ details.company }}</span>
      </a-form-item>
      <a-form-item
        label="放行时间"
      >
        <span>{{ details.releaseDatetimeStart }}~{{ details.releaseDatetimeEnd }}</span>
      </a-form-item>
      <a-form-item
        label="放行物品"
      >
        <!-- {{ orderTypeOpts }}{{ details.orderTypeItem }} -->
        <span>共{{ goodsCount }}件</span>
      </a-form-item>
      <a-form-item
        label="车牌号码"
      >
        <span>{{ details.carNumber }}</span>
      </a-form-item>
      <a-form-item
        label="备注"
      >
        <!-- {{ orderTypeOpts }}{{ details.orderTypeItem }} -->
        <span>{{ details.checkOpinion }}</span>
      </a-form-item>
      <a-form-item>
        <a-row>
          <a-col :span="3" />
          <a-col :span="21">
            <div
              v-for="(item,index) in details.goodsList"
              :key="index"
              class="goods-item"
            >
              <v-image
                :keys="item.picture"
                :width="50"
                :height="50"
              />
              <span>{{ item.name }}<span>*{{ item.count }}</span></span>
            </div>
          </a-col>
        </a-row>
      </a-form-item>
    </a-form>
    <div class="record-list">
      <a-timeline>
        <a-timeline-item
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
            <div class="record-item">
              <p class="label">
                提交申请
              </p>
              <p>{{ details.createTime }}</p>
            </div>
          </div>
        </a-timeline-item>
        <a-timeline-item
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
            <div class="record-item">
              <p class="label">
                {{ statusName }}
              </p>
              <p v-if="details.status!==1">
                {{ details.updateTime }}
              </p>
            </div>
            <template v-if="[2,3].includes (details.status)">
              <div class="record-item des">
                <p class="label">
                  审核人
                </p>
                <p>{{ details.updateByName }}</p>
              </div>
              <div class="record-item des">
                <p class="label">
                  审核意见
                </p>
                <p>{{ details.checkOpinion||'-' }}</p>
              </div>
            </template>
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
import { GoodsReleaseInfo } from '~models/customer-service';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
// import { orderTypeRepair, orderTypeComplaint } from '../config';

export default defineComponent({
  name: '',
  components: {},
  props: {
    details: {
      type: Object as PropType<GoodsReleaseInfo>,
      default: () => ({}),
    },
  },
  setup(props) {
    // const customerServiceInstance = new CustomerService();
    const { details } = toRefs(props);
    const options = useHashMapDics<['GOODS_RELEASE_STATUS']>(['GOODS_RELEASE_STATUS']);

    const goodsCount = computed(() => {
      let sum = 0;
      details.value.goodsList?.forEach(({ count }) => {
        sum += count || 0;
      });
      return sum;
    });

    const statusName = computed(() => {
      if (details.value.status === 5) return '超时自动取消';
      return options.GOODS_RELEASE_STATUS[details.value.status]?.label || '-';
    });
    // const orderTypeOpts = ref<SelectOptions>([]);
    // const details = ref<Partial<details>>({});
    // const recordItemConf = {
    //   1: { // 待派单
    //     createTime: '创建',
    //     createByName: '发布人',
    //   },
    //   2: { // 派单
    //     createTime: '派单',
    //     createByName: '派单人',
    //     employeeName: '处理人',
    //     mobile: '处理手机号',
    //     askFinishTime: '要求完成时间',
    //   },
    //   3: { // 退单
    //     createTime: '退单',
    //     createByName: '退单人',
    //     returnOrderReason: '退单原因',
    //   },
    //   4: { // 结单
    //     createTime: '结单',
    //     createByName: '结单人',
    //     finishTime: '完成时间',
    //     remark: '备注',
    //   },
    //   5: { // 撤单
    //     createTime: '撤单',
    //     createByName: '撤单人',
    //     cancelReason: {
    //       label: '撤单原因',
    //       getValue: (cancelReason:number) => options.CANCELORDERREASON[cancelReason]?.label,
    //     },
    //   },
    // };
    // const orderTypeKey = `ORDERTYPE-${orderType.value}`
    // const init = async () => {
    //   details.value = await customerServiceInstance.orderFindById(defaultData.value.id);
    //   options.value = await useHashMapDic<['ORDERTYPE-1', 'ORDERTYPE-2', 'CANCELORDERREASON']>(['ORDERTYPE-1', 'ORDERTYPE-2', 'CANCELORDERREASON']);
    //   // const a = orderType.value as 1|2;
    //   orderTypeOpts.value = (options.value as any)[`ORDERTYPE-${orderType.value}`];
    // };
    // init();
    return {
      // details,
      // orderTypeOpts,
      // recordItemConf,
      options,
      goodsCount,
      statusName,
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
    padding-bottom: 30px;
    margin-left: 20px;

    .record-item {
        display: flex;
        align-items: center;
        justify-content: flex-start;

        .label {
            min-width: 90px;
        }

        &.des {
            .label {
                color: #ddd;
            }
        }
    }
}

.goods-item {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    margin-bottom: 15px;

    >span {
        margin-left: 10px;
    }
}

</style>
