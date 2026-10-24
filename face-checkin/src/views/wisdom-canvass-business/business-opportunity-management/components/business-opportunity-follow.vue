<!--
 * @Description: 新增商机
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 18:10:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-21 17:30:16
-->
<template>
  <div class="business-opportunity-add">
    <a-form
      :model="modelRef"
      :label-col="{span:4}"
    >
      <a-form-item
        v-bind="validateInfos.chanceStage"
        label="商机阶段"
      >
        <v-select
          v-model:value="modelRef.chanceStage"
          placeholder="请选择商机阶段"
        >
          <template v-for="opt in options.BUSINESS_CHANCE_STAGE">
            <a-select-option
              v-if="opt.value!='7'"
              :key="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </template>
        </v-select>
      </a-form-item>
      <a-form-item
        v-bind="validateInfos.followType"
        label="跟进类型"
      >
        <v-select
          v-model:value="modelRef.followType"
          placeholder="请选择跟进类型"
        >
          <a-select-option
            v-for="opt in options.BUSINESS_FOLLOW_TYPE"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        v-bind="validateInfos.followTime"
        label="跟进时间"
      >
        <a-date-picker
          v-model:value="modelRef.followTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          show-time
          placeholder="请选择跟进时间"
        />
      </a-form-item>
      <a-form-item
        v-bind="validateInfos.followContent"
        label="跟进内容"
      >
        <a-textarea
          v-model:value="modelRef.followContent"
          placeholder="请输入跟进内容"
          :maxlength="999"
        />
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang='ts'>
import {
  ref, defineComponent, PropType, toRefs,
} from 'vue';
import { BusinessInfoFollow } from '~models/wisdom-canvass-business';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { useFollow } from '../composition/useFormConf';

export default defineComponent({
  // components: { employeeSelect },
  props: {
    orderType: {
      type: Number as PropType<1|2>,
      default: 1,
    },
    defaultData: {
      type: Object as PropType<BusinessInfoFollow>,
      default: () => ({}),
    },
  },
  setup(props) {
    const { defaultData } = toRefs(props);
    // const {} = useParkInfoOpts();
    const defaultOptions = ref<SelectOptions>([]);
    // const recordType = computed(() => defaultData.value.recordType);
    const options = ref<DictionaryMap<['BUSINESS_CHANCE_STAGE', 'BUSINESS_FOLLOW_TYPE']>>({
      BUSINESS_CHANCE_STAGE: [],
      BUSINESS_FOLLOW_TYPE: [],
    });
    const {
      modelRef,
      validateInfos,
      // fill,
    } = useFollow();
    const init = async () => {
      options.value = await useHashMapDic<['BUSINESS_CHANCE_STAGE', 'BUSINESS_FOLLOW_TYPE']>(['BUSINESS_CHANCE_STAGE', 'BUSINESS_FOLLOW_TYPE']);
      modelRef.id = defaultData.value.id;
      // fill(props.defaultData);
    };
    init();
    // onMounted(() => {
    // });
    return {
    //   parkOpts,
      modelRef,
      validateInfos,
      defaultOptions,
      // recordType,
      options,
    };
  },
});
</script>
<style lang="scss" scoped>
.business-opportunity-add {
    :deep(.ant-row) {
        display: flex;
        flex-wrap: nowrap;
    }
}
</style>
