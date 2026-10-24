<!--
 * @Description: 设计问卷
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 14:56:47
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-31 17:32:57
-->
<template>
  <div class="design-questionnaire">
    <div class="nav">
      <div
        v-if="!hiddenBack"
        class="left"
      >
        <v-svg
          name="return"
          color="#1175E2"
          @click="$router.go(-1)"
        /> 编辑模板
      </div>
      <div
        v-if="!disabled"
        class="right"
      >
        <a-button
          v-if="!isPreview"
          @click="save"
        >
          保存
        </a-button>
        <a-button @click="preview">
          {{ isPreview? '取消预览':'预览' }}
        </a-button>
      </div>
    </div>
    <section>
      <div
        v-if="!currentDisabled"
        class="left"
      >
        <choose-components
          @addComponent="addComponent"
        />
      </div>
      <div class="content-box">
        <div class="questionnaire-page-title">
          <slot name="title" />
        </div>
        <div class="questionnaire-description">
          <slot name="description" />
        </div>
        <div class="questions">
          <div
            v-for="(componentConf,index) in questionnaireConf"
            :key="index"
            :class="{'questions-item':true, activited: activeIndex === index}"
            @click="chooseComponent(componentConf,index)"
          >
            <component
              :is="components[componentConf.elementType]"
              :index="index"
              :disabled="currentDisabled"
              :questionnaire-conf="componentConf"
            >
              <template #remoteComponentIcon>
                <img
                  v-if="!disabled"
                  class="remove"
                  src="~assets/images/design-questionnaire/remove_icon.png"
                  @click="removeComponen(index)"
                >
              </template>
            </component>
          </div>
        </div>
      </div>
      <div
        v-if="!currentDisabled"
        class="right"
      >
        <set-rules
          ref="setRulesRef"
          :tag="activedQuestionnare.elementType"
          :rules="activedQuestionnare.constraintCondition"
        />
      </div>
    </section>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, computed, Ref, provide, ComputedRef,
} from 'vue';
import { useRoute } from 'vue-router';
import chooseComponents from './components/choose-components.vue';
import setRules from './components/set-rules.vue';
import { components } from './components/component-lib';
import { QuestionnaireConf, ElementType } from './type';
import { defaultConfigValue } from './config';

console.log(components, '----components');
export default defineComponent({
  components: { chooseComponents, setRules },
  props: {
    defaultComponentConf: {
      type: Array as PropType<QuestionnaireConf[]>,
      default: () => ([]),
    },
    disabled: { // 只读
      type: Boolean,
      default: false,
    },
    hiddenBack: {
      type: Boolean,
      default: false,
    },
  },
  emits: {
    save: (questionnaireConf:QuestionnaireConf[]) => true,
    preview: (isPreview:boolean) => true,
  },
  setup(props, { emit }) {
    const { defaultComponentConf, disabled } = toRefs(props);
    provide('parentDisabled', disabled);

    const route = useRoute();
    // console.log(route.query, 'quer');
    // disabled=route.query.type='design'
    // 点击添加组件
    const {
      questionnaireConf,
      addComponent,
      removeComponen,
      activedQuestionnare,
      chooseComponent,
      activeIndex,
    } = useComponent(defaultComponentConf);
    // 预览
    const { isPreview, preview, currentDisabled } = usePreview(disabled);
    // 初始化操作
    // const {} = useInit(currentDisabled,defaultComponentConf)
    // 保存
    const setRulesRef = ref();
    const save = async () => {
      setRulesRef.value.validate().then(() => {
        emit('save', questionnaireConf.value);
      }).catch(() => {
      });
      // console.log(setRulesRef.value);
    };
    return {
      addComponent,
      questionnaireConf,
      components,
      removeComponen,
      chooseComponent,
      activedQuestionnare,
      activeIndex,
      save,
      setRulesRef,
      isPreview,
      currentDisabled,
      preview,
    };
  },
});

// // 初始化操作
// function useInit(currentDisabled:ComputedRef<boolean>) {

// }

// 组件操作
function useComponent(defaultComponentConf:Ref<QuestionnaireConf[]>) {
  const questionnaireConf = ref<QuestionnaireConf[]>([]);
  if (defaultComponentConf.value && Array.isArray(defaultComponentConf.value)) {
    questionnaireConf.value = defaultComponentConf.value.slice();
  }
  const activedQuestionnare = ref<Partial<QuestionnaireConf>>({}); // 当前选中得题目
  const activeIndex = ref<number>();
  const addComponent = (tag:ElementType) => {
    const defaultComponent = defaultConfigValue[tag]() as unknown as any;
    questionnaireConf.value.push(defaultComponent);
    activedQuestionnare.value = defaultComponent;
    activeIndex.value = questionnaireConf.value.length - 1;
    console.log(questionnaireConf.value, '----questionnaireConf');
  };
  const removeComponen = (index:number) => {
    questionnaireConf.value.splice(index, 1);
  };
  const chooseComponent = (componentConf:QuestionnaireConf, index:number) => {
    activedQuestionnare.value = componentConf;
    activeIndex.value = index;
  };
  return {
    questionnaireConf,
    addComponent,
    removeComponen,
    activedQuestionnare,
    chooseComponent,
    activeIndex,
  };
}
// 预览
function usePreview(disabled:Ref<boolean>) {
  const isPreview = ref(false);
  const preview = async () => { // 预览
    isPreview.value = !isPreview.value;
  };
  const currentDisabled = computed(() => disabled.value || isPreview.value);
  return {
    isPreview,
    preview,
    currentDisabled,
  };
}

</script>

<style lang='scss' scoped>
.design-questionnaire {
    display: flex;
    flex-direction: column;
    height: 100%;
    background: #fff;

    .nav {
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 50px;

        .left {
            display: flex;
            align-items: center;
        }

        .right {
            button {
                margin-right: 20px;
            }
        }
    }

    section {
        display: flex;
        flex: 1;
        overflow: hidden;

        .left,
        .right {
            width: 240px;
            padding: 15px 0;
            overflow: auto;
            border: 1px solid #d1d3d9;
        }

        .content-box {
            flex: 1;
            height: 100%;
            padding: 15px;
            margin: 0 20px;
            overflow: auto;
            border: 1px solid #d1d3d9;

            .questionnaire-page-title {
                padding-bottom: 16px;
                font-size: 20px;
                font-weight: 500;
                text-align: center;
            }

            .questionnaire-description {
                padding-bottom: 20px;
                font-size: 14px;
                color: #424656;
                text-align: center;
            }

            .questions-item {
                margin-bottom: 16px;

                &:hover {
                    box-shadow: 1px 0 8px 3px #eaecf1;
                }

                &.activited {
                    background: #eaecf1;
                }
            }
        }
    }

    img.remove {
        width: 16px;
        height: 16px;
        cursor: pointer;
    }
}
</style>
