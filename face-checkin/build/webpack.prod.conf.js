/*
 * @Description: 
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-14 14:38:13
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-28 10:30:43
 */
const { merge } = require('webpack-merge')
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const baseConfig = require('./webpack.base.conf')
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer')
const CssMinimizerPlugin = require('css-minimizer-webpack-plugin');
// const UglifyJsPlugin = require('uglifyjs-webpack-plugin'); // 不支持es6
const TerserPlugin = require("terser-webpack-plugin");

const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const compileConf = require('../config')
const { getDllPlugins, resolve } = require('./utils')
const config = merge(baseConfig, {
    mode: 'production',
    // 生产不产出 .map：此前 743 个 sourcemap 占 54MB，且线上无需断点映射；排查线上问题时再临时打开
    devtool: false,
    stats: 'errors-only',
    output: {
        filename: 'js/[name].[contenthash].js',
        path: resolve(`../${compileConf.projectName}`),
        publicPath: compileConf.publicPath
    },
    optimization: {
        emitOnErrors: true,
        minimizer: [
            new CssMinimizerPlugin(),
            new TerserPlugin({
                terserOptions:{
                    compress:{
                        drop_console: process.env.ENV_MODE==='prod',
                        drop_debugger: true,
                        pure_funcs: process.env.ENV_MODE==='prod'?['console.log']:[],
                    },
                    keep_fnames: true,
                    warnings: true,
                },
                parallel: true,
            }),
        ],
        splitChunks:{
            minSize: 20000,
            maxSize: 500*1024,
            automaticNameDelimiter:"~",
            chunks: "all", 
        }
    },
    plugins: [
        new CleanWebpackPlugin(),
        new MiniCssExtractPlugin({
            filename: 'css/[name].[contenthash].css',
            chunkFilename: 'css/[id].[contenthash].css',
          }),
        ...getDllPlugins(),
        // new webpack.ContextReplacementPlugin(/moment[\/\\]locale$/, /ru/) 
    ]
})


if (process.env.npm_config_report) config.plugins.push(new BundleAnalyzerPlugin())

module.exports = config