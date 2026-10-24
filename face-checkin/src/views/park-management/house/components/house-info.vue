<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-23 15:10:36
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-02 16:55:02
-->
<template>
  <a-form
    :label-col="{span:4}"
  >
    <a-form-item label="关联园区">
      <span>{{ dialogData.parkOrgName }}</span>
    </a-form-item>
    <a-form-item label="所属楼栋">
      <!-- {{ dialogData }} -->
      <span>{{ dialogData.floorName }}</span>
    </a-form-item>
    <a-form-item
      label="楼层房号"
      name="name"
      v-bind="validateInfos.name"
    >
      <a-input
        v-model:value="modelRef.name"
        :disabled="formDisabled"
        placeholder="请输入楼层房号"
      />
    </a-form-item>
    <a-form-item label="房屋编码">
      <a-input
        v-model:value="modelRef.code"
        :maxlength="30"
        :disabled="formDisabled"
        placeholder="请输入房屋编码"
      />
    </a-form-item>
    <a-form-item label="楼层属性">
      <v-select
        v-model:value="modelRef.floorType"
        :disabled="formDisabled"
        placeholder="请选择楼层属性"
      >
        <a-select-option
          v-for="opt in options?.FLOOR_TYPE"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item
      label="所在楼层"
      v-bind="validateInfos.floor"
    >
      <!-- <a-input
        v-model:value="modelRef.floor"
        type="number"
        :maxlength="30"
        placeholder="请输入总楼层"
      /> -->
      <v-select
        v-model:value="modelRef.floor"
        :disabled="formDisabled"
        placeholder="请选择所在楼层"
      >
        <a-select-option
          v-for="floor in totalFloor"
          :key="floor"
        >
          {{ floor }} 楼
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item
      label="建筑面积（㎡）"
      v-bind="validateInfos.buildArea"
    >
      <a-input
        v-model:value="modelRef.buildArea"
        :disabled="formDisabled"
        type="number"
        :maxlength="30"
        placeholder="请输入建筑面积"
      />
    </a-form-item>
    <a-form-item
      label="套内面积（㎡）"
      v-bind="validateInfos.coverArea"
    >
      <a-input
        v-model:value="modelRef.coverArea"
        :disabled="formDisabled"
        type="number"
        :maxlength="30"
        placeholder="请输入套内面积"
      />
    </a-form-item>
    <a-form-item
      label="房屋功能"
      v-bind="validateInfos.houseFunction"
    >
      <v-select
        v-model:value="modelRef.houseFunction"
        placeholder="请选择房屋功能"
        :disabled="formDisabled"
      >
        <a-select-option
          v-for="opt in options?.HOUSE_FUNCTION"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item label="房屋朝向">
      <v-select
        v-model:value="modelRef.orientation"
        placeholder="请选择房屋朝向"
        :disabled="formDisabled"
      >
        <a-select-option
          v-for="opt in options?.HOUSE_ORIENTATION"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item
      label="房屋属性"
      v-bind="validateInfos.houseProperty"
    >
      <v-select
        v-model:value="modelRef.houseProperty"
        :disabled="formDisabled"
        placeholder="请选择房屋属性"
      >
        <a-select-option
          v-for="opt in options?.HOUSE_PROPERTY"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item>
    <a-form-item
      label="备注"
    >
      <a-input
        v-model:value="modelRef.remark"
        :disabled="formDisabled"
        :maxlength="200"
        placeholder="请输入备注"
      />
    </a-form-item>
    <!-- <a-form-item
      label="房屋状态"
      v-bind="validateInfos.houseStatus"
    >
      <v-select
        v-model:value="modelRef.houseStatus"
        placeholder="请选择房屋状态"
      >
        <a-select-option
          v-for="opt in options?.HOUSE_STATUS"
          :key="opt.value"
        >
          {{ opt.label }}
        </a-select-option>
      </v-select>
    </a-form-item> -->
  </a-form>
</template>
<script lang="ts">
import {
  defineComponent, PropType, computed, onMounted, ref,
} from 'vue';
import Building from '@/models/building';
import { useHashMapDic, DictionaryMap } from '~composition/useHashMapDic';
import { elevatorNames } from '../config';
import { useHouse } from '../composition/useHouse';
// import Floor, { FloorInfo, FloorListItem } from '~models/floor';

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
    const options = ref<DictionaryMap<['FLOOR_TYPE', 'HOUSE_ORIENTATION', 'HOUSE_STATUS', 'HOUSE_FUNCTION', 'HOUSE_PROPERTY']>>();
    const {
      modelRef, validateInfos, validate, fill,
    } = useHouse(props.dialogData);
    const totalFloor = ref(0);
    const init = async () => {
      fill(modelRef, props.dialogData);
      totalFloor.value = await new Building().getBuildingTotalFloor(modelRef.buildingId);
      console.log(totalFloor, '---totalFloor');
      options.value = await useHashMapDic<['FLOOR_TYPE', 'HOUSE_ORIENTATION', 'HOUSE_STATUS', 'HOUSE_FUNCTION', 'HOUSE_PROPERTY']>(['FLOOR_TYPE', 'HOUSE_ORIENTATION', 'HOUSE_STATUS', 'HOUSE_FUNCTION', 'HOUSE_PROPERTY']);
      if (props.type === 'detailHouse') {
        formDisabled.value = true;
      }
    };
    init();
    return {
      formDisabled,
      modelRef,
      validateInfos,
      elevatorNames,
      options,
      totalFloor,
    };
  },
});
</script>
