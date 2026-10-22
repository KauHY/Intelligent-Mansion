<!--
 * @Description:园区概览
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-31 16:08:49
 * @LastEditors: guochao82 guochao82@crc.com.hk
 * @LastEditTime: 2022-11-29 10:07:28
-->
<template>
  <div class="park-profile ">
    <slot name="title" />
    <div class="content-box">
      <div class="environment-info">
        <div class="left">
          <img src="~assets/images/operation-screen/park-profile_environment-info_icon1.png">
          <span>PM2.5</span>{{ weatherInfo.pm2p5||'-' }}
          <span class="pm10">PM10</span>{{ weatherInfo.pm10||'-' }}
        </div>
        <div class="right">
          <img src="~assets/images/operation-screen/park-profile_environment-info_icon2.png">
          <span>降水量</span>
          {{ weatherInfo.precip||'-' }}mm<span>/24H</span>
        </div>
      </div>
      <div class="process-box">
        <div
          v-for="(item,key) in processConf"
          :key="key"
          class="item"
        >
          <div class="info">
            <div class="title">
              {{ parkProfile? parkProfile.parkInfo[key]?.parkingName:'-' }}
            </div>
            <div class="right">
              <span>{{ item.driveIntoNum }}/{{ item.total }}</span>
              <span class="percent">{{ getPercent(item.driveIntoNum,item.total) }}%</span>
            </div>
          </div>
          <process-bar
            :background="item.bgColor"
            :percent="Number(getPercent(item.driveIntoNum,item.total))"
          />
        </div>
      </div>
      <div class="bottom">
        <div class="label">
          <img src="~assets/images/operation-screen/park-profile_bottom_icon.png">
          场地预定：
        </div>
        <div class="value">
          <span>{{ parkProfile?.reserveCount||0 }}/{{ parkProfile?.meetingCount||0 }}</span>
          {{ getPercent(parkProfile?.reserveCount||0,parkProfile?.meetingCount||0) }}%
        </div>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, computed,
} from 'vue';
import { divide } from 'mathjs';
import CommandBigScreen, { WeatherInfo, ParkProfile } from '~models/command-big-screen';
import { getPercent } from '~utils/tool';
import processBar from './process-bar.vue';

export default defineComponent({
  components: { processBar },
  props: {
    weatherInfo: {
      type: Object as PropType<WeatherInfo>,
      default: () => ({}),
    },
  },
  setup() {
    const commandBigScreenInstance = new CommandBigScreen();
    commandBigScreenInstance.initPark();
    const processConf = reactive({
      0: {
        title: 'A区停车场',
        total: 0,
        driveIntoNum: 0,

        bgColor: 'linear-gradient(101deg, #00E3D2 0%, #3F77E9 100%)',
      },
      1: {
        title: 'B区地下停车场',
        total: 0,
        driveIntoNum: 0,

        bgColor: 'linear-gradient(270deg, #D74177 0%, #FFE98A 100%)',
      },
      2: {
        title: 'B区地面停车场',
        total: 0,
        driveIntoNum: 0,

        bgColor: 'linear-gradient(101deg, #00E3D2 0%, #3F77E9 100%)',
      },
    });

    // const getPercent = computed(() => (driveIntoNum:number, total:number) => {
    //   debugger;
    //   // console.log(divide(driveIntoNum, total));
    //   if (!total) return 0;
    //   return divide(driveIntoNum, total).toFixed(2);
    // });

    const parkProfile = ref<ParkProfile>();
    const init = async () => {
      parkProfile.value = await commandBigScreenInstance.getParkProfile();
      Object.entries(processConf).forEach(([key, item]) => {
        if (!parkProfile.value?.parkInfo.length) return;
        const { stopCount, parkCount } = parkProfile.value.parkInfo[key] || {};
        item.total = parkCount || 0;
        item.driveIntoNum = stopCount || 0;
      });
    };
    init();
    return {
      processConf,
      getPercent,
      parkProfile,
    };
  },
});
</script>
<style lang="scss" scoped>
.park-profile {
    position: relative;
    height: h(312);
    margin-bottom: h(20);
    overflow: hidden;
    background: url('~assets/images/operation-screen/park-profile_bg.png') no-repeat;
    background-size: 100% 100%;

    .content-box {
        padding: 0 16px;
    }

    .environment-info {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        height: h(40);
        margin-bottom: h(13);
        font-size: 18px;
        color: #fff;

        .left,
        .right {
            display: flex;
            align-items: center;
            margin-right: 36px;
        }

        span {
            margin-right: 10px;
            font-size: 14px;
            color: #6c6e8e;

            &.pm10 {
                padding-left: 10px;
                margin-left: 10px;
                border-left: 1px solid #6c6e8e;
            }
        }
    }

    .process-box {
        .item {
            margin-bottom: h(21);
        }

        .info {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: h(8);
            font-size: 16px;
            font-weight: 600;
            color: #6c6e8e;

            .title {
                color: #fff;
            }

            .right {
                .percent {
                    margin-left: 16px;
                    font-size: 21px;
                    color: #51f1ff;
                }
            }
        }
    }

    .bottom {
        position: absolute;
        bottom: 0;
        left: 0;
        display: flex;
        align-items: center;
        width: 100%;
        height: h(50);
        padding: 0 16px;
        color: #6c6e8e;

        .label {
            display: flex;
            align-items: center;
            font-size: 14px;
        }

        .value {
            font-size: 18px;
            color: #fff;

            >span:first-child {
                margin: 0 20px;
                font-size: 16px;
                color: #7978ff;
            }
        }
    }

    img {
        width: 28px;
        height: 28px;
        padding-right: 6px;
    }
}
</style>
