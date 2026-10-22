<!--
 * @Description: 选择组件类型
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-03-22 15:08:50
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-03-23 12:01:01
-->
<template>
  <div class="choose-components">
    <div class="title">
      新增组件
    </div>
    <div class="component-list">
      <div
        v-for="component in componentList"
        :key="component.tag"
        class="component-item"
        @click="addComponent(component.tag)"
      >
        <div class="name">
          {{ component.name }}
        </div>
        <img
          :src="component.icon"
          alt=""
        >
      </div>
    </div>
  </div>
</template>
<script lang='ts'>
import {
  reactive, ref, defineComponent, PropType,
} from 'vue';
import { componentList } from '../config';
import { ElementType } from '../type';

export default defineComponent({
//   props: {
//   },
  emits: {
    addComponent: (tag:ElementType) => true,
  },
  setup(props, { emit }) {
    const addComponent = (tag:ElementType) => {
      emit('addComponent', tag);
    };
    return {
      componentList,
      addComponent,
    };
  },
});
</script>
<style lang='scss' scoped>
.choose-components {
    padding: 20px;

    .component-list {
        padding: 16px 16px 0 16px;

        .component-item {
            display: flex;
            justify-content: space-between;
            padding: 16px;
            cursor: pointer;
            border-bottom: 1px dashed #e6e4e3;

            >img {
                width: 16px;
                height: 16px;
            }

            &:hover {
                color: #1175e2;
            }
        }
    }
}
</style>
