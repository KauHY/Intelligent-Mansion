<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-20 17:28:30
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
      :label-col="{ span: 5 }"
    >
      <a-form-item
        label="所属园区"
        v-bind="validateInfos.parkOrgId"
      >
        <v-select
          v-model:value="modelRef.parkOrgId"
          :disabled="type === 'details' || type === 'add' || type === 'groupAdd'"
          placeholder="请选择所属园区"
          @change="parkOrgSelect"
        >
          <a-select-option
            v-for="opt in parkPop"
            :key="opt.value"
            :label="opt.label"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>

      <a-form-item
        label="所在群组"
        v-bind="validateInfos.categoryId"
      >
        <a-tree-select
          v-if="type !== 'details'"
          v-model:value="modelRef.categoryId"
          style="width: 100%;"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          :tree-data="groupOpts"
          placeholder="请选择所在群组"
        />
        <a-input
          v-else
          v-model:value="modelRef.categoryId"
          :disabled="true"
        />
      </a-form-item>

      <a-form-item
        label="选择企业"
        v-bind="validateInfos.businessIds"
      >
        <v-select
          v-model:value="modelRef.businessIds"
          placeholder="请选择企业"
          :disabled="type === 'details'"
          mode="multiple"
          option-filter-prop="label"
          show-search
        >
          <a-select-option
            v-for="opt in parkingLotOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>
      <a-form-item label="备注">
        <a-textarea
          v-model:value="modelRef.remark"
          placeholder="请输入备注"
          :rows="4"
          show-count
          :maxlength="200"
          :disabled="type === 'details'"
        />
      </a-form-item>
    </a-form>
    <template #footer>
      <a-button @click="currentVisible=false">
        {{ type==='details' ? "关闭" : "取消" }}
      </a-button>
      <a-button
        v-show="type!=='details'"
        type="primary"
        @click="confirm"
      >
        确定
      </a-button>
    </template>
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, ref, shallowRef, watch,
} from 'vue';
import { message } from 'ant-design-vue';
import { REGS } from '@/constants/validate';
import CompanyGroupService, { LaneInfo, TreeNodeItem } from '~models/company-group';
import PurchaseService from '~models/purchase-service';
import { useLaneAdd } from '../composition/useForm';

const serviceGroupRequest = new CompanyGroupService();
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
    defaultGroupData: {
      type: Object,
      default: () => ({}),
    },
    // parkingLotOpts: {
    //   type: Object as PropType<SelectOptionsMap>,
    //   default: () => ({}),
    // },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const {
      parkOpts, type, visible, defaultGroupData,
    } = toRefs(props);
    const isReady = ref(false);
    const { defaultData } = toRefs(props);
    const companyPop = ref<SelectOptionsMap>([]);
    const groupOpts = ref<any>([]);
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
    watch(defaultGroupData, (val:any) => {
      // init();
      fill(val);
      if (defaultGroupData.value.parkOrgId) treeHandel(defaultGroupData.value.parkOrgId);
      Object.entries(val).forEach(([key]:any) => {
        console.log('key', val[key]);
        modelRef[key] = val[key];
      });
    }, { deep: true });
    const parkingLotOpts = ref<SelectOptionsMap>([]);
    const parkOrgSelect = async (val:any) => {
      if (val) {
        treeHandel(val);
      }
    };
    const treeHandel = async (val:string) => {
      const params = {
        parkOrgId: val,
        // categoryType: 'TZ',
        categoryType: 'ENTERPRISE_GROUP',
      };
      const data = await serviceGroupRequest.listMultistageCategoryTreemultistage(params);
      const getColumns = data.children.map((item: any) => ({
        ...item, disabled: item.hierarchy === 1,
      }));
      groupOpts.value = getColumns;
      parkingLotOpts.value = await new PurchaseService().companyName(val);
    };
    const confirm = () => {
      validate().then(async () => {
        if (type.value === 'add' || type.value === 'groupAdd') {
          console.log('添加', type.value);

          await serviceGroupRequest.associateltistage(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error: any) => {
        message.warning('操作失败');
      });
    };

    // 初始化
    const details = ref();
    const init = async () => {
      fill(defaultGroupData.value);
      if (defaultGroupData.value.parkOrgId) treeHandel(defaultGroupData.value.parkOrgId);
      console.log('modelRef', modelRef);

      if (type.value === 'edit' || type.value === 'details') {
        // details.value = await serviceRequest.companyQuery(defaultData.value.id);
        console.log('details', details.value);

        // parkOrgChange(defaultData.value.parkOrgId);
      }
      isReady.value = true;
    };
    init();
    return {
      currentVisible,
      details,
      confirm,
      modelRef,
      validateInfos,
      companyPop,
      groupOpts,
      parkOrgSelect,
      parkingLotOpts,
    };
  },
});
</script>
<style lang="less" scoped>
:deep(.serviceLabel .ant-form-item-label > label::after) {
    display: none;
}
</style>
