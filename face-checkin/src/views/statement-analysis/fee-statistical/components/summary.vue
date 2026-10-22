<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-15 18:29:52
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-17 16:26:59
-->
<template>
  <div class="summary-box">
    <div class="title-box">
      <div class="title">
        收费统计
      </div>
      <a-switch
        v-model:checked="checked"
        checked-children="本月"
        un-checked-children="累计"
      />
    </div>
    <div class="summary">
      <div class="item">
        <div class="left">
          <img src="~assets/images/statement-analysis/fee-statistical_header_icon1.png">
          <div class="text-box">
            <span>{{ topStatistic.billReceived }}</span><br>
            账单收入（元）
          </div>
        </div>
      </div>
      <div class="item">
        <div class="left">
          <img src="~assets/images/statement-analysis/fee-statistical_header_icon2.png">
          <div class="text-box">
            <span>{{ (topStatistic.payRate*100).toFixed(2) }}%</span><br>
            收缴率
          </div>
        </div>
      </div>
      <div class="item">
        <div class="left">
          <img src="~assets/images/statement-analysis/fee-statistical_header_icon3.png">
          <div class="text-box">
            <span>{{ topStatistic.receivedAmount }}</span><br>
            已收金额（元）
          </div>
        </div>
      </div>
      <div class="item">
        <div class="left">
          <img src="~assets/images/statement-analysis/fee-statistical_header_icon4.png">
          <div class="text-box">
            <span>{{ topStatistic.dueAmount }}</span><br>
            待收金额（元）
          </div>
        </div>
      </div>
      <div class="item">
        <div class="left">
          <img src="~assets/images/statement-analysis/fee-statistical_header_icon5.png">
          <div class="text-box">
            <span>{{ topStatistic.overDateAmount }}</span><br>
            逾期金额（元）
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { toFixed } from 'ant-design-vue/lib/input-number/src/utils/MiniDecimal';
import { number } from 'mathjs';
import {
  reactive, ref, defineComponent, PropType, computed, toRefs,
} from 'vue';
import { TopStatistic } from '~models/statement-analysis';

export default defineComponent({
  props: {
    value: {
      type: Number,
      default: 1,
    },
    topStatistic: {
      type: Object as PropType<TopStatistic>,
      default: () => ({}),
    },
  },
  emits: {
    'update:value': (value:number) => true,
  },
  setup(props, { emit }) {
    const { value } = toRefs(props);
    const checked = computed({
      set(value:boolean) {
        emit('update:value', value ? 1 : 2);
      },
      get() {
        return value.value === 1;
      },
    });
    return {
      checked,
    };
  },
});

</script>
<style lang='scss' scoped>
.summary-box {
    padding: h(14) 14px 0;
    border: 1px solid #e6e4e3;
    border-radius: 6px;

    .title-box {
        display: flex;
        align-items: center;
        justify-content: space-between;

        .title {
            font-size: 14px;
            font-weight: 900;
        }
    }

    .summary {
        display: flex;
        align-items: center;

        img {
            width: 50px;
            height: 50px;
        }

        >.item,
        .left {
            display: flex;
            align-items: center;
        }

        >.item {
            flex: 1;
            justify-content: space-between;
            padding: 16px;
            line-height: 28px;

            &::after {
                display: block;
                width: 1px;
                height: 30px;
                content: "";
                background-color: #000;
                opacity: 0.1;
            }

            &:last-child {
                &::after {
                    display: none;
                }
            }

            .left {
                .text-box {
                    position: relative;
                    margin-left: 14px;
                    color: #7d8292;

                    >span {
                        font-size: 16px;
                        font-weight: 500;
                        color: #f65321;
                    }
                }
            }

            .right {
                color: #7d8292;

                span {
                    font-size: 16px;
                    font-weight: 500;
                    color: #f65321;
                }
            }
        }
    }
}
</style>
