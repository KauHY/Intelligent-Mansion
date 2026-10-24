<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-05-27 12:55:09
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <div class="content-section">
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
          @click="download"
        >
          导出
        </a-button>&nbsp;&nbsp;
      </template>
      <template #table="{ scroll }">
        <v-table
          row-key="id"
          bordered
          :columns="columns"
          :pagination="pagination"
          :data-source="tableList"
          :scroll="scroll"
          @change="pageChange"
        >
          <template #no="{ record }">
            <span
              class="table-text-link"
              @click="operate('detail', record)"
            >{{ record.no }}</span>
          </template>
          <template #operate="{ record }">
            <div>
              <a-button
                v-if="record.state === 1"
                type="link"
                @click="operate('edit', record)"
              >
                编辑
              </a-button>
              <a-button
                v-if="record.state === 1"
                type="link"
                @click="operate('nullify', record)"
              >
                作废
              </a-button>
              <a-button
                v-if="record.state === 1"
                type="link"
                @click="operate('sendVerify', record)"
              >
                提交审核
              </a-button>
              <a-button
                v-if="record.state === 3 || record.state === 4"
                type="link"
                @click="operate('change', record)"
              >
                变更
              </a-button>
              <a-button
                v-if="record.state === 4 || record.state === 8"
                type="link"
                @click="operate('extend', record)"
              >
                续约
              </a-button>
              <a-button
                v-if="record.state === 8"
                type="link"
                @click="operate('termination', record)"
              >
                终止
              </a-button>
              <a-button
                v-if="record.state === 3 || record.state === 4"
                type="link"
                @click="operate('withdrawal', record)"
              >
                退约
              </a-button>
              <a-button
                v-if="record.state === 7"
                type="link"
                @click="operate('del', record)"
              >
                删除
              </a-button>
              <a-button
                v-if="record.state === 2 || record.state === 9"
                type="link"
                @click="operate('verify', record)"
              >
                审核
              </a-button>
              <a-button
                type="link"
                @click="operate('copy', record)"
              >
                复制
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <ContractTerminationAndChange
      v-if="tcFlag"
      ref="contract"
      v-model:show="tcFlag"
      :tc-config="tcConfig"
      @refresh="operateSuccess"
    />
  </div>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, onActivated, PropType, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRouter, useRoute } from 'vue-router';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import ContractVo, { ContractList, ContractType, Operatetypes } from '~models/contract-manage';
import Park from '~models/park';
import House, { HouseInfo, SearchParams, TreeNodeItem } from '~models/house';
import Building, { FloorListItem } from '~models/building';
import { useSearchListPage, useCachePage } from '~composition/common';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import ContractTerminationAndChange from './components/ContractTerminationAndChange.vue';
import { getColumns, getFieldPrefix } from './config';
// '待提交1', '审核中2', '待生效3', '生效中4', '已退约5', '已失效6', '已作废7', '已过期8' '退约审核9'
interface TcConfig {
  title: string
  type: string
  tcData: any
}
const ContractVoReq = new ContractVo();
export default defineComponent({
  components: { searchBox, ContractTerminationAndChange },
  props: {
    contractType: {
      // 1: 入园协议 2: 招引合同 3: 租赁合同管理 4: 物业合同管理 5: 公寓合同管理
      type: Number as PropType<ContractType>,
      default: 1,
    },
  },
  setup(props) {
    const { contractType } = toRefs(props);
    const fieldPrefix = getFieldPrefix(contractType.value);
    // console.log(contractType.value, typeof contractType.value, 'contractType.value');
    const router = useRouter();
    const options = useHashMapDics(['CONTRACT_TYPE', 'CONTRACT_STATE', 'CONTRACT_ATTRIBUTES', 'PROTOCOL_ATTRIBUTES']);
    const columns = getColumns(options, contractType.value);
    const parkOpts = ref<SelectOptions>([]);
    const buildingIdOptions = ref<SelectOptions>([]);
    const houseIdOptions = ref<any>([]);
    const houseInstance = new House();
    const floorInstance = new Building();
    const tcFlag = ref<Boolean>(false);
    const tcConfig = ref<TcConfig>();
    const searchConf = reactive<SearchConf<['parkOrgId', 'buildingName', 'houseId', 'type', 'date', 'state', 'nameB', 'no']>>({
      no: {
        tag: 'input',
        value: '',
        placeholder: `请输入${fieldPrefix}编号`,
      },
      type: {
        tag: 'select',
        value: contractType.value,
        hidden: true,
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        placeholder: '请选择园区',
        options: () => parkOpts.value,
        filterOption: true,
        showSearch: true,
      },
      nameB: {
        tag: 'input',
        value: '',
        placeholder: '请输入入驻企业名称',
      },
      buildingName: {
        tag: 'select',
        value: '',
        hidden: contractType.value === 2,
        valueKey: 'label',
        showSearch: true,
        filterOption: (input: string, option: any) => {
          const label = option.key.split('_')[0];
          return label.includes(input);
        },
        options: buildingIdOptions,
        handleChange: (value:string) => {},
        placeholder: '请输入楼栋单元',
      },
      houseId: {
        tag: 'select',
        value: '',
        hidden: contractType.value === 2,
        showSearch: true,
        filterOption: (input: string, option: any) => {
          const label = option.key.split('_')[0];
          return label.includes(input);
        },
        options: houseIdOptions,
        placeholder: '请输入房号',
      },
      date: {
        tag: 'date-range',
        value: '',
        hidden: contractType.value === 2,
        placeholder: [`${fieldPrefix}有效期开始时间`, `${fieldPrefix}有效期结束时间`],
        endTimeKey: 'endDate',
        startTimeKey: 'startDate',
      },
      state: {
        tag: 'select',
        value: '',
        hidden: contractType.value === 2,
        placeholder: `请选择${fieldPrefix}状态`,
        options: () => options.CONTRACT_STATE,
      },
    });
    // 园区切换时
    const parkChange = async () => {
      if (!searchConf.parkOrgId.value) {
        buildingIdOptions.value = [];
        houseIdOptions.value = [];
        searchConf.buildingName.value = '';
        searchConf.houseId.value = '';
        return;
      }
      buildingIdOptions.value = await floorInstance.listBuildingDict(searchConf.parkOrgId.value as string);
      console.log(buildingIdOptions, '---buildingIdOptions');
    };
    // 楼栋切换时
    const buildingChange = async (value:string) => {
      searchConf.houseId.value = '';
      if (!value) {
        houseIdOptions.value = [];
        return;
      }
      buildingIdOptions.value.forEach((item) => {
        if (item.label === value) {
          getHouseFun(item.value);
        }
      });
    };
    const getHouseFun = async (value:string) => {
      houseIdOptions.value = await houseInstance.searchHouseOptsOne(value);
    };
    searchConf.buildingName.handleChange = buildingChange;
    searchConf.parkOrgId.handleChange = parkChange;
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(ContractVoReq.contractListPageApi, searchConf, null, ContractVoReq.contractExportApi);
    const operate = (type: Operatetypes, record?: ContractList) => {
      switch (type) {
        case 'add':
          console.log('add');
          router.push({ path: '/contract-manage/contract-draft', query: { handleType: type, contractType: contractType.value } });
          break;
        case 'export':
          console.log('export');
          break;
        case 'detail':
        case 'verify':
        case 'edit':
        case 'copy':
        case 'extend':
          if (!record) return;
          console.log('edit');
          router.push({
            path: '/contract-manage/contract-draft-edit',
            query: {
              id: record.id, handleType: type, contractType: contractType.value, state: record.state,
            },
          });
          break;
        case 'nullify':
          console.log('nullify');
          if (!record) return;
          Modal.confirm({
            content: `确定要将此${fieldPrefix}作废？作废后将不可恢复？`,
            onOk: async () => {
              await ContractVoReq.contractNullifyApi(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'sendVerify':
          console.log('sendVerify');
          if (!record) return;
          Modal.confirm({
            content: '确定提交审核？',
            onOk: async () => {
              await ContractVoReq.contractSubmitForReviewApi(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'change':
          if (!record) return;
          router.push({
            path: '/contract-manage/contract-draft-edit',
            query: {
              id: record.id, handleType: type, contractType: record.type, changeType: 1,
            },
          });
          break;
        case 'withdrawal':
          if (!record) return;
          tcConfig.value = {
            title: `${fieldPrefix}退约`,
            type,
            tcData: record,
          };
          tcFlag.value = true;
          break;
        case 'termination':
          console.log('termination');
          if (!record) return;
          Modal.confirm({
            content: `确定终止该${fieldPrefix}？`,
            onOk: async () => {
              await ContractVoReq.contractTerminationApi(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'del':
          console.log('del');
          if (!record) return;
          Modal.confirm({
            content: `确定删除该${fieldPrefix}？一旦删除，此合同的所有数据将不可恢复`,
            onOk: async () => {
              await ContractVoReq.contractRemoveApi(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        default:
          break;
      }
    };
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
    };
    const operateSuccess = () => {
      search(true);
    };
    onMounted(() => {
      search(true);
      init();
    });
    return {
      getHouseFun,
      searchConf,
      search,
      operate,
      pagination,
      pageChange,
      tableList,
      columns,
      tcFlag,
      operateSuccess,
      tcConfig,
      download,
    };
  },
});
</script>
