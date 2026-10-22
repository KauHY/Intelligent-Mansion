<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-09 15:25:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-15 10:54:14
-->
<template>
  <div class="add-user">
    <a-form
      :model="modelRef"
      :label-col="{span:5}"
    >
      <a-form-item
        label="用户名"
        v-bind="validateInfos.username"
      >
        <a-input
          v-model:value="modelRef.username"
          :disabled="defaultFormData.adminFlag"
          autocomplete="off"
          placeholder="请输入用户名"
        />
      </a-form-item>
      <!-- <a-form-item
        v-if="type !== 'edit'"
        label="密码"
        v-bind="validateInfos.password"
      >
        <a-input-password
          v-model:value="modelRef.password"
          autocomplete="new-password"
          placeholder="请输入密码"
        />
      </a-form-item> -->
      <a-form-item
        v-if="type!=='edit'"
        label="是否关联员工"
      >
        <a-radio-group
          v-model:value="relation"
          name="radioGroup"
          @change="relationChange"
        >
          <a-radio :value="1">
            是
          </a-radio>
          <a-radio :value="2">
            否
          </a-radio>
        </a-radio-group>
        <div style="color: #b7b2b2;">
          如关联员工，则此用户登录后将具备此员工相应的岗位权限
        </div>
      </a-form-item>
      <template v-if="relation===2">
        <a-form-item
          label="姓名"
          v-bind="validateInfos.realName"
        >
          <a-input
            v-model:value="modelRef.realName"
            placeholder="请输入姓名"
          />
        </a-form-item>
        <a-form-item
          label="手机号"
          v-bind="validateInfos.mobile"
        >
          <a-input
            v-model:value="modelRef.mobile"
            placeholder="请输入手机号"
          />
        </a-form-item>
      </template>
      <template v-else>
        <a-form-item
          v-bind="validateInfos.realName"
          label="姓名"
        >
          <!-- {{ currentUserOrgid }} -->
          <!-- <employee-select
            label-join-key="companyName"
            :org-id="currentUserOrgid"
            :company-type="0"
            placeholder="请输入关键字搜索"
            @change="employeeSelectChange"
          /> -->
          <a-select
            v-model:value="modelRef.realName"
            placeholder=""
            style="width: 100%;"
            :filter-option="false"
            :options="data"
            show-search
            allow-clear
            @change="selectUser"
            @search="fetchUser"
          >
            <template
              v-if="fetching"
              #notFoundContent
            >
              <a-spin size="small" />
            </template>
          </a-select>
          <div
            v-if="!modelRef.realName"
            style="color: #b7b2b2;"
          >
            请输入员工姓名搜索
          </div>
        </a-form-item>
        <a-form-item
          label="手机号"
        >
          <span>{{ modelRef.mobile }}</span>
        </a-form-item>
      </template>
      <a-form-item
        label="所属组织"
        v-bind="validateInfos.orgId"
        placeholder="请选择组织"
      >
        <!-- {{ modelRef.orgId }} -->
        <organization-select-tree
          v-model:value="modelRef.orgId"
          :org-name="defaultFormData.orgName"
        />
      </a-form-item>
      <a-form-item
        label="角色"
        v-bind="validateInfos.roleIds"
      >
        <a-select
          v-model:value="modelRef.roleIds"
          mode="multiple"
          :disabled="defaultFormData.adminFlag"
          :filter-option="false"
          placeholder="请输入角色名称"
          @search="remoteSearch($event)"
        >
          <template
            v-if="loading"
            #notFoundContent
          >
            <a-spin size="small" />
          </template>
          <a-select-option
            v-for="opt in roleOpts"
            :key="opt.value"
          >
            {{ opt.label }}
          </a-select-option>
        </a-select>
        <div
          v-if="type==='add'"
          style="color: #b7b2b2;"
        >
          用户创建成功后，账号及密码将通过短信形式通知用户
        </div>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, computed, onMounted,
} from 'vue';
import { useUserStore } from '@/store/user';
import Role from '~models/role';
import { useRemoteFilterSelect } from '~composition/common';
import { debounce } from 'lodash';
// import { AddUserParams } from '../type';
import organizationSelectTree from '~components/organization-select-tree';
import employeeSelect from '~components/employee-select';
import CompanyMangement, { CompanyInfo } from '~models/company-mangement';
import { useAddUserForm } from '../config';

// const a:string = 1;
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
    const { defaultFormData, type } = toRefs(props);

    const userStore = useUserStore();
    const fetching = ref<Boolean>(false);
    const data = ref<any[]>([]);
    let lastFetchId = 0;

    const currentUserOrgid = userStore.userInfo.orgId;
    // console.log(store.state.user.userInfo.orgId, '---- store.state.user.userInfo');
    const {
      modelRef, validateInfos, rulesRef, fill,
    } = useAddUserForm(defaultFormData.value);
    // rulesRef.password[0].required = props.type !== 'edit';

    // 是否管理员工
    const relation = ref(2);

    // const getDefaultRoleOpt = () => {
    //   // const defaultRoleOpt:any = [];
    //   if (modelRef.roleIds?.length) {
    //     const { roleNames } = props.defaultFormData;
    //     // const opt:any = {};
    //     modelRef.roleIds.forEach((key:string, index) => {
    //       defaultRoleOpt.push({
    //         value: key,
    //         label: roleNames[index],
    //       });
    //     });
    //   }
    //   console.log(defaultRoleOpt, '---defaultRoleOpt');
    //   return defaultRoleOpt;
    // };
    // 角色列表下拉框
    const {
      loading,
      remoteSearch,
      list: roleOpts,
      // setDefaultOptios,
    } = useRemoteFilterSelect<any>(new Role().getRolesListByOrgId, {
      optionsValueKey: 'id',
      disableAutoSearch: false,
    });
    const employeeSelectChange = (value:string, option:SelectOptions[number]) => {
      modelRef.mobile = option.extraInfo?.mobile;
      modelRef.realName = option.label;
    };
    const relationChange = () => {
      modelRef.mobile = '';
      modelRef.realName = '';
    };
    const selectUser = (value:string, option:any) => {
      if (!option) {
        modelRef.mobile = '';
        modelRef.realName = '';
        return;
      }
      const { mobile, name } = option;
      modelRef.mobile = mobile;
      modelRef.realName = name;
    };
    const fetchUser = debounce(async (value:any) => {
      lastFetchId += 1;
      const fetchId = lastFetchId;
      data.value = [];
      fetching.value = true;
      const datas = await new CompanyMangement().selectEmployeesList({
        orgId: userStore.userInfo.orgId || '',
        value,
        companyType: 0,
      });
      if (fetchId !== lastFetchId) {
        // for fetch callback order
        return;
      }
      data.value = datas.map((user:any) => ({
        label: `${user.label}/${user.extraInfo.companyName}`,
        value: user.value,
        name: user.label,
        mobile: user.extraInfo.mobile,
      }));
      // console.log(data.value, '77');
      fetching.value = false;
    }, 500);
    const init = async () => {
      if (type.value === 'edit') {
        fill(); // 弹框数据初始化
        // setDefaultOptios(getDefaultRoleOpt()); // 设置角色下拉框默认选项
      }
      // remoteSearch('', true);
      // console.log(roleOpts, '-----roleOpts');
    };
    onMounted(() => {
      init();
    });
    return {
      selectUser,
      fetchUser,
      data,
      fetching,
      modelRef,
      validateInfos,
      loading,
      remoteSearch,
      roleOpts,
      relation, // 关联用户
      currentUserOrgid,
      employeeSelectChange,
      relationChange,
    };
  },
});
</script>
