<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-23 15:10:36
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-02 14:38:28
-->
<template>
  <a-form
    :label-col="{span:4}"
  >
    <a-form-item label="关联园区">
      <span>{{ dialogData.parkOrgName }}</span>
    </a-form-item>
    <a-form-item
      label="楼栋单元名称"
      name="name"
      v-bind="validateInfos.name"
    >
      <a-input
        v-model:value="modelRef.name"
        :disabled="formDisabled"
        :maxlength="30"
        placeholder="请输入楼栋单元名称"
      />
    </a-form-item>
    <a-form-item label="楼栋单元编码">
      <a-input
        v-model:value="modelRef.code"
        :disabled="formDisabled"
        :maxlength="30"
        placeholder="请输入楼栋单元编码"
      />
    </a-form-item>
    <a-form-item
      label="总楼层"
      v-bind="validateInfos.totalFloor"
    >
      <a-input
        v-model:value="modelRef.totalFloor"
        :disabled="formDisabled"
        type="number"
        :maxlength="30"
        placeholder="请输入总楼层"
      />
    </a-form-item>
    <a-form-item
      label="楼栋属性"
      v-bind="validateInfos.property"
    >
      <v-select
        v-model:value="modelRef.property"
        :disabled="formDisabled"
        placeholder="请选择楼栋属性"
      >
        <a-select-option
          v-for="opt in options.BUILDING_PROPERTY"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item label="建成时间">
      <a-date-picker
        v-model:value="modelRef.buildTime"
        :disabled="formDisabled"
        value-format="YYYY-MM-DD"
        placeholder="请选择建成时间"
      />
    </a-form-item>

    <a-form-item label="有无电梯">
      <!-- {{ modelRef.elevator }} -->
      <a-radio-group
        v-model:value="modelRef.elevator"
        :disabled="formDisabled"
        name="radioGroup"
      >
        <a-radio
          v-for="(name,key) in elevatorNames"
          :key="key"
          :value="Number(key)"
        >
          {{ name }}
        </a-radio>
      </a-radio-group>
    </a-form-item>
    <a-form-item
      label="总占地面积（㎡）"
      v-bind="validateInfos.coverArea"
    >
      <a-input
        v-model:value="modelRef.coverArea"
        :maxlength="30"
        :disabled="formDisabled"
        placeholder="请输入总占地面积"
      />
    </a-form-item>
    <a-form-item
      label="总建筑面积（㎡）"
      v-bind="validateInfos.buildArea"
    >
      <a-input
        v-model:value="modelRef.buildArea"
        :maxlength="30"
        :disabled="formDisabled"
        placeholder="请输入总建筑面积"
      />
    </a-form-item>
    <a-form-item
      label="备注"
    >
      <a-input
        v-model:value="modelRef.remark"
        :maxlength="200"
        :disabled="formDisabled"
        placeholder="请输入备注"
      />
    </a-form-item>
  </a-form>
</template>
<script lang="ts">
import {
  defineComponent, PropType, computed, onMounted, ref,
} from 'vue';
import Building, { FloorInfo, FloorListItem } from '~models/building';
import { useHashMapDics, DictionaryMap } from '~composition/useHashMapDic';
import { useAddFloor } from '../composition/useAddFloor';
import { elevatorNames } from '../config';

export default defineComponent({
  props: {
    dialogData: {
      type: Object,
      default: () => ({}),
    },
    type: {
      type: String,
      default: '',
    },
  },

  setup(props) {
    const formDisabled = ref<boolean>(false);
    const floorInstance = new Building();
    const {
      modelRef, validateInfos, validate, fill,
    } = useAddFloor(props.dialogData);
    const options = useHashMapDics<
    ['BUILDING_PROPERTY']
  >(['BUILDING_PROPERTY']);
    const init = async () => {
      const data = await floorInstance.findBuildingById(props.dialogData.buildingId);
      fill(modelRef, { ...props.dialogData, ...data });
      console.log(props.type, 'props.type');
      if (props.type === 'detailBuilding') {
        formDisabled.value = true;
      }
    };
    init();
    return {
      formDisabled,
      options,
      modelRef,
      validateInfos,
      elevatorNames,
    };
  },
});
</script>
