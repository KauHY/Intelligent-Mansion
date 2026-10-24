<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-28 14:05:37
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="operate('export')"
        >
          导出
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :data-source="tableList"
          :columns="columns"
          :pagination="pagination"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #goodsNo="{text,record}">
            <span
              class="text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template #operate="{record}">
            <div>
              <!-- <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>&nbsp;&nbsp; -->
              <a-button
                v-if="record.status===1"
                type="link"
                @click="operate('audit',record)"
              >
                审核
              </a-button>
              <span v-else>-</span>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      @operate:success="search(true)"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import CustomerService, { GoodsReleaseInfo } from '~models/customer-service';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useHashMapDic, useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import {
  getColumns, operateDialogTitleName,
} from './config';

import operateDialog from './components/operate-dialog.vue';
// import useOpts from './composition/useOpts';

type Operatetypes = 'audit'|'details' |'export'
const customerServiceInstance = new CustomerService();
// const a:string = 1;
export default defineComponent({
  components: { searchBox },
  setup() {
    const { parkOpts, options } = useOptions();
    const searchConf = reactive<SearchConf<['submitterName', 'submitterMobile', 'parkOrgId', 'times', 'status']>>({
      submitterName: {
        tag: 'input',
        value: '',
        placeholder: '申请人姓名',
      },
      submitterMobile: {
        tag: 'input',
        value: '',
        placeholder: '申请人手机号',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange: (value:string) => {},
      },
      times: {
        tag: 'date-range',
        value: [],
        placeholder: ['申请开始时间', '申请结束时间'],
        startTimeKey: 'createTimeStart',
        endTimeKey: 'createTimeEnd',
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择状态',
        options: () => options.GOODS_RELEASE_STATUS,
      },
    });
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage<GoodsReleaseInfo>(customerServiceInstance.goodsReleaseListPage, searchConf, operateDialog, customerServiceInstance.exportGoodsRelease);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:GoodsReleaseInfo) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'export': {
          await download();
          break;
        }
        default: {
          operateDialogConf.visible = true;
          operateDialogConf.title = operateDialogTitleName[type];
          operateDialogConf.data = record as GoodsReleaseInfo;
        }
      }
    };
    search(true);

    return {
      columns: getColumns(options),
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
    };
  },
});

/**
 * @description: 获取下拉数据
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
function useOptions() {
  const parkOpts = ref<SelectOptions>([]);
  const options = useHashMapDics<['GOODS_RELEASE_STATUS']>(['GOODS_RELEASE_STATUS']);
  const init = async () => {
    parkOpts.value = await new Park().searchParkListName();
  };
  init();
  return {
    parkOpts,
    options,
  };
}
</script>
