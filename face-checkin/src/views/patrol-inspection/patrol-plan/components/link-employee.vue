<!--
 * @Description:关联巡检点
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-01 13:39:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-08 16:27:08
-->
<template>
  <div
    class="link-employee"
  >
    <a-form>
      <a-form-item
        label="关联巡检人"
        v-bind="validateInfos.employeeIds"
      >
        <v-select-employee
          v-model:value="modelRef.employeeIds"
          :park-org-ids="defaultData.parkOrgId"
          job-codes="11"
          :default-select-opts="defaultData.employees"
          placeholder="请选择巡检人"
        />
      </a-form-item>
    </a-form>
    <!-- <div class="left">
      <div class="content-list">
        <div class="company" />
        <employee-select
          v-model:value="modelRef.employeeIds"
          :select-data="employeesList[0]"
          @expand="expand"
          @select="select"
        />
      </div>
    </div>
    <div class="right">
      <div class="content-list">
        <div class="list-item title">
          <div class="left">
            以选（{{ Object.values(selectData).length }}）
          </div>
          <span
            type="text"
            @click="remove('all')"
          >
            全部清除
          </span>
        </div>
        <div
          v-for="(item,key) in selectData"
          :key="key"
          class="list-item"
        >
          <div class="left">
            <v-svg name="office-supplies" />
            <span>{{ item.label }}</span>
          </div>
          <v-svg
            name="close"
            @click="remove('single',key)"
          />
        </div>
      </div>
    </div> -->
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, onMounted, watch,
} from 'vue';
import CompanyMangement, { EmployeesDictByTagSort } from '~models/company-mangement';
import { PatrolPlan } from '~models/patrol-inspection';
import { useLinkEmployee } from '../composition/useFormConf';
// import employeeSelect from './select.vue';

interface selectData {
  [props:string]: EmployeesDictByTagSort
}

export default defineComponent({
  // components: { employeeSelect },
  props: {
    defaultData: {
      type: Object as PropType<PatrolPlan>,
      default: () => ({}),
    },
  },
  setup(props) {
    const { defaultData } = toRefs(props);
    const employeesList = ref<EmployeesDictByTagSort[]>([]);
    const { modelRef, validateInfos } = useLinkEmployee();
    const getSelectData = (data:EmployeesDictByTagSort) => {
      data.expanded = true;
      if (data.children?.length) {
        data.children.forEach((item) => getSelectData(item));
      }
    };
    const expand = (isExpand:boolean, node:EmployeesDictByTagSort) => {
      console.log(node, '---node');
      node.expanded = isExpand;
    };

    const selectData = ref<selectData>({});
    const select = (isChecked:boolean, node:EmployeesDictByTagSort) => {
      const { value } = node;
      if (!value) return;
      if (!isChecked) {
        delete selectData.value[value];
        return;
      }
      selectData.value[value] = node;
      console.log(selectData.value, '----selectData');
    };
    const remove = (type:string, key?:string) => {
      if (type === 'all') {
        selectData.value = {};
        modelRef.employeeIds = [];
        return;
      }
      if (key) {
        delete selectData.value[key];
        const index = modelRef.employeeIds.indexOf(key);
        if (index !== -1) modelRef.employeeIds.splice(index, 1);
      }
    };
    const fill = () => {
      const initSelectData:selectData = {};
      modelRef.employeeIds = (defaultData.value.employees as unknown as SelectOptions).map((opt) => {
        const { value } = opt;
        if (value) {
          initSelectData[value] = opt;
        }
        return value;
      });
      selectData.value = initSelectData;
      modelRef.planId = defaultData.value.id || '';
    };
    const init = async () => {
      employeesList.value = await new CompanyMangement().employeesDictByTagSort({
        companyType: 0,
      });
      getSelectData(employeesList.value[0]);
      fill();
    };
    init();
    return {
      employeesList,
      expand,
      select,
      selectData,
      remove,
      modelRef,
      validateInfos,
    };
  },
});
</script>
<style lang="scss" scoped>
.link-employee {
    svg {
        cursor: pointer;
    }

    >div {
        flex: 1;
        height: 60vh;
        padding: 12px;
        overflow: auto;

        &:first-child {
            border-right: 1px solid #f0f0f0;
        }

        &.right {
            .list-item {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 10px;

                &:hover {
                    background: #f5f7fa;
                }

                &.title {
                    padding: 0 26px 24px;

                    span {
                        color: var(--ant-primary-color);
                        cursor: pointer;
                    }
                }

                >.left {
                    display: flex;
                    align-items: center;
                }
            }
        }
    }
}
</style>
