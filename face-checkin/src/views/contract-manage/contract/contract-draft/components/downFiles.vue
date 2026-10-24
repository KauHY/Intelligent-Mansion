<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-20 17:57:33
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    v-model:visible="currentVisible"
    :width="730"
    :title="partyConfig.title"
    class="select-partyA-dialog"
    :footer="null"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div>
      <search-box :search-conf="searchConf">
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
            <template #operate="{record}">
              <div>
                <!-- <a-button
                  v-if="record.contractKey && record.contractKeyTime"
                  type="link"
                  @click="operate('export',record.id)"
                >
                  预览
                </a-button> -->
                <a-button
                  v-if="record.contractKey && record.contractKeyTime"
                  type="link"
                  @click="operate('export',record)"
                >
                  下载word
                </a-button>
                <a-button
                  v-if="record.contractKey && record.contractKeyTime"
                  type="link"
                  @click="operate('created',record)"
                >
                  重新生成
                </a-button>
                <a-button
                  v-else
                  type="link"
                  @click="operate('created',record)"
                >
                  {{ partyConfig.type==='1'?'生成协议':'生成合同' }}
                </a-button>
              </div>
            </template>
          </v-table>
          <div>
            注意：生成{{ partyConfig.type==='1'?'协议':'合同' }}时，覆盖原有模板{{ partyConfig.type==='1'?'协议':'合同' }}
          </div>
        </template>
      </search-box>
    </div>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, reactive, computed, defineComponent, onMounted, toRefs, PropType,
} from 'vue';
import { message } from 'ant-design-vue';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
import BusinessInvitation, { CustomerManagementInfo } from '~models/wisdom-canvass-business';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage, useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import ContractManage, { ContractTemplateListInfo, ContractType } from '~models/contract-manage';

const contractManageInstance = new ContractManage();
export default defineComponent({
  components: { searchBox },
  props: {
    partyConfig: {
      type: Object as PropType<{ title: string, type: string, id:string, parkOrgId:string }>,
      default: () => ({}),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    getPartyData: (data: CustomerManagementInfo | CompanyInfo) => true,
  },
  setup(props, { emit }) {
    const options = useHashMapDics(['COMPANY_TYPE']);
    const companyInstance = new CompanyMangement();
    const businessInvitationInstance = new BusinessInvitation();
    const searchConf = reactive<SearchConf<['type', 'parkOrgId']>>({
      type: {
        tag: 'input',
        value: Number(props.partyConfig.type),
        hidden: true,
      },
      parkOrgId: {
        tag: 'input',
        value: props.partyConfig.parkOrgId,
        hidden: true,
      },
    });
    const selectedRowKeys = ref<string[]>([]);
    const selectedRow = ref<CustomerManagementInfo | CompanyInfo>();
    const columns = [
      {
        title: '所用模板',
        dataIndex: 'templateName',
      },
      {
        title: '生成时间',
        dataIndex: 'contractKeyTime',
      },
      {
        title: '操作',
        dataIndex: 'operate',
        width: 300,
        slots: { customRender: 'operate' },
      },
    ];
    const apiStr = {
      partyA: companyInstance.searchListPage,
      partyB: businessInvitationInstance.customerManagementListpage,
    };
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(
      contractManageInstance.templateListPage,
      { ...searchConf },
    );
    const operate = async (type:any, record?:any) => {
      let data = null;
      switch (type) {
        case 'export':
          window.location.href = `${window.location.origin}/smartPark/park-common/file/download?key=${record.contractKey}`;
          break;
        case 'created':
          data = await contractManageInstance.generateContract({
            contractId: props.partyConfig.id,
            contractTemplateId: record.id,
          });
          if (data) {
            const toastMess = props.partyConfig.type === '1' ? '协议文档生成成功' : '合同文档生成成功';
            message.success(toastMess);
          } else {
            const toastMess = props.partyConfig.type === '1' ? '协议文档生成失败，请重新生成' : '合同文档生成失败，请重新生成';
            message.success(toastMess);
          }
          search(true);
          break;
        default:
          break;
      }
    };
    const confirm = () => {
      if (!selectedRow.value) return;
      emit('getPartyData', selectedRow.value);
      emit('update:show', false);
    };
    const onSelectChange = (keys: []) => {
      selectedRowKeys.value = keys;
    };
    const rowSelect = (record: CustomerManagementInfo | CompanyInfo) => {
      selectedRow.value = record;
    };
    const init = () => {
      search(true);
    };
    onMounted(() => {
      init();
    });
    return {
      operate,
      confirm,
      emit,
      columns,
      selectedRowKeys,
      onSelectChange,
      selectedRow,
      rowSelect,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      currentVisible: true,
    };
  },
});
</script>
<style scoped lang="scss">
.select-partyA-dialog {
    text-shadow: 0;
}
</style>
