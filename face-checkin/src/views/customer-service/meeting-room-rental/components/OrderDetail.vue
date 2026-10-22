<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-03-18 15:30:12
 * @LastEditors: xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    class="order-detail"
    :visible="true"
    :width="700"
    :title="orderConfig.title"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div class="detail-con">
      <ul>
        <li>
          <span>所在园区：</span>
          <p>{{ orderData.parkOrgName }}</p>
        </li>
        <li>
          <span>会议室名称：</span>
          <p>{{ orderData.meetingName }}</p>
        </li>
        <li>
          <span class="remark-title">预定时间：</span>
          <p>{{ orderData.reserveTimeStr }}</p>
        </li>
        <li>
          <span>申请单位：</span>
          <p>{{ orderData.applicantUnit }}</p>
        </li>
        <li>
          <span>联系人：</span>
          <p>{{ orderData.applicantName }}</p>
        </li>
        <li>
          <span>手机号：</span>
          <p>{{ orderData.mobile }}</p>
        </li>
        <li>
          <span>预计人数：</span>
          <p>{{ orderData.meetingPeopleNum }}</p>
        </li>
        <li>
          <span>会议主题：</span>
          <p>{{ orderData.meetingSubject }}</p>
        </li>
        <li>
          <span class="remark-title">备注：</span>
          <p class="remark-centent">
            {{ orderData.remark }}
          </p>
        </li>
        <li class="goods">
          <span>使用物资：</span>
          <div class="rg">
            <div
              v-for="(item, index) in orderData.reserveGoodsList"
              :key="index"
              class="rg-item"
            >
              <v-image
                width="80px"
                height="80px"
                :keys="item.oldGoodsImage"
              />
              <div class="rg-money">
                <div>{{ item.oldGoodsName }}</div>
                <!-- {{ item.oldUnit }} 单位 -->
                <div>￥{{ item.oldUnitPrice }}<span class="empty" /> * <span class="empty" /> {{ item.number }}</div>
              </div>
            </div>
          </div>
        </li>
        <li v-if="type==='verify'">
          <span>提交订单金额：</span>
          <p class="money-color">
            {{ orderData.estimatedAmount }}元
          </p>
        </li>
        <li v-if="type==='verify'">
          <span>提交时间：</span>
          <p>{{ orderData.applicantTime }}</p>
        </li>
      </ul>
    </div>
    <div
      v-if="type==='detail'"
      class="record-list"
    >
      <a-row>
        <a-col :span="5" />
        <a-col :span="7">
          <a-timeline>
            <a-timeline-item color="#2578ED">
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
                  <p>{{ orderData.createTime }}</p>
                </div>
                <div class="record-item">
                  <p class="label">
                    提交订单金额
                  </p>
                  <p class="money-color">
                    {{ orderData.estimatedAmount }}元
                  </p>
                </div>
              </div>
            </a-timeline-item>
            <a-timeline-item color="#2578ED">
              <template #dot>
                <v-svg
                  name="clock"
                  class="clock-svg"
                />
              </template>
              <div class="content-box">
                <div class="record-item">
                  <p class="label">
                    {{ statusName }}
                  </p>
                  <p v-if="orderData.status !== 1">
                    {{ orderData.updateTime }}
                  </p>
                </div>
                <template v-if="[2, 3].includes(orderData.reviewStatus)">
                  <div class="record-item des">
                    <p class="label">
                      审核人
                    </p>
                    <p>{{ orderData.updateByName }}</p>
                  </div>
                  <div class="record-item des">
                    <p class="label">
                      审核意见
                    </p>
                    <p>{{ orderData.reviewMsg || "-" }}</p>
                  </div>
                  <div
                    v-if="orderData.reviewStatus===2"
                    class="record-item des"
                  >
                    <p class="label">
                      实际订单金额
                    </p>
                    <p class="money-color">
                      {{ orderData.reviewAmount }}元
                    </p>
                  </div>
                </template>
              </div>
            </a-timeline-item>
          </a-timeline>
        </a-col>
      </a-row>
    </div>
    <div v-show="type !== 'detail'">
      <a-form
        ref="formRef"
        :model="modelRef"
        :rules="rulesRef"
        :label-col="{ span: 5 }"
      >
        <a-form-item
          label="审核结果"
          name="reviewStatus"
        >
          <a-radio-group v-model:value="modelRef.reviewStatus">
            <a-radio :value="2">
              通过
            </a-radio>
            <a-radio :value="3">
              拒绝
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item
          v-if="modelRef.reviewStatus === 2"
          label="实际订单金额(元)"
          name="reviewAmount"
        >
          <a-input-number
            v-model:value="modelRef.reviewAmount"
            placeholder="请输入实际订单金额(元)"
            :min="0"
            :max="999999999.99"
            style="width: 100%;"
          />
        </a-form-item>
        <a-form-item
          label="审核意见"
          name="reviewMsg"
        >
          <a-textarea
            v-model:value="modelRef.reviewMsg"
            :maxlength="200"
            placeholder="请输入审核意见"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <a-button @click="emit('update:show', false)">
        {{ formDisabled ? "关闭" : "取消" }}
      </a-button>
      <a-button
        v-show="!formDisabled"
        type="primary"
        @click="confirm"
      >
        确定
      </a-button>
    </template>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, defineComponent, onMounted, toRefs, computed,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import MeetingRentalVo, { MeetingRentalListKind } from '~models/meeting-rental';
import { orderForm } from '../composition/useForm';

export default defineComponent({
  props: {
    orderConfig: {
      type: Object,
      default: () => ({
        title: '',
        data: {},
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const { parkOpts, options } = useOptions();
    const { modelRef, rulesRef } = orderForm();
    const { orderData, type } = toRefs(props.orderConfig);
    const MeetingRentalVoReq = new MeetingRentalVo();
    const formDisabled = type.value === 'detail';
    const statusName = computed(
      () => options.CUS_MEETING_AUDIT_STATUS[orderData.value.reviewStatus]?.label || '-',
    );
    console.log(orderData.value, options, statusName);
    const fill = () => {
      Object.entries(modelRef).forEach(([key]) => {
        if (
          (orderData.value as any)[key]
          || (orderData.value as any)[key] === 0
        ) {
          const formData = modelRef as any;
          formData[key] = orderData.value[key];
        }
      });
    };

    // 不用回调数据
    if (type.value !== 'add') {
      modelRef.reviewAmount = orderData.value.estimatedAmount;
      modelRef.id = orderData.value.id;
      // fill();
    }
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          await MeetingRentalVoReq.rentalVerifyApi({
            ...modelRef,
            reviewAmount:
              modelRef.reviewStatus === 2 ? modelRef.reviewAmount : '',
          });
          message.success('操作成功');
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {};
    init();
    return {
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      parkOpts,
      formDisabled,
      orderData,
      type,
      statusName,
    };
  },
});

function useOptions() {
  const parkOpts = ref<SelectOptions>([]);
  const options = useHashMapDics<['CUS_MEETING_AUDIT_STATUS']>([
    'CUS_MEETING_AUDIT_STATUS',
  ]);
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
  })();
  return {
    parkOpts,
    options,
  };
}
</script>
<style scoped lang="scss">
.order-detail {
    .remark-title {
        min-width: 135px;
    }

    .remark-centent {
        width: 400px;
    }

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
                min-width: 100px;
            }

            &.des {
                .label {
                    color: #ddd;
                }
            }
        }
    }

    .money-color {
        color: var(--ant-primary-color);
    }

    .detail-con {
        ul {
            li {
                display: flex;
                margin-bottom: 10px;

                span {
                    display: inline-block;
                    width: 20.83333333%;
                    color: rgba(0, 0, 0, 0.85);
                    text-align: right;
                }
            }

            .goods {
                .rg {
                    width: 80%;

                    .rg-item {
                        display: flex;
                        flex: 1;
                        align-items: center;
                        margin-bottom: 5px;

                        .rg-money {
                            display: flex;
                            flex-direction: column;
                            justify-content: space-around;
                            width: 80px;
                            height: 80px;
                            margin-left: 10px;

                            .empty {
                                width: 5px;
                            }
                        }
                    }
                }
            }
        }
    }
}
</style>
