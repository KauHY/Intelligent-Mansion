<template>
  <div class="header-info">
    <!-- 科幻动画效果层 -->
    <div class="sci-fi-effects">
      <!-- 粒子流动效果 -->
      <div class="particles">
        <div
          v-for="i in 20"
          :key="i"
          class="particle"
          :style="getParticleStyle(i)"
        />
      </div>
      <!-- 扫描线效果 -->
      <div class="scan-line" />
      <!-- 发光边框 -->
      <div class="glow-border" />
      <!-- 全息投影效果 -->
      <div class="hologram-overlay" />
    </div>

    <div class="left">
      <div class="time">
        {{ currentTime }}
      </div>
      <div class="date">
        <span> {{ currentDate }}</span>
        <span>{{ currentWeek }}</span>
      </div>
    </div>
    <div class="center">
      <div class="title-glow">
        沃富智慧园区运营大屏
      </div>
    </div>
    <div class="right">
      <div>
        <img src="~assets/images/operation-screen//header_weather_warn.png">
        <span>{{ weatherInfo?.warning||'-' }}</span>
      </div>
      <div>
        <icon :name="weatherInfo?.icon||'101'" />
        <span>{{ weatherInfo?.text||'-' }}</span>
      </div>
      <div>
        <span>{{ weatherInfo?.temp||'-' }}℃</span>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';
import dayjs from 'dayjs';
import { WeatherInfo } from '~models/command-big-screen';
import icon from './icon.vue';

export default defineComponent({
  components: { icon },
  props: {
    weatherInfo: {
      type: Object as PropType<WeatherInfo>,
      default: () => ({}),
    },
  },
  setup() {
    // const commandBigScreenInstance = new CommandBigScreen();
    // commandBigScreenInstance.initPark();

    // const weatherInfo = ref<WeatherInfo>();
    // const init = async () => {
    //   weatherInfo.value = await commandBigScreenInstance.getWeather();
    //   // setSeries(houseRentInfo.value);
    // };
    // init();

    // 粒子动画样式生成
    const getParticleStyle = (index: number) => {
      const delay = Math.random() * 3;
      const duration = 2 + Math.random() * 2;
      const size = 2 + Math.random() * 3;
      const startX = Math.random() * 100;
      const startY = Math.random() * 100;

      return {
        '--delay': `${delay}s`,
        '--duration': `${duration}s`,
        '--size': `${size}px`,
        '--start-x': `${startX}%`,
        '--start-y': `${startY}%`,
        animationDelay: `${delay}s`,
        animationDuration: `${duration}s`,
        width: `${size}px`,
        height: `${size}px`,
        left: `${startX}%`,
        top: `${startY}%`,
      };
    };

    return {
      ...useCurrentTime(),
      getParticleStyle,
      // weatherInfo,
    };
  },
});
function useCurrentTime() {
  const currentTime = ref('00:00:00');
  const currentDate = ref('2022-01-01');
  const currentWeek = ref('');
  const weekNames = {
    1: '一',
    2: '二',
    3: '三',
    4: '四',
    5: '五',
    6: '六',
    0: '日',
  };
  setInterval(() => {
    const temp = dayjs(Date.now());
    currentTime.value = temp.format('HH:mm:ss');
    currentDate.value = temp.format('YYYY-MM-DD');
    currentWeek.value = `星期${weekNames[temp.day()]}`;
  }, 1000);
  return {
    currentTime,
    currentDate,
    currentWeek,
  };
}
</script>

<style lang="scss" scoped>

// 科幻动画关键帧
@keyframes particleFloat {
    0% {
        opacity: 0;
        transform: translateY(0) translateX(0) scale(0);
    }

    10% {
        opacity: 1;
    }

    90% {
        opacity: 1;
    }

    100% {
        opacity: 0;
        transform: translateY(-100px) translateX(50px) scale(1);
    }
}

@keyframes scanLine {
    0% {
        opacity: 0;
        transform: translateX(-100%);
    }

    50% {
        opacity: 1;
    }

    100% {
        opacity: 0;
        transform: translateX(100%);
    }
}

@keyframes glowPulse {
    0%,
    100% {
        box-shadow:
            0 0 5px rgba(87, 200, 255, 0.3),
            0 0 10px rgba(87, 200, 255, 0.2),
            0 0 15px rgba(87, 200, 255, 0.1);
    }

    50% {
        box-shadow:
            0 0 10px rgba(87, 200, 255, 0.6),
            0 0 20px rgba(87, 200, 255, 0.4),
            0 0 30px rgba(87, 200, 255, 0.2);
    }
}

@keyframes hologramFlicker {
    0%,
    100% { opacity: 1; }
    98% { opacity: 1; }
    99% { opacity: 0.8; }
    99.5% { opacity: 1; }
}

@keyframes titleGlow {
    0%,
    100% {
        text-shadow:
            0 0 10px rgba(193, 229, 255, 0.6),
            0 2px 4px rgba(0, 0, 0, 0.3),
            0 0 20px rgba(87, 200, 255, 0.4);
    }

    50% {
        text-shadow:
            0 0 15px rgba(193, 229, 255, 0.8),
            0 2px 4px rgba(0, 0, 0, 0.3),
            0 0 30px rgba(87, 200, 255, 0.6),
            0 0 40px rgba(193, 229, 255, 0.3);
    }
}

div {
    .header-info {
        position: relative;
        display: flex;
        align-items: flex-start;
        justify-content: space-between;
        height: h(90);
        padding: h(50) 50px 0;
        overflow: hidden;
        background: url('~assets/images/operation-screen/header-info.png') no-repeat;
        background-size: 100% 100%;

        // 科幻效果层
        .sci-fi-effects {
            position: absolute;
            top: 0;
            left: 0;
            z-index: 1;
            width: 100%;
            height: 100%;
            pointer-events: none;

            // 粒子效果
            .particles {
                position: absolute;
                width: 100%;
                height: 100%;

                .particle {
                    position: absolute;
                    background: radial-gradient(circle, rgba(87, 200, 255, 0.8) 0%, rgba(193, 229, 255, 0.4) 50%, transparent 100%);
                    filter: blur(0.5px);
                    border-radius: 50%;
                    animation: particleFloat infinite linear;
                }
            }

            // 扫描线效果
            .scan-line {
                position: absolute;
                top: 0;
                left: 0;
                width: 2px;
                height: 100%;
                background:
                    linear-gradient(
                        to bottom,
                        transparent 0%,
                        rgba(87, 200, 255, 0.8) 50%,
                        transparent 100%
                    );
                box-shadow: 0 0 10px rgba(87, 200, 255, 0.6);
                animation: scanLine 4s infinite linear;
            }

            // 发光边框
            .glow-border {
                position: absolute;
                top: 2px;
                right: 2px;
                bottom: 2px;
                left: 2px;
                pointer-events: none;
                border: 1px solid rgba(87, 200, 255, 0.3);
                border-radius: 4px;
                animation: glowPulse 3s infinite ease-in-out;
            }

            // 全息投影效果
            .hologram-overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                pointer-events: none;
                background:
                    repeating-linear-gradient(
                        0deg,
                        transparent,
                        transparent 2px,
                        rgba(87, 200, 255, 0.03) 2px,
                        rgba(87, 200, 255, 0.03) 4px
                    );
                animation: hologramFlicker 0.15s infinite linear;
            }
        }

        .left {
            position: relative;
            z-index: 10;
            display: flex;
            align-items: center;
            font-weight: bold;

            .time {
                margin-right: 20px;
                font-size: 26px;
                color: #c1e5ff;
                text-shadow: 0 0 10px rgba(193, 229, 255, 0.5);
                animation: titleGlow 2s infinite ease-in-out;
            }

            >.date {
                font-size: 20px;
                color: #57c8ff;
                text-shadow: 0 0 8px rgba(87, 200, 255, 0.4);

                >span:last-child {
                    margin-left: 20px;
                    font-size: 18px;
                    font-weight: 600;
                }
            }
        }

        .center {
            position: absolute;
            top: 50%;
            left: 50%;
            z-index: 10;
            font-family: serif;
            font-size: 32px;
            font-style: oblique;
            font-weight: 1000;
            color: #fff;
            letter-spacing: 2px;
            transform: translate(-50%, -50%);

            .title-glow {
                filter: drop-shadow(0 0 10px rgba(87, 200, 255, 0.6));
                animation: titleGlow 3s infinite ease-in-out;
            }
        }

        .right {
            position: relative;
            z-index: 10;
            display: flex;
            align-items: center;
            font-size: 22px;
            font-weight: 600;
            color: #57c8ff;

            >div {
                display: flex;
                align-items: center;
                margin-right: 30px;
                text-shadow: 0 0 8px rgba(87, 200, 255, 0.4);

                &:last-child {
                    font-size: 26px;
                    color: #c1e5ff;
                    text-shadow: 0 0 10px rgba(193, 229, 255, 0.5);
                }
            }

            img {
                width: 24px;
                height: 24px;
                margin-right: 6px;
                filter: drop-shadow(0 0 5px rgba(87, 200, 255, 0.6));
            }
        }

        // 鼠标悬停增强效果
        &:hover {
            .sci-fi-effects {
                .glow-border {
                    border-color: rgba(87, 200, 255, 0.6);
                    animation-duration: 1.5s;
                }

                .particles .particle {
                    animation-duration: 1.5s;
                }
            }
        }
    }
}
</style>
