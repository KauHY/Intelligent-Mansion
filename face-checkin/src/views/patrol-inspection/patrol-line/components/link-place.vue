<!--
 * @Description:关联巡检点
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-01 13:39:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-02 15:12:19
-->
<template>
  <div class="link-place">
    <div class="btns">
      <a-button @click="visible=true">
        选择巡检点
      </a-button>
    </div>
    <v-table
      row-key="id"
      bordered
      :columns="columns"
      :data-source="tableData"
      :scroll="{y:'400px'}"
    >
      <template #operate="{index}">
        <a-button
          :disabled="!index"
          size="small"
          type="text"
        >
          <v-svg
            name="rising"
            @click="operate(index,'up')"
          />
        </a-button>
        <a-button
          size="small"
          type="text"
          :disabled="index>tableData.length-2"
        >
          <v-svg
            name="falling"
            @click="operate(index,'down')"
          />
        </a-button>
        <a-button
          size="small"
          @click="operate(index,'del')"
        >
          移除
        </a-button>
      </template>
    </v-table>
    <select-place
      v-if="visible"
      v-model:visible="visible"
      :patrol-type="defaultData.type"
      :park-org-id="defaultData.parkOrgId"
      :inspection-types="inspectionTypes"
      :base-select-data="tableData"
      @select="select"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, onMounted, watch,
} from 'vue';
import { PatrolLine, PatrolPlace } from '~models/patrol-inspection';
import { DictionaryMap } from '~composition/useHashMapDic';
import { getSelectColumns } from '../config';
import selectPlace from './select-place.vue';
import { useAddLinkPlace } from '../composition/useFormConf';

export default defineComponent({
  components: { selectPlace },
  props: {
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
    defaultData: {
      type: Object as PropType<PatrolLine>,
      default: () => ({}),
    },
  },
  setup(prop) {
    const { inspectionTypes, defaultData } = toRefs(prop);
    const visible = ref(false);
    const { modelRef } = useAddLinkPlace();
    const tableData = ref<PatrolPlace[]>([]);
    const select = (selectData:PatrolPlace[]) => {
      tableData.value = selectData;
    };
    const operate = (index:number, type:string) => {
      if (type === 'del') {
        tableData.value.splice(index, 1);
        return;
      }
      const temp = tableData.value[index];
      const nextIndex = type === 'up' ? index - 1 : index + 1;
      tableData.value[index] = tableData.value[nextIndex];
      tableData.value[nextIndex] = temp;
    };
    onMounted(() => {
      // fill(defaultData.value);
      modelRef.circuitId = defaultData.value.id;
      tableData.value = defaultData.value.points || [];
    });
    watch(tableData, () => {
      modelRef.pointIds = tableData.value.map(({ id }) => id);
    }, {
      deep: true,
    });
    return {
      tableData,
      columns: getSelectColumns(inspectionTypes.value),
      visible,
      select,
      operate,
    };
  },
});
</script>
<style lang="scss" scoped>
.btns {
    padding: 0 10px 20px 0;
    text-align: right;
}

svg {
    cursor: pointer;

    &:hover {
        color: var(--ant-primary-color);
    }
}
</style>
