<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-28 16:19:11
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-29 15:33:08
-->
<template>
  <div class="content-section">
    <div class="analysis-report content-scroll">
      <div class="survey">
        <div>
          目标个人数：<span class="blue">{{ details.targetCount }}</span>
        </div>
        <div>收集量： <span class="blue">{{ details.actualCount }} ({{ getPercent(details.percent) }}%)</span> </div>
      </div>
      <div class="report-box">
        <div
          v-for="(questionnaire,index) in details.analysis"
          :key="questionnaire.id"
          class="report-item"
        >
          <div class="name">
            {{ index+1 }}、{{ questionnaire.name }}
          </div>
          <div class="reulst">
            <div
              v-if="['checkbox','radio'].includes(questionnaire.elementType)"
              class="choose"
            >
              <div
                v-for="(opt,inx) in questionnaire.choices"
                :key="inx"
                class="choose-item"
              >
                <div class="label">
                  {{ opt.label }}
                </div>
                <div class="right">
                  <a-progress
                    type="line"
                    :width="30"
                    class="progress"
                    :percent="Number(getPercent(opt.percent))"
                    :format="percent => `${percent}%`"
                  />
                  <span>{{ opt.count }}次</span>
                </div>
              </div>
            </div>
            <div
              v-else-if="['input','textarea'].includes(questionnaire.elementType)"
              class="text"
            >
              <span
                class="blue link"
                @click="goDetails('text',questionnaire.id)"
              >答案详情</span>
            </div>
            <div v-else>
              <span
                class="blue link"
                @click="goDetails('file', questionnaire.id)"
              >附件详情</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs,
} from 'vue';
import { useRouter } from 'vue-router';
import SatisfactionSurvey, { QuestionnaireAnalysisReport, QuestionnaireSet } from '~models/satisfaction-survey';

const satisfactionSurveyInstance = new SatisfactionSurvey();

export default defineComponent({
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const { id } = toRefs(props);
    const rotuer = useRouter();
    const getPercent = (num:number|undefined) => (num ? (num * 100).toFixed(2) : '0.00');
    const details = ref<Partial<QuestionnaireAnalysisReport>>({});
    const init = async () => {
      details.value = await satisfactionSurveyInstance.questionnaireAnalysisReport(id.value);
    };
    const goDetails = (type:string, editorId:string) => {
      rotuer.push({ path: '/satisfaction-survey/answer-details', query: { type, editorId, questionnaireId: id.value } });
    };
    init();
    return {
      details,
      getPercent,
      goDetails,
    };
  },
});
</script>
<style lang="less" scoped>
.analysis-report {
    padding: 0 20px;

    .survey {
        display: flex;
        margin-bottom: 20px;
        font-size: 24px;
        font-weight: 500;

        >div:first-child {
            padding-right: 60px;
        }
    }

    .report-box {
        .report-item {
            padding-bottom: 16px;
            margin-bottom: 24px;
            border-bottom: 1px dashed #d6d8de;

            .reulst {
                padding: 24px 0 0 24px;

                .choose-item {
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    margin-bottom: 24px;

                    .label {
                        flex: 1;
                    }

                    .right {
                        display: flex;
                        align-items: center;
                        justify-content: space-between;
                        width: 150px;

                        .progress {
                            flex: 1;
                        }

                        span {
                            margin-left: 20px;
                        }
                    }
                }
            }
        }
    }

    .blue {
        color: #1175e2;
    }

    .link {
        cursor: pointer;

        &:hover {
            text-decoration: underline;
        }
    }
}
</style>
