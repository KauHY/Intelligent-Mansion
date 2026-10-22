<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-02 14:28:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 16:52:37
-->
<template>
  <div class="details">
    <div class="item">
      <div class="label">
        巡检点ID:
      </div>
      <div>{{ patrolLineInfo?.id }}</div>
    </div>
    <div class="item">
      <div class="label">
        关联园区:
      </div>
      <div>{{ patrolLineInfo?.parkOrgName }}</div>
    </div>
    <div class="item">
      <div class="label">
        路线名称:
      </div>
      <div>{{ patrolLineInfo?.circuitName }}</div>
    </div>
    <div class="item">
      <div class="label">
        巡检类型:
      </div>
      <div>{{ inspectionTypes[patrolLineInfo?.type||999]?.label }}</div>
    </div>
    <div class="item">
      <div class="label">
        路线顺序:
      </div>
      <div>{{ patrolLineInfo?.orderly?'有序':'无序' }}</div>
    </div>
    <div class="item">
      <div class="label">
        备注:
      </div>
      <div>{{ patrolLineInfo?.remark }}</div>
    </div>
    <v-table
      row-key="id"
      bordered
      :columns="columns"
      :data-source="patrolLineInfo?.points"
      :scroll="{y:'400px'}"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs,
} from 'vue';
import PatrolInspection, { PatrolPlace, PatrolLine } from '~models/patrol-inspection';
import { getSelectColumns } from '../config';

export default defineComponent({
  components: {},
  props: {
    defaultData: {
      type: Object as PropType<PatrolLine>,
      default: () => ({}),
    },
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  setup(props) {
    const patrolInspectionInessInstance = new PatrolInspection();
    const { inspectionTypes, defaultData } = toRefs(props);
    const patrolLineInfo = ref<Partial<PatrolLine>>({ points: [] });
    const init = async () => {
      patrolLineInfo.value = await patrolInspectionInessInstance.patrolLineFindById(defaultData.value.id);
    };
    init();
    return {
      columns: getSelectColumns(inspectionTypes.value, false),
      patrolLineInfo,
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
</style>
