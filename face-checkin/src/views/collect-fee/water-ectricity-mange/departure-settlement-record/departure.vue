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
              v-if="contractType===5"
              label="结算房间"
              name="houseId"
            >
              <v-select
                v-model:value="modelRef.houseId"
                :disabled="!modelRef.contractId"
                placeholder="请选择结算房间"
              >
                <a-select-option
                  v-for="opt in houseOption"
                  :key="opt.value"
                  @click="changeHouseId(opt.value,opt.label )"
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
              v-if="contractType===5"
              label="结算房间"
            >
              <span>{{ contractLeaveOption?.houseName }}</span>
            </a-form-item>
            <a-form-item
              label="结算日期"
            >
              <span>{{ contractLeaveOption?.leaveDate||'-' }}</span>
            </a-form-item>
            <!-- <a-form-item
              v-if="contractType!==4"
              label="押金（元）"
            >
              <span>{{ contractLeaveOption?.deposit||'-' }}</span>
            </a-form-item> -->
            <div>
              待结算费用
              (<span class="yellow-text">此模块只结算未抄表的水电费，如有账单未缴纳。</span>)
            </div>
            <div class="item-box">
              <v-table
                row-key="id"
                bordered
                :columns="columns"
                :pagination="pagination"
                :data-source="contractLeaveOption.chargeDetail"
                @change="pageChange"
              >
                <template #thisReading="{record,index}">
                  <a-input
                    v-model:value="record.thisReading"
                    :maxlength="20"
                    placeholder="请输入当前读数"
                    @blur="validateReading($event, record,index )"
                  />
                </template>
              </v-table>
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
      :other-data="contractType"
    />
  </div>
</template>
<script lang="ts">
import {
  reactive, ref, onMounted, defineComponent, PropType,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import { useRouter } from 'vue-router';
import { REGS } from '@/constants/validate';

import { useSearchListPage } from '~composition/common';
import Park from '~models/park';
import CollectFee, { ChargeItem, RentFee } from '~models/collect-fee';
import CompanyMangement from '~models/company-mangement';
import { getDetailColumns } from './config';
import { customerForm } from './composition/useForm';
import getEntryEditAdd from './components/get-entry.vue';

interface AdvertConfig {
  parkOrgName?: string
  contractNo?: string
  leaveDate?: string
  deposit?: string
  chargeDetail?:Object,
  totalAmount?: string
  title: string
  type: string
  customerData?: object
  companyName?: string
  houseName?:string,
  parkOrgId?:string,
}

type Operatetypes = 'add' | 'detail' | 'export'
const collectFeeInessInstance = new CollectFee();
const rentFeeInstance = new RentFee();

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
    const columns = getDetailColumns();
    const router = useRouter();
    const visible = ref<boolean>(false);
    const flag = ref<boolean>(false);
    const stepStatus = ref<Number>(0);
    const goBackTag = ref<String>('返回');
    const option = ref<SelectOptions>([]);
    const houseOption = ref<SelectOptions>([]);
    const contractLeaveOption = ref<any>();
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
          const data = await rentFeeInstance.leaveSettlementmeterConsumptionLeave({
            ...modelRef,
            type: props.contractType === 3 ? 1 : 0,
          });
          contractLeaveOption.value = data;
          stepStatus.value = 1;
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const goBack = async () => {
      const names = {
        3: 'WaterAndEectricitySettlementRecord', // 企业
        5: 'WaterAndEectricityApartmentSettlementRecord', // 公寓
      };
      router.push({ name: names[props.contractType] });
    };
    const goCurrent = async () => {
      stepStatus.value = 0;
    };
    const changeContractId = async (value:string, label:string) => {
      modelRef.contractNo = label;
      houseOption.value = await rentFeeInstance.getHouseListByContract(value);
    };
    const changeHouseId = async (value:string, label:string) => {
    };
    const validateReading = async (e: any,
      bindHouseItem: any, index:number) => {
      if (!REGS.area.test(e.target.value)) {
        message.warn('请输入大于0的数字，小数点保留两位');
      } else {
        const data = await collectFeeInessInstance.computeAmount({
          lastReading: bindHouseItem.lastReading,
          parkOrgId: contractLeaveOption.value.parkOrgId,
          shareCount: bindHouseItem.shareCount,
          thisReading: e.target.value,
          type: bindHouseItem.type,
          needShare: bindHouseItem.needShare,
        });
        contractLeaveOption.value.chargeDetail[index].amountCount = data.amountCount;
        contractLeaveOption.value.chargeDetail[index].roomAmount = data.roomAmount;
        contractLeaveOption.value.chargeDetail[index].publicAmount = data.publicAmount;
        contractLeaveOption.value.chargeDetail[index].shareAmount = data.shareAmount;

        let totalList = 0;
        contractLeaveOption.value.chargeDetail.forEach((item: { amountCount: number; }) => {
          if (item.amountCount) {
            totalList += item.amountCount;
          }
        });
        contractLeaveOption.value.totalAmount = totalList;
        // console.log(totalList, 'totalList');
      }
    };
    const changeCompany = async (value:string, label:string) => {
      modelRef.companyName = label;
      option.value = await collectFeeInessInstance.getDictListByType(value, modelRef.parkOrgId, props.contractType);
    };
    const compareFee = async () => {
      flag.value = contractLeaveOption.value.chargeDetail.every((item: { thisReading: any; }) => !!item.thisReading);
      if (!flag.value) {
        message.warn('请输入所有收费项的当前读数！');
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
      flag,
      validateReading,
      houseOption,
      changeHouseId,
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
