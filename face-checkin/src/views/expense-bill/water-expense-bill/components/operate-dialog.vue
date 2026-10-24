<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-12 14:40:26
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
    class="operate-dialog"
    @ok="confirm"
  >
    <a-form
      :model="modelRef"
      :label-col="{span:5}"
    >
      <a-form-item
        label="账单编号"
        v-bind="validateInfos.billNo"
      >
        <span>{{ defaultData.billNo }}</span>
      </a-form-item>

      <a-form-item
        label="所在账期"
        v-bind="validateInfos.accountPeriod"
      >
        <span>{{ defaultData.accountPeriod }}</span>
      </a-form-item>

      <a-form-item
        label="企业名称"
        v-bind="validateInfos.companyName"
      >
        <span>{{ defaultData.companyName }}</span>
      </a-form-item>
      <a-form-item
        label="收款方式"
        v-bind="validateInfos.latestPayDate"
      >
        <span>线下银行转账</span>
      </a-form-item>
      <a-form-item
        label="待收金额（元）"
        v-bind="validateInfos.dueAmount"
      >
        <span>{{ defaultData.dueAmount }}</span>
      </a-form-item>
      <a-form-item
        label="上传凭证"
        v-bind="validateInfos.companyFiles"
      >
        <v-upload
          v-model:value="modelRef.companyFiles"
          :size="20"
          :multiple="true"
          :count="10"
          :disabled="type==='detail'"
          list-type="text"
          accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.png,.jpg,.jpeg,.gif,.zip"
          tip="仅支持以下格式文件，pdf/doc/docx/xls/xlsx/ppt/pptx/png/jpg/jpeg/gif/zip，文件大小不超过20M。"
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <div>
        <a-button @click="currentVisible=false ">
          {{ type==="detail"?'关闭':'取消' }}
        </a-button>
        <a-button
          v-if="type!=='detail'"
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
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watchEffect,
} from 'vue';
import { message } from 'ant-design-vue';
import { useHashMapDics } from '~composition/useHashMapDic';
import RentFee from '~models/company';
import { useModifyDate } from '../composition/useForm';

const rentFeeInstance = new RentFee();

export default defineComponent({
  name: 'OperateDialog',
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
    ids: {
      type: Array as PropType<string[]>,
      default: () => ([]),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const {
      type, visible, defaultData, ids,
    } = toRefs(props);

    const options = useHashMapDics<['Week_Day', 'FORM_CYCLE']>(['Week_Day', 'FORM_CYCLE']);
    // const isReady = ref(false);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });

    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = useModifyDate();

    const confirm = () => {
      validate().then(async () => {
        await rentFeeInstance.uploadMeterFile({
          ...modelRef,
        });
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        message.warning('操作失败');
      });
    };
    onMounted(() => {
      fill(defaultData.value);
    });
    return {
      currentVisible,
      confirm,
      modelRef,
      validateInfos,
      options,
    };
  },
});
</script>

<style lang="less" scoped>
.flex {
    display: flex;
    align-items: center;
    justify-content: flex-start;
}
</style>
