<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-07-08 10:08:16
 * @LastEditors: GUOCHAO82
 * @Description:
-->
<template>
  <v-modal
    class="advert-add-edit"
    :visible="true"
    :width="730"
    :title="advertConfig.title"
    @cancel="emit('update:show', false)"
  >
    <div>
      <a-form
        ref="formRef"
        :model="modelRef"
        :rules="rulesRef"
        :label-col="{ span: 5 }"
      >
        <a-form-item
          label="广告名称"
          name="name"
        >
          <a-input
            v-model:value="modelRef.name"
            :disabled="formDisabled"
            placeholder="请输入广告名称"
          />
        </a-form-item>
        <a-form-item
          label="推送范围"
          name="parkOrgIds"
        >
          <v-select
            v-model:value="modelRef.parkOrgIds"
            :disabled="formDisabled"
            placeholder="请选择推送范围"
            mode="multiple"
          >
            <a-select-option
              v-for="opt in parkOpts"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="排序"
          name="sort"
        >
          <a-input
            v-model:value="modelRef.sort"
            :disabled="formDisabled"
            :maxlength="2"
            placeholder="请输入排序序号（正整数），数字越小越靠前"
            style="width: 100%;"
          />
        </a-form-item>
        <a-form-item
          label="位置"
          name="position"
        >
          <v-select
            v-model:value="modelRef.position"
            :disabled="formDisabled"
            placeholder="请选择位置"
            :allow-clear="false"
          >
            <a-select-option
              v-for="opt in options.ADVERTISEMENT_POSITION"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <a-form-item
          label="广告图片"
          name="picture"
        >
          <v-upload
            v-model:value="modelRef.picture"
            :disabled="formDisabled"
            accept=".jpg,.bmp,.png,.gif"
            tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸1035*471PX"
            :size="5"
          />
        </a-form-item>
        <a-form-item
          label="跳转类型"
          name="jumpType"
        >
          <v-select
            v-model:value="modelRef.jumpType"
            placeholder="请选择跳转类型"
          >
            <a-select-option
              v-for="opt in options.ADVERTISEMENT_JUMP_TYPE"
              :key="opt.value"
              :value="opt.value"
            >
              {{ opt.label }}
            </a-select-option>
          </v-select>
        </a-form-item>
        <!-- '跳转地址' :'小程序AppID' -->
        <a-form-item
          v-if="modelRef.jumpType"
          :label="{ 1: '跳转地址', 2: '小程序AppID' }[modelRef.jumpType]"
          name="jumpContent"
        >
          <a-input
            v-model:value="modelRef.jumpContent"
            :disabled="formDisabled"
            :maxlength="20"
            :placeholder="{ 1: '请输入跳转地址', 2: '请输入需跳转的小程序AppID' }[modelRef.jumpType]"
          />
          <div v-if="modelRef.jumpType===1 && !formDisabled">
            请联系开发者将跳转地址添加至信任列表
          </div>
        </a-form-item>
        <a-form-item
          label="生效时间"
          name="effectTimeStart"
        >
          <a-range-picker
            v-model:value="effectTime"
            :show-time="{ format: 'HH:mm' }"
            style="width: 100%;"
            :disabled="formDisabled"
            :disabled-date="disabledCheckoutDate"
            :disabled-time="disabledCheckTime"
            :placeholder="['生效开始时间  ', '生效结束时间']"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
      </a-form>
    </div>
    <template #footer>
      <a-button
        @click="emit('update:show', false)"
      >
        {{ formDisabled ? '关闭':'取消' }}
      </a-button>
      <a-button
        v-show="!formDisabled"
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
  ref, reactive, computed, defineComponent, onMounted, PropType, toRefs,
} from 'vue';
import { message, Modal } from 'ant-design-vue';
import * as dayjs from 'dayjs';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import Park from '~models/park';
import AdvertVo, { AdvertListKind } from '~models/advertisement';
import { advertForm } from '../composition/useForm';

export default defineComponent({
  props: {
    advertConfig: {
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
    const { parkOpts, options } = useOptions();
    const { modelRef, rulesRef } = advertForm();
    const { advertData, type } = toRefs(props.advertConfig);
    const AdvertVoReq = new AdvertVo();
    const disabledCheckoutDate = (time: any) => time && time < dayjs().startOf('days');
    const disabledCheckTime = (date:any) => {
      const currentDay = dayjs().date(); // 当下的时间
      const currentHours = dayjs().hour();
      const currentMinutes = dayjs().minute(); // 设置的时间
      const currentSeconds = dayjs().second(); // 设置的时间
      const settingHours = dayjs(date).hour();
      const settingDay = dayjs(date).date();
      if (date && settingDay === currentDay && settingHours === currentHours) {
        return {
          disabledHours: () => range(0, currentHours - 1), // 设置为当天现在这小时，禁用该小时，该分钟之前的时间
          disabledMinutes: () => range(0, currentMinutes - 1),
          disabledSeconds: () => range(0, currentSeconds - 1),
        };
      } if (date && settingDay === currentDay && settingHours > currentHours) {
        return {
          disabledHours: () => range(0, currentHours - 1), // 设置为当天现在这小时之后，只禁用当天该小时之前的时间
        };
      } if (date && settingDay === currentDay && settingHours < currentHours) {
        return {
          disabledHours: () => range(0, currentHours - 1), // 若先设置了的小时小于当前的，再设置日期为当天，需要禁用当天现在这小时之前的时间和所有的分
          disabledMinutes: () => range(0, 59),
        };
      } if (date && settingDay > currentDay) {
        return {
          disabledHours: () => [], // 设置为当天之后的日期，则不应有任何时间分钟的限制
          disabledMinutes: () => [],
        };
      }
      return '';
    };
    const range = (start:any, end:any) => {
      const result = [];
      // eslint-disable-next-line no-plusplus
      for (let i = start; i <= end; i++) {
        result.push(i);
      }
      return result;
    };
    const formDisabled = type.value === 'detail';
    const fill = () => {
      Object.entries(modelRef).forEach(([key]) => {
        if ((advertData.value as any)[key] || (advertData.value as any)[key] === 0) {
          const formData = modelRef as any;
          if (key === 'parkOrgNames') {
            formData.parkOrgIds = advertData.value[key].map((item:any) => item.value);
          } else {
            formData[key] = advertData.value[key];
          }
        }
      });
    };
    if (type.value !== 'add') {
      fill();
    }
    const formRef = ref();
    const confirm = () => {
      formRef.value.validate().then(async () => {
        try {
          const apiReq = {
            add: AdvertVoReq.advertCreateApi,
            edit: AdvertVoReq.advertModifyApi,
          };
          await apiReq[type.value as 'add' | 'edit'](modelRef);
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const effectTime = computed({
      set(value: string[]) {
        const [effectTimeStart, effectTimeEnd] = value;
        if (!effectTimeStart || !effectTimeEnd) return;
        modelRef.effectTimeEnd = effectTimeEnd;
        modelRef.effectTimeStart = effectTimeStart;
      },
      get() {
        const { effectTimeStart, effectTimeEnd } = modelRef;
        return effectTimeStart && effectTimeEnd ? [effectTimeStart, effectTimeEnd] : [];
      },
    });
    console.log(effectTime, 555);
    const init = async () => {};
    init();
    return {
      disabledCheckTime,
      disabledCheckoutDate,
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
      options,
      parkOpts,
      formDisabled,
      effectTime,
    };
  },
});

function useOptions() {
  const parkOpts = ref<SelectOptions>([]);
  const options = useHashMapDics<['ADVERTISEMENT_STATUS', 'ADVERTISEMENT_POSITION', 'ADVERTISEMENT_JUMP_TYPE']>([
    'ADVERTISEMENT_STATUS', 'ADVERTISEMENT_POSITION', 'ADVERTISEMENT_JUMP_TYPE',
  ]);
  (async () => {
    parkOpts.value = await new Park().searchParkListName();
  })();
  return {
    parkOpts,
    options,
  };
}
</script>
<style scoped lang="scss">
.advert-add-edit {
    .cursor {
        cursor: pointer;
    }
}
</style>
