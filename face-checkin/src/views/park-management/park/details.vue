<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-27 16:52:03
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-28 11:38:53
-->
<template>
  <div class="park-details content-section">
    <div class="content-scroll">
      <a-form

        :label-col="{span:6}"
      >
        <!-- <a-divider orientation="left">
          基本信息
        </a-divider> -->
        <a-card
          title="基本信息"
          :bordered="false"
        >
          <div class="item-box">
            <!-- <div class="left"> -->
            <a-form-item
              label="园区名称"
              v-bind="validateInfos.name"
              name="name"
            >
              <a-input
                v-model:value="modelRef.name"
                placeholder="请输入园区名称"
                :disabled="disabled"
                :maxlength="30"
              />
            </a-form-item>
            <a-form-item
              label="上级组织"
            >
              <span>{{ parkInfo?.parentOrgName }}</span>
            </a-form-item>
            <a-form-item
              label="详细地址"
              name="address"
              v-bind="validateInfos.address"
              @click="mapShow=true"
            >
              <a-textarea
                v-model:value="modelRef.address"
                placeholder="请输入详情地址"
                :maxlength="200"
                :disabled="disabled"
                readonly
                allow-clear
              />
              <!-- <a-button type="link">
                获取坐标
              </a-button> -->
            </a-form-item>
            <!-- </div>
          <div class="right"> -->
            <a-form-item
              label="经度"
              v-bind="validateInfos.longitude"
              name="longitude"
            >
              <a-input
                v-model:value="modelRef.longitude"
                :disabled="disabled"
                readonly
                placeholder="输入地址、获取坐标后，点击地图中的定位自动获取经纬度"
              />
            </a-form-item>
            <a-form-item
              label="纬度"
              v-bind="validateInfos.latitude"
              name="latitude"
            >
              <a-input
                v-model:value="modelRef.latitude"
                :disabled="disabled"
                readonly
                placeholder="输入地址、获取坐标后，点击地图中的定位自动获取经纬度"
              />
            </a-form-item>
          <!-- </div> -->
          </div>
        </a-card>
        <!-- <a-divider orientation="left">
          物业信息
        </a-divider> -->
        <a-card
          title="物业信息"
          :bordered="false"
        >
          <div class="item-box">
            <a-form-item
              label="建成时间"
              name="buildTime"
            >
              <a-date-picker
                v-model:value="modelRef.buildTime"
                value-format="YYYY-MM-DD"
                :disabled="disabled"
                placeholder="请选建成时间"
              />
            </a-form-item>
            <a-form-item
              label="园区属性"
              name="parkType"
            >
              <a-select
                v-model:value="modelRef.parkType"
                :disabled="disabled"
                placeholder="请选园区属性"
              >
                <a-select-option
                  v-for="(name,key) in parkTypeNames"
                  :key="key"
                  :value="Number(key)"
                >
                  {{ name }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item
              label="功能用途"
              name="parkFunction"
            >
              <a-select
                v-model:value="modelRef.parkFunction"
                :disabled="disabled"
                placeholder="请选择功能用途"
              >
                <a-select-option
                  v-for="(name,key) in parkFunctionNames"
                  :key="key"
                  :value="Number(key)"
                >
                  {{ name }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item
              label="产权属性"
              name="property"
            >
              <a-select
                v-model:value="modelRef.property"
                :disabled="disabled"
                placeholder="请选择产权属性"
              >
                <a-select-option
                  v-for="(name,key) in propertyNames"
                  :key="key"
                  :value="Number(key)"
                >
                  {{ name }}
                </a-select-option>
              </a-select>
            </a-form-item>

            <a-form-item
              label="权属单位"
              name="companyName"
            >
              <a-input
                v-model:value="modelRef.companyName"
                placeholder="请输入园区权属单位"
                :disabled="disabled"
                :maxlength="30"
              />
            </a-form-item>
            <a-form-item
              label="权属单位联系人"
            >
              <a-input
                v-model:value="modelRef.companyContacts"
                :maxlength="30"
                :disabled="disabled"
                placeholder="请输入权属单位联系人"
              />
            </a-form-item>
            <a-form-item
              label="权属单位联系电话"
              v-bind="validateInfos.companyTel"
            >
              <a-input
                v-model:value="modelRef.companyTel"
                :disabled="disabled"
                placeholder="请输入权属单位联系电话"
              />
            </a-form-item>
            <a-form-item
              label="管理机构"
            >
              <a-input
                v-model:value="modelRef.managementName"
                :maxlength="30"
                :disabled="disabled"
                placeholder="请输入管理机构"
              />
            </a-form-item>
            <a-form-item
              label="管理机构联系人"
              name="managementContacts"
            >
              <a-input
                v-model:value="modelRef.managementContacts"
                :maxlength="30"
                :disabled="disabled"
                placeholder="请输入管理机构联系人"
              />
            </a-form-item>
            <a-form-item
              label="管理机构联系电话"
              v-bind="validateInfos.managementTel"
              name="managementTel"
            >
              <a-input
                v-model:value="modelRef.managementTel"
                :disabled="disabled"
                placeholder="请输入管理机构联系电话"
              />
            </a-form-item>
            <a-form-item
              label="总占地面积（㎡）"
              v-bind="validateInfos.coverArea"
            >
              <a-input-number
                v-model:value="modelRef.coverArea"
                type="number"
                :disabled="disabled"
                placeholder="请输入总占地面积"
              />
            </a-form-item>
            <a-form-item
              label="总建筑面积（㎡）"
              v-bind="validateInfos.buildingArea"
            >
              <a-input-number
                v-model:value="modelRef.buildingArea"
                type="number"
                :disabled="disabled"
                placeholder="请输入总建筑面积"
              />
            </a-form-item>
          </div>
        </a-card>
        <!-- <a-divider orientation="left">
          消防信息
        </a-divider> -->
        <a-card
          title="消防信息"
          :bordered="false"
        >
          <div class="item-box">
            <a-form-item
              label="是否通过消防审批"
              name="buildTime"
            >
              <a-radio-group
                v-model:value="modelRef.fireExamineStatus"
                name="fireExamineStatus"
                :disabled="disabled"
              >
                <a-radio
                  :value="0"
                >
                  是
                </a-radio>
                <a-radio
                  :value="1"
                >
                  否
                </a-radio>
                <a-radio
                  :value="2"
                >
                  暂无审批
                </a-radio>
              </a-radio-group>
            </a-form-item>
            <a-form-item
              label="消防责任管理人"
              name="firePerson"
            >
              <a-input
                v-model:value="modelRef.firePerson"
                :maxlength="30"
                :disabled="disabled"
                placeholder="请输入消防责任管理人"
              />
            </a-form-item>
            <a-form-item
              label="联系电话"
              v-bind="validateInfos.firePersonTel"
              name="firePersonTel"
            >
              <a-input
                v-model:value="modelRef.firePersonTel"
                :disabled="disabled"
                placeholder="请输入联系电话"
              />
            </a-form-item>
          </div>
        </a-card>

        <a-card
          title="其他"
          :bordered="false"
        >
          <div class="item-box">
            <a-form-item
              label="园区照片"
              name="pics"
            >
              <!-- <a-input
                v-model:value="modelRef.pics"
              /> -->
              <v-upload
                v-model:value="modelRef.pics"
                :disabled="disabled"
                accept=".jpg,.bmp,.png,.gif"
                tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸1125*844PX"
              />
            </a-form-item>
            <a-form-item
              label="园区介绍"
              name="description"
            >
              <a-textarea
                v-model:value="modelRef.description"
                placeholder="请输入园区介绍"
                :maxlength="200"
                :rows="6"
                show-count
                :disabled="disabled"
                allow-clear
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
        <a-button @click="goBlank">
          返回
        </a-button>
      </div>
    </div>
    <gdMap
      v-model:show="mapShow"
      @change="addressInfoChange"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineProps, defineComponent, onUnmounted, onMounted,
} from 'vue';
//
import {
  parkTypeNames, parkFunctionNames, propertyNames, fireExamineStatusNames,
} from '@/constants/config';
import { useRouter } from 'vue-router';
import { AddressInfo } from '@/components/map/type';
import { message } from 'ant-design-vue';
import { useCachePage } from '~composition/common';
import type { PropType } from 'vue';
import Park from '~models/park';
import gdMap from '~components/map/map.vue';
import type { ParkInfo } from '~models/park';
import { useCompleteParkInfoForm } from './config';

export default defineComponent({
  components: { gdMap },
  props: {
    params: {
      type: Object as PropType<{orgName:string, orgId:string, id:string, type:string}>,
      default: () => ({}),
    },
  },
  setup(props) {
    const parkInstance = new Park();
    // const store = useStore();
    const disabled = ref<Boolean>(false);
    const router = useRouter();
    const { remoteCachePage } = useCachePage();
    const {
      modelRef,
      validate,
      validateInfos,
    } = useCompleteParkInfoForm();

    const parkInfo = ref<ParkInfo>();
    // console.log(props.params, '----params');
    const fill = (target:any, baseData:any) => {
      Object.keys(target).forEach((key) => {
        if (baseData[key]) {
          target[key] = baseData[key];
        }
      });
    };
    const init = async () => {
      parkInfo.value = await parkInstance.getParkInfoById(props.params.id);
      fill(modelRef, parkInfo.value);
      if (props.params.type === 'detail') {
        disabled.value = true;
      }
    };
    const goBlank = (refresh = '') => {
      router.push({ name: 'parkManagementParkList', params: { refresh } });
    };

    //  地图操作
    const mapShow = ref(false);
    const addressInfoChange = (addressInfo:AddressInfo) => {
      console.log(addressInfo, '----addressInfo');
      const {
        location, pname, cityname, adname, address,
      } = addressInfo;
      // modelRef.latitude = location.lat;
      // modelRef.longitude = location.lng;
      // modelRef.address = `${pname}${cityname}${adname}${address}`;
      // modelRef.province = pname
      // modelRef.city = cityname
      Object.assign(modelRef, {
        latitude: location.lat,
        longitude: location.lng,
        address: `${pname}${cityname}${adname}${address}`,
        province: pname,
        city: cityname,
      });
    };
    const save = () => { // 保存
      console.log(modelRef, '----search');
      validate().then(async () => {
        await parkInstance.save(modelRef);
        message.success('操作成功');
        goBlank('refresh');
      });
    };
    onMounted(() => {
      init();
      console.log('调用了----');
    });
    onUnmounted(() => {
      // console.log('详情页面卸载了----');
      remoteCachePage('ParkList');
      // store.dispatch('common/delCachePageNames', 'ParkList'); // 离开时删除园区列表页面缓存
    });
    return {
      disabled,
      modelRef,
      validateInfos,
      parkInfo,
      parkTypeNames,
      mapShow,
      goBlank,
      parkFunctionNames,
      propertyNames,
      fireExamineStatusNames,
      addressInfoChange,
      save,
    };
  },
});
</script>
<style lang="scss" scoped>
.park-details {
    position: relative;
    padding: 24px 0;
    overflow: hidden;

    .content-scroll {
        padding-bottom: 80px;
    }

    .ant-form {
        padding-bottom: 80px;
    }

    .item-box {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        width: 100%;
        padding: 20px;

        .ant-form-item {
            flex: 0 0 47%;
            flex-wrap: nowrap;
            margin-bottom: 20px;
        }
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
