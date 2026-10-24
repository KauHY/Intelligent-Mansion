<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-14 14:50:58
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="780"
    :body-style="{ maxHeight: '600px', overflow: 'auto' }"
    class="operate-dialog"
    @ok="confirm"
  >
    <div class="view-house-apply-details">
      <a-form
        :label-col="{span:4}"
      >
        <a-form-item label="领用人">
          {{ defaultData.employeeName }}
        </a-form-item>
        <a-form-item label="所在园区">
          {{ defaultData.parkOrgName }}
        </a-form-item>
        <a-form-item label="所在公司">
          {{ defaultData.companyName }}
        </a-form-item>
        <a-form-item label="领用人电话">
          {{ defaultData.mobile }}
        </a-form-item>
        <a-form-item label="物品名称">
          {{ defaultData.goodsName }}
        </a-form-item>
        <a-form-item label="领用数量">
          {{ defaultData.receiveNum }}
        </a-form-item>
        <a-form-item
          v-if="isAuditInStock"
          label="已归还数量"
        >
          {{ defaultData.returnNumTotal }}
        </a-form-item>
        <a-form-item
          v-if="isAuditInStock"
          label="本次归还数量"
        >
          {{ defaultData.returnNumCurrent }}
        </a-form-item>
        <a-form-item label="备注">
          {{ defaultData.remark }}
        </a-form-item>
        <a-form-item label="申请时间">
          {{ defaultData.applyTime }}
        </a-form-item>
        <!-- <a-form-item
          v-if="isAuditInStock"
          label="本次申请时间"
        >
          {{ defaultData.thisApplyTime }}
        </a-form-item> -->
        <a-form-item
          label="审核结果"
          v-bind="validateInfos.auditState"
        >
          <a-radio-group v-model:value="modelRef.auditState">
            <a-radio :value="isAuditInStock ? 5 : 2">
              通过
            </a-radio>
            <a-radio :value="isAuditInStock ? 6 : 3">
              拒绝
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <template v-if="modelRef.auditState === 2 && !isAuditInStock">
          <a-form-item
            port="needReturn"
            label="是否需归还"
            v-bind="(modelRef.auditState === 2 && !isAuditInStock) ? validateInfos.needReturn : ''"
          >
            <a-radio-group v-model:value="modelRef.needReturn">
              <a-radio :value="1">
                需归还
              </a-radio>
              <a-radio :value="0">
                无需归还
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </template>
        <a-form-item
          label="审核意见"
          v-bind="validateInfos.auditMsg"
        >
          <a-textarea
            v-model:value="modelRef.auditMsg"
            :disabled="type==='details'"
            :rows="4"
            :maxlength="200"
            placeholder="请输入审核意见"
          />
        </a-form-item>
      </a-form>
    </div>

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
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import ReceiveInspection from '~models/asset-mangement';
import Park from '~models/park';
import { useAddPatrolPlace } from '../composition/useFormConf';

export default defineComponent({
  name: 'OperateDialog',
  props: {
    title: {
      type: String,
      default: '',
    },
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as any,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
    inspectionTypes: {
      type: Object as PropType<SelectOptionsMap>,
      default: () => ({}),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const receiveInspectionInessInstance = new ReceiveInspection();
    const isAuditInStock = computed(() => type.value === 'auditInStock');
    const {
      type, visible, defaultData,
    } = toRefs(props);
    const cp = shallowRef();

    // 弹框展示隐藏
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return visible.value;
      },
    });
    const {
      validate, validateInfos, modelRef, fill,
    } = useAddPatrolPlace();
    const equipmentList = ref<SelectOptions>(); // 没接口
    const init = async () => {
      // if (['edit', 'details'].includes(type.value)) {
      //   fill(defaultData.value);
      // }
      fill({ ...defaultData.value, auditState: null });
    };

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      try {
        const isNeedReturn = modelRef.auditState === 2 && !isAuditInStock.value;
        const rules = ['auditMsg', 'auditState'];
        if (isNeedReturn) rules.push('needReturn');
        await validate(rules);
        if (type.value === 'auditInStock') { // 入库审核
          await receiveInspectionInessInstance.auditReturn(modelRef);
        } else { // 出库审核
          await receiveInspectionInessInstance.auditReceive(modelRef);
        }
        message.info('操作成功');
        emit('operate:success');
        currentVisible.value = false;
      } catch (error) {
        console.error(error, 'error');
      }
    };
    const goodsVisible = ref(false);
    onMounted(() => {
      // fill();
      // if (type.value === 'edit') {
      //   // parkOrgIdChange(modelRef.parkOrgId, false);
      // }
      // jobOpts.value =
      init();
    });
    return {
      currentVisible,
      validateInfos,
      modelRef,
      equipmentList,
      confirm,
      goodsVisible,
      isAuditInStock,
      // cp,
    };
  },
});
</script>
<style lang="less" scoped>
.view-house-apply-details {
    .item {
        display: flex;
        justify-content: flex-start;
        margin-bottom: 30px;

        .label {
            width: 160px;
            margin-right: 40px;
            text-align: right;
        }
    }
}
</style>
