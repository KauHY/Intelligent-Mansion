<!--
 * @Description: 绑定房屋
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-26 16:40:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-12 10:59:03
-->
<template>
  <div class="bind-house content-section">
    <!-- 绑定房屋 -->
    <a-form
      ref="formRef"
      :label-col="{span:6}"
      :model="modelRef"
    >
      <a-card
        title="基本信息"
        :bordered="false"
      >
        <div class="item-box">
          <a-form-item label="企业名称">
            <span>{{ name }}</span>
          </a-form-item>
          <a-form-item label="关联园区">
            <span>{{ parkOrgName }}</span>
          </a-form-item>
        </div>
      </a-card>
      <a-card
        v-for="(bindHouseItem,index) in modelRef.houseList"
        :key="index"
        :title="`房屋${index+1}`"
        :bordered="false"
      >
        <template #extra>
          <a-button
            v-if="modelRef.houseList.length>1"
            type="link"
            @click="del(index)"
          >
            删除
          </a-button>
        </template>
        <div class="item-box">
          <a-form-item
            label="关联楼栋单元"
            :name="['houseList',index,'buildingId']"
            :rules="rulesRef.buildingId"
          >
            <v-select
              v-model:value="bindHouseItem.buildingId"
              placeholder="请选择楼栋单元"
              @change="buildingChange($event,bindHouseItem)"
            >
              <a-select-option
                v-for="opt in buildingOpts"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-form-item>
          <a-form-item
            label="关联楼层房号"
            :name="['houseList',index,'houseId']"
            :rules="rulesRef.houseId"
          >
            <v-select
              v-model:value="bindHouseItem.houseId"
              placeholder="请选择楼层房号"
            >
              <a-select-option
                v-for="opt in houseOpts"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </a-select-option>
            </v-select>
          </a-form-item>
          <a-form-item
            label="租期时间"
            :name="['houseList',index,'rentTimes']"
            :rules="rulesRef.rentTimes"
          >
            <a-range-picker
              v-model:value="bindHouseItem.rentTimes"
              style="width: 100%;"
              :placeholder="['起租时间', '到期时间']"
              value-format="YYYY-MM-DD"
              @change="dateChange($event,bindHouseItem)"
            />
          </a-form-item>
        </div>
        <div class="add-btn">
          <a-button
            v-if="index===modelRef.houseList.length-1"
            type="link"
            @click="add"
          >
            新增房屋
          </a-button>
        </div>
      </a-card>
      <div class="btn-section">
        <a-button
          type="primary"
          @click="save"
        >
          保存
        </a-button>
        <a-button
          type="primary"
          @click="goBlack"
        >
          返回
        </a-button>
      </div>
    </a-form>
  </div>
</template>
<script lang="ts">
import CompanyMangement, { BindHouseInfo } from '@/models/company-mangement';
import {
  reactive, ref, onMounted, defineComponent, watchEffect, PropType, computed, onActivated, toRef, toRefs, onUnmounted,
} from 'vue';
import { Dayjs } from 'dayjs';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { useCachePage } from '~composition/common';
import { useParkInfoOpts } from '~composition/park';
import { useBindHouse } from './config';

export default defineComponent({
  props: {
    companyId: {
      type: String as PropType<string>,
      default: '',
    },
    parkOrgId: {
      type: String as PropType<string>,
      default: '',
    },
    name: {
      type: String as PropType<string>,
      default: '',
    },
    parkOrgName: {
      type: String as PropType<string>,
      default: '',
    },
  },
  setup(props, { emit }) {
    const router = useRouter();
    const companyMangementInstance = new CompanyMangement();
    const {
      name, companyId, parkOrgName, parkOrgId,
    } = toRefs(props);
    // const buildingOpts = ref<SelectOptions>([]);
    // const houseOpts = ref<SelectOptions>([]);
    const {
      buildingOpts,
      houseOpts,
      getBuildingOpts,
      getHouseOpts,
    } = useParkInfoOpts();
    const { modelRef, rulesRef } = useBindHouse();
    const { remoteCachePage } = useCachePage();
    // console.log(name.value, companyId.value, parkOrgName.value, '----name, companyId, parkOrgName');
    const add = () => {
      modelRef.houseList.push({
        rentTimes: [],
        houseId: '',
        buildingId: '',
        rentEndTime: '',
        rentStartTime: '',
      });
    };
    const del = (index:number) => {
      modelRef.houseList.splice(index, 1);
    };
    const dateChange = (dates:string[], bindHouseItem:BindHouseInfo) => {
    //   console.log(dates, '----dates----dates');
      const [startTime, endTime] = dates;
      bindHouseItem.rentStartTime = startTime;
      bindHouseItem.rentEndTime = endTime;
    };
    const buildingChange = async (buildingId:string, bindHouseItem:BindHouseInfo) => {
      bindHouseItem.houseId = '';
      getHouseOpts(buildingId);
    };
    const goBlack = () => {
      router.push({ name: 'companyMangementPompanyList' });
    };
    const formRef = ref();
    const save = async () => {
      formRef.value.validate().then(async () => {
        await companyMangementInstance.bindHouse(props.companyId, modelRef.houseList);
        message.success('绑定成功');
        goBlack();
      });
    };

    const init = async () => {
      add();
      getBuildingOpts(parkOrgId.value);
    };
    onMounted(() => {
      init();
    });
    onUnmounted(() => {
      remoteCachePage('CompanyMangementPompanyList'); // 删除页面缓存
    });
    return {
      modelRef,
      rulesRef,
      formRef,
      buildingOpts,
      houseOpts,
      dateChange,
      add,
      del,
      goBlack,
      save,
      buildingChange,
    };
  },
});
</script>
<style lang="scss" scoped>
.bind-house {
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

    :deep(.ant-card:first-child .ant-card-body) {
        padding-bottom: 0;
    }

    .add-btn {
        text-align: right;
    }

    .btn-section {
        text-align: center;

        button {
            margin-right: 10px;
        }
    }
}
</style>
