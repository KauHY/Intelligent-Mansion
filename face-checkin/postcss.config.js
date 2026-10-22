/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-17 14:08:51
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-24 09:57:52
 */
module.exports = {
  plugins: {
    'postcss-preset-env': {},
    'postcss-px-to-viewport': {
      unitToConvert: 'px',
      viewportWidth: 1920,
      unitPrecision: 5,
      propList: ['*', '!min-*', '!max-*'],
      viewportUnit: 'vw',
      fontViewportUnit: 'vw',
      selectorBlackList: [/primeval/, /demo/],
      minPixelValue: 1,
      mediaQuery: false,
      replace: true,
      exclude: undefined,
      include: undefined,
      landscape: false,
      landscapeUnit: 'vw',
      landscapeWidth: 1920,
    },
  },
};
