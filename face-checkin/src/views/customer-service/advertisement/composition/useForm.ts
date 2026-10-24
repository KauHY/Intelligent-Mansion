/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:43:20
 * @LastEditTime: 2022-02-22 16:12:36
 * @LastEditors: Xucaizhi3
 * @Description:
 */
/*
 * @Author: Xucaizhi3
 * @Date: 2022-01-13 10:36:36
 * @LastEditTime: 2022-01-13 10:38:09
 * @LastEditors: Xucaizhi3
 * @Description:
 */
import {
  onMounted, onUnmounted, reactive, computed,
} from 'vue';
import { AdvertListKind } from '~models/advertisement';

export const advertForm = (() => {
  const modelRef = reactive<AdvertListKind>({
    name: '',
    sort: null, // 排序：数字越小越靠前
    position: 1, // 广告位置 ：1 首页轮播图
    jumpType: null, // 跳转类型：0 无，1 URL，2 小程序AppID
    effectTimeEnd: '', // 生效开始时间
    effectTimeStart: '', // 生效开始时间
    createByName: '', // 发布人
    createTime: '', // 创建时间
    status: null, // 状态： 0 生效中, 1 待生效，2 已过期
    parkOrgIds: [], // 推送范围
    parkOrgNames: [], // 推送范围
    id: '',
    picture: '',
    jumpContent: '', // 跳转地址/需跳转的小程序AppID
  });
  const rulesRef = reactive({
    name: { required: true, message: '请输入广告名称' },
    parkOrgIds: { required: true, message: '请选择推送范围' },
    sort: { required: true, message: '请输入排序序号，数字越小越靠前', pattern: /^\d+$/ },
    jumpContent: { required: true, message: '请输入小程序AppID' },
    effectTimeStart: { required: true, message: '请选择生效时间' },
    picture: { required: true, message: '请选择图片' },
    jumpType: { required: true, message: '请选择跳转类型' },
  });
  return {
    modelRef,
    rulesRef,
  };
});
