/*
 * @Description: 
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-05-18 17:15:03
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-05-18 18:57:42
 */
function test(req,res,next){
    // // console.log(req,res,'发起请求')
    next()
    // console.log(req,res,'返回响应')
}

module.exports = {
    test
}
