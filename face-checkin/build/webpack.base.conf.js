/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-14 14:37:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-26 19:46:06
 */

const webpack = require('webpack')
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { VueLoaderPlugin } = require('vue-loader');
const ProgressBarPlugin = require('progress-bar-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const StylelintPlugin = require('stylelint-webpack-plugin');
const chalk = require('chalk');
const ESLintPlugin = require('eslint-webpack-plugin');
// const webpack = require('webpack')
const path = require('path')
const devMode = process.env.ENV_MODE === 'dev';
const { resolve } = require('./utils');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
// const AntdDayjsWebpackPlugin = require('antd-dayjs-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const compileConf = require('../config')
// console.log(compileConf,'----compileConf----')

module.exports = {
  entry: {
    app: './src/main.ts',
  },
  performance: {
    hints: false,
  },
  resolve: {
    alias: {
      '@': resolve('../src'),
      'assets': resolve('../src/assets'),
      '~utils': resolve('../src/utils'),
      '~views': resolve('../src/views'),
      '~services': resolve('../src/services'),
      '~components': resolve('../src/components'),
      '~composition': resolve('../src/composition'),
      '~models': resolve('../src/models'),
    },
    extensions: ['.ts', '.js', '.tsx', '.vue', '.json','.d.ts'],
  },
  module: {
    rules: [
      {
        test: /\.(js|ts|tsx|jsx)$/,
        include: resolve('../src'),
        use: [
          'thread-loader',
          // {
          //   loader: 'esbuild-loader',
          //   options: {
          //     loader: 'tsx',
          //   },
          // },
          {
            loader: 'babel-loader',
            options: {
              cacheDirectory: true,
            },
          },
        ],
      },
      {
        test: /\.vue$/,
        // loader: 'vue-loader',
        use: ['thread-loader', 'vue-loader'],
        include: resolve('../src'),
      },
      {
       oneOf:[
          {
            test: /\.css$/,
            // include: resolve('../src'),
            use: [devMode?"style-loader":MiniCssExtractPlugin.loader, 'css-loader'],
          },
          {
            test: /\.(less)$/i,
            use: [
              // 'thread-loader',
              devMode? "style-loader":MiniCssExtractPlugin.loader,
              'css-loader',
              {
                loader: "less-loader",
                options: {
                  lessOptions:{
                    javascriptEnabled: true
                  }
                }
              },
              'postcss-loader',
            ],
          },
          {
            test: /\.(scss)$/i,
            use: [
              // 'thread-loader',
              devMode? "style-loader":MiniCssExtractPlugin.loader,
              'css-loader',
              'sass-loader',
              {
                loader: 'sass-resources-loader',
                options: {
                    resources: [
                        path.resolve(__dirname, '../src/assets/css/variable.scss'),
                    ]
                }
            },
              'postcss-loader',
            ],
          },
          {
            test: /\.(png|jpg|gif|svg)$/,
            type: 'asset',
            parser: {
              dataUrlCondition: {
                maxSize: 4 * 1024, // 4kb
              },
            },
            generator: {
              filename: 'imgs/[name].[contenthash:6][ext]',
            },
          },
          {
            test: /\.(woff|woff2|eot|ttf|otf)$/i,
            type: 'asset/resource',
            generator: {
              filename: 'fonts/[name].[contenthash:6][ext]',
            },
          },
          {
            test: /\.(mp4)$/i,
            type: 'asset/resource',
            generator: {
              filename: 'videos/[name].[contenthash:6][ext]',
            },
          },
       ],
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: resolve('../public/index.ejs'),
    }),
    new VueLoaderPlugin(),
    new CopyPlugin({
      patterns: [
        { from: "static", to: "static" },
      ],
    }),
    new ESLintPlugin({
      formatter: require('eslint-friendly-formatter'),
      extensions: ['js', 'ts', 'vue', 'tsx'],
      fix: true,
    }),
    new ProgressBarPlugin({
      format: `:msg ${chalk.yellow('[:bar]')} ${chalk.green.bold(':percent')} (:elapsed seconds)`,
      width: 250,
      clear: false,
    }),
    new StylelintPlugin({
      fix: true,
      files: ['src/**/*.vue', 'src/assets/css/*.(css|scss|less)']
    }),
    new ForkTsCheckerWebpackPlugin({
      // vue: true,
      async: false, //不允许异步检测，会影响打包效率
      typescript:{
        extensions:{
          vue:{
            enabled:true,
            compiler:'@vue/compiler-sfc' // 需指定vue单文件目标解析器
          },
          
        }
      }
    }),
    new webpack.DefinePlugin({
      compileConf:JSON.stringify(compileConf),
    }),
    // new AntdDayjsWebpackPlugin({
    //   preset: 'antdv3'
    // })
  ],
  cache: {
    // 1. 将缓存类型设置为文件系统
    type: 'filesystem',
    buildDependencies: {
      config: [__filename],
    },
  },
};
