<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-07-29 18:03:53
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="620"
    class="operate-dialog"
    @ok="confirm"
  >
    <component
      :is="contentCp"
      ref="cpRef"
      :type="type"
      :default-form-data="defaultData"
    />
  </v-modal>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, computed, toRefs, onMounted,
} from 'vue';
import { message } from 'ant-design-vue';
import Role, { AddParams } from '~models/role';
import roleInfo from './role-info.vue'; // 角色操作
import authorityAllot from './authority-allot.vue'; // 权限分配
import { useAddRoleForm } from '../config';

const roleInstance = new Role();
export default defineComponent({
  name: 'OperateDialog',
  components: {},
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
    menuList: {
      type: Array as PropType<Array<MenuItem>>,
      default: () => ([]),
    },
  },
  emits: ['update:visible', 'operate:success'],
  setup(props, { emit }) {
    const cpRef = ref<InstanceType<typeof authorityAllot >>();
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    const contentCp = props.type === 'authorityAllot' ? authorityAllot : roleInfo;
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = async () => {
      const { type } = props;
      if (type === 'authorityAllot') { // 权限分配
        await cpRef.value?.confirm();
        message.success('权限分配成功');
        currentVisible.value = false;
        emit('operate:success');
        return;
      }
      const form = useAddRoleForm();
      form.validate().then(async () => {
        if (type === 'add') {
          await roleInstance.add(form.modelRef as AddParams);
        } else {
          await roleInstance.update(form.modelRef as AddParams);
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        // console.log(error, '---error');
        message.warning('操作失败');
      });
    };
    onMounted(() => {
    });
    return {
      currentVisible,
      confirm,
      contentCp,
      cpRef,
      // isPlatformAdminRole,
    };
  },
});
</script>
