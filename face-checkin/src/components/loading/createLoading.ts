/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-03 16:48:01
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-12-07 17:43:54
 */
import { Spin } from 'ant-design-vue';
import {
  render, reactive, toRefs, h, createVNode, withDirectives, vShow,
} from 'vue';
import { Options, GlobalOptions } from './loading.type';
import './loading.scss';
import svg from './svg.vue';

export const createdLoading = ({ options, globalOptions }:{options:Options, globalOptions:GlobalOptions}) => {
  const data = reactive({
    ...options,
    visible: false,
  });
  const close = () => {
    globalOptions.num -= 1;
    if (globalOptions.num === 0 || globalOptions.num < 0) {
      data.visible = false;
      globalOptions.num = 0;
    }
  };
  const show = () => {
    globalOptions.num += 1;
    data.visible = true;
  };
  const componentSetupConfig = {
    ...toRefs(data),
    close,
    show,
  };
  const LoadingComponent = {
    setup() {
      return {
        ...componentSetupConfig,
      };
    },
    render() {
      const test = withDirectives(h(
        'div',
        {
          class: 'v-loading',
        },
        [
          h(Spin, {
            spinning: true,
            tip: options.tip,
            size: 'large',
            class: 'loadingIcon',
            indicator: h(svg),
          }),
        ],
      ),
      [[vShow, data.visible]]);
      return test;
    },
  };
  const vm = createVNode(LoadingComponent);

  render(vm, document.createElement('div'));
  return {
    ...componentSetupConfig,
    vm,
    get $el() {
      return vm.el as HTMLElement;
    },
  };
};
