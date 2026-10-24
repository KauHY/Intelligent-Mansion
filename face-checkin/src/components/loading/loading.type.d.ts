/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-11-03 16:12:12
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2021-11-03 18:09:37
 */
export interface ILoadingParentElement extends HTMLElement {
    vLoadingAddClassList?: () => void
}

export interface Options {
    visible?:boolean
    target?: string | HTMLElement
    fullscreen?:boolean
    tip?:string
}

export interface GlobalOptions {
    num:number
}
