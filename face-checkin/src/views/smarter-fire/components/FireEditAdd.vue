<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-03-03 11:20:07
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    class="customer-add-edit"
    :visible="true"
    :width="900"
    :title="customerConfig.title"
    @cancel="emit('update:show', false)"
  >
    <div>
      <a-form
        ref="formRef"
        :model="modelRef"
        :rules="rulesRef"
        :label-col="{ span: 5 }"
      >
        <div>
          <div>
            <h2 class="customer-title">
              设备信息
            </h2>
            <div>
              <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="所属园区"
                  >
                    <span>{{ modelRef.parkOrgName }}</span>
                  </a-form-item>
                </a-col>
                <!-- <a-col :span="12">
                  <a-form-item
                    label="所属系统"
                  >
                    <span>{{ options.SYSTEM_TYPE[modelRef.systemType]?.label }}</span>
                  </a-form-item>
                </a-col> -->
              </a-row>
              <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="设备ID"
                  >
                    <span>{{ modelRef.deviceId }}</span>
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item
                    label="设备名称"
                  >
                    <span>{{ modelRef.deviceName }}</span>
                  </a-form-item>
                </a-col>
              </a-row>
              <!-- <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="安装楼栋"
                  >
                    <span>{{ modelRef.buildingName }}</span>
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item
                    label="安装楼层"
                  >
                    <span>{{ modelRef.floor }}楼</span>
                  </a-form-item>
                </a-col>
              </a-row> -->
              <!-- <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="安装房屋"
                  >
                    <span>{{ modelRef.houseName }}</span>
                  </a-form-item>
                </a-col>
                <a-col :span="12">
                  <a-form-item
                    label="安装位置"
                  >
                    <span>{{ modelRef.installationLocation }}</span>
                  </a-form-item>
                </a-col>
              </a-row> -->
            </div>
          </div>
          <div>
            <h2 class="customer-title">
              报警信息
            </h2>
            <div>
              <a-row>
                <a-col :span="12">
                  <a-form-item
                    label="报警类型"
                  >
                    <span>{{ options['FILE_DEVICE_WARN_TYPE'][modelRef.warnType]?.label||'-' }}</span>
                  </a-form-item>
                </a-col>
                <!-- <a-col :span="12">
                  <a-form-item
                    label="报警原因"
                  >
                    <span>{{ modelRef.warnReasonCode }}</span>
                  </a-form-item>
                </a-col> -->
                <a-col :span="12">
                  <a-form-item
                    label="报警时间"
                  >
                    <span>{{ modelRef.warnTime }}</span>
                  </a-form-item>
                </a-col>
              </a-row>
            </div>
          </div>
          <div>
            <h2 class="customer-title">
              确认结果
            </h2>
            <div>
              <a-radio-group
                v-model:value="modelRef.realReasonType"
                name="radioGroup"
                :disabled="formDisabled"
              >
                <a-radio :value="1">
                  真实报警
                </a-radio>
                <a-radio :value="2">
                  误报
                </a-radio>
                <a-radio :value="3">
                  消防演习
                </a-radio>
              </a-radio-group>
            </div>
          </div>
          <div>
            <h2 class="customer-title mg-20">
              处理意见
            </h2>
            <div>
              <a-textarea
                v-model:value="modelRef.opinion"
                show-count
                :maxlength="99"
                placeholder="请输入处理意见"
                :disabled="formDisabled"
              />
            </div>
          </div>
        </div>
      </a-form>
    </div>
    <template #footer>
      <a-button
        @click="emit('update:show', false)"
      >
        {{ formDisabled ? '关闭':'取消' }}
      </a-button>
      <a-button
        v-show="customerConfig.type === 'edit'"
        type="primary"
        @click="confirm"
      >
        确定
      </a-button>
    </template>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, defineComponent, toRefs,
} from 'vue';
import { useHashMapDics } from '~composition/useHashMapDic';
import CustomerVo from '~models/smarter-fire';
import { customerForm } from '../composition/useForm';

export default defineComponent({
  props: {
    customerConfig: {
      type: Object,
      default: () => ({
        title: '',
        data: {},
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    const singleValue = ref<number>(1);
    const radioValue = ref<string>('');
    const { modelRef, rulesRef } = customerForm();
    const { customerData, type } = toRefs(props.customerConfig);
    const {
      parkOpts, options, tagOpts, defaultOptions,
    } = useOptions(customerData?.value, modelRef, type.value);
    const CustomerVoReq = new CustomerVo();
    const formDisabled = type.value === 'detail';
    const fill = () => {
      Object.entries(modelRef).forEach(([key]) => {
        if ((customerData.value as any)[key] || (customerData.value as any)[key] === 0) {
          const formData = modelRef as any;
          if (key === 'parkOrgNames') {
            formData.parkOrgIds = customerData.value[key].map((item: any) => item.value);
          } else {
            formData[key] = customerData.value[key];
          }
        }
      });
    };
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          await CustomerVoReq.updateMeterStatus({ ...modelRef });
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {
      if (type.value !== 'add') {
        fill();
      }
    };
    init();
    return {
      singleValue,
      radioValue,
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      parkOpts,
      tagOpts,
      formDisabled,
      defaultOptions,
    };
  },
});

function useOptions(customerData?:any, modelRef?:any, type?:any) {
  const parkOpts = ref<SelectOptions>([]);
  const tagOpts = ref<SelectOptions>([]);
  const defaultOptions = ref<SelectOptions>([]);
  const options = useHashMapDics<
    ['FILE_DEVICE_WARN_TYPE', 'SYSTEM_TYPE']
  >(['FILE_DEVICE_WARN_TYPE', 'SYSTEM_TYPE']);
  (async () => {
  })();
  return {
    parkOpts,
    options,
    tagOpts,
    defaultOptions,
  };
}
</script>
<style scoped lang="scss">
.customer-add-edit {
    .cursor {
        cursor: pointer;
    }

    .customer-title {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
        font-size: 18px;
        font-weight: bold;

        span {
            margin-left: 10px;
            font-weight: normal;
            color: var(--ant-primary-color);
            cursor: pointer;
        }
    }

    .mg-20 {
        margin-top: 20px;
    }
}
</style>
