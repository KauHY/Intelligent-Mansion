<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-24 17:34:57
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-10-21 16:42:42
-->
<!--
 * @Description: 解绑房屋
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-26 16:03:17
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-27 17:27:36
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
          <template #operate="{record}">
            <div v-if="record.bind">
              <a-button
                type="link"
                @click="operate('view',record)"
              >
                续租记录
              </a-button>
              <a-button
                type="link"
                @click="operate('rent',record)"
              >
                续租
              </a-button>
              <a-button
                type="link"
                @click="operate('unbund',record)"
              >
                解绑
              </a-button>
            </div>
            <span v-else>/</span>
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
      @operate:success="search(true)"
    />
  </div>
</template>

<script lang='ts'>
import {
  reactive, ref, onMounted, defineComponent, PropType, toRefs,
} from 'vue';

import { message, Modal } from 'ant-design-vue';
import router from '@/routers';
import { useRouter } from 'vue-router';

import CompanyMangement, { HouseBindRecord } from '~models/company-mangement';
import Park, { ParkInfo } from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';

import House from '~models/house';
import operateDialog from './components/operate-dialog.vue';
// import Building from '~models/building';
import {
  columns,
} from './config';

type Operatetypes = 'unbund'|'view'|'rent'

// const a:string = 1;
export default defineComponent({
  name: 'OccupancyRecord',
  components: { searchBox },
  setup(props) {
    // const a:string = 1;
    const router = useRouter();
    const companyInstance = new CompanyMangement();
    const parkInstance = new Park();

    // const buildingOpts = ref<SelectOptions>([]);
    // const houseOpts = ref<SelectOptions>([]);
    const {
      parkOpts,
      buildingOpts,
      houseOpts,
      getParkOpts,
      getHouseOpts,
      getBuildingOpts,
    } = useParkInfoOpts();
    // const parkOpts = ref<SelectOptions>([]);
    const searchConf = reactive<SearchConf<['companyName', 'parkOrgId', 'buildingId', 'houseId']>>({
      companyName: {
        tag: 'input',
        value: '',
        placeholder: '请输入企业名称',
      },
      parkOrgId: {
        tag: 'select',
        options: parkOpts,
        value: '',
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
        handleChange: (value:string) => {},
      },
      buildingId: {
        tag: 'select',
        options: buildingOpts,
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
    });
    // const { companyId } = toRefs(props);

    searchConf.parkOrgId.handleChange = (parkOrgId:string) => {
      searchConf.buildingId.value = '';
      searchConf.houseId.value = '';
      getBuildingOpts(parkOrgId);
    };
    searchConf.buildingId.handleChange = (buildingId:string) => {
      searchConf.houseId.value = '';
      getHouseOpts(buildingId);
    };

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage<HouseBindRecord>(companyInstance.pageHouseBindRecord, searchConf, operateDialog);
    const { addCachePage } = useCachePage();
    /**
     * @description: 用户操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const operate = async (type:Operatetypes, record?:HouseBindRecord) => {
      operateDialogConf.type = type;
      if (type === 'view') {
        addCachePage('OccupancyRecord');
        router.push({
          name: 'companyMangementRenewalRecord',
          params: {
            id: record?.id as string,
          },
        });
        return;
      }
      if (!record) return;
      if (type === 'unbund') {
        Modal.confirm({
          content: `确定解绑【${record.houseName}】吗？`,
          onOk: async () => {
            await companyInstance.unbindHouse(record.id as string);
            message.success('操作成功');
            search(true);
          },
        });
        return;
      }
      //   console.log('续租');
      operateDialogConf.data = record;
      operateDialogConf.visible = true;
      operateDialogConf.title = '续租房屋';
    //     }
    //   }
    };
    const init = async () => {
    //   buildingOpts.value = await new Floor().listBuildingDict(props.parkOrgId);
      getParkOpts();
      search(true);
    };
    init();
    onMounted(() => {
      // console.log('3.-组件挂载到页面之后执行-------onMounted');
    });
    return {
      columns,
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
