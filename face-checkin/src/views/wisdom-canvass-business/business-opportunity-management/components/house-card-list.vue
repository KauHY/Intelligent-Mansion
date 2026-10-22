<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-22 10:45:05
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-10 16:53:42
-->
<template>
  <div class="house-card-list">
    <a-tag
      v-for="item in houseList"
      :key="item.houseId"
      color="orange"
      :closable="!disabled"
      @close="del(item)"
    >
      <template #icon>
        <v-svg
          class="svg-icon"
          name="home-fill"
        />
      </template>
      {{ item.name }} <span v-if="!!item.buildArea">【{{ item.buildArea }}㎡】</span>
    </a-tag>
    <a-tag
      v-if="!disabled"
      class="add"
    >
      <template #icon>
        <v-svg
          name="add-select"
          class="svg-icon"
        />
      </template>
      <slot />
    </a-tag>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';
// import { SelectHouseInfo } from '../type';
import { SelectHouseInfo } from '~models/wisdom-canvass-business';

export default defineComponent({
  components: {},
  props: {
    houseList: {
      type: Object as PropType<SelectHouseInfo>,
      default: () => ({}),
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  emits: {
    del: (id:string) => true,
  },
  setup(props, { emit }) {
    const del = (item:SelectHouseInfo[string]) => {
      emit('del', item.houseId as string);
    };
    return {
      del,
    };
  },
});
</script>
<style lang="scss" scoped>
.house-card-list {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    justify-content: flex-start;
    font-size: 14px;

    .ant-tag {
        display: flex;
        align-items: center;
        justify-content: flex-start;
        padding: 4px;
        margin-bottom: 10px;
    }

    .svg-icon {
        width: 25px;
        height: 25px;
        margin-right: 4px;
    }

    .add {
        cursor: pointer;
    }
}
</style>
