/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-21 10:22:38
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-06-10 17:11:57
 */

/**
 * @description:
 * @param {string} formName
 * @return {*}
 * @author: GUOCHAO82
 */
import {
  onUnmounted, reactive,
} from 'vue';
import { REGS } from '@/constants/validate';
import { HousingBusinessInfo } from '~models/wisdom-canvass-business';
import useForm from '~utils/useForm';

export const useAddBusinessInfo = (() => {
  const createInstance = () => {
    const modelRef = reactive<HousingBusinessInfo>({
      address: {
        name: '',
        latitude: 0,
        longitude: 0,
      }, // 地址
      deadline: '', // 截至时间
      amount: 0, // 数量
      area: 0, // 面试
      companyId: '',
      houseInfoIds: [],
      content: '', // 内容详情
      createTime: '', // 创建时间
      effectType: null, // 招商时效类型,1:长期有效
      employeeId: '', // 咨询顾问（员工）id
      employeeName: '', // 咨询顾问（员工）
      employeePhone: '', // 咨询电话
      highlight: [], // 亮点描述
      id: '',
      parkOrgId: '', //
      parkOrgName: '', //  园区名称
      rentOrSale: [], // 租售属性，1出租，2出售，3出租出售
      status: null, // 发布状态,0，已下架，1已发布
      title: '', // 标题
      titlePicture: '', // 标题图片
    });
    const rulesRef = reactive({
      effectType: [{ required: true, message: '请选择招商类型' }],
      deadline: [{
        validator: (rules:unknown, value:string) => {
          if (!value && !modelRef.effectType) return Promise.reject('请选择截至时间');
          return Promise.resolve();
        },
        required: true,
      }],
      highlight: [{ required: true, message: '请选择亮点描述' }],
      title: [{ required: true, message: '请输入标题' }],
      rentOrSale: [{ required: true, message: '请选择租售属性' }],
      parkOrgId: [{ required: true, message: '请选择租售属性' }],
      // companyId: [{ required: true, message: '请选择所属公司' }],
      employeeId: [{ required: true, message: '请选择咨询顾问' }],
      houseInfoIds: [{ required: true, message: '请选择招商房屋' }],
      content: [{
        validator: (rules:unknown, value:string) => {
          if (!value) return Promise.reject('请输入内容详情');
          return Promise.resolve();
        // if(value.length>999) return Promise.reject('最多999')
        },
        required: true,
      }],
      employeePhone: [{
        validator: (rules:unknown, value:string) => {
          if (!REGS.mobile.test(value)) return Promise.reject('请输入正确的手机号码');
          return Promise.resolve();
        },
        required: true,
      }],
      titlePicture: [{ required: true, message: '请上传标题图片' }],
      'address.name': [{
        validator: (rules:unknown, value:string) => {
          if (!value) return Promise.reject('请输入详细地址');
          if (value.length > 200) return Promise.reject('最多只能输入200个字符');
          return Promise.resolve();
        },
        required: true,
      }],
    });
    const fill = (defaultModelRef:any) => {
      // debugger;
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
        copyModelRef.houseInfoIds = Object.keys(defaultModelRef.businessHouseVO);
      });
    };

    const { validateInfos, validate } = useForm(modelRef, rulesRef);
    return {
      modelRef,
      fill,
      validateInfos,
      validate,
      rulesRef,
    };
  };
    // 固定写法
  let userFormInstance:ReturnType<typeof createInstance>|null;
  return () => {
    if (!userFormInstance) {
      userFormInstance = createInstance();
      onUnmounted(() => {
        userFormInstance = null; // 卸载时回收
      });
    }
    return { ...userFormInstance };
  };
})();
