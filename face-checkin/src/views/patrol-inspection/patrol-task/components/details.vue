<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-02 14:28:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-07 16:30:31
-->
<template>
  <div class="details">
    <div class="item">
      <div class="label">
        关联园区:
      </div>
      <div>{{ details?.parkOrgName }}</div>
    </div>
    <div class="item">
      <div class="label">
        巡检任务id:
      </div>
      <div>{{ details?.id }}</div>
    </div>
    <div class="item">
      <div class="label">
        巡检任务:
      </div>
      <div>{{ details?.taskName }}</div>
    </div>
    <div class="item">
      <div class="label">
        巡检类型:
      </div>
      <div>{{ inspectionTypes[details?.type||999]?.label }}</div>
    </div>

    <div class="item">
      <div class="label">
        巡检人:
      </div>
      <div>
        <a-tag
          v-for="(item,index) in details?.employees"
          :key="index"
          color="green"
        >
          {{ item }}
        </a-tag>
      </div>
    </div>
    <div class="item">
      <div class="label">
        任务时间:
      </div>
      <div>{{ details?.taskTime }}</div>
    </div>
    <div class="item">
      <div class="label">
        巡检完成时间:
      </div>
      <div>{{ details?.completionTime }}</div>
    </div>
    <div class="item">
      <div class="label">
        巡检状态:
      </div>
      <div>{{ taskStatusOpts[details?.status]?.label }}</div>
    </div>
    <a-collapse v-model:activeKey="activeKey">
      <a-collapse-panel
        key="1"
        header="巡检详情"
      >
        <div class="point-details">
          <!-- <div class="title">
            巡检详情
          </div> -->
          <div
            v-for="(item,index) in details?.pointDetails"
            :key="index"
            class="details-item"
          >
            <div class="item">
              <div class="label">
                巡检点:
              </div>
              <div>{{ item.pointName }}</div>
            </div>
            <div class="item">
              <div class="label">
                巡检点状态:
              </div>
              <div>{{ options['INSPECTION_POINT_STATUS'][item?.pointStatus]?.label||'-' }}</div>
            </div>
            <div class="item">
              <div class="label">
                巡检情况:
              </div>
              <div>{{ item.state?'正常':'无' }}</div>
            </div>
            <div class="item">
              <div class="label">
                巡检说明:
              </div>
              <div>{{ item.description }}</div>
            </div>
            <div class="item">
              <div class="label">
                巡检时间:
              </div>
              <div>{{ item.updateTime }}</div>
            </div>
            <div class="item">
              <div class="label">
                巡检图片:
              </div>
              <!-- <div>{{ item.picture }}</div> -->
              <a-image
                v-if="item.picture"
                :src="getPreviewUrl(item.picture)"
                alt=""
              />
            </div>
          </div>
        </div>
      </a-collapse-panel>
    </a-collapse>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, computed,
} from 'vue';
import PatrolInspection, { PatrolPlace, PatrolTask } from '~models/patrol-inspection';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { getPreviewUrl } from '~models/common';
import { getSelectColumns } from '../config';

export default defineComponent({
  components: {},
  props: {
    defaultData: {
      type: Object as PropType<PatrolTask>,
      default: () => ({}),
    },
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    taskStatusOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  setup(props) {
    const patrolInspectionInessInstance = new PatrolInspection();
    const {
      inspectionTypes, defaultData,
    } = toRefs(props);
    const details = ref<PatrolTask>();
    const activeKey = ref<string[]>(['1', '2']);
    const options = ref<DictionaryMap<['INSPECTION_POINT_STATUS']>>({
      INSPECTION_POINT_STATUS: [],
    });
    const init = async () => {
      // console.log('xxxx--------');
      details.value = await patrolInspectionInessInstance.patrolTaskRotateFindById(defaultData.value.id as string);
      options.value = await useHashMapDic<['INSPECTION_POINT_STATUS']>(['INSPECTION_POINT_STATUS']);
    };
    init();
    return {
      columns: getSelectColumns(inspectionTypes.value, false),
      details,
      getPreviewUrl,
      activeKey,
      options,
      // executionRule,
    };
  },
});
</script>
<style lang="scss" scoped>
.item {
    display: flex;
    justify-content: flex-start;
    padding: 0 0 26px 6px;
    color: #999;

    .label {
        width: 160px;
        color: #000;
    }
}

.point-details {
    max-height: 24vh;
    overflow: auto;
    font-size: 16px;
}
</style>
