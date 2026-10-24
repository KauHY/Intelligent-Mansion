<!--
 * @Description:关联巡检点
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-12-01 13:39:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-28 21:10:13
-->
<template>
  <div
    class="v-select-employee"
  >
    <div class="tags-box">
      <div

        class="tags"
      >
        <div v-if="Object.keys(CheckedHouseNames).length">
          <a-tag
            v-for="(item,key) in CheckedHouseNames"
            :key="key"
            class="tag"
          >
            {{ item }}
          </a-tag>
        </div>
        <span
          v-else
          class="placeholder"
        >{{ disabled?'-':placeholder }}</span>
      </div>
      <a-button
        :disabled="disabled"
        @click="showModal"
      >
        点击选择
      </a-button>
    </div>
    <select-box
      v-if="houseFlag"
      ref="house"
      v-model:show="houseFlag"
      :house-config="houseConfig"
      @getHouseData="getHouseData"
    />
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType, toRefs, onMounted, watch,
} from 'vue';
import { boolean } from 'mathjs';
import { CheckedEmployees } from './type';
import selectBox from './components/select-box.vue';

interface HouseConfig {
  parkId?: string
  title?: string
  checkedKeys?: any
}
export default defineComponent({
  name: 'VSelectHouse',
  components: { selectBox },
  props: {
    value: {
      type: [Array] as PropType<string[]|string>,
      default: () => ([]),
    },
    parkOrgIds: { // 园区id
      type: String,
      default: () => (''),
    },
    defaultSelectOpts: {
      type: Array as PropType<SelectOptions>,
      default: () => ([]),
    },
    placeholder: {
      type: String,
      default: '请选择',
    },
    mode: {
      type: String as PropType<'single'|'multiple'>,
      default: 'multiple',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  emits: {
    'update:value': (checkedEmployeeIds:string[]|string) => true,
    change: (checkedEmployeeIds:string[]|string) => true,
  },
  setup(props, { emit }) {
    const visible = ref(false);
    const houseConfig = ref<HouseConfig>({});
    const houseFlag = ref<boolean>(false);
    const houseList = ref<any>([]);
    const CheckedHouseNames = ref<any>([]);
    const {
      defaultSelectOpts, mode, parkOrgIds, value, disabled,
    } = toRefs(props);
    const checkedEmployees = ref<CheckedEmployees>({});
    const tempCheckedEmployees = ref<CheckedEmployees>({});
    watch(defaultSelectOpts, (values) => { // 初始值发生变动时重新计算
      CheckedHouseNames.value = values;
    }, { deep: true, immediate: true });
    const showModal = async () => {
      houseConfig.value = {
        title: '关联房屋',
        parkId: parkOrgIds.value,
        checkedKeys: value,
      };
      houseFlag.value = true;
    };
    const getHouseData = (data: any) => {
      const houseNamesArr = ref<any>([]);
      const idsArr = ref<any>([]);
      if (value.value.length > 0 && !data.value.length) {
        houseNamesArr.value = defaultSelectOpts.value;
        idsArr.value = value.value;
      } else {
        data.value.forEach((element: any) => {
          houseNamesArr.value.push(`${element.buildingName}-${element.houseName}`);
          idsArr.value.push(element.houseId);
        });
      }
      CheckedHouseNames.value = houseNamesArr.value;
      emit('update:value', idsArr.value);
    };
    return {
      CheckedHouseNames,
      getHouseData,
      visible,
      checkedEmployees,
      tempCheckedEmployees,
      showModal,
      houseFlag,
      houseConfig,
      houseList,
    };
  },
});

</script>
<style lang="scss" scoped>
.v-select-employee {
    .tags-box {
        display: flex;
        align-items: center;
        justify-content: flex-start;

        .tags {
            flex: 1;
            height: 36px;
            padding: 4px 10px;
            overflow: auto;
            background-color: #fff;
            border: 1px solid #d9d9d9;
            border-right: 0;
            border-radius: 4px 0 0 4px;

            .tag {
                margin-bottom: 4px;
            }

            .placeholder {
                color: #d9d9d9;
            }
        }

        button {
            width: 80px;
            height: 36px;
            border-radius: 0 4px 4px 0;
        }
    }
}
</style>
