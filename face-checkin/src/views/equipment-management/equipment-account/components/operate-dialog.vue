<template>
  <v-modal
    ref="formRef"
    v-model:visible="currentVisible"
    :title="title"
    :width="820"
    class="operate-dialog"
    @ok="confirm"
  >
    <component
      :is="dialogCp"
      :dialog-data="dialogData"
      :type="type"
    />
    <template #footer>
      <a-button
        @click="emit('update:visible', false)"
      >
        {{ type==='detail' ? '关闭':'取消' }}
      </a-button>
      <a-button
        v-show="type!=='detail'"
        type="primary"
        @click="confirm"
      >
        确定
      </a-button>
    </template>
  </v-modal>
</template>
<script lang="ts">
import {
  defineComponent,
  PropType,
  computed,
  onMounted,
  toRefs,
} from 'vue';
import { message } from 'ant-design-vue';
import EquipmentApi from '~models/equipment-management';
import {
  useAddUserForm,
  houseDialogTitleNames,
} from '../config';
import { useAddFloor } from '../composition/useAddFloor';
import { useHouse } from '../composition/useHouse';
import floorInfo from './floor-info.vue';
import houseInfo from './house-info.vue';
import equipment from './equipment-add.vue';

const equipmentLedger = new EquipmentApi();
export default defineComponent({
  name: 'OperateDialog',
  components: {},
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    dialogData: {
      type: Object,
      default: () => ({}),
    },
    parkInfo: {
      type: Object as PropType<{ value: string; label: string }>,
      default: () => ({}),
    },
    type: {
      type: String as PropType<'add' | 'edit' | 'copy'|'detail' >,
      default: '',
    },
  },
  // emits: ['update:visible', 'operate:success'],
  emits: {
    'update:visible': (paylod: boolean) => true,
    'operate:success': (paylod: SelectOptions[0]) => true,
  },
  setup(props, { emit }) {
    const currentVisible = computed({
      set(show: boolean) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    const { type, dialogData } = toRefs(props);
    const dialogCp = computed(() => {
      console.log(dialogData.value, 'dialogData.value');
      let dialogTrst: any;
      if (dialogData.value.hierarchy === 1) {
        dialogTrst = floorInfo;
      } else if (dialogData.value.hierarchy === 2 && dialogData.value.operateType === 'add') {
        dialogTrst = houseInfo;
      } else if (dialogData.value.operateType === 'edit' && dialogData.value.hierarchy === 3) {
        dialogTrst = houseInfo;
      } else if (dialogData.value.operateType === 'edit' && dialogData.value.hierarchy === 2) {
        dialogTrst = floorInfo;
      } else {
        dialogTrst = equipment;
      }
      return dialogTrst;
    });
    const title = computed(() => {
      // 弹框标题
      if (type.value === 'add' && dialogData.value.hierarchy === 2) return '新增二级分类';
      if (type.value === 'add' && dialogData.value.hierarchy === 3) return '新增设备';
      if (type.value === 'edit' && dialogData.value.hierarchy === 3) return '编辑二级分类';
      if (type.value === 'edit' && dialogData.value.hierarchy === 2) return '编辑一级分类';
      if (type.value === 'edit') return '编辑设备';
      const dialogNames = houseDialogTitleNames;
      return dialogNames[type.value];
    });

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      console.log(dialogData.value, type.value, 'dialogData.value.type');
      let form: any;
      if (dialogData.value.hierarchy === 1) {
        form = useAddFloor();
      } else if (dialogData.value.hierarchy === 2) {
        if (type.value === 'edit') {
          form = useAddFloor();
        } else {
          form = useHouse();
        }
      } else if (dialogData.value.hierarchy === 3 || dialogData.value.type === 'edit' || dialogData.value.type === 'copy') {
        if (dialogData.value.operateType === 'edit') {
          form = useHouse();
        } else {
          form = useAddUserForm();
        }
      }
      // console.log(form);
      form
        .validate()
        .then(async () => {
          // console.log(form.modelRef, '----form.modelRef');
          // console.log(dialogData.value, '-----dialogData.value.type');
          if (dialogData.value.hierarchy === 1) {
            // 新增一级分类
            form.modelRef.parkOrgId = dialogData.value.parkOrgId;
            await equipmentLedger.createmultistage(form.modelRef);
          } else if (dialogData.value.hierarchy === 2) {
            if (dialogData.value.operateType === 'edit') {
              form.modelRef.id = dialogData.value.curentId;
              await equipmentLedger.modifymultistage(form.modelRef); // 编辑二级分类
            } else {
              form.modelRef.parentId = dialogData.value.curentId;
              await equipmentLedger.createmultistage(form.modelRef); // 新增二级分类
            }
          } else if (type.value === 'edit') {
            if (dialogData.value.operateType === 'edit') {
              form.modelRef.id = dialogData.value.curentId;
              await equipmentLedger.modifymultistage(form.modelRef); // 编辑二级分类
            } else {
              await equipmentLedger.modifyEquipmentLedger(form.modelRef); // 编辑设备
            }
          } else if (type.value === 'copy') {
            form.modelRef.id = '';
            await equipmentLedger.createEquipmentLedger(form.modelRef); // 复制设备
          } else {
            await equipmentLedger.createEquipmentLedger(form.modelRef); // 新增设备
          }
          message.success('操作成功');
          currentVisible.value = false;
          emit('operate:success', {
            label: form.modelRef.name as string,
            type: (dialogData.value.hierarchy + 1) as number,
            value: '',
          });
        })
        .catch((error: any) => {
          // console.log(error, '---error');
          // message.warning('请完成表单必填项');
        });
    };
    onMounted(() => {
    });
    return {
      currentVisible,
      confirm,
      dialogCp,
      title,
      emit,
    };
  },
});
</script>
<style lang="scss" scoped>
:deep(.ant-select-tree-treenode-disabled > .ant-select-tree-node-content-wrapper span) {
    color: rgba(0, 0, 0, 0.65);
    cursor: context-menu;
}

:deep(.ant-select-tree-treenode-disabled > .ant-select-tree-node-content-wrapper) {
    cursor: context-menu;
}
</style>
