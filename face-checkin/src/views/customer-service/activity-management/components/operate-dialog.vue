<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-23 15:37:48
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="820"
    class="operate-dialog"
  >
    <a-form
      ref="formRef"
      :model="modelRef"
      :rules="rulesRef"
      :label-col="{span:5}"
    >
      <a-form-item
        label="活动名称"
        name="name"
      >
        <a-input
          v-model:value="modelRef.name"
          :disabled="disabled"
          :maxlength="30"
          placeholder="请输入活动名称"
        />
      </a-form-item>
      <a-form-item
        label="推送范围"
        name="parkOrgIds"
      >
        <v-select
          v-model:value="modelRef.parkOrgIds"
          :disabled="disabled"
          mode="multiple"
          placeholder="请选择推送范围"
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
        label="活动时间"
        name="activityTimeStart"
      >
        <a-range-picker
          v-model:value="activityTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          :disabled="disabled"
          show-time
        />
      </a-form-item>
      <a-form-item
        label="生效时间"
        name="effectTime"
      >
        <a-date-picker
          v-model:value="modelRef.effectTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          show-time
          :disabled="disabled"
          placeholder="生效时间"
        /><br>
        <p class="form-tip">
          活动保存后，将根据设置的生效时间点显示于C端小程序
        </p>
      </a-form-item>

      <a-form-item
        label="活动地点"
        name="place"
      >
        <a-input
          v-model:value="modelRef.place"
          :disabled="disabled"
          :maxlength="30"
          placeholder="请输入活动地点"
        />
      </a-form-item>
      <a-form-item
        label="是否需要报名"
        name="confRegister"
      >
        <a-checkbox
          v-model:checked="modelRef.confRegister"
          :disabled="disabled"
        >
          需要报名
        </a-checkbox>
      </a-form-item>
      <a-form-item
        v-if="modelRef.confRegister"
        label="报名截止时间"
        name="confRegisterDeadline"
      >
        <a-date-picker
          v-model:value="modelRef.confRegisterDeadline"
          :disabled-date="disabledDateConfRegisterDeadline"
          value-format="YYYY-MM-DD HH:mm:ss"
          show-time
          :disabled="disabled"
          placeholder="报名截止时间"
        />
      </a-form-item>
      <a-form-item
        v-if="modelRef.confRegister"
        label="是否限制报名人数"
        name="confRegisterLimit"
      >
        <a-checkbox
          v-model:checked="modelRef.confRegisterLimit"
          :disabled="disabled"
        >
          限制报名人数
        </a-checkbox>
      </a-form-item>
      <a-form-item
        v-if="modelRef.confRegisterLimit"
        label="报名人数上限"
        name="confRegisterLimitCount"
      >
        <a-input
          v-model:value.number="modelRef.confRegisterLimitCount"
          :disabled="disabled"
          placeholder="请输入报名人数上限"
        />
      </a-form-item>

      <a-form-item
        label="列表图片"
        name="listPicture"
      >
        <v-upload
          v-model:value="modelRef.listPicture"
          accept=".jpg,.bmp,.png,.gif"
          tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸1035*471PX"
          :disabled="disabled"
        />
      </a-form-item>
      <a-form-item
        label="详情图片"
        name="detailPicture"
      >
        <v-upload
          v-model:value="modelRef.detailPicture"
          accept=".jpg,.bmp,.png,.gif"
          tip="支持5M以内的jpg、bmp、png、gif格式文件，建议尺寸1125*844PX"
          :disabled="disabled"
        />
      </a-form-item>
      <a-form-item
        label="活动详情"
        name="content"
      >
        <v-editor
          v-model:value="modelRef.content"
          :disabled="disabled"
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          {{ type==="details"?'关闭':'取消' }}
        </a-button>
        <a-button
          v-if="type!=='details'"
          type="primary"
          @click="confirm"
        >
          确认
        </a-button>
      </div>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, toRef,
} from 'vue';
import { message } from 'ant-design-vue';
// import Form, { FormItem } from 'ant-design-vue/lib/form';
import ActivityManagement, { ActivityInfo } from '~models/activity-management';
import { getFormCnfValue } from '~utils/tool';
import { DictionaryMap } from '~composition/useHashMapDic';
import { useActivityAdd } from '../composition/useForm';

const activityManagementInstance = new ActivityManagement();
export default defineComponent({
  name: 'OperateDialog',
  // components: {
  //   Form,
  //   FormItem,
  // },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object,
      default: () => ({}),
    },
    title: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: '',
    },
    parkOpts: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
    options: {
      type: Object as PropType< DictionaryMap<['DEVICE_TYPE', 'DEVICE_STATUS', 'GATEWAY_TYPE']>>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],

  setup(props, { emit }) {
    const {
      type, visible, defaultData, options,
    } = toRefs(props);
    const isReady = ref(false);

    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return ['edit'].includes(type.value) ? isReady.value && visible.value : visible.value;
      },
    });
    const disabled = computed(() => type.value === 'details'); // 是否只读

    const formRef = ref();
    const {
      modelRef,
      rulesRef,
    } = useActivityAdd();

    // 时间处理相关逻辑
    const { activityTime, disabledDateConfRegisterDeadline } = useTimes(modelRef);

    const confirm = () => {
      formRef.value.validate().then(async () => {
        if (type.value === 'add') {
          await activityManagementInstance.activityAdd(modelRef);
        } else {
          await activityManagementInstance.activityModify(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };

    // 初始化
    const details = ref<ActivityInfo>();
    const fill = async () => {
      modelRef.id = defaultData.value.id;
      details.value = await activityManagementInstance.activityFindById(defaultData.value.id);
      isReady.value = true;
      Object.entries(modelRef).forEach(([key]) => {
        const formData = modelRef as any;
        if ((details.value as any)[key]) {
          formData[key] = (details.value as any)[key];
        }
      });
      console.log(details.value);
      // parkOrgChange(modelRef.parkOrgId, true);
      // parkingLotChange(modelRef.extension.parkingLotId, true);
    };

    if (type.value !== 'add') {
      fill();
    }
    return {
      currentVisible,
      details,
      confirm,
      modelRef,
      activityTime,
      disabledDateConfRegisterDeadline,
      // validateInfos,
      formRef,
      rulesRef,
      disabled,
    };
  },
});
function useTimes(modelRef:ActivityInfo) {
  const activityTime = computed({
    get() {
      const { activityTimeStart, activityTimeEnd } = modelRef;
      return activityTimeStart && activityTimeEnd ? [activityTimeStart, activityTimeEnd] : [];
    },
    set(times:string[]) {
      if (!times) times = [];
      const [activityTimeStart, activityTimeEnd] = times;
      modelRef.activityTimeStart = activityTimeStart || '';
      modelRef.activityTimeEnd = activityTimeEnd || '';
    },
  });

  const disabledDateConfRegisterDeadline = computed(() => (e:any) => e.valueOf() > new Date(modelRef.activityTimeEnd).getTime());
  return {
    activityTime,
    disabledDateConfRegisterDeadline,
  };
}
</script>
