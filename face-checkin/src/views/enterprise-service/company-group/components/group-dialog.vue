<!--
 * @Author: your name
 * @Date: 2022-04-14 10:37:28
 * @LastEditTime: 2022-04-20 16:35:29
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \wisdom-park\src\views\enterprise-service\company-group\components\group-dialog.vue
-->
<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
    class="operate-dialog"
    @ok="confirm"
  >
    <a-form
      :model="modelRef"
      :label-col="{ span: 5 }"
    >
      <a-form-item
        label="群组标签名称"
        v-bind="validateInfos.label"
      >
        <a-input
          v-model:value="modelRef.label"
          placeholder="请输入群组标签名称"
          :maxlength="30"
        />
      </a-form-item>
    </a-form>
  </v-modal>
</template>
<script>
import { message } from 'ant-design-vue';
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watch,
} from 'vue';
import { string } from 'mathjs';
import CompanyGroupService, { LaneInfo, TreeNodeItem } from '~models/company-group';
import { groupAdd } from '../composition/groupFrom';

const serviceRequest = new CompanyGroupService();
export default {
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object,
      default: () => ({}),
    },
    title: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: '',
    },
    parkOpts: {
      type: Object,
      default: () => ({}),
    },
    parkPop: {
      type: Object,
      default: () => ({}),
    },
    defaultGroupData: {
      type: Object,
      default: () => ({}),
    },
    parkOrgId: {
      type: String,
      default: '',
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { parkOpts, type, visible } = toRefs(props);
    const isReady = ref(false);
    const { defaultGroupData, parkOrgId } = toRefs(props);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return ['edit'].includes(type.value) ? isReady.value && visible.value : visible.value;
      },
    });
    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = groupAdd();

    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'groupAdd') {
          await serviceRequest.createmultistage(modelRef);
        } else {
          await serviceRequest.modifymultistage(modelRef);
          console.log('树形编辑__________________', type);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error) => {
        message.warning('操作失败');
      });
    };
    watch(defaultGroupData, (val) => {
      console.log('监听数据变化', val);
      // init();
      fill(val);
      Object.entries(val).forEach(([key]) => {
        console.log('key', val[key]);
        modelRef[key] = val[key];
      });
    }, { deep: true });
    const init = () => {
      console.log('defaultGroupData__------------', defaultGroupData.value);
      fill(defaultGroupData.value);
      isReady.value = true;
    };
    init();
    return {
      currentVisible,
      confirm,
      modelRef,
      validateInfos,

    };
  },
};
</script>
