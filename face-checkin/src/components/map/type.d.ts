/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-08-05 18:11:17
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-08-05 18:21:17
 */
interface AddressInfo {
    pname:string // 省
    cityname:string // 市
    adname:string // 区
    address:string // 具体地址
    name:string // 名称
    location:AMap.LngLat
}

export {
  AddressInfo,
};
