<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-14 10:52:50
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-30 20:12:32
-->
<template>
  <a-tree-select
    v-model:value="currentValue"
    class="organization-select-tree"
    tree-data-simple-mode
    :field-names="{value:'id',label:'orgName'}"
    :tree-data="organizationOpts"
    placeholder="请选择组织"
    :load-data="onLoadData"
    @change="orgIdChange"
  />
</template>
<script lang='ts'>
import Organization, { OrganizationItem } from '@/models/organization';
import {
  ref, defineComponent, computed, watch, onMounted, nextTick,
} from 'vue';

export default defineComponent({
  name: '',
  components: {},
  props: {
    value: {
      type: String,
      default: '',
    },
    orgName: { // 组织名称
      type: String,
      default: '',
    },
  },
  // emits: ['update:value', 'change'],
  emits: {
    'update:value': (payload:string|null) => true,
    change: (payload:string) => true,
  },
  setup(props, { emit }) {
    const currentValue = computed<string|null>({
      set(value:string|null) {
        emit('update:value', value);
      },
      get() {
        return props.value || null;
      },
    });
    const orgIdChange = (value:string) => {
      emit('change', value);
    };
    const organizationOpts = ref<Array<OrganizationItem>>([]);
    // 去重
    const removal = (targetList:Array<OrganizationItem>, newData:Array<OrganizationItem>) => { // 去重
      const tempArry = [...targetList];
      newData.forEach(({ id }) => {
        const finIndex = tempArry.findIndex(({ id: targetId }) => id === targetId);
        if (finIndex !== -1)tempArry.splice(finIndex, 1);
      });
      // debugger;
      return tempArry;
    };
    const onLoadData = async (treeNode?:any) => {
      try {
        console.log(treeNode, '---treeNode');
        const id = treeNode?.id || '';
        const organizationList = await new Organization().getOrganizationByParentId(id);
        organizationOpts.value = removal(organizationOpts.value, organizationList);
        if (treeNode) treeNode.dataRef.isLeaf = !organizationList.length;
        const tempData = organizationList.map((item) => ({
          ...item,
          pId: id || -1,
        }));
        // debugger;
        organizationOpts.value = organizationOpts.value.concat(tempData);
        console.log(organizationOpts.value, '----organizationOpts.value');
      } catch (error) {
        console.error(error);
      }
    };
    nextTick(() => {
      // if(props.value&&)
      const { value, orgName } = props;
      if (!value || !orgName) return;
      organizationOpts.value = [{
        id: props.value,
        orgName: props.orgName,
      }];
    });
    onMounted(() => {
      onLoadData();
    });
    return {
      currentValue,
      organizationOpts,
      onLoadData,
      orgIdChange,
    };
  },
});
</script>
