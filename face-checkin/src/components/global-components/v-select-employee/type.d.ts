/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-02-14 17:47:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-07-05 17:43:26
 */
interface SelectCondition { // 选中的树节点值
    // type:number|null, //
    // ids:string[] // 选中的节点id
    parkOrgId:string
    companyIds:string[],
    tagIds:string[],
}

interface CheckedEmployees {
  [key:string]:SelectOptions[0]
}

// type a = SelectOptionsMap

export {
  SelectCondition,
  CheckedEmployees,
};
