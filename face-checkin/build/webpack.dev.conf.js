/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-14 14:38:39
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-05-05 14:22:28
 */
const { merge } = require('webpack-merge');
const baseConfig = require('./webpack.base.conf');
const { resolve } = require('./utils');
const isMock = process.env.MOCK
const { addOnProxyRes, use } = require('@gc082/mock')
const proxyTable = require('@gc082/dog-cli/config/proxyTable.json')
const { assemblyProxyTable } = require('../config/utils')
const webpack = require('webpack')
const compileConf = require('../config')
module.exports = merge(baseConfig, {
    mode: 'development',
    target: 'web', // 不加会热更新失效
    devtool: 'eval-cheap-module-source-map',
    // devtool: 'eval-source-map',
    // devtool: 'source-map',
    stats: 'errors-warnings',
    output: {
        filename: '[name].js',
        path: resolve(`../${compileConf.projectName}`),
        publicPath: compileConf.publicPath
    },
    optimization: {
        runtimeChunk: true,
        removeAvailableModules: false,
        removeEmptyChunks: false,
        splitChunks: false,
    },
    devServer: {
        // contentBase: resolve('../dist'),
        hot: true,
        hotOnly: true,
        // compress: true,
        host: '0.0.0.0',
        port: 9000,
        before: function (app, server, compiler) {
            if (isMock) {
                use('/smartPark', app)
                use('/park-system', app)
                use('/smartcommunitySit', app)
            }
        },
        // proxy: [
        //     {
        //         context: [
        //             '/smartcommunity',
        //             '/smartcommunitySit'
        //         ],
        //         // ...addOnProxyRes({
        //         target: 'http://10.200.3.145',
        //         changeOrigin: true,// 如果接口跨域，需要进行这个参数配置
        //         // pathRewrite: {
        //         //    '^/api': '/api' 
        //         // },
        //         // })
        //     },
        //     {
        //         context: [
        //           '/park-system',
        //         ],
        //         ...addOnProxyRes({
        //             // target: 'http://10.54.8.20:8082',
        //             target: 'http://10.54.19.12:8082',
        //             logLevel:'debug',
        //             changeOrigin: true,// 如果接口跨域，需要进行这个参数配置
        //         })
        //     }
        // ]
        proxy: assemblyProxyTable(proxyTable, addOnProxyRes)

    },
    plugins: [
        new webpack.DefinePlugin({
            'ENV_MODE': JSON.stringify(process.env.ENV_MODE),
            '__VUE_OPTIONS_API__': true,
            '__VUE_PROD_DEVTOOLS__':true
        }),
    ],
});
