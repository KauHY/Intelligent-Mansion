/*
 *                        .::::.
 *                      .::::::::.
 *                     :::::::::::
 *                  ..:::::::::::'
 *               '::::::::::::'
 *                 .::::::::::
 *            '::::::::::::::..
 *                 ..::::::::::::.
 *               ``::::::::::::::::
 *                ::::``:::::::::'        .:::.
 *               ::::'   ':::::'       .::::::::.
 *             .::::'      ::::     .:::::::'::::.
 *            .:::'       :::::  .:::::::::' ':::::.
 *           .::'        :::::.:::::::::'      ':::::.
 *          .::'         ::::::::::::::'         ``::::.
 *      ...:::           ::::::::::::'              ``::.
 *     ````':.          ':::::::::'                  ::::..
 *                        '.:::::'                    ':'````..
 *
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-08-30 12:24:25
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 14:07:17
 */
import { createApp } from 'vue';
import Antd from 'ant-design-vue';
import './utils/localStorage';

import { piniaInstance } from '@/store';
import router from '@/routers';
import globalCp from '@/components/global-components';
import pageProgress from '@/utils/pageProgress';
import { directives } from '@/directive'; // 全局自定义指令
import globalProperties from '@/utils/globalApi';
import App from './App.vue';
// import 'assets/css/antd/index.less';
// import '@surely-vue/table/dist/index.less';
import 'ant-design-vue/dist/antd.variable.min.css';
import 'assets/css/common.scss';
// import 'dayjs/locale/zh-cn';
import 'assets/css/antd-custom/index.scss'; // Ant Design Vue 组件样式微调

const app = createApp(App);

app.use(Antd);
app.use(piniaInstance);
app.use(router);
app.use(globalCp);
app.use(pageProgress);
app.use(directives);
app.use(globalProperties);
app.mount('#app');
