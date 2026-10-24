/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-07 11:57:03
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-07 12:38:25
 */
declare module '*.vue' {
    import { DefineComponent } from 'vue';

    const component: DefineComponent<{}, {}, any>;
    export default component;
  }

declare module 'nprogress' {
    type Nprogress = {
        configure:(opitions:any)=>void
        start:()=>void
        done:()=>void
    }
    const nprogress:Nprogress;
    export default nprogress;
}
