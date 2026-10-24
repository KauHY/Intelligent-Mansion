<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-03-31 17:25:13
 * @LastEditors: Xucaizhi3
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
          <template #companyInformationName="{ record }">
            <span
              class="text-link"
              @click="operate('detail', record)"
            >
              {{ record.companyInformationName }}
            </span>
          </template>
          <template #operate="{ record }">
            <a-button
              v-if="record.auditStatus === 1"
              type="link"
              @click="operate('verify', record)"
            >
              审核
            </a-button>
            <span v-else>-</span>
          </template>
        </v-table>
      </template>
    </search-box>
    <CompanyCheck
      v-if="checkFlag"
      ref="check"
      v-model:show="checkFlag"
      :company-check-config="companyCheckConfig"
      @refresh="operateSuccess"
    />
  </div>
</template>

<script lang="ts">
import {
  ref,
  reactive,
  computed,
  defineComponent,
  onMounted,
  onActivated,
  PropType,
  toRef,
  toRefs,
  watch,
} from 'vue';
import { useRouter } from 'vue-router';
import Park from '~models/park';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { useSearchListPage, useCachePage } from '~composition/common';
import CompanyMangementVo, { CompanyCheckInfo } from '~models/company-mangement';
import { getCompanyCheckColumns } from './config';
import CompanyCheck from './components/CompanyCheck.vue';

type Operatetypes = 'verify' | 'detail';
interface CompanyCheckConfig {
  title: string,
  type: string
  checkData?: object
}
const CompanyMangementVoReq = new CompanyMangementVo();
export default defineComponent({
  components: { searchBox, CompanyCheck },
  props: {
    selectTreeNode: {
      type: Object as PropType<{ name: string; key: string; type: number }>,
      default: () => ({}),
    },
  },
  setup(props) {
    const parkOpts = ref<SelectOptions>([]);
    const checkFlag = ref<Boolean>(false);
    const companyCheckConfig = ref<CompanyCheckConfig>();
    const options = useHashMapDics<['COMPANY_TYPE', 'SYS_COMPANY_AUDIT_STATUS']>([
      'COMPANY_TYPE',
      'SYS_COMPANY_AUDIT_STATUS',
    ]);
    const searchConf = reactive<
      SearchConf<['name', 'companyType', 'parkOrgId', 'auditStatus']>
    >({
      name: {
        tag: 'input',
        value: '',
        placeholder: '请输入企业名称',
      },
      companyType: {
        tag: 'select',
        value: '',
        options: () => options.COMPANY_TYPE,
        placeholder: '请选择企业类型',
      },
      parkOrgId: {
        tag: 'select',
        value: '',
        options: parkOpts,
        filterOption: true,
        showSearch: true,
        placeholder: '请选择园区',
      },
      auditStatus: {
        tag: 'select',
        value: '',
        options: options.SYS_COMPANY_AUDIT_STATUS,
        placeholder: '请选择状态',
      },
    });
    const columns = getCompanyCheckColumns(options);
    const {
      pagination, pageChange, tableList, search,
    } = useSearchListPage(
      CompanyMangementVoReq.companyCheckListPageApi,
      searchConf,
      null,
    );
    const operate = async (type: Operatetypes, record?: CompanyCheckInfo) => {
      let data = null;
      const title = type === 'verify' ? '审核入驻' : '申请详情';
      switch (type) {
        case 'verify':
        case 'detail':
          if (!record) return;
          data = await CompanyMangementVoReq.companyCheckDetailApi(record.id);
          companyCheckConfig.value = {
            title,
            type,
            checkData: {
              checkType: 'check', // check 企业入驻 checkInfo: 企业信息
              contactPhone: data.contactPhone,
              parkOrgName: record.parkOrgName,
              contactPerson: data.contactPerson,
              auditStatus: data.auditStatus,
              auditMsg: data.auditMsg,
              auditUserName: data.auditUserName,
              ...data.companyInformation,
              id: record.id,
              applyTime: record.applyTime,
              auditTime: record.auditTime,
            },
          };
          checkFlag.value = true;
          break;
        default:
          break;
      }
    };
    const operateSuccess = () => {
      search(true);
    };
    const init = async () => {
      search(true);
      parkOpts.value = await new Park().searchParkListName();
    };
    init();
    return {
      searchConf,
      search,
      pagination,
      pageChange,
      tableList,
      columns,
      options,
      operate,
      checkFlag,
      operateSuccess,
      companyCheckConfig,
    };
  },
});
</script>
<style lang="scss">
.info-css {
    margin-bottom: 20px;
}
</style>
