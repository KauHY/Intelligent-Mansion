<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-09 15:25:08
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-18 15:13:01
-->
<template>
  <div class="authority-allot">
    <a-tabs v-model:activeKey="activeKey">
      <a-tab-pane
        :key="0"
        tab="菜单权限"
      >
        <a-tree
          v-if="menuTree.length"
          v-model:checkedKeys="menuCheckedKeys"
          checkable
          default-expand-all
          :field-names="{key:'id',title:'name'}"
          :tree-data="menuTree"
          @check="treeCheck($event,'menu')"
        />
      </a-tab-pane>
      <a-tab-pane
        :key="2"
        tab="数据权限"
      >
        <a-tree
          v-if="organizationTree.length"
          ref="treeData"
          v-model:checkedKeys="organizationTreeCheckKeys"
          checkable
          default-expand-all
          :field-names="{key:'id',title:'orgName'}"
          :tree-data="organizationTree"
          @check="treeCheck($event,'organization')"
        />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>
<script lang='ts'>
import Menu, { RsMenuTree } from '@/models/meun';
import Organization, { OrganizationItem } from '@/models/organization';
import {
  defineComponent, ref, toRefs, computed, onMounted, reactive, nextTick,
} from 'vue';
import { rootMenu } from '@/constants/config';

import Role from '~models/role';

const roleInstance = new Role();

export default defineComponent({
  name: '',
  components: {},
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
  setup(props, { emit, attrs }) {
    const chartRef = ref(null);
    const activeKey = ref<0|2>(0);
    const menuTree = ref<Array<RsMenuTree>>([]);
    const menuCheckedKeys = ref<string[]>([]);
    const allOrganization = ref();
    const organizationTree = ref<Array<OrganizationItem>>([]);
    const organizationTreeCheckKeys = ref<string[]>([]);
    const calleArr = async (data:any, orgId:string, isInt:boolean, isGet:boolean) => {
      data.forEach((item:any) => {
        item.disabled = isInt;
        if (isGet) {
          calleArr(item.children, orgId, false, true);
        } else if (item.id === orgId) {
          if (item.children.length > 0) {
            calleArr(item.children, orgId, false, true);
          }
        } else if (item.children.length > 0) {
          calleArr(item.children, orgId, true, false);
        }
      });
      return data;
    };

    // 初始化选数据
    const fill = async () => {
      const { id } = props.defaultFormData;
      const { orgId } = props.defaultFormData;
      organizationTreeCheckKeys.value = await roleInstance.getPermission(id, 2);
      allOrganization.value = organizationTree.value[0];
      if (allOrganization.value.id !== orgId) {
        calleArr(allOrganization.value.children, orgId, true, false);
      }
      menuCheckedKeys.value = await roleInstance.getPermission(id, 0);
    };

    const init = async () => {
      rootMenu.children = await new Menu().getRsMenuTree();
      menuTree.value = [rootMenu as RsMenuTree];
      organizationTree.value = await new Organization().getOrganizationTreeByorgName();
      fill();
    };
    const treeCheck = (id:string, { checked }:{checked:boolean}) => {
      console.log(id, checked);
    };
    const confirm = async () => {
      // console.log('确认了-----');
      const { id } = props.defaultFormData;
      console.log(id, activeKey.value, '------id');
      // const ids = activeKey.value === 0 ? menuCheckedKeys.value : organizationTreeCheckKeys.value;
      await roleInstance.setPermission(id, menuCheckedKeys.value, organizationTreeCheckKeys.value);
    };
    // attrs.onConfirm = () => {
    //   console.log('确认了----阿斯顿发生发生');
    // };

    init();
    return {
      menuTree,
      menuCheckedKeys,
      treeCheck,
      chartRef,
      organizationTree,
      organizationTreeCheckKeys,
      activeKey, // 当前选中tab
      confirm,
    };
  },
});
</script>
