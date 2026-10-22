<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-27 12:32:30
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="630"
    class="operate-dialog"
    @ok="confirm"
  >
    <a-form
      :model="modelRef"
      :label-col="{span:6}"
    >
      <a-form-item
        label="组织名称"
        v-bind="validateInfos.orgName"
      >
        <a-input
          v-model:value="modelRef.orgName"
          autocomplete="off"
          :disabled="type==='details'"
          :maxlength="30"
          placeholder="请输入组织名称"
        />
      </a-form-item>
      <a-form-item
        label="组织类型"
        v-bind="validateInfos.address"
      >
        <a-select
          v-model:value="modelRef.orgType"
          :disabled="type==='edit' || type==='details'"
          placeholder="请输选择组织类型"
        >
          <a-select-option
            v-for="(orgName,key) in orgTypeNames"
            :key="key"
            :value="Number(key)"
          >
            {{ orgName }}
          </a-select-option>
        </a-select>
        <div class="title">
          类型为"机构时"，可创建下级组织；类型为"园区"时，不可创建下级组织
        </div>
      </a-form-item>

      <a-form-item
        label="上级组织"
        v-bind="validateInfos.creditCode"
      >
        <span>{{ defaultData.parentOrgName }}</span>
      </a-form-item>
      <a-form-item
        label="描述"
      >
        <a-textarea
          v-model:value="modelRef.description"
          placeholder="请输入描述"
          :disabled="type==='details'"
          :rows="4"
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
  defineComponent, PropType, computed, onMounted,
} from 'vue';
import { message } from 'ant-design-vue';
import Organizational, { AddOrganization, OrganizationListItem } from '~models/organization';
import { useAddOrgForm, orgTypeNames } from '../config';

const organizationalInstance = new Organizational();
export default defineComponent({
  name: 'OperateDialog',
  components: {},
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    defaultData: {
      type: Object as PropType<OrganizationListItem>,
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
    menuList: {
      type: Array as PropType<Array<MenuItem>>,
      default: () => ([]),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    // console.log(props.defaultData, '----props.defaultData');
    const {
      modelRef, validateInfos, validate, fill,
    } = useAddOrgForm(props.defaultData);
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      const { type } = props;

      validate().then(async () => {
        if (type === 'add') {
          await organizationalInstance.add(modelRef);
        } else {
          await organizationalInstance.update(modelRef);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        console.log(error, '---error');
        message.warning('操作失败');
      });
    };
    onMounted(() => {
      fill();
    });
    return {
      currentVisible,
      confirm,
      modelRef,
      validateInfos,
      orgTypeNames,
    };
  },
});
</script>
<style lang='scss' scoped>
.title {
    margin-top: 7px;
    color: grey;
}
</style>
