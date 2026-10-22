<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-30 16:22:53
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-20 14:15:24
-->
<template>
  <div
    ref="refEl"
    class="operation-screen"
  >
    <!-- 原始视频背景 - 已注释，可以还原 -->
    <!-- <video
      loop
      autoplay
      muted
      width="100%"
    >
      <source src="@/assets/video/operation-screen-bg.mp4">
    </video> -->

    <!-- 新的图片背景 -->
    <div class="background-image" />
    <div
      v-if="resetRender"
      :class="{'content-box':true,details:isDetails}"
    >
      <header-info :weather-info="weatherInfo" />
      <section>
        <section class="left">
          <!-- 园区概览 -->
          <park-profile :weather-info="weatherInfo">
            <template #title>
              <div class="section-title">
                <img
                  src="~assets/images/operation-screen/park-profile-title.svg"
                  alt=""
                >
              </div>
            </template>
          </park-profile>
          <!-- 园区服务 -->
          <park-service>
            <template #title>
              <div class="section-title">
                <img
                  src="~assets/images/operation-screen/park-service-title.svg"
                  alt=""
                >
              </div>
            </template>
          </park-service>
          <!-- 巡检任务 -->
          <patrol-task :exceptional-task-info="exceptionalTaskInfo">
            <template #title>
              <div class="section-title">
                <img
                  src="~assets/images/operation-screen/patrol-task-title.svg"
                  alt=""
                >
              </div>
            </template>
          </patrol-task>
        </section>
        <section class="center">
          <section class="top">
            <floor-distribution @update:pageState="pageStateChange" />
          </section>
          <section class="bottom">
            <!-- 企业总览 -->
            <enterprise-overview>
              <template #title>
                <div class="section-title section-long-title">
                  <img
                    src="~assets/images/operation-screen/enterprise-overview-title.svg"
                    alt=""
                  >
                </div>
              </template>
            </enterprise-overview>
          </section>
        </section>
        <section class="right">
          <!-- 智慧招商 -->
          <wise-investment>
            <template #title>
              <div class="section-title">
                <img
                  src="~assets/images/operation-screen/wise-investment-title.svg"
                  alt=""
                >
              </div>
            </template>
          </wise-investment>
          <!-- 收费管理 -->
          <charge-manage>
            <template #title>
              <div class="section-title">
                <img
                  src="~assets/images/operation-screen/charge-manage-title.svg"
                  alt=""
                >
              </div>
            </template>
          </charge-manage>
          <!-- 公寓管理 -->
          <apartment-manage>
            <template #title>
              <div class="section-title">
                <img
                  src="~assets/images/operation-screen/apartment-manage-title.svg"
                  alt=""
                >
              </div>
            </template>
          </apartment-manage>
        </section>
      </section>
      <!-- <a-button
        @click="switchFullscreen"
      >
        {{ isFullscreen?'退出全屏':'全屏' }}
      </a-button> -->
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, shallowRef, onMounted, nextTick, Ref, onBeforeUnmount,
} from 'vue';

import CommandBigScreen, { ExceptionalTaskInfo, WeatherInfo } from '~models/command-big-screen';

import floorDistribution from './components/floor-distribution.vue';
import headerInfo from './components/header-info/header-info.vue';
import parkProfile from './components/park-profile/park-profile.vue';
import parkService from './components/park-service/index.vue';
import patrolTask from './components/patrol-task.vue';
import enterpriseOverview from './components/enterprise-overview.vue';
import wiseInvestment from './components/wise-investment.vue';
import chargeManage from './components/charge-manage.vue';
import apartmentManage from './components/apartment-manage.vue';

export default defineComponent({
  components: {
    headerInfo,
    floorDistribution, // 楼层分布图
    parkProfile,
    parkService,
    patrolTask,
    enterpriseOverview,
    wiseInvestment,
    chargeManage,
    apartmentManage,
  },
  props: {
  },
  setup() {
    const commandBigScreenInstance = new CommandBigScreen();
    const exceptionalTaskInfo = ref<ExceptionalTaskInfo>();
    // const isDetails = ref(false);

    const { resetRender, isDetails } = useFullscreen();
    const weatherInfo = ref<WeatherInfo>(); // 天气信息

    const init = async () => {
      await commandBigScreenInstance.initPark();
      exceptionalTaskInfo.value = await commandBigScreenInstance.exceptionalTaskInfo();
      weatherInfo.value = await commandBigScreenInstance.getWeather();
      resetRender.value = true;
    };
    const pageStateChange = (stateChange:boolean) => {
      isDetails.value = stateChange;
    };
    init();
    return {
      ...useFullscreen(),
      resetRender,
      exceptionalTaskInfo,
      isDetails,
      weatherInfo,
      pageStateChange,
    };
  },
});

// 全屏逻辑
function useFullscreen() {
  const isDetails = ref(false);
  const resetRender = ref(false);
  let timeId:NodeJS.Timeout;
  const switchFullScreen = () => {
    resetRender.value = false;
    clearTimeout(timeId);
    timeId = setTimeout(() => {
      isDetails.value = false;
      resetRender.value = true;
    }, 800);
  };

  onMounted(() => {
    window.addEventListener('resize', switchFullScreen);
  });
  onBeforeUnmount(() => {
    document.removeEventListener('resize', switchFullScreen);
  });
  return {
    resetRender,
    isDetails,
  };
}
</script>

<style lang="scss" scoped>
.operation-screen {
    position: relative;
    width: 1920px;
    height: h(1080);
    overflow: hidden;
    color: #fff;

    /* 原始视频样式 - 已注释，可以还原 */

    /* video {
        width: 100%;
        height: 100%;
        object-fit: fill;
    } */

    /* 新的图片背景样式 */
    .background-image {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: url('~assets/images/operation-screen/background.png') no-repeat;
        background-position: center center; /* 确保初始位置居中 */
        background-size: 110% 110%; /* 轻微放大保持清晰度 */
        animation: backgroundMove 40s ease-in-out infinite alternate;
    }

    /* 背景平移动画 - 微小范围移动保持清晰度 */
    @keyframes backgroundMove {
        0% {
            background-position: 48% 48%;
        }

        25% {
            background-position: 52% 50%;
        }

        50% {
            background-position: 50% 52%;
        }

        75% {
            background-position: 48% 51%;
        }

        100% {
            background-position: 46% 49%;
        }
    }

    >.content-box {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;

        &.details {
            background: linear-gradient(180deg, rgba(5, 13, 47, 0.6) 0%, #111d4e 100%);
            box-shadow: inset 0 1px 3px 4px rgba(24, 41, 83, 0.5), inset 0 1px 3px 24px #1b243b;
        }

        >button {
            position: absolute;
            top: 10px;
            right: 10px;
        }

        div.header {
            height: h(90);
            background: url('~assets/images/operation-screen/header_bg.gif') no-repeat 100%/100%;
        }

        >section {
            display: flex;
            height: calc(100% - 8.333vh);
            padding: 0 40px 20px;

            >.left,
            .right {
                width: 470px;
                height: 100%;
            }

            .section-title {
                position: relative;
                display: flex;
                align-items: center;
                justify-content: center;
                height: h(40);
                background: url('~assets/images/operation-screen/section-title.gif') no-repeat;
                background-size: 100% 100%;

                img {
                    height: h(20);
                }

                &.section-long-title {
                    background: url('~assets/images/operation-screen/section-long-title.gif') no-repeat 100%/100%;
                }
            }

            >.center {
                display: flex;
                flex: 1;
                flex-direction: column;
                justify-content: space-between;
                height: 100%;
                padding-left: 20px;
                overflow: hidden;

                >section.bottom {
                    height: h(302);
                    padding-right: 20px;
                }

                >section.top {
                    flex: 1;
                    margin-bottom: h(18);
                    overflow: hidden;
                }
            }
        }
    }
}
</style>
