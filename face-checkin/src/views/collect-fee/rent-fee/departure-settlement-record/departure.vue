<template>
  <div class="park-details content-section">
    <div class="content-scroll">
      &nbsp;&nbsp;&nbsp;<a-button
        type="primary"
        @click="goBack"
      >
        {{ goBackTag }}
      </a-button>
      <a-form
        ref="formRef"
        :model="modelRef"
        label-align="left"
        :label-col="{span:6}"
        :rules="rulesRef"
      >
        <a-card
          v-if="stepStatus===0"
          title="离场结算"
          :bordered="false"
        >
          <div class="item-box">
            <a-form-item
              label="所属园区"
              name="parkOrgId"
            >
              <v-select
                v-model:value="modelRef.parkOrgId"
                placeholder="请选择所属园区"
              >
                <a-select-option
                  v-for="opt in parkOpts"
                  :key="opt.value"
                  :value="opt.value"
                  @click="parkOrgIdChange(opt.value,opt.label )"
                >
                  {{ opt.label }}
                </a-select-option>
              </v-select>
            </a-form-item>
            <a-form-item
              label="入驻企业名称"
              name="companyId"
            >
              <v-select
                v-model:value="modelRef.companyId"
                filter-option

                placeholder="请选择入驻企业"
              >
                <a-select-option
                  v-for="opt in tagOpts"
                  :key="opt.label"
                  :value="opt.value"
                  @click="changeCompany(opt.value,opt.label )"
                >
                  {{ opt.label }}
                </a-select-option>
              </v-select>
            </a-form-item>
            <a-form-item
              label="退租合同"
              name="contractId"
            >
              <v-select
                v-model:value="modelRef.contractId"
                :disabled="!modelRef.companyId || !modelRef.parkOrgId"
                placeholder="请选择退租合同"
              >
                <a-select-option
                  v-for="opt in option"
                  :key="opt.value"
                  @click="changeContractId(opt.value,opt.label )"
                >
                  {{ opt.label }}
                </a-select-option>
              </v-select>
            </a-form-item>
            <a-form-item
              label="结算日期"
              name="leaveDate"
            >
              <a-date-picker
                v-model:value="modelRef.leaveDate"
                style="width: 100%;"

                placeholder="请选择结算日期"
                value-format="YYYY-MM-DD"
              />
            </a-form-item>
          </div>
        </a-card>
        <a-card
          v-if="stepStatus===1"
          :title="`入驻企业名称:  ${contractLeaveOption?.companyName}`"
          :bordered="false"
        >
          <div class="item-box">
            <a-form-item
              label="所属园区"
              :model="contractLeaveOption"
            >
              <span>{{ contractLeaveOption?.parkOrgName||'-' }}</span>
            </a-form-item>
            <a-form-item
              label="结算合同"
            >
              <span>{{ contractLeaveOption?.contractNo }}</span>
            </a-form-item>
            <a-form-item
              label="结算日期"
            >
              <span>{{ contractLeaveOption?.leaveDate||'-' }}</span>
            </a-form-item>
            <a-form-item
              v-if="contractType!==4"
              label="押金（元）"
            >
              <span>{{ contractLeaveOption?.deposit||'-' }}</span>
            </a-form-item>
            <div>
              待缴纳账单明细
              (<span class="yellow-text">如有未出费用，请先在“未出租赁费用”页面生成账单，否则，离场结算确认后，未出费用项将自动废除。</span>)
            </div>
            <div class="item-box">
              <v-table
                row-key="id"
                bordered
                :columns="columns"
                :pagination="pagination"
                :data-source="contractLeaveOption?.list"
                @change="pageChange"
              />
              <div style="text-align: right;">
                待收总金额（元）<span>{{ contractLeaveOption?.totalAmount }}</span>
              </div>
            </div>
          </div>
        </a-card>
      </a-form>
      <div class="buttons">
        <a-button
          v-if="stepStatus===0"
          type="primary"
          @click="comparedCost"
        >
          计算费用
        </a-button>
        <a-button
          v-if="stepStatus===0"
          @click="goBack"
        >
          返回
        </a-button>
        <a-button
          v-if="stepStatus===1"
          type="primary"
          @click="goCurrent"
        >
          上一步
        </a-button>
        <a-button
          v-if="stepStatus===1"
          @click="compareFee"
        >
          下一步
        </a-button>
      </div>
    </div>
    <getEntryEditAdd
      v-if="getEntryFlag"
      ref="customer"
      v-model:show="getEntryFlag"
      :customer-config="getEntryConfig"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';

import CustomerVo, { SupplyListItem } from '~models/company';
import { useSearchListPage } from '~composition/common';
import Park from '~models/park';
import CollectFee, { BillingScheme } from '~models/collect-fee';
import CompanyMangement from '~models/company-mangement';
import { getDetailColumns } from './config';
import { customerForm } from './composition/useForm';
import getEntryEditAdd from './components/get-entry.vue';

interface AdvertConfig {
  parkOrgName?: string
  contractNo?: string
  leaveDate?: string
  deposit?: string
  list?:Object,
  totalAmount?: 0,
  title: string
  type: string
  customerData?: object
  companyName?: string
}

type Operatetypes = 'add' | 'detail' | 'export'
const collectFeeInessInstance = new CollectFee();
export default defineComponent({
  components: {
    getEntryEditAdd,
  },
  props: {
    contractType: { // 合同费用类型3-租赁；4-物业；5-公寓
      type: Number as PropType<3|4|5>,
      default: 3,
    },
  },
  setup(props) {
    const clearTimeSet = ref();
    const columns = getDetailColumns();
    const router = useRouter();
    const visible = ref<boolean>(false);
    const stepStatus = ref<Number>(0);
    const goBackTag = ref<String>('返回');
    const option = ref<SelectOptions>([]);
    const contractLeaveOption = ref<AdvertConfig>();
    const informationFlag = ref<Boolean>(false);
    const informationConfig = ref<AdvertConfig>();
    const getEntryFlag = ref<Boolean>(false);
    const getEntryConfig = ref<AdvertConfig>();
    const customerFlag = ref<Boolean>(false);
    const customerConfig = ref<AdvertConfig>();
    const importFlag = ref<Boolean>(false);
    const formRef = ref();
    const { modelRef, rulesRef } = customerForm();
    const {
      parkOpts, tagOpts, parkOrgIdChange,
    } = useOptions(modelRef);

    const {
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
    } = useSearchListPage(collectFeeInessInstance.billingSchemeListPage);
    const comparedCost = async () => {
      formRef.value.validate().then(async () => {
        try {
          const data = await collectFeeInessInstance.leaveSettlement({
            ...modelRef,
            contractType: props.contractType,
          });
          console.log(data, 'data');
          contractLeaveOption.value = data;
          stepStatus.value = 1;
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const goBack = async () => {
      const names = {
        3: 'departureSettlementRecord',
        4: 'propertyDepartureSettlementRecord', // 公寓
        5: 'apartmentDepartureSettlementRecord', // 物业
      };
      router.push({ name: names[props.contractType] });
    };
    const goCurrent = async () => {
      stepStatus.value = 0;
    };
    const changeContractId = async (value:string, label:string) => {
      modelRef.contractNo = label;
    };
    const changeCompany = async (value:string, label:string) => {
      modelRef.companyName = label;
      option.value = await collectFeeInessInstance.getDictListByType(value, modelRef.parkOrgId, props.contractType);
    };
    const compareFee = async () => {
      if (contractLeaveOption.value && contractLeaveOption.value.totalAmount === 0) {
        message.warning('待收总金额为0，无需缴费，该页面将在5秒后自动关闭');
        const timeId = setInterval(() => {
          goBack();
          clearInterval(timeId);
        }, 5000);
      } else {
        getEntryFlag.value = true;
        getEntryConfig.value = {
          title: '结算缴费',
          type: 'add',
          customerData: contractLeaveOption.value || [],
        };
      }
    };
    const handleCancel = () => {
      visible.value = false;
    };

    const init = async () => {
    };
    onMounted(() => {
      init();
    });
    return {
      clearTimeSet,
      goCurrent,
      contractLeaveOption,
      formRef,
      changeContractId,
      option,
      changeCompany,
      comparedCost,
      stepStatus,
      goBackTag,
      columns,
      compareFee,
      goBack,
      pagination,
      pageChange,
      tableList,
      search,
      operateDialogConf,
      modelRef,
      rulesRef,
      tagOpts,
      parkOrgIdChange,
      parkOpts,
      customerFlag,
      customerConfig,
      importFlag,
      getEntryConfig,
      getEntryFlag,
      informationFlag,
      informationConfig,
      handleCancel,
      visible,
    };
  },
});
function useOptions(modelRef?:any) {
  const parkOpts = ref<SelectOptions>([]);
  const tagOpts = ref<SelectOptions>([]);
  const getTagOpts = async (value:string) => {
    tagOpts.value = await new CompanyMangement().listCompanyDict(value);
  };
  (async () => {
    parkOpts.value = await new Park().getTenantParkOrgDict();
    // 新增默认选择园区列表第一个
    modelRef.parkOrgId = parkOpts.value[0].value;
    modelRef.parkOrgName = parkOpts.value[0].label;
    getTagOpts(modelRef.parkOrgId);
  })();
  const parkOrgIdChange = async (value: string, label:any) => {
    modelRef.parkOrgName = label;
    modelRef.companyId = '';
    getTagOpts(value);
  };
  return {
    parkOrgIdChange,
    parkOpts,
    tagOpts,
    getTagOpts,
  };
}
</script>
<style lang="scss" scoped>
.park-details {
    position: relative;
    padding: 24px 0;
    overflow: hidden;

    .yellow-text {
        margin-left: 5px;
        color: #f59a23;
    }

    .go-back {
        margin-left: 24px;
        cursor: pointer;
    }

    :deep(.ant-card-body) {
        display: flex;
        justify-content: center;
    }

    .item-box {
        width: 80%;
    }

    .content-scroll {
        padding-bottom: 80px;
    }

    .ant-form {
        padding-bottom: 80px;
    }

    .buttons {
        position: absolute;
        bottom: 0;
        width: 100%;
        padding: 15px;
        text-align: center;
        background: #fff;
        box-shadow: 0 0 10px 0 #f0f0f0;

        >button {
            margin-left: 20px;
        }
    }
}
</style>
