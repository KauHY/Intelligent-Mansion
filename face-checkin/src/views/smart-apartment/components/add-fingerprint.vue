<!--
 * @Description:添加指纹
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-08-01 15:49:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-08-03 10:23:37
-->
<template>
  <a-modal
    v-model:visible="currentVisible"
    :width="800"
    :closable="false"
    :mask-closable="false"
    title="添加指纹"
  >
    <div class="add-fingerprint">
      <div
        v-if="step==-1"
        class="step_1"
      >
        <img src="~assets/images/add-fingerprint/add-fingerprint_start.png">
        <a-button
          type="primary"
          @click="startEntry"
        >
          放置手指，开始录入指纹
        </a-button>
      </div>
      <div
        v-else
        class="start-entry"
      >
        <div class="tip">
          <div
            v-if="currentStatus==='error'"
            class="error"
          >
            采集失败，采集器连接异常或采集时间不够，请重新采集
          </div>
          <div
            v-else-if="step === 2"
            class="success"
          >
            采集完成
          </div>
          <div v-else>
            正在录入，请不要松开手指 <loadingIcon />
          </div>
        </div>
        <div class="result">
          <img
            v-if="step===0"
            src="~assets/images/add-fingerprint/add-fingerprint_1.png"
          >
          <img
            v-else-if="step===1"
            src="~assets/images/add-fingerprint/add-fingerprint_2.png"
          >
          <img
            v-else-if="step===2"
            src="~assets/images/add-fingerprint/add-fingerprint_3.png"
          >
        </div>
        <div class="process-box">
          <a-steps
            v-model:current="step"
            :status="currentStatus"
            size="small"
            class="process"
          >
            <a-step />
            <a-step />
            <a-step />
          </a-steps>
        </div>
        <div class="tip">
          请按下手指，设备会连续滴三声，若最后一声
        </div>
      </div>
    </div>
    <template
      #footer
    >
      <a-button
        v-if="currentStatus === 'error'||step===2"
        type="primary"
        @click="resetEntry"
      >
        重新采集
      </a-button>
      <a-button
        @click="close"
      >
        取消
      </a-button>
    </template>
  </a-modal>
</template>
<script lang='ts' setup >
import {
  reactive, ref, defineComponent, PropType, computed, toRefs, defineProps, defineEmits,
} from 'vue';
import { useEntryFingerprint } from './useEntryFingerprint';
import loadingIcon from './loading-icon.vue';

const props = defineProps({
  visible: {
    type: Boolean,
    default: false,
  },
  employeeId: {
    type: String,
    default: '',
  },
});
const emit = defineEmits({
  'update:visible': (visible:boolean) => true,
});
const { visible, employeeId } = toRefs(props);

const currentVisible = computed({
  get() {
    return visible.value;
  },
  set(value:boolean) {
    emit('update:visible', value);
  },
});
const {
  step, operate, startEntry, QuitApp, currentStatus, resetEntry,
} = useEntryFingerprint(employeeId.value);
// 保存
const close = async () => {
  resetEntry();
  currentVisible.value = false;
};

</script>
<style lang='scss' scoped>
.add-fingerprint {
    height: 50vh;

    .step_1,
    .result {
        display: flex;
        flex-direction: column;
        align-items: center;

        img {
            width: h(312);
            height: h(292);
            margin-bottom: h(160);
        }
    }

    .start-entry {
        text-align: center;

        .result {
            color: #424656;

            img {
                margin: 0;
            }
        }

        .process-box {
            display: flex;
            justify-content: center;
            margin: h(25) 0 h(40);

            .process {
                width: h(350);
                min-width: 350px;
            }
        }

        .tip {
            div {
                display: flex;
                align-items: flex-end;
                justify-content: center;
                margin-bottom: 20px;

                &.error {
                    color: var(--ant-error-color);
                }

                &.success {
                    color: var(--ant-primary-color);
                }
            }

            &:last-child {
                margin-top: 30px;
                color: #7d8292;
            }
        }
    }
}
</style>
