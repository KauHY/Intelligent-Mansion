<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-28 17:44:42
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-05 17:58:45
-->
<template>
  <div class="employee-list">
    <div class="content-box">
      <div class="search-box">
        <a-input-search
          v-model:value="searchParams.name"
          placeholder="输入员工姓名搜索"
          enter-button
          @search="searchEmployeeList"
        />
      </div>
      <div class="list-content">
        <slot
          name="employeeList"
          :list="employeeList"
        />
      </div>
    </div>
    <div class="page-footer">
      <a-pagination
        v-model:current="searchParams.pageNum"
        v-model:pageSize="searchParams.pageSize"
        show-size-changer
        :total="totalNum"
        size="small"
        @change="pageChange"
      />
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, Ref, watch, toRef, toRefs,
} from 'vue';
import CompanyMangement, { SelectEmployeesByIdAndType } from '~models/company-mangement';
import { SelectCondition } from '../type';

export default defineComponent({
  props: {
    selectCondition: {
      type: Object as PropType<SelectCondition>,
      default: () => ({}),
    },
    jobCodes: { // 园区id
      type: [Array, String] as PropType<string[]|string>,
      default: () => ([]),
    },
  },

  setup(porps) {
    const { selectCondition, jobCodes } = toRefs(porps);
    const {
      employeeList,
      searchEmployeeList,
      searchParams,
      pageChange,
      totalNum,
    } = useSearch(selectCondition, jobCodes.value); // 查询员工

    return {
      employeeList,
      searchEmployeeList,
      searchParams,
      totalNum,
      pageChange,
    };
  },
});

// 搜索员工
function useSearch(selectCondition:Ref<SelectCondition>, jobCodes:string|string[]) {
  const employeeList = ref<SelectOptions>([]);
  const totalNum = ref(0);

  const searchParams = reactive({
    name: '',
    pageSize: 10,
    pageNum: 1,
  });
  const searchEmployeeList = async () => {
    // const { companyIds, tagIds, parkOrgId } = selectCondition.value;
    const jobCodesStr = jobCodes.toString();
    const { list, total } = await new CompanyMangement().selectEmployeesByIdAndType({
      ...searchParams,
      ...selectCondition.value,
      jobCodes: jobCodesStr ? jobCodesStr.split(',') : [],
    });

    console.log(list, '------list');
    employeeList.value = list;
    totalNum.value = total;
  };
  const pageChange = (pageNum:number, pageSize:string) => {
    Object.assign(searchParams, {
      pageNum,
      pageSize,
    });
    searchEmployeeList();
  };

  watch(selectCondition, () => {
    searchEmployeeList();
  }, { deep: true });
  return {
    searchParams,
    employeeList,
    searchEmployeeList,
    // showSizeChange,
    totalNum,
    pageChange,
  };
}
</script>
<style lang='scss' scoped>
.employee-list {
    display: flex;
    flex-direction: column;
    height: 100%;

    .page-footer {
        height: 40px;
    }

    .content-box {
        flex: 1;
        overflow: auto;

        .search-box {
            padding: 12px;
            margin-bottom: 16px;
            background: linear-gradient(180deg, rgba(255, 255, 255, 0.78) 0%, rgba(244, 249, 255, 0.9) 100%);
            border: 1px solid rgba(var(--theme-primary-rgb), 0.12);
            border-radius: 18px;
            box-shadow:
                inset 0 1px 0 rgba(255, 255, 255, 0.84),
                0 12px 24px rgba(8, 24, 40, 0.05);
            backdrop-filter: blur(14px);
        }

        .list-content {
            padding-left: 10px;
        }
    }
}
</style>
