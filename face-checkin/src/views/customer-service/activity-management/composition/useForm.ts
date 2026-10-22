/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-09-29 17:35:34
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-02-23 15:37:20
 */
import {
  onUnmounted, reactive, Ref, ref,
} from 'vue';
import { REGS } from '@/constants/validate';
import useForm from '~utils/useForm';
import { ActivityInfo } from '~models/activity-management';
import { DictionaryMap } from '~composition/useHashMapDic';

import CarPass from '~models/car-pass';
import CompanyMangement from '~models/company-mangement';

/**
 * @description: 新增员工
 * @param {*}
 * @return {*}
 * @author: GUOCHAO82
 */
export const useActivityAdd = () => {
  const modelRef = reactive<ActivityInfo>({
    activityTimeEnd: '', // 活动结束时间
    activityTimeStart: '', // 活动结束时间
    confRegister: false, // 是否需要报名
    confRegisterDeadline: '', // 报名截止时间
    confRegisterLimit: false, // 是否限制报名人数
    confRegisterLimitCount: null, // 报名人数上限
    content: '', // 活动详情
    effectTime: '', // 生效时间
    id: '',
    name: '', // 活动名称
    listPicture: '', // 图片
    detailPicture: '',
    place: '', // 活动地点
    registeredCount: null, // 报名人数
    selfRegistered: false, // 自己是否报名
    status: null,
    parkOrgIds: [],
  });

  const rulesRef = reactive({
    name: [{ required: true, message: '请输入活动名称' }],
    place: [{ required: true, message: '请输入活动地点' }],
    parkOrgIds: [{ required: true, message: '请选择推送范围' }],
    activityTimeStart: [{ required: true, message: '请选择活动时间' }],
    effectTime: [{ required: true, message: '请选择生效时间' }],
    // confRegister: [{ required: true, message: '请选择报名截止时间' }],
    // confRegisterLimit: [{ required: true, message: '请选择报名截止时间' }],
    confRegisterDeadline: [{ required: true, message: '请选择报名截止时间' }],
    confRegisterLimitCount: [{
      validator: (rules:unknown, value:string) => {
        if (!/^\d{1,10}$/.test(value)) return Promise.reject('请输入报名人数上线，只能输入数字，最多10位');
        return Promise.resolve();
      },
      required: true,
    }],
    listPicture: [{ required: true, message: '请上传列表图片' }],
    detailPicture: [{ required: true, message: '请上传详情图片' }],
    content: [{ required: true, message: '请输入活动详情内容' }],

  });

  return {
    modelRef,
    rulesRef,
  };
};
