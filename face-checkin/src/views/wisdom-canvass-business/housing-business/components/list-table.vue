<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-12 10:19:49
-->
<template>
  <div class="menu-list">
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
          @click="operate('add')"
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
          :scroll="scroll"
          @change="pageChange"
        >
          <template #title="{text,record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ text }}</span>
          </template>
          <template

            #operate="{record}"
          >
            <span v-if="record.status!==1">
              <a-button
                type="link"
                @click="operate('edit',record)"
              >
                编辑
              </a-button>
              <a-button
                type="link"
                @click="operate('del',record)"
              >
                删除
              </a-button>
            </span>
            <a-button
              v-if="!hiddenSoldOutBtn(record)"
              type="link"
              @click="operate('soldOut',record)"
            >
              <!-- {{ record.status===1? '下架':'发布' }} -->
              <span v-if="record.status===1">下架</span>
              <span v-else>发布</span>
            </a-button>
          </template>
          <template #orgType="{record}">
            <span>{{ orgTypeNames[record.orgType] }}</span>
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
  reactive, ref, onMounted, defineComponent, PropType, computed, watch, onActivated,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

import BusinessInvitation, { HousingBusinessInfo } from '~models/wisdom-canvass-business';
import Park from '~models/park';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import {
  statusName,
  getColumns,
  // operateDialogTitleName,
  orgTypeNames,
} from '../config';

type Operatetypes = 'add'|'edit'|'del'|'soldOut'|'details' |'export' //
const businessInvitationInstance = new BusinessInvitation();
export default defineComponent({
  components: { searchBox },
  props: {
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
    const router = useRouter();
    const options = ref<DictionaryMap<['EFFECT_TYPE', 'BUSINESS_STATUS']>>({
      EFFECT_TYPE: [],
      BUSINESS_STATUS: [],
    });
    const columns = getColumns(options);
    const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['effectType', 'title', 'status', 'orgId']>>({
      orgId: {
        tag: 'select',
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        options: () => parkOpts.value,
      },
      title: {
        tag: 'input',
        value: '',
        placeholder: '请输入招商标题',
      },
      effectType: {
        tag: 'select',
        value: '',
        placeholder: '请选择招商类型',
        options: () => options.value?.EFFECT_TYPE,
      },
      status: {
        tag: 'select',
        value: '',
        placeholder: '请选择招商状态',
        options: () => options.value?.BUSINESS_STATUS,
      },
    });
    const hiddenSoldOutBtn = computed(() => ({ deadline, status, effectType }:HousingBusinessInfo) => [0, 2].includes(status as number) && (!effectType && Date.now() > new Date(deadline).getTime()));

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      download,
    } = useSearchListPage(businessInvitationInstance.housingBusinessListPage, searchConf, null, businessInvitationInstance.housingBusinessExport);
    const operateSuccess = () => {
      search(true);
      // emit('on-refresh');
    };
    onActivated(() => { // 返回时刷新列表数据
      console.log('激活了-----', props.refresh);
      if (props.refresh)operateSuccess();
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:HousingBusinessInfo) => {
      // operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.title}】吗？`,
            onOk: async () => {
              await businessInvitationInstance.housingBusinessDel(record.id as string);
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
        case 'soldOut': {
          const status = record?.status === 1 ? 2 : 1;
          await businessInvitationInstance.housingUpdateStatus({
            id: (record as HousingBusinessInfo).id as string,
            status,
          });
          message.success('操作成功');
          operateSuccess();
          break;
        }
        default: {
          addCachePage('HousingBusiness');
          router.push({
            name: 'wisdomCanvassBusinessBusinessInfoAdd',
            params: {
              type,
              id: record?.id || '',
            },
          });
        }
      }
    };

    // watch(() => props.selectTreeNode.key, () => {
    //   console.log(props.selectTreeNode.key, '----props.selectTreeNode.key');
    //   if (!props.selectTreeNode.key) return;
    //   searchConf.orgId.value = props.selectTreeNode.key;
    // });
    search(true);
    const init = async () => {
      options.value = await useHashMapDic<['EFFECT_TYPE', 'BUSINESS_STATUS']>(['EFFECT_TYPE', 'BUSINESS_STATUS']);
      parkOpts.value = await new Park().searchParkListName();
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
      hiddenSoldOutBtn,
      operateSuccess,
      orgTypeNames,
    };
  },
});
</script>
