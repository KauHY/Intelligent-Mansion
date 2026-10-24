<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-05-31 10:42:25
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-20 14:53:20
-->
<template>
  <div :class="{'floor-distribution':true}">
    <v-svg
      v-if="!!building.label"
      name="return"
      class="go-black"
      @click="goBlack"
    />
    <div class="center">
      <div
        v-if="building.label"
        class="left"
      >
        <span
          :class="{left:true,disabled:floorScrollState==='top'}"
          @click="floorScroll('top')"
        />
        <span
          :class="{right:true,disabled:floorScrollState==='bottom'}"
          @click="floorScroll('bottom')"
        />
        <div
          ref="floorScrollEl"
          class="sroll-content"
        >
          <div
            v-for="item in totalFloor"
            :key="item"
            :class="{activated:Number(floor)===item}"
            class="floor"
            @click="switchObj(`${item}`)"
          >
            {{ item }}楼
          </div>
        </div>
      </div>
      <div
        v-if="building.label"
        ref="renderEl"
        class="room-distribution"
      />
    </div>
    <div class="bottom-box">
      <div>
        <span
          :class="{left:true,disabled:buildingScrollState==='left'}"
          @click="buildingScroll('left')"
        />
        <span
          :class="{right:true,disabled:buildingScrollState==='right'}"
          @click="buildingScroll('right')"
        />
        <div
          ref="buildingScrollEl"
          class="sroll-content"
        >
          <div
            v-for="(item,index) in buildingList"
            :key="item.value"
            :class="{activated:building.label===item.label,building:true,first:!index,last:index===buildingList.length-1}"
            @click="switchObj(item)"
          >
            {{ item.label }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, watch,
} from 'vue';

import { useThree } from '../composition';

export default defineComponent({
  props: {
  },
  emits: {
    'update:pageState': (isDetails:boolean) => true,
  },
  setup(props, { emit }) {
    const {
      floor, building, room, switchObj, renderEl, buildingList, totalFloor, clearObjCache,
    } = useThree();

    watch(building, (value) => {
      if (value) emit('update:pageState', true);
    });
    const goBlack = () => {
      emit('update:pageState', false);
      building.value.value = '';
      building.value.label = '';
      room.value = '101';
      clearObjCache();
    };
    return {
      floor,
      building,
      renderEl,
      room,
      buildingList,
      totalFloor,
      goBlack,
      switchObj,
      ...useScroll(),
    };
  },
});
function useScroll() {
  const buildingScrollEl = ref();
  const floorScrollEl = ref();
  const buildingScrollState = ref('left');
  const floorScrollState = ref('top');
  const buildingScroll = (direction:string) => {
    const step = buildingScrollEl.value.offsetWidth - 50;
    const scrollWidth = buildingScrollEl.value.scrollWidth - buildingScrollEl.value.offsetWidth;
    // let distance = 0
    if (direction === 'right') {
      buildingScrollEl.value.scrollLeft += step;
      // if (!buildingScrollEl.value.scrollLeft < scrollWidth) {
      // buildingScrollState.value = 'right';
      // }
    } else {
      buildingScrollEl.value.scrollLeft -= step;
      // buildingScrollState.value = 'left';
    }
    console.log(buildingScrollEl.value.scrollLeft, scrollWidth);
    if (!(buildingScrollEl.value.scrollLeft < scrollWidth)) {
      buildingScrollState.value = 'right';
    } else if (buildingScrollEl.value.scrollLeft === 0) {
      buildingScrollState.value = 'left';
    } else {
      buildingScrollState.value = '';
    }
  };

  const floorScroll = (direction:string) => {
    const step = floorScrollEl.value.offsetHeight - 50;
    const scrollHeight = floorScrollEl.value.scrollHeight - floorScrollEl.value.offsetHeight;
    // let distance = 0
    if (direction === 'top') {
      floorScrollEl.value.scrollTop -= step;
    } else {
      floorScrollEl.value.scrollTop += step;
    }
    console.log(floorScrollEl.value.scrollTop, scrollHeight, floorScrollEl.value.offsetHeight);
    if (!(floorScrollEl.value.scrollTop < scrollHeight)) {
      floorScrollState.value = 'bottom';
    } else if (floorScrollEl.value.scrollTop === 0) {
      floorScrollState.value = 'top';
    } else {
      floorScrollState.value = '';
    }
  };

  return {
    floorScrollEl,
    buildingScrollState,
    floorScrollState,
    buildingScrollEl,
    buildingScroll,
    floorScroll,
  };
}
</script>
<style lang="scss" scoped>
.floor-distribution {
    position: relative;
    display: flex;
    flex: 1;
    flex-direction: column;
    height: 100%;
    padding-left: 20px;
    overflow: hidden;

    .go-black {
        position: absolute;
        top: 10px;
        right: 10px;
        z-index: 2;
        width: 42px;
        height: 42px;
        cursor: pointer;
    }

    >.center {
        display: flex;
        flex: 1;
        height: 100%;
        overflow: hidden;

        >.left {
            position: relative;
            width: 60px;
            height: calc(100% - 2.1296vh);
            padding: h(40) 0;
            background: rgba(15, 46, 96, 0.7);
            border: 1px solid rgba(8, 119, 229, 0.9);
            border-radius: 10px;
            box-shadow: 0 0 6px 0 rgba(8, 119, 229, 0.9), 0 0 10px 0 rgba(59, 108, 251, 0.3);
            opacity: 0.7;

            >span {
                position: absolute;
                left: 5px;
                width: 43px;
                height: h(46);
                cursor: pointer;

                &.left {
                    top: 0;
                    background: url('~assets/images/operation-screen/sroll-content_icon_2.png') no-repeat 100%/100%;
                    transform: rotateZ(90deg);

                    &.disabled {
                        background: url('~assets/images/operation-screen/sroll-content_icon_2_disabled.png') no-repeat 100%/100%;
                    }
                }

                &.right {
                    bottom: 0;
                    background: url('~assets/images/operation-screen/sroll-content_icon_1.png') no-repeat 100%/100%;
                    transform: rotateZ(90deg);

                    &.disabled {
                        background: url('~assets/images/operation-screen/sroll-content_icon_1_disabled.png') no-repeat 100%/100%;
                    }
                }
            }

            >.sroll-content {
                width: 100%;
                height: 100%;
                overflow: auto;

                &::-webkit-scrollbar {
                    display: none;
                }
            }

            .floor {
                position: relative;
                width: 100%;
                height: h(48);
                line-height: h(48);
                color: #fff;
                text-align: center;
                cursor: pointer;

                &::after {
                    position: absolute;
                    bottom: -1px;
                    left: 50%;
                    width: 28px;
                    height: 1px;
                    content: '';
                    background: #ffff;
                    opacity: 0.2;
                    transform: translateX(-50%);
                }

                &.activated {
                    color: #fff;
                    border: 1px solid rgba(8, 119, 229, 0.9);
                    border-radius: 10px;
                    box-shadow: 0 0 10px 2px rgba(8, 119, 229, 0.9), 0 0 10px 2px rgba(59, 108, 251, 0.3);
                }
            }
        }

        >.room-distribution {
            flex: 1;
            height: 100%;
        }
    }

    .bottom-box {
        width: 100%;
        padding-right: 20px;

        >div {
            position: relative;
            width: 100%;
            height: h(50);
            padding: 0 40px;
            margin-bottom: h(4);
            background: rgba(15, 46, 96, 0.7);
            border: 1px solid rgba(99, 158, 255, 1);
            border-radius: 10px;
            box-shadow: 0 0 6px 0 rgba(8, 119, 229, 0.9), 0 0 10px 0 rgba(59, 108, 251, 0.3);
            opacity: 0.7;

            >.sroll-content {
                display: flex;
                align-items: center;
                height: 100%;
                overflow: auto;

                &::-webkit-scrollbar {
                    display: none;
                }
            }

            >span {
                position: absolute;
                bottom: 0;
                width: 43px;
                height: h(46);
                cursor: pointer;

                &.left {
                    left: 0;
                    background: url('~assets/images/operation-screen/sroll-content_icon_2.png') no-repeat 100%/100%;

                    &.disabled {
                        background: url('~assets/images/operation-screen/sroll-content_icon_2_disabled.png') no-repeat 100%/100%;
                    }
                }

                &.right {
                    right: 0;
                    background: url('~assets/images/operation-screen/sroll-content_icon_1.png') no-repeat 100%/100%;

                    &.disabled {
                        background: url('~assets/images/operation-screen/sroll-content_icon_1_disabled.png') no-repeat 100%/100%;
                    }
                }
            }

            .building {
                position: relative;
                display: flex;
                flex: 0 0 80px;
                align-items: center;
                justify-content: center;
                height: 100%;
                color: #fff;
                cursor: pointer;

                &::after {
                    position: absolute;
                    top: 50%;
                    right: -1px;
                    width: 1px;
                    height: h(30);
                    content: '';
                    background: #ffff;
                    opacity: 0.2;
                    transform: translateY(-50%);
                }

                &.activated {
                    color: #fff;
                    border: 1px solid rgba(8, 119, 229, 0.9);
                    border-radius: 10px;
                    box-shadow: 0 0 10px 2px rgba(8, 119, 229, 0.9), 0 0 10px 2px rgba(59, 108, 251, 0.3);
                }
            }
        }
    }
}
</style>
