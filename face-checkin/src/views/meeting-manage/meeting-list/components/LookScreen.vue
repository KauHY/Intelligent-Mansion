<!--
 * @Author: Xucaizhi3
 * @Date: 2022-01-11 15:33:34
 * @LastEditTime: 2022-04-06 15:40:06
 * @LastEditors: Xucaizhi3
 * @Description:
-->
<template>
  <v-modal
    class="meeting-add-edit"
    :visible="true"
    title="信息屏当前屏幕截图"
    :footer="null"
    :width="830"
    @ok="emit('update:show', false)"
    @cancel="emit('update:show', false)"
  >
    <v-image
      class="img-css"
      :src="meetData"
      width="100%"
      height="70vh"
    />
  </v-modal>
</template>

<script lang="ts">
import {
  ref, defineComponent, toRefs,
} from 'vue';
import { Meeting } from '~models/meeting-manage';

export default defineComponent({
  components: { },
  props: {
    meetingConfig: {
      type: Object,
      default: () => ({
        title: '',
        type: '',
        meetData: null,
      }),
    },
  },
  emits: {
    'update:show': (show: boolean) => true,
    refresh: (data: object) => true,
  },
  setup(props, { emit }) {
    console.log(props.meetingConfig, 'props.meetingConfig55');
    const { meetData, type } = toRefs(props.meetingConfig);
    const init = async () => {

    };
    init();
    return {
      emit,
      meetData,
    };
  },
});

function useOptions(meetData:Meeting, type: string, modelRef:any) {
  return {

  };
}
</script>
<style lang="scss" scoped>
.img-css {
    display: inline-block;
    width: 100%;
    height: 100%;
}
</style>
