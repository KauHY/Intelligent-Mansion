/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-07-07 12:30:59
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-20 16:28:49
 */
export const ENCRYPT_SLAT = 'biubiubui~~~'; // 登录加密盐

export const IMAGE_TYPE = ['bmp', 'jpg', 'png', 'tif', 'gif', 'svg', 'webp'];

export const rootMenu: any = {
  id: '0',
  routeName: 'index',
  type: '0',
  name: '智慧园区运营平台',
  parentId: -999,
  isMenu: 1,
};

export const fireExamineStatusNames = {
  // 消防审批状态
  0: '是',
  1: '否',
  2: '暂未审批',
};

export const parkTypeNames = {
  // 园区属性
  1: '社会大型园区（总建筑面积大于2万平方米）',
  2: '政府园区',
  3: '社区集体园区',
  4: '区属国企物业',
  5: '社会零散物业（总建筑面积小于2万平方米）',
};

export const parkFunctionNames = {
  // 功能用途
  1: '研发办公',
  2: '工业生产',
  3: '物流仓储',
  4: '商业配套',
  5: '其他',
};

export const propertyNames = {
  // 产权属性
  1: '无',
  2: '红本',
  3: '绿本',
  4: '其他',
};

// 体温是否正常
export const isOverTempNames = {
  0: '正常',
  1: '异常',
};
