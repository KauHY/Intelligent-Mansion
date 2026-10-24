<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-08 16:30:55
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="980"
    class="operate-dialog"

    @ok="confirm"
  >
    <component
      :is="cp"
      :type="type"
      :default-data="defaultData"
      :inspection-types="inspectionTypes"
      :task-status-opts="taskStatusOpts"
      :use-link-employee="useLinkEmployee"
      :checked-max-num="1"
      :is-fill="false"
    />
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

import PatrolInspection, { PatrolTask, PatrolTaskRotateParams } from '~models/patrol-inspection';
import linkEmployee from './link-employee.vue';
import { useLinkEmployee } from '../composition/useFormConf';
// import patroLineOperate from './patro-plan-operate.vue';
import details from './details.vue';
// import houseCardList from './house-card-list.vue';

export default defineComponent({
  name: 'OperateDialog',
  components: { linkEmployee },
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
      type: Object as PropType<PatrolTask>,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
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
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const patrolInspectionInessInstance = new PatrolInspection();
    const {
      type, visible, defaultData,
    } = toRefs(props);
    // const operateType = type.value as 'add'|'edit'|'details';
    const cp = shallowRef();
    const cpConf = {
      link: linkEmployee,
      details,
    };
    cp.value = cpConf[type.value as 'link'|'details'];
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
      validate,
      modelRef,
    } = useLinkEmployee();
    const parkList = ref<SelectOptions>();
    const equipmentList = ref<SelectOptions>(); // 没接口
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      try {
        await validate();
        modelRef.taskId = defaultData.value.id as string;
        await patrolInspectionInessInstance.patrolTaskRotate(modelRef);
        emit('operate:success');
        currentVisible.value = false;
      } catch (error) {
        console.error(error, 'error');
      }
    };
    // onMounted(() => {
    //   init();
    // });
    return {
      currentVisible,
      // validateInfos,
      // modelRef,
      parkList,
      equipmentList,
      confirm,
      cp,
      useLinkEmployee,
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
