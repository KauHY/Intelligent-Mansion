<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-21 10:44:17
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
      ref="formRef"
      :model="modelRef"
      :label-col="{ span: 5 }"
    >
      <a-form-item
        label="所属园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          placeholder="请选择所属园区"
          :disabled="type==='details'"
          @change="parkOrgChange"
        >
          <a-select-option
            v-for="opt in parkPop"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item
        label="企业名称"
        v-bind="validateInfos.companyId"
      >
        <v-select
          v-model:value="modelRef.companyId"
          placeholder="请选择企业名称"
          :disabled="type==='details'"
        >
          <a-select-option
            v-for="opt in parkingLotOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>

      <a-form-item
        label="服务名称"
        v-bind="validateInfos.name"
      >
        <a-input
          v-model:value="modelRef.name"
          :disabled="type==='details'"
          placeholder="请输入服务名称"
          :maxlength="30"
        />
      </a-form-item>

      <a-form-item
        label="供应类型"
        v-bind="validateInfos.supplyType"
      >
        <v-select
          v-model:value="modelRef.supplyType"
          :disabled="type==='details'"
          placeholder="请选择供应类型"
        >
          <a-select-option
            v-for="opt in parkOpts.SUPPLY_TYPE"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>

      <a-form-item
        label="供应详情"
        v-bind="validateInfos.detail"
      >
        <v-editor
          v-model:value="modelRef.detail"
          :disabled="type==='details'"
          placeholder="请输入供应详情"
          @change="changeQuillEditor"
        />
      </a-form-item>

      <a-form-item
        label="跳转类型"
        v-bind="validateInfos.jumpType"
      >
        <v-select
          v-model:value="modelRef.jumpType"
          placeholder="请选择跳转类型"
          :disabled="type==='details'"
        >
          <a-select-option
            v-for="opt in parkOpts.JUMP_TYPE"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>

      <a-form-item
        v-if="modelRef.jumpType !== 0"
        :label="modelRef.jumpType===1?'跳转地址':'小程序AppID'"
        v-bind="validateInfos.url"
      >
        <a-input
          v-model:value="modelRef.url"
          :disabled="type==='details'"
          :maxlength="modelRef.jumpType===1?200:20"
          :placeholder="modelRef.jumpType===1?'请输入跳转地址':'请输入小程序AppID'"
        />
      </a-form-item>
      <a-form-item
        label="联系人"
        v-bind="validateInfos.contact"
      >
        <a-input
          v-model:value="modelRef.contact"
          :disabled="type==='details'"
          placeholder="请输入联系人"
          :maxlength="30"
        />
      </a-form-item>
      <a-form-item
        label="联系电话"
        v-bind="validateInfos.tel"
        :rules="{
          required: modelRef.tel?true:false,
        }"
      >
        <a-input
          v-model:value="modelRef.tel"
          :disabled="type==='details'"
          placeholder="请输入联系电话"
        />
      </a-form-item>

      <a-form-item label="备注">
        <a-textarea
          v-model:value="modelRef.remark"
          placeholder="请输入备注"
          :rows="4"
          show-count
          :maxlength="200"
          :disabled="type==='details'"
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
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watch,
} from 'vue';
import { message } from 'ant-design-vue';
import { REGS } from '@/constants/validate';
import CompanyMangement from '~models/company-mangement';
import EnterpriseService, { LaneInfo } from '~models/supply-service';
import { useLaneAdd } from '../composition/useForm';

const serviceRequest = new EnterpriseService();

// const carPassInstance = new CarPass();
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
      type: Object,
      default: () => ({}),
    },
    parkPop: {
      type: Object as PropType<SelectOptions>,
      default: () => ({}),
    },
    // parkingLotOpts: {
    //   type: Object as PropType<SelectOptionsMap>,
    //   default: () => ({}),
    // },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const formRef = ref();
    const { parkOpts, type, visible } = toRefs(props);
    const isReady = ref(false);
    const { defaultData } = toRefs(props);
    const companyPop = ref<SelectOptionsMap>([]);
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return ['edit'].includes(type.value) ? isReady.value && visible.value : visible.value;
      },
    });

    const {
      modelRef,
      validate,
      validateInfos,
      fill,
    } = useLaneAdd();

    watch(() => modelRef.jumpType, (value, oldValue) => {
      // console.log(props.selectTreeNode.key, '----props.selectTreeNode.key');
      console.log('value', value);
      if (oldValue) modelRef.url = '';
    });
    watch(() => modelRef.parkOrgId, (value, oldValue) => {
      console.log(value, '----props.selectTreeNode.key');
      if (oldValue)modelRef.companyId = '';
    });

    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'add') {
          console.log('添加', type.value);
          await serviceRequest.saveSupplyMsg(modelRef);
          // await serviceRequest.laneAdd(modelRef);
        } else if (type.value === 'edit') {
          await serviceRequest.supplyServicemModify(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error: any) => {
        message.warning('操作失败');
      });
    };

    const parkingLotOpts = ref<SelectOptionsMap>([]);
    const parkOrgChange = async (orgId: string) => {
      console.log('orgId', orgId);
      parkingLotOpts.value = await serviceRequest.companyName(orgId);
      console.log('parkingLotOpts', parkingLotOpts.value);
    };
    const changeQuillEditor = () => {
      if (modelRef.detail) {
        formRef.value.clearValidate('detail');
      }
    };

    // 初始化
    const details = ref();
    const init = async () => {
      if (type.value === 'edit' || type.value === 'details') {
        details.value = await serviceRequest.querysupplyService(defaultData.value.id);
        fill(details.value);
        parkOrgChange(defaultData.value.parkOrgId);
        isReady.value = true;
      }
    };
    init();
    return {
      formRef,
      changeQuillEditor,
      currentVisible,
      details,
      confirm,
      modelRef,
      validateInfos,
      parkOrgChange,
      companyPop,
      parkingLotOpts,
    };
  },
});
</script>
