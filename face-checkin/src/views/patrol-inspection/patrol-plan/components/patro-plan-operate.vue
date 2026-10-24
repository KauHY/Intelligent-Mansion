<!--
 * @Description: 巡检路线操作
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-30 16:37:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-06 15:21:40
-->
<template>
  <a-form
    :label-col="{span:3}"
  >
    <a-form-item
      label="关联园区"
      v-bind="validateInfos.parkOrgId"
    >
      <v-select
        v-model:value="modelRef.parkOrgId"
        placeholder="请选择关联园区"
        :disabled="type==='edit'"
        @change="getPatroLineList"
      >
        <a-select-option
          v-for="opt in parkList"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item
      label="计划名称"
      v-bind="validateInfos.planName"
    >
      <a-input
        v-model:value="modelRef.planName"
        :maxlength="30"
        placeholder="请输入计划名称"
      />
    </a-form-item>
    <a-form-item
      label="巡检类型"
      v-bind="validateInfos.type"
    >
      <a-select
        v-model:value="modelRef.type"
        placeholder="请选择巡检类型"
        @change="getPatroLineList"
      >
        <a-select-option
          v-for="opt in inspectionTypes"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </a-select>
    </a-form-item>
    <a-form-item
      label="巡检路线"
      v-bind="validateInfos.circuitId"
    >
      <a-select
        v-model:value="modelRef.circuitId"
        placeholder="请选择巡检路线"
        :disabled="!modelRef.type||!modelRef.parkOrgId"
      >
        <a-select-option
          v-for="opt in patroLineList"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </a-select>
    </a-form-item>
    <div class="item-box">
      <a-form-item
        label="执行周期"
        class="cycle-type"
        :label-col="{span:8}"
        required
        v-bind="validateInfos.cycleType"
      >
        <a-select
          v-model:value="modelRef.cycleType"
          placeholder="请选择执行周期"
          @change="modelRef.executionRule=''"
        >
          <a-select-option
            v-for="opt in cycleTypeOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <div class="execution-rule">
        <template v-if="modelRef.cycleType===1">
          <a-form-item
            v-bind="validateInfos.executionRule"
          >
            <a-checkbox-group
              v-model:value="checkedWeeks"
              :options="Object.values(options['Week_Day'])"
            />
          </a-form-item>
        </template>
        <template v-else>
          <a-form-item
            label=""
            v-bind="validateInfos.executionRule"
          >
            每
            <a-input
              v-model:value="modelRef.executionRule"
              :maxlength="30"
              style="width: 100px;"
              placeholder="请输入循环天数"
            />&nbsp;天循环一次
          </a-form-item>
          <!-- <a-form-item class="can-delay">
            <a-checkbox v-model:checked="modelRef.canDelay">
              节假日是否顺延
            </a-checkbox>
          </a-form-item> -->
        </template>
      </div>
    </div>
    <a-form-item
      v-bind="validateInfos.executionTimeStart"
      label="执行时间"
    >
      <v-range-time
        v-model:startTime="modelRef.executionTimeStart"
        v-model:endTime="modelRef.executionTimeEnd"
      />
    </a-form-item>
    <a-form-item
      label="生效日期"
      v-bind="validateInfos.effectDateEnd"
    >
      <a-range-picker
        v-model:value="effectDateTimes"
        value-format="YYYY-MM-DD"
      />
    </a-form-item>
    <a-form-item label="备注">
      <a-textarea
        v-model:value="modelRef.remark"
        :rows="4"
        :maxlength="99"
        placeholder="请输入备注"
      />
    </a-form-item>
  </a-form>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, toRefs, computed,
} from 'vue';
import { message } from 'ant-design-vue';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import PatrolInspection, { PatrolPlan } from '~models/patrol-inspection';
import Park from '~models/park';
import { useAddPatrolPlan } from '../composition/useFormConf';
import { weeks } from '../config';

export default defineComponent({
  props: {
    defaultData: {
      type: Object as PropType<PatrolPlan>,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
    cycleTypeOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  setup(props) {
    const { defaultData, type, cycleTypeOpts } = toRefs(props);
    const { modelRef, validateInfos, fill } = useAddPatrolPlan();
    const parkList = ref<SelectOptions>([]);
    const patroLineList = ref<SelectOptions>([]);
    const options = ref<DictionaryMap<['Week_Day']>>({
      Week_Day: [],
    });
    const checkedWeeks = computed({
      set(days:number[]) {
        // console.log(days, '---days');
        modelRef.executionRule = days.join(',');
      },
      get() {
        return modelRef.cycleType === 1 && modelRef.executionRule ? modelRef.executionRule.split(',').map((val) => Number(val)) : [];
      },
    });
    const effectDateTimes = computed(({
      set([effectDateStart, effectDateEnd]:string[]) {
        if (effectDateStart && effectDateEnd) {
          if (new Date(effectDateEnd).getTime() < Date.now()) {
            message.error('生效结束时间不能晚于当前时间');
            return;
          }
          modelRef.effectDateStart = effectDateStart;
          modelRef.effectDateEnd = effectDateEnd;
        }
      },
      get() {
        const { effectDateStart, effectDateEnd } = modelRef;
        return effectDateStart && effectDateEnd ? [effectDateStart, effectDateEnd] : [];
      },
    }));
    const getPatroLineList = async () => {
      if (!modelRef.type || !modelRef.parkOrgId) return;
      patroLineList.value = await new PatrolInspection().patrolLineLinkOpts({
        parkOrgId: modelRef.parkOrgId,
        type: modelRef.type,
      });
    };
    // 执行范围时间限制
    const timeChange = (time:string, type:'executionTimeStart'|'executionTimeEnd') => {
      const startTime = type === 'executionTimeStart' ? time : modelRef.executionTimeStart;
      const endTime = type === 'executionTimeEnd' ? time : modelRef.executionTimeEnd;
      const startTimeStr = startTime ? startTime.split(':') : [];
      const entTimeStr = endTime ? endTime.split(':') : [];
      const startNum = Number(`${startTimeStr[0]}.${startTimeStr[1]}`);
      const endNum = Number(`${entTimeStr[0]}.${entTimeStr[1]}`);
      debugger;
      if (endNum - startNum < 0) {
        message.warning('执行开始时间不能晚于于执行结束时间，请重新选择');
        return;
      }
      modelRef[type] = time;
    };
    const init = async () => {
      parkList.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['Week_Day']>(['Week_Day']);
      if (type.value === 'edit') {
        fill(defaultData.value);
        getPatroLineList();
      }
    };
    init();
    return {
      modelRef,
      validateInfos,
      parkList,
      patroLineList,
      weeks,
      checkedWeeks,
      getPatroLineList,
      timeChange,
      effectDateTimes,
      options,
    };
  },
});
</script>
<style lang="scss" scoped>
.item-box {
    display: flex;
    align-items: center;
    justify-content: flex-start;

    .cycle-type {
        flex: 35%;
    }

    .execution-rule {
        display: flex;
        flex: 60%;
        align-items: center;
        margin-left: 20px;

        >.can-delay {
            margin-left: 50px;
        }
    }
}
</style>
