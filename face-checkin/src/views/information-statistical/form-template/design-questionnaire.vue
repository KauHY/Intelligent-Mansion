<!--
 * @Description: 设计
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 14:37:39
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 18:57:43
-->
<template>
  <div class="design-questionnaire-page content-section">
    <!-- design-questionnaire-page -->
    <a-skeleton
      :loading="loading"
    >
      <design-questionnaire
        v-if="!loading"
        :disabled="currentDisabled"
        :default-component-conf="questionnaireInfo.editors"
        @save="save"
      >
        <template #title>
          <v-edit-text
            v-model:value="questionnaireInfo.title"
            display="block"
          />
        </template>
        <template #description>
          <v-edit-text
            v-model:value="questionnaireInfo.description"
            display="block"
          />
        </template>
      </design-questionnaire>
    </a-skeleton>
  </div>
</template>
<script lang='ts'>
import { message } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import {
  reactive, ref, defineComponent, PropType, toRefs, computed, nextTick,
} from 'vue';

import DesignQuestionnaire, { QuestionnaireConf } from '~components/design-questionnaire';
import InformationStatistical, { FormTemplateInfo, FormTemplateSet } from '~models/information-statistical';

const informationStatisticalInstance = new InformationStatistical();

// 操作类型
type OperateType = 'details'|'design'
export default defineComponent({
  components: { DesignQuestionnaire },
  props: {
    id: {
      type: String,
      default: '',
    },
    type: {
      type: String as PropType<OperateType>,
      default: '',
    },
  },
  setup(props) {
    const router = useRouter();
    const { type, id } = toRefs(props);

    // 问卷相关逻辑
    const { questionnaireInfo, loading } = useQuestionnaireInfo(type.value, id.value);

    const currentDisabled = computed(() => type.value === 'details');

    const save = async (questionnaireConf:QuestionnaireConf[]) => {
      if (!questionnaireInfo.value.id) {
        message.warn('id不能为空');
        return;
      }
      questionnaireInfo.value.editors = questionnaireConf;
      await informationStatisticalInstance.formTemplateDesign(questionnaireInfo.value as FormTemplateInfo);
      message.success('保存成功！');
      router.go(-1);

      // console.log(questionnaireInfo, '----questionnaireInfo');
    };
    return {
      questionnaireInfo,
      save,
      currentDisabled,
      loading,
    };
  },
});
// 初始化问卷信息
function useQuestionnaireInfo(type:OperateType, id:string) {
  const questionnaireInfo = ref<Partial<FormTemplateInfo>>({});
  const loading = ref(true);
  const init = async () => {
    loading.value = true;
    questionnaireInfo.value = await informationStatisticalInstance.formTemplateFindById(id);
    loading.value = false;
  };
  init();
  return {
    questionnaireInfo,
    loading,
  };
}
</script>
<style lang='scss' scoped>
.design-questionnaire-page {
    padding: 0 20px 20px;
}
</style>
