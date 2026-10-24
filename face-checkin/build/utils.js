/*
 * @Description: 
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-17 14:54:45
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-10-14 10:49:09
 */
const path = require('path')
const AddAssetHtmlWebpackPlugin  = require('add-asset-html-webpack-plugin')
const fs = require('fs')
const webpack  = require('webpack')
const compileConf = require('../config')
const resolve = (url) => {
    return path.resolve(__dirname, url)
}

const getDllPlugins = ()=>{
    const plugins = []
    const files = fs.readdirSync(path.resolve(__dirname, '../dll'));
    files.forEach(file => {
        if(/.*\.dll.js/.test(file)) {
          plugins.push(new AddAssetHtmlWebpackPlugin({
            filepath: path.resolve(__dirname, '../dll', file),
            outputPath:'/js',
            publicPath:`${compileConf.publicPath}js`
          }))
        }
        if(/.*\.manifest.json/.test(file)) {
          plugins.push(new webpack.DllReferencePlugin({
            manifest: path.resolve(__dirname, '../dll', file)
          }))
        }
    })
    return plugins
}

module.exports = {
    resolve,
    getDllPlugins
}