<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-02 11:31:15
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-26 14:25:38
-->
<template>
  <div class="follow-record">
    <search-box :search-conf="searchConf">
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          force-render
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        />
      </template>
    </search-box>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, toRefs,
} from 'vue';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import WisdomCanvassBusiness from '~models/wisdom-canvass-business';
import { getFollowRecordColumns } from '../../config';

export default defineComponent({
  components: { searchBox },
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const options = ref<DictionaryMap<['BUSINESS_FOLLOW_TYPE', 'BUSINESS_CHANCE_STAGE']>>({
      BUSINESS_FOLLOW_TYPE: [],
      BUSINESS_CHANCE_STAGE: [],
    });
    const columns = getFollowRecordColumns(options);
    const searchConf = reactive<SearchConf<['id']>>({
      id: {
        tag: 'input',
        value: '',
        hidden: true,
      },
    });
    const { id } = toRefs(props);
    searchConf.id.value = id.value;
    const {
      pagination,
      pageChange,
      tableList,
      search,
    } = useSearchListPage(new WisdomCanvassBusiness().followRecordFindById, searchConf, null);
    const init = async () => {
      options.value = await useHashMapDic<['BUSINESS_FOLLOW_TYPE', 'BUSINESS_CHANCE_STAGE']>(['BUSINESS_FOLLOW_TYPE', 'BUSINESS_CHANCE_STAGE']);
      search(true);
    };
    init();
    return {
      searchConf,
      pagination,
      pageChange,
      tableList,
      columns,
      // search,
    };
  },
});
</script>
