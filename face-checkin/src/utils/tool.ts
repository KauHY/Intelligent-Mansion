/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-06-03 20:39:54
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-17 18:17:12
 */
import { sha256 } from 'js-sha256';
import { ENCRYPT_SLAT, rootMenu } from '@/constants/config';
import { create, all } from 'mathjs';

export function getFileName(filePath:string) {
  const startIndex = filePath.lastIndexOf('/');
  const temp = filePath.substring(startIndex + 1);
  return temp.substring(0, temp.indexOf('.'));
}
/**
 * @description: sha256加密
 * @param {string} data
 * @return {*}
 * @author: GUOCHAO82
 */
export const encrypt = (data:string) => sha256(`${data}_${ENCRYPT_SLAT}`);

/**
 * @description: 扁平数据转换为数结构
 * @param {Array} target
 * @param {Array} meunList
 * @param {MenuItem} defaultMenuItem
 * @return {*}
 * @author: GUOCHAO82
 */

interface TreeItem { // 必去参数
  id: string
  parentId: string
  type: 0|1|2
  sort?:number,
  children?:Array<TreeItem>
}
export const getTree = <T extends TreeItem>(meunList:Array<T>, defaultTreeItem?:T, fitterType:string|string[] = 'all') => {
  const menuTree:Array<T> = [];
  const fitterTypeArray = (Array.isArray(fitterType) ? [String(fitterType)] : fitterType) as unknown as string[];
  const defaultItem = defaultTreeItem || rootMenu;
  menuTree.push(defaultItem as T);
  const find = (target:Array<T>, meunList:Array<T>) => {
    // debugger;
    target.forEach((targetItem) => {
      const { id } = targetItem;
      let children = meunList.filter((meunListItem) => {
        const { parentId, type } = meunListItem;
        return targetItem !== meunListItem && id === parentId && (fitterTypeArray.includes('all') || fitterType.includes(String(type)));
      });
      children = children.sort((item, nextItem) => (item.sort || 0) - (nextItem.sort || 0));
      // console.log(children, '-------children');
      if (children.length) {
        targetItem.children = children;
        find(children, meunList);
      }
    });
  };
  find(menuTree, meunList);
  return menuTree[0]?.children || [];
};

export const getFormCnfValue = (formConf:{[key:string]:FormFieldConf}) => {
  const formData:{[key:string]:any} = {};
  Object.entries(formConf).forEach(([key, fieldConf]) => {
    const { fieldKey, value } = fieldConf;
    if (!fieldKey) {
      formData[key] = value;
      return;
    }
    const fields = fieldKey.split('.');
    if (!formData[fields[0]]) {
      formData[fields[0]] = {};
    }
    let temp:any = formData[fields[0]];
    fields.splice(0, 1);

    while (fields.length > 1) {
      const field = fields[0];
      if (!temp[field]) {
        temp[field] = {};
      }
      temp = temp[field];
      fields.splice(0, 1);
    }
    temp[fields[0]] = value;
  });
  return formData;
};

export const getArrayParams = (key:string, ary:string[]) => {
  const tempAry = ary.map((item) => `${key}=${item}`);
  return tempAry.join('&');
};

export const getYearsToNow = (oldYear: number, newYear?:number) => {
  const yearsToNow: { label: string; value: any; }[] = [];
  const smallYears = oldYear;
  // 获取当前时间
  const date = new Date();
  let nowYears;
  if (newYear) {
    nowYears = newYear;
  } else {
    nowYears = date.getFullYear();
  }
  const Years = nowYears - smallYears;
  const arrYear = [];
  // eslint-disable-next-line no-plusplus
  for (let i = 0; i <= Years; i++) {
    // eslint-disable-next-line no-plusplus
    arrYear.push(nowYears--);
  }
  arrYear.forEach((item:any) => {
    // 下拉框的数组
    yearsToNow.push({
      label: `${item}年`,
      value: item,
    });
  });
  return yearsToNow;
};

export const getMonths = () => {
  const year = new Date().getFullYear();
  const month = new Date().getMonth() + 1;
  const yearsToNow: { label: string; value: any; }[] = [];
  if (year > 9) {
    // eslint-disable-next-line no-plusplus
    for (let i = 1; i < month + 1; i++) {
      yearsToNow.push({
        label: `${year}-${i}`,
        value: i,
      });
    }
  } else {
    // eslint-disable-next-line no-plusplus
    for (let i = 1; i < month + 1; i++) {
      yearsToNow.push({
        label: `${year}-0${i}`,
        value: i,
      });
    }
  }

  return yearsToNow;
};

export const mathjs = create(all, {
  number: 'BigNumber',
  precision: 20,
});

// 计算所占百分比
export const getPercent = (num:number, totalNum:number) => {
  if (!totalNum) return 0;
  const a = mathjs.divide(num, totalNum);
  // console.log(mathjs.multiply(a, 100), '---divide(num, totalNum)');
  return (mathjs.multiply((mathjs.divide(num, totalNum)), 100) || 0).toFixed(2);
};
