<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-06-06 18:35:15
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-09 14:56:48
-->
<template>
  <div class="apartment-manage">
    <slot name="title" />
    <div class="summary">
      <div>
        {{ apartmentInfo?.totalHouseCount }}<br>
        <span>房间总数</span>
      </div>
      <div>
        {{ apartmentInfo?.rentedHouseCount }}<br>
        <span>已租数</span>
      </div>
      <div>
        {{ apartmentInfo?.rentingHouseCount }}<br>
        <span>待租数</span>
      </div>
    </div>
    <div class="bottom-section">
      <div class="left">
        <div class="total">
          {{ apartmentInfo?.amountData.METER.totalAmount }}&nbsp;<span class="utils">万</span> <br>
          <span>累计</span>
        </div>
        <div>
          {{ apartmentInfo?.amountData.METER.nearOneMonthAmount }}&nbsp;<span class="utils">万</span><br>
          <span>近一月</span>
        </div>
      </div>
      <div class="right">
        <div class="total">
          {{ apartmentInfo?.amountData.RENT.totalAmount }}&nbsp;<span class="utils">万</span><br>
          <span>累计</span>
        </div>
        <div>
          {{ apartmentInfo?.amountData.RENT.nearOneMonthAmount }}&nbsp;<span class="utils">万</span><br>
          <span>近一月</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';

import CommandBigScreen, { ApartmentInfo } from '~models/command-big-screen';

export default defineComponent({
  props: {
  },
  setup() {
    const commandBigScreenInstance = new CommandBigScreen();
    commandBigScreenInstance.initPark();

    const apartmentInfo = ref<ApartmentInfo>();
    const init = async () => {
      apartmentInfo.value = await commandBigScreenInstance.getApartmentDataForScreen();
      // setSeries(houseRentInfo.value);
    };

    init();
    return {
      apartmentInfo,
    };
  },
});
</script>
<style lang='scss' scoped>
.apartment-manage {
    height: h(282);
    overflow: hidden;
    background: url('~assets/images/operation-screen/apartment-manage_bg.png') no-repeat;
    background-size: 100% 100%;

    .summary {
        display: flex;
        align-items: center;
        padding-top: h(10);
        padding-left: 45px;
        margin-bottom: h(10);
        font-size: 20px;
        line-height: 24px;

        >div {
            position: relative;
            padding-right: 50px;
            margin-left: 50px;

            &::after {
                position: absolute;
                top: 50%;
                right: 0;
                display: block;
                width: 1px;
                height: h(30);
                content: '';
                background: #fff;
                opacity: 0.2;
                transform: translateY(-45%);
            }

            &:last-child::after {
                display: none;
            }

            &:first-child {
                margin: 0;
            }

            span {
                font-size: 16px;
                color: #6c6e8e;
            }
        }
    }

    .bottom-section {
        display: flex;
        align-items: center;
        height: h(170);
        padding: 0 20px;

        >.left,
        .right {
            position: relative;
            display: flex;
            flex: 1;
            height: 100%;
            background: url('~assets/images/operation-screen/apartment-manage_bottom-section_bg.gif') no-repeat;
            background-size: 100% 100%;

            &::before {
                position: absolute;
                bottom: h(35);
                height: 16px;
                content: '';
            }

            >div {
                position: absolute;
                top: 35%;
                width: 50%;
                line-height: h(24);
                text-align: center;
                transform: translateY(-50%);

                &.total {
                    left: 0;
                    padding-left: 10px;

                    .utils {
                        color: #57a4fd;
                    }
                }

                &:last-child {
                    right: 0;

                    >.utils {
                        color: #57e8fd;
                    }
                }

                >span {
                    font-size: 14px;
                    color: #6c6e8e;
                }
            }
        }

        >.left {
            &::before {
                left: 50%;
                width: 92px;
                background: url('~assets/images/operation-screen/apartment-manage_bottom-section_left_title.svg') no-repeat;
                background-size: 100% 100%;
                transform: translateX(-50%);
            }
        }

        >.right {
            &::before {
                right: 50%;
                width: 73px;
                background: url('~assets/images/operation-screen/apartment-manage_bottom-section_right_title.svg') no-repeat;
                background-size: 100% 100%;
                transform: translateX(50%);
            }
        }
    }
}
</style>
