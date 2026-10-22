/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-14 17:59:00
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-01-26 14:38:18
 */
module.exports = {
  presets: [
    '@babel/preset-env',
    [
      '@babel/preset-typescript', {
        allExtensions: true, // 不加识别不了script lang=ts
      },
    ],
  ],
  plugins: [
    [
      '@babel/plugin-transform-runtime',
      {
        corejs: 2, // 指定 runtime-corejs 的版本，目前有 2 3 两个版本
      },
    ],
    ['@vue/babel-plugin-jsx', {
      transformOn: true,
    }],
    // '@babel/plugin-proposal-class-properties',
    // '@babel/plugin-proposal-object-rest-spread',
    // [
    //     "import",
    //     {
    //         libraryName: 'element-plus',
    //         customStyleName: (name) => {
    //             name = name.slice(3)
    //             return `element-plus/packages/theme-chalk/src/${name}.scss`;
    //         },
    //     },
    // ],
  ],
};
//   }
