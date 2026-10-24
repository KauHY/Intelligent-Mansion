<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 17:56:00
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-28 20:24:09
-->
<template>
  <div class="input">
    <div class="name">
      <div class="left">
        {{ index+1 }}、 <v-edit-text v-model:value="questionnaireConf.name" />
      </div>
      <span class="remove-icon-box">
        <slot name="remoteComponentIcon" />
      </span>
    </div>
    <div class="input-content">
      <a-input
        placeholder="请输入内容"
        :value="questionnaireConf.value"
      />
    </div>
    <!-- <a-radio-group
      v-model:value="value"
      style="width: 100%;"
    >
      <div
        v-for="(opt,inx) in questionnaireConf.elementContent"
        :key="inx"
        class="item"
      >
        <div class="left">
          <a-radio :value="inx" />
          <v-edit-text
            v-model:value="questionnaireConf.elementContent[inx]"
            display="block"
          />
        </div>
        <img
          v-if="inx===questionnaireConf.elementContent.length-1"
          class="add"
          src="~assets/images/design-questionnaire/add_icon.png"
          @click="add"
        >
        <img
          class="remove"
          src="~assets/images/design-questionnaire/remove_icon.png"
          @click="remove(inx)"
        >
      </div>
    </a-radio-group> -->
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
  },
  setup(props) {
    // const { questionnaireConf } = toRefs(props);
    const value = ref('');
    return {
      value,
    };
  },
});
</script>
<style lang='scss' scoped>
.input {
    padding: 10px;
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
            margin-right: 10px;
        }

        .remove-icon-box {
            visibility: hidden;
        }
    }

    .input-content {
        margin: 10px 0 0 26px;
    }

    &:hover {
        .name .remove-icon-box {
            visibility: inherit;
        }
    }
}
</style>
