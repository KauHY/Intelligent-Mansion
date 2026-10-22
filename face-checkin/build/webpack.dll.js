/*
 * @Description: 
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-06 16:58:52
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-23 20:45:04
 */
const path = require('path');
const webpack = require('webpack');
const {CleanWebpackPlugin} = require('clean-webpack-plugin')
// const AntdDayjsWebpackPlugin = require('antd-dayjs-webpack-plugin');
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer')
config = {
  mode: 'production',
  entry: {
    "common":['js-sha256', 'dayjs'],
    'vendor': ['vue','vue-router','pinia','axios',]
  },
  output: {
    filename: '[name].dll.js',
    path: path.resolve(__dirname, '../dll'),
    library: '[name]'
  },
  plugins: [
    new CleanWebpackPlugin(),
    new webpack.DllPlugin({
      name: '[name]',
      path: path.resolve(__dirname, '../dll/[name].manifest.json')
    }),
    // new AntdDayjsWebpackPlugin({
    //   preset: 'antdv3'
    // })
  ]
}

if (process.env.npm_config_report) config.plugins.push(new BundleAnalyzerPlugin())

module.exports = config