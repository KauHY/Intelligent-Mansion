/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-08-04 18:04:28
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-08-05 16:37:06
 */
interface loadOptions {
    container:string,
    mapOptions: AMap.MapOptions,
    plugins?:string[],
    AMapUIPlugins?:string[]
}
// poi选点配置项目
interface PoiPickerOptions {
    input:string|HTMLElement
    city?:string
    searchResultsContainer?:string|HTMLElement
    suggestContainer?:string|HTMLElement
}

// poi选中搜索结果返回值
interface PoiPickedEventOptions {
    source: 'search'|'suggest', // 信息来源，search代表搜索服务，suggest代表输入提示
    item: {
        id: string, // poi点的id
        name: string|string[] // 名称
        location: AMap.LngLat, // 经纬度，AMap.LngLat实例,
        address: string // 地址信息
        district:string
    }
}

/**
 * @description: AMap marker配置
 *
 *  “AMAP_ANIMATION_NONE”，无动画效果
 *   “AMAP_ANIMATION_DROP”，点标掉落效果
 *   “AMAP_ANIMATION_BOUNCE”，点标弹跳效果
 */
interface MarkerOptions extends AMap.MarkerOptions{
  animation: 'AMAP_ANIMATION_NONE'|'AMAP_ANIMATION_DROP'|'AMAP_ANIMATION_BOUNCE'
}

export {
  loadOptions,
  PoiPickerOptions,
  PoiPickedEventOptions,
  MarkerOptions,
};
