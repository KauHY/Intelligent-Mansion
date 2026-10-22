<!--
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-22 10:45:05
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-10 16:54:31
-->
<template>
  <div class="house-card-list">
    <div
      v-for="item in houseList"
      :key="item.houseId"
      class="house-card-item"
      style="width: 300px;"
    >
      <!-- <template #title> -->
      <div class="content">
        <v-svg
          name="home-fill"
        />
        <p>
          <span>{{ item.name }}</span><br>
          <span>{{ item.buildArea || '/' }} ㎡</span>
        </p>
      </div>
      <v-svg
        v-if="!disabled"
        class="close"
        name="close"
        @click="del(item)"
      />
    </div>
  </div>
</template>
<script lang='ts'>
import { defineComponent, PropType } from 'vue';
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
    align-items: center;
    justify-content: flex-start;

    .house-card-item {
        position: relative;
        flex: 0 0 250px;
        padding: 10px;
        margin-left: 10px;
        color: var(--ant-primary-color);
        border: 1px solid #f0f0f0;
        border-radius: 4px;

        .content {
            display: flex;
            align-items: center;
            justify-content: flex-start;
        }

        .close {
            position: absolute;
            top: 4px;
            right: 4px;
            width: 25px;
            height: 25px;
            cursor: pointer;
        }
    }
}
</style>
