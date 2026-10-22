<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-09-16 17:03:37
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
      :type="type"
      :default-form-data="defaultData"
    />
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted,
} from 'vue';
import { message } from 'ant-design-vue';
import User, { UserPwdReset, AddUserParams } from '~models/user';
import addUser from './add-user.vue';
import resetPwd from './reset-pwd.vue';
import { useAddUserForm, useResetPwdForm } from '../config';

const userInstance = new User();
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
    const currentVisible = computed({
      set(show) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    const contentCp = props.type === 'resetPwd' ? resetPwd : addUser;
    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      const { type } = props;
      const form = props.type === 'resetPwd' ? useResetPwdForm() : useAddUserForm();
      const params = { ...form.modelRef };

      form.validate().then(async () => {
        if (type === 'resetPwd') {
          await userInstance.resetPwd(params as UserPwdReset);
          message.success('修改成功');
          currentVisible.value = false;
          return;
        } if (type === 'add') {
          await userInstance.save(params as AddUserParams);
        } else {
          await userInstance.update(params as AddUserParams);
        }
        // if (type === 'edit') delete params.password; // 密码不能编辑
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success');
      }).catch((error:any) => {
        console.log(error, 'error');
        message.warning('操作失败');
      });
    };
    onMounted(() => {
    });
    return {
      currentVisible,
      confirm,
      contentCp,
    };
  },
});
</script>
