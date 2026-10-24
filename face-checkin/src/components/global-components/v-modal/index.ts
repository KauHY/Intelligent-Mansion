/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-12 14:21:52
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-27 14:06:36
 */
import { Modal } from 'ant-design-vue';
import { defineComponent, h } from 'vue';

export default defineComponent({
  name: 'VModal',
  props: {
    maxHeight: {
      type: String,
      default: '70vh',
    },
    minHeight: {
      type: String,
      default: '20vh',
    },
  },
  setup(props, { attrs, slots }) {
    const defaultProps = {
      keyboard: false,
      maskClosable: false,
    };

    return () => {
      const { maxHeight, minHeight } = props;
      return h(Modal, {
        bodyStyle: { maxHeight, overflow: 'auto', minHeight },
        ...defaultProps,
        ...attrs,
      }, slots);
    };
  },
});
