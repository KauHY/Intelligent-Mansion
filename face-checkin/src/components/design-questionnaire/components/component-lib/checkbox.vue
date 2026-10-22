<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 17:56:00
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-28 20:42:00
-->
<template>
  <div class="checkbox">
    <div class="name">
      <div class="left">
        {{ index+1 }}、 <v-edit-text v-model:value="questionnaireConf.name" />
      </div>
      <span class="remove-icon-box">
        <slot name="remoteComponentIcon" />
      </span>
    </div>
    <a-checkbox-group
      :value="questionnaireConf.value"
      style="width: 100%;"
    >
      <div
        v-for="(opt,inx) in questionnaireConf.elementContent"
        :key="inx"
        class="item"
      >
        <div class="left">
          <a-checkbox :value="inx" />&nbsp;&nbsp;
          <v-edit-text
            v-model:value="questionnaireConf.elementContent[inx]"
            display="block"
          />
        </div>
        <template v-if="!disabled">
          <img
            v-if="inx===questionnaireConf.elementContent.length-1"
            class="add"
            src="~assets/images/design-questionnaire/add_icon.png"
            @click="add"
          >
          <img
            v-if="questionnaireConf.elementContent.length>1"
            class="remove"
            src="~assets/images/design-questionnaire/remove_icon.png"
            @click="remove(inx)"
          >
        </template>
      </div>
    </a-checkbox-group>
  </div>
</template>
<script lang='ts'>

import {
  reactive, ref, defineComponent, PropType, toRefs,
} from 'vue';
import { QuestionnaireConf } from '../../type';

// const radioStyle = {
//   display: 'block',
//   height: '30px',
//   lineHeight: '30px',
// };
// console.log('执行了------');
export default defineComponent({
  props: {
    index: {
      type: Number,
      default: 0,
    },
    questionnaireConf: {
      type: Object as PropType<QuestionnaireConf>,
      default: () => ({}),
    },
    disabled: { // 只读
      type: Boolean,
      default: false,
    },
  },
  setup(props) {
    const { questionnaireConf, disabled } = toRefs(props);
    const value = ref<string[]>([]);
    const add = () => {
      if (Array.isArray(questionnaireConf.value.elementContent)) {
        questionnaireConf.value.elementContent?.push('');
      }
    };
    const remove = (inx:number) => {
      if (Array.isArray(questionnaireConf.value.elementContent)) {
        questionnaireConf.value.elementContent.splice(inx, 1);
      }
    };
    return {
      add,
      remove,
      value,
    };
  },
});
</script>
<style lang='scss' scoped>
.checkbox {
    padding: 4px;
    cursor: pointer;

    .name {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        font-size: 16px;
        font-weight: 500;

        .left {
            display: flex;
            align-items: center;
            margin-right: 20px;
        }

        .remove-icon-box {
            visibility: hidden;
        }
    }

    &:hover {
        .name .remove-icon-box {
            visibility: inherit;
        }
    }

    .item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 12px;
        margin-left: 15px;
        font-size: 12px;

        &:hover {
            color: #1175e2;

            img.remove {
                visibility: inherit;
            }
        }

        .left {
            display: flex;
            flex: 1;
            align-items: center;
            justify-content: flex-start;
        }
    }

    img {
        width: 16px;
        height: 16px;
        margin-right: 10px;
        cursor: pointer;

        &.remove {
            visibility: hidden;
        }
    }
}
</style>
