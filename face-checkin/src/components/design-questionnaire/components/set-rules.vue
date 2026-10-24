<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 15:10:14
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-07 13:45:54
-->
<template>
  <div class="set_rules">
    <div class="title">
      属性设置
    </div>
    <template v-if="!!tag">
      <div class="tag-type">
        字段类型：<span>{{ componentList[tag]?.name }}</span>
      </div>
      <a-form
        :model="rules"
        :rules="rulesRef"
      >
        <a-form-item v-bind="validateInfos.required">
          <a-radio-group v-model:value="rules.required">
            <a-radio
              :value="true"
            >
              必须
            </a-radio>
            <a-radio
              :value="false"
            >
              非必选
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <!-- 多选项目设置 -->
        <template v-if="tag==='checkbox'">
          <a-form-item
            v-bind="validateInfos.minlength"
          >
            最少选&nbsp;
            <a-input-number
              v-model:value="rules.minlength"
              :min="0"
              placeholder="请输入数字"
            />&nbsp;项
          </a-form-item>
          <a-form-item v-bind="validateInfos.maxlength">
            最多选&nbsp;
            <a-input-number
              v-model:value="rules.maxlength"
              :min="0"
              placeholder="请输入数字"
            />&nbsp;项
          </a-form-item>
        </template>
      </a-form>
    </template>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs,
} from 'vue';
import useForm from '~utils/useForm';
import { componentList } from '../config';
import { ConstraintCondition, ElementType } from '../type';

export default defineComponent({
  props: {
    tag: {
      type: String as PropType<ElementType>,
      default: '',
    },
    rules: {
      type: Object as PropType<ConstraintCondition>,
      default: () => ({}),
    },
  },
  setup(props, { expose }) {
    const { rules } = toRefs(props);
    let copyValidateField:any;
    const rulesRef = reactive({
      required: [{ required: true, message: '请选择是否必填' }],
      minlength: [{
        validator: (rule:unknown, tempValue:{value:number, isAutoTrigger:boolean}) => {
          // debugger;
          const value = tempValue?.value || tempValue;
          if (rules.value.maxlength && value > rules.value.maxlength) return Promise.reject('最少项不能大于最大项目');
          if (!tempValue?.isAutoTrigger) copyValidateField('maxlength', { value: rules.value.maxlength, isAutoTrigger: true }, rulesRef.maxlength);
          return Promise.resolve();
        },
      }],
      maxlength: [{
        validator: (rule:unknown, tempValue:{value:number, isAutoTrigger:boolean}) => {
          const value = tempValue?.value || tempValue;
          if (rules.value.minlength && value < rules.value.minlength) return Promise.reject('最大项不能小于最小项目');
          if (!tempValue?.isAutoTrigger) copyValidateField('minlength', { value: rules.value.minlength, isAutoTrigger: true }, rulesRef.minlength);
          return Promise.resolve();
        },
      }],
    });
    const { validateInfos, validate, validateField } = useForm(rules, rulesRef);
    copyValidateField = validateField;

    expose({ validate });
    return {
      rulesRef,
      componentList,
      validateInfos,
    };
  },
});
</script>
<style lang='scss' scoped>
.set_rules {
    padding: 0 10px;

    .title {
        padding-bottom: 20px;
        font-size: 14px;
        font-weight: 500;
    }

    .tag-type {
        padding-bottom: 16px;
        font-size: 16px;
    }

    :deep(.ant-form .ant-form-item .ant-radio) {
        font-size: 12px;
    }
}
</style>
