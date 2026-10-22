/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-07 17:54:44
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-13 16:09:52
 */
export const REGS = {
  passWord: /^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$).{8,20}$/, // 至少8位数字、字母、符号两种或以上组合
  // mobile: /^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/,
  email: /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
  notNegativeNumber: /^[+]{0,1}(\d+)$/, // 非负整数
  nonzeroInteger: /^-?[1-9]\d*$/, // 非零整数
  zeroInteger: /^\d+$/, // 大于等于0的整数
  numberAndFloat_2: /^(([1-9]{1}\d*)|(0{1}))(.\d{1,2})?$/, // 数字保留两位小数
  mobile: /^1[3-9]\d{9}$/,
  correctId: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
  phoAndMob: /^((0\d{2,3}-\d{7,8})|(1[3456789]\d{9}))$/, // 手机座机同时验证
  creditCode: /^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/, // 统一社会信用代码校验
  idCard: {
    test(val:string) {
      const reg = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
      const factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
      const parity = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
      const code = val.substring(17);
      if (reg.test(val)) {
        let sum = 0;
        const tempVal = val as any;
        // eslint-disable-next-line no-plusplus
        for (let i = 0; i < 17; i++) {
          sum += tempVal[i] * factor[i];
        }
        if (parity[sum % 11] === code.toUpperCase()) {
          return true;
        }
      }
      return false;
    },
  },
  position: /[\u4E00-\u9FA5\uF900-\uFA2D]/, // 职位
  appId: /^w[xw](?=.*\d)(?=.*[a-z])[\da-z]{16}$/, // 小程序/公众号/企业微信 appid
  // eslint-disable-next-line no-useless-escape
  linkUrl: /(ht|f)tp(s?)\:\/\/[0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*(:(0-9)*)*(\/?)([a-zA-Z0-9\-\.\?\,\'\/\\\+&amp;%$#_]*)?/, // 常见链接地址正则
  carNumber: /^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z](([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$/, // 常规/新能源车牌正则
  area: /^\d{1,9}(\.\d{1,2})?$/, // 面积|金额 9位整数，最多两位小数
  rate: /^\d{1,2}(\.\d{1,2})?$/, // 利率，最多两位整数，小数点后最多两位
  mounth: /^(1[0-2]|[1-9])$/, // 月份校验
  money: /^(([1-9]{1}\d{0,8})|(0{0}))(\.\d{0,2})?$|^0?\.\d{0,2}$/, // 9位整数，最多两位小数,非零
};
