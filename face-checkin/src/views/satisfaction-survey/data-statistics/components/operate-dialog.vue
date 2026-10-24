<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-31 16:35:06
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="820"
    class="operate-dialog"
    @ok="confirm"
  >
    <design-questionnaire
      v-if="!loading"
      hidden-back
      disabled
      :default-component-conf="details.editors"
    >
      <template #title>
        {{ details.title }}
      </template>
      <template #description>
        {{ details.description }}
      </template>
    </design-questionnaire>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect,
} from 'vue';
import { message } from 'ant-design-vue';
import CompanyMangement from '~models/company-mangement';
import SatisfactionSurvey, { QuestionnaireInfo, QuestionnaireSet, StatisticsListInfo } from '~models/satisfaction-survey';

import DesignQuestionnaire, { QuestionnaireConf } from '~components/design-questionnaire';

// import { useQuestionnaireSet } from '../composition/useForm';

const satisfactionSurveyInstance = new SatisfactionSurvey();
export default defineComponent({
  name: 'OperateDialog',
  components: { DesignQuestionnaire },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<StatisticsListInfo>,
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
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const { type, visible, defaultData } = toRefs(props);

    // const isReady = ref(false);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    const confirm = () => {
      currentVisible.value = false;
    };

    // 初始化
    const details = ref<Partial<QuestionnaireInfo>>({});
    const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    const loading = ref(true);
    // const defaultOptions = ref<SelectOptions>([]);
    const init = async () => {
      details.value = await satisfactionSurveyInstance.questionnaireConfAndResultDetails(defaultData.value.questionnaireSubmissionId);
      const { editors, editorResults } = details.value;
      if (editors && editorResults) {
        Object.entries(editors).forEach(([index, item]) => {
          // debugger;
          const key = item.id as string;
          const result = editorResults[key] as any;
          if (result || result === 0) {
            item.value = result;
          }
        });
      }
      console.log(details.value, '----details');
      loading.value = false;
    };
    init();
    return {
      currentVisible,
      details,
      confirm,
      // modelRef,
      // validateInfos,
      // companyOpts,
      // parkOrgChange,
      // isChooseTarget,
      defaultEmployeeSelectOpts,
      loading,
      // defaultOptions,
      // visitorClientTypeChange,
    };
  },
});
</script>
