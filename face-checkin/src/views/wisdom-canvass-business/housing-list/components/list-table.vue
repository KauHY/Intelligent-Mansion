<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-15 10:41:06
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
        </a-button>
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
          <template #houseVO="{record}">
            <span
              class="table-text-link"
              @click="operate('details',record)"
            >{{ record.houseVO?.name||'-' }}</span>
          </template>
          <template
            #operate="{record}"
          >
            <a-button
              v-if="record.businessStatus!==1"
              type="link"
              @click="operate('edit',record)"
            >
              编辑
            </a-button>

            <a-button
              v-if="record.businessStatus===null"
              type="link"
              @click="operate('del',record)"
            >
              删除
            </a-button>
          </template>
        </v-table>
      </template>
    </search-box>
    <!-- <component
      :is="operateDialogConf.cp"
      v-model:visible="operateDialogConf.visible"
      :default-data="operateDialogConf.data"
      :type="operateDialogConf.type"
      :title="operateDialogConf.title"
      :menu-list="tableList"
      @operate:success="operateSuccess"
    /> -->
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, computed, watch, onActivated,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';

import BusinessInvitation, { HousingInfo } from '~models/wisdom-canvass-business';
// import Park from '~models/park';

import { useSearchListPage, useCachePage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';

import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import {
  statusName,
  getColumns,
  orgTypeNames,
} from '../config';

type Operatetypes = 'add'|'edit'|'del'|'soldOut'|'details' //
const businessInvitationInstance = new BusinessInvitation();
export default defineComponent({
  name: 'MenuList',
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
    // const parkOpts = ref<SelectOptions>([]);
    const {
      buildingOpts,
      houseOpts,
      parkOpts,
      getParkOpts,
      getHouseOpts,
      getBuildingOpts,
    } = useParkInfoOpts();
    const searchConf = reactive<SearchConf<['buildingId', 'houseId', 'effectType', 'businessStatus', 'employeeName', 'orgId']>>({
      orgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        filterOption: true,
        showSearch: true,
        options: () => parkOpts.value,
        handleChange: (parkOrgId:string) => {
          getBuildingOpts(parkOrgId);
          searchConf.buildingId.value = '';
          searchConf.houseId.value = '';
        },
      },
      buildingId: {
        tag: 'select',
        value: '',
        placeholder: '请选楼栋单元',
        filterOption: true,
        showSearch: true,
        options: () => buildingOpts.value,
        handleChange: (buildingId:string) => {
          getHouseOpts(buildingId);
          searchConf.houseId.value = '';
        },
      },
      houseId: {
        tag: 'select',
        value: '',
        placeholder: '请选楼层房号',
        filterOption: true,
        showSearch: true,
        options: () => houseOpts.value,
      },
      effectType: {
        tag: 'select',
        value: '',
        placeholder: '请选择招商类型',
        options: () => options.value?.EFFECT_TYPE,
      },
      businessStatus: {
        tag: 'select',
        value: '',
        placeholder: '请选择招商状态',
        options: () => options.value?.BUSINESS_STATUS,
      },
      employeeName: {
        tag: 'input',
        value: '',
        placeholder: '请输入招商顾问名称',
      },
    });
    // const isCheckAllOrg = computed(() => props.selectTreeNode.key === '0');
    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(businessInvitationInstance.houseInfoListPage, searchConf, null);
    const operateSuccess = () => {
      search(true);
      // emit('on-refresh');
    };
    onActivated(() => { // 返回时刷新列表数据
      // console.log('激活了-----', props.refresh);
      if (props.refresh)operateSuccess();
    });

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:HousingInfo) => {
      // operateDialogConf.type = type;
      switch (type) {
        case 'del': {
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.houseVO.name}】吗？`,
            onOk: async () => {
              await businessInvitationInstance.houseDel(record.id as string);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        }
        // case 'add': {
        //   operateDialogConf.visible = true;
        //   break;
        // }
        default: {
          addCachePage('HousingList');
          router.push({
            name: 'wisdomCanvassBusinessHousingInfoEdit',
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
    //   search(true);
    // });

    search(true);
    const init = async () => {
      getParkOpts();
      options.value = await useHashMapDic<['EFFECT_TYPE', 'BUSINESS_STATUS']>(['EFFECT_TYPE', 'BUSINESS_STATUS']);
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
      // isCheckAllOrg,
      operateSuccess,
      orgTypeNames,
    };
  },
  // async mounted() {
  //   console.log('xxxx');
  // },
});
</script>
