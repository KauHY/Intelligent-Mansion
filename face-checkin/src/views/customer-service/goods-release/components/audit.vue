<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-28 14:10:27
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-28 14:13:09
-->
<template>
  <div class="audit">
    <a-form
      :model="modelRef"
      :label-col="{span:5}"
    >
      <a-form-item
        label="审核结果"
        v-bind="validateInfos.checkStatus"
      >
        <a-radio-group v-model:value="modelRef.checkStatus">
          <a-radio :value="2">
            通过
          </a-radio>
          <a-radio :value="3">
            拒绝
          </a-radio>
        </a-radio-group>
      </a-form-item>
      <a-form-item
        label="审核意见"
        v-bind="validateInfos.checkOpinion"
      >
        <a-textarea
          v-model:value="modelRef.checkOpinion"
          placeholder="请输入审核意见"
          :rows="4"
        />
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, toRefs, ref,
} from 'vue';
import { message } from 'ant-design-vue';
import { useGoodsReleaseAudit } from '../composition/useForm';

export default defineComponent({
  props: {
  },
  setup() {
    const {
      modelRef,
      validateInfos,
    } = useGoodsReleaseAudit();
    return {
      modelRef,
      validateInfos,
    };
  },
});
</script>
