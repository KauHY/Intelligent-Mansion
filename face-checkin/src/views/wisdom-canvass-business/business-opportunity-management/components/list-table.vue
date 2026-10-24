<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-05 15:19:43
-->
<template>
  <div class="list-table">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp;
        <a-button
          v-if="!type"
          type="primary"
          @click="operate('add',null)"
        >
          新增
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
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="{...scroll,x:1800}"
          @change="pageChange"
        >
          <template #chanceName="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template

            #operate="{record}"
          >
            <div v-if="record.chanceStage!==7">
              <a-button
                v-for="btnInfo in operateBtnConf[type]"
                :key="btnInfo.operateType"
                type="link"
                @click="operate(btnInfo.operateType,record,btnInfo)"
              >
                {{ btnInfo.btnText }}
              </a-button>
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
      :menu-list="tableList"
      @operate:success="operateSuccess"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, computed, watch, onActivated, toRefs,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';

import WisdomCanvassBusiness, { BusinessInfo } from '~models/wisdom-canvass-business';
import Park from '~models/park';

import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import operateDialog from './operate-dialog.vue';

import {
  statusName,
  getColumns,
  // operateDialogTitleName,
  orgTypeNames,
  operateBtnConf,
} from '../config';

type Operatetypes = 'add'|'del'|'details'|'other'|'convertToCustomer'|'remind'|'follow'|'release'| 'export'|'receive' //
const wisdomCanvassBusinessInstance = new WisdomCanvassBusiness();
export default defineComponent({
  // name: 'BusinessOpportunityManagement',
  components: { searchBox },
  props: {
    type: {
      type: Number as PropType<0|1|2>,
      default: 0,
    },
    selectTreeNode: {
      type: Object as PropType<{name:string, key:string}>,
      default: () => ({}),
    },
    refresh: {
      type: String as PropType<'refresh'|''>,
      default: '',
    },
  },
  emits: ['on-refresh'],
  setup(props, { emit }) {
    const { addCachePage } = useCachePage();
    const { type, refresh, selectTreeNode } = toRefs(props);
    const currentType = type.value;
    const router = useRouter();
    const options = ref<DictionaryMap<['BUSINESS_CHANCE_STAGE', 'BUSINESS_CHANCE_SOURCE']>>({
      BUSINESS_CHANCE_STAGE: [],
      BUSINESS_CHANCE_SOURCE: [],
    });
    const columns = getColumns(options, type.value);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['businessSource', 'followTimes', 'keyword', 'chanceStage', 'type', 'orgId']>>({
      orgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
      },
      businessSource: {
        tag: 'select',
        value: '',
        placeholder: '请选择商机来源',
        options: () => options.value?.BUSINESS_CHANCE_SOURCE,
      },
      followTimes: {
        tag: 'date-range',
        startTimeKey: 'followStartTime',
        endTimeKey: 'followEndTime',
        value: [],
        placeholder: ['跟进开始时间', '跟进结束时间'],
      },
      keyword: {
        tag: 'input',
        value: '',
        placeholder: '请输入关键字',
      },
      chanceStage: {
        tag: 'select',
        value: '',
        placeholder: '请选择阶段',
        options: () => options.value?.BUSINESS_CHANCE_STAGE,
      },
      type: {
        tag: 'input',
        value: type.value,
        hidden: true,
      },
    });
    const isCheckAllOrg = computed(() => props.selectTreeNode.key === '0');
    let urls;
    if (currentType === 0) {
      urls = wisdomCanvassBusinessInstance.allBusinessInfoExport;
    } else if (currentType === 1) {
      urls = wisdomCanvassBusinessInstance.commonBusinessInfoExport;
    } else {
      urls = wisdomCanvassBusinessInstance.myBusinessInfoExport;
    }
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage(wisdomCanvassBusinessInstance.businessInfoListPage, searchConf, operateDialog, urls);
    const operateSuccess = () => {
      search(true);
      // emit('on-refresh');
    };
    onActivated(() => { // 返回时刷新列表数据
      // console.log('激活了-----', props.refresh);
      if (refresh.value)operateSuccess();
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record:BusinessInfo|null, btnConfItem?: typeof operateBtnConf[0][number]) => {
      operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.chanceName}】吗？`,
            onOk: async () => {
              await wisdomCanvassBusinessInstance.businessInfoDel(record.id as string);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        }
        case 'export': {
          await download();
          break;
        }
        case 'release': {
          if (record?.id) {
            Modal.confirm({
              content: `确定释放【${record.chanceName}】商机吗？`,
              onOk: async () => {
                await wisdomCanvassBusinessInstance.businessInfoOperate({
                  followEmployeeId: '', // 跟进人id
                  id: record?.id,
                  recordType: 8, // 操作类型
                });
                message.success('操作成功');
                operateSuccess();
              },
            });
          }
          break;
        }
        case 'receive': {
          if (!record?.id) return;
          Modal.confirm({
            content: `确定领取【${record.chanceName}】商机吗？`,
            onOk: async () => {
              await wisdomCanvassBusinessInstance.businessInfoOperate({
                parkOrgId: record.parkOrgId, // 跟进人id
                id: record?.id,
                recordType: 3, // 领取商机
              });
              message.success('操作成功');
              operateSuccess();
            },
          });

          break;
        }
        case 'details': {
          router.push({
            name: 'wisdomCanvassBusinessOpportunitiesDetails',
            params: {
              id: record?.id || '',
            },
          });
          addCachePage('BusinessOpportunityManagement');
          break;
        }
        default: {
          operateDialogConf.title = btnConfItem?.title || '新增商机';
          operateDialogConf.visible = true;
          operateDialogConf.data = { ...record, recordType: btnConfItem?.typeValue } as BusinessInfo;
        }
      }
    };

    // watch(() => selectTreeNode.value.key, () => {
    //   console.log(selectTreeNode.value.key, '----props.selectTreeNode.key');
    //   if (!selectTreeNode.value.key) return;
    //   searchConf.orgId.value = selectTreeNode.value.key;
    //   search(true);
    // });
    search(true);
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      options.value = await useHashMapDic<['BUSINESS_CHANCE_STAGE', 'BUSINESS_CHANCE_SOURCE']>(['BUSINESS_CHANCE_STAGE', 'BUSINESS_CHANCE_SOURCE']);
      // console.log(options.value, '-------options.value');
    };
    onMounted(() => {
      init();
    });
    return {
      columns,
      statusName,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
      isCheckAllOrg,
      operateSuccess,
      orgTypeNames,
      operateBtnConf,
      currentType,
    };
  },
});
</script>
