<!--
 * @Description:关联巡检点
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-01 13:39:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-09 17:11:11
-->
<template>
  <div
    class="v-select-employee"
  >
    <div class="tags-box">
      <div

        class="tags"
      >
        <div v-if="Object.keys(checkedEmployees).length">
          <a-tag
            v-for="(item,key) in checkedEmployees"
            :key="key"
            class="tag"
          >
            {{ item.label }}
          </a-tag>
        </div>
        <span
          v-else
          class="placeholder"
        >{{ disabled?'':placeholder }}</span>
      </div>
      <a-button
        :disabled="disabled"
        @click="showModal"
      >
        点击选择
      </a-button>
    </div>
    <a-modal
      v-model:visible="visible"
      :title="title"
      :width="1200"
      @ok="confrim"
    >
      <select-box
        v-if="visible"
        v-model:value="tempCheckedEmployees"
        :company-type="companyType"
        :org-id="orgId"
        :company-ids="companyIds"
        :job-codes="jobCodes"
        :park-org-ids="parkOrgIds"
        :mode="mode"
        :disabled="disabled"
      />
    </a-modal>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, onMounted, watch,
} from 'vue';
import { message, Form } from 'ant-design-vue';
import { CheckedEmployees } from './type';
import selectBox from './components/select-box.vue';

export default defineComponent({
  name: 'VSelectEmployee',
  components: { selectBox },
  props: {
    title: {
      type: String,
      default: '选择员工',
    },
    value: {
      type: [Array, String] as PropType<string[]|string>,
      default: () => ([]),
    },
    companyType: {
      type: Number as PropType<0|1|null>, // 公司类型 物业公司0 | 普通公司1 | null全部公司
      default: null,
    },
    orgId: { // 组织id
      type: String,
      default: '',
    },
    parkOrgIds: { // 园区id
      type: [Array, String] as PropType<string[]|string>,
      default: () => ([]),
    },
    companyIds: {
      type: [Array, String] as PropType<string[]|string>,
      default: () => ([]),
    },
    jobCodes: { // 园区id
      type: [Array, String] as PropType<string[]|string>,
      default: () => ([]),
    },
    defaultSelectOpts: {
      type: Array as PropType<SelectOptions>,
      default: () => ([]),
    },
    placeholder: {
      type: String,
      default: '请选择',
    },
    mode: {
      type: String as PropType<'single'|'multiple'>,
      default: 'multiple',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  emits: {
    'update:value': (checkedEmployeeIds:string[]|string) => true,
    change: (checkedEmployeeIds:string[]|string, checkedEmployees:CheckedEmployees) => true,
  },
  setup(props, { emit }) {
    const formItemContext = Form.useInjectFormItemContext();
    const visible = ref(false);
    const {
      defaultSelectOpts, mode, companyIds,
    } = toRefs(props);
    const checkedEmployees = ref<CheckedEmployees>({});
    const tempCheckedEmployees = ref<CheckedEmployees>({});

    watch(defaultSelectOpts, (values) => { // 初始值发生变动时重新计算
      const temp:CheckedEmployees = {};
      values.forEach((item) => {
        temp[item.value] = item;
      });
      checkedEmployees.value = temp;
    }, { deep: true, immediate: true });

    const confrim = () => {
      // console.log('确定');
      checkedEmployees.value = { ...tempCheckedEmployees.value };
      const keys = Object.keys(checkedEmployees.value);
      const values = mode.value === 'single' ? keys[0] || '' : keys;
      emit('update:value', values);
      emit('change', values, checkedEmployees.value);
      visible.value = false;
      formItemContext.onFieldChange();
    };
    const showModal = () => {
      tempCheckedEmployees.value = { ...checkedEmployees.value };
      visible.value = true;
    };
    return {
      visible,
      confrim,
      checkedEmployees,
      tempCheckedEmployees,
      showModal,
    };
  },
});

</script>
<style lang="scss" scoped>
.v-select-employee {
    .tags-box {
        display: flex;
        align-items: center;
        justify-content: flex-start;

        .tags {
            flex: 1;
            height: 36px;
            min-height: 30px;
            padding: 4px 10px;
            overflow: auto;
            background-color: #fff;
            border: 1px solid #d9d9d9;
            border-right: 0;
            border-radius: 4px 0 0 4px;

            .tag {
                margin-bottom: 4px;
            }

            .placeholder {
                color: #d9d9d9;
            }
        }

        button {
            width: 80px;
            min-width: 76px;
            height: 36px;
            min-height: 30px;
            border-radius: 0 4px 4px 0;
        }
    }
}
</style>
