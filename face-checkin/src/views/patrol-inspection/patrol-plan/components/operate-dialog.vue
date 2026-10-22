<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 16:22:53
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
      :cycle-type-opts="cycleTypeOpts"
      :use-link-employee="useLinkEmployee"
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

import { message } from 'ant-design-vue';
import PatrolInspection, { PatrolPlan, LinkEmployee } from '~models/patrol-inspection';
import { useAddPatrolPlan, useLinkEmployee } from '../composition/useFormConf';
import patroLineOperate from './patro-plan-operate.vue';
import linkEmployee from './link-employee.vue';
import details from './details.vue';
// import houseCardList from './house-card-list.vue';

export default defineComponent({
  name: 'OperateDialog',
  components: { patroLineOperate, linkEmployee },
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
      type: Object as PropType<PatrolPlan>,
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
    cycleTypeOpts: {
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
    const operateType = type.value as 'add'|'edit'|'details';
    const cp = shallowRef();
    const cpConf = {
      add: patroLineOperate,
      edit: patroLineOperate,
      link: linkEmployee,
      details,
    };
    cp.value = cpConf[type.value as 'add'|'edit'];
    // 弹框展示隐藏
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });
    const useForm = type.value === 'link' ? useLinkEmployee : useAddPatrolPlan;
    const {
      validate,
      modelRef,
    } = useForm();
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
        const messageTost = type.value === 'edit' ? '修改后的巡检计划下次生效' : '操作成功';
        if (type.value === 'edit') {
          await patrolInspectionInessInstance.patrolPlanEdit(modelRef as PatrolPlan);
        } else if (type.value === 'add') {
          await patrolInspectionInessInstance.patrolPlanAdd(modelRef as PatrolPlan);
        } else if (type.value === 'link') {
          const { employeeIds } = modelRef as LinkEmployee;
          await patrolInspectionInessInstance.patrolPlanLinkEmployees({
            employeeIds,
            planId: defaultData.value.id as string,
          });
          // await patrolInspectionInessInstance.patrolPlanLinkEmployees(modelRef as LinkEmployee);
          // patrolInspectionInessInstance.patrolLineLinkPlace(modelRef as PatrolLineLinkPlaceParams);
        }
        message.success(messageTost);
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
