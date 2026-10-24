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
          @click="manipulate('add')"
        >
          供应发布
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
          <template #name="{ record }">
            <span
              class="text-link"
              @click="manipulate('detail', record)"
            >{{ record.name }}</span>
          </template>
          <template #operate="{ record }">
            <div>
              <a-button
                v-if="record.state===0"
                type="link"
                @click="manipulate('edit', record)"
              >
                编辑
              </a-button>
              <a-button
                v-if="record.state===0"
                type="link"
                @click="manipulate('del', record)"
              >
                删除
              </a-button>
              <a-button
                v-if="record.state===0"
                type="link"
                @click="manipulate('pulish', record)"
              >
                发布
              </a-button>
              <a-button
                v-if="record.state===1"
                type="link"
                @click="manipulate('unpulish', record)"
              >
                下架
              </a-button>
            </div>
          </template>
        </v-table>
      </template>
    </search-box>
    <SupplyAddEdit
      v-if="customerFlag"
      ref="customer"
      v-model:show="customerFlag"
      :customer-config="customerConfig"
      @refresh="operateSuccess(1)"
    />
    <!-- 企业信息 -->
    <InformationEditAdd
      v-if="informationFlag"
      ref="customer"
      v-model:show="informationFlag"
      :customer-config="informationConfig"
      @refresh="operateSuccess(2)"
    />
    <!-- 申请入驻 -->
    <getEntryEditAdd
      v-if="getEntryFlag"
      ref="customer"
      v-model:show="getEntryFlag"
      :customer-config="getEntryConfig"
      @refresh="operateSuccess(3)"
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
import CustomerVo, { SupplyListItem } from '~models/company';
import searchBox, { SearchConf } from '~components/search-box/search-box.vue';
import { useSearchListPage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';
import { getCustomerColumns } from './config';
import SupplyAddEdit from './components/SupplyAddEdit.vue';
import InformationEditAdd from './components/operate-dialog.vue';
import getEntryEditAdd from './components/get-entry.vue';

interface AdvertConfig {
  title: string
  type: string
  customerData?: object
}

type Operatetypes = 'add' | 'edit' | 'del' | 'detail' | 'import'|'pulish'|'unpulish'
const CustomerVoReq = new CustomerVo();
export default defineComponent({
  components: {
    searchBox, SupplyAddEdit, InformationEditAdd, getEntryEditAdd,
  },
  setup() {
    const router = useRouter();
    const visible = ref<boolean>(false);
    const informationFlag = ref<Boolean>(false);
    const informationConfig = ref<AdvertConfig>();
    const getEntryFlag = ref<Boolean>(false);
    const getEntryConfig = ref<AdvertConfig>();
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const options = useHashMapDics<['SUPPLY_TYPE', 'SUPPLY_PUBLISH_STATUS', 'ADD_DATA_SOURCE']>(['SUPPLY_TYPE', 'SUPPLY_PUBLISH_STATUS', 'ADD_DATA_SOURCE']);
    const {
      getParkOpts,
    } = useParkInfoOpts();
    const searchConf = reactive<SearchConf<['name', 'supplyType']>>({
      name: {
        tag: 'input',
        placeholder: '请输入服务名称',
        value: '',
      },
      supplyType: {
        tag: 'select',
        value: '',
        options: () => options.SUPPLY_TYPE,
        placeholder: '请选择供应类型',
      },

    });
    const columns = getCustomerColumns(options);
    const {
      pagination, pageChange, tableList, search, download,
    } = useSearchListPage(
      CustomerVoReq.listSupplyServicePageSupply,
      searchConf,
      null,
    );
    const manipulate = async (type: Operatetypes, record?: SupplyListItem) => {
      let data = null;
      switch (type) {
        case 'add':
          data = await CustomerVoReq.getDetail();
          if (!data) {
            Modal.confirm({
              content: '请先完善企业信息并入驻后，再进行此操作',
              okText: '完善企业信息',
              onOk: async () => {
                informationFlag.value = true;
                informationConfig.value = {
                  title: '企业信息',
                  type: 'add',
                };
              },
            });
          } else {
            data = await CustomerVoReq.settled();
            if (data && data.length > 0) {
              customerConfig.value = {
                title: '新增服务',
                type,
              };
              customerFlag.value = true;
            } else if (data && data.length === 0) {
              Modal.confirm({
                content: '请先入驻园区后，再进行此操作',
                okText: '申请入驻',
                onOk: async () => {
                  getEntryFlag.value = true;
                  getEntryConfig.value = {
                    title: '入驻园区',
                    type: 'add',
                  };
                },
              });
            } else {
              Modal.confirm({
                content: '企业信息审核中，请审核通过后再进行此操作',
                okText: '查看审核结果',
                onOk: async () => {
                  router.push({ name: 'enterpriseInformation' });
                },
              });
            }
          }
          break;
        case 'edit':
        case 'detail':
          if (!record) return;
          data = await CustomerVoReq.findByIdSupply(record.id);
          customerConfig.value = {
            title: type === 'edit' ? '编辑服务' : '服务详情',
            type,
            customerData: data,
          };
          customerFlag.value = true;
          break;
        case 'del':
          if (!record) return;
          Modal.confirm({
            content: `确定删除【${record.name}】吗？`,
            onOk: async () => {
              await CustomerVoReq.removeSupply(record.id);
              message.success('操作成功');
              operateSuccess();
            },
          });
          break;
        case 'unpulish':
          if (!record) return;

          await CustomerVoReq.changeState(record.id, 2);
          operateSuccess();

          break;
        case 'pulish':
          if (!record) return;
          await CustomerVoReq.changeState(record.id, 1);
          operateSuccess();
          break;

        default:
          break;
      }
    };
    const handleCancel = () => {
      visible.value = false;
    };
    const operateSuccess = (type?:any) => {
      if (type && type === 2) {
        informationFlag.value = false;
      } else if (type && type === 1) {
        customerFlag.value = false;
      } else {
        getEntryFlag.value = false;
      }
      search(true);
    };
    const importSuccess = async () => {
      try {
        operateSuccess();
        message.success('操作成功');
      } catch (error) {
        console.log('上传失败');
      }
    };
    search(true);
    const init = async () => {
      getParkOpts();
    };
    onMounted(() => {
      init();
    });
    return {
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
      importFlag,
      importSuccess,
      getEntryConfig,
      getEntryFlag,
      informationFlag,
      informationConfig,
      handleCancel,
      visible,
    };
  },
});
</script>
