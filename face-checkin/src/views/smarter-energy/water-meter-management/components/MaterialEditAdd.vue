<template>
  <v-modal
    class="customer-add-edit"
    :visible="true"
    :width="500"
    :title="changeMeterConfig.title"
    @ok="confirm"
    @cancel="emit('update:show', false)"
  >
    <div>
      <a-form
        ref="formRef"
        :model="modelRef"
        :rules="rulesRef"
        :label-col="{span:6}"
      >
        <a-form-item
          label="旧表读数"
          name="oldReading"
        >
          <a-input
            v-model:value="modelRef.oldReading"
            :maxlength="30"
            placeholder="请输入旧表读数"
          />
        </a-form-item>
        <a-form-item
          v-if="changeMeterConfig.data.meterType===2"
          label="新表设备ID"
          name="newDeviceId"
        >
          <a-input
            v-model:value="modelRef.newDeviceId"
            :maxlength="30"
            placeholder="请输入新表设备ID"
          />
        </a-form-item>
        <a-form-item
          label="新表读数"
          name="newReading"
        >
          <a-input
            v-model:value.number="modelRef.newReading"
            placeholder="请输入新表读数"
          />
        </a-form-item>
        <a-form-item
          label="最大量程"
          name="newMaxRange"
        >
          <a-input
            v-model:value.number="modelRef.newMaxRange"
            placeholder="最大量程"
          />
        </a-form-item>
      </a-form>
    </div>
  </v-modal>
</template>

<script lang="ts">
import {
  ref, defineComponent, toRefs,
} from 'vue';
import { message } from 'ant-design-vue';
import SmarterMeter, { changeParams } from '~models/smarter-energy';
import { useResetPwdForm } from '../composition/useForm';

const userInstance = new SmarterMeter();

export default defineComponent({
  props: {
    changeMeterConfig: {
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
    const { modelRef, rulesRef } = useResetPwdForm();
    const form = useResetPwdForm();
    const formRef = ref();
    const { data } = toRefs(props.changeMeterConfig);
    console.log(data, 'data');
    const confirm = () => {
      const params = { ...form.modelRef, oldMeterId: data.value.id, deviceType: 6 };
      console.log(params, 'params');
      formRef.value.validate().then(async () => {
        try {
          await userInstance.exchangeMeter(params as unknown as changeParams);
          message.success('操作成功');
          emit('refresh', {});
          emit('update:show', false);
        } catch (error: any) {
          console.log(error);
        }
      });
    };
    const init = async () => {};
    init();
    return {
      confirm,
      emit,
      modelRef,
      formRef,
      rulesRef,
    };
  },
});

</script>
<style scoped lang="scss">
.customer-add-edit {
    .cursor {
        cursor: pointer;
    }

    .customer-title {
        margin-bottom: 20px;
        font-size: 18px;
        font-weight: bold;
    }
}
</style>
