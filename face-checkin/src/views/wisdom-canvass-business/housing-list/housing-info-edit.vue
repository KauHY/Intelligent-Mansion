<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-27 16:52:03
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 16:14:05
-->
<template>
  <div class="park-details content-section">
    <a-form
      label-align="left"
      :label-col="{span:6}"
    >
      <a-card
        title="房源信息"
        :bordered="false"
      >
        <template
          v-if="type==='add'"
          #extra
        >
          <a-form-item v-bind="validateInfos.houseId">
            <a-button
              type="primary"
              class="add-house"
              @click="addHouseVisible=true"
            >
              新增房屋
              <v-svg name="add-select" />
            </a-button>
          </a-form-item>
        </template>
        <div
          v-if="modelRef.houseId"
          class="item-box"
        >
          <a-form-item
            label="关联园区"
          >
            <span class="readonly-content">{{ houseInfo?.parkOrgName }}</span>
          </a-form-item>
          <a-form-item
            label="所属楼栋单元"
          >
            <span class="readonly-content">{{ houseInfo?.buildingName }}</span>
          </a-form-item>
          <a-form-item
            label="楼层房号"
          >
            <span class="readonly-content">{{ houseInfo?.name }}</span>
          </a-form-item>
          <a-form-item
            label="所在楼层"
          >
            <span class="readonly-content">{{ houseInfo?.floor }}</span>
          </a-form-item>
          <a-form-item
            label="建筑面积（㎡）"
          >
            <span class="readonly-content">{{ houseInfo?.buildArea }}</span>
          </a-form-item>
          <a-form-item
            label="套内面积（㎡）"
          >
            <span class="readonly-content">{{ houseInfo?.coverArea }}</span>
          </a-form-item>
          <!-- <template v-if="houseInfo"> -->
          <a-form-item
            label="楼层属性"
          >
            <span class="readonly-content">{{ options.FLOOR_TYPE[houseInfo?.floorType]?.label||'-' }}</span>
          </a-form-item>
          <a-form-item
            label="房屋功能"
          >
            <span class="readonly-content">{{ options.HOUSE_FUNCTION[houseInfo?.houseFunction]?.label||'-' }}</span>
          </a-form-item>
          <a-form-item
            label="房屋朝向"
          >
            <span class="readonly-content">{{ options.HOUSE_ORIENTATION[houseInfo?.orientation]?.label||'-' }}</span>
          </a-form-item>
          <a-form-item
            label="房屋属性"
          >
            <span class="readonly-content">{{ options.HOUSE_PROPERTY[houseInfo?.houseProperty]?.label ||'-' }}</span>
          </a-form-item>
          <a-form-item
            label="房屋状态"
          >
            <span class="readonly-content">{{ options.HOUSE_STATUS[houseInfo?.houseStatus]?.label ||'-' }}</span>
          </a-form-item>
          <!-- </template> {{ options['HOUSE_STATUS']? options['HOUSE_STATUS'][text]?.label ||'-':'-' }}-->
        </div>
      </a-card>
      <a-card
        title="基础信息"
        :bordered="false"
      >
        <div class="item-box">
          <a-form-item
            label="容纳工位"
            v-bind="validateInfos.stationNum"
          >
            <a-input
              v-model:value="modelRef.stationNum"
              :maxlength="5"
              :disabled="disabled"
              placeholder="请输入容纳工位"
            />
          </a-form-item>
          <a-form-item
            label="看房时间"
          >
            <!-- <a-range-picker
              v-model:value="visitTime"
              style="width: 100%;"
              :disabled="disabled"
              show-time
              value-format="YYYY-MM-DD HH:mm:ss"
            /> -->
            <a-time-picker
              v-model:value="modelRef.visitStartTime"
              format="HH:mm"
              :disabled="disabled"
              value-format="HH:mm"
              @change="timeChange($event,'visitStartTime')"
            />至
            <a-time-picker
              v-model:value="modelRef.visitEndTime"
              format="HH:mm"
              :disabled="disabled"
              value-format="HH:mm"
              @change="timeChange($event,'visitEndTime')"
            />
          </a-form-item>
          <a-form-item
            label="压付款方式"
          >
            <v-select
              v-model:value="modelRef.chargeType"
              :disabled="disabled"
              placeholder="请选择压付款方式"
            >
              <a-select-option
                v-for="opt in options?.CHARGE_TYPE"
                :key="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-form-item>
          <a-form-item
            label="免租期（月）"
            v-bind="validateInfos.freeRentMonth"
          >
            <a-input
              v-model:value="modelRef.freeRentMonth"
              :maxlength="5"
              :disabled="disabled"
              placeholder="请输入免租期"
            />
          </a-form-item>
        <!-- </div> -->
        </div>
      </a-card>
      <a-card
        title="策略信息"
        :bordered="false"
      >
        <div class="item-box">
          <a-form-item
            label="能否分割"
          >
            <!-- <a-checkbox v-model:checked="modelRef.canDivided" /> -->
            <a-switch
              v-model:checked="modelRef.canDivided"
              :disabled="disabled"
              checked-children="能"
              un-checked-children="不能"
            />
          </a-form-item>

          <a-form-item
            label="单位租金(元/㎡/月)"
            v-bind="validateInfos.unitRent"
          >
            <a-checkbox
              v-model:checked="modelRef.discussPersonally"
              :disabled="disabled"
              style="width: 100px; margin-left: 20px;"
              @change="modelRef.unitRent=null"
            >
              面议
            </a-checkbox>
            <a-input
              v-model:value="modelRef.unitRent"
              :maxlength="5"
              :disabled="disabled||modelRef.discussPersonally"
              placeholder="请输入单位租金"
            />
          </a-form-item>
          <a-form-item
            label="最早租期"
          >
            <a-date-picker
              v-model:value="modelRef.earliestRentTime"
              :disabled="disabled"
              value-format="YYYY-MM-DD"
            />
          </a-form-item>
          <a-form-item
            label="最短租期(月)"
            v-bind="validateInfos.minRentMonth"
          >
            <a-input
              v-model:value="modelRef.minRentMonth"
              :maxlength="5"
              :disabled="disabled"
              placeholder="请输入最短租期"
            />
          </a-form-item>
        </div>
      </a-card>
    </a-form>
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
    <add-house-info
      v-if="addHouseVisible"
      v-model:visible="addHouseVisible"
      @select="getHousingInfo"
    />
  </div>
</template>
<script lang='ts'>
import {
  computed, ref, toRefs, defineComponent, onUnmounted, onMounted, nextTick,
} from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import BusinessInvitation, { HousingInfo, SelectHouseInfo } from '~models/wisdom-canvass-business';
import House, { HouseInfo } from '~models/house';
import Park from '~models/park';

import { useCachePage } from '~composition/common';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { useAddBusinessInfo } from './composition/useFormConf';

import addHouseInfo from './components/add-house-info.vue';

export default defineComponent({
  components: { addHouseInfo },
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
    const disabled = computed(() => type.value === 'details');
    const { remoteCachePage } = useCachePage();
    // const parkOpts = ref<SelectOptions>([]);
    // const options = ref<DictionaryMap<['CHARGE_TYPE']>>();
    // const companyOpts = ref<SelectOptions>([]);
    const { options, parkOpts } = useOpts();
    const {
      modelRef,
      validate,
      fill,
      validateInfos,
    } = useAddBusinessInfo();

    const goBlank = (refresh = '') => {
      router.push({ name: 'wisdomCanvassBusinessHouseList', params: { refresh } });
    };

    const save = () => { // 保存
      validate().then(async () => {
        if (type.value === 'add') {
          await businessInvitationInstance.addBusinessHouse(modelRef);
        } else {
          await businessInvitationInstance.houseModify(modelRef);
        }
        message.success('操作成功');
        goBlank('refresh');
      });
    };

    const addHouseVisible = ref(false);
    const houseInfo = ref<Partial<HouseInfo>>({}); // 房屋基础信息
    const getHousingInfo = async (id:string, selectHouse: SelectHouseInfo) => {
      houseInfo.value = await new House().housefindById(id);
      modelRef.houseId = id;
      modelRef.parkOrgId = houseInfo.value.parkOrgId as string;
      validate();
    };

    const timeChange = (time:string, type:'visitStartTime'|'visitEndTime') => {
      const startTime = type === 'visitStartTime' ? time : modelRef.visitStartTime;
      const endTime = type === 'visitEndTime' ? time : modelRef.visitEndTime;
      const startTimeStr = startTime ? startTime.split(':') : [];
      const entTimeStr = endTime ? endTime.split(':') : [];
      const startNum = Number(`${startTimeStr[0]}.${startTimeStr[1]}`);
      const endNum = Number(`${entTimeStr[0]}.${entTimeStr[1]}`);
      if (endNum - startNum < 0) {
        message.warning('看房开始时间不能晚于看房结束时间，请重新选择');
        time = '';
      }
      if (endNum === startNum) {
        message.warning('看房开始时间不能等于看房结束时间，请重新选择');
        time = '';
      }
      nextTick(() => {
        modelRef[type] = time;
      });
    };

    const housingInfo = ref<Partial<HousingInfo>>({}); // 房源招商详情
    const init = async () => {
      if (['edit', 'details'].includes(type.value)) {
        housingInfo.value = await businessInvitationInstance.housefindById(id.value);
        houseInfo.value = housingInfo.value.houseVO as HouseInfo;
        fill(housingInfo.value);
      }
    };
    onMounted(() => {
      init();
    });
    onUnmounted(() => {
      // console.log('详情页面卸载了----');
      remoteCachePage('HousingList');
    });
    return {
      modelRef,
      validateInfos,
      // parkTypeNames,
      goBlank,
      save,
      options,
      disabled, // 是否只读
      housingInfo,
      // houseId,
      addHouseVisible,
      getHousingInfo,
      houseInfo,
      timeChange,
    };
  },
});
// 获取下拉项
function useOpts() {
  const options = useHashMapDics<['CHARGE_TYPE', 'HOUSE_PROPERTY', 'HOUSE_FUNCTION', 'FLOOR_TYPE', 'HOUSE_ORIENTATION', 'HOUSE_STATUS']>(['CHARGE_TYPE', 'HOUSE_PROPERTY', 'HOUSE_FUNCTION', 'FLOOR_TYPE', 'HOUSE_ORIENTATION', 'HOUSE_STATUS']);
  const parkOpts = ref<SelectOptions>([]);
  const init = async () => {
    parkOpts.value = await new Park().searchParkListName();
  };
  return {
    options,
    parkOpts,
  };
}
</script>
<style lang="scss" scoped>
.park-details {
    padding: 0 60px 0 60px;
    overflow: auto;

    .ant-form {
        padding-bottom: 80px;
    }

    .item-box {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        width: 100%;
        padding: 0 20px;

        .ant-form-item {
            flex: 0 0 47%;
            flex-wrap: nowrap;

            .readonly-content {
                margin-left: 20px;
            }

            :deep(.ant-form-item-control-input-content) {
                display: flex;
                align-items: center;
                justify-content: flex-start;
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

    .add-house {
        display: flex;
        align-items: center;
        margin-right: 20px;
    }
}
</style>
