/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2021-10-21 10:22:38
 * @LastEditors: Xucaizhi3
 * @LastEditTime: 2022-03-04 10:14:45
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
import {
  BusinessInfo, OperateBussInfo, BusinessInfoFollow, BusinessInRemind, BusinessInconvertToCustomer, TurnIntoBusiness,
} from '~models/wisdom-canvass-business';
import useForm from '~utils/useForm';

/**
 * @description: 新增商机
 * @param {*} param1
 * @return {*}
 * @author: GUOCHAO82
 */
export const useAddBusinessInfo = (() => {
  const createInstance = () => {
    const modelRef = reactive<BusinessInfo>({
      businessSource: null, // 商机来源：1商机录入，2入驻申请
      chanceName: '', // 商机名称
      chanceStage: null, // 商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭
      contactMail: '', // 联系人邮箱
      contactName: '', // 联系人
      contactPhone: '', // 联系人电话
      expectedPrice: null, // 期望价格（元/㎡/月）
      followEmployeeId: '',
      followEmployeeName: '', // 跟进人
      id: '',
      houseIds: [],
      latestFollowTime: '', // 最近实际跟进时间
      parkOrgName: '', // 关联园区
      parkOrgId: '',
      quantityNumber: null, // 需求数量
      remark: '', // 备注
      remindByDay: '', // 按天提醒
      remindByWeek: '', //  按周提醒
      remindContent: '', // 提醒内容
      remindFrequency: null, // 提醒频次: 1 天、2 周
      threadSource: null, // 线索来源：1老客户介绍，2政府资源，3公共资源，4第三方资源，5内部资源，6网络资源，7招商人员资源
      updateTime: '', // 修改时间
    });
    const rulesRef = reactive({
      chanceName: [{ required: true, message: '请输入商机名称' }],
      contactName: [{ required: true, message: '请输入联系人' }],
      contactPhone: [{
        validator: (rules:unknown, value:string) => {
          if (!REGS.mobile.test(value)) return Promise.reject('请输入正确的手机号码');
          return Promise.resolve();
        },
        required: true,
      }],
      contactMail: [{
        validator: (rules:unknown, value:string) => {
          if (value && !REGS.email.test(value)) return Promise.reject('请输入正确的联系人邮箱');
          return Promise.resolve();
        },
      }],
      quantityNumber: [{
        validator: (rules:unknown, value:string) => {
          if (value && !REGS.notNegativeNumber.test(value)) return Promise.reject('请输入需求数量，只能输入整数');
          return Promise.resolve();
        },
      }],
      // unitRent: [{ required: true, message: '请输入单位租金' }],
      expectedPrice: [{
        validator: (rules:unknown, value:string) => {
          if (value && !REGS.money.test(value)) return Promise.reject('请输入期望价格，只能数字，小数点前最多九位，最多两位小数');
          return Promise.resolve();
        },
      }],
      parkOrgId: [{ required: true, message: '请选择关联园区' }],
      chanceStage: [{ required: true, message: '请选择商机阶段' }],

    });
    const fill = (defaultModelRef:any) => {
      // debugger;
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
        // copyModelRef.houseIds = Object.keys(defaultModelRef.businessHouseVO);
      });
      // copyModelRef.
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

/**
 * @description: 跟进商机
 * @param {*} param1
 * @return {*}
 * @author: GUOCHAO82
 */
export const useOperateBussInfo = (() => { // 分配/领取/转移/释放商机
  const createInstance = () => {
    const modelRef = reactive<OperateBussInfo>({
      followEmployeeId: '',
      companyId: '',
      id: '',
      recordType: 2,
    });
    const rulesRef = reactive({
      followEmployeeId: [{
        validator: (rules:unknown, value:string) => {
          if (modelRef.recordType === 2 && !value) return Promise.reject('请选择跟进人');
          return Promise.resolve();
        },
        required: true,
      }],
      companyId: [{
        validator: (rules:unknown, value:string) => {
          if (modelRef.recordType === 3 && !value) return Promise.reject('请选择所属公司');
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
        // copyModelRef.houseIds = Object.keys(defaultModelRef.businessHouseVO);
      });
      console.log();
      // copyModelRef.
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

/**
 * @description: 分配/领取/转移/释放商机
 * @param {*} param1
 * @return {*}
 * @author: GUOCHAO82
 */
export const useFollow = (() => { // 分配/领取/转移/释放商机
  const createInstance = () => {
    const modelRef = reactive<BusinessInfoFollow>({
      id: '',
      chanceStage: null, // 商机阶段
      followContent: '', // 跟进内容
      followTime: '', // 跟进时间
      followType: null, // 跟进类型
    });
    const rulesRef = reactive({
      chanceStage: [{ required: true, message: '请选择商机阶段' }],
      followType: [{ required: true, message: '请选择跟进类型' }],
      followTime: [{ required: true, message: '请选择跟进时间' }],
      // followEmployeeId: [{
      //   validator: (rules:unknown, value:string) => {
      //     if (modelRef.recordType === 2 && !value) return Promise.reject('请选择跟进人');
      //     return Promise.resolve();
      //   },
      //   required: true,
      // }],
      // companyId: [{
      //   validator: (rules:unknown, value:string) => {
      //     if (modelRef.recordType === 3 && !value) return Promise.reject('请选择所属公司');
      //     return Promise.resolve();
      //   },
      //   required: true,
      // }],
    });
    const fill = (defaultModelRef:any) => {
      // debugger;
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      Object.entries(copyModelRef).forEach(([key]) => {
        if (defaultModelRef[key] || defaultModelRef[key] === 0) {
          copyModelRef[key] = defaultModelRef[key];
        }
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

/**
 * @description: 提醒
 * @param {*} param1
 * @return {*}
 * @author: GUOCHAO82
 */
export const useRemind = (() => { // 分配/领取/转移/释放商机
  const createInstance = () => {
    const modelRef = reactive<BusinessInRemind>({
      id: '',
      remindByDay: null, // 按天提醒:天数
      remindByWeek: [], // 按周提醒: 0 全选，1~~7 周一~~周日
      remindContent: '', // 提醒内容
      remindFrequency: null, // 提醒频次: 1 天、2 周
      remindTime: '', // 提醒时间: HH:mm
    });
    const rulesRef = reactive({
      remindFrequency: [{ required: true, message: '请选择提醒频次' }],
      remindTime: [{ required: true, message: '请选择提醒时间' }],
      remindContent: [{ required: true, message: '请输入提醒内容' }],
      remindByDay: [{
        validator: (rule:unknown, value:string) => {
          if (modelRef.remindFrequency === 1 && !value) return Promise.reject('请输入提醒天数');
          return Promise.resolve();
        },
        required: true,
      }],
      remindByWeek: [{
        validator: (rule:unknown, value:string) => {
          if (modelRef.remindFrequency === 2 && !value) return Promise.reject('请输入提醒天数');
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

export const useConvertToCustomer = (() => { // 分配/领取/转移/释放商机
  const createInstance = () => {
    const modelRef = reactive<TurnIntoBusiness>({
      businessId: '', // 商机id
      code: '', // 统一社会信用代码
      contacts: '', // 联系人
      contactsMail: '', // 联系人邮箱
      contactsTel: '', // 联系电话
      employeeId: '', // 跟进人
      name: '', // 客户名称
      parkOrgId: '',
      status: null, // 客户类型 0：物业公司，1：普通企业
      type: null, //
      employeeIdName: '', //
    });
    const rulesRef = reactive({
      name: [{ required: true, message: '请输入客户名称' }],
      parkOrdId: [{ required: true, message: '请选择关联园区' }],
      code: [{
        validator: (rules: unknown, value: string) => {
          if (!REGS.creditCode.test(value)) return Promise.reject('请输入18位统一社会信用代码');
          return Promise.resolve();
        },
        required: true,
      }],
      type: [{ required: true, message: '请选择客户类型' }],
      status: [{ required: true, message: '请选择客户状态' }],
      contacts: [{ required: true, message: '请输入联系人' }],
      contactsTel: [{
        required: true,
        validator: (rules: unknown, value: string) => {
          if (!REGS.mobile.test(value)) return Promise.reject('请输入正确的电话号码');
          return Promise.resolve();
        },
      }],
      // contactsMail: [{
      //   required: true,
      //   validator: (rules: unknown, value: string) => {
      //     if (!REGS.email.test(value)) return Promise.reject('请输入正确的邮箱');
      //     return Promise.resolve();
      //   },
      // }],
      employeeId: [{ required: true, message: '请选择跟进人' }],
    });
    const fill = (defaultModelRef:any) => {
      if (!defaultModelRef && !modelRef) return;
      const copyModelRef = modelRef as any;
      copyModelRef.businessId = defaultModelRef.id; // 商机id
      copyModelRef.contacts = defaultModelRef.contactName; // 联系人
      copyModelRef.contactsMail = defaultModelRef.contactMail; // 联系人邮箱
      copyModelRef.contactsTel = defaultModelRef.contactPhone; // 联系电话
      copyModelRef.employeeId = defaultModelRef.followEmployeeId; // 跟进人
      copyModelRef.name = defaultModelRef.chanceName;// 客户名称
      copyModelRef.parkOrgId = defaultModelRef.parkOrgId;
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
