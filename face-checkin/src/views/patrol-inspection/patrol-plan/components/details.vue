<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-02 14:28:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-06 19:33:16
-->
<template>
  <div class="details">
    <div class="item">
      <div class="label">
        计划ID:
      </div>
      <div>{{ patrolPlanInfo?.id }}</div>
    </div>
    <div class="item">
      <div class="label">
        关联园区:
      </div>
      <div>{{ patrolPlanInfo?.parkOrgName }}</div>
    </div>
    <div class="item">
      <div class="label">
        计划名称:
      </div>
      <div>{{ patrolPlanInfo?.planName }}</div>
    </div>
    <div class="item">
      <div class="label">
        巡检类型:
      </div>
      <div>{{ inspectionTypes[patrolPlanInfo?.type||999]?.label }}</div>
    </div>
    <div class="item">
      <div class="label">
        巡检路线:
      </div>
      <div>{{ patrolPlanInfo?.circuitName }}</div>
    </div>
    <div class="item">
      <div class="label">
        执行周期:
      </div>
      <div>{{ cycleTypeOpts[patrolPlanInfo?.cycleType]?.label }}</div>
      【<div>{{ executionRule }}</div>】
    </div>
    <div class="item">
      <div class="label">
        执行时间:
      </div>
      <div>{{ patrolPlanInfo?.executionTimeStart }}—{{ patrolPlanInfo?.executionTimeEnd }}</div>
    </div>
    <div class="item">
      <div class="label">
        生效日期:
      </div>
      <div>{{ patrolPlanInfo?.effectDateStart }}—{{ patrolPlanInfo?.effectDateEnd }}</div>
    </div>
    <div class="item">
      <div class="label">
        备注:
      </div>
      <div>{{ patrolPlanInfo?.remark }}</div>
    </div>
    <div class="item">
      <div class="label">
        巡检人:
      </div>
      <div>
        <a-tag
          v-for="(item,index) in patrolPlanInfo?.employees"
          :key="index"
          color="green"
        >
          {{ item.label }}
        </a-tag>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, computed,
} from 'vue';
import PatrolInspection, { PatrolPlace, PatrolPlan } from '~models/patrol-inspection';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { getSelectColumns } from '../config';

export default defineComponent({
  components: {},
  props: {
    defaultData: {
      type: Object as PropType<PatrolPlan>,
      default: () => ({}),
    },
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    cycleTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  setup(props) {
    const patrolInspectionInessInstance = new PatrolInspection();
    const { inspectionTypes, defaultData } = toRefs(props);
    const patrolPlanInfo = ref<PatrolPlan>();
    const weekDays = ref<SelectOptionsMap>({});
    const executionRule = computed(() => {
      // patrolPlanInfo.value?.executionRule
      if (!patrolPlanInfo.value) return '';
      const { executionRule, cycleType } = patrolPlanInfo.value;
      if (cycleType === 0) { // 按天
        const canDelayText = cycleType ? '顺延' : '不顺延';
        return `每${executionRule}天一次,节假日${canDelayText}`;
      }
      if (!executionRule) return '';
      const weekDayTexts = executionRule.split(',').map((key) => weekDays.value[Number(key)]?.label);
      return `每周${weekDayTexts.join()}循环一次`;
      // return 1;
    });
    const init = async () => {
      // console.log('xxxx--------');
      patrolPlanInfo.value = await patrolInspectionInessInstance.patrolPlanFindById(defaultData.value.id as string);
      weekDays.value = (await useHashMapDic<['Week_Day']>(['Week_Day'])).Week_Day;
    };
    init();
    return {
      columns: getSelectColumns(inspectionTypes.value, false),
      patrolPlanInfo,
      executionRule,
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
