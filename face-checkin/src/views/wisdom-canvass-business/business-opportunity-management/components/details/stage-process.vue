<!--
 * @Description:阶段流程
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-02 13:45:41
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-26 14:25:24
-->
<template>
  <div class="stage-process">
    <a-timeline>
      <a-timeline-item
        v-for="record in stageProcess"
        :key="record.id"
        color="#2578ED"
      >
        <template #dot>
          <v-svg
            name="clock"
            style="font-size: 16px; color: var(--ant-primary-color);"
          />
        </template>
        <div class="content-box">
          <!-- <div class="create" > -->
          <div
            class="record-item"
          >
            <span>{{ record.createTime }}</span>
            <span>{{ record.createByName }}</span>
            <span>{{ record.recordTypeName }}</span>
            <!-- <div
                v-for="(label, fieldKey) in recordItemConf[record.state]"
                :key="fieldKey"
                class="field"
              >
                <p class="label">
                  {{ label }}
                </p>
                <p>{{ record[fieldKey] }}</p>
              </div> -->
          </div>
        </div>
      </a-timeline-item>
    </a-timeline>
  </div>
</template>
<script lang='ts'>
import {
  ref, defineComponent,
} from 'vue';
import WisdomCanvassBusiness, { StageProcess } from '~models/wisdom-canvass-business';

export default defineComponent({
  components: {},
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const stageProcess = ref<Array<StageProcess>>([]);
    const init = async () => {
      stageProcess.value = await new WisdomCanvassBusiness().stageProcessById(props.id);
    };
    init();
    return {
      stageProcess,
    };
  },
});
</script>
<style lang="scss" scoped>
.stage-process {
    padding-top: 30px;
    padding-left: 40px;

    .record-item {
        padding: 4px 0 20px;

        span {
            margin-right: 20px;
        }
    }
}
</style>
