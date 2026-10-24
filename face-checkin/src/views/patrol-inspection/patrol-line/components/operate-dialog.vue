<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-06 11:12:36
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
import PatrolInspection, { PatrolLine, PatrolLineLinkPlaceParams } from '~models/patrol-inspection';
import { useAddPatrolLine, useAddLinkPlace } from '../composition/useFormConf';
import patroLineOperate from './patro-line-operate.vue';
import linkPlace from './link-place.vue';
import details from './details.vue';
// import houseCardList from './house-card-list.vue';

export default defineComponent({
  name: 'OperateDialog',
  components: { patroLineOperate, linkPlace },
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
      type: Object as PropType<PatrolLine>,
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
      link: linkPlace,
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
    const useForm = type.value === 'link' ? useAddLinkPlace : useAddPatrolLine;
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
        if (type.value === 'edit') {
          await patrolInspectionInessInstance.patrolLineEdit(modelRef as PatrolLine);
        } else if (type.value === 'add') {
          await patrolInspectionInessInstance.patrolLineAdd(modelRef as PatrolLine);
        } else if (type.value === 'link') {
          if (modelRef.pointIds && modelRef.pointIds.length === 0) {
            message.warning('请至少选择一个巡检点');
            return;
          }
          await patrolInspectionInessInstance.patrolLineLinkPlace(modelRef as PatrolLineLinkPlaceParams);
        }
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
