<!--
 * @Description:员工管理
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-04 19:07:45
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-04 18:06:47
-->
<template>
  <div class="jobs-mangement content-section">
    <search-box :search-conf="searchConf">
      <template #searchOperate>
        <!-- <a-button
          type="primary"
          @click="search(true)"
        >
          查询
        </a-button>&nbsp;&nbsp; -->
        <a-button
          type="primary"
          @click="manipulate('add')"
        >
          申请入驻
        </a-button>&nbsp;&nbsp;
        <!-- <a-button
          type="primary"
          @click="manipulate('import')"
        >
          导入
        </a-button>&nbsp;&nbsp;
        <a-button
          type="primary"
          @click="download"
        >
          导出
        </a-button> -->
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
          <template #floor="{ record }">
            <span>{{ record.floor }}层</span>
          </template>
          <template #operate="{ record }">
            <div>
              <a-button
                v-if="record.auditStatus===3"
                type="link"
                @click="manipulate('edit', record)"
              >
                再次申请
              </a-button>
              <a-button
                v-if="record.auditStatus===1"
                type="link"
                @click="manipulate('del', record)"
              >
                取消申请
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <EntryAddEdit
      v-if="customerFlag"
      ref="customer"
      v-model:show="customerFlag"
      :customer-config="customerConfig"
      @refresh="operateSuccess(1)"
    />
    <v-modal
      v-model:visible="visible"
    >
      <p>请先完善企业信息后，再进行此操作</p>
      <template #footer>
        <a-button
          @click="handleCancel"
        >
          取消
        </a-button>
        <a-button
          type="primary"
          @click="confirm"
        >
          完善企业信息
        </a-button>
      </template>
    </v-modal>
    <InformationEditAdd
      v-if="informationFlag"
      ref="customer"
      v-model:show="informationFlag"
      :customer-config="informationConfig"
      @refresh="operateSuccess(2)"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, onMounted, defineComponent,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { useHashMapDics } from '~composition/useHashMapDic';
import CustomerVo from '~models/company';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import InformationEditAdd from '../components/operate-dialog.vue';
import { getCustomerColumns } from './config';
import EntryAddEdit from './components/EntryAddEdit.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import'
const CustomerVoReq = new CustomerVo();
export default defineComponent({
  components: {
    searchBox, EntryAddEdit, InformationEditAdd,
  },
  setup() {
    const router = useRouter();
    const visible = ref<boolean>(false);
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const informationFlag = ref<Boolean>(false);
    const informationConfig = ref<AdvertConfig>();
    const options = useHashMapDics<['SYS_COMPANY_AUDIT_STATUS']>(['SYS_COMPANY_AUDIT_STATUS']);
    const searchConf = reactive<SearchConf<[]>>({

    });
    const handleCancel = () => {
      visible.value = false;
    };
    const confirm = (e: MouseEvent) => {
      visible.value = false;
      informationFlag.value = true;
      informationConfig.value = {
        title: '企业信息',
        type: 'add',
      };
    };
    const columns = getCustomerColumns(options);
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      CustomerVoReq.listCompanyAuditPage,
      searchConf,
      null,
    );
    const manipulate = async (type: Operatetypes, record?: any) => {
      let data = null;
      switch (type) {
        case 'add':
          data = await CustomerVoReq.getDetail();
          if (!data) {
            visible.value = true;
          } else {
            data = await CustomerVoReq.settled();
            if (!data) {
              Modal.confirm({
                content: '企业信息审核中，请审核通过后再进行此操作',
                okText: '查看审核结果',
                onOk: async () => {
                  router.push({ name: 'enterpriseInformation' });
                },
              });
            } else {
              customerConfig.value = {
                title: '入驻申请',
                type,
              };
              customerFlag.value = true;
            }
          }

          break;
        case 'del':
          Modal.confirm({
            content: '确定取消入驻申请？',
            onOk: async () => {
              await CustomerVoReq.cancelAudit(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;

        case 'edit':
        case 'detail':
          if (!record) return;
          data = await CustomerVoReq.findById(record.id);
          customerConfig.value = {
            title: type === 'edit' ? '再次申请' : '设备详情',
            type,
            customerData: data,
          };
          customerFlag.value = true;
          break;

        default:
          break;
      }
    };
    const operateSuccess = (type?:any) => {
      if (type && type === 2) {
        informationFlag.value = false;
      } else if (type && type === 1) {
        customerFlag.value = false;
      }
      search(true);
    };
    const importSuccess = async () => {
      try {
        // await CustomerVoReq.customerRemoveApi('55555');
        operateSuccess();
        message.success('操作成功');
      } catch (error) {
        console.log('上传失败');
      }
    };
    search(true);
    const init = async () => {
    };
    onMounted(() => {
      init();
    });
    return {
      confirm,
      visible,
      options,
      columns,
      searchConf,
      pagination,
      pageChange,
      tableList,
      search,
      download,
      operateSuccess,
      manipulate,
      customerFlag,
      customerConfig,
      informationFlag,
      informationConfig,
      handleCancel,
      importSuccess,
    };
  },
});
</script>
