/*
 * @Author: Xucaizhi3
 * @Date: 2022-03-04 13:45:03
 * @LastEditTime: 2022-04-13 16:47:21
 * @LastEditors: GUOCHAO82
 * @Description:
 */
interface ImportExcelConfig {
  title: string,
  postData?: any,
  isZip?:Boolean,
  onConfim: (...ary:any)=>void,
  categoryType?: null | number
}

interface ImportExcelKind {
  parkOrgId: string
  file: any
  companyId: string
  time?:string
  parkingLotIds?:Array<any>;
}
interface ImportResultKind {
  successTotal: null | number
  failTotal: null | number
  fileKey: any | string
}

export {
  ImportExcelConfig,
  ImportExcelKind,
  ImportResultKind,
};
