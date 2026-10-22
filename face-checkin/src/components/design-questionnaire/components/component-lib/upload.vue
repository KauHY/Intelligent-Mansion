<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 17:56:00
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-28 20:26:04
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
      <v-upload
        placeholder="请上传文件"
        :disabled="isAled"
        accept=".png,.jpg,.jpeg,.gif,.doc,.pdf,.docx,.xls,.xlsx,.ppt,.pptx"
        :value="questionnaireConf.value"
      />
    </div>
  </div>
</template>
<script lang='ts'>

import {
  reactive, ref, defineComponent, PropType, toRefs,
} from 'vue';
import { useRoute } from 'vue-router';
import { boolean } from 'mathjs';
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
    const isAled = ref<Boolean>(false);
    // const { questionnaireConf } = toRefs(props);
    const route = useRoute();
    isAled.value = route.query.type === 'design';
    const value = ref('');
    return {
      value,
      isAled,
    };
  },
});
</script>
<style lang='scss' scoped>
.input {
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
