<!--
 * @Description: 新增商机
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-09 18:10:06
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-24 11:06:42
-->
<template>
  <div class="business-opportunity-add">
    <a-form
      :model="modelRef"
      :label-col="{span:8}"
    >
      <a-row>
        <a-col :span="12">
          <a-form-item
            label="商机名称"
            v-bind="validateInfos.chanceName"
          >
            <a-input
              v-model:value="modelRef.chanceName"
              :maxlength="30"
              placeholder="请输入商机名称"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            label="联系人"
            v-bind="validateInfos.contactName"
          >
            <a-input
              v-model:value="modelRef.contactName"
              :maxlength="30"
              placeholder="请输入联系人"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item
            label="联系人电话"
            v-bind="validateInfos.contactPhone"
          >
            <a-input
              v-model:value="modelRef.contactPhone"
              :maxlength="20"
              placeholder="请输入联系电话"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            label="联系人邮箱"
            v-bind="validateInfos.contactMail"
          >
            <a-input
              v-model:value="modelRef.contactMail"
              :maxlength="30"
              placeholder="请输入联系人邮箱"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item
            label="需求数量"
            v-bind="validateInfos.quantityNumber"
          >
            <a-input
              v-model:value="modelRef.quantityNumber"
              :maxlength="4"
              placeholder="请输入需求数量"
            />
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            label="期望价格（元/㎡/月）"
            v-bind="validateInfos.expectedPrice"
          >
            <a-input
              v-model:value="modelRef.expectedPrice"
              :maxlength="12"
              placeholder="请输入期望价格"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item
            label="关联园区"
            v-bind="validateInfos.parkOrgId"
          >
            <v-select
              v-model:value="modelRef.parkOrgId"
              :maxlength="20"
              placeholder="请选择关联园区"
              @change="parkOrgChange"
            >
              <a-select-option
                v-for="opt in parkOpts"
                :key="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            label="跟进人"
          >
            <!-- <employee-select
              v-model:value="modelRef.followEmployeeId"
              label-join-key="companyName"
              :org-id="modelRef.parkOrgId"
              :default-options="defaultOptions"
              placeholder="请输入关键字搜索"
            /> -->
            <v-select-employee
              v-model:value="modelRef.followEmployeeId"
              :default-select-opts="defaultOptions"
              placeholder="请选择跟进人"
              title="选择跟进人"
              job-codes="10"
              mode="single"
              :company-type="0"
              :park-org-ids="modelRef.parkOrgId"
            />
          </a-form-item>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="12">
          <a-form-item
            label="线索来源"
          >
            <v-select
              v-model:value="modelRef.threadSource"
              :maxlength="20"
              placeholder="请选择线索来源"
            >
              <a-select-option
                v-for="opt in hashMapDic.THREAD_SOURCE"
                :key="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item
            label="商机阶段"
            v-bind="validateInfos.chanceStage"
          >
            <v-select
              v-model:value="modelRef.chanceStage"
              :maxlength="20"
              placeholder="请选择线索来源"
            >
              <a-select-option
                v-for="opt in optionText"
                :key="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-form-item>
        </a-col>
      </a-row>
      <a-form-item
        label="意向房源"
        :label-col="{span:4}"
      >
        <house-card-list
          :house-list="houseHashMap"
          @del="delHouseInfo"
        >
          <!-- <a-button
            type="link"
            class="add-housing"
            :disabled="!modelRef.parkOrgId"
            @click="showHouseInfo"
          > -->
          <!-- <template #icon>
            <v-svg name="add-select" /> -->
          <span
            v-if="modelRef.parkOrgId"
            @click="showHouseInfo"
          >
            新增招商房屋
          </span>
          <!-- </template> -->
          <!-- </a-button> -->
        </house-card-list>
      </a-form-item>
      <a-form-item
        label="备注"
        :label-col="{span:4}"
      >
        <a-textarea
          v-model:value="modelRef.remark"
          :maxlength="999"
          placeholder="请输入备注"
        />
      </a-form-item>
    </a-form>
    <component
      :is="addHouseInfoCp"
      v-model:visible="houseInfoIsShow"
      :park-org-id="modelRef.parkOrgId"
      :default-select-data="houseHashMap"
      @select="houseInfoChange"
    />
  </div>
</template>
<script lang='ts'>
import {
  ref, defineComponent, PropType, toRefs, watch,
} from 'vue';
// import useOpts from '../composition/useOpts';
import { useAddHouse } from '@/views/wisdom-canvass-business/common-composition';
import WisdomCanvassBusiness, { SelectHouseInfo, BusinessInfo } from '~models/wisdom-canvass-business';

import { useParkInfoOpts } from '~composition/park';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import employeeSelect from '~components/employee-select';
import { useAddBusinessInfo } from '../composition/useFormConf';
import selectHouseInfo from './select-house-info.vue';
import houseCardList from './house-card-list.vue';

export default defineComponent({
  name: '',
  components: { employeeSelect, houseCardList },
  props: {
    orderType: {
      type: Number as PropType<1|2>,
      default: 1,
    },
    type: {
      type: String,
      default: '',
    },
    defaultData: {
      type: Object as PropType<BusinessInfo>,
      default: () => ({}),
    },
  },
  setup(props) {
    const { type, defaultData } = toRefs(props);
    const optionText = ref();
    const { parkOpts, getParkOpts } = useParkInfoOpts();
    const hashMapDic = ref<DictionaryMap<['THREAD_SOURCE', 'BUSINESS_CHANCE_STAGE']>>({
      THREAD_SOURCE: [],
      BUSINESS_CHANCE_STAGE: [],
    });
    const {
      modelRef,
      validateInfos,
      fill,
    } = useAddBusinessInfo();
    const {
      addHouseInfoCp,
      houseInfoIsShow,
      houseInfoChange,
      houseHashMap,
      houseIds,
      delHouseInfo,
      showHouseInfo,
    } = useAddHouse<SelectHouseInfo>(selectHouseInfo);
    watch(houseIds, (value:string[]) => {
      modelRef.houseIds = value;
    });

    // 设置跟进人默认下拉数据
    const defaultOptions = ref<SelectOptions>([]);
    const parkOrgChange = async (orgId:string) => {
      defaultOptions.value = [];
      houseHashMap.value = {};
      modelRef.houseIds = [];
      modelRef.followEmployeeId = '';
    };
    const init = async () => {
      getParkOpts();
      hashMapDic.value = await useHashMapDic<['THREAD_SOURCE', 'BUSINESS_CHANCE_STAGE']>(['THREAD_SOURCE', 'BUSINESS_CHANCE_STAGE']);
      optionText.value = [
        {
          label: '机会评估',
          value: 1,
          sort: 1,
        },
        {
          label: '需求分析',
          value: 2,
          sort: 2,
        },
        {
          label: '报价',
          value: 3,
          sort: 3,
        },
        {
          label: '谈判',
          value: 4,
          sort: 4,
        },
        {
          label: '达成意向',
          value: 5,
          sort: 5,
        },
        {
          label: '签订合同',
          value: 6,
          sort: 6,
        },

      ];
      if (type.value !== 'edit') return;
      const initData = await new WisdomCanvassBusiness().businessInfoFindById(defaultData.value.id);
      fill(initData);
      if (initData.businessHouseVO)houseHashMap.value = initData.businessHouseVO;
      const { followEmployeeName, followEmployeeId } = initData;
      if (followEmployeeName && followEmployeeId) {
        defaultOptions.value = [{
          label: initData.followEmployeeName,
          value: initData.followEmployeeId,
        }];
      }
    };
    init();
    return {
      optionText,
      parkOrgChange,
      parkOpts,
      modelRef,
      validateInfos,
      hashMapDic,
      addHouseInfoCp,
      houseInfoIsShow,
      houseInfoChange,
      houseHashMap,
      delHouseInfo,
      showHouseInfo,
      defaultOptions,
    //   orderTypeOpts,
    };
  },
});
</script>
<style lang="scss" scoped>
.business-opportunity-add {
    :deep(.ant-row) {
        display: flex;
        flex-wrap: nowrap;
    }
}
</style>
