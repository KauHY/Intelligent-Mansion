<!--
 * @Description: 解绑房屋
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-26 16:03:17
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-12 11:05:13
-->
<template>
  <div class="unbind-house content-section">
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
          @click="operate('black')"
        >
          返回
        </a-button>
      </template>
      <template #table="{scroll}">
        <v-table
          row-key="id"
          bordered
          :columns="companyBindHouseListPageColumns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #operate="{record}">
            <div>
              <a-button
                type="link"
                @click="operate('unbund',record)"
              >
                解绑
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
  </div>
</template>

<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, toRefs, onDeactivated,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import router from '@/routers';
import { useRouter } from 'vue-router';

import CompanyMangement, { CompanyHouseBindListPageItem } from '~models/company-mangement';
import Park, { ParkInfo } from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import Building from '~models/building';
import House from '~models/house';
import operateDialog from './components/operate-dialog.vue';
import {
//   companyTypeNames,
//   columns,
  companyBindHouseListPageColumns,
  operateDialogTitleName,
} from './config';

type Operatetypes = 'unbund'|'black'

// const a:string = 1;
export default defineComponent({
  name: 'CompanyMangementPompanyList',
  components: { searchBox },
  props: {
    companyId: {
      type: String as PropType<string>,
      default: '',
    },
    parkOrgId: {
      type: String as PropType<string>,
      default: '',
    },
    name: {
      type: String as PropType<string>,
      default: '',
    },
    parkOrgName: {
      type: String as PropType<string>,
      default: '',
    },
  },
  setup(props) {
    // const a:string = 1;
    const router = useRouter();
    const companyInstance = new CompanyMangement();
    const parkInstance = new Park();

    const buindingOpts = ref<SelectOptions>([]);
    const houseOpts = ref<SelectOptions>([]);

    const searchConf = reactive<SearchConf<['buildingId', 'houseId', 'companyId']>>({
      buildingId: {
        tag: 'select',
        options: buindingOpts,
        value: '',
        placeholder: '请选择楼栋单元',
        handleChange: (value:string) => {},
      },
      houseId: {
        tag: 'select',
        value: '',
        options: houseOpts,
        placeholder: '请选择楼层房号',
      },
      companyId: {
        tag: 'select',
        value: '',
        hidden: true,
        placeholder: '请选择园区',
      },
    });
    const { companyId } = toRefs(props);
    searchConf.companyId.value = companyId.value;

    searchConf.buildingId.handleChange = async (buildingId:string) => {
      houseOpts.value = await new House().searchHouseOpts(buildingId);
    };

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<CompanyHouseBindListPageItem>(companyInstance.companyBindHouseListPage, searchConf, null);

    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:CompanyHouseBindListPageItem) => {
      operateDialogConf.type = type;
      if (type === 'black') {
        router.push({ name: 'companyMangementPompanyList' });
        return;
      }
      if (!record) return;
      Modal.confirm({
        content: `确定解绑【${record.houseName}】吗？`,
        onOk: async () => {
          await companyInstance.unbindHouse(record.id as string);
          message.success('操作成功');
          search(true);
        },
      });

    //     }
    //   }
    };
    const init = async () => {
      // debugger;
      buindingOpts.value = await new Building().listBuildingDict(props.parkOrgId);
      search(true);
    };

    const { remoteCachePage } = useCachePage();
    onDeactivated(() => {
      remoteCachePage('CompanyMangementPompanyList'); // 删除缓存
      console.log('删除缓存');
    });

    init();
    // companyInstance.getUserMenuList();
    onMounted(() => {
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      companyBindHouseListPageColumns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      operate,
    //   parkOpts,
    };
  },
});
</script>
