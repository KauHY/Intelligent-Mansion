/*
 * @Author: Xucaizhi3
 * @Date: 2022-02-21 11:29:23
 * @LastEditTime: 2022-02-22 16:18:34
 * @LastEditors: Xucaizhi3
 * @Description:
 */
interface SearchAdvertParams extends BaseSearchParams {
  status: string // 广告状态
  effectTimeEnd: string // 广告日期
  effectTimeStart: string // 广告日期
  parkOrgId: string // 广告状态
  name: string // 广告名称
}

interface AdvertListKind {
  name: string
  picture: string
  parkOrgNames: string[] // 推送范围
  parkOrgIds: string[] // 推送范围
  sort: number | null // 排序：数字越小越靠前
  position: number | null // 广告位置 ：1 首页轮播图
  jumpType: number | null // 跳转类型：0 无，1 URL，2 小程序AppID
  effectTimeEnd: string // 生效开始时间
  effectTimeStart: string // 生效开始时间
  createByName: string // 发布人
  createTime: string // 创建时间
  status: number | null // 状态： 0 生效中, 1 待生效，2 已过期
  id: string
  jumpContent: string // 跳转地址/需跳转的小程序AppID
}

export { SearchAdvertParams, AdvertListKind };
