<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 14:03:44
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="780"
    class="operate-dialog"

    @ok="confirm"
  >
    <div class="view-house-apply-details">
      <a-form
        :label-col="{span:4}"
      >
        <a-form-item
          v-show="type!=='add'"
          label="巡检点ID"
        >
          <a-input
            v-model:value="modelRef.id"
            :maxlength="30"
            :disabled="true"
          />
        </a-form-item>
        <a-form-item
          label="关联园区"
          v-bind="type==='details'?'':validateInfos.parkOrgId"
        >
          <v-select
            v-model:value="modelRef.parkOrgId"
            :disabled="type!=='add'"
            placeholder="请选择关联园区"
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
          label="巡检点名称"
          v-bind="type==='details'?'':validateInfos.pointName"
        >
          <a-input
            v-model:value="modelRef.pointName"
            :maxlength="30"
            :disabled="type==='details'"
            placeholder="请输入巡检点名称"
          />
        </a-form-item>
        <a-form-item
          label="巡检点类型"
          v-bind="type==='details'?'':validateInfos.type"
        >
          <a-select
            v-model:value="modelRef.type"
            :disabled="type==='details'"
            placeholder="请选择巡检点类型"
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
          v-bind="type==='details'?'':validateInfos.location"
          label="巡检位置"
        >
          <v-select
            v-if="modelRef.type===1"
            v-model:value="modelRef.location"
            :disabled="type==='details'"
            placeholder="请选择巡检设备"
          >
            <a-select-option
              v-for="opt in equipmentList"
              :key="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
          <a-input
            v-else
            v-model:value="modelRef.location"
            :maxlength="30"
            :disabled="type==='details'"
            placeholder="请输入巡检位置"
          />
        </a-form-item>
        <a-form-item label="备注">
          <a-textarea
            v-model:value="modelRef.remark"
            :disabled="type==='details'"
            :rows="4"
            :maxlength="99"
            placeholder="请输入备注"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          {{ type==="details"?'关闭':'取消' }}
        </a-button>
        <a-button
          v-if="type!=='details'"
          type="primary"
          @click="confirm"
        >
          确认
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import PatrolInspection, {
  PatrolPlace,
} from '~models/patrol-inspection';
import Park from '~models/park';
import { useAddPatrolPlace } from '../composition/useFormConf';
// import houseCardList from './house-card-list.vue';

export default defineComponent({
  name: 'OperateDialog',
  props: {
    title: {
      type: String,
      default: '',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<PatrolInspection>,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
    orderType: { // 类型 报事|报修
      type: Number as PropType<1|2>,
      default: 1,
    },
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const patrolInspectionInessInstance = new PatrolInspection();
    const {
      type, visible, orderType, defaultData,
    } = toRefs(props);
    const operateType = type.value as 'add'|'other'|'follow'|'remind'|'convertToCustomer';
    const cp = shallowRef();

    // 弹框展示隐藏
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });
    const {
      validate, validateInfos, modelRef, fill,
    } = useAddPatrolPlace();
    const parkList = ref<SelectOptions>();
    const equipmentList = ref<SelectOptions>(); // 没接口
    const init = async () => {
      parkList.value = await new Park().searchParkListName();
      if (['edit', 'details'].includes(type.value)) {
        fill(defaultData.value);
      }
    };

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      try {
        await validate();
        if (type.value === 'edit') {
          await patrolInspectionInessInstance.patrolPlaceEdit(modelRef);
        } else {
          await patrolInspectionInessInstance.patrolPlaceAdd(modelRef);
        }
        emit('operate:success');
        message.success('操作成功');
        currentVisible.value = false;
      } catch (error) {
        message.warning('操作失败');
        console.error(error, 'error');
      }
    };
    onMounted(() => {
      // fill();
      // if (type.value === 'edit') {
      //   // parkOrgIdChange(modelRef.parkOrgId, false);
      // }
      // jobOpts.value =
      init();
    });
    return {
      currentVisible,
      validateInfos,
      modelRef,
      parkList,
      equipmentList,
      confirm,
      // cp,
    };
  },
});
</script>
<style lang="less" scoped>
.view-house-apply-details {
    .item {
        display: flex;
        justify-content: flex-start;
        margin-bottom: 30px;

        .label {
            width: 160px;
            margin-right: 40px;
            text-align: right;
        }
    }
}
</style>
