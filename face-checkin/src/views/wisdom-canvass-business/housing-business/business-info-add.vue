<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-27 16:52:03
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-15 10:47:04
-->
<template>
  <div class="park-details content-section">
    <a-form

      :label-col="{span:6}"
    >
      <div class="item-box">
        <a-form-item
          label="招商类型"
          v-bind="validateInfos.effectType"
        >
          <v-select
            v-model:value="modelRef.effectType"
            :disabled="disabled"
            placeholder="请选择招商类型"
            @change="effectTypeChange"
          >
            <a-select-option
              v-for="opt in options?.['EFFECT_TYPE']"
              :key="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          v-if="!modelRef.effectType"
          v-bind="validateInfos.deadline"
          label="截至时间"
        >
          <a-date-picker
            v-model:value="modelRef.deadline"
            :disabled="disabled"
            :disabled-date="disabledDeadlineDate"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item
          label="招商标题"
          v-bind="validateInfos.title"
        >
          <a-input
            v-model:value="modelRef.title"
            :disabled="disabled"
            placeholder="请输入招商标题"
          />
        </a-form-item>
        <a-form-item
          label="租赁属性"
          v-bind="validateInfos.rentOrSale"
        >
          <a-checkbox-group
            v-model:value="modelRef.rentOrSale"
            :disabled="disabled"
          >
            <a-checkbox :value="1">
              出租
            </a-checkbox>
            <a-checkbox :value="2">
              出售
            </a-checkbox>
          </a-checkbox-group>
        </a-form-item>
        <a-form-item
          label="亮点描述"
          v-bind="validateInfos.highlight"
        >
          <v-select
            v-model:value="modelRef.highlight"
            :disabled="disabled"
            mode="multiple"
            placeholder="请选择亮点描述"
          >
            <a-select-option
              v-for="opt in options?.HIGHLIGHT"
              :key="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="地址"
          name="address"
          v-bind="validateInfos['address.name']"
          @click="mapShow=true"
        >
          <a-textarea
            v-model:value="modelRef.address.name"
            :disabled="disabled"
            placeholder="请选择详情地址"
            readonly
            allow-clear
          />
        </a-form-item>
        <a-form-item
          label="关联园区"
          v-bind="validateInfos.parkOrgId"
        >
          <v-select
            v-model:value="modelRef.parkOrgId"
            :disabled="disabled"
            placeholder="请选择关联园区"
            @change="parkChange($event,false)"
          >
            <a-select-option
              v-for="opt in parkOpts"
              :key="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="咨询顾问"
          v-bind="validateInfos.employeeId"
        >
          <!-- <employee-select
            v-if="initComplate"
            v-model:value="modelRef.employeeId"
            :org-id="modelRef.parkOrgId"
            :disabled="disabled"
            label-join-key="companyName"
            :default-options="defaultEmployeeOptions"
          /> -->
          <v-select-employee
            v-model:value="modelRef.employeeId"
            :park-org-ids="modelRef.parkOrgId"
            :default-select-opts="defaultEmployeeOptions"
            job-codes="10"
            mode="single"
            :disabled="!modelRef.parkOrgId"
            placeholder="请选择咨询顾问"
            @change="employeeIdChange"
          />
        </a-form-item>
        <a-form-item
          label="咨询电话"
          v-bind="validateInfos.employeePhone"
        >
          <a-input
            v-model:value="modelRef.employeePhone"
            :disabled="disabled"
            placeholder="请输入咨询电话"
          />
        </a-form-item>
        <!-- </div> -->
      </div>
      <div class="item-box">
        <a-form-item
          label="标题图"
          v-bind="validateInfos.titlePicture"
        >
          <!-- <a-input
            v-model:value="modelRef.titlePicture"
            :disabled="disabled"
            placeholder="请选上传标题图"
          /> -->
          <v-upload
            v-if="initComplate"
            v-model:value="modelRef.titlePicture"
            accept=".jpg,.bmp,.png,.gif"
            tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸1125*844PX"
          />
        </a-form-item>
      </div>
      <div class="item-box single">
        <a-form-item
          label="详情描述"
          v-bind="validateInfos.content"
        >
          <v-editor
            v-model:value="modelRef.content"
            :disabled="disabled"
          />
        </a-form-item>
      </div>
      <div class="item-box single">
        <a-form-item
          label="招商房屋"
          v-bind="validateInfos.houseInfoIds"
        >
          <house-card-list
            :house-list="houseHashMap"
            :disabled="disabled"
            @del="delHouseInfo"
          >
            <a-button
              type="link"
              class="add-housing"
              :disabled="!modelRef.parkOrgId||disabled"
              @click="showHouseInfo"
            >
              <template #icon>
                <v-svg name="add-select" />
                新增招商房屋
              </template>
            </a-button>
          </house-card-list>
        </a-form-item>
      </div>
      <div class="buttons">
        <a-button
          v-if="!disabled"
          type="primary"
          @click="save"
        >
          保存
        </a-button>
        <a-button @click="goBlank('')">
          返回
        </a-button>
      </div>
    </a-form>
    <component
      :is="addHouseInfoCp"
      v-model:visible="houseInfoIsShow"
      :park-org-id="modelRef.parkOrgId"
      :default-select-data="houseHashMap"
      :business-info-id="modelRef.id"
      @select="houseInfoChange"
    />
    <gdMap
      v-model:show="mapShow"
      @change="addressInfoChange"
    />
  </div>
</template>
<script lang='ts'>
import {
  computed, ref, toRefs, defineComponent, onUnmounted, shallowRef, watch, Ref,
} from 'vue';
//
import {
  parkTypeNames, parkFunctionNames, propertyNames, fireExamineStatusNames,
} from '@/constants/config';
import { useRouter } from 'vue-router';
import { AddressInfo } from '@/components/map/type';
import { Dayjs } from 'dayjs';
import BusinessInvitation, { SelectHouseInfo, HousingBusinessInfo } from '~models/wisdom-canvass-business';
import CompanyMangement from '~models/company-mangement';
import gdMap from '~components/map/map.vue';
import type { ParkInfo } from '~models/park';
import { useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import employeeSelect from '~components/employee-select';
import addHouseInfo from './components/add-house-info.vue';
import houseCardList from './components/house-card-list.vue';
import { useAddBusinessInfo } from './composition/useFormConf';
// import { SelectHouseInfo } from './type';

export default defineComponent({
  components: { gdMap, houseCardList, employeeSelect },
  props: {
    id: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: 'add',
    },
  },
  setup(props) {
    const { id, type } = toRefs(props);
    const businessInvitationInstance = new BusinessInvitation();
    const router = useRouter();
    const { remoteCachePage } = useCachePage();

    // 字典
    const options = useHashMapDics<['EFFECT_TYPE', 'HIGHLIGHT', 'CHARGE_TYPE', 'BUSINESS_STATUS']>(['EFFECT_TYPE', 'HIGHLIGHT', 'CHARGE_TYPE', 'BUSINESS_STATUS']);

    const disabled = computed(() => type.value === 'details');
    // 页面数据
    const {
      modelRef,
      validate,
      fill,
      validateInfos,
    } = useAddBusinessInfo();

    //  地图操作
    const { mapShow, addressInfoChange } = useOperateMap(modelRef);

    // 新增招商房源
    const {
      addHouseInfoCp,
      houseInfoIsShow,
      showHouseInfo,
      houseInfoChange,
      houseHashMap,
      delHouseInfo,
    } = useHouse(modelRef);

    // 特定值变化时回调处理
    const {
      effectTypeChange,
      employeeIdChange,
      parkChange,
      defaultEmployeeOptions,
      companyOpts,
    } = useChangeEffect(modelRef, houseHashMap);

    const disabledDeadlineDate = (e:Dayjs) => e.valueOf() < Date.now();
    const save = () => { // 保存
      validate().then(async () => {
        // console.log(modelRef, '----search');
        if (modelRef.effectType === 1) {
          modelRef.deadline = '';
        }
        const operateRequests = {
          edit: businessInvitationInstance.housingBusinessEdit,
          add: businessInvitationInstance.housingBusinessAdd,
        };
        const operateType = type.value as 'add' |'edit';
        console.log(operateType, '----operateType---');
        await operateRequests[operateType](modelRef);
        // await businessInvitationInstance.housingBusinessAdd(modelRef);
        // message.success('操作成功');
        goBlank('refresh');
      });
    };

    // 返回操作
    const goBlank = (refresh = '') => {
      router.push({ name: 'wisdomCanvassBusinessHousingBusiness', params: { refresh } });
    };
    const initComplate = ref(false);

    const parkOpts = ref<SelectOptions>([]);
    const init = async () => {
      parkOpts.value = await new Park().searchParkListName();
      if (['edit', 'details'].includes(type.value)) {
        const data = await businessInvitationInstance.housingBusinessById(id.value);
        fill(data);
        debugger;
        houseHashMap.value = data.businessHouseVO || {}; // 勾选的房源树
        parkChange(modelRef.parkOrgId, true);
        defaultEmployeeOptions.value = [{
          value: data.employeeId,
          label: data.employeeName,
        }];
      }
      initComplate.value = true;
    };

    init();
    onUnmounted(() => {
      remoteCachePage('HousingBusiness');
    });
    return {
      modelRef,
      validateInfos,
      // parkInfo,
      parkTypeNames,
      mapShow,
      goBlank,
      parkFunctionNames,
      propertyNames,
      fireExamineStatusNames,
      addressInfoChange,
      save,
      defaultEmployeeOptions,
      options,
      parkOpts,
      companyOpts,
      parkChange,
      addHouseInfoCp,
      houseInfoIsShow,
      showHouseInfo,
      houseInfoChange,
      houseHashMap,
      delHouseInfo,
      effectTypeChange,
      initComplate, // 是否初始化完成
      disabled, // 是否只读
      employeeIdChange,
      disabledDeadlineDate,
    };
  },
});

/**
 *  添加房屋操作
 * @param modelRef
 */
function useHouse(modelRef:HousingBusinessInfo) {
  const houseHashMap = ref<SelectHouseInfo>({});
  const addHouseInfoCp = shallowRef();
  const houseInfoIsShow = ref(false);
  const showHouseInfo = () => {
    addHouseInfoCp.value = addHouseInfo;
    // setTimeout(() => {
    houseInfoIsShow.value = true;
    // }, 300);
  };
  watch(houseInfoIsShow, (value:boolean) => {
    if (!value) {
      addHouseInfoCp.value = null;
    }
  });
  const houseInfoChange = (selectKeys:string[], selectData:SelectHouseInfo) => {
    modelRef.houseInfoIds = selectKeys;
    houseHashMap.value = selectData;
    console.log(houseHashMap, '----houseHashMap');
  };
  const delHouseInfo = (id:string) => {
    console.log(id, houseHashMap, '----id');
    delete houseHashMap.value[id];
    modelRef.houseInfoIds = Object.keys(houseHashMap.value);
  };
  return {
    addHouseInfoCp,
    houseInfoIsShow,
    showHouseInfo,
    houseInfoChange,
    houseHashMap,
    delHouseInfo,
  };
}

/**
 * 地图操作
 * @param modelRef
 */
function useOperateMap(modelRef:HousingBusinessInfo) {
  const mapShow = ref(false);
  const addressInfoChange = (addressInfo:AddressInfo) => {
    console.log(addressInfo, '----addressInfo');
    const {
      location, pname, cityname, adname, address,
    } = addressInfo;
      // modelRef.latitude = location.lat;
      // modelRef.longitude = location.lng;
    modelRef.address = {
      name: `${pname}${cityname}${adname}${address}`,
      latitude: location.lat,
      longitude: location.lng,
    };
  };
  return {
    mapShow,
    addressInfoChange,
  };
}

/**
 * 特定值变化时回调处理
 * @param modelRef
 * @param houseHashMap
 */
function useChangeEffect(modelRef:HousingBusinessInfo, houseHashMap:Ref<SelectHouseInfo>) {
  const defaultEmployeeOptions = ref<SelectOptions>([]); // 默认员工列表
  const companyOpts = ref<SelectOptions>([]); // 公司下拉数据
  // 招商类型变化
  const effectTypeChange = (value:number) => {
    if (!value)modelRef.deadline = '';
  };
  // 咨询顾问发生变动
  const employeeIdChange = (value:string, employeeOpt:SelectOptionsMap) => {
    // debugger;
    const opt = Object.values(employeeOpt)[0];
    modelRef.employeePhone = opt ? opt.extraInfo?.mobile : '';
  };
  // 园区变动
  const parkChange = async (parkOrgId:string, isInit = true) => {
    if (!isInit) {
      modelRef.employeeId = '';
      modelRef.employeePhone = '';
      modelRef.houseInfoIds = [];
      houseHashMap.value = {};
      defaultEmployeeOptions.value = [];
    }
    companyOpts.value = await new CompanyMangement().listCompanyDict(parkOrgId);
  };
  return {
    effectTypeChange,
    employeeIdChange,
    parkChange,
    defaultEmployeeOptions,
    companyOpts,
  };
}

</script>
<style lang="scss" scoped>
.park-details {
    padding-right: 40px;
    overflow: auto;

    .ant-form {
        padding-bottom: 80px;
    }

    .item-box {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        width: 100%;

        .ant-form-item {
            flex: 0 0 48%;
            flex-wrap: nowrap;

            :deep(.ant-form-item-label) {
                flex: 0 0 120px;
            }
        }

        &.single {
            .ant-form-item {
                flex: 1;
                margin-bottom: 20px;
            }
        }

        .add-housing {
            svg {
                width: 100%;
                height: 100%;
            }
        }
    }

    .buttons {
        position: absolute;
        bottom: 20px;
        width: calc(100% - 66px);
        padding: 15px;
        text-align: center;
        background: #fff;
        border-top: 1px solid #f0f0f0;

        >button {
            margin-left: 20px;
        }
    }
}
</style>
