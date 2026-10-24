<template>
  <div class="infors-box">
    <div class="white-box">
      <span class="img-box">{{ nowDate }}</span>
      <span class="time">{{ nowTime }}</span>
    </div>
    <div class="body-box">
      <div class="center-middle">
        <div
          v-if="meetingContent.meetingStatus !== 0"
          class="meeting-centent"
          :style="{
            backgroundImage:
              bgStatus === 1
                ? initMeetingBgOne.backgroundImage
                : bgStatus === 3
                  ? initMeetingBgThree.backgroundImage
                  : initMeetingBgTwo.backgroundImage,
            backgroundRepeat: 'no-repeat',
            backgroundSize: '100% 100%',
          }"
        >
          <div
            class="foor"
            :class="meetingContent.meetingStatus === 1 ? 'black-meet' : ''"
          >
            {{ meetingContent.meetingName }}
          </div>

          <div
            v-if="
              meetingContent.meetingStatus === 2 ||
                meetingContent.meetingStatus === 3
            "
            class="big-centent"
          >
            <div class="icon-box">
              <img
                src="~assets/images/statusone.png"
                class="icon"
                alt=""
              >
              <span class="status">
                {{ meetingContent.meetingStatus === 2 ? "即将开始" : "使用中" }}
              </span>
            </div>
            <div
              v-if="meetingContent.meetingTimes"
              class="time-centent"
            >
              {{ meetingContent.meetingTimes[0]?.startTime }}-{{
                meetingContent.meetingTimes[0]?.endTime
              }}
            </div>
            <div
              v-if="meetingContent.meetingTimes"
              class="name-meeting"
            >
              {{ meetingContent.meetingTimes[0]?.meetingSubject }}
            </div>
          </div>

          <div
            v-if="meetingContent.meetingStatus === 1"
            class="charts"
          >
            空闲中
          </div>
          <div
            v-if="meetingContent.meetingStatus === 1"
            class="sub-chat"
          >
            VHCANT
          </div>
        </div>
        <div
          v-if="meetingContent.meetingStatus !== 0"
          class="meeting-status"
        >
          <div class="title-header">
            <div>会议时间</div>
            <div class="line-cs" />
            <div>状态</div>
          </div>
          <div class="content-infos">
            <img
              v-if="meetingContent.meetingStatus === 1 && meetingContent.meetingTimes.length===0"
              class="empty"
              src="~assets/images/empty.png"
            >
            <div
              v-if="meetingContent.meetingStatus === 1 && meetingContent.meetingTimes.length===0"
              class="empty-title"
            >
              暂无会议
            </div>
            <div
              v-for="(item, index) in meetingContent.meetingTimes"
              v-else
              :key="index"
              class="line-css"
            >
              <div
                class="left-title"
                :class="
                  index === 0 && meetingContent.meetingStatus === 3
                    ? 'add-css'
                    : ''
                "
              >
                {{ item.startTime
                }}<span
                  :class="index === 0 && meetingContent.meetingStatus === 3 ? 'bg-css' : ''"
                  class="black-line"
                />{{ item.endTime }}
              </div>
              <div
                class="right-status"
                :class="
                  index === 0 && meetingContent.meetingStatus === 3
                    ? 'add-css'
                    : ''
                "
              >
                {{
                  index === 0 && meetingContent.meetingStatus === 3
                    ? "进行中"
                    : "未开始"
                }}
              </div>
            </div>
          </div>
          <img
            v-if="meetingContent && meetingContent.meetingTimes && meetingContent.meetingTimes.length>0"
            class="three-add"
            src="~assets/images/icon_more.png"
          >
        </div>
        <div
          v-if="meetingContent.meetingStatus === 0"
          class="meeting-stop"
          :style="{
            backgroundImage: initMeetingBgZero.backgroundImage,
            backgroundRepeat: 'no-repeat',
            backgroundSize: '100% 100%',
          }"
        >
          <div class="foor white-meet">
            会议室{{ meetingContent.meetingName }}
          </div>

          <div class="icon-box">
            <span class="stop-status"> 暂停使用 </span>
          </div>
          <div class="stop-sub">
            Temporarily Closed
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  ref, defineComponent, onMounted, onBeforeUnmount,
} from 'vue';
import MeetingVo from '~models/meeting-manage';
import * as dayjs from 'dayjs';

export default defineComponent({
  components: {},
  props: {
    meetingConfig: {
      type: Object,
      default: () => ({
        title: '',
        type: '',
        meetData: null,
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const MeetingVoReq = new MeetingVo();
    const meetingContent = ref<any>([]);
    const timer = ref<any>();
    const timerTop = ref<any>();
    const nowDate = ref<string>('');
    const nowTime = ref<string>('');
    const bgStatus = ref<number>(0);
    const initMeetingBgOne = ref({
      backgroundImage: `url(${require('@/assets/images/login/bg_1.png')})`,
    });
    const initMeetingBgThree = ref({
      backgroundImage: `url(${require('@/assets/images/login/bg_3.png')})`,
    });
    const initMeetingBgTwo = ref({
      backgroundImage: `url(${require('@/assets/images/login/bg_2.png')})`,
    });
    const initMeetingBgZero = ref({
      backgroundImage: `url(${require('@/assets/images/login/bg_0.png')})`,
    });
    const getMeeTime = async () => {
      const url = window.location.href.toString();
      const arrUrl = url.split('=')[1];

      if (url.indexOf('type') >= 0) {
        meetingContent.value = (await MeetingVoReq.overviewMeeting(arrUrl, 1)) || [];
      } else {
        meetingContent.value = (await MeetingVoReq.overviewMeeting(arrUrl, 4)) || [];
      }
      const { now, meetingStatus } = meetingContent.value;
      bgStatus.value = meetingStatus;
      nowDate.value = now.split(' ')[0];
      nowTime.value = now.split(' ')[1];
    };
    const init = async () => {
      // meetingContent.value = {
      //   meetingName: '阿凡达',
      //   meetingStatus: 1,
      //   meetingTimes: [
      //     {
      //       endTime: '15:30',
      //       meetingSubject: 'w我是主题',
      //       startTime: '16:43',
      //     },
      //     {
      //       endTime: '15:30',
      //       meetingSubject: 'w我是主题',
      //       startTime: '16:43',
      //     },
      //   ],
      //   now: '2022-04-25 16:43',
      // };
      getMeeTime();
      timer.value = setInterval(() => {
        getMeeTime();
      }, 10000);
      // timerTop.value = setInterval(() => {
      //   nowDate.value = `${dayjs().format('YYYY-MM-DD')}`;
      //   nowTime.value = `${dayjs().format('HH:mm')}`;
      // }, 1000);
    };
    onMounted(() => {
      init();
    });
    onBeforeUnmount(() => {
      if (timer.value) {
        clearInterval(timer.value); // 清除定时器
      }
      // if (timerTop.value) {
      //   clearInterval(timerTop.value); // 清除定时器
      // }
    });
    return {
      initMeetingBgOne,
      initMeetingBgThree,
      initMeetingBgTwo,
      initMeetingBgZero,
      bgStatus,
      nowTime,
      nowDate,
      meetingContent,
      emit,
    };
  },
});

function useOptions() {
  return {};
}
</script>
<style lang="scss" scoped>
.infors-box {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;

    .black-meet {
        color: #424656;
    }

    .white-meet {
        color: #fff;
    }

    .white-box {
        position: relative;
        width: 100%;
        height: 100px;
        line-height: 100px;
        background: #fff;
    }

    .body-box {
        display: flex;
        flex: 1;
        justify-content: center;
        background: url("assets/images/meetingbg.png") no-repeat;
        background-size: 100% 100%;

        .center-middle {
            display: flex;
            justify-content: center;
            width: 90%;
            height: 80%;
            margin-top: 70px;
        }

        .meeting-centent {
            position: relative;
            width: 70%;
            height: 100%;
            color: #fff;

            .foor {
                position: absolute;
                top: 90px;
                left: 90px;
                font-size: 34px;
                font-weight: 600;
            }

            @media screen and (max-width: 1280px) {
                .foor {
                    font-size: 51px;
                }
            }

            .big-centent {
                position: absolute;
                top: 50%;
                left: 50%;
                display: flex;
                flex-direction: column;
                align-items: center;
                width: 70%;
                transform: translate(-50%, -50%);
            }

            .icon {
                display: inline-block;
                width: 60px;
                height: 60px;
                margin-right: 11px;
                vertical-align: middle;
            }

            @media screen and (max-width: 1280px) {
                .icon {
                    width: 80px;
                    height: 80px;
                }
            }

            .time-centent {
                height: 84px;
                margin-bottom: 20px;
                font-family: PingFangSC-Semibold, PingFang SC;
                font-size: 60px;
                font-weight: 600;
                line-height: 84px;
                color: #fff;
            }

            @media screen and (max-width: 1280px) {
                .time-centent {
                    margin-bottom: 60px;
                    font-size: 90px;
                }
            }

            .name-meeting {
                height: 56px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-size: 40px;
                font-weight: 400;
                line-height: 56px;
                color: #fff;
            }

            @media screen and (max-width: 1280px) {
                .name-meeting {
                    font-size: 60px;
                }
            }

            .icon-box {
                height: 70px;
                margin-bottom: 20px;
                font-family: PingFangSC-Semibold, PingFang SC;
                font-size: 50px;
                font-weight: 600;
                line-height: 70px;
                color: #fff;
            }

            @media screen and (max-width: 1280px) {
                .icon-box {
                    margin-bottom: 60px;
                    font-size: 75px;
                }
            }

            .charts {
                position: absolute;
                top: 40%;
                left: 50%;
                font-family: PingFangSC-Semibold, PingFang SC;
                font-size: 70px;
                font-weight: 600;
                color: #424656;
                transform: translate(-50%, -40%);
            }

            @media screen and (max-width: 1280px) {
                .charts {
                    font-size: 105px;
                }
            }

            .sub-chat {
                position: absolute;
                top: 60%;
                left: 50%;
                font-size: 40px;
                font-weight: 400;
                color: #959ba7;
                transform: translate(-50%, -54%);
            }

            @media screen and (max-width: 1280px) {
                .sub-chat {
                    font-size: 60px;
                }
            }
        }

        .meeting-stop {
            position: relative;
            width: 95%;

            .foor {
                position: absolute;
                top: 90px;
                left: 90px;
                font-size: 34px;
                font-weight: 600;
            }

            @media screen and (max-width: 1280px) {
                .foor {
                    font-size: 51px;
                }
            }

            .stop-status {
                position: absolute;
                top: 40%;
                left: 50%;
                font-size: 80px;
                font-weight: 600;
                color: #fff;
                transform: translate(-50%, -40%);
            }

            @media screen and (max-width: 1280px) {
                .stop-status {
                    font-size: 120px;
                }
            }

            .stop-sub {
                position: absolute;
                top: 60%;
                left: 50%;
                font-family: PingFangSC-Regular, PingFang SC;
                font-size: 50px;
                font-weight: 400;
                color: #c9c9c9;
                transform: translate(-50%, -60%);
            }

            @media screen and (max-width: 1280px) {
                .stop-sub {
                    font-size: 60px;
                }
            }
        }

        .meeting-status {
            position: relative;
            width: 40%;
            height: 100%;
            background: url("assets/images/meeting_status.png") no-repeat;
            background-size: 100% 100%;

            .three-add {
                position: absolute;
                bottom: 38px;
                left: 50%;
                width: 40px;
                height: 40px;
                transform: translateX(-50%);
            }

            .title-header {
                position: absolute;
                top: 52px;
                left: 50%;
                display: flex;
                align-items: center;
                justify-content: space-between;
                width: 80%;
                height: 70px;
                padding: 0 60px;
                font-family: PingFangSC-Semibold, PingFang SC;
                font-size: 24px;
                font-weight: 600;
                color: #424656;
                background: #d8d8d8 linear-gradient(180deg, #fff 0%, #d0e0f3 100%);
                border-radius: 30px;
                transform: translateX(-50%);

                .line-cs {
                    width: 3px;
                    height: 30px;
                    background-color: #bcc4cd;
                }
            }

            @media screen and (max-width: 1280px) {
                .title-header {
                    font-size: 30px;
                }
            }

            .content-infos {
                position: absolute;
                top: 160px;
                left: 50%;
                width: 80%;
                transform: translateX(-50%);

                .empty {
                    position: absolute;
                    left: 50%;
                    width: 90%;
                    height: 250px;
                    transform: translateX(-50%);
                }

                .empty-title {
                    position: absolute;
                    top: 300px;
                    left: 50%;
                    font-size: 20px;
                    font-weight: 400;
                    color: #959ba7;
                    transform: translateX(-50%);
                }

                @media screen and (max-width: 1280px) {
                    .empty-title {
                        font-size: 30px;
                    }
                }

                .line-css {
                    display: flex;
                    justify-content: space-between;
                    padding: 0 40px 30px 40px;
                    margin-bottom: 30px;
                    font-size: 22px;
                    font-weight: 400;
                    line-height: 30px;
                    color: #424656;
                    border-bottom: 1px solid #cdd2d9;

                    .black-line {
                        display: inline-block;
                        width: 10px;
                        height: 1px;
                        margin: 0 3px;
                        vertical-align: middle;
                        background-color: #424656;
                    }

                    .bg-css {
                        background-color: #f65321;
                    }
                }

                @media screen and (max-width: 1280px) {
                    .line-css {
                        font-size: 33px;
                    }
                }
            }
        }
    }

    .img-box {
        position: relative;
        left: 70px;
        font-size: 32px;
        font-weight: 400;
        color: #424656;
    }

    @media screen and (max-width: 1280px) {
        .img-box {
            font-size: 48px;
        }
    }

    .time {
        position: relative;
        left: 35%;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-size: 32px;
        font-weight: 600;
        color: #424656;
    }

    @media screen and (max-width: 1280px) {
        .time {
            font-size: 48px;
        }
    }

    .add-css {
        color: #f65321;
    }
}
</style>
