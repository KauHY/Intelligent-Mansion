<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-28 14:54:47
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
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
          @click="currentVisible=false "
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
// import addUser from './add-component.vue';
// import resetPwd from './reset-pwd.vue';
// import { useRegister } from '../composition/useForm';
// import { companyTypeNames } from '../config';
// import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
// import useOpts from '../composition/useOpts';
import VisitorsManagement, { RegisterInfo } from '~models/visitors-management';
import Details from '../../components/details.vue';

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
    const contentCp = shallowRef();
    const details = ref<RegisterInfo>();
    const init = async () => {
      details.value = await visitorsManagementInstance.visitorDetails(defaultData.value.id);
      contentCp.value = Details;
    };
    init();
    return {
      currentVisible,
      details,
      contentCp,
    };
  },
});
</script>
