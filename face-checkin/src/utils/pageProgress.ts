/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-04 17:09:19
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-04 17:21:25
 */
import 'nprogress/nprogress.css';
import NProgress from 'nprogress';
import { CreateAppFunction } from 'vue';

export default (app:ReturnType<CreateAppFunction<Element>>) => {
  NProgress.configure({
    easing: 'ease', // 动画方式
    speed: 500, // 递增进度条的速度
    showSpinner: false, // 是否显示加载ico
    trickleSpeed: 200, // 自动递增间隔
    minimum: 0.3, // 初始化时的最小百分比
  });
  app.config.globalProperties.progress = NProgress;
};
