<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 17:56:00
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-28 20:25:37
-->
<template>
  <div class="textarea">
    <div class="name">
      <div class="left">
        {{ index+1 }}、 <v-edit-text v-model:value="questionnaireConf.name" />
      </div>
      <span class="remove-icon-box">
        <slot name="remoteComponentIcon" />
      </span>
    </div>
    <div class="input-content">
      <a-textarea
        :value="questionnaireConf.value"
        show-count
        :maxlength="200"
      />
    </div>
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
.textarea {
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
