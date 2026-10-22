<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-09 15:25:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-20 10:14:57
-->
<template>
  <div class="add-user">
    <a-form
      :model="modelRef"
      :label-col="{span:5}"
    >
      <a-form-item
        label="角色名称"
        v-bind="validateInfos.roleName"
      >
        <a-input
          v-model:value="modelRef.roleName"
          autocomplete="off"
          :maxlength="30"
          placeholder="请输入用户名"
        />
      </a-form-item>
      <!-- <a-form-item
        v-if="isPlatformAdminRole"
        label="角色类型"
        v-bind="validateInfos.roleType"
      >
        <a-radio-group
          v-model:value="modelRef.roleType"
          name="roleType"
        >
          <template
            v-for="(opt,key) in roleTypeOpt"
            :key="key"
          >
            <a-radio
              v-if="key!=2"
              :value="Number(key)"
            >
              {{ opt }}
            </a-radio>
          </template>
        </a-radio-group>
      </a-form-item> -->

      <!-- <a-form-item
        v-if="modelRef.roleType===1&&isPlatformAdminRole"
        label="关联租户"
        v-bind="validateInfos.orgId"
      >
        <a-select
          v-model:value="modelRef.orgId"
          show-search
          allow-clear
          :filter-option="false"
          placeholder="请输入住户名称"
          @change="orgIdChange($event,false)"
          @search="remoteSearch"
        >
          <template
            v-if="tenantSelectLoading"
            #notFoundContent
          >
            <a-spin size="small" />
          </template>
          <a-select-option
            v-for="opt in tenantOpts"
            :key="opt.headOrgId"
          >
            {{ opt.orgName }}
          </a-select-option>
        </a-select>
      </a-form-item> -->

      <a-form-item
        v-if="!isPlatformAdminRole"
        label="关联组织"
        v-bind="validateInfos.orgId"
      >
        <organization-select-tree
          v-model:value="modelRef.orgId"
          :org-name="defaultFormData.orgName"
          @change="orgIdChange"
        />
      </a-form-item>
      <a-form-item
        label="关联用户"
        v-bind="validateInfos.userIds"
      >
        <!-- <v-select-employee
          v-model:value="modelRef.userIds"
          :org-id="modelRef.orgId"
          :disabled="!modelRef.orgId"
          :default-select-opts="defaultFormData.users"
          placeholder="请选择关联用户"
        /> -->
        <v-select
          v-model:value="modelRef.userIds"
          placeholder="请选择关联用户"
          :disabled="!modelRef.orgId"
          mode="multiple"
        >
          <a-select-option
            v-for="opt in parkList"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </v-select>
      </a-form-item>

      <a-form-item
        label="角色描述"
        v-bind="validateInfos.password"
      >
        <a-textarea
          v-model:value="modelRef.roleDescription"
          :maxlength="200"
        />
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, toRefs, computed, onMounted, watch,
} from 'vue';
// import Organization, { OrganizationItem } from '@/models/organization';
import { useUserInfo } from '@/composition/user';
import User, { RsUserListItem } from '~models/user';
// import Tenant from '~models/tenant';
import { useRemoteFilterSelect } from '~composition/common';
import organizationSelectTree from '~components/organization-select-tree';
import { useAddRoleForm, roleTypeOpt } from '../config';
// const roleInstance = new Role();
export default defineComponent({
  name: '',
  components: { organizationSelectTree },
  props: {
    defaultFormData: {
      type: Object,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const { defaultFormData } = toRefs(props);
    const { isPlatformAdminRole } = useUserInfo();
    const parkList = ref<SelectOptions>();

    // 初始化表单
    const {
      modelRef,
      validateInfos,
      fill: baseFill,
    } = useAddRoleForm(isPlatformAdminRole, defaultFormData.value);

    // 租户远程搜索
    // const {
    //   loading: tenantSelectLoading,
    //   list: tenantOpts,
    //   remoteSearch,
    //   setDefaultOptios:
    //   tenantSetDefaultOptios,
    // } = useRemoteFilterSelect(new Tenant().getTenantOpt, 'headOrgId');
    const {
      loading: userSelectLoading,
      list: userOpts,
      remoteSearch: userRemoteSearch,
      otherParams,
      // setDefaultOptios: userSetDefaultOptios,
    } = useRemoteFilterSelect(new User().rsUserList, {
      optionsValueKey: 'userId',
      disableAutoSearch: true,
    });

    const defaultEmployeeSelectOpts = ref<SelectOptions>([]);
    const fill = async () => {
      baseFill();
      defaultEmployeeSelectOpts.value = props.defaultFormData.users;
      // tenantSetDefaultOptios([{
      //   headOrgId: props.defaultFormData.orgId,
      //   orgName: props.defaultFormData.orgName,
      // }]);
      // const userDefaultOpts:RsUserListItem[] = [];
      // Object.entries(props.defaultFormData.userMap).forEach(([userId, username]) => {
      //   userDefaultOpts.push({
      //     userId,
      //     username: username as string,
      //   });
      // });
      // userSetDefaultOptios(props.defaultFormData.users);
      parkList.value = await new User().rsUserList({
        value: '',
        orgId: modelRef.orgId,
      });
    };
    const orgIdChange = async () => {
      defaultEmployeeSelectOpts.value = [];
      modelRef.userIds = [];
      parkList.value = await new User().rsUserList({
        value: '',
        orgId: modelRef.orgId,
      });
    };
    const init = async () => {
      if (props.type !== 'add') fill(); // 初始化赋值
      // orgIdChange(modelRef.orgId, true); // 初始化获取租户下拉数据
      // userRemoteSearch('', true); // 重新调用索关联租户下拉列表
    };
    onMounted(() => {
      init();
    });
    return {
      parkList,
      modelRef,
      validateInfos,
      roleTypeOpt,
      isPlatformAdminRole, // 是否是平台管理员
      // tenantSelectLoading, // 租户下拉框loading
      // tenantOpts, // 租户下拉数据
      // remoteSearch, // 租户下拉搜索
      // 用户下拉
      userSelectLoading,
      userOpts,
      userRemoteSearch,
      orgIdChange,
    };
  },
});
</script>
