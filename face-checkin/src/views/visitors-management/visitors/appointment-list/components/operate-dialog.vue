<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-28 11:22:32
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="820"
    class="operate-dialog"
    @ok="confirm"
  >
    <component
      :is="contentCp"
      :details="details"
      :visit-reason-opts="visitReasonOpts"
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
import VisitorsManagement, { RegisterInfo } from '~models/visitors-management';
import Register from './register.vue';
import Details from '../../components/details.vue';
import { useRegister } from '../composition/useForm';
import { companyTypeNames } from '../config';
// import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

// import useOpts from '../composition/useOpts';

const visitorsManagementInstance = new VisitorsManagement();
export default defineComponent({
  name: 'OperateDialog',
  components: {},
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
    visitReasonOpts: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },

  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { type, visible } = toRefs(props);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });
    const { defaultData } = toRefs(props);
    const {
      modelRef,
      validate,
      // validateInfos,
      // fill,
    } = useRegister();

    const contentCp = shallowRef();
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      // const form = props.type === 'resetPwd' ? useResetPwdForm() : useAddCompany();
      // const params = { ...modelRef };

      validate().then(async () => {
        await visitorsManagementInstance.visitorRegister(modelRef);
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    const details = ref<RegisterInfo>();
    const init = async () => {
      // options.value = await useHashMapDic<['VISIT_REASON']>(['VISIT_REASON']);
      details.value = await visitorsManagementInstance.visitorDetails(defaultData.value.id);
      contentCp.value = props.type === 'register' ? Register : Details;
    };
    init();
    // onMounted(() => {
    // });
    return {
      currentVisible,
      confirm,
      modelRef,
      contentCp,
      details,
      // validateInfos,
    };
  },
});
</script>
