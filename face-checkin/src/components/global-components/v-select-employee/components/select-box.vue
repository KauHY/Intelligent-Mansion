<!--
 * @Description:关联巡检点
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-01 13:39:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-05 18:19:43
-->
<template>
  <div
    class="link-employee"
  >
    <div class="left">
      <a-tree
        :tree-data="tagTreeData"
        :field-names="{title:'label',key:'value'}"
        :selected-keys="selectCondition.ids"
        @select="select"
      />
    </div>
    <div class="center">
      <employee-list
        :select-condition="selectCondition"
        :job-codes="jobCodes"
      >
        <template #employeeList="{list}">
          <!-- <a-checkbox-group v-model:value="checkedEmployees"> -->
          <!-- 插槽要有父节点，不然生产环境更新会报错 Cannot read properties of null (reading 'parentNode')-->
          <div>
            <div
              v-for="(item,index) in list"
              :key="index"
              class="list-item"
            >
              <a-checkbox
                :value="item.value"
                :disabled="disabledCheckBox||disabled"
                :checked="!!checkedEmployees[item.value]"
                @change="checkedChange($event,item)"
              >
                {{ item.label }}
              </a-checkbox>
            </div>
          </div>
          <!-- </a-checkbox-group> -->
        </template>
      </employee-list>
    </div>
    <div class="right">
      <div class="title">
        已选（{{ Object.keys(checkedEmployees).length }}）人
      </div>
      <div
        v-for="(item,index) in checkedEmployees"
        :key="index"
        class="list-item result"
      >
        <div>
          {{ item.label }}
        </div>
        <v-svg
          v-if="!disabled"
          name="ashbin"
          @click="removeChecked(item.value)"
        />
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, onMounted, watch, Ref, computed,
} from 'vue';
import CompanyMangement, { SelectTagTree, SelectTagTreeSearchParams } from '~models/company-mangement';
import { getArrayParams } from '~utils/tool';
import employeeList from './employee-list.vue';
// import result from './result.vue';
import { SelectCondition, CheckedEmployees } from '../type';
// import employeeSelect from './select.vue';

export default defineComponent({
  components: { employeeList },
  props: {
    value: {
      type: Object as PropType<CheckedEmployees>,
      default: () => ({}),
    },
    companyType: {
      type: Number as PropType<0|1|null>, // 公司类型
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
    companyIds: { // 园区id
      type: [Array, String] as PropType<string[]|string>,
      default: () => ([]),
    },
    jobCodes: { // 园区id
      type: [Array, String] as PropType<string[]|string>,
      default: () => ([]),
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
    'update:value': (checkedEmployees:CheckedEmployees) => true,
  },
  setup(props, { emit }) {
    const {
      value, companyType, orgId, parkOrgIds, companyIds, jobCodes, mode,
    } = toRefs(props);
    const seachTreeParam = reactive({
      companyType: companyType.value, // 0物业|1 除了物业得公司
      orgId: orgId.value,
      companyIds: companyIds.value.toString() ? companyIds.value.toString().split(',') : [],
      parkOrgIds: parkOrgIds.value,
      // jobCodes: jobCodes.value,
    });
    const selectedKeys = ref<string[]>([]);

    // 选中树节点
    const { select, selectCondition } = useTreeSelect(seachTreeParam);

    // 获取公司/标签树
    const {
      tagTreeData,
      searchTreeData,
    } = useTagTree(seachTreeParam, selectCondition);

    // 勾选员工
    const {
      checkedEmployees, checkedChange, removeChecked, disabledCheckBox,
    } = useChecked(mode);
    Object.assign(checkedEmployees, value.value);
    watch(checkedEmployees, () => {
      emit('update:value', checkedEmployees);
      // console.log('变化了------');
    }, {
      deep: true,
    });
    return {
      tagTreeData,
      selectedKeys,
      searchTreeData,
      select,
      selectCondition,
      checkedEmployees,
      checkedChange,
      removeChecked,
      disabledCheckBox,
    };
  },
});

/**
 * @description: 获取物业类型得公司
 * @param {*} seachTreeParam
 * @return {*}
 * @author: GUOCHAO82
 */
function useTagTree(seachTreeParam:SelectTagTreeSearchParams, selectCondition:Ref<SelectCondition>) {
  const tagTreeData = ref<SelectTagTree[]>([]);
  const searchTreeData = async () => {
    tagTreeData.value = await new CompanyMangement().selectTagTree(seachTreeParam);
    if (tagTreeData.value.length) { // 默认选中第一个
      const { type, value, extraInfo } = tagTreeData.value[0];
      // const companyIds = seachTreeParam.companyIds.toString();

      selectCondition.value = {
        companyIds: seachTreeParam.companyIds as string[],
        tagIds: [],
        parkOrgId: extraInfo as unknown as string,
      };
    }
  };
  searchTreeData();

  return {
    tagTreeData,
    searchTreeData,
  };
}

// 勾选树节点
function useTreeSelect(seachTreeParam:SelectTagTreeSearchParams) {
  const selectCondition = ref<SelectCondition>({
    companyIds: [],
    tagIds: [],
    parkOrgId: '',
  });
  const typeNames = {
    1: 'parkOrgId',
    4: 'companyIds',
    5: 'tagIds',
  };
  const select = (selectedKeys:unknown, e:any) => {
    const node = e.node.dataRef as SelectTagTree[0];
    // console.log(node, '---node');
    // debugger;
    // // selectCondition.value = {
    // //   companyIds: [],
    // //   tagIds: [],
    // //   parkOrgId: node.extraInfo as unknown as string,
    // // };
    const { type, extraInfo, value } = node;
    Object.assign(selectCondition.value, {
      companyIds: typeNames[type] === 'companyIds' ? [value] : seachTreeParam.companyIds,
      tagIds: typeNames[type] === 'tagIds' ? [value] : [],
    });
    console.log(selectCondition.value, 'select---');
  };
  return {
    selectCondition,
    select,
  };
}

// 勾选员工
function useChecked(mode:Ref<'single'|'multiple'>) {
  const checkedEmployees = reactive<CheckedEmployees>({});
  const checkedChange = (e:any, checkedEmployee:SelectOptions[0]) => {
    if (e.target.checked) {
      checkedEmployees[checkedEmployee.value] = checkedEmployee;
      return;
    }
    delete checkedEmployees[checkedEmployee.value];
    console.log(e, '----checkedChange');
  };
  const removeChecked = (key:string) => {
    delete checkedEmployees[key];
  };

  // 禁用勾选员工
  const disabledCheckBox = computed(() => {
    if (mode.value === 'multiple') return false;
    return !!Object.keys(checkedEmployees).length;
  });
  return {
    checkedEmployees,
    checkedChange,
    removeChecked,
    disabledCheckBox,
  };
}
</script>
<style lang="scss" scoped>
.link-employee {
    display: flex;
    align-items: center;
    justify-content: space-between;
    border: 1px solid #f0f0f0;

    svg {
        cursor: pointer;
    }

    >div {
        flex: 1;
        height: 60vh;
        padding: 12px;
        overflow: auto;
        border-right: 1px solid #ddd;

        &:last-child {
            border: none;
        }
    }

    .list-item {
        padding: 6px;

        &:hover {
            background: #ffeed4;
        }

        &.result {
            display: flex;
            align-items: center;
            justify-content: space-between;
        }
    }
}
</style>
