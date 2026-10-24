<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-01 17:38:03
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-02 14:44:25
-->
<template>
  <div class="business-opportunity-details content-section">
    <div class="content-scroll">
      <div class="header">
        <a-row class="item-box">
          <a-col :span="12">
            <p class="label">
              当前商机阶段：
            </p>
            <span>{{ options.BUSINESS_CHANCE_STAGE[businessInfo?.chanceStage]?.label }}</span>
          </a-col>
          <a-col :span="12">
            <p class="label">
              最近跟进时间：
            </p>
            <span>{{ businessInfo?.latestFollowTime }}</span>
          </a-col>
        </a-row>
        <a-row class="item-box">
          <a-col :span="12">
            <p class="label">
              跟进人:
            </p>
            <span>{{ businessInfo?.followEmployeeName }}</span>
          </a-col>
        </a-row>
      </div>
      <div class="tabs-box">
        <a-tabs v-model:activeKey="activeKey">
          <a-tab-pane
            :key="1"
            tab="商机信息"
          >
            <!-- <businessOpportunityDetails :id="id" /> -->
            <!-- <keep-alive> -->
            <div v-if="activeKey===1">
              <a-row class="item-box">
                <a-col :span="12">
                  <p class="label">
                    商机名称：
                  </p>
                  <span>{{ businessInfo?.chanceName }}</span>
                </a-col>
                <a-col :span="12">
                  <p class="label">
                    联系人：
                  </p>
                  <span>{{ businessInfo?.contactName }}</span>
                </a-col>
              </a-row>
              <a-row class="item-box">
                <a-col :span="12">
                  <p class="label">
                    联系电话：
                  </p>
                  <span>{{ businessInfo?.contactPhone }}</span>
                </a-col>
                <a-col :span="12">
                  <p class="label">
                    联系人邮箱：
                  </p>
                  <span>{{ businessInfo?.contactMail }}</span>
                </a-col>
              </a-row>
              <a-row class="item-box">
                <a-col :span="12">
                  <p class="label">
                    需求数量：
                  </p>
                  <span>{{ businessInfo?.quantityNumber }}</span>
                </a-col>
                <a-col :span="12">
                  <p class="label">
                    期望价格（元/㎡/月）:
                  </p>
                  <span>{{ businessInfo?.expectedPrice }}</span>
                </a-col>
              </a-row>
              <a-row class="item-box">
                <a-col :span="12">
                  <p class="label">
                    关联园区：
                  </p>
                  <span>{{ businessInfo?.parkOrgName }}</span>
                </a-col>
                <a-col :span="12">
                  <p class="label">
                    线索来源:
                  </p>
                  <span>{{ options.THREAD_SOURCE[businessInfo?.threadSource]?.label }}</span>
                </a-col>
              </a-row>
              <a-row class="item-box">
                <a-col :span="23">
                  <p class="label">
                    备注：
                  </p>
                  <span>{{ businessInfo?.remark }}</span>
                </a-col>
              </a-row>
              <a-row class="item-box">
                <a-col :span="23">
                  <p class="label">
                    意向房源：
                  </p>
                  <house-card-list
                    :house-list="businessInfo?.businessHouseVO||{}"
                    disabled
                  />
                </a-col>
              </a-row>
            </div>
            <!-- </keep-alive> -->
          </a-tab-pane>
          <a-tab-pane
            :key="2"
            tab="跟进记录"
            force-render
          >
            <followRecord
              v-if="activeKey===2"
              :id="id"
            />
          </a-tab-pane>
          <a-tab-pane
            :key="3"
            tab="阶段流程"
          >
            <stageProcess
              v-if="activeKey===3"
              :id="id"
            />
          </a-tab-pane>
        </a-tabs>
      </div>
      <div class="btns">
        <a-button
          type="primary"
          @click="goBlank"
        >
          返回
        </a-button>
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  ref,
  defineComponent,
  onUnmounted,
} from 'vue';
import { useRouter } from 'vue-router';
import WisdomCanvassBusiness, { BusinessInfo } from '~models/wisdom-canvass-business';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
// import businessOpportunityDetails from './components/details/business-opportunity-details.vue';
import { useCachePage } from '~composition/common';
import followRecord from './components/details/follow-record.vue';
import stageProcess from './components/details/stage-process.vue';
import houseCardList from './components/house-card-list.vue';

export default defineComponent({
  name: '',
  components: {
    // businessOpportunityDetails,
    followRecord,
    stageProcess,
    houseCardList,
  },
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const activeKey = ref(1);
    const businessInfo = ref<BusinessInfo>();
    const options = ref<DictionaryMap<['BUSINESS_CHANCE_STAGE', 'THREAD_SOURCE', ]>>({
      BUSINESS_CHANCE_STAGE: [],
      THREAD_SOURCE: [],
    });

    const router = useRouter();
    const goBlank = () => {
      router.go(-1);
    };

    const { remoteCachePage } = useCachePage();
    onUnmounted(() => {
      remoteCachePage('BusinessOpportunityManagement');
    });
    const init = async () => {
      if (!props.id) return;
      console.log(props.id, '---props.id');
      businessInfo.value = await new WisdomCanvassBusiness().businessInfoFindById(props.id);
      options.value = await useHashMapDic<['BUSINESS_CHANCE_STAGE', 'THREAD_SOURCE']>(['BUSINESS_CHANCE_STAGE', 'THREAD_SOURCE']);
    };
    init();
    return {
      businessInfo,
      options,
      activeKey,
      goBlank,
    };
  },
});
</script>
<style lang="scss" scoped>
.business-opportunity-details {
    position: relative;
    padding: 30px;
    font-size: 16px;

    .btns {
        position: absolute;
        bottom: 20px;
        left: 0;
        width: 100%;
        text-align: center;
    }

    .item-box {
        padding: 20px 0 20px 0;
        margin-bottom: 10px;
        font-size: 16px;

        .label {
            width: 180px;
            margin-left: 24px;
            color: #8f8f8f;
        }

        .ant-col {
            display: flex;
            justify-content: flex-start;
        }
    }

    .header {
        .item-box:nth-child(2) {
            margin-bottom: 60px;
            border-bottom: 1px solid #f0f0f0;
        }
    }
}
</style>
