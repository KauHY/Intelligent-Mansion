/*
 * @Author: Xucaizhi3
 * @Date: 2022-04-07 16:56:32
 * @LastEditTime: 2022-04-20 15:22:07
 * @LastEditors: Xucaizhi3
 * @Description:
 */
type PartyStr = 'partyA' | 'partyB'

interface PartyConfig {
  partyStr: PartyStr
  title: string
  parkOrgId: string
}
interface HouseConfig {
  parkId?: string
  title?: string
  checkedKeys?: string[]
  unavailableHouseIds?: string[]
  houseStatusSet?: number[]
}
interface BillSchemeConfig {
  title?: string
  parkOrgId?: string
  type?: string // 合同类型
}

export {
  PartyStr,
  PartyConfig,
  HouseConfig,
  BillSchemeConfig,
};
