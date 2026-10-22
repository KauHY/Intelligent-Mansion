<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-08 10:44:29
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-12 18:24:58
-->

<template>
  <v-modal
    v-model:visible="currentVisible"
    :title="title"
    :width="820"
    class="operate-dialog"
    @ok="confirm"
  >
    <!-- {{ dialogData }} -->
    <component
      :is="dialogCp"
      :type="modelType"
      :dialog-data="dialogData"
    />
  </v-modal>
</template>
<script lang='ts'>
import {
  defineComponent, PropType, computed, onMounted, toRefs, watch, markRaw, shallowRef,
} from 'vue';
import { message } from 'ant-design-vue';
import Building, { FloorInfo, FloorListItem } from '~models/building';
import House from '~models/house';
import {
  elevatorNames, houseDialogTitleNames, floorDialogTitleNames,
} from '../config';
import { useAddFloor } from '../composition/useAddFloor';
import { useHouse } from '../composition/useHouse';
import floorInfo from './floor-info.vue';
import houseInfo from './house-info.vue';

const floorInstance = new Building();
const houseInstance = new House();
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
      type: Object as PropType<{value:string, label:string}>,
      default: () => ({}),
    },
    // title: {
    //   type: String,
    //   default: '',
    // },
    type: {
      type: String as PropType<'add'|'edit'|'detailBuilding'>,
      default: '',
    },
  },
  // emits: ['update:visible', 'operate:success'],
  emits: {
    'update:visible': (paylod:boolean) => true,
    'operate:success': (paylod:SelectOptions[0]) => true,
  },
  setup(props, { emit }) {
    const currentVisible = computed({
      set(show:boolean) {
        emit('update:visible', show);
      },
      get() {
        return props.visible;
      },
    });
    const { type, dialogData } = toRefs(props);
    console.log(type, 'dialogData');
    // 是否是操作楼栋
    const isOperateFloor = computed(() => dialogData.value.type === 1 || (type.value === 'edit' && dialogData.value.type === 2) || (type.value === 'detailBuilding'));

    const dialogCp = computed(() => (isOperateFloor.value ? floorInfo : houseInfo));
    const modelType = type;
    const title = computed(() => { // 弹框标题
      const dialogNames = isOperateFloor.value ? floorDialogTitleNames : houseDialogTitleNames;
      // debugger;
      return dialogNames[type.value];
    });

    /**
     * @description: 确定操作
     * @param {*}
     * @return {*}
     * @author: GUOCHAO82
     */
    const confirm = () => {
      const form:any = isOperateFloor.value ? useAddFloor() : useHouse();
      form.validate().then(async () => {
        // console.log(form.modelRef, '----form.modelRef');
        // if (type === 'add') {
        // debugger;
        // console.log(dialogData.value.type, '-----dialogData.value.type');
        if (isOperateFloor.value) { // 新增楼栋
          await floorInstance.add(form.modelRef);
        } else {
          await houseInstance.add(form.modelRef); // 新增房屋
        }
        message.success('操作成功');
        currentVisible.value = false;
        emit('operate:success', {
          label: form.modelRef.name as string,
          type: dialogData.value.type + 1 as number,
          value: '',
        });
      }).catch((error:any) => {
        console.log(error, '---error');
        message.warning('操作失败');
      });
    };
    onMounted(() => {
    //   fill();
      // init();
    });
    return {
      currentVisible,
      confirm,
      modelType,
      dialogCp,
      title,
    //   orgTypeNames,
    };
  },
});
</script>
