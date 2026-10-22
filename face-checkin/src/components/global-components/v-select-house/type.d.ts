/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-02-14 17:47:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-01 15:02:39
 */
interface SelectCondition { // 选中的树节点值
    type:number|null, //
    ids:string[] // 选中的节点id
    parkOrgId:string
}

interface CheckedEmployees {
  [key:string]:SelectOptions[0]
}

// type a = SelectOptionsMap

export {
  SelectCondition,
  CheckedEmployees,
};
